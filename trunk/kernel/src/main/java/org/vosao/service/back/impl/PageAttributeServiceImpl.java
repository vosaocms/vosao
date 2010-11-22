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

package org.vosao.service.back.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.vosao.entity.PageAttributeEntity;
import org.vosao.service.ServiceResponse;
import org.vosao.service.back.PageAttributeService;
import org.vosao.service.impl.AbstractServiceImpl;

/**
 * @author Alexander Oleynik
 */
public class PageAttributeServiceImpl extends AbstractServiceImpl 
		implements PageAttributeService {

	@Override
	public List<PageAttributeEntity> getByPage(String pageUrl) {
		return getBusiness().getPageAttributeBusiness().getByPage(pageUrl);
	}

	@Override
	public ServiceResponse save(Map<String, String> vo) {
		PageAttributeEntity attr;
		if (StringUtils.isEmpty(vo.get("id"))) {
			attr = new PageAttributeEntity();
		}
		else {
			attr = getDao().getPageAttributeDao().getById(Long.valueOf(
					vo.get("id")));
		}
		attr.setPageUrl(vo.get("url"));
		attr.setName(vo.get("name"));
		attr.setTitle(vo.get("title"));
		attr.setDefaultValue(vo.get("defaultValue"));
		attr.setInherited(Boolean.valueOf(vo.get("inherited")));
		// TODO add validation
		getDao().getPageAttributeDao().save(attr);
		return ServiceResponse.createSuccessResponse("Success");
	}

}
