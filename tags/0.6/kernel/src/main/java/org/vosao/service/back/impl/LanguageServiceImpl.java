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

import org.datanucleus.util.StringUtils;
import org.vosao.entity.LanguageEntity;
import org.vosao.i18n.Messages;
import org.vosao.service.ServiceResponse;
import org.vosao.service.back.LanguageService;
import org.vosao.service.impl.AbstractServiceImpl;
import org.vosao.utils.StrUtil;

/**
 * 
 * @author Alexander Oleynik
 *
 */
public class LanguageServiceImpl extends AbstractServiceImpl 
		implements LanguageService {

	@Override
	public List<LanguageEntity> select() {
		return getDao().getLanguageDao().select();
	}

	@Override
	public ServiceResponse remove(List<String> ids) {
		getDao().getLanguageDao().remove(StrUtil.toLong(ids));
		return ServiceResponse.createSuccessResponse(
				Messages.get("languages.success_delete"));
	}

	@Override
	public LanguageEntity getById(Long id) {
		return getDao().getLanguageDao().getById(id);
	}

	private List<String> validate(LanguageEntity entity) {
		List<String> errors = new ArrayList<String>();
		LanguageEntity found = getDao().getLanguageDao().getByCode(
				entity.getCode());
		if (found != null && !found.getId().equals(entity.getId())) {
				errors.add(Messages.get("language.code_registered"));
		}
		return errors;
	}
	@Override
	
	public ServiceResponse save(Map<String, String> vo) {
		LanguageEntity language = null;
		if (!StringUtils.isEmpty(vo.get("id"))) {
			language = getDao().getLanguageDao().getById(Long.valueOf(
					vo.get("id")));
		}
		if (language == null) {
			language = new LanguageEntity();
		}
		language.setCode(vo.get("code"));
		language.setTitle(vo.get("title"));
		List<String> errors = validate(language);
		if (errors.isEmpty()) {
			getDao().getLanguageDao().save(language);
			return ServiceResponse.createSuccessResponse(
						Messages.get("language.success_save"));
		}
		else {
			return ServiceResponse.createErrorResponse(
					Messages.get("errors_occured"), errors);
		}
	}

}
