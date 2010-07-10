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

import org.mapsto.Filter;
import org.mapsto.Query;
import org.vosao.dao.BaseMapstoDaoImpl;
import org.vosao.dao.FieldDao;
import org.vosao.entity.FieldEntity;
import org.vosao.entity.FormEntity;
import org.vosao.entity.helper.FieldHelper;

public class FieldDaoImpl extends BaseMapstoDaoImpl<FieldEntity> 
		implements FieldDao {

	public FieldDaoImpl() {
		super("FieldEntity");
	}

	@Override
	public List<FieldEntity> getByForm(final FormEntity form) {
		Query<FieldEntity> q = newQuery();
		q.addFilter("formId", Filter.EQUAL, form.getId());
		List<FieldEntity> result = q.select("getByForm", params(form.getId()));
		Collections.sort(result, new FieldHelper.IndexAsc());
		return result;
	}
	
	@Override
	public FieldEntity getByName(final FormEntity form, final String name) {
		Query<FieldEntity> q = newQuery();
		q.addFilter("formId", Filter.EQUAL, form.getId());
		q.addFilter("name", Filter.EQUAL, name);
		return q.selectOne("getByName", params(form.getId(), name));
	}
	
}
