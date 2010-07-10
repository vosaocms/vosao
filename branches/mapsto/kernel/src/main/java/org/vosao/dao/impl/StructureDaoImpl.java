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

import java.util.Collection;
import java.util.List;

import org.mapsto.Filter;
import org.mapsto.Query;
import org.vosao.common.VosaoContext;
import org.vosao.dao.BaseDaoImpl;
import org.vosao.dao.BaseMapstoDaoImpl;
import org.vosao.dao.StructureDao;
import org.vosao.dao.StructureTemplateDao;
import org.vosao.entity.StructureEntity;
import org.vosao.entity.helper.StructureTemplateHelper;

/**
 * @author Alexander Oleynik
 */
public class StructureDaoImpl extends BaseMapstoDaoImpl<StructureEntity> 
		implements StructureDao {

	public StructureDaoImpl() {
		super("StructureEntity");
	}

	public StructureTemplateDao getStructureTemplateDao() {
		return VosaoContext.getInstance().getBusiness().getDao()
				.getStructureTemplateDao();
	}
	
	@Override
	public StructureEntity getByTitle(String title) {
		Query<StructureEntity> q = newQuery();
		q.addFilter("title", Filter.EQUAL, title);
		return q.selectOne("getByTitle", params(title));
	}
	
	@Override
	public void remove(Long id) {
		List<Long> structureTemplateIds = StructureTemplateHelper.createIdList(
				getStructureTemplateDao().selectByStructure(id));
		getStructureTemplateDao().remove(structureTemplateIds);
		super.remove(id);
	}

	@Override
	public void remove(Collection<Long> ids) {
		for (Long id : ids) {
			remove(id);
		}
	}

}
