/*
 * @(#)SqlBuilder.java 2013-8-4
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.txweb.framework.dao.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;

import com.uuola.commons.StringUtil;
import com.uuola.commons.constant.CST_CHAR;
import com.uuola.commons.exception.Assert;
import com.uuola.commons.reflect.ClassUtil;
import com.uuola.txweb.framework.dao.support.BaseEntity;


/**
 * <pre>
 * SQL 构建器
 * 可以手动输入表名，字段名列表，字段值列表构建语句，也可以通过实体对象构建
 * 用于插入和更新记录
 * @author tangxiaodong
 * 创建日期: 2013-8-4
 * </pre>
 */
public class SqlBuilder{
    
    private String tableName;
    
    private List<String> sqlColumns;
            
    private List<Object> sqlParams;
    
    private BaseEntity entity;
    
    private String uniqueKeyName;
    
    private Object uniqueKeyValue;
    
    public SqlBuilder(){
        
    }
    
    public SqlBuilder(BaseEntity entity){
        this.entity = entity;
    }
    
    /**
     * 通过实体类构建SQL所需参数
     */
    public SqlBuilder build() {
        Assert.notNull(this.entity, "Entity must not be null!");
        Class<?> clazz = getEntity().getClass();
        this.tableName = ClassUtil.getTableName(clazz);
        Collection<Field> fields = ClassUtil.getAllAccessibleFields(clazz, BaseEntity.class);
        sqlColumns = new ArrayList<String>();
        sqlParams = new ArrayList<Object>();
        for (Field f : fields) {
            Object val = ClassUtil.getFieldValue(f, getEntity());
            Column col = f.getAnnotation(Column.class);
            if (null != val && null != col) {
                String columnName = col.name();
                // 匿名注解Column.name 字段名不能为空或空串
                if (StringUtil.isEmpty(columnName)) {
                    throw new IllegalArgumentException(clazz.getCanonicalName() + "." + f.getName()
                            + "[Annotation Column.name() must not be null!]");
                }
                sqlColumns.add(columnName);
                sqlParams.add(val);
            }
            if ((null == uniqueKeyName) && (null != f.getAnnotation(Id.class))) {
                uniqueKeyName = f.getName();
                uniqueKeyValue = val;
            }
        }
        return this;
    }
    
    /**
     * 构建SQL插入预处理语句
     * @return
     */
    public String getInsertSql(){
        Assert.notNull(this.sqlColumns, "sqlColumns must not be null!");
        StringBuilder sql = new StringBuilder("INSERT INTO ");
        sql.append(this.getTableName()).append("(");
        sql.append(StringUtil.join(this.sqlColumns,CST_CHAR.CHAR_COMMA));
        sql.append(") VALUES (").append(SqlBuilder.getPlaceholder(this.sqlColumns.size())).append(")");
        return sql.toString();
    }
    
    /**
     * 构建SQL更新预处理语句
     * @return
     */
    public String getUpdateSql(){
        Assert.notNull(this.uniqueKeyName, "uniqueKeyName must not be null!");
        Assert.notNull(this.uniqueKeyValue, "uniqueKeyValue must not be null!");
        StringBuilder sql = new StringBuilder("UPDATE ");
        sql.append(this.getTableName()).append(" SET ");
        for(String col : sqlColumns){
            sql.append(col).append("=?,");
        }
        sql.deleteCharAt(sql.length()-1);
        sql.append(" WHERE ").append(this.uniqueKeyName).append("=?");
        sqlParams.add(this.uniqueKeyValue);// append where condition to last
        return sql.toString();
    }

    /**
     * 构建IN条件参数占位符如 3个参数为 ?,?,?
     * eg: Long[] inIds = {111L,222L,333L}; length = 3
     * return ?,?,?
     * @param argNum
     * @return
     */
    public static String getPlaceholder(int argNum) {
        StringBuilder buffer = new StringBuilder();
        for(int i=0; i<argNum; i++){
            buffer
            .append(CST_CHAR.CHAR_QUESTION)
            .append(CST_CHAR.CHAR_COMMA);
        }
        return buffer.deleteCharAt(buffer.length()-1).toString();
    }
    
    /**
     * 将数组对象Object中的集合对象拆解成单个元素组合到新的对象数组中，新对象数组不应有集合对象。
     * eg: args ={ "a", 123, array{1,2,3} , 1, "t" }
     * return : array{"a", 123, 1, 2, 3, 1, "t"}
     * Long[] inIds = {111L,222L,333L};
     * DAO.update("update table set flag=? where id in (?,?,?)", SqlBuilder.getArgsList(1, inIds));
     * @param args
     * @return
     */
    public static List<Object> getArgsList(Object... args) {
        List<Object> results = new ArrayList<Object>(args.length);
        for(Object o: args) {
            if(o instanceof Collection) {
                Collection<?> c = (Collection<?>)o;
                for(Object item: c) {
                    results.add(item);
                }
            } else if(o.getClass().isArray()) {
                Object[] c = (Object[])o;
                for(Object item: c) {
                    results.add(item);
                }
            } else {
                results.add(o);
            }
        }
        return results;
    }
    
    public static Object[] getArgsArray(Object... args){
        return getArgsList(args).toArray();
    }

    
    public String getTableName() {
        return tableName;
    }

    
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    
    public List<Object> getSqlParams() {
        return sqlParams;
    }

    
    public void setSqlParams(List<Object> sqlParams) {
        this.sqlParams = sqlParams;
    }

    
    public BaseEntity getEntity() {
        return entity;
    }

    
    public void setEntity(BaseEntity entity) {
        this.entity = entity;
    }

    
    public String getUniqueKeyName() {
        return uniqueKeyName;
    }

    
    public void setUniqueKeyName(String uniqueKeyName) {
        this.uniqueKeyName = uniqueKeyName;
    }

    
    public Object getUniqueKeyValue() {
        return uniqueKeyValue;
    }

    
    public void setUniqueKeyValue(Object uniqueKeyValue) {
        this.uniqueKeyValue = uniqueKeyValue;
    }

    
    public List<String> getSqlColumns() {
        return sqlColumns;
    }

    
    public void setSqlColumns(List<String> sqlColumns) {
        this.sqlColumns = sqlColumns;
    }
    
    
}
