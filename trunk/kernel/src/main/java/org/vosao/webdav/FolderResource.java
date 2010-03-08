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

package org.vosao.webdav;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.vosao.business.Business;
import org.vosao.entity.FileEntity;
import org.vosao.entity.FolderEntity;
import org.vosao.servlet.FolderUtil;

import com.bradmcevoy.http.Auth;
import com.bradmcevoy.http.CollectionResource;
import com.bradmcevoy.http.GetableResource;
import com.bradmcevoy.http.Range;
import com.bradmcevoy.http.Resource;
import com.bradmcevoy.http.exceptions.BadRequestException;
import com.bradmcevoy.http.exceptions.NotAuthorizedException;

public class FolderResource extends AbstractResource implements 
		CollectionResource, GetableResource {

	private FolderEntity folder;
	private String path;
	
	public FolderResource(Business aBusiness, FolderEntity aFolder, 
			String aPath) {
		super(aBusiness, aFolder.getName(), new Date());
		folder = aFolder;
		path = aPath;
	}

	@Override
	public Resource child(String childName) {
		List<FolderEntity> children = getDao().getFolderDao().getByParent(
				folder.getId());
		for (FolderEntity child : children) {
			if (child.getName().equals(childName)) {
				return new FolderResource(getBusiness(), child, 
						path + "/" + childName);
			}
		}
		List<FileEntity> files = getDao().getFileDao().getByFolder(folder.getId());
		for (FileEntity file : files) {
			if (file.equals(childName)) {
				return new FileResource(getBusiness(), file);
			}
		}
		return null;
	}

	@Override
	public List<? extends Resource> getChildren() {
		List<Resource> result = new ArrayList<Resource>();
		List<FolderEntity> children = getDao().getFolderDao().getByParent(
				folder.getId());
		for (FolderEntity child : children) {
			result.add(new FolderResource(getBusiness(), child, 
					path + "/" + child.getName()));
		}
		List<FileEntity> files = getDao().getFileDao().getByFolder(folder.getId());
		for (FileEntity file : files) {
			result.add(new FileResource(getBusiness(), file));
		}
		return result;
	}

	@Override
	public Long getContentLength() {
		return null;
	}

	@Override
	public String getContentType(String accepts) {
		return "text/html";
	}

	@Override
	public Long getMaxAgeSeconds(Auth arg0) {
		return null;
	}

	@Override
	public void sendContent(OutputStream out, Range range,
			Map<String, String> params, String contentType) throws IOException,
			NotAuthorizedException, BadRequestException {
		StringBuffer s = new StringBuffer();
		s.append("<html><body><head>")
			.append("<link rel=\"stylesheet\" href=\"/static/css/style.css\" type=\"text/css\" />")
			.append("</head><h2>Vosao WebDAV site repository</h2>");
		s.append("<table width=\"50%\" class=\"form-table\"><th width=\"40%\">Name</th><th>Size in bytes</th><th>Modified date</th></tr>");
		List<FolderEntity> children = getDao().getFolderDao().getByParent(
				folder.getId());
		String relPath = path.replace("/_ah/webdav", "");
		String basePath = relPath.length() <= 1 ? "/_ah/webdav/" : path + "/";
		if (!folder.isRoot()) {
			String parentPath = FolderUtil.getParentPath(path);
			s.append("<tr><td colspan=\"3\"><img src=\"/static/images/01_left.png\"/><a href=\"")
				.append(parentPath).append("\">Parent</a></td></tr>");
		}
		int i = 1;
		for (FolderEntity child : children) {
			s.append("<tr ").append(i++ % 2 == 1 ? "class=\"even\"" : "")
				.append("><td colspan=\"3\"><img src=\"/static/images/folder.png\"><a href=\"")
				.append(basePath + child.getName()).append("\">").append(child.getName())
				.append("</a></td></tr>");
		}
		List<FileEntity> files = getDao().getFileDao().getByFolder(folder.getId());
		for (FileEntity file : files) {
			s.append("<tr ").append(i++ % 2 == 1 ? "class=\"even\"" : "")
				.append("><td><img src=\"/static/images/page.png\" /><a href=\"")
				.append(basePath + file.getFilename())
				.append("\">").append(file.getFilename())
				.append("</a></td><td>")
				.append(file.getSize()).append("</td<td>").append(file.getLastModifiedTime())
				.append("</td></tr>");
		}
		s.append("</table>");
		out.write(s.toString().getBytes("UTF-8"));
	}

}
