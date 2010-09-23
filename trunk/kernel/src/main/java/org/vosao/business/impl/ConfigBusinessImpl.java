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

package org.vosao.business.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.vosao.business.ConfigBusiness;
import org.vosao.common.VosaoContext;
import org.vosao.entity.ConfigEntity;
import org.vosao.filter.SiteFilter;
import org.vosao.i18n.Messages;

public class ConfigBusinessImpl extends AbstractBusinessImpl 
	implements ConfigBusiness {

	@Override
	public ConfigEntity getConfig() {
		return VosaoContext.getInstance().getConfig();
	}

	@Override
	public boolean isTextFileExt(String ext) {
		ConfigEntity config = getConfig();
		String[] exts = config.getEditExt().split(",");
		for (String textExt : exts) {
			if (ext.equals(textExt)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isImageFileExt(String ext) {
		String[] exts = {"jpg","jpeg","png","gif","ico"};
		for (String imageExt : exts) {
			if (ext.equals(imageExt)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public List<String> validateBeforeUpdate(ConfigEntity entity) {
		List<String> errors = new ArrayList<String>();
		if (StringUtils.isEmpty(entity.getSiteDomain())) {
			errors.add(Messages.get("config.site_domain_is_empty"));
		}
		if (StringUtils.isEmpty(entity.getSiteEmail())) {
			errors.add(Messages.get("config.site_email_is_empty"));
		}
		if (VosaoContext.getInstance().isSkipUrl(entity.getSiteUserLoginUrl())) {
			errors.add(entity.getSiteUserLoginUrl() 
					+ Messages.get("config.url_reserved"));
		}
		return errors;
	}
	
}
