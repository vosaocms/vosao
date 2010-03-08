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

package org.vosao.webdav;

import org.vosao.business.Business;
import org.vosao.business.decorators.TreeItemDecorator;
import org.vosao.dao.Dao;
import org.vosao.entity.FileEntity;
import org.vosao.entity.FolderEntity;
import org.vosao.webdav.sysfile.SystemFileFactory;

import com.bradmcevoy.http.Resource;
import com.bradmcevoy.http.ResourceFactory;

public class WebdavResourceFactory implements ResourceFactory {

	private Business business;
	private TreeItemDecorator<FolderEntity> root;
	private SystemFileFactory systemFileFactory;
	
	public WebdavResourceFactory() {
	}
	
	public Dao getDao() {
		return getBusiness().getDao();
	}

	public Business getBusiness() {
		return business;
	}

	public void setBusiness(Business business) {
		this.business = business;
	}

	@Override
	public Resource getResource(String host, String aPath) {
		String path = removeTrailingSlash(aPath.replace("/_ah/webdav", ""));
		if (getSystemFileFactory().isSystemFile(path)) {
			return getSystemFileFactory().getSystemFile(path);
		}
		root = getBusiness().getFolderBusiness().getTree();
		TreeItemDecorator<FolderEntity> folderItem = getBusiness()
				.getFolderBusiness()	.findFolderByPath(root, path);
		if (folderItem != null) {
			return getFolder(folderItem.getEntity(), aPath);
		}
		FileEntity file = getBusiness().getFileBusiness().findFile(path);
		if (file != null) {
			return getFile(file);
		}
		return null;
	}

	private String removeTrailingSlash(String path) {
		if (path.equals("")) {
			return "/";
		}
		if (path.charAt(path.length() - 1) == '/' && !path.equals("/")) {
			return path.substring(0, path.length() - 1);
		}
		return path;
	}
	
	private Resource getFolder(FolderEntity folder, String path) {
		return new FolderResource(getBusiness(), getSystemFileFactory(), 
				folder, path);
	}

	private Resource getFile(FileEntity file) {
		return new FileResource(getBusiness(), file);
	}

	public SystemFileFactory getSystemFileFactory() {
		if (systemFileFactory == null) {
			systemFileFactory = new SystemFileFactory(getBusiness());
		}
		return systemFileFactory;
	}
	
}
