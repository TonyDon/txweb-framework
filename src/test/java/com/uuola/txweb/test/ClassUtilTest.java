/*
 * @(#)ClassUtilTest.java 2013-7-27
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.txweb.test;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.springframework.util.ReflectionUtils;

import com.uuola.commons.DateUtil;
import com.uuola.commons.reflect.FieldUtil;
import com.uuola.txweb.framework.action.BaseAction;
import com.uuola.txweb.framework.dao.support.EntityDefManager;
import com.uuola.txweb.framework.dao.support.EntityDefine;
import com.uuola.txweb.framework.dao.support.SqlMaker;


/**
 * <pre>
 *
 * @author tangxiaodong
 * 创建日期: 2013-7-27
 * </pre>
 */
public class ClassUtilTest {

    @Test
    public void test_class_getName(){
        System.out.println(DateUtil.class.getName());
        System.out.println(DateUtil.class.getCanonicalName());
        System.out.println(DateUtil.class.getSimpleName());
        System.out.println(StringUtils.substringAfterLast(DateUtil.class.getName(), "."));
    }
    
    @Test
    public void test_table_column(){
        DemoEntity de = new DemoEntity();
        de.setCitycode("00000123");
        Collection<Field> fset = FieldUtil.getAllAccessibleFields(de.getClass());
        for (Field f : fset)
            System.out.println(f.getAnnotation(Column.class) == null ? f.getName() : f.getAnnotation(Column.class)
                    .name());
        Field cityCode = FieldUtil.findField(fset, "citycode", null);
        if (cityCode != null)
            System.out.println(FieldUtil.getValue(cityCode, de));
    }
    
    @Test
    public void test_sql_buidler(){
        DemoEntity de = new DemoEntity();
        de.setId(-DateUtil.getCurrTime());
        de.setCode("000000123");
        de.setGender((byte)1);
        de.setBirthday(new Date());
        EntityDefine entityDef = EntityDefManager.getDef(DemoEntity.class);
        SqlMaker sqlBuilder = new SqlMaker(entityDef).build(de);
        System.out.println(ReflectionUtils.findField(DemoEntity.class, "code").getType().getName());
        System.out.println(sqlBuilder.getInsertSql());
        System.out.println(sqlBuilder.getUpdateSql());
    }
    
    @Test
    public void test_class_1(){
        System.out.println(ClassUtils.getPackageName(BaseAction.class));
        System.out.println(BaseAction.class.getPackage().getName());
        System.out.println(ClassUtils.getShortClassName(BaseAction.class));
    }

}
