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

package org.vosao.dao.impl;

import java.io.Serializable;

import org.vosao.dao.CommentDao;
import org.vosao.dao.ConfigDao;
import org.vosao.dao.ContentDao;
import org.vosao.dao.Dao;
import org.vosao.dao.FieldDao;
import org.vosao.dao.FileDao;
import org.vosao.dao.FolderDao;
import org.vosao.dao.FormDao;
import org.vosao.dao.LanguageDao;
import org.vosao.dao.PageDao;
import org.vosao.dao.SeoUrlDao;
import org.vosao.dao.TemplateDao;
import org.vosao.dao.UserDao;

public class DaoImpl implements Dao, Serializable {

	private PageDao pageDao;
	private FileDao fileDao;
	private FolderDao folderDao;
	private UserDao userDao;
	private TemplateDao templateDao;
	private ConfigDao configDao;
	private FormDao formDao;
	private CommentDao commentDao;
	private FieldDao fieldDao;
	private SeoUrlDao seoUrlDao;
	private LanguageDao languageDao;
	private ContentDao contentDao;
	
	public PageDao getPageDao() {
		return pageDao;
	}
	public void setPageDao(PageDao aPageDao) {
		pageDao = aPageDao;		
	}

	public FileDao getFileDao() {
		return fileDao;
	}
	public void setFileDao(FileDao aFileDao) {
		fileDao = aFileDao;		
	}

	public UserDao getUserDao() {
		return userDao;
	}
	public void setUserDao(UserDao aUserDao) {
		userDao = aUserDao;		
	}
	
	public FolderDao getFolderDao() {
		return folderDao;
	}
	
	public void setFolderDao(FolderDao folderDao) {
		this.folderDao = folderDao;
	}
	
	public TemplateDao getTemplateDao() {
		return templateDao;
	}
	
	public void setTemplateDao(TemplateDao templateDao) {
		this.templateDao = templateDao;
	}

	public ConfigDao getConfigDao() {
		return configDao;
	}
	
	public void setConfigDao(ConfigDao configDao) {
		this.configDao = configDao;
	}

	public FormDao getFormDao() {
		return formDao;
	}

	public void setFormDao(FormDao formDao) {
		this.formDao = formDao;
	}

	public CommentDao getCommentDao() {
		return commentDao;
	}
	
	public void setCommentDao(CommentDao commentDao) {
		this.commentDao = commentDao;
	}

	public FieldDao getFieldDao() {
		return fieldDao;
	}

	public void setFieldDao(FieldDao bean) {
		this.fieldDao = bean;
	}
	
	public SeoUrlDao getSeoUrlDao() {
		return seoUrlDao;
	}
	
	public void setSeoUrlDao(SeoUrlDao seoUrlDao) {
		this.seoUrlDao = seoUrlDao;
	}

	public LanguageDao getLanguageDao() {
		return languageDao;
	}
	
	public void setLanguageDao(LanguageDao bean) {
		this.languageDao = bean;
	}

	public ContentDao getContentDao() {
		return contentDao;
	}
	
	public void setContentDao(ContentDao contentDao) {
		this.contentDao = contentDao;
	}
	
}
