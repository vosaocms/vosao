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

package org.vosao.business.imex;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.zip.ZipEntry;

import org.dom4j.DocumentException;
import org.vosao.business.decorators.TreeItemDecorator;
import org.vosao.business.imex.task.TaskTimeoutException;
import org.vosao.business.imex.task.ZipOutStreamTaskAdapter;
import org.vosao.dao.DaoTaskException;
import org.vosao.entity.FolderEntity;

public interface ResourceExporter {

	String getFolderSystemFile(FolderEntity folder);

	/**
	 * Add folder and _folder.xml file to zip archive.
	 * @param out - zip output stream
	 * @param folder - folder 
	 * @param zipPath - folder path under which resources will be placed to zip. 
	 * 	             Should not start with / symbol and should end with / symbol.
	 * @throws IOException
	 */
	void addFolder(final ZipOutStreamTaskAdapter out, 
			final FolderEntity folder, final String zipPath) 
			throws IOException, TaskTimeoutException;
	
	/**
	 * Add files from resource folder to zip archive.
	 * @param out - zip output stream
	 * @param folder - folder tree item
	 * @param zipPath - folder path under which resources will be placed to zip. 
	 * 	             Should not start with / symbol and should end with / symbol.
	 * @throws IOException
	 */
	void addResourcesFromFolder(final ZipOutStreamTaskAdapter out, 
			final TreeItemDecorator<FolderEntity> folder, final String zipPath) 
			throws IOException, TaskTimeoutException;

	/**
	 * Add files from resource folder to zip archive.
	 * @param out - zip output stream
	 * @param folder - folder tree item
	 * @param zipPath - folder path under which resources will be placed to zip. 
	 * 	             Should not start with / symbol and should end with / symbol.
	 * @throws IOException
	 */
	void addResourcesFromPage(final ZipOutStreamTaskAdapter out, 
			final String pageURL, final String zipPath) 
			throws IOException, TaskTimeoutException;
	
	String importResourceFile(final ZipEntry entry, byte[] data)
			throws UnsupportedEncodingException, DaoTaskException;
	
	/**
	 * Read and import data from _folder.xml file.
	 * @param folderPath - folder path.
	 * @param xml - _folder.xml file.
	 * @throws DocumentException 
	 * @throws DaoTaskException 
	 */
	void readFolderFile(String folderPath, String xml) 
			throws DocumentException, DaoTaskException;
	
}
