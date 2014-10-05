/*
 * @(#)BaseQuery.java 2013-6-23
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.txweb.framework.query;

import java.io.Serializable;

import com.uuola.txweb.framework.dto.ValidateDTO;


/**
 * <pre>
 *
 * @author tangxiaodong
 * 创建日期: 2013-6-23
 * </pre>
 */
public abstract class BaseQuery extends ValidateDTO implements Serializable {

    private static final long serialVersionUID = 6592438136405632705L;

   // 当前页码
    private Integer pageNo;
    
    // 当前排号
    protected Integer crow;
    
    // 列表显示记录数
    protected Integer listSize;
    
    // 查询总记录数
    protected Integer records;

    // 排序 asc or desc
    protected String sord;
    
    // 排序所在字段名称
    protected String sidx;

    /**
     * 过滤参数不符合要求的属性值
     */
    abstract public void filter();

    public BaseQuery() {
        if (crow == null) {
            crow = 0;
        }
        if (listSize == null || listSize > 100) {
            listSize = 20;
        }
        if (records == null) {
            records = 0;
        }
    }

    /**
     * @return the crow
     */
    public Integer getCrow() {
        return crow;
    }

    /**
     * @param crow the crow to set
     */
    public void setCrow(Integer crow) {
        this.crow = crow;
    }

    /**
     * @return the listsize
     */
    public Integer getListSize() {
        return listSize;
    }

    /**
     * @param listsize the listsize to set
     */
    public void setListSize(Integer listSize) {
        if (listSize == null || listSize > 100) {
            this.listSize = 20;
        } else {
            this.listSize = listSize;
        }
    }

    
    public String getSord() {
        return sord;
    }

    
    public void setSord(String sord) {
        this.sord = sord;
    }

    
    public String getSidx() {
        return sidx;
    }

    
    public void setSidx(String sidx) {
        this.sidx = sidx;
    }

    
    public Integer getRecords() {
        return records;
    }

    
    public void setRecords(Integer records) {
        this.records = records;
    }

    
    public Integer getPageNo() {
        return pageNo;
    }

    
    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }
    
    /**
     * 计算当前记录行号
     */
    public void calcCurrRowIndex() {
        if (null != pageNo && pageNo > 0) {
            this.crow = this.listSize * (this.pageNo - 1);
        }
    }
}
