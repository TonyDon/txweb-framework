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
    public static final String ERRORS_ATTR = "errors";

    // 更新结果属性
    public static final String UPDATE_RESULT_ATTR = "result";
    
    // 页面使用PageDTO的属性名称 如: page.datas , page.total
    public static final String QUERY_PAGE_ATTR = "page";
    
    // 存放异常信息
    public static final String EXCEPTION ="exception";
    
}
