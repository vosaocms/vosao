package org.vosao.webdav;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.vosao.business.Business;
import org.vosao.business.decorators.TreeItemDecorator;
import org.vosao.dao.Dao;
import org.vosao.entity.FileEntity;
import org.vosao.entity.FolderEntity;
import org.vosao.servlet.FolderUtil;
import org.vosao.webdav.sysfile.ConfigFileFactory;
import org.vosao.webdav.sysfile.FileFactory;

import com.bradmcevoy.http.Resource;
import com.bradmcevoy.http.ResourceFactory;

public class WebdavResourceFactory implements ResourceFactory {

	private Business business;
	private TreeItemDecorator<FolderEntity> root;
	private List<FileFactory> systemFileFactories;
	
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
		if (isSystemFile(path)) {
			return getSystemFile(path);
		}
		root = getBusiness().getFolderBusiness().getTree();
		TreeItemDecorator<FolderEntity> folderItem = getBusiness()
				.getFolderBusiness()	.findFolderByPath(root, path);
		if (folderItem != null) {
			return getFolder(folderItem.getEntity());
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
	
	private final String[] SYSTEMFILES = {"_config.xml", "_comments.xml",
			"_languages.xml", "_messages.xml", "_users.xml", "_groups.xml",
			"_plugins.xml", "_forms.xml", "_seourls.xml", "_structures.xml",
			"_structure_templates.xml",
			
			"_folder.xml",
			
			"_template.xml",
			
			"_content.xml", "_comments.xml", "_page_permissions.xml"
	}; 
	
	private boolean isSystemFile(String path) {
		if (path.equals("/")) {
			return false;
		}
		String filename = FolderUtil.getFolderName(path);
		for (String s : SYSTEMFILES) {
			if (s.equals(filename)) {
				return true;
			}
		}
		return false;
	}

	private Resource getSystemFile(String path) {
		String filename = FolderUtil.getFolderName(path);
		for (FileFactory factory : getSystemFileFactories()) {
			if (factory.isCorrectPath(path)) {
				return getXMLFile(filename, factory.getFile(path));
			}
		}
		return null;
	}
	
	private Resource getXMLFile(String name, String xml) {
		return new SystemFileResource(getBusiness(), name, new Date(), xml);
	}

	private Resource getFolder(FolderEntity folder) {
		return new FolderResource(getBusiness(), folder);
	}

	private Resource getFile(FileEntity file) {
		return new FileResource(getBusiness(), file);
	}

	private List<FileFactory> getSystemFileFactories() {
		if (systemFileFactories == null) {
			systemFileFactories = new ArrayList<FileFactory>();
			systemFileFactories.add(new ConfigFileFactory(getBusiness()));
			// TODO add more for every system file
		}
		return systemFileFactories;
	}
	
}
