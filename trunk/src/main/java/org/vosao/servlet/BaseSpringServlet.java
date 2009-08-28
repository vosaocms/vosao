package org.vosao.servlet;

import javax.servlet.http.HttpServlet;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.vosao.business.Business;
import org.vosao.dao.Dao;

/**
 * Base spring servlet.
 * 
 * @author Aleksandr Oleynik
 */
public class BaseSpringServlet extends HttpServlet {

	/**
	 * Default constructor.
	 */
	public BaseSpringServlet() {
		super();
	}

	/**
	 * Getter for business Spring bean.
	 * 
	 * @return Business bean.
	 */
	public Business getBusiness() {
		WebApplicationContext wac = WebApplicationContextUtils
				.getRequiredWebApplicationContext(getServletContext());
		return (Business) wac.getBean("business");
	}

	/**
	 * Getter for dao Spring bean.
	 * 
	 * @return Dao bean.
	 */
	public Dao getDao() {
		WebApplicationContext wac = WebApplicationContextUtils
				.getRequiredWebApplicationContext(getServletContext());
		return (Dao) wac.getBean("dao");
	}

}
