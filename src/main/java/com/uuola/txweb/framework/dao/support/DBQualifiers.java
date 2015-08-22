/*
 * @(#)DBQualifiers.java 2013-6-15
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.txweb.framework.dao.support;


/**
 * <pre>
 * 如果应用需要访问多个数据库，则需要为每个数据库分别定义SessionFactory与TransactionManager。
 * 本接口对系统需要访问的每个数据库的TransactionManager的Qualifier和SessionFactory的Qualifier进行定义，
 * 注意相关的值必须与配置文件"spring-dao*.xml"中的对应的Qualifier值保存一致
 * @author tangxiaodong
 * 创建日期: 2013-6-15
 * </pre>
 */
public interface DBQualifiers {
    
    /**
     * TXCMS base data
     */
    String TXWEB_TS = "txwebfw_tx";
    String TXWEB_SF = "txwebfw_sf";
    String TXWEB_DS = "txwebfw_ds";
    String TXWEB_JDBC_TEMPLATE = "txwebJdbcTemplate";
    String TXWEB_MYBATIS_SQL_TEMPLATE = "txwebSqlSessionTemplate";
}
