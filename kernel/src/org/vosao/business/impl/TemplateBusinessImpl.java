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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.vosao.business.TemplateBusiness;
import org.vosao.entity.PageEntity;
import org.vosao.entity.TemplateEntity;
import org.vosao.i18n.Messages;

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
		if (StringUtils.isEmpty(template.getUrl())) {
			errors.add(Messages.get("url_is_empty"));
		}
		if (StringUtils.isEmpty(template.getTitle())) {
			errors.add(Messages.get("title_is_empty"));
		}
		if (StringUtils.isEmpty(template.getContent())) {
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

	@Override
	public TemplateEntity save(TemplateEntity template) {
		Set<String> pages = new HashSet<String>();
		for (PageEntity page : getDao().getPageDao().selectByTemplate(
				template.getId())) {
			pages.add(page.getFriendlyURL());
		}
		for (String url : pages) {
			getBusiness().getSystemService().getPageCache().remove(url);
		}
		return getDao().getTemplateDao().save(template);
	}

}
