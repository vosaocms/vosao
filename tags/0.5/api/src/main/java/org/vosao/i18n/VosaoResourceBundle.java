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

package org.vosao.i18n;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.business.Business;
import org.vosao.business.plugin.PluginEntryPoint;
import org.vosao.common.VosaoContext;
import org.vosao.entity.PluginEntity;

/**
 * 
 * @author Alexander Oleynik
 * 
 */
public class VosaoResourceBundle extends ResourceBundle {

	private static final String BUNDLE_NAME = "org.vosao.resources.messages";

	private static final Log logger = LogFactory.getLog(
			VosaoResourceBundle.class);
	
	private Locale locale;

	public VosaoResourceBundle(Locale aLocale) {
		locale = aLocale;
	}

	@Override
	public Enumeration<String> getKeys() {
		List<String> result = new ArrayList<String>();
		for (ResourceBundle bundle : getResourceBundles()) {
			result.addAll(Collections.list(bundle.getKeys()));
		}
		return Collections.enumeration(result);
	}

	private Business getBusiness() {
		return VosaoContext.getInstance().getBusiness();
	}

	@Override
	protected Object handleGetObject(String key) {
		Object result = null;
		for (ResourceBundle bundle : getResourceBundles()) {
			try {
				result = bundle.getObject(key);
			}
			catch (MissingResourceException e) {
			}
			if (result != null) {
				return result;
			}
		}
		return result;
	}

	private List<ResourceBundle> getResourceBundles() {
		List<ResourceBundle> result = new ArrayList<ResourceBundle>();
		for (PluginEntity plugin : getBusiness().getDao().getPluginDao()
				.selectEnabled()) {
			PluginEntryPoint entryPoint = getBusiness().getPluginBusiness()
					.getEntryPoint(plugin);
			if (entryPoint.getBundleName() != null) {
				result.add(ResourceBundle.getBundle(entryPoint.getBundleName(),
						locale, 
						getBusiness().getPluginBusiness().getClassLoader(plugin)));
			}
		}
		result.add(ResourceBundle.getBundle(BUNDLE_NAME, locale));
		return result;
	}
	
}
