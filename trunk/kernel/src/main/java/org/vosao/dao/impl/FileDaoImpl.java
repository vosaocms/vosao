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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;

import org.vosao.dao.FileDao;
import org.vosao.entity.FileChunkEntity;
import org.vosao.entity.FileEntity;

public class FileDaoImpl extends BaseDaoImpl<String, FileEntity> 
		implements FileDao {

	public FileDaoImpl() {
		super(FileEntity.class);
	}

	@Override
	public void remove(final String fileId) {
		if (fileId == null) {
			return;
		}
		PersistenceManager pm = getPersistenceManager();
		try {
			removeChunks(pm, fileId);
			pm.deletePersistent(pm.getObjectById(FileEntity.class, fileId));
		}
		finally {
			pm.close();
		}
	}

	private void removeChunks(PersistenceManager pm, final String fileId) {
		List<FileChunkEntity> chunks = getByFile(fileId);
		for (FileChunkEntity chunk : chunks) {
			pm.deletePersistent(pm.getObjectById(FileChunkEntity.class, 
					chunk.getId()));
		}
	}
	
	@Override
	public void remove(final List<String> ids) {
		PersistenceManager pm = getPersistenceManager();
		try {
			for (String fileId : ids) {
				removeChunks(pm, fileId);
				pm.deletePersistent(pm.getObjectById(FileEntity.class, fileId));
			}
		}
		finally {
			pm.close();
		}
	}

	@Override
	public List<FileEntity> getByFolder(String folderId) {
		String query = "select from " + FileEntity.class.getName()
			    + " where folderId == pFolderId parameters String pFolderId";
		return select(query, params(folderId));
	}

	@Override
	public FileEntity getByName(String folderId, String name) {
		String query = "select from " + FileEntity.class.getName()
			    + " where folderId == pFolderId && filename == pName" 
			    + " parameters String pFolderId, String pName";
		return selectOne(query, params(folderId, name));
	}

	@Override
	public void save(FileEntity file, byte[] content) {
		file.setLastModifiedTime(new Date());
		file.setSize(content.length);
		save(file);
		removeChunks(getByFile(file.getId()));
		List<FileChunkEntity> chunks = createChunks(file, content);
		save(chunks);
	}
	
	public static int ENTITY_SIZE_LIMIT = 1000000; 
	
	@Override
	public List<FileChunkEntity> createChunks(FileEntity file, 
			byte[] content) {
		List<FileChunkEntity> result = new ArrayList<FileChunkEntity>();
		long n = content.length / ENTITY_SIZE_LIMIT;
		int finalChunkSize = content.length % ENTITY_SIZE_LIMIT;
		int i = 0;
		int start;
		int end;
		for (i = 0; i < n; i++) {
			start = i * ENTITY_SIZE_LIMIT;
			end = start + ENTITY_SIZE_LIMIT;
			result.add(new FileChunkEntity(file.getId(), 
					Arrays.copyOfRange(content, start, end), i));
		}
		if (finalChunkSize > 0) {
			start = i * ENTITY_SIZE_LIMIT;
			end = start + finalChunkSize;
			result.add(new FileChunkEntity(file.getId(), 
					Arrays.copyOfRange(content, start, end), i));
		}
		return result;
	}
	
	private void save(final List<FileChunkEntity> list) {
		PersistenceManager pm = getPersistenceManager();
		try {
			for (FileChunkEntity entity : list) {
				if (entity.getId() != null) {
					FileChunkEntity p = pm.getObjectById(FileChunkEntity.class, 
						entity.getId());
					p.copy(entity);
				}
				else {
					pm.makePersistent(entity);
				}
			}
		}
		finally {
			pm.close();
		}
	}

	private List<FileChunkEntity> getByFile(String fileId) {
		PersistenceManager pm = getPersistenceManager();
		try {
			String query = "select from " + FileChunkEntity.class.getName()
			    + " where fileId == pFileId" 
			    + " parameters String pFileId";
			List<FileChunkEntity> result = (List<FileChunkEntity>)pm.newQuery(query)
				.execute(fileId);
			Collections.sort(result, new Comparator<FileChunkEntity>() {

				@Override
				public int compare(FileChunkEntity o1, FileChunkEntity o2) {
					if (o1.getIndex() > o2.getIndex()) {
						return 1;
					}
					if (o1.getIndex() < o2.getIndex()) {
						return -1;
					}
					return 0;
				}
				
			});
			
			return result;
		}
		finally {
			pm.close();
		}
	}

	@Override
	public byte[] getFileContent(FileEntity file) {
		List<FileChunkEntity> list = getByFile(file.getId());
		int size = 0;
		for (FileChunkEntity chunk : list) {
			size += chunk.getContent().length;
		}
		byte[] result = new byte[size];
		int i = 0;
		int start = 0;
		for (FileChunkEntity chunk : list) {
			start = i * ENTITY_SIZE_LIMIT;
			System.arraycopy(chunk.getContent(), 0, result, start, 
					chunk.getContent().length);
			i++;
		}
		return result;
	}
	
	private void removeChunks(final List<FileChunkEntity> chunks) {
		PersistenceManager pm = getPersistenceManager();
		try {
			for (FileChunkEntity entity : chunks) {
				pm.deletePersistent(pm.getObjectById(FileChunkEntity.class, 
						entity.getId()));
			}
		}
		finally {
			pm.close();
		}
	}

	@Override
	public void removeByFolder(String folderId) {
		List<FileEntity> files = getByFolder(folderId);
		List<String> ids = new ArrayList<String>();
		for (FileEntity file : files) {
			ids.add(file.getId());
		}
		remove(ids);
	}
	

}
