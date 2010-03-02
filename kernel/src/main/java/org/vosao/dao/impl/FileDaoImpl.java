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

import static com.google.appengine.api.datastore.Query.FilterOperator.EQUAL;

import java.util.Date;
import java.util.List;

import org.vosao.dao.BaseDaoImpl;
import org.vosao.dao.FileChunkDao;
import org.vosao.dao.FileDao;
import org.vosao.entity.FileEntity;

import com.google.appengine.api.datastore.Query;

public class FileDaoImpl extends BaseDaoImpl<FileEntity> 
		implements FileDao {

	private FileChunkDao fileChunkDao;
	
	public FileDaoImpl() {
		super(FileEntity.class);
	}

	@Override
	public void remove(final Long fileId) {
		if (fileId == null) {
			return;
		}
		getFileChunkDao().removeByFile(fileId);
		super.remove(fileId);
	}

	@Override
	public void remove(final List<Long> ids) {
		for (Long fileId : ids) {
			remove(fileId);
		}
	}

	@Override
	public List<FileEntity> getByFolder(Long folderId) {
		Query q = newQuery();
		q.addFilter("folderId", EQUAL, folderId);
		return select(q, "getByFolder", params(folderId));
	}

	@Override
	public FileEntity getByName(Long folderId, String name) {
		Query q = newQuery();
		q.addFilter("folderId", EQUAL, folderId);
		q.addFilter("filename", EQUAL, name);
		return selectOne(q, "getByName", params(folderId, name));
	}

	@Override
	public void save(FileEntity file, byte[] content) {
		file.setLastModifiedTime(new Date());
		file.setSize(content.length);
		save(file);
		getFileChunkDao().save(file, content);
	}

	@Override
	public void removeByFolder(Long folderId) {
		List<FileEntity> files = getByFolder(folderId);
		for (FileEntity file : files) {
			remove(file.getId());
		}
	}

	@Override
	public void removeAll() {
		super.removeAll();
		getFileChunkDao().removeAll();
	}

	public FileChunkDao getFileChunkDao() {
		return fileChunkDao;
	}

	public void setFileChunkDao(FileChunkDao fileChunkDao) {
		this.fileChunkDao = fileChunkDao;
	}

	@Override
	public byte[] getFileContent(FileEntity file) {
		return getFileChunkDao().getFileContent(file);
	}

}
