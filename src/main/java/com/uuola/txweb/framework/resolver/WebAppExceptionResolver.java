/*
 * @(#)WebAppExceptionResolver.java 2013-9-8
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.txweb.framework.resolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.uuola.commons.constant.HTTP_STATUS_CODE;


/**
 * <pre>
 * 系统异常处理,action抛出的异常将被此类拦截
 * @author tangxiaodong
 * 创建日期: 2013-9-8
 * </pre>
 */
public class WebAppExceptionResolver implements HandlerExceptionResolver, Ordered  {


    private int order = Ordered.LOWEST_PRECEDENCE;
    
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) {
        response.setStatus(HTTP_STATUS_CODE.SC_BZ_ERROR);
        ModelAndView model = new ModelAndView();
        model.addObject("exception", ExceptionUtils.getFullStackTrace(ex));
        model.setViewName("exception/index");
        return model;
    }

    public void setOrder(int order) {
        this.order = order;
    }
    
    @Override
    public int getOrder() {
        return order;
    }

}
