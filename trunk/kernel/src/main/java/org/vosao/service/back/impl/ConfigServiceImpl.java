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

package org.vosao.service.back.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.business.impl.SetupBeanImpl;
import org.vosao.entity.ConfigEntity;
import org.vosao.entity.FormConfigEntity;
import org.vosao.service.ServiceResponse;
import org.vosao.service.back.ConfigService;
import org.vosao.service.impl.AbstractServiceImpl;
import org.vosao.utils.StreamUtil;

public class ConfigServiceImpl extends AbstractServiceImpl 
		implements ConfigService {

	private static Log logger = LogFactory.getLog(ConfigServiceImpl.class);

	@Override
	public ConfigEntity getConfig() {
		return getBusiness().getConfigBusiness().getConfig();
	}

	@Override
	public ServiceResponse saveConfig(Map<String, String> vo) {
		ConfigEntity config = getBusiness().getConfigBusiness().getConfig();
		config.setCommentsEmail(vo.get("commentsEmail"));
		config.setCommentsTemplate(vo.get("commentsTemplate"));
		config.setEditExt(vo.get("editExt"));
		config.setGoogleAnalyticsId(vo.get("googleAnalyticsId"));
		config.setEnableRecaptcha(Boolean.valueOf(vo.get("enableRecaptcha")));
		config.setRecaptchaPrivateKey(vo.get("recaptchaPrivateKey"));
		config.setRecaptchaPublicKey(vo.get("recaptchaPublicKey"));
		config.setSiteDomain(vo.get("siteDomain"));
		config.setSiteEmail(vo.get("siteEmail"));
		config.setSiteUserLoginUrl(vo.get("siteUserLoginUrl"));
		List<String> errors = getBusiness().getConfigBusiness()
				.validateBeforeUpdate(config);
		if (errors.isEmpty()) {
			getDao().getConfigDao().save(config);
			return ServiceResponse.createSuccessResponse(
					"Configuration was successfully saved.");
		}
		else {
			return ServiceResponse.createErrorResponse(
					"Error during save config.", errors);
		}
	}

	@Override
	public ServiceResponse restoreCommentsTemplate() throws IOException {
		ConfigEntity config = getDao().getConfigDao().getConfig();
		config.setCommentsTemplate(StreamUtil.getTextResource(
			SetupBeanImpl.COMMENTS_TEMPLATE_FILE));
		getDao().getConfigDao().save(config);			
		return ServiceResponse.createSuccessResponse(
				"Comments template was successfully restored.");
	}
	
}
