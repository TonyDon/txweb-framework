/*
 *  txdnet.cn tonydon
 * 
 */
package com.uuola.txweb.framework.view.xml;

import java.io.ByteArrayOutputStream;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.oxm.Marshaller;
import javax.xml.transform.stream.StreamResult;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.view.xml.MarshallingView;

/**
 *
 * @author txdnet
 */
public class XmlView extends MarshallingView {

    private Marshaller marshaller;
    
    @Override
    protected void renderMergedOutputModel(Map<String, Object> model,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Object toBeMarshalled = locateToBeMarshalled(model);
        if (toBeMarshalled == null) {
            throw new ServletException("Unable to locate object to be marshalled in model: " + model);
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream(2048);
        getMarshaller().marshal(toBeMarshalled, new StreamResult(bos));
        response.setContentType(getContentType());
        response.setContentLength(bos.size());

        FileCopyUtils.copy(bos.toByteArray(), response.getOutputStream());
    }

    /**
     * @return the marshaller
     */
    public Marshaller getMarshaller() {
        return marshaller;
    }

    /**
     * @param marshaller the marshaller to set
     */
    @Override
    public void setMarshaller(Marshaller marshaller) {
        this.marshaller = marshaller;
        super.setMarshaller(marshaller);
    }
}
