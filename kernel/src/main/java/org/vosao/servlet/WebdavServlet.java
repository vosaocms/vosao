package org.vosao.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bradmcevoy.http.HttpManager;
import com.bradmcevoy.http.Request;
import com.bradmcevoy.http.Response;
import com.bradmcevoy.http.ServletResponse;

public class WebdavServlet extends BaseSpringServlet {
	    
	    ServletConfig config;
	    HttpManager httpManager;
	    
	    private static final ThreadLocal<HttpServletRequest> originalRequest = 
	    		new ThreadLocal<HttpServletRequest>();
	    private static final ThreadLocal<HttpServletResponse> originalResponse = 
	    		new ThreadLocal<HttpServletResponse>();

	    public static HttpServletRequest request() {
	        return originalRequest.get();
	    }
	    
	    public static HttpServletResponse response() {
	        return originalResponse.get();
	    }
	    
	    public static void forward(String url) {
	        try {
	            request().getRequestDispatcher(url).forward(originalRequest.get(),
	            		originalResponse.get());
	        } catch (IOException ex) {
	            throw new RuntimeException(ex);
	        } catch (ServletException ex) {
	            throw new RuntimeException(ex);
	        }
	    }
	    
	    public void init(ServletConfig config) throws ServletException {
	        try {
	            this.config = config;
	            httpManager = (HttpManager) getSpringBean("milton.http.manager");
	        } catch (Throwable ex) {
	            logger.error("Exception starting milton servlet " + ex.getMessage());
	            throw new RuntimeException(ex);
	        }        
	    }
	    
	    public void service(javax.servlet.ServletRequest servletRequest, javax.servlet.ServletResponse servletResponse) throws ServletException, IOException {
	        HttpServletRequest req = (HttpServletRequest) servletRequest;
	        HttpServletResponse resp = (HttpServletResponse) servletResponse;
	        try {
	            originalRequest.set(req);
	            originalResponse.set(resp);
	            Request request = new WebdavServletRequest(req);
	            Response response = new ServletResponse(resp);
	            httpManager.process(request, response);
	        } finally {
	            originalRequest.remove();
	            originalResponse.remove();
	            servletResponse.getOutputStream().flush();            
	            servletResponse.flushBuffer();
	        }
	    }

	    public String getServletInfo() {
	        return "WebdavServlet";
	    }

	    public ServletConfig getServletConfig() {
	        return config;
	    }

	    public void destroy() {
	        
	    }
	}


