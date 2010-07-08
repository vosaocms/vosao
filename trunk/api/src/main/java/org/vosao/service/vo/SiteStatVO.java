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

package org.vosao.service.vo;

import org.vosao.common.VosaoContext;
import org.vosao.dao.Dao;

/**
 * Value object to be returned from services.
 * 
 * @author Alexander Oleynik
 */
public class SiteStatVO {

	public SiteStatVO() {
	}

	private Dao getDao() {
		return VosaoContext.getInstance().getBusiness().getDao();
	}
	
	public int getPages() {
		return getDao().getPageDao().count();
	}

	public int getPagePermissions() {
		return getDao().getContentPermissionDao().count();
	}

	public int getStructures() {
		return getDao().getStructureDao().count();
	}

	public int getStructureTemplates() {
		return getDao().getStructureTemplateDao().count();
	}

	public int getTemplates() {
		return getDao().getTemplateDao().count();
	}

	public int getFolders() {
		return getDao().getFolderDao().count();
	}

	public int getFolderPermissions() {
		return getDao().getFolderPermissionDao().count();
	}

	public int getFiles() {
		return getDao().getFileDao().count();
	}

	public int getLanguages() {
		return getDao().getLanguageDao().count();
	}

	public int getMessages() {
		return getDao().getMessageDao().count();
	}

	public int getUsers() {
		return getDao().getUserDao().count();
	}

	public int getGroups() {
		return getDao().getGroupDao().count();
	}

	public int getTags() {
		return getDao().getTagDao().count();
	}

	public int getTotal() {
		return getFiles() + getFolderPermissions() + getFolders()
			+ getGroups() + getLanguages() + getMessages() +getPagePermissions()
			+ getPages() + getStructures() + getStructureTemplates() + getTags()
			+ getTemplates() + getUsers();
	}
}
