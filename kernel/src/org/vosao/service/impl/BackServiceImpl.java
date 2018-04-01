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

package org.vosao.service.impl;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jabsorb.JSONRPCBridge;
import org.vosao.business.Business;
import org.vosao.common.VosaoContext;
import org.vosao.dao.Dao;
import org.vosao.entity.PluginEntity;
import org.vosao.service.BackService;
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
import org.vosao.service.back.PageAttributeService;
import org.vosao.service.back.PageService;
import org.vosao.service.back.PicasaService;
import org.vosao.service.back.PluginService;
import org.vosao.service.back.SeoUrlService;
import org.vosao.service.back.StructureService;
import org.vosao.service.back.StructureTemplateService;
import org.vosao.service.back.TagService;
import org.vosao.service.back.TemplateService;
import org.vosao.service.back.UserService;
import org.vosao.service.back.impl.CommentServiceImpl;
import org.vosao.service.back.impl.ConfigServiceImpl;
import org.vosao.service.back.impl.ContentPermissionServiceImpl;
import org.vosao.service.back.impl.FieldServiceImpl;
import org.vosao.service.back.impl.FileServiceImpl;
import org.vosao.service.back.impl.FolderPermissionServiceImpl;
import org.vosao.service.back.impl.FolderServiceImpl;
import org.vosao.service.back.impl.FormServiceImpl;
import org.vosao.service.back.impl.GroupServiceImpl;
import org.vosao.service.back.impl.LanguageServiceImpl;
import org.vosao.service.back.impl.MessageServiceImpl;
import org.vosao.service.back.impl.PageAttributeServiceImpl;
import org.vosao.service.back.impl.PageServiceImpl;
import org.vosao.service.back.impl.PicasaServiceImpl;
import org.vosao.service.back.impl.PluginServiceImpl;
import org.vosao.service.back.impl.SeoUrlServiceImpl;
import org.vosao.service.back.impl.StructureServiceImpl;
import org.vosao.service.back.impl.StructureTemplateServiceImpl;
import org.vosao.service.back.impl.TagServiceImpl;
import org.vosao.service.back.impl.TemplateServiceImpl;
import org.vosao.service.back.impl.UserServiceImpl;
import org.vosao.service.plugin.PluginServiceManager;

public class BackServiceImpl implements BackService, Serializable {

	private static final Log log = LogFactory.getLog(BackServiceImpl.class);

	private FileService fileService;
	private FolderService folderService;
	private CommentService commentService;
	private PageService pageService;
	private TemplateService templateService;
	private FormService formService;
	private FieldService fieldService;
	private ConfigService configService;
	private SeoUrlService seoUrlService;
	private UserService userService;
	private LanguageService languageService;
	private MessageService messageService;
	private GroupService groupService;
	private ContentPermissionService contentPermissionService;
	private FolderPermissionService folderPermissionService;
	private StructureService structureService;
	private StructureTemplateService structureTemplateService;
	private PluginService pluginService;
	private TagService tagService;
	private PicasaService picasaService;
	private PageAttributeService pageAttributeService;
	
	@Override
	public void register(JSONRPCBridge bridge) {
		bridge.registerObject("fileService", getFileService());
		bridge.registerObject("folderService", getFolderService());
		bridge.registerObject("commentService", getCommentService());
		bridge.registerObject("pageService", getPageService());
		bridge.registerObject("templateService", getTemplateService());
		bridge.registerObject("fieldService", getFieldService());
		bridge.registerObject("formService", getFormService());
		bridge.registerObject("configService", getConfigService());
		bridge.registerObject("seoUrlService", getSeoUrlService());
		bridge.registerObject("userService", getUserService());
		bridge.registerObject("languageService", getLanguageService());
		bridge.registerObject("messageService", getMessageService());
		bridge.registerObject("groupService", getGroupService());
		bridge.registerObject("contentPermissionService", 
				getContentPermissionService());
		bridge.registerObject("folderPermissionService", 
				getFolderPermissionService());
		bridge.registerObject("structureService", getStructureService());
		bridge.registerObject("structureTemplateService", 
				getStructureTemplateService());
		bridge.registerObject("pluginService", getPluginService());
		bridge.registerObject("tagService", getTagService());
		bridge.registerObject("picasaService", getPicasaService());
		bridge.registerObject("pageAttributeService", getPageAttributeService());
		registerPluginServices(bridge);
	}
	
	@Override
	public void unregister(JSONRPCBridge bridge) {
		bridge.unregisterObject("fileService");
		bridge.unregisterObject("folderService");
		bridge.unregisterObject("commentService");
		bridge.unregisterObject("pageService");
		bridge.unregisterObject("templateService");
		bridge.unregisterObject("fieldService");
		bridge.unregisterObject("formService");
		bridge.unregisterObject("configService");
		bridge.unregisterObject("seoUrlService");
		bridge.unregisterObject("userService");
		bridge.unregisterObject("languageService");
		bridge.unregisterObject("messageService");
		bridge.unregisterObject("groupService");
		bridge.unregisterObject("contentPermissionService");
		bridge.unregisterObject("folderPermissionService");
		bridge.unregisterObject("structureService");
		bridge.unregisterObject("structureTemplateService");
		bridge.unregisterObject("pluginService");
		bridge.unregisterObject("tagService");
		bridge.unregisterObject("picasaService");
		bridge.unregisterObject("pageAttributeService");
		unregisterPluginServices(bridge);
	}

	@Override
	public FileService getFileService() {
		if (fileService == null) {
			fileService = new FileServiceImpl();
		}
		return fileService;
	}

	@Override
	public void setFileService(FileService bean) {
		fileService = bean;
	}

	@Override
	public FolderService getFolderService() {
		if (folderService == null) {
			folderService = new FolderServiceImpl();
		}
		return folderService;
	}

	@Override
	public void setFolderService(FolderService bean) {
		folderService = bean;
	}

	@Override
	public CommentService getCommentService() {
		if (commentService == null) {
			commentService = new CommentServiceImpl();
		}
		return commentService;
	}

	@Override
	public void setCommentService(CommentService bean) {
		commentService = bean;		
	}

	@Override
	public PageService getPageService() {
		if (pageService == null) {
			pageService = new PageServiceImpl();
		}
		return pageService;
	}

	@Override
	public void setPageService(PageService bean) {
		pageService = bean;		
	}

	@Override
	public TemplateService getTemplateService() {
		if (templateService == null) {
			templateService = new TemplateServiceImpl();
		}
		return templateService;
	}

	@Override
	public void setTemplateService(TemplateService templateService) {
		this.templateService = templateService;
	}

	@Override
	public FieldService getFieldService() {
		if (fieldService == null) {
			fieldService = new FieldServiceImpl();
		}
		return fieldService;
	}

	@Override
	public void setFieldService(FieldService bean) {
		fieldService = bean;		
	}

	@Override
	public ConfigService getConfigService() {
		if (configService == null) {
			configService = new ConfigServiceImpl();
		}
		return configService;
	}

	@Override
	public void setConfigService(ConfigService bean) {
		configService = bean;		
	}

	@Override
	public FormService getFormService() {
		if (formService == null) {
			formService = new FormServiceImpl();
		}
		return formService;
	}

	@Override
	public void setFormService(FormService bean) {
		formService = bean;		
	}

	@Override
	public SeoUrlService getSeoUrlService() {
		if (seoUrlService == null) {
			seoUrlService = new SeoUrlServiceImpl();
		}
		return seoUrlService;
	}

	@Override
	public void setSeoUrlService(SeoUrlService bean) {
		seoUrlService = bean;		
	}

	@Override
	public UserService getUserService() {
		if (userService == null) {
			userService = new UserServiceImpl();
		}
		return userService;
	}

	@Override
	public void setUserService(UserService bean) {
		userService = bean;		
	}

	@Override
	public LanguageService getLanguageService() {
		if (languageService == null) {
			languageService = new LanguageServiceImpl();
		}
		return languageService;
	}

	@Override
	public void setLanguageService(LanguageService bean) {
		languageService = bean;		
	}

	@Override
	public MessageService getMessageService() {
		if (messageService == null) {
			messageService = new MessageServiceImpl();
		}
		return messageService;
	}

	@Override
	public void setMessageService(MessageService bean) {
		messageService = bean;		
	}

	@Override
	public GroupService getGroupService() {
		if (groupService == null) {
			groupService = new GroupServiceImpl();
		}
		return groupService;
	}

	@Override
	public void setGroupService(GroupService bean) {
		groupService = bean;		
	}

	@Override
	public ContentPermissionService getContentPermissionService() {
		if (contentPermissionService == null) {
			contentPermissionService = new ContentPermissionServiceImpl();
		}
		return contentPermissionService;
	}

	@Override
	public void setContentPermissionService(ContentPermissionService bean) {
		contentPermissionService = bean;		
	}

	@Override
	public FolderPermissionService getFolderPermissionService() {
		if (folderPermissionService == null) {
			folderPermissionService = new FolderPermissionServiceImpl();
		}
		return folderPermissionService;
	}

	@Override
	public void setFolderPermissionService(FolderPermissionService bean) {
		folderPermissionService = bean;		
	}

	@Override
	public StructureService getStructureService() {
		if (structureService == null) {
			structureService = new StructureServiceImpl();
		}
		return structureService;
	}

	@Override
	public void setStructureService(StructureService structureService) {
		this.structureService = structureService;
	}

	@Override
	public StructureTemplateService getStructureTemplateService() {
		if (structureTemplateService == null) {
			structureTemplateService = new StructureTemplateServiceImpl();
		}
		return structureTemplateService;
	}

	@Override
	public void setStructureTemplateService(
			StructureTemplateService structureTemplateService) {
		this.structureTemplateService = structureTemplateService;
	}
	
	@Override
	public PluginService getPluginService() {
		if (pluginService == null) {
			pluginService = new PluginServiceImpl();
		}
		return pluginService;
	}

	@Override
	public void setPluginService(PluginService bean) {
		this.pluginService = bean;
	}

	@Override
	public TagService getTagService() {
		if (tagService == null) {
			tagService = new TagServiceImpl();
		}
		return tagService;
	}

	@Override
	public void setTagService(TagService bean) {
		this.tagService = bean;
	}

	@Override
	public PicasaService getPicasaService() {
		if (picasaService == null) {
			picasaService = new PicasaServiceImpl();
		}
		return picasaService;
	}

	@Override
	public void setPicasaService(PicasaService bean) {
		this.picasaService = bean;
	}

	private Business getBusiness() {
		return VosaoContext.getInstance().getBusiness();
	}

	private void registerPluginServices(JSONRPCBridge bridge) {
		for (PluginEntity plugin : getDao().getPluginDao().selectEnabled()) {
			try {
				PluginServiceManager manager = getBusiness()
						.getPluginBusiness().getBackServices(plugin);
				if (manager != null) {
					manager.register(bridge);
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void unregisterPluginServices(JSONRPCBridge bridge) {
		for (PluginEntity plugin : getDao().getPluginDao().selectEnabled()) {
			try {
				PluginServiceManager manager = getBusiness()
					.getPluginBusiness().getBackServices(plugin);
				if (manager != null) {
					manager.unregister(bridge);
				}				
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	private Dao getDao() {
		return getBusiness().getDao();
	}

	@Override
	public PageAttributeService getPageAttributeService() {
		if (pageAttributeService == null) {
			pageAttributeService = new PageAttributeServiceImpl();
		}
		return pageAttributeService;
	}

	@Override
	public void setPageAttributeService(PageAttributeService bean) {
		pageAttributeService = bean;		
	}
	
}
