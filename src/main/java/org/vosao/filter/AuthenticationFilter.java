package org.vosao.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.support.WebApplicationContextUtils;
import org.vosao.business.Business;


public class AuthenticationFilter implements Filter {
    
    public static final String ORIGINAL_VIEW_KEY = "originalViewKey";
    public static final String LOGIN_VIEW = "/login";
  
    FilterConfig config = null;
    ServletContext servletContext = null;
  
    public AuthenticationFilter() {
    }
  
    public void init(FilterConfig filterConfig) throws ServletException {
        config = filterConfig;
        servletContext = config.getServletContext();
    }
    
    public void doFilter(ServletRequest request, ServletResponse response, 
        FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        HttpServletResponse httpResponse = (HttpServletResponse)response;
        HttpSession session = httpRequest.getSession();
        if (!isLoggedIn(httpRequest)) {
          session.setAttribute(ORIGINAL_VIEW_KEY, httpRequest.getRequestURI());
          httpResponse.sendRedirect(httpRequest.getContextPath() + LOGIN_VIEW);
        }
        else {
          chain.doFilter(request, response);
        }
    }
    
    private boolean isLoggedIn(final HttpServletRequest request) {
        Business business = (Business)WebApplicationContextUtils
        	.getRequiredWebApplicationContext(servletContext)
        	.getBean("business");
    	return business.getUserPreferences(request).isLoggedIn();
    }
  
    public void destroy() {
    }

}
