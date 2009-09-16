package org.vosao.dao;

public interface Dao {

	PageDao getPageDao();
	void setPageDao(final PageDao pageDao);
	
	FileDao getFileDao();
	void setFileDao(final FileDao fileDao);

	FolderDao getFolderDao();
	void setFolderDao(final FolderDao folderDao);

	UserDao getUserDao();
	void setUserDao(final UserDao userDao);

	TemplateDao getTemplateDao();
	void setTemplateDao(final TemplateDao templateDao);

	ConfigDao getConfigDao();
	void setConfigDao(final ConfigDao configDao);
	
}
