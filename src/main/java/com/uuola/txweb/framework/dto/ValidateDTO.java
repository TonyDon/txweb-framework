
package com.uuola.txweb.framework.dto;

import java.io.Serializable;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.apache.commons.collections.CollectionUtils;

import com.uuola.txweb.framework.utils.ValidateUtil;

/**
 * bean 验证方法封装
 * 对BEAN标注字段全部验证， 调用validatePass()
 * 对BEAN标注某一个字段进行验证， 调用validatePass(String propertyName)
 * @author tangxiaodong
 */
public abstract class ValidateDTO implements Serializable {

    private static final long serialVersionUID = 2713308806270579105L;

    private transient Set<ConstraintViolation<ValidateDTO>> validSet;

    private boolean validPass = true;

    /**
     * 验证dto所有字段方法，通过返回true 否则返回false
     * 
     * @return
     */
    public boolean validatePass() {
        if (CollectionUtils.isEmpty(validSet)) {
            validSet = ValidateUtil.validate(this);
        } else {
            validSet.addAll(ValidateUtil.validate(this));
        }
        this.validPass = validSet.isEmpty();
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
        this.validPass = validSet.isEmpty();
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
}
