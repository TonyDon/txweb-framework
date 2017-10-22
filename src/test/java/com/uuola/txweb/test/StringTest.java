package com.uuola.txweb.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.support.JdbcUtils;

import com.uuola.commons.JsonUtil;
import com.uuola.commons.ObjectUtil;
import com.uuola.commons.StringUtil;
import com.uuola.commons.constant.CST_REGEX;
import com.uuola.txweb.framework.dao.support.SqlMaker;


public class StringTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        String name ="uc_name";
        System.out.println(JdbcUtils.convertUnderscoreNameToPropertyName(name));
        Integer[] ids = {1,2,3,4,5};
        List<Long> kList = new ArrayList<Long>(){{
            add(111L);
            add(222L);
        }
        };
        System.out.println(JsonUtil.toJSONString(ObjectUtil.getArgsArray("hello", ids, kList, 1985, new Date())));
        System.out.println(CST_REGEX.RE_DATETIME.matcher("2012-9-1 12:11").matches());
        System.out.println(StringUtil.getPlaceholder(10));
        
        List<String> keys = new ArrayList<String>();
        keys.add("A");
        keys.add("B");
        System.out.println(StringUtils.join(ObjectUtil.getArgsArray(keys),','));
    }

}
