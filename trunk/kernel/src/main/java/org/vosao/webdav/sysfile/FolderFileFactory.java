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

import org.vosao.business.Business;
import org.vosao.business.decorators.TreeItemDecorator;
import org.vosao.entity.FolderEntity;
import org.vosao.utils.FolderUtil;

import com.bradmcevoy.http.Resource;

public class FolderFileFactory extends AbstractFileFactory {

	public FolderFileFactory(Business business) {
		super(business);
	}

	@Override
	public String getName() {
		return "_folder.xml";
	}
	
	@Override
	public Resource getFile(String path) {
		String folderPath = FolderUtil.getFilePath(path);
		TreeItemDecorator<FolderEntity> folder = getBusiness()
				.getFolderBusiness().findFolderByPath(
						getBusiness().getFolderBusiness().getTree(), folderPath);
		if (folder != null) {
			return new FolderFileResource(getBusiness(), folder.getEntity());
		}
		return null;
	}

	@Override
	public boolean isCorrectPath(String path) {
		String filename = FolderUtil.getFolderName(path);
		return getName().equals(filename);
	}

	@Override
	public boolean existsIn(String folderPath) {
		return true;
	}

}
