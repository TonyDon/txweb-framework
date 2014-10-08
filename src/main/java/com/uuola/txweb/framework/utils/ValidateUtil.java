/*
 *  txdnet.cn tonydon
 * 
 */
package com.uuola.txweb.framework.utils;

import com.uuola.commons.CollectionUtil;
import com.uuola.commons.StringUtil;
import com.uuola.txweb.framework.dto.ValidateDTO;
import java.util.ArrayList;
import java.util.Collections;
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
        if (CollectionUtil.isNotEmpty(validSet)) {
            List<String> errors = new ArrayList<String>(validSet.size());
            for (ConstraintViolation<ValidateDTO> cv : validSet) {
                errors.add(formatMessage(cv));
            }
            return errors;
        }
        return Collections.emptyList();
    }
    
    /**
     * 格式化验证器返回的消息文本<br/>
     * 如果消息文本中含 %s,则将无效对象的toString()内容进行替换
     * @param cv
     * @return
     */
    public static <T> String formatMessage(ConstraintViolation<T> cv) {
        String cvMessage = cv.getMessage();
        Object invalidValue = cv.getInvalidValue();
        if (cvMessage.contains("%s") && null != invalidValue) {
            cvMessage = StringUtil.replace(cvMessage, "%s", invalidValue.toString());
        }
        return cvMessage;
    }
}
