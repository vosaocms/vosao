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

package org.vosao.business.impl.imex.task;

import org.vosao.dao.DaoTaskException;
import org.vosao.entity.CommentEntity;
import org.vosao.entity.ConfigEntity;
import org.vosao.entity.FieldEntity;
import org.vosao.entity.FileEntity;
import org.vosao.entity.FolderEntity;
import org.vosao.entity.FormConfigEntity;
import org.vosao.entity.FormEntity;
import org.vosao.entity.GroupEntity;
import org.vosao.entity.LanguageEntity;
import org.vosao.entity.MessageEntity;
import org.vosao.entity.PageEntity;
import org.vosao.entity.PluginEntity;
import org.vosao.entity.StructureEntity;
import org.vosao.entity.StructureTemplateEntity;
import org.vosao.entity.TemplateEntity;
import org.vosao.entity.UserEntity;
import org.vosao.entity.UserGroupEntity;

/**
 * @author Alexander Oleynik
 */
public interface DaoTaskAdapter {

	int getStart();

	void setStart(int value);

	int getEnd();
	
	void resetCounters();

	String getCurrentFile();

	void setCurrentFile(String file);

	void configSave(ConfigEntity entity) throws DaoTaskException;
	
	void languageSave(LanguageEntity entity) throws DaoTaskException;
	
	void folderSave(FolderEntity entity) throws DaoTaskException;

	void formSave(FormEntity entity) throws DaoTaskException;

	void fieldSave(FieldEntity entity) throws DaoTaskException;

	void formConfigSave(FormConfigEntity entity) throws DaoTaskException;

	void groupSave(GroupEntity entity) throws DaoTaskException;

	void userGroupSave(UserGroupEntity entity) throws DaoTaskException;

	void messageSave(MessageEntity entity) throws DaoTaskException;
	
	void pageSave(PageEntity entity) throws DaoTaskException;

	void setPageContent(Long pageId, String languageCode, String content) 
			throws DaoTaskException;

	void commentSave(CommentEntity entity) throws DaoTaskException;

	void fileSave(FileEntity entity, byte[] data) throws DaoTaskException;

	void structureSave(StructureEntity entity) throws DaoTaskException;

	void structureTemplateSave(StructureTemplateEntity entity) 	
			throws DaoTaskException;
	
	void templateSave(TemplateEntity entity) throws DaoTaskException;

	void userSave(UserEntity entity) throws DaoTaskException;

	void pluginSave(PluginEntity entity) throws DaoTaskException;
}