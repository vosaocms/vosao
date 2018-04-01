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

package org.vosao.business.impl.pagefilter.fragments;

import org.apache.commons.lang.StringUtils;
import org.vosao.business.Business;
import org.vosao.business.impl.pagefilter.ContentFragment;
import org.vosao.business.plugin.PluginEntryPoint;
import org.vosao.entity.PageEntity;
import org.vosao.entity.PluginEntity;

public class PluginHeadFragment	implements ContentFragment {

	@Override
	public String get(Business business, PageEntity page) {
		StringBuffer code = new StringBuffer(); 
		for (PluginEntity plugin : business.getDao().getPluginDao()
				.selectEnabled()) {
			PluginEntryPoint entryPoint = business.getPluginBusiness()
					.getEntryPoint(plugin);
			if (entryPoint == null) continue;
			if (entryPoint.isHeadInclude()) {
				entryPoint.setHeadInclude(false);
				if (!StringUtils.isEmpty(plugin.getPageHeader())) {
					code.append(plugin.getPageHeader());
				}
			}
			code.append(entryPoint.getHeadBeginInclude());
		}
		return code.toString();
	}

}
