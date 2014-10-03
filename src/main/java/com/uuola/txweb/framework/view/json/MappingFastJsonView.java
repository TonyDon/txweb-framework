/*
 *  txdnet.cn tonydon
 * 
 */
package com.uuola.txweb.framework.view.json;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.view.AbstractView;

import com.uuola.commons.JsonUtil;
import com.uuola.commons.constant.CST_ENCODING;

/**
 * MVC视图JSON转换
 * @author txdnet
 */
public class MappingFastJsonView extends AbstractView {

    public static final String DEFAULT_CONTENT_TYPE = "application/json";
    private boolean disableCaching = true;
    private String encoding = CST_ENCODING.UTF8;
    private boolean extractValueFromSingleKeyModel = false;
    private Set<String> modelKeys;

    public MappingFastJsonView() {
        setContentType(DEFAULT_CONTENT_TYPE);
        setExposePathVariables(false);
    }

    @Override
    protected void prepareResponse(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType(getContentType());
        response.setCharacterEncoding(this.getEncoding());
        if (this.isDisableCaching()) {
            response.addHeader("Pragma", "no-cache");
            response.addHeader("Cache-Control", "no-cache, no-store, max-age=0");
            response.addDateHeader("Expires", 1L);
        }
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Object value = filterModel(model);
        JsonUtil.toJSONString(value, response.getOutputStream(), this.getEncoding());
    }

    protected Object filterModel(Map<String, Object> model) {
        Map<String, Object> result = new HashMap<String, Object>(model.size());
        Set<String> renderedAttributes = (!CollectionUtils.isEmpty(this.modelKeys) ? this.modelKeys : model.keySet());
        for (Map.Entry<String, Object> entry : model.entrySet()) {
            if (!(entry.getValue() instanceof BindingResult) && renderedAttributes.contains(entry.getKey())) {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        return (this.isExtractValueFromSingleKeyModel() && result.size() == 1 ? result.values().iterator().next() : result);
    }

    /**
     * @return the disableCaching
     */
    public boolean isDisableCaching() {
        return disableCaching;
    }

    /**
     * @return the extractValueFromSingleKeyModel
     */
    public boolean isExtractValueFromSingleKeyModel() {
        return extractValueFromSingleKeyModel;
    }

    /**
     * @param extractValueFromSingleKeyModel the extractValueFromSingleKeyModel
     * to set
     */
    public void setExtractValueFromSingleKeyModel(boolean extractValueFromSingleKeyModel) {
        this.extractValueFromSingleKeyModel = extractValueFromSingleKeyModel;
    }

    /**
     * @param disableCaching the disableCaching to set
     */
    public void setDisableCaching(boolean disableCaching) {
        this.disableCaching = disableCaching;
    }

    /**
     * @return the encoding
     */
    public String getEncoding() {
        return encoding;
    }

    /**
     * @param encoding the encoding to set
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    /**
     * Set the attribute in the model that should be rendered by this view. When
     * set, all other model attributes will be ignored.
     */
    public void setModelKey(String modelKey) {
        this.modelKeys = Collections.singleton(modelKey);
    }

    /**
     * Set the attributes in the model that should be rendered by this view.
     * When set, all other model attributes will be ignored.
     */
    public void setModelKeys(Set<String> modelKeys) {
        this.modelKeys = modelKeys;
    }

    /**
     * Return the attributes in the model that should be rendered by this view.
     */
    public Set<String> getModelKeys() {
        return this.modelKeys;
    }
}
