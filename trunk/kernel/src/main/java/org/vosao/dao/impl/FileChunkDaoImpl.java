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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.vosao.dao.BaseDaoImpl;
import org.vosao.dao.FileChunkDao;
import org.vosao.entity.FileChunkEntity;
import org.vosao.entity.FileEntity;

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
	
	private List<FileChunkEntity> getByFile(Long fileId) {
		Query q = newQuery();
		q.addFilter("fileId", EQUAL, fileId);
		List<FileChunkEntity> result = selectNotCache(q, "getByFile", params(fileId));
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

}
