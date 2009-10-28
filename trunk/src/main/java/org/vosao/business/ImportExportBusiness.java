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

package org.vosao.business;

import java.io.IOException;
import java.util.List;
import java.util.zip.ZipInputStream;

import org.dom4j.DocumentException;
import org.vosao.dao.Dao;
import org.vosao.entity.FolderEntity;
import org.vosao.entity.TemplateEntity;

public interface ImportExportBusiness {

	/**
	 * Create export file with selected themes.
	 * @param list - selected themes.
	 * @return zip file as byte array
	 * @throws IOException
	 */
	byte[] createExportFile(final List<TemplateEntity> list) throws IOException;

	/**
	 * Create export file for whole site.
	 * @return zip file as byte array
	 * @throws IOException
	 */
	byte[] createExportFile() throws IOException;

	/**
	 * Import site data from zip file.
	 * @return list of imported resources.
	 */
	List<String> importZip(ZipInputStream in) throws IOException,
		DocumentException;

	/**
	 * Create export file for folder with files and subfolders.
	 * @param folder - folder to export.
	 * @return zip file as byte array
	 * @throws IOException
	 */
	byte[] createExportFile(final FolderEntity folder) throws IOException;
	
}
