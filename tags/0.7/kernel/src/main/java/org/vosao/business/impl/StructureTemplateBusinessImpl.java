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

import org.vosao.business.StructureTemplateBusiness;
import org.vosao.entity.PageEntity;
import org.vosao.entity.StructureTemplateEntity;
import org.vosao.i18n.Messages;

import com.google.appengine.repackaged.com.google.common.base.StringUtil;

/**
 * @author Alexander Oleynik
 */
public class StructureTemplateBusinessImpl extends AbstractBusinessImpl 
	implements StructureTemplateBusiness {

	@Override
	public List<String> validateBeforeUpdate(
			final StructureTemplateEntity entity) {
		List<String> errors = new ArrayList<String>();
		StructureTemplateEntity foundStructure = getDao().getStructureTemplateDao()
				.getByName(entity.getName());
		if (entity.getId() == null) {
			if (foundStructure != null) {
				errors.add(Messages.get("structureTemplate.already_exists"));
			}
		}
		else {
			if (foundStructure != null 
				&& !foundStructure.getId().equals(entity.getId())) {
				errors.add(Messages.get("structureTemplate.already_exists"));
			}
		}
		if (StringUtil.isEmpty(entity.getTitle())) {
			errors.add(Messages.get("title_is_empty"));
		}
		if (StringUtil.isEmpty(entity.getName())) {
			errors.add(Messages.get("name_is_empty"));
		}
		return errors;
	}
	
	@Override
	public List<String> remove(List<Long> ids) {
		List<String> result = new ArrayList<String>();
		for (Long id : ids) {
			StructureTemplateEntity entity = getDao().getStructureTemplateDao()
					.getById(id);
			if (entity == null) {
				continue;
			}
			List<PageEntity> pages = getDao().getPageDao()
					.selectByStructureTemplate(id);
			if (pages.size() > 0) {
				result.add(Messages.get("structureTemplate.has_references", 
						entity.getTitle(), pages.get(0).getFriendlyURL()));
			}
			else {
				getDao().getStructureTemplateDao().remove(id);
			}
		}	
		return result;
	}
}
