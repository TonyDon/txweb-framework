/*
 * @(#)BaseAction.java 2013-6-12
 * 
 * Copy Right@ uuola
 */

package com.uuola.txweb.framework.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.uuola.commons.StringUtil;
import com.uuola.commons.constant.CST_CHAR;
import com.uuola.commons.constant.HTTP_STATUS_CODE;
import com.uuola.commons.exception.BusinessException;
import com.uuola.commons.exception.BusinessExceptionMessageProvider;
import com.uuola.txweb.framework.action.methods.UpdateCallbackHandler;
import com.uuola.txweb.framework.dto.ValidateDTO;
import com.uuola.txweb.framework.utils.TxAssert;
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
     * 存放 validate校验错误信息
     */
    protected static final String ERRORS_ATTR = "errors";

    protected static final String UPDATE_RESULT_ATTR = "updateResult";

    /**
     * 根据包路径和Action类构建视图名路径，如： com.uuola.txcms.portal.user.action.UserInfoAction
     * 转为 ：
     * com/uuola/txcms/portal/user/${actionPrefixName}-${methodName/otherName}
     */
    protected String viewPrefixName;

    public BaseAction() {
        String viewPath = StringUtil.replace(getPackagePath(), "/action", CST_CHAR.STR_EMPTY);
        this.viewPrefixName = viewPath.concat(CST_CHAR.STR_SLASH).concat(getActionPrefixName())
                .concat(CST_CHAR.STR_LINE);
        if (log.isInfoEnabled()) {
            log.info("viewPrefixName:" + this.viewPrefixName);
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
     * 得到action名称前缀 eg : DemoHelloAction -> demoHello
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
        TxAssert.hasLength(packageName);
        return StringUtil.replace(packageName, CST_CHAR.STR_DOT, CST_CHAR.STR_SLASH);
    }

    /**
     * 返回视图路径名称.<br/>
     * packageDirPath + '/' + ActionPrefixName + '-' + methodName
     * 
     * @param methodName
     * @return
     */
    protected String getViewName(String methodName) {
        return viewPrefixName.concat(methodName);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    protected String doShow(@PathVariable("id")
    Serializable id, Model model) {
        Object obj = show(id, model);
        if (null != obj) {
            String objAttrName = StringUtils.uncapitalize(obj.getClass().getSimpleName());
            model.addAttribute(objAttrName, obj);
        }
        return getViewName("show");
    }

    /**
     * 展示或获取记录<br/>
     * HTTP GET, with id, url: /path/1234
     */
    protected Object show(Serializable id, Model model) {
        return null;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    protected void doDelete(@PathVariable("id")
    Serializable id, ServletWebRequest webRequest, Model model) {
        Integer num = delete(id);
        if (num == null) {
            webRequest.getResponse().setStatus(HTTP_STATUS_CODE.SC_NOT_FOUND);
        }
        model.addAttribute("num", num);
    }

    /**
     * 返回影响记录数<br/>
     * HTTP POST /path/123 ; _method="delete"
     * 
     * @param id
     * @return
     */
    protected Integer delete(Serializable id) {
        return null;
    }

    /**
     * 更新操作模版方法封装
     * 
     * @param webRequest
     * @param clientDTO
     * @param handler
     * @return
     */
    protected <T> ModelAndView executeUpdate(ServletWebRequest webRequest, ValidateDTO clientDTO, UpdateCallbackHandler<T> handler) {
        ModelAndView model = new ModelAndView();
        List<String> errors = new ArrayList<String>();
        // 需要验证客户端DTO，但没有通过则不进行后续业务处理
        if (clientDTO.isNeedValid() && !clientDTO.validatePass()) {
            errors.addAll(getErrors(clientDTO));
        } else {
            try {
                T result = handler.doUpdate(clientDTO);
                if (null != result) {
                    model.getModel().put(UPDATE_RESULT_ATTR, result);
                }
            } catch (BusinessException be) {
                errors.add(BusinessExceptionMessageProvider.getMessage(be));
                log.error("", be);
            } catch (Exception e) {
                errors.add(ExceptionUtils.getFullStackTrace(e));
                log.error("", e);
            }
        }
        if (!errors.isEmpty()) {
            model.addObject(ERRORS_ATTR, errors);
            webRequest.getResponse().setStatus(HTTP_STATUS_CODE.SC_BZ_ERROR);
        }
        return model;
    }

}
