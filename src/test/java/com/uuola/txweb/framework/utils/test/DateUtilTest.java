/*
 * @(#)DateUtilTest.java 2013-6-16
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.txweb.framework.utils.test;

import junit.framework.Assert;

import org.junit.Test;

import com.uuola.commons.DateUtil;


/**
 * <pre>
 *
 * @author tangxiaodong
 * 创建日期: 2013-6-16
 * </pre>
 */
public class DateUtilTest {

    @Test
    public void test1(){
        String t = Long.toString( DateUtil.getCurrTime() );
        System.out.println(t);
        Assert.assertEquals("000", t.substring(t.length()-3,t.length()));
    }
    
    @Test
    public void test2(){
        String t = DateUtil.formatDate(DateUtil.parseDate("2013-1-1", "yyyy-MM-dd"),"yyyy/M/d");
        System.out.println(t);
        Assert.assertEquals("2013/1/1", t);
    }
}
