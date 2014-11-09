package com.uuola.txweb.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.support.JdbcUtils;

import com.uuola.commons.JsonUtil;
import com.uuola.commons.constant.CST_REGEX;
import com.uuola.txweb.framework.dao.support.SqlBuilder;


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
        System.out.println(JsonUtil.toJSONString(SqlBuilder.getArgsArray("hello", ids, kList, 1985, new Date())));
        System.out.println(CST_REGEX.RE_DATETIME.matcher("2012-9-1 12:11").matches());
        System.out.println(SqlBuilder.getPlaceholder(10));
    }

}
