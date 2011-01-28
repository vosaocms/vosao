/**
 * Vosao CMS. Simple CMS for Google App Engine.
 * 
 * Copyright (C) 2009-2010 Vosao development team.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * email: vosao.dev@gmail.com
 */

package org.vosao.plugins.rssatom;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.VelocityContext;
import org.vosao.business.Business;
import org.vosao.common.VosaoContext;
import org.vosao.entity.ConfigEntity;
import org.vosao.entity.PageEntity;
import org.vosao.entity.PluginEntity;
import org.vosao.entity.helper.PageHelper;
import org.vosao.utils.ListUtil;

/**
 * Servlet for generating feeds.
 * URL: /_ah/plugin/rssatom/feed?format=rss or atom
 * 
 * @author Alexander Oleynik
 *
 */
public class RssatomServlet extends HttpServlet {

	protected static final Log logger = LogFactory.getLog(RssatomServlet.class);

	private Business business;
	
	public RssatomServlet(Business aBusiness) {
		business = aBusiness;
	}
	
	private Business getBusiness() {
		return business;
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String format = request.getParameter("format");
		format = format == null ? "rss" : format;
		ConfigEntity config = getBusiness().getConfigBusiness().getConfig();
		PluginEntity plugin = getBusiness().getDao().getPluginDao().getByName(
				"rssatom");
		RssatomConfig rssatomConfig = new RssatomConfig(plugin);
		String template = "";
		if (format.equals("rss")) {
			template = rssatomConfig.getRssTemplate();
			response.setContentType("application/rss+xml");
		}
		if (format.equals("atom")) {
			template = rssatomConfig.getAtomTemplate();
			response.setContentType("application/atom+xml");
		}
		VelocityContext context = new VelocityContext();
		getBusiness().getPageBusiness().addVelocityTools(context);
		context.put("config", config);
		context.put("service", getBusiness().getPageBusiness()
				.getVelocityService());
		context.put("plugin", getBusiness().getPageBusiness()
				.getVelocityPluginService().getPlugins());
		context.put("messages", getBusiness().getMessageBusiness().getBundle(
				VosaoContext.getInstance().getLanguage()));
		context.put("rssatomConfig", rssatomConfig);
		context.put("pages", getPages(plugin, rssatomConfig));
		context.put("rss", new RssTool(getBusiness()));
		String feed = getBusiness().getSystemService().render(template, 
				context);
    	response.setCharacterEncoding("UTF-8");
		response.getWriter().write(feed);
	}

	private List<PageEntity> getPages(PluginEntity plugin, 
			RssatomConfig rssatomConfig) { 
		List<PageEntity> result = new ArrayList<PageEntity>();
		for (String pageURL : rssatomConfig.getPagesList()) {
			addPages(getBusiness().getPageBusiness().getByUrl(pageURL), result);
		}
		Collections.sort(result, PageHelper.PUBLISH_DATE);
		return ListUtil.slice(result, 0, rssatomConfig.getItems());
	}
	
	private void addPages(PageEntity page, List<PageEntity> result) {
		if (page != null) {
			for (PageEntity child : getBusiness().getPageBusiness()
					.getByParent(page.getFriendlyURL())) {
				if (child.isForInternalUse() || !child.isPublished()) {
					continue;
				}
				result.add(child);
				addPages(child, result);
			}
		}
	}
	
	
	
}
