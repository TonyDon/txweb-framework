/*
 * @(#)SqlPropertyValue.java 2014-11-9
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.txweb.framework.dao.support;


/**
 * <pre>
 * 查询属性名称，值对象
 * @author tangxiaodong
 * 创建日期: 2014-11-9
 * </pre>
 */
public class SqlPropValue {

    // 属性名
    private String propertyName;
    
    //属性值
    private Object value;
    
    // 判断条件, 默认: EQUAL 属性名=查询值
    private SqlCondDef filterCondition;
    
    // 多属性关联条件,默认 null, 仅限于: and , or
    private SqlCondDef relationCondition;
    
    public SqlPropValue(String name, Object value){
        this.propertyName = name;
        this.value = value;
        this.filterCondition = SqlCondDef.EQUAL;
    }
    
    public SqlPropValue(String name, Object value, SqlCondDef filterCondition){
        this.propertyName = name;
        this.value = value;
        this.filterCondition = filterCondition;
    }
    
    public SqlPropValue(String name, Object value, SqlCondDef filterCondition, SqlCondDef relationCondition){
        this.propertyName = name;
        this.value = value;
        this.filterCondition = filterCondition;
        this.relationCondition = relationCondition;
    }


    
    public String getPropertyName() {
        return propertyName;
    }

    
    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    
    public Object getValue() {
        return value;
    }

    
    public void setValue(Object value) {
        this.value = value;
    }

    
    public SqlCondDef getFilterCondition() {
        return filterCondition;
    }

    
    public SqlPropValue setFilterCondition(SqlCondDef filterCondition) {
        this.filterCondition = filterCondition;
        return this;
    }

    
    public SqlCondDef getRelationCondition() {
        return relationCondition;
    }

    
    public SqlPropValue setRelationCondition(SqlCondDef relationCondition) {
        this.relationCondition = relationCondition;
        return this;
    }

}
