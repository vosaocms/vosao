package org.vosao.business.impl;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.vosao.business.ConfigBusiness;
import org.vosao.business.PageBusiness;
import org.vosao.business.decorators.TreeItemDecorator;
import org.vosao.entity.PageEntity;
import org.vosao.entity.TemplateEntity;
import org.vosao.velocity.VelocityService;
import org.vosao.velocity.impl.VelocityServiceImpl;

public class PageBusinessImpl extends AbstractBusinessImpl 
	implements PageBusiness {

	VelocityEngine ve;
	ConfigBusiness configBusiness;
	VelocityService velocityService;

	public void init() throws Exception {
		ve = new VelocityEngine();
		ve.init();
		velocityService = new VelocityServiceImpl(getDao());
	}
	
	@Override
	public TreeItemDecorator<PageEntity> getTree(final List<PageEntity> pages) {
		Map<String, TreeItemDecorator<PageEntity>> buf = 
				new HashMap<String, TreeItemDecorator<PageEntity>>();
		for (PageEntity page : pages) {
			buf.put(page.getId(), new TreeItemDecorator<PageEntity>(page, null));
		}
		TreeItemDecorator<PageEntity> root = null;
		for (String id : buf.keySet()) {
			TreeItemDecorator<PageEntity> page = buf.get(id);
			if (page.getEntity().getParent() == null) {
				root = page;
			}
			else {
				TreeItemDecorator<PageEntity> parent = buf.get(page.getEntity()
						.getParent());
				if (parent != null) {
					parent.getChildren().add(page);
					page.setParent(parent);
				}
			}
		}
		return root;
	}

	@Override
	public String render(PageEntity page) {
		if (page.getTemplate() != null) {
			TemplateEntity template = getDao().getTemplateDao().getById(
					page.getTemplate());
			VelocityContext context = new VelocityContext();
			context.put("page", page);
			Map<String, String> config = getDao().getConfigDao().getConfig();
			context.put("config", config);
			context.put("service", getVelocityService());
			StringWriter wr = new StringWriter();
			String log = null;
			try {
				ve.evaluate(context, wr, log, template.getContent());
				String resultPage = wr.toString();
				return pagePostProcess(resultPage);
			} catch (ParseErrorException e) {
				return e.toString();
			} catch (MethodInvocationException e) {
				return e.toString();
			} catch (ResourceNotFoundException e) {
				return e.toString();
			} catch (IOException e) {
				return e.toString();
			}
		}
		else {
			return pagePostProcess(page.getContent());
		}
	}
	
	private static String getGoogleAnalyticsCode(final String id) { 
		return  
		"<!-- Google Analytics -->\n"
		+ "<script type=\"text/javascript\">\n"
	    + "var gaJsHost = ((\"https:\" == document.location.protocol) ? \"https://ssl.\" : \"http://www.\");\n"
        + "document.write(unescape(\"%3Cscript src='\" + gaJsHost + \"google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E\"));\n"
	    + "</script>\n"
	    + "<script type=\"text/javascript\">\n"
	    + "var pageTracker = _gat._getTracker(\"" + id +"\");\n"
	    + "pageTracker._trackPageview();\n"
	    + "</script>\n"
	    + "</html>";
	}
	
	private String pagePostProcess(final String page) {
		if (!StringUtils.isEmpty(getConfigBusiness().getGoogleAnalyticsId())) {
			String id = getConfigBusiness().getGoogleAnalyticsId();
			String htmlTag = "</html>";
			if (page.indexOf("</HTML>") != -1) {
				htmlTag = "</HTML>";
			}
			return StringUtils.replace(page, htmlTag, 
					getGoogleAnalyticsCode(id));
		}
		else {
			return page;
		}
	}
	
	@Override
	public List<String> validateBeforeUpdate(final PageEntity page) {
		List<String> errors = new ArrayList<String>();
		if (page.getId() == null) {
			PageEntity myPage = getDao().getPageDao().getByUrl(
					page.getFriendlyURL());
			if (myPage != null) {
				errors.add("Page with such friendly URL already exists");
			}
		}
		if (StringUtils.isEmpty(page.getFriendlyURL())) {
			errors.add("Friendly URL is empty");
		}
		if (!page.getFriendlyURL().equals("/") 
			&& StringUtils.isEmpty(page.getPageFriendlyURL())) {
			errors.add("Friendly URL is empty");
		}
		if (StringUtils.isEmpty(page.getTitle())) {
			errors.add("Title is empty");
		}
		if (page.getContent() == null) {
			errors.add("Content is empty");
		}
		return errors;
	}

	@Override
	public TreeItemDecorator<PageEntity> getTree() {
		return getTree(getDao().getPageDao().select());
	}

	@Override
	public ConfigBusiness getConfigBusiness() {
		return configBusiness;
	}

	@Override
	public void setConfigBusiness(ConfigBusiness bean) {
		configBusiness = bean;		
	}

	private VelocityService getVelocityService() {
        return velocityService;
	}
	
}
