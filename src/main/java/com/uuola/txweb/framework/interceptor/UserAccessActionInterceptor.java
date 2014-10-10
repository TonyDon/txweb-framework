/*
 * @(#)UserAccessActionInterceptor.java 2014-4-20
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.txweb.framework.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.MethodParameter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.uuola.commons.constant.HTTP_STATUS_CODE;


/**
 * <pre>
 *
 * @author tangxiaodong
 * 创建日期: 2014-4-20
 * </pre>
 */
public class UserAccessActionInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
     // TODO Auto-generated method stub
        if(handler instanceof HandlerMethod){
            //TODO
            HandlerMethod method = (HandlerMethod)handler;
            System.out.println(method.getBeanType().getCanonicalName());
            System.out.println(method.getMethod().getName());
            MethodParameter[] params = method.getMethodParameters();
            System.out.println(params.length);
        }
        System.out.println(handler.getClass().getCanonicalName());
        
        response.setStatus(HTTP_STATUS_CODE.SC_FORBIDDEN);
        response.getWriter().print("deny access.");
        return false;
    }
}
