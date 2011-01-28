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

package org.vosao.plugins.include;

import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.vosao.business.Business;
import org.vosao.business.vo.PluginPropertyVO;
import org.vosao.entity.PluginEntity;
import org.vosao.utils.ParamUtil;
import org.vosao.velocity.plugin.AbstractVelocityPlugin;

public class IncludeVelocityPlugin extends AbstractVelocityPlugin {

	private static final String PLUGIN_NAME = "include";
	private static final String PARAM_EXPIRATION_TIME_SECS = "expiration_time_secs";
	private static final int DEFAULT_EXPIRATION_TIME_SECS = 3600;
	
	private static final Log logger = LogFactory.getLog(IncludeVelocityPlugin.class);
	private static final Map<String,Include> cache = new HashMap<String,Include>();
	
	public IncludeVelocityPlugin(Business business) {
		setBusiness(business);
	}
	
	private synchronized Include loadInclude(String url, long expiration_time_secs)
	{
		Include doc = cache.get(url);
		if (doc == null || new Date().getTime() > doc.getFetchTime().getTime()
				+ expiration_time_secs*1000) {
			try	{
				doc = Include.load(new URL(url), getBusiness().getLanguage());
				cache.put(url, doc);
			}
			catch (Exception e)	{
				logger.error(String.format("%s: Unable to load include: %s", 
						PLUGIN_NAME, e.toString(), e));
			}
		}
		return doc;
	}
	
	public String include(String url, int expiration_time_secs)	{
		Include doc = loadInclude(url, expiration_time_secs);
		if (doc != null)
			return doc.getContent();
		else
			return "";
	}
	
	public String include(String url)
	{
		Map<String, Object> cfg = getConfig(getDao().getPluginDao().getByName(
				PLUGIN_NAME));
		
		return include(url, (Integer)cfg.get(PARAM_EXPIRATION_TIME_SECS));
	}
	
	private Map<String, Object> getConfig(PluginEntity plugin)
	{
		Map<String, Object> result = new HashMap<String, Object>();
		if (StringUtils.isEmpty(plugin.getConfigData())) {
			Map<String, PluginPropertyVO> map = getBusiness().getPluginBusiness()
					.getPropertiesMap(plugin);
			
			PluginPropertyVO exp = map.get(PARAM_EXPIRATION_TIME_SECS);
			if (exp != null)
				result.put(PARAM_EXPIRATION_TIME_SECS, 
						ParamUtil.getInteger(exp.getDefaultValue(), 
								DEFAULT_EXPIRATION_TIME_SECS));

			return result;
		}

		try {
			Document doc = DocumentHelper.parseText(plugin.getConfigData());
			Element root = doc.getRootElement();
			result.put(PARAM_EXPIRATION_TIME_SECS, 
					ParamUtil.getInteger(
							root.elementText(PARAM_EXPIRATION_TIME_SECS), 
							DEFAULT_EXPIRATION_TIME_SECS));
		}
		catch (DocumentException e)	{
			logger.error(String.format("%s: Unable to load configuration: %s", 
					PLUGIN_NAME, e.toString(), e));
		}
		return result;
	}

}
