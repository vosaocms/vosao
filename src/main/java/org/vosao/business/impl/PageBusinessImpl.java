/**
 * Vosao CMS. Simple CMS for Google App Engine.
 * Copyright (C) 2009 Vosao development team
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * email: vosao.dev@gmail.com
 */

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
import org.vosao.business.impl.pagefilter.GoogleAnalyticsPageFilter;
import org.vosao.business.impl.pagefilter.JavaScriptPageFilter;
import org.vosao.business.impl.pagefilter.PageFilter;
import org.vosao.entity.ConfigEntity;
import org.vosao.entity.PageEntity;
import org.vosao.entity.TemplateEntity;
import org.vosao.velocity.VelocityPluginService;
import org.vosao.velocity.VelocityService;
import org.vosao.velocity.impl.VelocityPluginServiceImpl;
import org.vosao.velocity.impl.VelocityServiceImpl;

public class PageBusinessImpl extends AbstractBusinessImpl 
	implements PageBusiness {

	VelocityEngine ve;
	ConfigBusiness configBusiness;
	VelocityService velocityService;
	VelocityPluginService velocityPluginService;

	public void init() throws Exception {
		ve = new VelocityEngine();
		ve.init();
		velocityService = new VelocityServiceImpl(getDao());
		velocityPluginService = new VelocityPluginServiceImpl(getDao());
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
			VelocityContext context = createContext();
			PageRenderDecorator pageDecorator = new PageRenderDecorator(page, 
					getConfigBusiness(), this);
			context.put("page", pageDecorator);
			return pagePostProcess(render(template.getContent(), context));
		}
		else {
			return pagePostProcess(page.getContent());
		}
	}
	
	public VelocityContext createContext() {
		VelocityContext context = new VelocityContext();
		ConfigEntity configEntity = getConfigBusiness().getConfig();
		context.put("config", configEntity.getConfigMap());
		context.put("service", getVelocityService());
		context.put("plugin", getVelocityPluginService());
		return context;
	}
	
	private String pagePostProcess(final String page) {
		List<PageFilter> filters = createFilters();
		String result = page;
		for (PageFilter filter : filters) {
			result = filter.apply(result);
		}
		return result;
	}
	
	private List<PageFilter> createFilters() {
		List<PageFilter> result = new ArrayList<PageFilter>();
		result.add(new GoogleAnalyticsPageFilter(configBusiness, this));
		result.add(new JavaScriptPageFilter(configBusiness, this));
		return result;
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

	public VelocityPluginService getVelocityPluginService() {
		return velocityPluginService;
	}

	@Override
	public String render(String template, VelocityContext context) {
		StringWriter wr = new StringWriter();
		String log = null;
		try {
			ve.evaluate(context, wr, log, template);
			return wr.toString();
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
	
}
