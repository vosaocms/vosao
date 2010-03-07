package org.vosao.webdav.sysfile;

import org.vosao.business.Business;

public class ConfigFileFactory extends AbstractFileFactory {

	public ConfigFileFactory(Business business) {
		super(business);
	}
	
	@Override
	public String getFile(String path) {
		return "config xml content";
	}

	@Override
	public boolean isCorrectPath(String path) {
		return "/_config.xml".equals(path);
	}

	@Override
	public void setFile(String path, String xml) {
		// TODO Auto-generated method stub
	}

}
