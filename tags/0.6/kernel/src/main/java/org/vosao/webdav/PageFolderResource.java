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
import org.vosao.entity.PageEntity;
import org.vosao.utils.FolderUtil;
import org.vosao.webdav.sysfile.FileFactory;
import org.vosao.webdav.sysfile.SystemFileFactory;

import com.bradmcevoy.http.Auth;
import com.bradmcevoy.http.CollectionResource;
import com.bradmcevoy.http.GetableResource;
import com.bradmcevoy.http.Range;
import com.bradmcevoy.http.Resource;
import com.bradmcevoy.http.exceptions.BadRequestException;
import com.bradmcevoy.http.exceptions.NotAuthorizedException;

public class PageFolderResource extends AbstractResource implements 
		CollectionResource, GetableResource {

	private String path;
	private PageEntity page;
	private SystemFileFactory systemFileFactory;
	
	public PageFolderResource(Business aBusiness, 
			SystemFileFactory aSystemFileFactory, String aPath, 
			PageEntity aPage) {
		super(aBusiness, aPage.getPageFriendlyURL(), new Date());
		systemFileFactory = aSystemFileFactory;
		path = aPath;
		page = aPage;
	}

	@Override
	public Resource child(String childName) {
		if (systemFileFactory.isSystemFile(path + "/" + childName)) {
			return systemFileFactory.getSystemFile(path + "/" + childName);
		}
		List<PageEntity> children = getDao().getPageDao().getByParent(
				page.getFriendlyURL());
		for (PageEntity child : children) {
			return new PageFolderResource(getBusiness(), systemFileFactory,
					path + "/" + child.getPageFriendlyURL(), child);
		}
		return null;
	}

	@Override
	public List<? extends Resource> getChildren() {
		List<Resource> result = new ArrayList<Resource>();
		systemFileFactory.addSystemFiles(result, path);
		List<PageEntity> children = getDao().getPageDao().getByParent(
				page.getFriendlyURL());
		for (PageEntity child : children) {
			result.add(new PageFolderResource(getBusiness(), systemFileFactory,
					path + "/" + child.getPageFriendlyURL(), child));
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
		String relPath = path.replace("/_ah/webdav", "");
		String basePath = relPath.length() <= 1 ? "/_ah/webdav/" : path + "/";
		String parentPath = FolderUtil.getParentPath(path);
		s.append("<tr><td colspan=\"3\"><img src=\"/static/images/01_left.png\"/><a href=\"")
			.append(parentPath).append("\">Parent</a></td></tr>");
		int i = 1;
		List<PageEntity> children = getDao().getPageDao().getByParent(
				page.getFriendlyURL());
		for (PageEntity child : children) {
			s.append("<tr ").append(i++ % 2 == 1 ? "class=\"even\"" : "")
				.append("><td colspan=\"3\"><img src=\"/static/images/folder.png\"><a href=\"")
				.append(basePath + child.getPageFriendlyURL()).append("\">")
				.append(child.getPageFriendlyURL())
				.append("</a></td></tr>");
		}
		String factoryPath = relPath.length() < 1 ? "/" : relPath;
		for (FileFactory factory : systemFileFactory.getFactories()) {
			if (factory.existsIn(factoryPath)) {
				s.append("<tr ").append(i++ % 2 == 1 ? "class=\"even\"" : "")
					.append("><td><img src=\"/static/images/page.png\" /><a href=\"")
					.append(basePath + factory.getName())
					.append("\">").append(factory.getName())
					.append("</a></td><td>")
					.append(0).append("</td<td>").append(new Date())
					.append("</td></tr>");
			}
		}		
		s.append("</table>");
		out.write(s.toString().getBytes("UTF-8"));
	}

}
