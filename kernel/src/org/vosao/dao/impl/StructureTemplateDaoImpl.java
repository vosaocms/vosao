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

package org.vosao.dao.impl;

import java.util.List;

import org.vosao.dao.BaseDaoImpl;
import org.vosao.dao.StructureTemplateDao;
import org.vosao.entity.StructureTemplateEntity;

import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;

/**
 * @author Alexander Oleynik
 */
public class StructureTemplateDaoImpl 
		extends BaseDaoImpl<StructureTemplateEntity> 
		implements StructureTemplateDao {

	public StructureTemplateDaoImpl() {
		super(StructureTemplateEntity.class);
	}

	@Override
	public List<StructureTemplateEntity> selectByStructure(Long structureId) {
		Query q = newQuery();
		q.addFilter("structureId", FilterOperator.EQUAL, structureId);
		return select(q, "selectByStructure", params(structureId));
	}

	@Override
	public StructureTemplateEntity getByName(String name) {
		Query q = newQuery();
		q.addFilter("name", FilterOperator.EQUAL, name);
		return selectOne(q, "getByName", params(name));
	}

	@Override
	public StructureTemplateEntity getByTitle(String title) {
		Query q = newQuery();
		q.addFilter("title", FilterOperator.EQUAL, title);
		return selectOne(q, "getByTitle", params(title));
	}
}
