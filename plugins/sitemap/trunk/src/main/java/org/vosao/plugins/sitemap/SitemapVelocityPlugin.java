package org.vosao.plugins.sitemap;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.VelocityContext;
import org.datanucleus.util.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.vosao.business.decorators.TreeItemDecorator;
import org.vosao.entity.PageEntity;
import org.vosao.entity.PluginEntity;
import org.vosao.utils.DateUtil;
import org.vosao.utils.StreamUtil;
import org.vosao.velocity.plugin.AbstractVelocityPlugin;

public class SitemapVelocityPlugin extends AbstractVelocityPlugin {

	private static final Log logger = LogFactory.getLog(
			SitemapVelocityPlugin.class);

	public String render() {
		try {
			PluginEntity plugin = getDao().getPluginDao().getByName("sitemap");
			TreeItemDecorator<PageEntity> root = getBusiness().getPageBusiness()
				.getTree();
			String template = StreamUtil.getTextResource(
				SitemapVelocityPlugin.class.getClassLoader(), 
				"org/vosao/plugins/sitemap/sitemap.vm");
			VelocityContext context = new VelocityContext();
			context.put("root", root);
			context.put("config", getConfig(plugin));
			return getBusiness().getSystemService().render(template, context);
		}
		catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
	
	private Map<String, Object> getConfig(PluginEntity plugin) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (StringUtils.isEmpty(plugin.getConfigData())) {
			return result;
		}
		try {
			Document doc = DocumentHelper.parseText(plugin.getConfigData());
			Element root = doc.getRootElement();
			result.put("title", root.elementText("title"));
			result.put("level", Integer.valueOf(root.elementText("level")));
			result.put("hint", root.elementText("hint"));
			result.put("showHint", Boolean.valueOf(root.elementText("showHint")));
			if (StringUtils.isEmpty(root.elementText("published"))) {
				result.put("published", new Date());
			}
			else {
				result.put("published", DateUtil.toDate(root.elementText(
						"published")));
			}
		}
		catch (DocumentException e) {
			logger.error("Sitemap plugin config " + e.getMessage());
		}
		catch (NumberFormatException e) {
			logger.error("Sitemap plugin config " + e.getMessage());
		}
		catch (ParseException e) {
			logger.error("Sitemap plugin config " + e.getMessage());
		}
		return result;
	}
	
}
