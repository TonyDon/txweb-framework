/*
 * @(#)IConstant.java 2014-10-18
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.txweb.framework.action;


/**
 * <pre>
 * 常用ModelAndView 属性
 * @author tangxiaodong
 * 创建日期: 2014-10-18
 * </pre>
 */
public interface IConstant {

    /**
     * 存放 validate校验错误信息
     */
    public static final String VALID_ERRORS_ATTR = "validErrors";

    // 更新结果属性
    public static final String UPDATE_RESULT_ATTR = "result";
    
    // 存放异常信息
    public static final String EXCEPTION ="exception";
    
}
