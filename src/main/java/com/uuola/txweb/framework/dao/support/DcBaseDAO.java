/*
 * @(#)DcBaseDAO.java 2013-6-15
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
 * @author tangxiaodong
 * dc_base_data库DAO
 * 创建日期: 2013-6-15
 * </pre>
 */
public class DcBaseDAO<T extends BaseEntity> extends GenericBaseDAO<T> {

    /**
     * 通过该方法初始化SqlSessionFactory，
     * 当存在多个SqlSessionFactory时，通过@Qualifier注解确定具体的sessionFactory
     * @param sqlSessionFactory
     */
    @Autowired
    public void initSessionFactory(
            @Qualifier(DBQualifiers.DC_BASE_SF) SqlSessionFactory sqlSessionFactory,
            @Qualifier(DBQualifiers.DC_BASE_DS) DataSource dataSource){
        super.setSqlSessionFactory(sqlSessionFactory);
        super.setJdbcTemplateDataSource(dataSource);
    }
}
