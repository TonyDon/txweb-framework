/*
 * @(#)BaseQueryAction.java 2013-6-23
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.txweb.framework.action;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.ServletWebRequest;

import com.uuola.commons.constant.HTTP_STATUS_CODE;
import com.uuola.commons.exception.BusinessException;
import com.uuola.commons.exception.BusinessExceptionMessageProvider;
import com.uuola.txweb.framework.dto.JqGridModel;
import com.uuola.txweb.framework.dto.PageDTO;
import com.uuola.txweb.framework.query.BaseQuery;


/**
 * <pre>
 * 支持jquery grid的查询基类
 * 继承该类后只能实现query方法和覆盖父类的方法
 * 页面查询条件 继承BaseQuery
 * 封装需要返回客户端的数据，最终返回的数据由该类型对象的get和is方法确定
 * @author tangxiaodong
 * 创建日期: 2013-6-23
 * </pre>
 */
public abstract class BaseQueryAction extends BaseAction{
    

    
    /**
     * 校验查询参数合法性
     * @param query
     * @param errors
     */
    protected boolean preValidQuery(BaseQuery query, List<String> errors) {
        query.validatePass();
        if (query.isValidPass()) {
            return true;
        }
        errors.addAll(getErrors(query));
        return false;
    }
    
    private <T> void preHandlerQuery(BaseQuery query, JqGridModel<T> model){
        query.setCrow(model.getCurrRow());
        query.setListSize(model.getRows());
        query.setSord(model.getSord());
        query.setSidx(model.getSidx());
    }
    
    /**
     * jqGrid查询列表<br/>
     * Ajax Get 查询方法入口  model/query.json
     * @param query
     * @param model
     * @throws IOException 
     */
//    @RequestMapping(value="/query", method = RequestMethod.GET)
//    public <T> void list(
//            BaseQuery query, 
//            @ModelAttribute("model") JqGridModel<T> model, 
//            @ModelAttribute(ERRORS_ATTR) ArrayList<String> errors, 
//            ServletWebRequest webRequest) {
//        if (!preValidQuery(query, errors)) {
//            webRequest.getResponse().setStatus(HTTP_STATUS_CODE.SC_BAD_REQUEST);
//            return;
//        }
//        preHandlerQuery(query, model);
//        query.filter();
//        try {
//            PageDTO pageDTO = query(query);
//            toModel(pageDTO, model);
//        } catch (BusinessException be) {
//            errors.add(BusinessExceptionMessageProvider.getMessage(be));
//        } catch (Exception e) {
//            errors.add(ExceptionUtils.getFullStackTrace(e));
//        }
//        if(!errors.isEmpty()){
//            webRequest.getResponse().setStatus(HTTP_STATUS_CODE.SC_BZ_ERROR);
//        }
//    }
    
    /**
     * jsTable通用列表<br/>
     * Ajax Get 查询方法入口  model/search.json
     * @param query
     * @param errors
     * @param webRequest
     * @param model
     */
//    @RequestMapping(value = "/search", method = RequestMethod.GET)
//    public void search(BaseQuery query, 
//            @ModelAttribute(ERRORS_ATTR)ArrayList<String> errors, 
//            ServletWebRequest webRequest, Model model) {
//        if (!preValidQuery(query, errors)) {
//            webRequest.getResponse().setStatus(HTTP_STATUS_CODE.SC_BAD_REQUEST);
//            return;
//        }
//        query.filter();
//        query.calcCurrRowIndex();
//        try {
//            PageDTO pageDTO = query(query);
//            model.addAttribute("page", pageDTO);
//        } catch (BusinessException be) {
//            errors.add(BusinessExceptionMessageProvider.getMessage(be));
//        } catch (Exception e) {
//            errors.add(ExceptionUtils.getFullStackTrace(e));
//        }
//        if (!errors.isEmpty()) {
//            webRequest.getResponse().setStatus(HTTP_STATUS_CODE.SC_BZ_ERROR);
//        }
//    }
    
    private <T> void toModel(PageDTO pageDto, JqGridModel<T> model){
        List<T> rows = parseViewDto(pageDto.getData());
        model.setRowDatas(rows);
        model.setRecords(pageDto.getTotalCount());
    }
    
    @SuppressWarnings({"unchecked","rawtypes"})
    protected <T> List<T> parseViewDto(Collection data){
        if (data == null || data.isEmpty()) {
            return Collections.emptyList();
        }
        Object first = CollectionUtils.get(data, 0);
        if(first.getClass() == getModelClass()){
            return new ArrayList<T>(data);
        }
        List<T> dtoList  = new ArrayList<T>(data.size());
        for (Object o : data) {
            T target = newModel();
            BeanUtils.copyProperties(o, target);
            dtoList.add(target);
        }
        return (List<T>)data;
    }
    
    protected <T> T newModel() {
        Class<T> t = getModelClass();
        try {
            return t.newInstance();
        } catch (Exception e) {
            throw new BusinessException("Class[%s] new Instance Error!", t.getCanonicalName());
        }
    }
    
    /**
     * 得到类泛型参数
     * @return
     */
    @SuppressWarnings("unchecked")
    protected <T> Class<T> getModelClass() {    
            ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
            return (Class<T>) pt.getActualTypeArguments()[1];
    }
    
    /**
     * 查询分页数据
     * @param query
     * @param model
     * @return
     */
//    protected abstract PageDTO query(BaseQuery query);
 
}
