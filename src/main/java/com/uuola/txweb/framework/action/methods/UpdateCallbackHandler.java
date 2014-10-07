/*
 * @(#)UpdateCallbackHandler.java 2014-10-7
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.txweb.framework.action.methods;

import com.uuola.txweb.framework.dto.ValidateDTO;



/**
 * <pre>
 * Action 更新操作模版方法接口
 * @author tangxiaodong
 * 创建日期: 2014-10-7
 * </pre>
 */
public interface UpdateCallbackHandler<T> {

    /**
     * 更新结果返回泛型对象
     * @param clientDTO
     * @return
     */
     T doUpdate(ValidateDTO clientDTO);
}
