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

package org.vosao.dao.impl;

import java.util.Collections;
import java.util.List;

import org.vosao.dao.FieldDao;
import org.vosao.entity.FieldEntity;
import org.vosao.entity.FormEntity;
import org.vosao.entity.helper.FieldHelper;

public class FieldDaoImpl extends BaseDaoImpl<String, FieldEntity> 
		implements FieldDao {

	public FieldDaoImpl() {
		super(FieldEntity.class);
	}

	@Override
	public List<FieldEntity> getByForm(final FormEntity form) {
		String query = "select from " + FieldEntity.class.getName()
				+ " where formId == pFormId parameters String pFormId";
		List<FieldEntity> result = select(query, params(form.getId()));
		Collections.sort(result, new FieldHelper.IndexAsc());
		return result;
	}
	
	@Override
	public FieldEntity getByName(final FormEntity form, final String name) {
		String query = "select from " + FieldEntity.class.getName()
				+ " where formId == pFormId && name == pName"
				+ " parameters String pFormId, String pName";
		return selectOne(query, params(form.getId(), name));
	}
	
}
