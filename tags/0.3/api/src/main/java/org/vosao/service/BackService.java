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

package org.vosao.service;

import org.jabsorb.JSONRPCBridge;
import org.vosao.service.back.CommentService;
import org.vosao.service.back.ConfigService;
import org.vosao.service.back.ContentPermissionService;
import org.vosao.service.back.FieldService;
import org.vosao.service.back.FileService;
import org.vosao.service.back.FolderPermissionService;
import org.vosao.service.back.FolderService;
import org.vosao.service.back.FormService;
import org.vosao.service.back.GroupService;
import org.vosao.service.back.LanguageService;
import org.vosao.service.back.MessageService;
import org.vosao.service.back.PageService;
import org.vosao.service.back.PluginService;
import org.vosao.service.back.SeoUrlService;
import org.vosao.service.back.StructureService;
import org.vosao.service.back.StructureTemplateService;
import org.vosao.service.back.TemplateService;
import org.vosao.service.back.UserService;

public interface BackService {
	
	void register(JSONRPCBridge bridge);
	void unregister(JSONRPCBridge bridge);

	FileService getFileService();
	void setFileService(FileService bean);

	FolderService getFolderService();
	void setFolderService(FolderService bean);
	
	CommentService getCommentService();
	void setCommentService(CommentService bean);

	PageService getPageService();
	void setPageService(PageService bean);

	TemplateService getTemplateService();
	void setTemplateService(TemplateService bean);

	FormService getFormService();
	void setFormService(FormService bean);

	FieldService getFieldService();
	void setFieldService(FieldService bean);
	
	ConfigService getConfigService();
	void setConfigService(ConfigService bean);
	
	SeoUrlService getSeoUrlService();
	void setSeoUrlService(SeoUrlService bean);

	UserService getUserService();
	void setUserService(UserService bean);

	LanguageService getLanguageService();
	void setLanguageService(LanguageService bean);

	MessageService getMessageService();
	void setMessageService(MessageService bean);

	GroupService getGroupService();
	void setGroupService(GroupService bean);

	ContentPermissionService getContentPermissionService();
	void setContentPermissionService(ContentPermissionService bean);

	FolderPermissionService getFolderPermissionService();
	void setFolderPermissionService(FolderPermissionService bean);

	StructureService getStructureService();
	void setStructureService(StructureService bean);
	
	StructureTemplateService getStructureTemplateService();
	void setStructureTemplateService(StructureTemplateService bean);

	PluginService getPluginService();
	void setPluginService(PluginService bean);

}
