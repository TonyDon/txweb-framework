/*
 * @(#)EntityPropColumnBean.java 2014-11-12
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.txweb.framework.dao.support;

import java.lang.reflect.Field;
import java.util.Map;


/**
 * <pre>
 * 表实体定义对象
 * @author tangxiaodong
 * 创建日期: 2014-11-12
 * </pre>
 */
public class EntityDefine {
    
    /**
     * 实体类class
     */
    private Class<?> entityClass;

    /**
     * 对应表名称
     */
    private String tableName;
    
    /**
     * 实体属性名与表字段名关系
     */
    private Map<String, String> propColumnMap;
    
    /**
     * 实体属性名与属性类关系
     */
    private Map<String, Field> propFieldMap;
    
    /**
     * 主键属性名
     */
    private String uniqueKeyPropName;
    
    /**
     * 主键ID列名
     */
    private String idColumnName;

    
    public Class<?> getEntityClass() {
        return entityClass;
    }

    
    public void setEntityClass(Class<?> entityClass) {
        this.entityClass = entityClass;
    }

    
    public String getTableName() {
        return tableName;
    }

    
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    
    public Map<String, String> getPropColumnMap() {
        return propColumnMap;
    }

    
    public void setPropColumnMap(Map<String, String> propColumnMap) {
        this.propColumnMap = propColumnMap;
    }

    
    public Map<String, Field> getPropFieldMap() {
        return propFieldMap;
    }

    
    public void setPropFieldMap(Map<String, Field> propFieldMap) {
        this.propFieldMap = propFieldMap;
    }


    
    public String getUniqueKeyPropName() {
        return uniqueKeyPropName;
    }


    
    public void setUniqueKeyPropName(String uniqueKeyPropName) {
        this.uniqueKeyPropName = uniqueKeyPropName;
    }


    
    public String getIdColumnName() {
        return idColumnName;
    }


    
    public void setIdColumnName(String idColumnName) {
        this.idColumnName = idColumnName;
    }

}
