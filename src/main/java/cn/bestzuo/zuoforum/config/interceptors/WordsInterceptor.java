package cn.bestzuo.zuoforum.config.interceptors;

import cn.bestzuo.zuoforum.util.WordsFilterUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.util.*;

@Component
@Slf4j
public class WordsInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()){
            String paramName = parameterNames.nextElement();
            String paramValue =request.getParameter(paramName);
            String filterParamValue = WordsFilterUtils.Html2Text(paramValue);
            log.info("请求参数过滤前{Name:value}---{"+paramName+":"+paramValue+"}---{过滤后："+filterParamValue+"}");
        }

//        Iterator<String> iterator = attributeNames.asIterator();
//        if (iterator.hasNext()){
//            String paramName = iterator.next();
//            Object paramValue = request.getAttribute(paramName);
//            String filterParamValue = WordsFilterUtils.Html2Text(paramValue.toString());
//            log.info("请求参数过滤Name:"+paramName+"过滤前：value:"+paramValue.toString()+"过滤后：value"+filterParamValue);
//            request.setAttribute(filterParamValue,paramName);
//        }

        return true;

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }


}
