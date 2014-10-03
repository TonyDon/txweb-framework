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
 *
 * @author tangxiaodong
 * 创建日期: 2013-6-23
 * </pre>
 */
public class PageDTO implements Serializable {

    private static final long serialVersionUID = -4153700948568583441L;

    @SuppressWarnings("rawtypes")
    private Collection data;

    private int totalCount;

    private Map<String, Object> extraInfo;

    public PageDTO() {
    }

    @SuppressWarnings("rawtypes")
    public PageDTO(Collection data, int totalCount) {
        if (data == null) {
            this.data = Collections.emptyList();
        }
        this.data = data;
        this.totalCount = totalCount;
    }

    public PageDTO(Object[] data, int totalCount) {
        if (data == null) {
            this.data = Collections.emptyList();
            return;
        }
        this.data = Arrays.asList(data);
        this.totalCount = totalCount;
    }

    @SuppressWarnings("rawtypes")
    public Collection getData() {
        return data;
    }

    @SuppressWarnings("rawtypes")
    public void setData(Collection data) {
        this.data = data;
    }

    public Map<String, Object> getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(Map<String, Object> extraInfo) {
        this.extraInfo = extraInfo;
    }

    
    public int getTotalCount() {
        return totalCount;
    }

    
    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
    
}
