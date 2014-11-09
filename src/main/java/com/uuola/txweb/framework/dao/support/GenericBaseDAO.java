/*
 * @(#)GenericBaseDAO.java 2013-6-15
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.txweb.framework.dao.support;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.sql.DataSource;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;

import com.uuola.commons.CollectionUtil;
import com.uuola.commons.StringUtil;
import com.uuola.commons.constant.CST_CHAR;
import com.uuola.commons.exception.Assert;
import com.uuola.commons.reflect.ClassUtil;
import com.uuola.commons.reflect.FieldUtil;


/**
 * <pre>
 * @author tangxiaodong
 * DAO基础类，封装jdbcTemplate和mybatis 常用的数据库操作方法，
 * 创建日期: 2013-6-15
 * </pre>
 */
public abstract class GenericBaseDAO<T extends BaseEntity> extends SqlSessionDaoSupport {

    private JdbcTemplate jdbcTemplate;
    
    private Class<T> entityClass;
    
    private String tableName;
    
    
    public GenericBaseDAO(){
        setEntityClass();
        initTableName();
    }
    
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    
    public void setJdbcTemplateDataSource(DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.jdbcTemplate.setFetchSize(50);
        this.jdbcTemplate.setMaxRows(1000);
    }

    /**
     * 
     * @return 查询的实体类
     */
    @SuppressWarnings("unchecked")
    private void setEntityClass() {
        ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
        this.entityClass = (Class<T>) pt.getActualTypeArguments()[0];
    }
    
    /**
     * 通过实体获取表名
     * @return
     */
    private void initTableName(){
        this.tableName = ClassUtil.getTableName(this.entityClass);
    }
    
    /**
     * 得到实体对应表名称
     * @return
     */
    public String getTableName(){
        return this.tableName;
    }
    
    /**
     * 得到当前的数据源
     * @return
     */
    public DataSource getDataSource(){
        return this.getSqlSession().getConfiguration().getEnvironment().getDataSource();
    }
    
    /**
     * 得到 jdbcTemplate 预处理创建者工厂
     * @param sql
     * @param params
     * @return
     */
    private PreparedStatementCreatorFactory getPreparedStatementCreatorFactory(String sql, List<?> params){
        int[] types = getSqlParamsTypes(params);
        PreparedStatementCreatorFactory pscFactory = 
                new PreparedStatementCreatorFactory(sql, types);
        return pscFactory;
    }
    
    /**
     * jdbcTemplate工具通过ID得到实体类
     * @param id
     * @return
     */
    public T get(Serializable id) {
        String sql = "select * from " + this.tableName + " where id=? ";
        List<T> list = this.getJdbcTemplate().query(sql, new Object[] { id },
                new RowMapperResultSetExtractor<T>(BeanPropertyRowMapper
                        .newInstance(this.entityClass), 1));
        return extractSingleObject(list);
    }
    
    /**
     * 通过属性名，值条件查询实体记录
     * @param propertyName
     * @param conditionValue
     * @return  
     */
    public List<T> findByPropValue(String propertyName, Object conditionValue){
        SqlPropertyValue pv = new SqlPropertyValue(propertyName, conditionValue);
        return findByProperty(null, pv);
    }
    
    /**
     * jdbcTemplate 根据实体属性查询记录结果
     * @param selectPropertys 选择需要查出的属性
     * @param findCondits
     * @return
     */
    public List<T> findByProperty(String[] selectPropertys, SqlPropertyValue... findCondits) {
        Assert.notEmpty(findCondits);

        int conditsCount = findCondits.length;
        Map<String, String> propColumnMap = getPropertyColumnMap(this.entityClass);
        String queryColumn = ObjectUtils.isEmpty(selectPropertys) ? " * " : getQueryColumn(selectPropertys, propColumnMap);

        StringBuilder sql = new StringBuilder("select " + queryColumn + " from " + this.tableName + " where ");
        Object[] valueArgs = null;
        if (1 == conditsCount) {
            SqlPropertyValue pv = findCondits[0];
            sql.append(makeProperySqlFragment(propColumnMap, pv));
            valueArgs = SqlBuilder.getArgsArray(pv.getValue());
            
        } else {
            Object[] params = new Object[conditsCount];
            for (int k = 0; k < conditsCount; k++) {
                SqlPropertyValue pv = findCondits[k];

                sql.append(makeProperySqlFragment(propColumnMap, pv));
                params[k] = pv.getValue();

                // AND , OR
                if (null != pv.getRelationCondition()) {
                    sql.append(pv.getRelationCondition().getTag());
                }
            }

            valueArgs = SqlBuilder.getArgsArray(params);
        }

        return this.getJdbcTemplate().query(sql.toString(), valueArgs,
                BeanPropertyRowMapper.newInstance(this.entityClass));
    }

    /**
     * 通过属性值对象，构建SQL条件片段
     * @param propColumnMap
     * @param pv
     */
    private StringBuilder makeProperySqlFragment(Map<String, String> propColumnMap, SqlPropertyValue pv) {
        String columnName = propColumnMap.get(pv.getPropertyName());
        Assert.hasLength(columnName, entityClass.getCanonicalName() + "." + pv.getPropertyName()
                + " @Column.name must not be null!");
        StringBuilder sql = new StringBuilder();
        sql.append("(");
        sql.append(columnName);
        sql.append(pv.getFilterCondition().getTag());
        Object value = pv.getValue();
        Assert.notNull(value);
        int placeNum = getParamsPlaceholder(value);
        boolean isMultipleParam = placeNum > 1;
        if (isMultipleParam) {
            sql.append("(");
        }
        sql.append(SqlBuilder.getPlaceholder(placeNum));
        if (isMultipleParam) {
            sql.append(")");
        }
        sql.append(")");
        return sql;
    }
    
    /**
     * 得到实体属性名与列标注名关系
     * @param entityClass2
     * @return
     */
    private Map<String, String> getPropertyColumnMap(Class<T> entityClass) {
        Map<String, Field> nameFieldMap = FieldUtil.getAllAccessibleFieldNameMap(entityClass, BaseEntity.class);
        Assert.notEmpty(nameFieldMap);
        Map<String, String> propColumnMap = new HashMap<String,String>(CollectionUtil.preferedMapSize(nameFieldMap.size()));
        boolean isFoundIdColumn = false;
        for(Map.Entry<String,Field> entry : nameFieldMap.entrySet()){
            String propName = entry.getKey();
            Field field = nameFieldMap.get(propName);
            Assert.notNull(field);
            Column column = field.getAnnotation(Column.class);
            String columnName = null;
            if(null != column && StringUtil.isNotEmpty(columnName = column.name())){
                propColumnMap.put(propName, columnName);
            }
            if(!isFoundIdColumn){
                // 构建主键ID对应的表字段名称
                Id id  = field.getAnnotation(Id.class);
                if(null != id){
                    isFoundIdColumn = true;
                    propColumnMap.put(field.getName(), StringUtil.getUnderscoreName(field.getName()));
                }
            }
        }
        return propColumnMap;
    }

    /**
     * 根据object类型得到参数占位符个数
     * @param value
     * @return
     */
    private int getParamsPlaceholder(Object value) {
        int placeNum = 1;
        if (value instanceof Collection) {
            placeNum = ((Collection<?>) value).size();
        } else if (value.getClass().isArray()) {
            placeNum = ((Object[]) value).length;
        }
        return placeNum;
    }

    /**
     * 根据输入的属性名得到查询列名称信息
     * @param selectPropertys
     * @param propColumnMap
     * @return
     */
    private String getQueryColumn(String[] selectPropertys, Map<String, String> propColumnMap) {
        List<String> columns = new ArrayList<String>(selectPropertys.length);
        for (String prop : selectPropertys) {
            String columnName = propColumnMap.get(prop);
            Assert.hasLength(columnName, entityClass.getCanonicalName() + "." + prop
                    + " @Column.name must not be null!");
            columns.add(columnName);
        }
        return StringUtil.join(columns, CST_CHAR.CHAR_COMMA);
    }

    /**
     * jdbcTemplate方法删除实体记录
     * @param id
     * @return
     */
    public int deleteById(Serializable id){
        String sql = "delete from " +  this.tableName +" where id=? ";
        return this.getJdbcTemplate().update(sql, id);
    }
    
    /**
     * 通过唯一key值删除实体记录
     * @param entity
     * @return
     */
    public int deleteByUniqueKey(T entity){
        SqlBuilder sqlBuilder = new SqlBuilder(entity).build();
        return delete(sqlBuilder);
    }
    

    /**
     * 与 deleteByUniqueKey()相同
     * @param entity
     * @return
     */
    public int delete(T entity){
        return deleteByUniqueKey(entity);
    }
    
    /**
     * jdbcTemplate 通过SqlBuilder构建器 删除实体记录
     * @param id
     * @return
     */
    public int delete(SqlBuilder sqlBuilder){
        return this.update(sqlBuilder.getDeleteSql(), sqlBuilder.getUniqueKeyValue());
    }
    
    /**
     * JdbcTemplate 更新记录
     * @param sql
     * @param params
     * @return
     */
    public int update(String sql, List<?> params) {
        return jdbcTemplate.update(getPreparedStatementCreatorFactory(sql, params)
                .newPreparedStatementCreator(params));
    }
    
    /**
     * JdbcTemplate 更新记录
     * @param sql
     * @param params
     * @return
     */
    public int update(String sql, Object... params){
        return this.getJdbcTemplate().update(sql, params);
    }
    
    /**
     * JdbcTemplate 更新实体记录到数据库
     * @param entity
     * @return 
     */
    public int update(T entity){
        SqlBuilder sqlBuilder = new SqlBuilder(entity).build();
        return update(sqlBuilder);
    }
    
    /**
     * JdbcTemplate 通过SqlBuilder构建器更新记录到数据库
     * @param entity
     * @return 
     */
    public int update(SqlBuilder sqlBuilder){
        return this.update(sqlBuilder.getUpdateSql(), sqlBuilder.getSqlParams());
    }

    
    /**
     * JdbcTemplate 保存实体记录到数据库, 并将自动递增
     * @param entity 
     */
    public void save(T entity){
        SqlBuilder sqlBuilder = new SqlBuilder(entity).build();
        Number id = this.saveReturnKey(sqlBuilder.getInsertSql(), sqlBuilder.getSqlParams());
        if (null != id) {
            Field idField = ReflectionUtils.findField(entity.getClass(), "id");
            Assert.notNull(idField, "The id Field, Not found in [" + entity.getClass().getCanonicalName() + "]");
            ReflectionUtils.makeAccessible(idField);
            Object idValue = getIdValueByNumber(id, idField.getType());
            ReflectionUtils.setField(idField, entity, idValue);
        }
    }
    
    /**
     * 将Number值转为具体字段类型值(Integer or Long)
     * @param id
     * @param fieldType
     * @return
     */
    private Object getIdValueByNumber(Number id, Class<?> fieldType) {
        if (fieldType == Integer.class) {
            return id.intValue();
        } else {
            return id.longValue();
        }
    }
    
    /**
     * jdbcTemplate 保存记录返回自动递增KEY值,如果没有自动递增，则返回null
     * @param sql
     * @param params
     * @return
     */
    public Number saveReturnKey(String sql, List<?> params) {
        PreparedStatementCreatorFactory pscFactory = getPreparedStatementCreatorFactory(sql, params);
        //pscFactory.setGeneratedKeysColumnNames(new String[]{"id"});
        pscFactory.setReturnGeneratedKeys(true);// return auto-GenerateKey
        KeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreator psc = pscFactory.newPreparedStatementCreator(params);
        jdbcTemplate.update(psc, keyHolder);
        return keyHolder.getKey();
    }
    
    public Number saveReturnKey(String sql, Object...params){
        return saveReturnKey(sql, Arrays.asList(params));
    }
    
    /**
     * 获取SQL查询对象数组对应的SQL TYPE
     * @param params
     * @return
     */
    public int[] getSqlParamsTypes(List<?> params) {
        if(null == params || params.isEmpty()){
            return null;
        }
        int[] types = new int[params.size()];
        for(int i=0; i<params.size(); i++){
            types[i] = getObjectSqlType(params.get(i));
        }
       return types;
    }
    
    /**
     * 得到对象对应的常用SQL TYPE
     * @param o
     * @return
     */
    public int getObjectSqlType(Object o) {
        if (o == null) {
            return JdbcUtils.TYPE_UNKNOWN;
        }
        if (o instanceof String) {
            return Types.VARCHAR;
        }
        if( o instanceof java.util.Date){
            return Types.TIMESTAMP;
        }
        if (o instanceof Number) {
            if (o instanceof Long) {
                return Types.BIGINT;
            }
            if (o instanceof Integer) {
                return Types.INTEGER;
            }
            if (o instanceof Short) {
                return Types.SMALLINT;
            }
            if (o instanceof Byte) {
                return Types.TINYINT;
            }
            if (o instanceof BigDecimal) {
                return Types.DECIMAL;
            }
            if (o instanceof Double) {
                return Types.DOUBLE;
            }
        }
        return JdbcUtils.TYPE_UNKNOWN;
    }


    /**
     * JdbcTemplate 返回单个Map记录
     * @param sql
     * @param params
     * @return
     */
    public Map<String, Object> queryForMap(String sql, Object... params) {
        List<Map<String, Object>> results = this.getJdbcTemplate().query(sql, params,
                new RowMapperResultSetExtractor<Map<String, Object>>(new ColumnMapRowMapper(), 1));
        return extractSingleObject(results);
    }
    
    /**
     * 从集合中取出一个对象
     * @param results
     * @return
     */
    private  <E> E extractSingleObject(Collection<E> results) {
        int size = (results != null ? results.size() : 0);
        if(size == 0){
            return null;
        }
        return results.iterator().next();
    }


    /**
     * jdbcTemplate 查询单个对象，如果有多个对象返回会抛出异常<br/>
     * 返回对象为表列对应的JAVA基本对象如Long, String, Double，不能是POJO对象。
     * @param sql
     * @param requiredType 
     * @param params
     * @return
     */
    public <E> E queryForObject(String sql, Class<E> requiredType, Object... params) {
        List<E> results = this.getJdbcTemplate().query(sql, params,
                new RowMapperResultSetExtractor<E>(new SingleColumnRowMapper<E>(requiredType)));
        return extractSingleObject(results);
    }
    
    /**
     * jdbcTemplate 查询结果应为数值类型
     * @param sql
     * @param params
     * @return
     */
    public Number queryForNumber(String sql, Object... params){
        return this.queryForObject(sql, Number.class, params);
    }
    
    /**
     * JdbcTemplate 查询记录
     * @param sql
     * @param params
     * @return List<Map<String, Object>>
     * @throws SQLException 
     */
    public List<Map<String, Object>> executeQuery(String sql, Object... params){
        return this.getJdbcTemplate().queryForList(sql, params);
    }
    
    /**
     * JdbcTemplate 查询记录，返回结果集使用Clazz的实例，而不是map
     * @param sql
     * @param clazz
     * @param params
     * @return
     */
    public <E> List<E> executeQuery(String sql, Class<E> clazz, Object... params ){
        return this.getJdbcTemplate().query(sql, params, BeanPropertyRowMapper.newInstance(clazz));
    }
    

    /**
     * Mybatis 通过ID查询实体
     * @param id
     * @return
     */
    public T getByMapper(String mapperId, Long id){
        if(null == id){
            return null;
        }
        return this.getSqlSession().selectOne(mapperId, id);
    }
    
    
    /**
     * Mybatis 通过ID删除记录
     * @param mapperId
     * @param id
     * @return
     */
    public int deleteByMapper(String mapperId, Long id){
        if(null == id){
            return 0;
        }
        return this.getSqlSession().delete(mapperId, id);
    }
    
    /**
     *  Mybatis Update
     * @param mapperId
     * @param parameter
     * @return
     */
    public int updateByMapper(String mapperId, Object parameter){
        return this.getSqlSession().update(mapperId, parameter);
    }
    
    /**
     * Mybatis Insert
     * @param mapperId
     * @param parameter
     * @return
     */
    public int insertByMapper(String mapperId, Object parameter){
        return this.getSqlSession().insert(mapperId, parameter);
    }
    
    /**
     * Mybatis Select One Query
     * @param mapperId
     * @param parameter
     * @return
     */
    public <E> E selectOne(String mapperId, Object parameter){
        return this.getSqlSession().selectOne(mapperId, parameter);
    }
    
    /**
     * Mybatis Select Collections Query
     * @param mapperId
     * @param parameter
     * @return
     */
    public <E> List<E> selectList(String mapperId, Object parameter){
        return this.getSqlSession().selectList(mapperId, parameter);
    }
}
