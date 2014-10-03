/*
 * @(#)WebAppExceptionResolver.java 2013-9-8
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.txweb.framework.resolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Component;
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
@Component
public class WebAppExceptionResolver implements HandlerExceptionResolver {


    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) {
        response.setStatus(HTTP_STATUS_CODE.SC_BZ_ERROR);
        ModelAndView model = new ModelAndView();
        model.addObject("exception", ExceptionUtils.getRootCauseMessage(ex));
        model.setViewName("exception/index");
        return model;
    }

}
