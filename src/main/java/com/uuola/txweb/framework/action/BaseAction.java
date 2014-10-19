/*
 * @(#)BaseAction.java 2013-6-12
 * 
 * Copy Right@ uuola
 */

package com.uuola.txweb.framework.action;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import com.uuola.commons.StringUtil;
import com.uuola.commons.constant.CST_CHAR;
import com.uuola.commons.exception.Assert;
import com.uuola.commons.exception.BusinessException;
import com.uuola.txweb.framework.action.methods.QueryCallbackHandler;
import com.uuola.txweb.framework.action.methods.UpdateCallbackHandler;
import com.uuola.txweb.framework.dto.ValidateDTO;
import com.uuola.txweb.framework.query.BaseQuery;
import com.uuola.txweb.framework.utils.ValidateUtil;

/**
 * <pre>
 * 抽象Action类，子类名称要求以*Action命名，Action 类所在包路径以/action结束
 * @author tangxiaodong
 * 创建日期: 2013-6-12
 * </pre>
 */
public abstract class BaseAction {

    protected Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 根据包路径和Action类构建视图名路径，如： com.uuola.txcms.portal.user.action.UserInfoAction<br/>
     * 转为 ：<br/>
     * com/uuola/txcms/portal/user/${actionPrefixName}-${methodName/defineName}
     */
    private String viewPrefixPath;

    public BaseAction() {
        String viewPath = StringUtil.replace(getPackagePath(), "/action", CST_CHAR.STR_EMPTY);
        this.viewPrefixPath = viewPath.concat(CST_CHAR.STR_SLASH).concat(getActionPrefixName())
                .concat(CST_CHAR.STR_LINE);
        if (log.isInfoEnabled()) {
            log.info("viewPrefixPath:" + this.viewPrefixPath);
        }
    }

    /**
     * 如果使用<mvc:annotation-driven /> 则使用该方法初始化绑定
     * 
     * @param request
     * @param binder
     */
    // @InitBinder
    // protected void init(HttpServletRequest request, ServletRequestDataBinder
    // binder) {
    // binder.registerCustomEditor(Date.class, new MyDateEditor());
    // //initDataBinder(request, binder);
    // }

    /**
     * 将bean验证错误信息放入model属性
     * 
     * @param bean
     * @param model
     */
    protected List<String> getErrors(ValidateDTO bean) {
        return ValidateUtil.getErrorList(bean.getValidSet());
    }

    protected Long parseId(Serializable id) {
        return Long.valueOf((String) id);
    }

    /**
     * 得到action名称前缀 eg : DemoHelloAction -&gt; demoHello
     * 
     * @return
     */
    private String getActionPrefixName() {
        String className = this.getClass().getSimpleName();
        int actionIdx = className.indexOf("Action");
        if (actionIdx < 1) {
            throw new RuntimeException(this.getClass().getCanonicalName() + "-[The Class Name Must end with 'Action'!]");
        }
        return StringUtils.uncapitalize(className.substring(0, actionIdx));
    }

    /**
     * 转换包名称为目录路径<br/>
     * eg: com.uuola.txweb.action → com/uuola/txweb/action
     * 
     * @return
     */
    private String getPackagePath() {
        String packageName = this.getClass().getPackage().getName();
        Assert.hasLength(packageName);
        if (StringUtil.endNotWith(packageName, ".action")) {
            throw new RuntimeException(packageName + "-[Must End With '.action', The Path Of Action Package!]");
        }
        return StringUtil.replace(packageName, CST_CHAR.STR_DOT, CST_CHAR.STR_SLASH);
    }

    /**
     * 返回视图路径名称.<br/>
     * packageDirPath + '/' + ActionPrefixName + '-' + defineSuffixName
     * 
     * @param defineSuffixName
     * @return
     */
    protected String getViewName(String defineSuffixName) {
        return viewPrefixPath.concat(defineSuffixName);
    }
    
    /**
     * 定义包含视图名的ModelAndView
     * @param defineName
     * @return
     */
    protected ModelAndView makeModelView(String defineSuffixName){
        return new ModelAndView(getViewName(defineSuffixName));
    }
    
    /**
     * 设置视图名，并返回ModelAndView
     * @param mv
     * @param defineSuffixName
     * @return ModelAndView
     */
    protected ModelAndView assignViewName(ModelAndView mv, String defineSuffixName){
        mv.setViewName(getViewName(defineSuffixName));
        return mv;
    }


    /**
     * 更新操作模版方法封装
     * 
     * @param webRequest
     * @param clientDTO
     * @param handler
     * @return
     */
    protected <T> ModelAndView updateAction(ValidateDTO clientDTO, UpdateCallbackHandler<T> handler) {
        ModelAndView mv = new ModelAndView();
        List<String> errors = new ArrayList<String>();
        // 需要验证客户端DTO，但没有通过则不进行后续业务处理
        if (clientDTO.isNeedValid() && !clientDTO.validatePass()) {
            errors.addAll(getErrors(clientDTO));
            mv.addObject(IConstant.VALID_ERRORS_ATTR, errors);
        } else {
            T result = handler.doUpdate(clientDTO);
            mv.addObject(IConstant.UPDATE_RESULT_ATTR, result);
        }
        return mv;
    }
    
    
    /**
     * 封装查询过程
     * 
     * @param query 查询条件
     * @param handler 查询处理外部回调执行者
     * @return ModelAndView
     */
    protected <T> ModelAndView queryAction(BaseQuery query, QueryCallbackHandler<T> handler) {
        return queryAction(query,  null, handler);
    }
    
    /**
     * 封装查询过程
     * 
     * @param query 查询条件
     * @param handler 查询处理外部回调执行者
     * @param queryResultAttrName 给查询结果定一个属性名称到ModelAndView 若为空则使用结果对象的SimpleName作为属性名
     * @return ModelAndView
     */
    protected <T> ModelAndView queryAction(BaseQuery query, String queryResultAttrName, QueryCallbackHandler<T> handler) {
        ModelAndView mv = new ModelAndView();
        List<String> errors = new ArrayList<String>();
        if (query.isNeedValid() && !query.validatePass()) {
            errors.addAll(getErrors(query));
            mv.addObject(IConstant.VALID_ERRORS_ATTR, errors);
        } else {
            // 执行查询条件过滤方法，如非法值过滤，默认条件设置, 值转换等
            query.filter();
            // 执行分页查询记录行计算
            query.calcCurrRowIndex();
            // 查询返回结果对象如PageDTO
            T result = handler.doQuery(query);
            if(null == result){
                result = newQueryCallbackHandlerResult(handler);
                log.debug("Query Result Is Null! Try New Instance." );
            }
            if (StringUtil.isEmpty(queryResultAttrName)) {
                mv.addObject(StringUtils.uncapitalize(result.getClass().getSimpleName()), result);
            }else{
                mv.addObject(queryResultAttrName, result);
            }
        }
        return mv;
    }

    @SuppressWarnings("unchecked")
    private <T> T newQueryCallbackHandlerResult(QueryCallbackHandler<T> handler) {
        try {
            // 得到所有泛型接口类型
            Type[] types = handler.getClass().getGenericInterfaces();
            ParameterizedType pType = null;
            if (types.length == 1) {
                pType = (ParameterizedType) types[0];
            } else {
                for (Type type : types) {
                    // 判断泛型类型的声明类是否匹配
                    if (((ParameterizedType) type).getRawType() == QueryCallbackHandler.class) {
                        pType = (ParameterizedType) type;
                    }
                }
            }
            if (pType != null) {
                return ((Class<T>) pType.getActualTypeArguments()[0]).newInstance();
            }
            throw new RuntimeException("Don't Implement The QueryCallbackHandler<T> Interface !");
        } catch (Exception e) {
            throw new BusinessException(e, "QueryCallbackHandler<T> at Class<T>.newInstance() error!");
        }
    }
}
