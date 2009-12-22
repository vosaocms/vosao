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

package org.vosao.update;

import java.io.IOException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.dao.Dao;
import org.vosao.entity.ContentPermissionEntity;
import org.vosao.entity.FolderEntity;
import org.vosao.entity.FolderPermissionEntity;
import org.vosao.entity.GroupEntity;
import org.vosao.entity.LanguageEntity;
import org.vosao.entity.PageEntity;
import org.vosao.entity.TemplateEntity;
import org.vosao.enums.ContentPermissionType;
import org.vosao.enums.FolderPermissionType;
import org.vosao.enums.PageState;
import org.vosao.utils.StreamUtil;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;

/**
 * @author Alexander Oleynik
 */
public class UpdateTask01 implements UpdateTask {

	private static final Log logger = LogFactory.getLog(UpdateTask01.class);

	private Dao dao;
	private DatastoreService datastore;
	
	private Dao getDao() {
		return dao;
	}
	
	public UpdateTask01(Dao aDao) {
		dao = aDao;
	}
	
	@Override
	public String getFromVersion() {
		return "0.0.4";
	}

	@Override
	public String getToVersion() {
		return "0.1";
	}

	@Override
	public void update() throws UpdateException {
		datastore = DatastoreServiceFactory.getDatastoreService();
		updateConfig();
		addLoginPage();
		addGroups();
		setPermissions();
	}

	private void updateConfig() {
		Query query = new Query("ConfigEntity");
		for (Entity e : datastore.prepare(query).asIterable()) {
			e.setProperty("siteUserLoginUrl", "/login");
			datastore.put(e);
		}
	}

	private void addLoginPage() {
		List<TemplateEntity> templates = getDao().getTemplateDao().select();
		if (templates.size() == 0) {
			logger.error("No templates found. Can't add login page.");
			return;
		}
		PageEntity page = new PageEntity("Site user login","/login",
				templates.get(0).getId());
		page.setState(PageState.APPROVED);
		getDao().getPageDao().save(page);
		String content = loadResource("org/vosao/resources/html/login.html");
		getDao().getPageDao().setContent(page.getId(), 
				LanguageEntity.ENGLISH_CODE, content);
	}

	private String loadResource(final String url) {
		try {
			return StreamUtil.getTextResource(url);
		}
		catch(IOException e) {
			logger.error("Can't read file." + e);
			return "Error during load resources " + url;
		}
	}
	
	private void addGroups() {
		GroupEntity guests = getDao().getGroupDao().getGuestsGroup();
		if (guests == null) {
			guests = new GroupEntity("guests");
			getDao().getGroupDao().save(guests);
		}
	}
	
	private void setPermissions() {
		GroupEntity guests = getDao().getGroupDao().getGuestsGroup();
		ContentPermissionEntity contentPermission = new ContentPermissionEntity(
				"/", ContentPermissionType.READ, guests.getId()); 
		getDao().getContentPermissionDao().save(contentPermission);
		List<FolderEntity> folders = getDao().getFolderDao().getByParent(null);
		if (folders.size() > 0) {
			FolderEntity root = folders.get(0);
			FolderPermissionEntity folderPermission = new FolderPermissionEntity(
					root.getId(), FolderPermissionType.READ, guests.getId()); 
			getDao().getFolderPermissionDao().save(folderPermission);
		}
	}
	
}
