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

package org.vosao.update;

import org.vosao.business.Business;
import org.vosao.dao.Dao;
import org.vosao.entity.StructureTemplateEntity;

/**
 * 
 * @author Alexander Oleynik
 *
 */
public class UpdateTask05 implements UpdateTask {

	private Business business;
	
	public UpdateTask05(Business aBusiness) {
		business = aBusiness;
	}
	
	private Dao getDao() {
		return business.getDao();
	}
	
	private Business getBusiness() {
		return business;
	}

	@Override
	public String getFromVersion() {
		return "0.4";
	}

	@Override
	public String getToVersion() {
		return "0.5";
	}

	@Override
	public String update() throws UpdateException {
		getBusiness().getSetupBean().clearSessions();
		updateStructureTemplates();
		return "Successfully updated to 0.5 version.";
	}

	private void updateStructureTemplates() {
		for (StructureTemplateEntity template : getDao()
				.getStructureTemplateDao().select()) {
			template.setName(template.getTitle());
			getDao().getStructureTemplateDao().save(template);
		}
	}
	
}
