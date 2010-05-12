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
import java.util.List;

import org.vosao.business.TemplateBusiness;
import org.vosao.common.Messages;
import org.vosao.entity.PageEntity;
import org.vosao.entity.TemplateEntity;

import com.google.appengine.repackaged.com.google.common.base.StringUtil;

/**
 * 
 * @author Alexander Oleynik
 *
 */
public class TemplateBusinessImpl extends AbstractBusinessImpl 
	implements TemplateBusiness {

	@Override
	public List<String> validateBeforeUpdate(final TemplateEntity template) {
		List<String> errors = new ArrayList<String>();
		if (template.getId() == null) {
			TemplateEntity myTemplate = getDao().getTemplateDao().getByUrl(
					template.getUrl());
			if (myTemplate != null) {
				errors.add(Messages.get("template.already_exists"));
			}
		}
		if (StringUtil.isEmpty(template.getUrl())) {
			errors.add(Messages.get("url_is_empty"));
		}
		if (StringUtil.isEmpty(template.getTitle())) {
			errors.add(Messages.get("title_is_empty"));
		}
		if (StringUtil.isEmpty(template.getContent())) {
			errors.add(Messages.get("content_is_empty"));
		}
		return errors;
	}

	@Override
	public List<String> remove(List<Long> ids) {
		List<String> result = new ArrayList<String>();
		for (Long id : ids) {
			TemplateEntity template = getDao().getTemplateDao().getById(id);
			if (template == null) {
				continue;
			}
			List<PageEntity> pages = getDao().getPageDao().selectByTemplate(id);
			if (pages.size() > 0) {
				result.add(Messages.get("template.has_references",
						template.getTitle(), pages.get(0).getFriendlyURL()));
			}
			else {
				getDao().getTemplateDao().remove(id);
			}
		}	
		return result;
	}
	
}
