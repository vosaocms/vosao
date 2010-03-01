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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.business.TemplateBusiness;
import org.vosao.entity.PageEntity;
import org.vosao.entity.TemplateEntity;

import com.google.appengine.repackaged.com.google.common.base.StringUtil;

public class TemplateBusinessImpl extends AbstractBusinessImpl 
	implements TemplateBusiness {

	private static final Log logger = LogFactory.getLog(TemplateBusinessImpl.class);
	
	@Override
	public List<String> validateBeforeUpdate(final TemplateEntity template) {
		List<String> errors = new ArrayList<String>();
		if (template.getId() == null) {
			TemplateEntity myTemplate = getDao().getTemplateDao().getByUrl(
					template.getUrl());
			if (myTemplate != null) {
				errors.add("Template with such URL already exists");
			}
		}
		if (StringUtil.isEmpty(template.getUrl())) {
			errors.add("URL is empty");
		}
		if (StringUtil.isEmpty(template.getTitle())) {
			errors.add("Title is empty");
		}
		if (StringUtil.isEmpty(template.getContent())) {
			errors.add("Content is empty");
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
				result.add("Template " + template.getTitle() 
						+ " has references " + pages.get(0).getFriendlyURL());
			}
			else {
				getDao().getTemplateDao().remove(id);
			}
		}	
		return result;
	}
	
}
