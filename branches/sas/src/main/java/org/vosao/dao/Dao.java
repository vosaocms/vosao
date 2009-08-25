package org.vosao.dao;

public interface Dao {

	PageDao getPageDao();
	void setPageDao(final PageDao pageDao);
	
	FileDao getFileDao();
	void setFileDao(final FileDao fileDao);

	UserDao getUserDao();
	void setUserDao(final UserDao userDao);
	
}
