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

package org.vosao.business;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.vosao.business.decorators.TreeItemDecorator;
import org.vosao.entity.FolderEntity;

/**
 * @author Alexander Oleynik
 */
public interface FolderBusiness {

	FolderPermissionBusiness getFolderPermissionBusiness();
	void setFolderPermissionBusiness(FolderPermissionBusiness bean);

	/**
	 * Security filtered dao version.
	 * @return found folder.
	 */
	FolderEntity getById(final Long id);
	
	/**
	 * Security filtered dao version.
	 * @return found folders.
	 */
	List<FolderEntity> getByParent(final Long id);

	
	TreeItemDecorator<FolderEntity> getTree(final List<FolderEntity> list);

	TreeItemDecorator<FolderEntity> getTree();
	
	TreeItemDecorator<FolderEntity> findFolderByPath(
			final TreeItemDecorator<FolderEntity> root, final String path);
	
	List<String> validateBeforeUpdate(final FolderEntity folder);
	
	/**
	 * Create all directories in the path.
	 * @param path - directories path.
	 * @return last folder in the path.
	 * @throws UnsupportedEncodingException
	 */
	FolderEntity createFolder(final String path); 
	
	String getFolderPath(final FolderEntity folder);
	String getFolderPath(final FolderEntity folder, 
			TreeItemDecorator<FolderEntity> root);
	
	void recursiveRemove(final List<Long> folderIds);

	void recursiveRemove(final String path);
}
