/*
 *  txdnet.cn tonydon
 * 
 */
package com.uuola.txweb.framework.utils;

import com.uuola.txweb.framework.dto.ValidateDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;

/**
 * Bean Validate 验证器
 *
 * @author tangxiaodong
 */
public class ValidateUtil {

    /**
     * 对bean的所有要求验证的字段进行验证，返回失败的校验集合
     *
     * @param bean
     * @return
     */
    public static Set<ConstraintViolation<ValidateDTO>> validate(ValidateDTO bean, Class<?>... groups) {
        return Validation.buildDefaultValidatorFactory().getValidator().validate(bean, groups);
    }

    /**
     * 对bean的某个字段进行校验，返回失败的校验集合
     *
     * @param bean
     * @param propertyName
     * @param groups
     * @return
     */
    public static Set<ConstraintViolation<ValidateDTO>> validateProperty(ValidateDTO bean, String propertyName, Class<?>... groups) {
        return Validation.buildDefaultValidatorFactory().getValidator().validateProperty(bean, propertyName, groups);
    }

    /**
     * 从校验集合中取出错误信息
     *
     * @param validSet
     * @return
     */
    public static List<String> getErrorList(Set<ConstraintViolation<ValidateDTO>> validSet) {
        List<String> errors = new ArrayList<String>();
        for (ConstraintViolation<?> cv : validSet) {
            errors.add(String.format(cv.getMessage(), cv.getInvalidValue()));
        }
        return errors;
    }
}
