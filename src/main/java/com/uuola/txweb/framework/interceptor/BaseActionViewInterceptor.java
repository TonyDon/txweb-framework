/*
 * @(#)BaseActionViewInterceptor.java 2015年11月3日
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.txweb.framework.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.uuola.txweb.framework.action.BaseAction;


/**
 * <pre>
 * BaseAction 视图拦截
 * @author tangxiaodong
 * 创建日期: 2015年11月3日
 * </pre>
 */
public class BaseActionViewInterceptor extends HandlerInterceptorAdapter {

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {

        if (null != modelAndView && !modelAndView.hasView() && handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler;
            Object bean = hm.getBean();
            // 针对继承BaseAction 自动设置视图
            if (bean instanceof BaseAction) {
                ((BaseAction)bean).assignViewName(modelAndView, hm.getMethod().getName());
            }
        }

        super.postHandle(request, response, handler, modelAndView);
    }

    
}
