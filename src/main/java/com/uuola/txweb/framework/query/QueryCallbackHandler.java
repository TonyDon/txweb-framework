/*
 * @(#)QueryCallbackHandler.java 2014-10-6
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.txweb.framework.query;

import com.uuola.txweb.framework.dto.PageDTO;


/**
 * <pre>
 *
 * @author tangxiaodong
 * 创建日期: 2014-10-6
 * </pre>
 */
public interface QueryCallbackHandler {

    PageDTO doQuery(BaseQuery query);
}