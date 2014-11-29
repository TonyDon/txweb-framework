/*
 * @(#)SqlConditionDef.java 2014-11-9
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.txweb.framework.dao.support;


/**
 * <pre>
 * sql 查询条件标识枚举
 * @author tangxiaodong
 * 创建日期: 2014-11-9
 * </pre>
 */
public enum SqlCondDef {

    /**
     * 查询条件等于参数值
     */
    EQUAL("="),
    /**
     * 查询条件不等于参数值
     */
    NOT_EQUAL("<>"),
    /**
     * 查询条件小于参数值
     */
    LESS_THAN("<"),
    /**
     * 查询条件大于等于参数值
     */
    NOT_LESS_THAN(">="),
    /**
     * 查询条件大于参数值
     */
    GREAT_THAN(">"),
    /**
     * 查询条件小于等于参数值
     */
    NOT_GREAT_THAN("<="),
    /**
     * 查询条件类似于参数值 %parm%
     */
    LIKE(" like "),

    /**
     * 忽略查询条件
     */
    IGNORE(""),
    
    /**
     * 通过IN操作进行查询,IN内部可以使用子查询
     */
    IN(" in "),
    /**
     * 通过not in进行操作(传入的参数应该为子查询)
     */
    NOT_IN(" not in "),
    /**
     * 查询条件为null
     */
    IS_NULL(" is null "),
    /**
     * 查询条件不为null
     */
    IS_NOT_NULL(" is not null "),
    
    AND(" and "),
    
    OR(" or ");
    
    private String sqlTag;
    
    SqlCondDef(String sqlTag){
        this.sqlTag = sqlTag;
    }
    
    public String getTag(){
        return this.sqlTag;
    }
}
