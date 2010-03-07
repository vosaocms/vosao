package org.vosao.webdav;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.vosao.business.Business;
import org.vosao.entity.FileEntity;
import org.vosao.entity.FolderEntity;

import com.bradmcevoy.http.CollectionResource;
import com.bradmcevoy.http.Resource;

public class FolderResource extends AbstractResource implements 
		CollectionResource {

	private FolderEntity folder;
	
	public FolderResource(Business aBusiness, FolderEntity aFolder) {
		super(aBusiness, aFolder.getName(), new Date());
		folder = aFolder;
	}

	@Override
	public Resource child(String childName) {
		List<FolderEntity> children = getDao().getFolderDao().getByParent(
				folder.getId());
		for (FolderEntity child : children) {
			if (child.getName().equals(childName)) {
				return new FolderResource(getBusiness(), child);
			}
		}
		List<FileEntity> files = getDao().getFileDao().getByFolder(folder.getId());
		for (FileEntity file : files) {
			if (file.equals(childName)) {
				return new FileResource(getBusiness(), file);
			}
		}
		return null;
	}

	@Override
	public List<? extends Resource> getChildren() {
		List<Resource> result = new ArrayList<Resource>();
		List<FolderEntity> children = getDao().getFolderDao().getByParent(
				folder.getId());
		for (FolderEntity child : children) {
			result.add(new FolderResource(getBusiness(), child));
		}
		List<FileEntity> files = getDao().getFileDao().getByFolder(folder.getId());
		for (FileEntity file : files) {
			result.add(new FileResource(getBusiness(), file));
		}
		return result;
	}

}
