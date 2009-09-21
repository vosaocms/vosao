package org.vosao.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.jdo.PersistenceManager;

import org.vosao.dao.FileDao;
import org.vosao.entity.FileChunkEntity;
import org.vosao.entity.FileEntity;

public class FileDaoImpl extends AbstractDaoImpl implements FileDao {

	public void save(final FileEntity entity) {
		PersistenceManager pm = getPersistenceManager();
		try {
			if (entity.getId() != null) {
				FileEntity p = pm.getObjectById(FileEntity.class, entity.getId());
				p.copy(entity);
			}
			else {
				pm.makePersistent(entity);
			}
		}
		finally {
			pm.close();
		}
	}
	
	public FileEntity getById(final String id) {
		if (id == null) {
			return null;
		}
		PersistenceManager pm = getPersistenceManager();
		try {
			return pm.getObjectById(FileEntity.class, id);
		}
		finally {
			pm.close();
		}
	}
	
	public void remove(final String id) {
		if (id == null) {
			return;
		}
		PersistenceManager pm = getPersistenceManager();
		try {
			pm.deletePersistent(pm.getObjectById(FileEntity.class, id));
		}
		finally {
			pm.close();
		}
	}
	
	public void remove(final List<String> ids) {
		PersistenceManager pm = getPersistenceManager();
		try {
			for (String id : ids) {
				pm.deletePersistent(pm.getObjectById(FileEntity.class, id));
			}
		}
		finally {
			pm.close();
		}
	}

	@Override
	public List<FileEntity> getByFolder(String folderId) {
		PersistenceManager pm = getPersistenceManager();
		try {
			String query = "select from " + FileEntity.class.getName()
			    + " where folderId == pFolderId parameters String pFolderId";
			List<FileEntity> result = (List<FileEntity>)pm.newQuery(query)
				.execute(folderId);
			return copy(result);
		}
		finally {
			pm.close();
		}
	}

	@Override
	public FileEntity getByName(String folderId, String name) {
		PersistenceManager pm = getPersistenceManager();
		try {
			String query = "select from " + FileEntity.class.getName()
			    + " where folderId == pFolderId && filename == pName" 
			    + " parameters String pFolderId, String pName";
			List<FileEntity> result = (List<FileEntity>)pm.newQuery(query)
				.execute(folderId, name);
			if (result.size() > 0) {
				return result.get(0);
			}
			return null;
		}
		finally {
			pm.close();
		}
	}

	@Override
	public void saveFileContent(FileEntity file, byte[] content) {
		removeChunks(getByFile(file.getId()));
		List<FileChunkEntity> chunks = createChunks(file, content);
		save(chunks);
	}
	
	public static int ENTITY_SIZE_LIMIT = 1000000; 
	
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
				pm.deletePersistent(pm.getObjectById(FileEntity.class, 
						entity.getId()));
			}
		}
		finally {
			pm.close();
		}
	}
	

}
