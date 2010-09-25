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

package org.vosao.plugins.backup;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.business.plugin.AbstractPluginEntryPoint;
import org.vosao.entity.PluginEntity;
import org.vosao.entity.helper.PluginHelper;
import org.vosao.entity.helper.PluginParameter;

public class BackupEntryPoint extends AbstractPluginEntryPoint {

	private static final Log logger = LogFactory.getLog(
			BackupEntryPoint.class);

	@Override
	public String getBundleName() {
		return "org.vosao.plugins.backup.messages";
	}
	
	@Override
	public void init() {
		getJobs().add(new BackupJob(getConfig()));
	}
	
	private BackupConfig getConfig() {
		PluginEntity plugin = getDao().getPluginDao().getByName("backup");
		Map<String, PluginParameter> params = PluginHelper.parseParameters(
				plugin);
		BackupConfig result = new BackupConfig();
		try {
			if (params.containsKey("cron")) {
				result.setCron(params.get("cron").getValue());
			}
			else {
				result.setCron(params.get("cron").getDefaultValue());
			}
		}
		catch (Exception e) {
			logger.error("cron parameter: " + e.getMessage());
		}
		return result;
	}

}
