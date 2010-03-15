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
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.vosao.business.Business;
import org.vosao.entity.FileEntity;
import org.vosao.entity.FolderEntity;
import org.vosao.entity.PageEntity;
import org.vosao.utils.FolderUtil;
import org.vosao.utils.StreamUtil;
import org.vosao.webdav.sysfile.FileFactory;
import org.vosao.webdav.sysfile.SystemFileFactory;

import com.bradmcevoy.http.Auth;
import com.bradmcevoy.http.CollectionResource;
import com.bradmcevoy.http.GetableResource;
import com.bradmcevoy.http.PutableResource;
import com.bradmcevoy.http.Range;
import com.bradmcevoy.http.Resource;
import com.bradmcevoy.http.exceptions.BadRequestException;
import com.bradmcevoy.http.exceptions.ConflictException;
import com.bradmcevoy.http.exceptions.NotAuthorizedException;

public class FolderResource extends AbstractResource implements 
		CollectionResource, GetableResource, PutableResource {

	private FolderEntity folder;
	private String path;
	private String relPath;
	private String basePath;
	private String factoryPath;
	private SystemFileFactory systemFileFactory;
	
	public FolderResource(Business aBusiness, 
			SystemFileFactory aSystemFileFactory,
			FolderEntity aFolder, String aPath) {
		super(aBusiness, aFolder.getName(), new Date());
		systemFileFactory = aSystemFileFactory;
		folder = aFolder;
		path = FolderUtil.removeTrailingSlash(aPath);
		relPath = path.replace("/_ah/webdav", "");
		basePath = relPath.length() <= 1 ? "/_ah/webdav/" : path + "/";
		factoryPath = relPath.length() < 1 ? "/" : relPath;

	}

	@Override
	public Resource child(String childName) {
		String childPath = path + "/" + childName;
		if (systemFileFactory.isSystemFile(childPath)) {
			return systemFileFactory.getSystemFile(childPath);
		}
		List<FolderEntity> children = getDao().getFolderDao().getByParent(
				folder.getId());
		for (FolderEntity child : children) {
			if (child.getName().equals(childName)) {
				return new FolderResource(getBusiness(), systemFileFactory, 
						child, childPath);
			}
		}
		String pageURL = FolderUtil.getPageURLFromFolderPath(childPath);
		if (pageURL != null) {
			List<PageEntity> pages = getDao().getPageDao().selectByUrl(pageURL);
			return new PageFolderResource(getBusiness(), systemFileFactory, 
					childPath, pages.get(0));
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
		systemFileFactory.addSystemFiles(result, factoryPath);
		List<FolderEntity> children = getDao().getFolderDao().getByParent(
				folder.getId());
		for (FolderEntity child : children) {
			result.add(new FolderResource(getBusiness(), systemFileFactory, child, 
					path + "/" + child.getName()));
		}
		String pageURL = FolderUtil.getPageURLFromFolderPath(path);
		if (pageURL != null) {
			List<PageEntity> pages = getDao().getPageDao().getByParent(pageURL);
			for (PageEntity page : pages) {
				if (!contains(children, page.getPageFriendlyURL())) {
					result.add(new PageFolderResource(getBusiness(), 
							systemFileFactory, 
							path + "/" + page.getPageFriendlyURL(), 
							page));
				}
			}
		}
		List<FileEntity> files = getDao().getFileDao().getByFolder(folder.getId());
		for (FileEntity file : files) {
			result.add(new FileResource(getBusiness(), file));
		}
		return result;
	}

	private boolean contains(List<FolderEntity> folders, String name) {
		for (FolderEntity folder : folders) {
			if (folder.getName().equals(name)) {
				return true;
			}
		}
		return false;
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
		return 10L;
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
		if (!folder.isRoot()) {
			String parentPath = FolderUtil.getParentPath(path);
			s.append("<tr><td colspan=\"3\"><img src=\"/static/images/01_left.png\"/><a href=\"")
				.append(parentPath).append("\">Parent</a></td></tr>");
		}
		int i = 1;
		// children folders
		for (FolderEntity child : children) {
			s.append("<tr ").append(i++ % 2 == 1 ? "class=\"even\"" : "")
				.append("><td colspan=\"3\"><img src=\"/static/images/folder.png\"><a href=\"")
				.append(basePath + child.getName()).append("\">").append(child.getName())
				.append("</a></td></tr>");
		}
		// page folders
		String pageURL = FolderUtil.getPageURLFromFolderPath(relPath);
		if (pageURL != null) {
			List<PageEntity> pages = getDao().getPageDao().getByParent(pageURL);
			for (PageEntity child : pages) {
				s.append("<tr ").append(i++ % 2 == 1 ? "class=\"even\"" : "")
					.append("><td colspan=\"3\"><img src=\"/static/images/folder.png\"><a href=\"")
					.append(basePath + child.getPageFriendlyURL()).append("\">")
					.append(child.getPageFriendlyURL())
					.append("</a></td></tr>");
			}
		}
		// system files
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
		// folders files
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

	@Override
	public Resource createNew(String newName, InputStream inputStream, 
			Long length, String contentType) throws IOException, 
			ConflictException {
		byte[] data = StreamUtil.readFileStream(inputStream);
		for (FileFactory factory : systemFileFactory.getFactories()) {
			if (factory.isCreatable(factoryPath)) {
				return factory.createFile(data);
			}
		}
		FileEntity file = new FileEntity();
		file.setFilename(newName);
		file.setFolderId(folder.getId());
		file.setTitle(newName);
		file.setLastModifiedTime(new Date());
		file.setSize(length.intValue());
		file.setMimeType(contentType);
		getDao().getFileDao().save(file, data);
		return new FileResource(getBusiness(), file);
	}

}
