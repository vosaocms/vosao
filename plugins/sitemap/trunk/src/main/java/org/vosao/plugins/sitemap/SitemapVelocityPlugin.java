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

package org.vosao.plugins.sitemap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.VelocityContext;
import org.vosao.business.Business;
import org.vosao.business.decorators.TreeItemDecorator;
import org.vosao.common.VosaoContext;
import org.vosao.entity.PageEntity;
import org.vosao.entity.PluginEntity;
import org.vosao.entity.helper.PluginHelper;
import org.vosao.entity.helper.PluginParameter;
import org.vosao.enums.PageState;
import org.vosao.utils.StrUtil;
import org.vosao.utils.StreamUtil;
import org.vosao.velocity.plugin.AbstractVelocityPlugin;

public class SitemapVelocityPlugin extends AbstractVelocityPlugin {

	private static final Log logger = LogFactory.getLog(
			SitemapVelocityPlugin.class);

	public SitemapVelocityPlugin(Business business) {
		setBusiness(business);
	}
	
	private String renderSitemap(String templateUrl) {
		try {
			PluginEntity plugin = getDao().getPluginDao().getByName("sitemap");
			SitemapConfig config = getConfig(plugin);
			TreeItemDecorator<PageEntity> root = getBusiness().getPageBusiness()
					.getTree();
			filterExclude(root, config.getExclude());
			String template = StreamUtil.getTextResource(
				SitemapVelocityPlugin.class.getClassLoader(), 
				templateUrl);
			VelocityContext context = new VelocityContext();
			getBusiness().getPageBusiness().addVelocityTools(context);
			context.put("root", root);
			context.put("config", config);
			context.put("siteConfig", getDao().getConfigDao().getConfig());
			context.put("languageCode", getBusiness().getLanguage());
			return getBusiness().getSystemService().render(template, context);
		}
		catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
	
	public String render() {
		return renderSitemap("org/vosao/plugins/sitemap/sitemap.vm");
	}

	private void filterExclude(TreeItemDecorator<PageEntity> page,
			List<String> urls) {
		if (urls == null) {
			return;
		}
		List<TreeItemDecorator<PageEntity>> children = 
			new ArrayList<TreeItemDecorator<PageEntity>>();
		for (TreeItemDecorator<PageEntity> child : page.getChildren()) {
			boolean excluded = false;
			for (String url : urls) {
				if (child.getEntity().getFriendlyURL().equals(url)) {
					excluded = true;
					break;
				}
			}
			if (!excluded && child.getEntity().getState().equals(
					PageState.APPROVED)) {
				children.add(child);
				filterExclude(child, urls);
			}
		}
		page.setChildren(children);
	}
	
	private SitemapConfig getConfig(PluginEntity plugin) {
		Map<String, PluginParameter> params = PluginHelper.parseParameters(
				plugin);
		SitemapConfig result = new SitemapConfig();
		try {
			result.setLevel(params.get("level").getValueInteger());
		}
		catch (Exception e) {
			logger.error("level parameter: " + e.getMessage());
		}
		try {
			result.setExclude(StrUtil.fromCSV(params.get("exclude")
					.getValue()));
		}
		catch (Exception e) {
			logger.error("exclude parameter: " + e.getMessage());
		}
		return result;
	}

	public String renderXML() {
		return renderSitemap("org/vosao/plugins/sitemap/sitemapxml.vm");
	}
	
	public String renderBreadcrumbs(String url) {
		String language = getBusiness().getLanguage();
		StringBuilder b = new StringBuilder();
		StringBuilder path = new StringBuilder("/");
		PageEntity page = getBusiness().getPageBusiness().getByUrl(
				path.toString());
		b.append("<ul class=\"breadcrumbs\"><li><a href=\"/\">")
				.append(page.getLocalTitle(language))
				.append("</a></li>");
		if (!url.equals("/")) {
			path.deleteCharAt(0);
			for (String part : url.substring(1).split("/")) {
				path.append("/").append(part);
				b.append("<li>");
				page = getBusiness().getPageBusiness().getByUrl(path.toString());
				if (path.toString().equals(url)) {
					b.append(page.getLocalTitle(language));
				}
				else {
					b.append("<a href=\"").append(path).append("\">")
						.append(page.getLocalTitle(language))
						.append("</a>");
				}
				b.append("</li>");
			}
		}
		b.append("</ul>");
		return b.toString();
	}
	
}
