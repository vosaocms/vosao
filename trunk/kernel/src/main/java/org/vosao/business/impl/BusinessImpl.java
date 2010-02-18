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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.business.Business;
import org.vosao.business.CommentBusiness;
import org.vosao.business.ConfigBusiness;
import org.vosao.business.ContentPermissionBusiness;
import org.vosao.business.CurrentUser;
import org.vosao.business.FieldBusiness;
import org.vosao.business.FileBusiness;
import org.vosao.business.FolderBusiness;
import org.vosao.business.FolderPermissionBusiness;
import org.vosao.business.FormBusiness;
import org.vosao.business.GroupBusiness;
import org.vosao.business.MessageBusiness;
import org.vosao.business.PageBusiness;
import org.vosao.business.PluginBusiness;
import org.vosao.business.PluginResourceBusiness;
import org.vosao.business.StructureBusiness;
import org.vosao.business.StructureTemplateBusiness;
import org.vosao.business.TemplateBusiness;
import org.vosao.business.UserBusiness;
import org.vosao.business.UserPreferences;
import org.vosao.dao.Dao;
import org.vosao.entity.UserEntity;
import org.vosao.global.SystemService;
import org.vosao.search.SearchEngine;

public class BusinessImpl implements Business, Serializable {

	private static final Log log = LogFactory.getLog(BusinessImpl.class);

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

	public void init() {
	}
	
	@Override
	public UserPreferences getUserPreferences(final HttpServletRequest request) {
		String name = UserPreferences.class.getName();
		HttpSession session = request.getSession(true);
		if (session.getAttribute(name) == null) {
			session.setAttribute(name, new UserPreferences());
		}
		return (UserPreferences)session.getAttribute(name);
	}

	@Override
	public void setUserPreferences(UserPreferences bean, 
			final HttpServletRequest request) {
		String name = UserPreferences.class.getName();
		HttpSession session = request.getSession(true);
		session.setAttribute(name, bean);
	}

	@Override
	public UserEntity getUser() {
		return CurrentUser.getInstance();
	}
	
	@Override
	public PageBusiness getPageBusiness() {
		return pageBusiness;
	}

	@Override
	public void setPageBusiness(PageBusiness bean) {
		pageBusiness = bean;
	}
	
	@Override
	public FolderBusiness getFolderBusiness() {
		return folderBusiness;
	}

	@Override
	public void setFolderBusiness(FolderBusiness bean) {
		folderBusiness = bean;
	}

	@Override
	public TemplateBusiness getTemplateBusiness() {
		return templateBusiness;
	}

	@Override
	public void setTemplateBusiness(TemplateBusiness bean) {
		templateBusiness = bean;
	}

	@Override
	public ConfigBusiness getConfigBusiness() {
		return configBusiness;
	}
	@Override
	public void setConfigBusiness(ConfigBusiness bean) {
		configBusiness = bean;
	}

	@Override
	public FormBusiness getFormBusiness() {
		return formBusiness;
	}

	@Override
	public void setFormBusiness(FormBusiness formBusiness) {
		this.formBusiness = formBusiness;
	}

	@Override
	public FileBusiness getFileBusiness() {
		return fileBusiness;
	}

	@Override
	public void setFileBusiness(FileBusiness bean) {
		fileBusiness = bean;		
	}

	@Override
	public CommentBusiness getCommentBusiness() {
		return commentBusiness;
	}

	@Override
	public void setCommentBusiness(CommentBusiness bean) {
		commentBusiness = bean;
	}

	@Override
	public FieldBusiness getFieldBusiness() {
		return fieldBusiness;
	}

	@Override
	public void setFieldBusiness(FieldBusiness bean) {
		fieldBusiness = bean;
	}

	@Override
	public SystemService getSystemService() {
		return systemService;
	}

	@Override
	public void setSystemService(SystemService bean) {
		systemService = bean;
	}

	private static final String LANGUAGE_PARAM = "language";
	
	@Override
	public String getLanguage(HttpServletRequest request) {
		return (String)request.getSession(true).getAttribute(LANGUAGE_PARAM);
	}

	@Override
	public void setLanguage(String language, HttpServletRequest request) {
		request.getSession(true).setAttribute(LANGUAGE_PARAM, language);
	}

	@Override
	public MessageBusiness getMessageBusiness() {
		return messageBusiness;
	}

	@Override
	public void setMessageBusiness(MessageBusiness messageBusiness) {
		this.messageBusiness = messageBusiness;
	}
	
	@Override
	public UserBusiness getUserBusiness() {
		return userBusiness;
	}

	@Override
	public void setUserBusiness(UserBusiness bean) {
		this.userBusiness = bean;
	}

	@Override
	public ContentPermissionBusiness getContentPermissionBusiness() {
		return contentPermissionBusiness;
	}

	@Override
	public void setContentPermissionBusiness(ContentPermissionBusiness bean) {
		contentPermissionBusiness = bean;
	}

	@Override
	public GroupBusiness getGroupBusiness() {
		return groupBusiness;
	}

	@Override
	public void setGroupBusiness(GroupBusiness bean) {
		groupBusiness = bean;
	}

	@Override
	public FolderPermissionBusiness getFolderPermissionBusiness() {
		return folderPermissionBusiness;
	}

	@Override
	public void setFolderPermissionBusiness(FolderPermissionBusiness bean) {
		folderPermissionBusiness = bean;
	}

	@Override
	public StructureBusiness getStructureBusiness() {
		return structureBusiness;
	}

	@Override
	public void setStructureBusiness(StructureBusiness structureBusiness) {
		this.structureBusiness = structureBusiness;
	}

	@Override
	public StructureTemplateBusiness getStructureTemplateBusiness() {
		return structureTemplateBusiness;
	}

	@Override
	public void setStructureTemplateBusiness(
			StructureTemplateBusiness structureTemplateBusiness) {
		this.structureTemplateBusiness = structureTemplateBusiness;
	}
	
	@Override
	public PluginBusiness getPluginBusiness() {
		return pluginBusiness;
	}

	@Override
	public void setPluginBusiness(PluginBusiness bean) {
		this.pluginBusiness = bean;
	}
	
	@Override
	public PluginResourceBusiness getPluginResourceBusiness() {
		return pluginResourceBusiness;
	}

	@Override
	public void setPluginResourceBusiness(PluginResourceBusiness bean) {
		this.pluginResourceBusiness = bean;
	}
	
	@Override
	public Dao getDao() {
		return dao;
	}

	@Override
	public void setDao(Dao bean) {
		this.dao = bean;
	}

	public SearchEngine getSearchEngine() {
		return searchEngine;
	}

	public void setSearchEngine(SearchEngine searchEngine) {
		this.searchEngine = searchEngine;
	}
	
}
