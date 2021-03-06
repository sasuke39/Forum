package cn.bestzuo.zuoforum.config.interceptors;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;

/**
 * 登录拦截器
 *
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    //这个方法是在访问接口之前执行的，我们只需要在这里写验证登陆状态的业务逻辑，就可以在用户调用指定接口之前验证登陆状态了
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException, IOException {
//        HttpSession session = request.getSession();
//        String token = (String) session.getAttribute("token");
////        return username != null;
//        if(token == null){
//            request.getRequestDispatcher("/login").forward(request, response);
//            return false;
//        }
        Enumeration<String> headerNames = request.getHeaderNames();

//        Enumeration<String> headers = request.getHeaders(headerNames.toString());

        if (headerNames.hasMoreElements()){
            System.out.println(request.getHeader(headerNames.nextElement()));
        }

        return true;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) {

    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) {

    }
}
