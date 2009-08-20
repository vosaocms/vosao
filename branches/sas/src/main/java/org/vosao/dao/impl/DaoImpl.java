package org.vosao.dao.impl;

import org.vosao.dao.Dao;
import org.vosao.dao.PageDao;

public class DaoImpl implements Dao {

	private PageDao pageDao;
	
	public PageDao getPageDao() {
		return pageDao;
	}

	public void setPageDao(PageDao aPageDao) {
		pageDao = aPageDao;		
	}

}
