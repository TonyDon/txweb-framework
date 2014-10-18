/*
 * @(#)WebAppExceptionResolver.java 2013-9-8
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.txweb.framework.resolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.uuola.commons.StringUtil;
import com.uuola.commons.constant.HTTP_STATUS_CODE;
import com.uuola.commons.exception.BusinessException;
import com.uuola.commons.exception.BusinessExceptionMessageProvider;
import com.uuola.txweb.framework.action.IConstant;


/**
 * <pre>
 * 系统异常处理,action抛出的异常将被此类拦截
 * @author tangxiaodong
 * 创建日期: 2013-9-8
 * </pre>
 */
public class WebAppExceptionResolver implements HandlerExceptionResolver, Ordered  {
    
    private Logger logger = LoggerFactory.getLogger(getClass());
    
    /**
     * 是否将异常写入日志
     */
    private boolean useLogger = false;
    
    /**
     * 设置异常页视图名称
     */
    private String exceptionViewName ;

    /**
     * 多个resolver配置时当前解析器的排序位置，默认最低位
     */
    private int order = Ordered.LOWEST_PRECEDENCE;
    
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) {
        response.setStatus(HTTP_STATUS_CODE.SC_BZ_ERROR);
        ModelAndView model = new ModelAndView();
        exceptionLogHandle(ex);
        model.addObject(IConstant.EXCEPTION, exceptionMessageResolve(ex));
        model.setViewName(getExceptionViewName());
        return model;
    }


    private String exceptionMessageResolve(Exception ex) {
        if(ex instanceof BusinessException){
            BusinessException bizEx = (BusinessException)ex;
           return ex.getMessage() +" : "+ BusinessExceptionMessageProvider.getMessage(bizEx);
        }
        return ExceptionUtils.getRootCauseMessage(ex);
    }


    private void exceptionLogHandle(Exception ex) {
        if(isUseLogger() && logger.isErrorEnabled()){
            logger.error("", ex);
        }
    }

    public void setOrder(int order) {
        this.order = order;
    }
    
    @Override
    public int getOrder() {
        return order;
    }

    
    public boolean isUseLogger() {
        return useLogger;
    }

    
    public void setUseLogger(boolean useLogger) {
        this.useLogger = useLogger;
    }


    
    public String getExceptionViewName() {
        return StringUtil.isEmpty(exceptionViewName) ? "exception/index" : exceptionViewName;
    }


    
    public void setExceptionViewName(String exceptionViewName) {
        this.exceptionViewName = exceptionViewName;
    }

}
