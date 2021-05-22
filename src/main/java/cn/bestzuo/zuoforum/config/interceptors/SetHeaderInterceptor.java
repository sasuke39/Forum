package cn.bestzuo.zuoforum.config.interceptors;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

@Component
public class SetHeaderInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        Enumeration<String> headerNames = request.getHeaderNames();

//        Enumeration<String> headers = request.getHeaders(headerNames.toString());

        if (headerNames.hasMoreElements()){
            System.out.println(request.getHeader(headerNames.nextElement()));
        }

        return true;
    }
}
