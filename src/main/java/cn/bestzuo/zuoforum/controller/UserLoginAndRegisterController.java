package cn.bestzuo.zuoforum.controller;

import cn.bestzuo.zuoforum.pojo.EmailInfo;
import cn.bestzuo.zuoforum.pojo.User;
import cn.bestzuo.zuoforum.pojo.UserInfo;
import cn.bestzuo.zuoforum.pojo.UserToken;
import cn.bestzuo.zuoforum.pojo.vo.UserVO;
import cn.bestzuo.zuoforum.service.EmailService;
import cn.bestzuo.zuoforum.service.UserInfoService;
import cn.bestzuo.zuoforum.service.UserService;
import cn.bestzuo.zuoforum.common.ForumResult;
import cn.bestzuo.zuoforum.service.UserTokenService;
import cn.bestzuo.zuoforum.util.HS256;
import cn.bestzuo.zuoforum.util.MD5Password;
import cn.bestzuo.zuoforum.util.MyMD5;
import cn.bestzuo.zuoforum.util.VerifyCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 用户登录注册Controller
 */
@Controller
@Slf4j
public class UserLoginAndRegisterController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private UserTokenService userTokenService;

    /**
     * 跳转到注册页面
     *
     * @return
     */
    @GetMapping("/register")
    public String register() {
        return "register";
    }

    /**
     * 跳转到登录页面
     *
     * @return
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * 注册页面获取验证码图片
     *
     * @param response 响应
     * @param request  请求
     */
    @GetMapping("/getVerifyCode")
    public void getVerificationCode(HttpServletResponse response, HttpServletRequest request) {
        try {
            int width = 200;
            int height = 60;
            BufferedImage verifyImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            //生成对应宽高的初始图片
            String randomText = VerifyCode.drawRandomText(width, height, verifyImg);
            //单独的一个类方法，出于代码复用考虑，进行了封装。
            //功能是生成验证码字符并加上噪点，干扰线，返回值为验证码字符
            request.getSession().setAttribute("verifyCode", randomText);
            response.setContentType("image/png");//必须设置响应内容类型为图片，否则前台不识别
            OutputStream os = response.getOutputStream(); //获取文件输出流
            ImageIO.write(verifyImg, "png", os);//输出图片流
            os.flush();
            os.close();//关闭流
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询该用户名是否已被注册
     *
     * @param username
     * @return
     */
    @RequestMapping("/getUserByName")
    @ResponseBody
    public ForumResult getUserByName(@RequestParam("username") String username) {
        //后端校验判空
        if (StringUtils.isEmpty(username)) {
            return new ForumResult(400, "用户名不能为空", null);
        }

        return userService.getUserByName(username) == null ? ForumResult.ok() : new ForumResult(500, "用户名已被注册", null);
    }

    /**
     * 用户注册按钮
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public ForumResult register(@RequestParam("username") String username,
                                @RequestParam("password") String password,
                                @RequestParam("verifyCode") String verifyCode,
                                HttpServletRequest request) {

        //后端数据校验
        if (StringUtils.isEmpty(username))
            return new ForumResult(400, "用户名不能为空", null);
        if(username.length() < 2 || username.length() > 12)
            return new ForumResult(400,"用户名必须在2-12个字符之内",null);
        if (StringUtils.isEmpty(password))
            return new ForumResult(400, "密码不能为空", null);
        if (StringUtils.isEmpty(verifyCode))
            return new ForumResult(400, "验证码不能为空", null);

        //后端校验该用户名是否已经注册
        if(userService.getUserByName(username) != null)
            return new ForumResult(400,"该用户名已被注册",null);

        //后端校验用户名格式是否正确，要求：仅包含中文、英文字母、数字，且数字不能在最前面
        String regex = "^[a-z0-9A-Z\u4e00-\u9fa5]+$";
        if(username.matches(regex) && !Character.isDigit(username.toCharArray()[0])){
            //放行，可以注册
            String code = (String) request.getSession().getAttribute("verifyCode");
            //验证码校验
            if (code.compareToIgnoreCase(verifyCode) != 0)
                return new ForumResult(405, "验证码错误", null);

            //插入用户信息表
            return userService.insertUser(username, password) > 0 ? ForumResult.ok() : new ForumResult(500, "注册失败，请稍后再试", username);
        }
        return new ForumResult(400,"用户名必须由中文、英文字母或者数字组成，且数字不能在最前面",null);
    }

    /**
     * 登录请求
     *
     * @param username 用户名
     * @param password 密码
     * @return json
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public ForumResult loginUser(@RequestParam("username") String username,
                                 @RequestParam("password") String password,
                                 HttpServletRequest request) {

        ForumResult forumResult =new ForumResult();
        //后端校验用户名
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return new ForumResult(500, "用户名或密码不能为空", null);
        }

        //判断用户名格式
        User user;
        String regex = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

        //先判断是否为邮箱格式，如果不为邮箱格式，那么就是使用用户名登录的
        if (username.matches(regex)) {
            //使用邮箱登录的，先验证邮箱是否验证过
            EmailInfo emailInfo = emailService.selectEmailInfoByEmail(username);

            //邮箱是否存在
            if (emailInfo == null) return new ForumResult(400, "邮箱不存在", null);

            //邮箱是否已经被激活
            if (emailInfo.getCheck() == 0) return new ForumResult(500, "邮箱未验证", null);

            //再根据邮箱查询密码
            user = userService.getUserByName(emailInfo.getUsername());
        } else {
            user = userService.getUserByName(username);
            if (user == null) return new ForumResult(500, "用户不存在", null);
        }

        //后端校验密码
        if (!userService.getUserByUserId(user.getUid()).getPassword().equals(MyMD5.string2Md5(password))) {
            return new ForumResult(500, "密码错误", null);
        }
        String token ;
        //用户没有token时生成token
        UserToken tokenServiceToken = userTokenService.getToken(user.getUid());
        if (ObjectUtils.isEmpty(tokenServiceToken)) {
             token=HS256.returnSign(user.getUid().toString());
            if (token.isEmpty()){
                forumResult.setMsg("生成token失败");
                forumResult.setStatus(500);
                return forumResult;
            }
            UserToken userToken = new UserToken();
            userToken.setToken(token);
            userToken.setUid(user.getUid());
            userTokenService.insertUserToken(userToken);
        }else {
            token=tokenServiceToken.getToken();
        }
        //将用户信息存在session中
        HttpSession session = request.getSession();
        session.setAttribute("username", user.getUsername());
        session.setAttribute("uid", user.getUid());

        //将所有页面需要的用户信息存储到session中
        UserInfo userInfo = userInfoService.selectUserInfoByUid(user.getUid());
        UserVO userVO = new UserVO();
        userVO.setUid(user.getUid());
        userVO.setUsername(user.getUsername());
        userVO.setAvatar(userInfo.getAvatar());

        //将所有需要用到的用户信息放到session中
        //储存UserToken
        log.info("用户:"+username+"登陆，新增token:"+token);
        session.setAttribute("token",token);

        forumResult.setData(token);
        forumResult.setStatus(200);
        return forumResult;
    }

    /**
     * 使用token登陆
     */
    @RequestMapping(value = "/loginByToken",method = RequestMethod.POST)
    @ResponseBody
    public ForumResult loginUserByToken(@RequestParam("token") String token,HttpServletRequest request){
        ForumResult forumResult =new ForumResult();
        log.info("来自客户端的token:"+token);
        String  tokenOnSession = (String) request.getSession().getAttribute("token");
        log.info("服务端与用户对应的token:"+tokenOnSession);
        if (token.equals(tokenOnSession)){
            Integer uidByToken = userTokenService.getUidByToken(token);
            UserInfo userInfo = userInfoService.selectUserInfoByUid(uidByToken);
            request.getSession().setAttribute("username", userInfo.getUsername());
            request.getSession().setAttribute("uid", userInfo.getUId());

            //将所有页面需要的用户信息存储到session中
            UserVO userVO = new UserVO();
            userVO.setUid(userInfo.getUId());
            userVO.setUsername(userInfo.getUsername());
            userVO.setAvatar(userInfo.getAvatar());

            forumResult.setData(userVO);
            forumResult.setStatus(200);
            return forumResult;
        }

        forumResult.setStatus(500);
        forumResult.setMsg("登录失败");

        return forumResult;

    }

    /**
     * 注销
     *
     * @param username 用户名
     * @param request  请求
     * @return 页面
     */
    @GetMapping("/logout")
    public String logout(@RequestParam("username") String username, HttpServletRequest request) {
        String name = (String) request.getSession().getAttribute("username");
        if (name != null) {
            request.getSession().setAttribute("username", null);
            request.getSession().removeAttribute("username");
            request.getSession().invalidate();
        }
        return "redirect:/";
    }
}
