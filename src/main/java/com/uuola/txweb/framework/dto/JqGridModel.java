/*
 * @(#)JqGridModel.java 2013-6-23
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.txweb.framework.dto;

import java.io.Serializable;
import java.util.List;


/**
 * <pre>
 * 用于jqgrid数据封装展示
 * @author tangxiaodong
 * 创建日期: 2013-6-23
 * </pre>
 */
public class JqGridModel<T> implements Serializable {
    
    private static final long serialVersionUID = -8937546833416069802L;

    // Your result List
    private List<T> rowDatas;

    // get how many rows we want to have into the grid - rowNum attribute in the
    // grid
    private Integer rows = 0;

    // Get the requested page. By default grid sets this to 1.
    private Integer page = 1;

    // sorting order - asc or desc
    private String sord;

    // get index row - i.e. user click to sort.
    private String sidx;

    // Search Field
    private String searchField;

    // The Search String
    private String searchString;

    // he Search Operation
    // ['eq','ne','lt','le','gt','ge','bw','bn','in','ni','ew','en','cn','nc']
    private String searchOper;

    // All Record
    private Integer records = 0;
    
    // 分页记录当前排号
    private Integer currRow = 0;

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
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

    public String getSearchField() {
        return searchField;
    }

    public void setSearchField(String searchField) {
        this.searchField = searchField;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public String getSearchOper() {
        return searchOper;
    }

    public void setSearchOper(String searchOper) {
        this.searchOper = searchOper;
    }

    public Integer getTotal() {
        return (int) Math.ceil((double) records / (double) rows);
    }

    public Integer getRecords() {
        return records;
    }

    public void setRecords(Integer records) {
        this.records = records;
    }

    
    public List<T> getRowDatas() {
        return rowDatas;
    }

    
    public void setRowDatas(List<T> rowDatas) {
        this.rowDatas = rowDatas;
    }

    
    public Integer getCurrRow() {
        this.currRow = rows * (page - 1);
        return currRow;
    }

    
    public void setCurrRow(Integer currRow) {
        this.currRow = currRow;
    }
}
