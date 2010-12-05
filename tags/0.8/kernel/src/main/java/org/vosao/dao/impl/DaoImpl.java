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

import java.io.Serializable;

import org.vosao.common.VosaoContext;
import org.vosao.dao.CommentDao;
import org.vosao.dao.ConfigDao;
import org.vosao.dao.ContentDao;
import org.vosao.dao.ContentPermissionDao;
import org.vosao.dao.Dao;
import org.vosao.dao.FieldDao;
import org.vosao.dao.FileChunkDao;
import org.vosao.dao.FileDao;
import org.vosao.dao.FolderDao;
import org.vosao.dao.FolderPermissionDao;
import org.vosao.dao.FormConfigDao;
import org.vosao.dao.FormDao;
import org.vosao.dao.FormDataDao;
import org.vosao.dao.GroupDao;
import org.vosao.dao.LanguageDao;
import org.vosao.dao.MessageDao;
import org.vosao.dao.PageAttributeDao;
import org.vosao.dao.PageDao;
import org.vosao.dao.PageDependencyDao;
import org.vosao.dao.PageTagDao;
import org.vosao.dao.PluginDao;
import org.vosao.dao.PluginResourceDao;
import org.vosao.dao.SeoUrlDao;
import org.vosao.dao.StructureDao;
import org.vosao.dao.StructureTemplateDao;
import org.vosao.dao.TagDao;
import org.vosao.dao.TemplateDao;
import org.vosao.dao.UserDao;
import org.vosao.dao.UserGroupDao;
import org.vosao.dao.cache.EntityCache;
import org.vosao.dao.cache.QueryCache;
import org.vosao.dao.cache.impl.EntityCacheImpl;
import org.vosao.dao.cache.impl.QueryCacheImpl;
import org.vosao.global.SystemService;

public class DaoImpl implements Dao, Serializable {

	private EntityCache entityCache;
	private QueryCache queryCache;
	
	private PageDao pageDao;
	private FileDao fileDao;
	private FileChunkDao fileChunkDao;
	private FolderDao folderDao;
	private UserDao userDao;
	private TemplateDao templateDao;
	private ConfigDao configDao;
	private FormDao formDao;
	private FormConfigDao formConfigDao;
	private CommentDao commentDao;
	private FieldDao fieldDao;
	private SeoUrlDao seoUrlDao;
	private LanguageDao languageDao;
	private ContentDao contentDao;
	private MessageDao messageDao;
	private GroupDao groupDao;
	private UserGroupDao userGroupDao;
	private ContentPermissionDao contentPermissionDao;
	private FolderPermissionDao folderPermissionDao;
	private StructureDao structureDao;
	private StructureTemplateDao structureTemplateDao;
	private PluginDao pluginDao;
	private PluginResourceDao pluginResourceDao;
	private TagDao tagDao;
	private PageTagDao pageTagDao;
	private FormDataDao formDataDao;
	private PageDependencyDao pageDependencyDao;
	private PageAttributeDao pageAttributeDao;

	@Override
	public SystemService getSystemService() {
		return VosaoContext.getInstance().getBusiness().getSystemService();
	}

	public PageDao getPageDao() {
		if (pageDao == null) {
			pageDao = new PageDaoImpl();
		}
		return pageDao; 
	}
	public void setPageDao(PageDao aPageDao) {
		pageDao = aPageDao;		
	}

	public FileDao getFileDao() {
		if (fileDao == null) {
			fileDao = new FileDaoImpl();
		}
		return fileDao;
	}
	public void setFileDao(FileDao aFileDao) {
		fileDao = aFileDao;		
	}

	public UserDao getUserDao() {
		if (userDao == null) {
			userDao = new UserDaoImpl();
		}
		return userDao;
	}
	public void setUserDao(UserDao aUserDao) {
		userDao = aUserDao;		
	}
	
	public FolderDao getFolderDao() {
		if (folderDao == null) {
			folderDao = new FolderDaoImpl();
		}
		return folderDao;
	}
	
	public void setFolderDao(FolderDao folderDao) {
		this.folderDao = folderDao;
	}
	
	public TemplateDao getTemplateDao() {
		if (templateDao == null) {
			templateDao = new TemplateDaoImpl();
		}
		return templateDao;
	}
	
	public void setTemplateDao(TemplateDao templateDao) {
		this.templateDao = templateDao;
	}

	public ConfigDao getConfigDao() {
		if (configDao == null) {
			configDao = new ConfigDaoImpl();
		}
		return configDao;
	}
	
	public void setConfigDao(ConfigDao configDao) {
		this.configDao = configDao;
	}

	public FormDao getFormDao() {
		if (formDao == null) {
			formDao = new FormDaoImpl();
		}
		return formDao;
	}

	public void setFormDao(FormDao formDao) {
		this.formDao = formDao;
	}

	public FormConfigDao getFormConfigDao() {
		if (formConfigDao == null) {
			formConfigDao = new FormConfigDaoImpl();
		}
		return formConfigDao;
	}

	public void setFormConfigDao(FormConfigDao formConfigDao) {
		this.formConfigDao = formConfigDao;
	}

	public CommentDao getCommentDao() {
		if (commentDao == null) {
			commentDao = new CommentDaoImpl();
		}
		return commentDao;
	}
	
	public void setCommentDao(CommentDao commentDao) {
		this.commentDao = commentDao;
	}

	public FieldDao getFieldDao() {
		if (fieldDao == null) {
			fieldDao = new FieldDaoImpl();
		}
		return fieldDao;
	}

	public void setFieldDao(FieldDao bean) {
		this.fieldDao = bean;
	}
	
	public SeoUrlDao getSeoUrlDao() {
		if (seoUrlDao == null) {
			seoUrlDao = new SeoUrlDaoImpl();
		}
		return seoUrlDao;
	}
	
	public void setSeoUrlDao(SeoUrlDao seoUrlDao) {
		this.seoUrlDao = seoUrlDao;
	}

	public LanguageDao getLanguageDao() {
		if (languageDao == null) {
			languageDao = new LanguageDaoImpl();
		}
		return languageDao;
	}
	
	public void setLanguageDao(LanguageDao bean) {
		this.languageDao = bean;
	}

	public ContentDao getContentDao() {
		if (contentDao == null) {
			contentDao = new ContentDaoImpl();
		}
		return contentDao;
	}
	
	public void setContentDao(ContentDao contentDao) {
		this.contentDao = contentDao;
	}

	public MessageDao getMessageDao() {
		if (messageDao == null) {
			messageDao = new MessageDaoImpl();
		}
		return messageDao;
	}
	
	public void setMessageDao(MessageDao messageDao) {
		this.messageDao = messageDao;
	}

	public GroupDao getGroupDao() {
		if (groupDao == null) {
			groupDao = new GroupDaoImpl();
		}
		return groupDao;
	}
	
	public void setGroupDao(GroupDao bean) {
		this.groupDao = bean;
	}

	public UserGroupDao getUserGroupDao() {
		if (userGroupDao == null) {
			userGroupDao = new UserGroupDaoImpl();
		}
		return userGroupDao;
	}
	
	public void setUserGroupDao(UserGroupDao bean) {
		this.userGroupDao = bean;
	}

	public ContentPermissionDao getContentPermissionDao() {
		if (contentPermissionDao == null) {
			contentPermissionDao = new ContentPermissionDaoImpl();
		}
		return contentPermissionDao;
	}
	
	public void setContentPermissionDao(ContentPermissionDao bean) {
		this.contentPermissionDao = bean;
	}

	public FolderPermissionDao getFolderPermissionDao() {
		if (folderPermissionDao == null) {
			folderPermissionDao = new FolderPermissionDaoImpl();
		}
		return folderPermissionDao;
	}
	
	public void setFolderPermissionDao(FolderPermissionDao bean) {
		this.folderPermissionDao = bean;
	}
	
	public StructureDao getStructureDao() {
		if (structureDao == null) {
			structureDao = new StructureDaoImpl();
		}
		return structureDao;
	}
	
	public void setStructureDao(StructureDao structureDao) {
		this.structureDao = structureDao;
	}
	
	public StructureTemplateDao getStructureTemplateDao() {
		if (structureTemplateDao == null) {
			structureTemplateDao = new StructureTemplateDaoImpl();
		}
		return structureTemplateDao;
	}
	
	public void setStructureTemplateDao(StructureTemplateDao structureTemplateDao) {
		this.structureTemplateDao = structureTemplateDao;
	}
	
	public EntityCache getEntityCache() {
		if (entityCache == null) {
			entityCache = new EntityCacheImpl();
		}
		return entityCache;
	}
	
	public void setEntityCache(EntityCache entityCache) {
		this.entityCache = entityCache;
	}
	
	public QueryCache getQueryCache() {
		if (queryCache == null) {
			queryCache = new QueryCacheImpl();
		}
		return queryCache;
	}
	
	public void setQueryCache(QueryCache queryCache) {
		this.queryCache = queryCache;
	}

	public PluginDao getPluginDao() {
		if (pluginDao == null) {
			pluginDao = new PluginDaoImpl();
		}
		return pluginDao;
	}
	
	public void setPluginDao(PluginDao value) {
		this.pluginDao = value;
	}

	public PluginResourceDao getPluginResourceDao() {
		if (pluginResourceDao == null) {
			pluginResourceDao = new PluginResourceDaoImpl();
		}
		return pluginResourceDao;
	}
	
	public void setPluginResourceDao(PluginResourceDao value) {
		this.pluginResourceDao = value;
	}

	@Override
	public FileChunkDao getFileChunkDao() {
		if (fileChunkDao == null) {
			fileChunkDao = new FileChunkDaoImpl();
		}
		return fileChunkDao;
	}

	@Override
	public void setFileChunkDao(FileChunkDao bean) {
		fileChunkDao = bean;
	}
	
	@Override
	public TagDao getTagDao() {
		if (tagDao == null) {
			tagDao = new TagDaoImpl();
		}
		return tagDao;
	}

	@Override
	public void setTagDao(TagDao bean) {
		tagDao = bean;
	}

	@Override
	public PageTagDao getPageTagDao() {
		if (pageTagDao == null) {
			pageTagDao = new PageTagDaoImpl();
		}
		return pageTagDao;
	}

	@Override
	public void setPageTagDao(PageTagDao bean) {
		pageTagDao = bean;
	}

	@Override
	public FormDataDao getFormDataDao() {
		if (formDataDao == null) {
			formDataDao = new FormDataDaoImpl();
		}
		return formDataDao;
	}

	@Override
	public void setFormDataDao(FormDataDao bean) {
		formDataDao = bean;
	}

	@Override
	public PageDependencyDao getPageDependencyDao() {
		if (pageDependencyDao == null) {
			pageDependencyDao = new PageDependencyDaoImpl();
		}
		return pageDependencyDao;
	}

	@Override
	public void setPageDependencyDao(PageDependencyDao bean) {
		pageDependencyDao = bean;
	}

	@Override
	public PageAttributeDao getPageAttributeDao() {
		if (pageAttributeDao == null) {
			pageAttributeDao = new PageAttributeDaoImpl();
		}
		return pageAttributeDao;
	}

	@Override
	public void setPageAttributeDao(PageAttributeDao bean) {
		pageAttributeDao = bean;
	}

}
