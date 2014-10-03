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
     *  diancai base data
     *  基础数据库 事务管理器， 会话工厂, 数据源
     */
    String DC_BASE_TX = "dc_base_tx";
    String DC_BASE_SF = "dc_base_sf";
    String DC_BASE_DS = "dc_base_ds";
    
    /**
     * TXCMS base data
     */
    String TS_BASE_TX = "txwebfw_tx";
    String TS_BASE_SF = "txwebfw_sf";
    String TS_BASE_DS = "txwebfw_ds";
}
