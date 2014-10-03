/*
 * @(#)UserAccessActionInterceptor.java 2014-4-20
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.txweb.framework.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
        }
        response.setStatus(HTTP_STATUS_CODE.SC_FORBIDDEN);
        response.getWriter().print("deny access.");
        return false;
    }
}
