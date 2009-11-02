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

package org.vosao.business.impl.imex;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.business.Business;
import org.vosao.business.decorators.TreeItemDecorator;
import org.vosao.dao.Dao;
import org.vosao.entity.FileEntity;
import org.vosao.entity.FolderEntity;
import org.vosao.servlet.FolderUtil;
import org.vosao.servlet.MimeType;

public class ResourceExporter extends AbstractExporter {

	private static final Log logger = LogFactory.getLog(ResourceExporter.class);

	public ResourceExporter(Dao aDao, Business aBusiness) {
		super(aDao, aBusiness);
	}
	
	/**
	 * Add files from resource folder to zip archive.
	 * @param out - zip output stream
	 * @param folder - folder tree item
	 * @param zipPath - folder path under which resources will be placed to zip. 
	 * 	             Should not start with / symbol and should end with / symbol.
	 * @throws IOException
	 */
	public void addResourcesFromFolder(final ZipOutputStream out, 
			final TreeItemDecorator<FolderEntity> folder, final String zipPath) 
		throws IOException {
		
		out.putNextEntry(new ZipEntry(zipPath));
		out.closeEntry();
		for (TreeItemDecorator<FolderEntity> child : folder.getChildren()) {
			addResourcesFromFolder(out, child, 
					zipPath + child.getEntity().getName() + "/");
		}
		List<FileEntity> files = getDao().getFileDao().getByFolder(
				folder.getEntity().getId());
		for (FileEntity file : files) {
			String filePath = zipPath + file.getFilename();
			out.putNextEntry(new ZipEntry(filePath));
			out.write(getDao().getFileDao().getFileContent(file));
			out.closeEntry();
		}
	}


	public String importResourceFile(final ZipEntry entry, byte[] data)
			throws UnsupportedEncodingException {

		String[] chain = FolderUtil.getPathChain(entry);
		String folderPath = FolderUtil.getFilePath(entry);
		String fileName = chain[chain.length - 1];
		logger.debug("importResourceFile: " + folderPath + " " + fileName + " "
				+ data.length);
		getBusiness().getFolderBusiness().createFolder(folderPath);
		TreeItemDecorator<FolderEntity> root = getBusiness()
				.getFolderBusiness().getTree();
		FolderEntity folderEntity = getBusiness()
				.getFolderBusiness().findFolderByPath(root,
				folderPath).getEntity();
		String contentType = MimeType.getContentTypeByExt(FolderUtil
				.getFileExt(fileName));
		FileEntity fileEntity = getDao().getFileDao().getByName(
				folderEntity.getId(), fileName);
		if (fileEntity != null) {
			fileEntity.setLastModifiedTime(new Date());
			fileEntity.setSize(data.length);
		} else {
			fileEntity = new FileEntity(fileName, fileName, folderEntity
					.getId(), contentType, new Date(), data.length);
		}
		getDao().getFileDao().save(fileEntity, data);
		return "/" + entry.getName();
	}
	
}
