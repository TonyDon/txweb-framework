
package com.uuola.txweb.framework.dto;

import java.io.Serializable;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.apache.commons.collections.CollectionUtils;

import com.uuola.txweb.framework.utils.ValidateUtil;

/**
 * bean dto validate验证方法封装<br/>
 * 默认开启DTO字段验证<br/>
 * 对BEAN标注字段全部验证， 调用validatePass()
 * 对BEAN标注某一个字段进行验证， 调用validatePass(String propertyName)
 * @author tangxiaodong
 */
public abstract class ValidateDTO implements Serializable {

    private static final long serialVersionUID = 2713308806270579105L;

    private transient Set<ConstraintViolation<ValidateDTO>> validSet;

    /**
     * 默认验证通过
     */
    private boolean validPass = true;
    
    /**
     * 默认需要验证
     */
    private boolean isNeedValid = true; 

    /**
     * 验证dto所有字段方法，通过返回true 否则返回false
     * 
     * @return
     */
    public boolean validatePass() {
        validSet = ValidateUtil.validate(this);
        validPass = validSet.isEmpty();
        return validPass;
    }

    /**
     * 验证某个字段方法，通过返回true 否则返回false
     * 
     * @return
     */
    public boolean validatePass(String propertyName) {
        if (CollectionUtils.isEmpty(validSet)) {
            validSet = ValidateUtil.validateProperty(this, propertyName);
        } else {
            validSet.addAll(ValidateUtil.validateProperty(this, propertyName));
        }
        validPass = validSet.isEmpty();
        return validPass;
    }

    /**
     * @return the validSet
     */
    public Set<ConstraintViolation<ValidateDTO>> getValidSet() {
        return validSet;
    }

    public boolean isValidPass() {
        return validPass;
    }

    
    public boolean isNeedValid() {
        return isNeedValid;
    }
    
    /**
     * 关闭DTO验证
     */
    public void closeValid(){
        this.isNeedValid = false;
    }
    
    /**
     * 开启DTO验证
     */
    public void openValid(){
        this.isNeedValid = true;
    }
}
