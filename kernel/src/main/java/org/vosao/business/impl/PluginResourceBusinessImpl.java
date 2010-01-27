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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.business.PluginResourceBusiness;
import org.vosao.entity.PluginResourceEntity;

public class PluginResourceBusinessImpl extends AbstractBusinessImpl 
	implements PluginResourceBusiness {

	private static final Log logger = LogFactory.getLog(PluginResourceBusinessImpl.class);

	private static final String PLUGIN_RESOURCES = "pluginResources";
	private static final int CHUNK_SIZE = 720000;
	
	@Override
	public byte[] findResource(String name) {
		List<String> refs = getResourcesChunksKeyList();
		for (String ref : refs) {
			Map<String, PluginResourceEntity> map = (Map<String, PluginResourceEntity>) 
					getSystemService().getCache().get(ref);
			if (map.containsKey(name)) {
				return map.get(name).getContent();
			}
		}
		return null;
	}

	private List<String> getResourcesChunksKeyList() {
		if (!getSystemService().getCache().containsKey(
				PLUGIN_RESOURCES)) {
			initResources();
		}
		else {
			List<String> refs = (List<String>) getSystemService().getCache()
					.get(PLUGIN_RESOURCES);
			for (String ref : refs) {
				if (!getSystemService().getCache().containsKey(ref)) {
					initResources();
					break;
				}
			}
		}
		return (List<String>) getSystemService().getCache()
			.get(PLUGIN_RESOURCES);
	}

	private void initResources() {
		List<PluginResourceEntity> list = getDao().getPluginResourceDao().select();
		List<String> refs = new ArrayList<String>();
		int size = 0;
		int chunkCount = 0;
		Map<String, PluginResourceEntity> map = 
				new HashMap<String, PluginResourceEntity>();
		for (PluginResourceEntity r : list) {
			if (size < CHUNK_SIZE) {
				map.put(r.getUrl(), r);
				size += r.getContent().length;
			}
			else {
				String key = PLUGIN_RESOURCES + chunkCount; 
				refs.add(key);
				getSystemService().getCache().put(key, map);
				size = 0;
				chunkCount++;
				map = new HashMap<String, PluginResourceEntity>();
			}
		}
		getSystemService().getCache().put(PLUGIN_RESOURCES, refs);
	}

}
