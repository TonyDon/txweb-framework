/*
 * @(#)ContextUtil.java 2013-11-17
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.txweb.framework.utils;

import java.io.FileNotFoundException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.WebUtils;


/**
 * <pre>
 * @author tangxiaodong
 * 创建日期: 2013-11-17
 * </pre>
 */
public class ContextUtil {

    /**
     * 获取Spring WebApplicationContext
     * @return
     */
    public static WebApplicationContext getWebApplicationContext(){
        return ContextLoader.getCurrentWebApplicationContext();
    }
    
    /**
     * 获取web server ServletContext
     * @return
     */
    public static ServletContext getServletContext(){
        return ContextLoader.getCurrentWebApplicationContext().getServletContext();
    }
    
    /**
     * 获取 Spring Bean
     * @param <T>
     */
    public static <T> T getBean(String name, Class<T> requiredType){
        return getWebApplicationContext().getBean(name, requiredType);
    }
    
    public static <T> T getBean(Class<T> requiredType){
        return getWebApplicationContext().getBean(requiredType);
    }
    
    /**
     * 得到绝对路径
     * @param path
     * @return
     * @throws FileNotFoundException
     */
    public static String getRealPath(String path) throws FileNotFoundException{
        return WebUtils.getRealPath(getServletContext(), path);
    }
    
    /**
     * 得到WEBAPP上下文路径
     * @return
     */
    public static String getContextPath(){
        return ContextLoader.getCurrentWebApplicationContext().getServletContext().getContextPath();
    }
    
    /**
     * 得到HttpServletRequest
     * org.springframework.web.context.request.RequestContextListener  
     * @return
     */
    public static HttpServletRequest getHttpServletRequest(){
        return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();  
    }
}
