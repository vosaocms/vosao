package org.vosao.webdav.sysfile;

import java.util.ArrayList;
import java.util.List;

import org.vosao.business.Business;
import org.vosao.common.AbstractServiceBean;
import org.vosao.webdav.sysfile.global.ConfigFileFactory;
import org.vosao.webdav.sysfile.global.FormsFileFactory;
import org.vosao.webdav.sysfile.global.GroupsFileFactory;
import org.vosao.webdav.sysfile.global.LanguagesFileFactory;
import org.vosao.webdav.sysfile.global.MessagesFileFactory;
import org.vosao.webdav.sysfile.global.PluginsFileFactory;
import org.vosao.webdav.sysfile.global.SeourlsFileFactory;
import org.vosao.webdav.sysfile.global.StructuresFileFactory;
import org.vosao.webdav.sysfile.global.UsersFileFactory;
import org.vosao.webdav.sysfile.local.FolderFileFactory;

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
			factories.add(new LanguagesFileFactory(getBusiness()));
			factories.add(new MessagesFileFactory(getBusiness()));
			factories.add(new UsersFileFactory(getBusiness()));
			factories.add(new GroupsFileFactory(getBusiness()));
			factories.add(new PluginsFileFactory(getBusiness()));
			factories.add(new FormsFileFactory(getBusiness()));
			factories.add(new SeourlsFileFactory(getBusiness()));
			factories.add(new StructuresFileFactory(getBusiness()));
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
