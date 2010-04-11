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

package org.vosao.business;

import javax.servlet.http.HttpServletRequest;

import org.vosao.dao.Dao;
import org.vosao.entity.UserEntity;
import org.vosao.global.SystemService;
import org.vosao.search.SearchEngine;

import com.google.gdata.client.photos.PicasawebService;


public interface Business {
	
	SystemService getSystemService();
	void setSystemService(final SystemService bean);

	Dao getDao();
	void setDao(Dao bean);
	
	SearchEngine getSearchEngine();
	void setSearchEngine(SearchEngine bean);

	UserEntity getUser();
	
	String getLanguage();

	PicasawebService getPicasawebService();
	
	PageBusiness getPageBusiness();
	void setPageBusiness(final PageBusiness bean);

	FolderBusiness getFolderBusiness();
	void setFolderBusiness(final FolderBusiness bean);
	
	TemplateBusiness getTemplateBusiness();
	void setTemplateBusiness(final TemplateBusiness bean);

	ConfigBusiness getConfigBusiness();
	void setConfigBusiness(final ConfigBusiness bean);

	FormBusiness getFormBusiness();
	void setFormBusiness(final FormBusiness bean);

	FileBusiness getFileBusiness();
	void setFileBusiness(final FileBusiness bean);

	CommentBusiness getCommentBusiness();
	void setCommentBusiness(final CommentBusiness bean);

	FieldBusiness getFieldBusiness();
	void setFieldBusiness(final FieldBusiness bean);

	MessageBusiness getMessageBusiness();
	void setMessageBusiness(final MessageBusiness bean);

	UserBusiness getUserBusiness();
	void setUserBusiness(final UserBusiness bean);

	ContentPermissionBusiness getContentPermissionBusiness();
	void setContentPermissionBusiness(final ContentPermissionBusiness bean);

	GroupBusiness getGroupBusiness();
	void setGroupBusiness(final GroupBusiness bean);

	FolderPermissionBusiness getFolderPermissionBusiness();
	void setFolderPermissionBusiness(final FolderPermissionBusiness bean);
	
	StructureBusiness getStructureBusiness();
	void setStructureBusiness(final StructureBusiness bean);

	StructureTemplateBusiness getStructureTemplateBusiness();
	void setStructureTemplateBusiness(final StructureTemplateBusiness bean);

	PluginBusiness getPluginBusiness();
	void setPluginBusiness(final PluginBusiness bean);

	PluginResourceBusiness getPluginResourceBusiness();
	void setPluginResourceBusiness(final PluginResourceBusiness bean);
	
	ImportExportBusiness getImportExportBusiness();
	void setImportExportBusiness(final ImportExportBusiness bean);

	TagBusiness getTagBusiness();
	void setTagBusiness(final TagBusiness bean);

}
