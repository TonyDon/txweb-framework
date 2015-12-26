/*
 * @(#)PageDTO.java 2013-6-23
 * 
 * Copy Right@ uuola
 */

package com.uuola.txweb.framework.dto;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * <pre>
 * 用于页面记录展示，DataGrid处理
 * @author tangxiaodong
 * 创建日期: 2013-6-23
 * </pre>
 */
public class PageDTO implements Serializable {

    private static final long serialVersionUID = -4153700948568583441L;

    /**
     * 当前记录集合
     */
    @SuppressWarnings("rawtypes")
    private Collection datas;

    /**
     * 总记录数
     */
    private int total;

    /**
     * 扩展数据集合
     */
    private Map<String, Object> extraInfo;

    public PageDTO() {}

    /**
     * 当前数据集，总数据条数
     * @param datas
     * @param total
     */
    @SuppressWarnings("rawtypes")
    public PageDTO(Collection datas, int total) {
        if (datas == null) {
            this.datas = Collections.emptyList();
        }
        this.datas = datas;
        this.total = total;
    }

    public PageDTO(Object[] datas, int total) {
        if (datas == null) {
            this.datas = Collections.emptyList();
            return;
        }
        this.datas = Arrays.asList(datas);
        this.total = total;
    }

    @SuppressWarnings("rawtypes")
    public Collection getDatas() {
        return datas;
    }

    @SuppressWarnings("rawtypes")
    public void setDatas(Collection datas) {
        this.datas = datas;
    }

    public Map<String, Object> getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(Map<String, Object> extraInfo) {
        this.extraInfo = extraInfo;
    }

    
    public int getTotal() {
        return total;
    }

    
    public void setTotal(int total) {
        this.total = total;
    }
    
}
