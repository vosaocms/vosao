package org.vosao.plugins.sitemap;

import org.apache.velocity.VelocityContext;
import org.vosao.business.decorators.TreeItemDecorator;
import org.vosao.entity.PageEntity;
import org.vosao.utils.StreamUtil;
import org.vosao.velocity.plugin.AbstractVelocityPlugin;

public class SitemapVelocityPlugin extends AbstractVelocityPlugin {

	public String render() {
		try {
			TreeItemDecorator<PageEntity> root = getBusiness().getPageBusiness()
				.getTree();
			String template = StreamUtil.getTextResource(
				SitemapVelocityPlugin.class.getClassLoader(), 
				"org/vosao/plugins/sitemap/sitemap.vm");
			VelocityContext context = new VelocityContext();
			context.put("root", root);
			return getBusiness().getSystemService().render(template, context);
		}
		catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
	
}
