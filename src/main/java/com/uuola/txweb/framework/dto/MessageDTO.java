/*
 *  txdnet.cn tonydon
 * 
 */
package com.uuola.txweb.framework.dto;


/**
 *
 * @author tangxiaodong
 */
public class MessageDTO {
        /**
     * 校验信息类型
     */
    private String key;

    /**
     * 校验信息参数
     */
    private Object[] params;

    /**
     * 构造一个不含参数的校验信息的消息对象
     * 
     * @param key
     */
    public MessageDTO(String key) {
        this.key = key;
    }

    /**
     * 构造一个包含参数的校验信息的消息对象
     * 
     * @param key
     * @param params
     */
    public MessageDTO(String key, Object... params) {
        this.key = key;
        this.params = params;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

}
