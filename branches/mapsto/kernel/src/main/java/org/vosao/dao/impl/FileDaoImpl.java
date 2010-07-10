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

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.mapsto.Filter;
import org.mapsto.Query;
import org.vosao.dao.BaseDaoImpl;
import org.vosao.dao.BaseMapstoDaoImpl;
import org.vosao.dao.FileChunkDao;
import org.vosao.dao.FileDao;
import org.vosao.entity.FileEntity;

public class FileDaoImpl extends BaseMapstoDaoImpl<FileEntity> 
		implements FileDao {

	public FileDaoImpl() {
		super("FileEntity");
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
	public void remove(Collection<Long> ids) {
		for (Long fileId : ids) {
			remove(fileId);
		}
	}

	@Override
	public List<FileEntity> getByFolder(Long folderId) {
		Query<FileEntity> q = newQuery();
		q.addFilter("folderId", Filter.EQUAL, folderId);
		return q.select("getByFolder", params(folderId));
	}

	@Override
	public FileEntity getByName(Long folderId, String name) {
		Query<FileEntity> q = newQuery();
		q.addFilter("folderId", Filter.EQUAL, folderId);
		q.addFilter("filename", Filter.EQUAL, name);
		return q.selectOne("getByName", params(folderId, name));
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
		return getDao().getFileChunkDao();
	}

	@Override
	public byte[] getFileContent(FileEntity file) {
		return getFileChunkDao().getFileContent(file);
	}

}
