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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.datanucleus.util.StringUtils;
import org.vosao.entity.SeoUrlEntity;
import org.vosao.service.ServiceResponse;
import org.vosao.service.back.SeoUrlService;
import org.vosao.service.impl.AbstractServiceImpl;
import org.vosao.utils.StrUtil;

public class SeoUrlServiceImpl extends AbstractServiceImpl 
		implements SeoUrlService {

	private static final Log logger = LogFactory.getLog(SeoUrlServiceImpl.class);

	@Override
	public List<SeoUrlEntity> select() {
		return getDao().getSeoUrlDao().select();
	}

	@Override
	public ServiceResponse remove(List<String> ids) {
		getDao().getSeoUrlDao().remove(StrUtil.toLong(ids));
		return ServiceResponse.createSuccessResponse(
				"Seo urls were successfully deleted");
	}

	@Override
	public SeoUrlEntity getById(Long id) {
		return getDao().getSeoUrlDao().getById(id);
	}

	private List<String> validate(SeoUrlEntity entity) {
		List<String> errors = new ArrayList<String>();
		SeoUrlEntity found = getDao().getSeoUrlDao().getByFrom(
				entity.getFromLink());
		if (found != null && !found.getId().equals(entity.getId())) {
				errors.add("Sush \"from\" url already exists.");
		}
		return errors;
	}
	
	@Override
	public ServiceResponse save(Map<String, String> vo) {
		SeoUrlEntity seoUrl = null;
		if (!StringUtils.isEmpty(vo.get("id"))) {
			seoUrl = getDao().getSeoUrlDao().getById(Long.valueOf(vo.get("id")));
		}
		if (seoUrl == null) {
			seoUrl = new SeoUrlEntity();
		}
		seoUrl.setFromLink(vo.get("fromLink"));
		seoUrl.setToLink(vo.get("toLink"));
		List<String> errors = validate(seoUrl);
		if (errors.isEmpty()) {
			getDao().getSeoUrlDao().save(seoUrl);
			return ServiceResponse.createSuccessResponse(
						"Seo url was successfully saved.");
		}
		else {
			return ServiceResponse.createErrorResponse(
					"Error occured during SEO url save", errors);
		}
	}

}
