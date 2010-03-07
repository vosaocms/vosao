package org.vosao.servlet;

import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bradmcevoy.http.FileItemWrapper;
import com.bradmcevoy.http.RequestParseException;
import com.bradmcevoy.http.ServletRequest;

public class WebdavServletRequest extends ServletRequest {

	private static final Log logger = LogFactory
			.getLog(WebdavServletRequest.class);
	
	private HttpServletRequest request;
	
	public WebdavServletRequest(HttpServletRequest aRequest) {
		super(aRequest);
		request = aRequest;
	}
	
	@Override
	public void parseRequestParameters(Map<String, String> params,
			Map<String, com.bradmcevoy.http.FileItem> files) throws RequestParseException {
        try {
            if (isMultiPart()) {
                ServletFileUpload upload = new ServletFileUpload();
                List<FileItem> items = upload.parseRequest(request);
                parseQueryString(params);
                for (FileItem item : items) {
                    if (item.isFormField()) {
                        params.put(item.getFieldName(), item.getString());
                    } else {
                        files.put(item.getFieldName(), new FileItemWrapper(item) );
                    }
                }
            } else {
                for (Enumeration en = request.getParameterNames(); 
                		en.hasMoreElements();) {
                    String nm = (String) en.nextElement();
                    String val = request.getParameter(nm);
                    logger.debug("..param: " +nm + " = " + val);
                    params.put(nm,val);
                }
            }
        } catch (FileUploadException ex) {
            throw new RequestParseException("FileUploadException",ex);
        } catch (Throwable ex) {
            throw new RequestParseException(ex.getMessage(),ex);
        }
	}

	private void parseQueryString(Map<String, String> map) {
        String qs = request.getQueryString();
        parseQueryString(map, qs);
    }

}
