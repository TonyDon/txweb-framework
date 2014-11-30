/*
 * @(#)SqlBuilder.java 2013-8-4
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.txweb.framework.dao.support;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.uuola.commons.ObjectUtil;
import com.uuola.commons.StringUtil;
import com.uuola.commons.constant.CST_CHAR;
import com.uuola.commons.exception.Assert;
import com.uuola.commons.reflect.FieldUtil;


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
    
    /**
     * 唯一字段名
     */
    private String uniqueColName;
    
    /**
     * 唯一字段值
     */
    private Object uniqueColValue;
    
    private Class<? extends BaseEntity> entityClass;
    
    private String whereCondition;
    
    private Object[] whereArgs;
    
    public SqlBuilder(){
        
    }
    
    public SqlBuilder(Class<? extends BaseEntity> entityClass){
        this.entityClass = entityClass;
    }
    
    public SqlBuilder(BaseEntity entity){
        this.entity = entity;
    }
    
    /**
     * 通过实体类构建SQL所需参数<br/>
     * 主键与非主键字段分别存储
     */
    public SqlBuilder build() {
        Assert.notNull(this.entity, "Entity must not be null!");
        this.entityClass = (Class<? extends BaseEntity>)entity.getClass();
        EntityDefBean entityDef = EntityDefManager.getDef(this.entityClass);
        this.tableName = entityDef.getTableName();
        Map<String, Field> propNameFieldMap = entityDef.getPropFieldMap();
        Map<String, String> propNameColumnMap = entityDef.getPropColumnMap();
        sqlColumns = new ArrayList<String>();
        sqlParams = new ArrayList<Object>();
        Object uniquePropVal = null;
        String uniquePropName = entityDef.getUniqueKeyPropName();
        for (Map.Entry<String, Field> pf : propNameFieldMap.entrySet()) {
            String propName = pf.getKey();
            Field propField = pf.getValue();
            Object val = FieldUtil.getValue(propField, entity);
            String col = propNameColumnMap.get(propName);
            if (propName.equals(uniquePropName)) {
                uniquePropVal = val;
            } else if (null != val && null != col) {
                sqlColumns.add(col);
                sqlParams.add(val);
            }
        }
        if (null == uniqueColName && null != uniquePropVal) {
            uniqueColName = propNameColumnMap.get(uniquePropName);
            uniqueColValue = uniquePropVal;
        }
        return this;
    }
    
    /**
     * 构建SQL插入预处理语句，可以结合build方法使用，也可单独调用
     * @return
     */
    public String getInsertSql() {
        Assert.notNull(this.sqlColumns, "sqlColumns must not be null!");
        if (null != this.uniqueColName && null != this.uniqueColValue) {
            sqlColumns.add(this.uniqueColName);
            sqlParams.add(this.uniqueColValue);
        }
        StringBuilder sql = new StringBuilder("INSERT INTO ");
        sql.append(this.getTableName()).append("(");
        sql.append(StringUtil.join(this.sqlColumns, CST_CHAR.CHAR_COMMA));
        sql.append(") VALUES (").append(StringUtil.getPlaceholder(this.sqlColumns.size())).append(")");
        return sql.toString();
    }
    
    /**
     * 构建SQL更新预处理语句，结合build方法使用<br/>
     * 如果不通过build方式，需要手动设置唯一列和值
     * @return
     */
    public String getUpdateSql() {
        Assert.notNull(this.uniqueColName, "uniqueColName must not be null!");
        Assert.notNull(this.uniqueColValue, "uniqueColValue must not be null!");
        StringBuilder sql = new StringBuilder("UPDATE ");
        sql.append(this.getTableName()).append(" SET ");
        int colCount = sqlColumns.size();
        int lastColIndex = colCount - 1;
        for (int k = 0; k < lastColIndex; k++) {
            sql.append(sqlColumns.get(k)).append("=?").append(",");
        }
        sql.append(sqlColumns.get(lastColIndex)).append("=?");
        sql.append(" WHERE ").append(this.uniqueColName).append("=?");
        sqlParams.add(this.uniqueColValue);// append where condition to last
        return sql.toString();
    }
    
    /**
     * 构建SQL删除预处理语句，结合build方法使用
     * @return
     */
    public String getDeleteSql() {
        StringBuilder sql = new StringBuilder("DELETE FROM ");
        sql.append(this.getTableName()).append(" WHERE ");
        if (StringUtil.isNotEmpty(uniqueColName) && null != uniqueColValue) {
            // 添加主键列名 和值
            sqlColumns.clear();
            sqlParams.clear();
            sqlColumns.add(uniqueColName);
            sqlParams.add(uniqueColValue);
        }
        Assert.notEmpty(this.sqlColumns);
        int count = this.sqlColumns.size();
        int lastColIndex = count - 1;
        for (int k = 0; k < lastColIndex; k++) {
            sql.append(this.sqlColumns.get(k)).append("=? and ");
        }
        sql.append(this.sqlColumns.get(lastColIndex)).append("=? ");
        return sql.toString();
    }

    /**
     * 构建where 条件
     * @param findCondits
     * @return
     */
    public SqlBuilder where(SqlPropValue... findCondits) {
        Object[] valueArgs = null;
        int conditsCount = findCondits.length;
        Class<? extends BaseEntity> clazz = null == entity ? this.entityClass : (Class<? extends BaseEntity>) entity
                .getClass();
        Map<String, String> propColumnMap = EntityDefManager.getDef(clazz).getPropColumnMap();
        // where ...
        StringBuilder sql = new StringBuilder();
        if (1 == conditsCount) {
            SqlPropValue pv = findCondits[0];
            sql.append(SqlBuilder.makeProperySqlFragment(clazz, propColumnMap, pv));
            valueArgs = ObjectUtil.getArgsArray(pv.getValue());

        } else {
            Object[] params = new Object[conditsCount];
            for (int k = 0; k < conditsCount; k++) {
                SqlPropValue pv = findCondits[k];

                sql.append(SqlBuilder.makeProperySqlFragment(clazz, propColumnMap, pv));
                params[k] = pv.getValue();

                // AND , OR;
                if (null != pv.getRelationCondition()) {
                    sql.append(pv.getRelationCondition().getTag());
                }
            }
            valueArgs = ObjectUtil.getArgsArray(params);
        }
        this.whereArgs = valueArgs;
        this.whereCondition = sql.toString();
        return this;
    }
    
    /**
     * 通过属性值对象，构建SQL条件片段
     * 
     * @param propColumnMap
     * @param pv
     */
    public static StringBuilder makeProperySqlFragment(Class<? extends BaseEntity> entityClass,
            Map<String, String> propColumnMap, SqlPropValue pv) {
        String columnName = propColumnMap.get(pv.getPropertyName());
        Assert.hasLength(columnName, entityClass.getCanonicalName() + "." + pv.getPropertyName()
                + " @Column.name must not be null!");
        StringBuilder sql = new StringBuilder();
        Object value = pv.getValue();
        Assert.notNull(value);
        int placeNum = SqlBuilder.getSqlPlaceholderCount(value);
        boolean isMultipleParam = placeNum > 1;
        sql.append("(");
        sql.append(columnName);
        sql.append(autoCheckConditionTag(isMultipleParam,pv));
        if (isMultipleParam) {
            sql.append("(");
        }
        sql.append(StringUtil.getPlaceholder(placeNum));
        if (isMultipleParam) {
            sql.append(")");
        }
        sql.append(")");
        return sql;
    }

    /**
     * 自动检测属性值，如果为集合或数组，过滤条件根据预期条件推断出对应的多参数过滤标识
     * @param isMultipleParam
     * @param pv
     * @return
     */
    private static String autoCheckConditionTag(boolean isMultipleParam, SqlPropValue pv) {
        SqlCondDef actualCondTag = pv.getFilterCondition();
        if(isMultipleParam){
            switch(pv.getFilterCondition()){
                case EQUAL : 
                    actualCondTag = SqlCondDef.IN;
                    break;
                case NOT_EQUAL : 
                    actualCondTag = SqlCondDef.NOT_IN;
                    break;
                default:
                    break;
            }
        }
        return actualCondTag.getTag();
    }

    /**
     * 根据object类型得到参数占位符个数
     * @param value
     * @return
     */
    public static int getSqlPlaceholderCount(Object value) {
        int placeNum = 1;
        if (value instanceof Collection) {
            placeNum = ((Collection<?>) value).size();
        } else if (value.getClass().isArray()) {
            placeNum = ((Object[]) value).length;
        }
        return placeNum;
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

    
    public String getUniqueColName() {
        return uniqueColName;
    }

    
    public void setUniqueColName(String uniqueColName) {
        this.uniqueColName = uniqueColName;
    }

    
    public Object getUniqueColValue() {
        return uniqueColValue;
    }

    
    public void setUniqueColValue(Object uniqueColValue) {
        this.uniqueColValue = uniqueColValue;
    }

    
    public List<String> getSqlColumns() {
        return sqlColumns;
    }

    
    public void setSqlColumns(List<String> sqlColumns) {
        this.sqlColumns = sqlColumns;
    }

    
    public String getWhereCondition() {
        return whereCondition;
    }

    
    public void setWhereCondition(String whereCondition) {
        this.whereCondition = whereCondition;
    }

    
    public Object[] getWhereArgs() {
        return whereArgs;
    }

    
    public void setWhereArgs(Object[] whereArgs) {
        this.whereArgs = whereArgs;
    }

    
    public Class<? extends BaseEntity> getEntityClass() {
        return entityClass;
    }

    
    public void setEntityClass(Class<? extends BaseEntity> entityClass) {
        this.entityClass = entityClass;
    }
    
    
}
