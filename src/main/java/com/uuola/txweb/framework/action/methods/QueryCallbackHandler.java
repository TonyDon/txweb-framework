/*
 * @(#)QueryCallbackHandler.java 2014-10-6
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.txweb.framework.action.methods;

import com.uuola.txweb.framework.dto.PageDTO;
import com.uuola.txweb.framework.query.BaseQuery;


/**
 * <pre>
 *
 * @author tangxiaodong
 * 创建日期: 2014-10-6
 * </pre>
 */
public interface QueryCallbackHandler {

    /**
     * 查询执行回调
     * @param query
     * @param webRequest
     * @return
     */
    PageDTO doQuery(BaseQuery query);
}
