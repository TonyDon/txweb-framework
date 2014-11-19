/*
 * @(#)EntityPropColumnManager.java 2014-11-12
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.txweb.framework.dao.support;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.persistence.Column;
import javax.persistence.Id;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uuola.commons.CollectionUtil;
import com.uuola.commons.StringUtil;
import com.uuola.commons.exception.Assert;
import com.uuola.commons.reflect.ClassUtil;
import com.uuola.commons.reflect.FieldUtil;



/**
 * <pre>
 * 实体属性与表列名属性管理器
 * @author tangxiaodong
 * 创建日期: 2014-11-12
 * </pre>
 */
public class EntityDefManager {
    
    private static Logger log = LoggerFactory.getLogger(EntityDefManager.class);

    private static ConcurrentMap<Class<? extends BaseEntity>, EntityDefBean> entityPropContainer = new ConcurrentHashMap<Class<? extends BaseEntity>, EntityDefBean>();

    public static EntityDefBean addEntityClass(Class<? extends BaseEntity> clazz) {
        EntityDefBean defBean = entityPropContainer.get(clazz);
        if (null == defBean) {
            defBean = resolveEntityClass(clazz);
            entityPropContainer.putIfAbsent(clazz, defBean);
            log.info("add entity class def bean to manager : " + clazz.getCanonicalName());
        }
        return defBean;
    }
    
    public static EntityDefBean getDef(Class<? extends BaseEntity> clazz){
        return addEntityClass(clazz);
    }

    private static EntityDefBean resolveEntityClass(Class<? extends BaseEntity> clazz) {
        EntityDefBean defBean = new EntityDefBean();
        defBean.setEntityClass(clazz);
        defBean.setTableName(ClassUtil.getTableName(clazz));
        Map<String, Field> propFieldMap = FieldUtil.getAllAccessibleFieldNameMap(defBean.getEntityClass(),
                BaseEntity.class);
        Assert.notEmpty(propFieldMap);
        defBean.setPropFieldMap(propFieldMap);
        Map<String, String> propColumnMap = getPropertyColumnMap(defBean);
        Assert.notEmpty(propColumnMap);
        defBean.setPropColumnMap(propColumnMap);
        return defBean;
    }
    
    /**
     * 得到实体属性名与列标注名关系
     * @param entityClass2
     * @return
     */
    private  static Map<String, String> getPropertyColumnMap(EntityDefBean defBean) {
        Map<String, Field> propFieldMap = defBean.getPropFieldMap();
        Map<String, String> propColumnMap = new HashMap<String, String>(CollectionUtil.preferedMapSize(propFieldMap
                .size()));
        boolean isFoundIdColumn = false;
        for (Map.Entry<String, Field> entry : defBean.getPropFieldMap().entrySet()) {
            String propName = entry.getKey();
            Field field = propFieldMap.get(propName);
            Assert.notNull(field);
            Column column = field.getAnnotation(Column.class);
            String columnName = null;
            if (null != column && StringUtil.isNotEmpty(columnName = column.name())) {
                propColumnMap.put(propName, columnName);
            }
            if (!isFoundIdColumn) {
                // 构建主键ID对应的表字段名称
                Id id = field.getAnnotation(Id.class);
                if (null != id) {
                    isFoundIdColumn = true;
                    String fieldName = field.getName();
                    propColumnMap.put(fieldName,
                            fieldName.equals("id") ? "id" : StringUtil.getUnderscoreName(fieldName));
                }
            }
        }
        return propColumnMap;
    }
}
