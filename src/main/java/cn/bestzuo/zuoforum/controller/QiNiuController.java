package cn.bestzuo.zuoforum.controller;

import com.qiniu.util.Auth;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 七牛云后端token的Controller
 */
@Controller
@RequestMapping("/api/open/qiNiu")
public class QiNiuController {
    // 访问密钥
    private static final String ACCESS_KEY = "";
    // 应用编码
    private static final String SECRET_KEY = "";
    // 访问空间
    private static final String BUCKET_NAME = "";

    /**
     * 后端获取七牛云存储桶的token
     * @param request
     * @param response
     */
    @RequestMapping("/getUpToken")
    public void getUpToken(HttpServletRequest request, HttpServletResponse response) {
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        String token = auth.uploadToken(BUCKET_NAME);
        //组装令牌返回前台
        String Uptoken = "{\"uptoken\":\"" + token + "\"}";
        try {
            response.setContentType("application/json;charset=utf-8");
            request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            PrintWriter out = response.getWriter();
            out.print(Uptoken);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
