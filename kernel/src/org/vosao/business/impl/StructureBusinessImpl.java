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
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.vosao.business.StructureBusiness;
import org.vosao.entity.PageEntity;
import org.vosao.entity.StructureEntity;
import org.vosao.i18n.Messages;

/**
 * @author Alexander Oleynik
 */
public class StructureBusinessImpl extends AbstractBusinessImpl 
	implements StructureBusiness {

	@Override
	public List<String> validateBeforeUpdate(final StructureEntity entity) {
		List<String> errors = new ArrayList<String>();
		StructureEntity foundStructure = getDao().getStructureDao().getByTitle(
				entity.getTitle());
		if (entity.getId() == null) {
			if (foundStructure != null) {
				errors.add(Messages.get("structure.already_exists"));
			}
		}
		else {
			if (foundStructure != null 
				&& !foundStructure.getId().equals(entity.getId())) {
				errors.add(Messages.get("structure.already_exists"));
			}
		}
		if (StringUtils.isEmpty(entity.getTitle())) {
			errors.add(Messages.get("title_is_empty"));
		}
		try {
			Document document = DocumentHelper.parseText(entity.getContent());
			if (!document.getRootElement().getName().equals("structure")) {
				errors.add(Messages.get("structure.invalid_xml"));
			}
		} catch (DocumentException e) {
			errors.add(Messages.get("parsing_error") + " " + e.getMessage());
		}
		return errors;
	}

	@Override
	public List<String> remove(List<Long> ids) {
		List<String> result = new ArrayList<String>();
		for (Long id : ids) {
			StructureEntity structure = getDao().getStructureDao().getById(id);
			if (structure == null) {
				continue;
			}
			List<PageEntity> pages = getDao().getPageDao().selectByStructure(id);
			if (pages.size() > 0) {
				result.add(Messages.get("structure.has_references", 
						structure.getTitle(), pages.get(0).getFriendlyURL()));
			}
			else {
				getDao().getStructureDao().remove(id);
			}
		}	
		return result;
	}
}
