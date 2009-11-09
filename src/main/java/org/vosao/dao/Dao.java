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

package org.vosao.dao;

public interface Dao {

	PageDao getPageDao();
	void setPageDao(final PageDao pageDao);
	
	FileDao getFileDao();
	void setFileDao(final FileDao fileDao);

	FolderDao getFolderDao();
	void setFolderDao(final FolderDao folderDao);

	UserDao getUserDao();
	void setUserDao(final UserDao userDao);

	TemplateDao getTemplateDao();
	void setTemplateDao(final TemplateDao templateDao);

	ConfigDao getConfigDao();
	void setConfigDao(final ConfigDao configDao);

	FormDao getFormDao();
	void setFormDao(final FormDao formDao);
	
	FieldDao getFieldDao();
	void setFieldDao(final FieldDao bean);

	CommentDao getCommentDao();
	void setCommentDao(final CommentDao bean);

	SeoUrlDao getSeoUrlDao();
	void setSeoUrlDao(final SeoUrlDao bean);

	LanguageDao getLanguageDao();
	void setLanguageDao(final LanguageDao bean);

	ContentDao getContentDao();
	void setContentDao(final ContentDao bean);

	MessageDao getMessageDao();
	void setMessageDao(final MessageDao bean);

}
