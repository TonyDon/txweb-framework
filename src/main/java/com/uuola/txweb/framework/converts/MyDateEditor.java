/*
 * @(#)MyDateEditor.java 2013-6-12
 * 
 * Copy Right@ uuola
 */

package com.uuola.txweb.framework.converts;

import java.beans.PropertyEditorSupport;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.uuola.commons.StringUtil;
import com.uuola.commons.constant.CST_CHAR;
import com.uuola.commons.constant.CST_DATE_FORMAT;
import com.uuola.commons.constant.CST_REGEX;

/**
 * <pre>
 * @author tangxiaodong
 * 日期类型属性编辑器
 * 创建日期: 2013-6-12
 * </pre>
 */
public class MyDateEditor extends PropertyEditorSupport {

    private DateFormat dateFormat;

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        String dateFmt = null;
        if (StringUtil.isEmpty(text)) {
            setValue(null);
            return;
        }
        if (CST_REGEX.RE_DATE.matcher(text).matches()) {
            dateFmt = CST_DATE_FORMAT.YYYY_MM_DD;
        } else if (CST_REGEX.RE_DATETIME_NOT_SS.matcher(text).matches()) {
            dateFmt = CST_DATE_FORMAT.YYYY_MM_DD_HH_MM;
        } else if (CST_REGEX.RE_DATETIME.matcher(text).matches()) {
            dateFmt = CST_DATE_FORMAT.YYYY_MM_DD_HH_MM_SS;
        }
        if (null == dateFmt) {
            throw new IllegalArgumentException("MyDateEditor Could not parse date: " + text);
        }
        this.dateFormat = new SimpleDateFormat(dateFmt);
        this.dateFormat.setLenient(true);
        try {
            setValue(dateFormat.parse(text));
        } catch (ParseException ex) {
            throw new IllegalArgumentException("MyDateEditor Could not parse date: " + ex.getMessage(), ex);
        }
    }

    @Override
    public String getAsText() {
        if (null == dateFormat) {
            this.dateFormat = new SimpleDateFormat(CST_DATE_FORMAT.YYYY_MM_DD_HH_MM_SS);
        }
        Date value = (Date) getValue();
        return (value != null ? this.dateFormat.format(value) : CST_CHAR.STR_EMPTY);
    }

    public DateFormat getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

}
