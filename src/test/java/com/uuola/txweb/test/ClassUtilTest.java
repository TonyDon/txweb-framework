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

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.springframework.util.ReflectionUtils;

import com.uuola.commons.DateUtil;
import com.uuola.commons.reflect.ClassUtil;
import com.uuola.commons.reflect.FieldUtil;
import com.uuola.txweb.framework.dao.support.BaseEntity;
import com.uuola.txweb.framework.dao.support.SqlBuilder;


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
        BaseEntity de = new DemoEntity();
        ((DemoEntity)de).setCitycode("00000123");
        Collection<Field> fset = FieldUtil.getAllAccessibleFieldList(de.getClass(), BaseEntity.class);
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
        SqlBuilder sqlBuilder = new SqlBuilder(de).build();
        System.out.println(ReflectionUtils.findField(DemoEntity.class, "code").getType().getName());
        System.out.println(sqlBuilder.getInsertSql());
        System.out.println(sqlBuilder.getUpdateSql());
    }

}
