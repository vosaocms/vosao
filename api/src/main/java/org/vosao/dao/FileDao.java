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

package org.vosao.dao;

import java.util.List;

import org.vosao.entity.FileEntity;

public interface FileDao extends BaseDao<FileEntity> {

	List<FileEntity> getByFolder(final Long folderId);

	FileEntity getByName(final Long folderId, final String name);

	void save(final FileEntity file, byte[] content);
	
	byte[] getFileContent(final FileEntity file);
	
	void removeByFolder(final Long folderId);

}
