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
public class SqlPropertyValue {

    // 属性名
    private String propertyName;
    
    //属性值
    private Object value;
    
    // 判断条件, 默认: EQUAL 属性名=查询值
    private SqlConditionDef filterCondition;
    
    // 多属性关联条件,默认 null, 仅限于: and , or
    private SqlConditionDef relationCondition;
    
    public SqlPropertyValue(String name, Object value){
        this.propertyName = name;
        this.value = value;
        this.filterCondition = SqlConditionDef.EQUAL;
    }
    
    public SqlPropertyValue(String name, Object value, SqlConditionDef filterCondition){
        this.propertyName = name;
        this.value = value;
        this.filterCondition = filterCondition;
    }
    
    public SqlPropertyValue(String name, Object value, SqlConditionDef filterCondition, SqlConditionDef relationCondition){
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

    
    public SqlConditionDef getFilterCondition() {
        return filterCondition;
    }

    
    public void setFilterCondition(SqlConditionDef filterCondition) {
        this.filterCondition = filterCondition;
    }

    
    public SqlConditionDef getRelationCondition() {
        return relationCondition;
    }

    
    public void setRelationCondition(SqlConditionDef relationCondition) {
        this.relationCondition = relationCondition;
    }

}
