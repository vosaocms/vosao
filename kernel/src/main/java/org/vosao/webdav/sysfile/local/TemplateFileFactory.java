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

import org.vosao.business.Business;
import org.vosao.entity.TemplateEntity;
import org.vosao.utils.FolderUtil;
import org.vosao.webdav.sysfile.AbstractFileFactory;

import com.bradmcevoy.http.Resource;

public class TemplateFileFactory extends AbstractFileFactory {

	public TemplateFileFactory(Business business) {
		super(business);
	}

	@Override
	public String getName() {
		return "_template.xml";
	}
	
	@Override
	public Resource getFile(String path) {
		String folderPath = FolderUtil.getFilePath(path);
		TemplateEntity template = getTemplateByPath(folderPath);
		if (template != null) {
			return new TemplateFileResource(getBusiness(), template);
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
		if (folderPath.startsWith("/theme/")) {
			return getTemplateByPath(folderPath) != null;
		}
		return false;
	}

	private TemplateEntity getTemplateByPath(String folderPath) {
		try {
			String[] chain = FolderUtil.getPathChain(folderPath);
			if (chain.length == 2) {
				return getDao().getTemplateDao().getByUrl(chain[1]);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Resource createFile(byte[] content) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isCreatable(String folderPath) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
