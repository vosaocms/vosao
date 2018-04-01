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

package org.vosao.business;

import java.util.TimeZone;

import org.vosao.business.mq.MessageQueue;
import org.vosao.dao.Dao;
import org.vosao.entity.UserEntity;
import org.vosao.global.SystemService;
import org.vosao.search.SearchEngine;

/**
 * 
 * @author Alexander Oleynik
 *
 */
public interface Business {
	
	SystemService getSystemService();
	void setSystemService(final SystemService bean);

	Dao getDao();
	void setDao(Dao bean);
	
	SearchEngine getSearchEngine();
	void setSearchEngine(SearchEngine bean);

	UserEntity getUser();
	TimeZone getTimeZone();
	
	/**
	 * Get current langauge for rendering page's content. It can be different 
	 * from current locale language as site languages set can not include current
	 * locale language. 
	 * @return language code.
	 */
	String getLanguage();
	
	String getDefaultLanguage();
	
	MessageQueue getMessageQueue();

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

	PicasaBusiness getPicasaBusiness();
	void setPicasaBusiness(final PicasaBusiness bean);

	SetupBean getSetupBean();
	void setSetupBean(final SetupBean bean);

	FormDataBusiness getFormDataBusiness();
	void setFormDataBusiness(final FormDataBusiness bean);

	PageAttributeBusiness getPageAttributeBusiness();
	void setPageAttributeBusiness(final PageAttributeBusiness bean);
	
	RewriteUrlBusiness getRewriteUrlBusiness();
	void setRewriteUrlBusiness(RewriteUrlBusiness bean);
}
