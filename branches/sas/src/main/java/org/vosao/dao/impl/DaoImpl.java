package org.vosao.dao.impl;

import org.vosao.dao.Dao;
import org.vosao.dao.FileDao;
import org.vosao.dao.FolderDao;
import org.vosao.dao.PageDao;
import org.vosao.dao.UserDao;

public class DaoImpl implements Dao {

	private PageDao pageDao;
	private FileDao fileDao;
	private FolderDao folderDao;
	private UserDao userDao;
	
	public PageDao getPageDao() {
		return pageDao;
	}
	public void setPageDao(PageDao aPageDao) {
		pageDao = aPageDao;		
	}

	public FileDao getFileDao() {
		return fileDao;
	}
	public void setFileDao(FileDao aFileDao) {
		fileDao = aFileDao;		
	}

	public UserDao getUserDao() {
		return userDao;
	}
	public void setUserDao(UserDao aUserDao) {
		userDao = aUserDao;		
	}
	
	public FolderDao getFolderDao() {
		return folderDao;
	}
	
	public void setFolderDao(FolderDao folderDao) {
		this.folderDao = folderDao;
	}
	
}
