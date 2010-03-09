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

package org.vosao.webdav.sysfile.local;

import java.util.List;

import org.vosao.business.Business;
import org.vosao.entity.PageEntity;
import org.vosao.utils.FolderUtil;
import org.vosao.webdav.sysfile.AbstractFileFactory;

import com.bradmcevoy.http.Resource;

public class CommentsFileFactory extends AbstractFileFactory {

	public CommentsFileFactory(Business business) {
		super(business);
	}

	@Override
	public String getName() {
		return "_comments.xml";
	}
	
	@Override
	public Resource getFile(String path) {
		String pageURL = FolderUtil.getPageURLFromFolderPath(
				FolderUtil.getFilePath(path));
		List<PageEntity> pages = getDao().getPageDao().selectByUrl(pageURL);
		if (pages.size() > 0) {
			return new CommentsFileResource(getBusiness(), pageURL);
		}
		return null;
	}

	@Override
	public boolean isCorrectPath(String path) {
		String filename = FolderUtil.getFolderName(path);
		String filePath = FolderUtil.getFilePath(path);
		return existsIn(filePath) && getName().equals(filename);
	}

	@Override
	public boolean existsIn(String folderPath) {
		String pageURL = FolderUtil.getPageURLFromFolderPath(folderPath);
		if (pageURL != null) {
			List<PageEntity> pages = getDao().getPageDao().selectByUrl(pageURL);
			return pages.size() > 0;
		}
		return false;
	}
	
}
