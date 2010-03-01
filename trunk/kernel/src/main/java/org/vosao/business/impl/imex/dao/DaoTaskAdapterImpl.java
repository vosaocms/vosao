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

package org.vosao.business.impl.imex.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.dao.Dao;
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
public class DaoTaskAdapterImpl implements DaoTaskAdapter {

	private static final Log logger = LogFactory.getLog(DaoTaskAdapterImpl.class);

	private Dao dao;
	private int current;
	private int start;
	private long startTime = 0;
	
	public Dao getDao() {
		return dao;
	}

	public void setDao(Dao dao) {
		this.dao = dao;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int aStart) {
		this.start = aStart;
		current = 0;
		startTime = System.currentTimeMillis();
	}

	public int getEnd() {
		return current;
	}
	
	private boolean isSkip() throws DaoTaskException {
		current++;
		if (System.currentTimeMillis() - startTime > 24000) {
			throw new DaoTaskFinishedException();
		}
		if (current < start) {
			return true;
		}
		if (current == start) {
			//logger.info("current == start");
		}
		return false;
	}
	
	@Override
	public void configSave(ConfigEntity entity) throws DaoTaskException {
		if (isSkip()) {
			if (entity.getId() == null) {
				ConfigEntity config = getDao().getConfigDao().getConfig();
				entity.setId(config.getId());
			}
		}
		else {
			getDao().getConfigDao().save(entity);
		}
	}

	@Override
	public void languageSave(LanguageEntity entity) throws DaoTaskException {
		if (isSkip()) {
			if (entity.getId() == null) {
				LanguageEntity found = getDao().getLanguageDao().getByCode(
						entity.getCode());
				if (found == null) {
					throw new DaoTaskException("Language not found while " 
						+ "skipping save operation. code=" + entity.getCode());
				}
				entity.setId(found.getId());
			}
		}
		else {
			getDao().getLanguageDao().save(entity);
		}
	}
	
	@Override
	public void folderSave(FolderEntity entity) throws DaoTaskException {
		if (isSkip()) {
			if (entity.getId() == null) {
				FolderEntity found = getDao().getFolderDao().getByParentName(
						entity.getParent(), entity.getName());
				if (found == null) {
					throw new DaoTaskException("Folder not found while " 
						+ "skipping save operation. " + entity.getName());
				}
				entity.setId(found.getId());
			}
		}
		else {
			getDao().getFolderDao().save(entity);
		}
	}

	@Override
	public void formSave(FormEntity entity) throws DaoTaskException {
		if (isSkip()) {
			if (entity.getId() == null) {
				FormEntity found = getDao().getFormDao().getByName(
						entity.getName());
				if (found == null) {
					throw new DaoTaskException("Form not found while " 
						+ "skipping save operation. " + entity.getName());
				}
				entity.setKey(found.getKey());
			}
		}
		else {
			getDao().getFormDao().save(entity);
		}
	}

	@Override
	public void fieldSave(FieldEntity entity) throws DaoTaskException {
		if (isSkip()) {
			if (entity.getId() == null) {
				FormEntity form = getDao().getFormDao().getById(
						entity.getFormId());
				FieldEntity found = getDao().getFieldDao().getByName(form, 
						entity.getName());
				if (found == null) {
					throw new DaoTaskException("Field not found while " 
						+ "skipping save operation. " + entity.getName());
				}
				entity.setKey(found.getKey());
			}
		}
		else {
			getDao().getFieldDao().save(entity);
		}
	}
	
	@Override
	public void formConfigSave(FormConfigEntity entity) throws DaoTaskException {
		if (isSkip()) {
			if (entity.getId() == null) {
				FormConfigEntity found = getDao().getFormConfigDao().getConfig();
				if (found == null) {
					throw new DaoTaskException("Form config not found while " 
						+ "skipping save operation. ");
				}
				entity.setKey(found.getKey());
			}
		}
		else {
			getDao().getFormConfigDao().save(entity);
		}
	}

	@Override
	public void groupSave(GroupEntity entity) throws DaoTaskException {
		if (isSkip()) {
			if (entity.getId() == null) {
				GroupEntity found = getDao().getGroupDao().getByName(
						entity.getName());
				if (found == null) {
					throw new DaoTaskException("Group not found while " 
						+ "skipping save operation. " + entity.getName());
				}
				entity.setKey(found.getKey());
			}
		}
		else {
			getDao().getGroupDao().save(entity);
		}
	}
	
	@Override
	public void userGroupSave(UserGroupEntity entity) throws DaoTaskException {
		if (isSkip()) {
			if (entity.getId() == null) {
				UserGroupEntity found = getDao().getUserGroupDao()
					.getByUserGroup(entity.getGroupId(), entity.getUserId());
				if (found == null) {
					throw new DaoTaskException("UserGroup not found while " 
						+ "skipping save operation. group=" + entity.getGroupId()
						+ " user=" + entity.getUserId());
				}
				entity.setKey(found.getKey());
			}
		}
		else {
			getDao().getUserGroupDao().save(entity);
		}
	}

	@Override
	public void messageSave(MessageEntity entity) throws DaoTaskException {
		if (isSkip()) {
			if (entity.getId() == null) {
				MessageEntity found = getDao().getMessageDao().getByCode(
						entity.getCode(), entity.getLanguageCode());
				if (found == null) {
					throw new DaoTaskException("Message not found while " 
						+ "skipping save operation. code=" + entity.getCode()
						+ " language=" + entity.getLanguageCode());
				}
				entity.setId(found.getId());
			}
		}
		else {
			getDao().getMessageDao().save(entity);
		}
	}

	@Override
	public void pageSave(PageEntity entity) throws DaoTaskException {
		if (isSkip()) {
			//logger.info("skip " + entity.getFriendlyURL()	+ " version=" 
			//		+ entity.getVersion() + " current " + current);
			if (entity.getId() == null) {
				PageEntity found = getDao().getPageDao().getByUrlVersion(
						entity.getFriendlyURL(), entity.getVersion());
				if (found == null) {
					throw new DaoTaskException("Page not found while " 
						+ "skipping save operation. " + entity.getFriendlyURL()
						+ " version=" + entity.getVersion());
				}
				entity.setId(found.getId());
			}
		}
		else {
			//logger.info("import " + entity.getFriendlyURL()	+ " version=" 
			//		+ entity.getVersion() + " current " + current);
			getDao().getPageDao().save(entity);
		}
	}

	@Override
	public void commentSave(CommentEntity entity) throws DaoTaskException {
		if (!isSkip()) {
			getDao().getCommentDao().save(entity);
		}
	}

	@Override
	public void setPageContent(Long pageId, String languageCode,
			String content) throws DaoTaskException {
		if (!isSkip()) {
			getDao().getPageDao().setContent(pageId, languageCode, content);
		}
	}

	@Override
	public void fileSave(FileEntity entity, byte[] data) throws DaoTaskException {
		if (!isSkip()) {
			getDao().getFileDao().save(entity, data);
		}
	}
	
	@Override
	public void structureSave(StructureEntity entity) throws DaoTaskException {
		if (isSkip()) {
			if (entity.getId() == null) {
				StructureEntity found = getDao().getStructureDao().getByTitle(
						entity.getTitle());
				if (found == null) {
					throw new DaoTaskException("Structure not found while " 
						+ "skipping save operation. " + entity.getTitle());
				}
				entity.setId(found.getId());
			}
		}
		else {
			getDao().getStructureDao().save(entity);
		}
	}

	@Override
	public void structureTemplateSave(StructureTemplateEntity entity) 
			throws DaoTaskException {
		if (!isSkip()) {
			getDao().getStructureTemplateDao().save(entity);
		}
	}

	@Override
	public void templateSave(TemplateEntity entity) throws DaoTaskException {
		if (isSkip()) {
			if (entity.getId() == null) {
				TemplateEntity found = getDao().getTemplateDao().getByUrl(
						entity.getUrl());
				if (found == null) {
					throw new DaoTaskException("Template not found while " 
						+ "skipping save operation. " + entity.getTitle());
				}
				entity.setId(found.getId());
			}
		}
		else {
			getDao().getTemplateDao().save(entity);
		}
	}
	
	@Override
	public void userSave(UserEntity entity) throws DaoTaskException {
		if (isSkip()) {
			if (entity.getId() == null) {
				UserEntity found = getDao().getUserDao().getByEmail(
						entity.getEmail());
				if (found == null) {
					throw new DaoTaskException("User not found while " 
						+ "skipping save operation. " + entity.getEmail());
				}
				entity.setKey(found.getKey());
			}
		}
		else {
			getDao().getUserDao().save(entity);
		}
	}

	@Override
	public void pluginSave(PluginEntity entity) throws DaoTaskException {
		if (isSkip()) {
			if (entity.getId() == null) {
				PluginEntity found = getDao().getPluginDao().getByName(
						entity.getName());
				if (found == null) {
					throw new DaoTaskException("Plugin not found while " 
						+ "skipping save operation. " + entity.getName());
				}
				entity.setId(found.getId());
			}
		}
		else {
			getDao().getPluginDao().save(entity);
		}
	}
	
}
