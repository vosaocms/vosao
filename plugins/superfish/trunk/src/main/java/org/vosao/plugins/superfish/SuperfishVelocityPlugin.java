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

package org.vosao.plugins.superfish;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.dom4j.DocumentException;
import org.vosao.business.Business;
import org.vosao.business.decorators.TreeItemDecorator;
import org.vosao.entity.PageEntity;
import org.vosao.entity.PluginEntity;
import org.vosao.utils.StreamUtil;
import org.vosao.velocity.plugin.AbstractVelocityPlugin;

/**
 * 
 * @author Alexander Oleynik
 *
 */
public class SuperfishVelocityPlugin extends AbstractVelocityPlugin {

	public SuperfishVelocityPlugin(Business aBusiness) {
		setBusiness(aBusiness);
	}
	
	public String render() {
		PluginEntity plugin = getDao().getPluginDao().getByName("superfish");
		try {
			SuperfishConfig config = SuperfishConfig.parse(plugin.getConfigData());
			TreeItemDecorator<PageEntity> root = getBusiness().getPageBusiness()
				.getTree();
			filterEnabled(root, config.getEnabledPages());
			String template = StreamUtil.getTextResource(
				SuperfishVelocityPlugin.class.getClassLoader(), 
				"org/vosao/plugins/superfish/menu.vm");
			VelocityContext context = new VelocityContext();
			context.put("root", root);
			context.put("config", config);
			return getBusiness().getSystemService().render(template, context);
		}
		catch (DocumentException e) {
			SuperfishConfig config = new SuperfishConfig();
			plugin.setConfigData(config.toXML());
			getDao().getPluginDao().save(plugin);
			return e.getMessage() + " Default config restored. Try again.";
		}
		catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
	
	private void filterEnabled(TreeItemDecorator<PageEntity> page, 
			final Map<String, Integer> enabledPages) {
		List<TreeItemDecorator<PageEntity>> children = 
				new ArrayList<TreeItemDecorator<PageEntity>>();
		for (TreeItemDecorator<PageEntity> child : page.getChildren()) {
			if (enabledPages.keySet().contains(child.getEntity().getFriendlyURL())) {
				filterEnabled(child, enabledPages);
				children.add(child);
			}
		}
		Collections.sort(children, new MenuComparator(enabledPages));
		page.setChildren(children);
	}
	
}
