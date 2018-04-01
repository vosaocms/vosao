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

package org.vosao.dao.impl;

import static com.google.appengine.api.datastore.Query.FilterOperator.EQUAL;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.vosao.dao.BaseDaoImpl;
import org.vosao.dao.FileChunkDao;
import org.vosao.entity.FileChunkEntity;
import org.vosao.entity.FileEntity;
import org.vosao.utils.ArrayUtil;

import com.google.appengine.api.datastore.Query;

public class FileChunkDaoImpl extends BaseDaoImpl<FileChunkEntity> 
		implements FileChunkDao {

	public FileChunkDaoImpl() {
		super(FileChunkEntity.class);
	}

	@Override
	public void removeByFile(final Long fileId) {
		if (fileId == null) {
			return;
		}
		Query q = newQuery();
		q.addFilter("fileId", EQUAL, fileId);
		removeSelected(q);
	}

	@Override
	public void save(FileEntity file, byte[] content) {
		removeByFile(file.getId());
		List<FileChunkEntity> chunks = createChunks(file, content);
		for (FileChunkEntity chunk : chunks) {
			save(chunk);
		}
	}
	
	public static int ENTITY_SIZE_LIMIT = 1000000; 
	
	@Override
	public List<FileChunkEntity> createChunks(FileEntity file, 
			byte[] content) {
		List<FileChunkEntity> result = new ArrayList<FileChunkEntity>();
		List<byte[]> chunks = ArrayUtil.makeChunks(content, ENTITY_SIZE_LIMIT);
		int i = 0;
		for (byte[] chunk : chunks) {
			result.add(new FileChunkEntity(file.getId(), chunk, i++));
		}
		return result;
	}
	
	private List<FileChunkEntity> getByFile(Long fileId) {
		Query q = newQuery();
		q.addFilter("fileId", EQUAL, fileId);
		List<FileChunkEntity> result = selectNotCache(q);
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

	@Override
	public byte[] getFileContent(FileEntity file) {
		List<byte[]> chunks = new ArrayList<byte[]>();
		for (FileChunkEntity chunk : getByFile(file.getId())) {
			chunks.add(chunk.getContent());
		}
		return ArrayUtil.packChunks(chunks);
	}

}
