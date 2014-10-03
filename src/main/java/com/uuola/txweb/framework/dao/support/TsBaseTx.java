/*
 * @(#)TsBaseTx.java 2013-10-5
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.txweb.framework.dao.support;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.transaction.annotation.Transactional;


/**
 * <pre>
 *
 * @author tangxiaodong
 * 创建日期: 2013-10-5
 * </pre>
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Transactional(DBQualifiers.TS_BASE_TX)
public @interface TsBaseTx {

}
