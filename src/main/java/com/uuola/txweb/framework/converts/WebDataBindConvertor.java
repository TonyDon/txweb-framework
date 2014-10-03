/*
 * @(#)WebDataBindConvertor.java 2013-6-11
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.txweb.framework.converts;

import java.util.Date;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.WebRequest;


/**
 * <pre>
 * @author tangxiaodong
 * 数据绑定转换器
 * 创建日期: 2013-6-11
 * </pre>
 */
public class WebDataBindConvertor implements WebBindingInitializer {


    /**
     * 对请求中的特殊对象进行转换<br/>
     * 该方法在HandlerMethodInvoker.initBinder被调用
     */
    @Override
    public void initBinder(WebDataBinder binder, WebRequest req) {
        binder.registerCustomEditor(Date.class, new MyDateEditor());
    }

}
