/*
 * @(#)TsBaseDAO.java 2013-10-5
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.txweb.framework.dao.support;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;


/**
 * <pre>
 * TXCMS
 * @author tangxiaodong
 * 创建日期: 2013-10-5
 * </pre>
 */
public class TsBaseDAO<T extends BaseEntity>  extends GenericBaseDAO<T> {

    /**
     * 通过该方法初始化SqlSessionFactory，
     * 当存在多个SqlSessionFactory时，通过@Qualifier注解确定具体的sessionFactory
     * @param sqlSessionFactory
     */
    @Autowired
    public void initSessionFactory(
            @Qualifier(DBQualifiers.TS_BASE_SF) SqlSessionFactory sqlSessionFactory,
            @Qualifier(DBQualifiers.TS_BASE_DS) DataSource dataSource){
        super.setSqlSessionFactory(sqlSessionFactory);
        super.setJdbcTemplateDataSource(dataSource);
    }
}
