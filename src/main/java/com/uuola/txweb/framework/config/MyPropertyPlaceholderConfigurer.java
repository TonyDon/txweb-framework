/*
 * @(#)MyPropertyPlaceholderConfigurer.java 2013-10-27
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.txweb.framework.config;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import com.uuola.commons.StringUtil;
import com.uuola.commons.coder.DESede;
import com.uuola.commons.coder.MyBase64;


/**
 * <pre>
 * 解析配置文件
 * @author tangxiaodong
 * 创建日期: 2013-10-27
 * </pre>
 */
public class MyPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(MyPropertyPlaceholderConfigurer.class);

    private static final String PASSWORD_ENCRYPT_PREFIX = "enc_";

    private static final String HASH_KEY = "@$DGLuE14vblL3D1LSyQ==$@"; 
 
    @Override
    protected String resolvePlaceholder(String placeholder, Properties props) {
        String value = props.getProperty(placeholder);
        if (StringUtil.isNotEmpty(value)) {
            int encPrefixIdx = value.indexOf(PASSWORD_ENCRYPT_PREFIX);
            if (encPrefixIdx >= 0) {
                // 对后部分字符串进行解密返回
                try {
                    value = value.substring(PASSWORD_ENCRYPT_PREFIX.length() + encPrefixIdx);
                    value = DESede.decrypt(MyBase64.decode(value), HASH_KEY);
                } catch (Exception e) {
                    logger.error("decrypt error.", e);
                    throw new RuntimeException(e);
                }
            }
        }
        return value;
    }
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        String enc = DESede.encrypt("root", HASH_KEY);
        System.out.println(enc);
        System.out.println(DESede.decrypt(enc, HASH_KEY));
        System.out.println(MyBase64.encode(enc));
    }

}
