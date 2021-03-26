package cn.bestzuo.zuoforum.admin.controller;

import cn.bestzuo.zuoforum.admin.common.LayuiTableResult;
import cn.bestzuo.zuoforum.admin.controller.VO.UserInfoVO;
import cn.bestzuo.zuoforum.admin.utils.IpUtil;
import cn.bestzuo.zuoforum.common.ForumResult;
import cn.bestzuo.zuoforum.pojo.UserInfo;
import cn.bestzuo.zuoforum.service.UserInfoService;
import cn.bestzuo.zuoforum.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.util.StringUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用户信息管理Controller
 */
@Controller
@Slf4j
public class AdminUserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private UserService userService;

    @Autowired
    TransactionTemplate transactionTemplate;

    @Autowired
    PlatformTransactionManager transactionManager;

    /**
     * 查询所有用户信息
     *
     * @return
     */
    @RequestMapping("/getUsers")
    @ResponseBody
    public LayuiTableResult queryAllUserInfo(@RequestParam("page") Integer page,
                                             @RequestParam("limit") Integer limit,
                                             @RequestParam(value = "key[username]", required = false) String username) {
        if (!StringUtils.isEmpty(username)) {
            //根据用户名或者邮箱查询
            String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern p = Pattern.compile(regEx1);
            Matcher m = p.matcher(username);
            if (m.matches()) {
                //表明搜索的是邮箱
                PageHelper.startPage(page, limit);
                List<UserInfo> userInfos = userInfoService.queryUserInfoByEmail(username);
                PageInfo<UserInfo> pageInfo = new PageInfo<>(userInfos);

                if (pageInfo.getList().size() == 0) {
                    return new LayuiTableResult(1, "查询成功", 0, null);
                }

                List<UserInfoVO> res = new ArrayList<>();
                for (UserInfo info : pageInfo.getList()) {
                    res.add(convertUserInfoToVO(info));
                }

                int total = new Long(pageInfo.getTotal()).intValue();
                return new LayuiTableResult(0, "查询成功", total, res);
            } else {
                //搜索的是用户名，只会存在一条用户名
                List<UserInfo> userInfos = new ArrayList<>();
                UserInfo info = userInfoService.getUserInfoByName(username);
                if (info == null) {
                    return new LayuiTableResult(1, "查询成功", 0, null);
                }
                userInfos.add(info);
                List<UserInfoVO> res = new ArrayList<>();
                for (UserInfo info1 : userInfos) {
                    res.add(convertUserInfoToVO(info1));
                }
                return new LayuiTableResult(0, "查询成功", 1, res);
            }
        } else {
            try {
                PageHelper.startPage(page, limit);
                List<UserInfo> userInfos = userInfoService.queryAllUserInfo();
                PageInfo<UserInfo> pageInfo = new PageInfo<>(userInfos);

                List<UserInfoVO> res = new ArrayList<>();
                for (UserInfo info : pageInfo.getList()) {
                    res.add(convertUserInfoToVO(info));
                }
                int total = new Long(pageInfo.getTotal()).intValue();
                return new LayuiTableResult(0, "查询成功", total, res);
            } catch (Exception e) {
                e.printStackTrace();
                return new LayuiTableResult(1, "查询失败", 0, null);
            }
        }
    }

    /**
     * 将用户信息转换成前端VO类
     * @param userInfo 用户信息
     * @return
     */
    private UserInfoVO convertUserInfoToVO(UserInfo userInfo) {
        UserInfoVO vo = new UserInfoVO();
        vo.setId(userInfo.getUId());
        vo.setUsername(userInfo.getUsername());
        if (userInfo.getSex().equals("0")) {
            vo.setSex("男");
        } else if (userInfo.getSex().equals("1")) {
            vo.setSex("女");
        } else {
            vo.setSex("保密");
        }
        vo.setEmail(userInfo.getEmail());
        vo.setBirthday(userInfo.getBirthday());
        vo.setArea(userInfo.getArea());
        vo.setSite(userInfo.getSite());
        vo.setGithub(userInfo.getGithub());
        vo.setWeibo(userInfo.getWeibo());
        vo.setUniversity(userInfo.getUniversity());
        vo.setMajority(userInfo.getMajority());
        vo.setCompany(userInfo.getCompany());
        vo.setJobTitle(userInfo.getJobTitle());

        if (userInfo.getIsopen()) {
            vo.setIsopen("是");
        } else {
            vo.setIsopen("否");
        }

        return vo;
    }

    /**
     * 根据用户ID删除用户信息
     * @param uid
     * @return
     */
    @RequestMapping("/deleteUser")
    @ResponseBody
    public ForumResult deleteUser(@RequestParam("uid") String uid) throws Exception {
        if (StringUtils.isEmpty(uid)) {
            return new ForumResult(400, "uid不能为空", null);
        }
        Integer id = Integer.valueOf(uid);
        String ip = IpUtil.getMyV4IP();
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
        log.info("管理员ip:"+ip+"删除用户id为:"+uid+"时间为:"+ time);
        boolean result = transactionTemplate.execute(status -> {
            try {
                // TODO something
                userService.deleteUserById(id);
                userInfoService.deleteUserInfoByUid(id);
            } catch (Exception e) {
                //TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
                status.setRollbackOnly();
                log.error(e.getMessage(), e);
                return false;
            }
            return true;
        });
        if (result){
            return new ForumResult(200, "删除成功", null);
        }else {
            return new ForumResult(500, "删除失败", null);
        }
    }
}
