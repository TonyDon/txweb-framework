/*
 * @(#)BaseQueryAction.java 2013-6-23
 * 
 * Copy Right@ uuola
 */

package com.uuola.txweb.framework.action;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.uuola.commons.constant.HTTP_STATUS_CODE;
import com.uuola.commons.exception.BusinessException;
import com.uuola.commons.exception.BusinessExceptionMessageProvider;
import com.uuola.txweb.framework.action.methods.QueryCallbackHandler;
import com.uuola.txweb.framework.dto.PageDTO;
import com.uuola.txweb.framework.query.BaseQuery;

/**
 * <pre>
 * 查询Action方法封装
 * @author tangxiaodong
 * 创建日期: 2013-6-23
 * </pre>
 */
public abstract class BaseQueryAction extends BaseAction {

    // 页面使用PageDTO的属性名称 如: page.datas , page.total
    protected final String QUERY_PAGE_ATTR = "page";

    /**
     * 封装查询过程
     * 
     * @param query
     *            查询条件
     * @param handler
     *            查询处理外部回调执行者
     * @param webRequest
     *            当前Request
     * @return
     */
    protected ModelAndView executeQuery(ServletWebRequest webRequest, BaseQuery query, QueryCallbackHandler handler) {
        ModelAndView model = new ModelAndView();
        List<String> errors = new ArrayList<String>();
        if (query.validatePass()) {
            try {
                // 执行查询条件过滤方法，如非法值过滤，默认条件设置, 值转换等
                query.filter();
                // 执行分页查询记录行计算
                query.calcCurrRowIndex();
                // 查询返回PageDTO对象
                PageDTO pageDTO = handler.doQuery(query);
                model.addObject(QUERY_PAGE_ATTR, pageDTO);
            } catch (BusinessException be) {
                errors.add(BusinessExceptionMessageProvider.getMessage(be));
                log.error("", be);
            } catch (Exception e) {
                errors.add(ExceptionUtils.getFullStackTrace(e));
                log.error("", e);
            }

        } else {
            errors.addAll(getErrors(query));
        }

        if (!errors.isEmpty()) {
            model.addObject(ERRORS_ATTR, errors);
            webRequest.getResponse().setStatus(HTTP_STATUS_CODE.SC_BZ_ERROR);
        }

        return model;
    }

}
