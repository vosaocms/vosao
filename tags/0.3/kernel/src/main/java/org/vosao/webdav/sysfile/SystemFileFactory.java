/**
 * Vosao CMS. Simple CMS for Google App Engine.
 * Copyright (C) 2009 Vosao development team
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * email: vosao.dev@gmail.com
 */

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
import org.vosao.webdav.sysfile.local.CommentsFileFactory;
import org.vosao.webdav.sysfile.local.ContentFileFactory;
import org.vosao.webdav.sysfile.local.FolderFileFactory;
import org.vosao.webdav.sysfile.local.PagePermissionsFileFactory;
import org.vosao.webdav.sysfile.local.TemplateFileFactory;

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
			factories.add(new LanguagesFileFactory(getBusiness()));
			factories.add(new MessagesFileFactory(getBusiness()));
			factories.add(new UsersFileFactory(getBusiness()));
			factories.add(new GroupsFileFactory(getBusiness()));
			factories.add(new PluginsFileFactory(getBusiness()));
			factories.add(new FormsFileFactory(getBusiness()));
			factories.add(new SeourlsFileFactory(getBusiness()));
			factories.add(new StructuresFileFactory(getBusiness()));
			factories.add(new FolderFileFactory(getBusiness()));
			factories.add(new TemplateFileFactory(getBusiness()));
			factories.add(new ContentFileFactory(getBusiness()));
			factories.add(new CommentsFileFactory(getBusiness()));
			factories.add(new PagePermissionsFileFactory(getBusiness()));
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
		for (FileFactory factory : getFactories()) {
			if (factory.isCorrectPath(path)) {
				return true;
			}
		}
		return false;
	}

	public void addSystemFiles(List<Resource> resources, String path) {
		for (FileFactory factory : getFactories()) {
			if (factory.existsIn(path)) {
				String p = path.equals("/") ? "" : path;
				resources.add(factory.getFile(p + "/" + factory.getName()));
			}
		}
	}
}
