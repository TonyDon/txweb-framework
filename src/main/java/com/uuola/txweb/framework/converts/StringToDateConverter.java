/*
 * @(#)StringToDateConverter.java 2013-11-9
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.txweb.framework.converts;

import java.util.Date;

import org.springframework.core.convert.converter.Converter;

import com.uuola.commons.DateUtil;
import com.uuola.commons.StringUtil;
import com.uuola.commons.constant.CST_DATE_FORMAT;
import com.uuola.commons.constant.CST_REGEX;


/**
 * <pre>
 * String -> Data 转换器
 * @author tangxiaodong
 * 创建日期: 2013-11-9
 * </pre>
 */
public class StringToDateConverter implements Converter<String, Date> {

    @Override
    public Date convert(String source) {
        if (StringUtil.isEmpty(source)) {
            return null;
        }
        if (CST_REGEX.RE_DATE.matcher(source).matches()) {
            return DateUtil.parseDate(source, CST_DATE_FORMAT.YYYY_MM_DD);
        } else if (CST_REGEX.RE_DATETIME_NOT_SS.matcher(source).matches()) {
            return DateUtil.parseDate(source, CST_DATE_FORMAT.YYYY_MM_DD_HH_MM);
        } else if (CST_REGEX.RE_DATETIME.matcher(source).matches()) {
            return DateUtil.parseDate(source, CST_DATE_FORMAT.YYYY_MM_DD_HH_MM_SS);
        } else {
            throw new IllegalArgumentException("Invalid Date value '" + source + "'");
        }
    }

}
