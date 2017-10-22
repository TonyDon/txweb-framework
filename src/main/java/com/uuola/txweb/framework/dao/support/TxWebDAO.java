/*
 * @(#)TsBaseDAO.java 2013-10-5
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.txweb.framework.dao.support;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;


/**
 * <pre>
 * TXCMS
 * @author tangxiaodong
 * 创建日期: 2013-10-5
 * </pre>
 */
public class TxWebDAO<T>  extends GenericBaseDAO<T> {

    @Autowired
    public void initSessionFactory(
            @Qualifier(DBQualifiers.TXWEB_MYBATIS_SQL_TEMPLATE) SqlSessionTemplate sqlSessionTemplate) {
        super.setSqlSessionTemplate(sqlSessionTemplate);
    }

    @Autowired
    public void initJdbcTemplate(
            @Qualifier(DBQualifiers.TXWEB_JDBC_TEMPLATE) NamedParameterJdbcTemplate jdbcTemplate) {
        super.setJdbcTemplate(jdbcTemplate);
    }
}
