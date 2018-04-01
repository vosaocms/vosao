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

package org.vosao.update;

import org.vosao.business.Business;
import org.vosao.dao.Dao;
import org.vosao.entity.PluginEntity;
import org.vosao.entity.StructureTemplateEntity;
import org.vosao.entity.UserEntity;

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
		updateStructureTemplates();
		updateUsers();
		updatePlugins();
		return "Successfully updated to 0.5 version.";
	}

	private void updateStructureTemplates() {
		for (StructureTemplateEntity template : getDao()
				.getStructureTemplateDao().select()) {
			template.setName(template.getTitle());
			getDao().getStructureTemplateDao().save(template);
		}
	}
	
	private void updateUsers() {
		for (UserEntity user : getDao().getUserDao().select()) {
			user.setDisabled(false);
			getDao().getUserDao().save(user);
		}
	}
	
	private void updatePlugins() {
		for (PluginEntity plugin : getDao().getPluginDao().select()) {
			plugin.setDisabled(true);
			getDao().getPluginDao().save(plugin);
		}
	}

}
