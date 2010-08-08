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

package org.vosao.business.impl;

import java.io.Serializable;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.vosao.business.Business;
import org.vosao.business.CommentBusiness;
import org.vosao.business.ConfigBusiness;
import org.vosao.business.ContentPermissionBusiness;
import org.vosao.business.FieldBusiness;
import org.vosao.business.FileBusiness;
import org.vosao.business.FolderBusiness;
import org.vosao.business.FolderPermissionBusiness;
import org.vosao.business.FormBusiness;
import org.vosao.business.FormDataBusiness;
import org.vosao.business.GroupBusiness;
import org.vosao.business.ImportExportBusiness;
import org.vosao.business.MessageBusiness;
import org.vosao.business.PageBusiness;
import org.vosao.business.PicasaBusiness;
import org.vosao.business.PluginBusiness;
import org.vosao.business.PluginResourceBusiness;
import org.vosao.business.SetupBean;
import org.vosao.business.StructureBusiness;
import org.vosao.business.StructureTemplateBusiness;
import org.vosao.business.TagBusiness;
import org.vosao.business.TemplateBusiness;
import org.vosao.business.UserBusiness;
import org.vosao.business.mq.MessageQueue;
import org.vosao.common.VosaoContext;
import org.vosao.dao.Dao;
import org.vosao.dao.impl.DaoImpl;
import org.vosao.entity.UserEntity;
import org.vosao.global.SystemService;
import org.vosao.global.impl.SystemServiceImpl;
import org.vosao.search.SearchEngine;
import org.vosao.search.impl.SearchEngineImpl;

/**
 * 
 * @author Alexander Oleynik
 *
 */
public class BusinessImpl implements Business, Serializable {

	private SystemService systemService;
	private Dao dao;
	private SearchEngine searchEngine;
	
	private PageBusiness pageBusiness;
	private FolderBusiness folderBusiness;
	private TemplateBusiness templateBusiness;
	private ConfigBusiness configBusiness;
	private FormBusiness formBusiness;
	private FileBusiness fileBusiness;
	private CommentBusiness commentBusiness;
	private FieldBusiness fieldBusiness;
	private MessageBusiness messageBusiness;
	private UserBusiness userBusiness;
	private ContentPermissionBusiness contentPermissionBusiness;
	private GroupBusiness groupBusiness;
	private FolderPermissionBusiness folderPermissionBusiness;
	private StructureBusiness structureBusiness;
	private StructureTemplateBusiness structureTemplateBusiness;
	private PluginBusiness pluginBusiness;
	private PluginResourceBusiness pluginResourceBusiness;
	private ImportExportBusiness importExportBusiness;
	private TagBusiness tagBusiness;
	private PicasaBusiness picasaBusiness;
	private FormDataBusiness formDataBusiness;

	private SetupBean setupBean;

	@Override
	public UserEntity getUser() {
		return VosaoContext.getInstance().getUser();
	}
	
	@Override
	public MessageQueue getMessageQueue() {
		return VosaoContext.getInstance().getMessageQueue();
	}

	@Override
	public PageBusiness getPageBusiness() {
		if (pageBusiness == null) {
			pageBusiness = new PageBusinessImpl();
		}
		return pageBusiness;
	}

	@Override
	public void setPageBusiness(PageBusiness bean) {
		pageBusiness = bean;
	}
	
	@Override
	public FolderBusiness getFolderBusiness() {
		if (folderBusiness == null) {
			folderBusiness = new FolderBusinessImpl();
		}
		return folderBusiness;
	}

	@Override
	public void setFolderBusiness(FolderBusiness bean) {
		folderBusiness = bean;
	}

	@Override
	public TemplateBusiness getTemplateBusiness() {
		if (templateBusiness == null) {
			templateBusiness = new TemplateBusinessImpl();
		}
		return templateBusiness;
	}

	@Override
	public void setTemplateBusiness(TemplateBusiness bean) {
		templateBusiness = bean;
	}

	@Override
	public ConfigBusiness getConfigBusiness() {
		if (configBusiness == null) {
			configBusiness = new ConfigBusinessImpl();
		}
		return configBusiness;
	}
	@Override
	public void setConfigBusiness(ConfigBusiness bean) {
		configBusiness = bean;
	}

	@Override
	public FormBusiness getFormBusiness() {
		if (formBusiness == null) {
			formBusiness = new FormBusinessImpl();
		}
		return formBusiness;
	}

	@Override
	public void setFormBusiness(FormBusiness formBusiness) {
		this.formBusiness = formBusiness;
	}

	@Override
	public FileBusiness getFileBusiness() {
		if (fileBusiness == null) {
			fileBusiness = new FileBusinessImpl();
		}
		return fileBusiness;
	}

	@Override
	public void setFileBusiness(FileBusiness bean) {
		fileBusiness = bean;		
	}

	@Override
	public CommentBusiness getCommentBusiness() {
		if (commentBusiness == null) {
			commentBusiness = new CommentBusinessImpl();
		}
		return commentBusiness;
	}

	@Override
	public void setCommentBusiness(CommentBusiness bean) {
		commentBusiness = bean;
	}

	@Override
	public FieldBusiness getFieldBusiness() {
		if (fieldBusiness == null) {
			fieldBusiness = new FieldBusinessImpl();
		}
		return fieldBusiness;
	}

	@Override
	public void setFieldBusiness(FieldBusiness bean) {
		fieldBusiness = bean;
	}

	@Override
	public SystemService getSystemService() {
		if (systemService == null) {
			systemService = new SystemServiceImpl();
		}
		return systemService;
	}

	@Override
	public void setSystemService(SystemService bean) {
		systemService = bean;
	}

	@Override
	public String getLanguage() {
		return VosaoContext.getInstance().getLanguage();
	}

	@Override
	public MessageBusiness getMessageBusiness() {
		if (messageBusiness == null) {
			messageBusiness = new MessageBusinessImpl();
		}
		return messageBusiness;
	}

	@Override
	public void setMessageBusiness(MessageBusiness messageBusiness) {
		this.messageBusiness = messageBusiness;
	}
	
	@Override
	public UserBusiness getUserBusiness() {
		if (userBusiness == null) {
			userBusiness = new UserBusinessImpl();
		}
		return userBusiness;
	}

	@Override
	public void setUserBusiness(UserBusiness bean) {
		this.userBusiness = bean;
	}

	@Override
	public ContentPermissionBusiness getContentPermissionBusiness() {
		if (contentPermissionBusiness == null) {
			contentPermissionBusiness = new ContentPermissionBusinessImpl();
		}
		return contentPermissionBusiness;
	}

	@Override
	public void setContentPermissionBusiness(ContentPermissionBusiness bean) {
		contentPermissionBusiness = bean;
	}

	@Override
	public GroupBusiness getGroupBusiness() {
		if (groupBusiness == null) {
			groupBusiness = new GroupBusinessImpl();
		}
		return groupBusiness;
	}

	@Override
	public void setGroupBusiness(GroupBusiness bean) {
		groupBusiness = bean;
	}

	@Override
	public FolderPermissionBusiness getFolderPermissionBusiness() {
		if (folderPermissionBusiness == null) {
			folderPermissionBusiness = new FolderPermissionBusinessImpl();
		}
		return folderPermissionBusiness;
	}

	@Override
	public void setFolderPermissionBusiness(FolderPermissionBusiness bean) {
		folderPermissionBusiness = bean;
	}

	@Override
	public StructureBusiness getStructureBusiness() {
		if (structureBusiness == null) {
			structureBusiness = new StructureBusinessImpl();
		}
		return structureBusiness;
	}

	@Override
	public void setStructureBusiness(StructureBusiness structureBusiness) {
		this.structureBusiness = structureBusiness;
	}

	@Override
	public StructureTemplateBusiness getStructureTemplateBusiness() {
		if (structureTemplateBusiness == null) {
			structureTemplateBusiness = new StructureTemplateBusinessImpl();
		}
		return structureTemplateBusiness;
	}

	@Override
	public void setStructureTemplateBusiness(
			StructureTemplateBusiness structureTemplateBusiness) {
		this.structureTemplateBusiness = structureTemplateBusiness;
	}
	
	@Override
	public PluginBusiness getPluginBusiness() {
		if (pluginBusiness == null) {
			pluginBusiness = new PluginBusinessImpl();
		}
		return pluginBusiness;
	}

	@Override
	public void setPluginBusiness(PluginBusiness bean) {
		this.pluginBusiness = bean;
	}
	
	@Override
	public PluginResourceBusiness getPluginResourceBusiness() {
		if (pluginResourceBusiness == null) {
			pluginResourceBusiness = new PluginResourceBusinessImpl();
		}
		return pluginResourceBusiness;
	}

	@Override
	public void setPluginResourceBusiness(PluginResourceBusiness bean) {
		this.pluginResourceBusiness = bean;
	}
	
	@Override
	public Dao getDao() {
		if (dao == null) {
			dao = new DaoImpl();
		}
		return dao;
	}

	@Override
	public void setDao(Dao bean) {
		this.dao = bean;
	}

	public SearchEngine getSearchEngine() {
		if (searchEngine == null) {
			searchEngine = new SearchEngineImpl();
		}
		return searchEngine;
	}

	public void setSearchEngine(SearchEngine searchEngine) {
		this.searchEngine = searchEngine;
	}

	@Override
	public ImportExportBusiness getImportExportBusiness() {
		if (importExportBusiness == null) {
			importExportBusiness = new ImportExportBusinessImpl();
		}
		return importExportBusiness;
	}

	@Override
	public void setImportExportBusiness(ImportExportBusiness bean) {
		importExportBusiness = bean;
	}

	@Override
	public TagBusiness getTagBusiness() {
		if (tagBusiness == null) {
			tagBusiness = new TagBusinessImpl();
		}
		return tagBusiness;
	}

	@Override
	public void setTagBusiness(TagBusiness bean) {
		tagBusiness = bean;
	}

	@Override
	public PicasaBusiness getPicasaBusiness() {
		if (picasaBusiness == null) {
			picasaBusiness = new PicasaBusinessImpl();
		}
		return picasaBusiness;
	}

	@Override
	public void setPicasaBusiness(PicasaBusiness bean) {
		picasaBusiness = bean;
	}

	@Override
	public SetupBean getSetupBean() {
		if (setupBean == null) {
			setupBean = new SetupBeanImpl();
		}
		return setupBean;
	}

	@Override
	public void setSetupBean(SetupBean bean) {
		setupBean = bean;
	}

	@Override
	public FormDataBusiness getFormDataBusiness() {
		if (formDataBusiness == null) {
			formDataBusiness = new FormDataBusinessImpl();
		}
		return formDataBusiness;
	}

	@Override
	public void setFormDataBusiness(FormDataBusiness bean) {
		formDataBusiness = bean;
	}

	@Override
	public TimeZone getTimeZone() {
		if (getUser() != null 
				&& !StringUtils.isEmpty(getUser().getTimezone())) {
			return TimeZone.getTimeZone(getUser().getTimezone());
		}
		return TimeZone.getDefault();
	}

}
