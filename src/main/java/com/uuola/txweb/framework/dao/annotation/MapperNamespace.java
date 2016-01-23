/*
 * @(#)MapperNamespace.java 2016年1月23日
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.txweb.framework.dao.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * <pre>
 * Mybatis DAO MapperNamespace 注解
 * @author tangxiaodong
 * 创建日期: 2016年1月23日
 * </pre>
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MapperNamespace {
    
    /**
     * mybatis mapper namespace , if default "" , will use class path full name.
     * @return
     */
    String value() default "";
}
