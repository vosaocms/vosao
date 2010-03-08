package org.vosao.webdav.sysfile;

import java.util.ArrayList;
import java.util.List;

import org.vosao.business.Business;
import org.vosao.common.AbstractServiceBean;
import org.vosao.utils.FolderUtil;

import com.bradmcevoy.http.Resource;

public class SystemFileFactory extends AbstractServiceBean {

	private List<FileFactory> factories;

	public SystemFileFactory(Business aBusiness) {
		super(aBusiness);
	}

	public List<FileFactory> getFactories() {
		if (factories == null) {
			factories = new ArrayList<FileFactory>();
			factories.add(new ConfigFileFactory(getBusiness()));
			factories.add(new FolderFileFactory(getBusiness()));
			// TODO add more for every system file
		}
		return factories;
	}
	
	public Resource getSystemFile(String path) {
		for (FileFactory factory : getFactories()) {
			if (factory.isCorrectPath(path)) {
				return factory.getFile(path);
			}
		}
		return null;
	}

	public boolean isSystemFile(String path) {
		if (path.equals("/")) {
			return false;
		}
		for (FileFactory factory : factories) {
			if (factory.isCorrectPath(path)) {
				return true;
			}
		}
		return false;
	}

	public void addSystemFiles(List<Resource> resources, String path) {
		for (FileFactory factory : factories) {
			if (factory.existsIn(path)) {
				resources.add(factory.getFile(path + "/" + factory.getName()));
			}
		}
	}
}
