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

package org.vosao.plugins.upload;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.business.plugin.AbstractPluginEntryPoint;
import org.vosao.entity.PluginEntity;
import org.vosao.entity.helper.PluginHelper;
import org.vosao.entity.helper.PluginParameter;

public class UploadEntryPoint extends AbstractPluginEntryPoint {

	private static final Log logger = LogFactory.getLog(
			UploadEntryPoint.class);

	@Override
	public String getBundleName() {
		return "org.vosao.plugins.upload.messages";
	}
	
	@Override
	public void init() {
		getServlets().put("post", new UploadServlet(getConfig()));
	}
	
	private static String getStringValue(PluginParameter param) {
		return StringUtils.isEmpty(param.getValue()) ? param.getDefaultValue()
				: param.getValue();
	}
	
	private UploadConfig getConfig() {
		PluginEntity plugin = getDao().getPluginDao().getByName("upload");
		Map<String, PluginParameter> params = PluginHelper.parseParameters(plugin);
		UploadConfig result = new UploadConfig();
		if (params.containsKey("folder")) {
			result.setFolder(getStringValue(params.get("folder")));
		}
		if (params.containsKey("secret_key")) {
			result.setSecretKey(getStringValue(params.get("secret_key")));
		}
		if (params.containsKey("use_key")) {
			result.setUseKey(params.get("use_key").getValueBoolean());
		}
		if (params.containsKey("subscriber_class")) {
			result.setSubscriberClass(getStringValue(params.get("subscriber_class")));
		}
		if (params.containsKey("max_size")) {
			result.setMaxSize(params.get("max_size").getValueInteger() * 1024);
		}
		return result;
	}

}
