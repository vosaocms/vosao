package org.vosao.dao.impl;

import org.vosao.dao.Dao;
import org.vosao.dao.FileDao;
import org.vosao.dao.PageDao;

public class DaoImpl implements Dao {

	private PageDao pageDao;
	private FileDao fileDao;
	
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
	
}
