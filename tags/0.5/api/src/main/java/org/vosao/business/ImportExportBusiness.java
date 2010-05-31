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
import org.vosao.business.imex.ExporterFactory;
import org.vosao.business.imex.task.TaskTimeoutException;
import org.vosao.business.imex.task.ZipOutStreamTaskAdapter;
import org.vosao.dao.DaoTaskException;
import org.vosao.entity.FolderEntity;
import org.vosao.entity.TemplateEntity;

public interface ImportExportBusiness {

	ExporterFactory getExporterFactory();

	/**
	 * Create export file with selected templates. Inside task execution.
	 * @param list - selected themes.
	 * @return zip file as byte array
	 * @throws IOException
	 */
	void createTemplateExportFile(final ZipOutStreamTaskAdapter zip, 
			final List<TemplateEntity> list) throws IOException, 
			TaskTimeoutException;

	/**
	 * Create export file for whole site. Excluding unconnected resources.
	 * Inside task execution.
	 * @return zip file as byte array
	 * @throws IOException
	 */
	void createSiteExportFile(final ZipOutStreamTaskAdapter zip) throws IOException,
			TaskTimeoutException;

	/**
	 * Create export file for whole site. Including unconnected resources.
	 * Inside task execution.
	 * @return zip file as byte array
	 * @throws IOException
	 */
	void createFullExportFile(final ZipOutStreamTaskAdapter zip) throws IOException,
			TaskTimeoutException;

	/**
	 * Import site data from zip file.
	 * @return list of imported resources.
	 * @throws DaoTaskException 
	 */
	void importZip(ZipInputStream in) throws IOException,
		DocumentException, DaoTaskException;

	/**
	 * Import site data from vz file. Format 2.0
	 * @return list of imported resources.
	 * @throws DaoTaskException 
	 */
	void importZip2(ZipInputStream in) throws IOException,
		DocumentException, DaoTaskException;

	/**
	 * Create export file for folder with files and subfolders.
	 * Inside task execution.
	 * @param folder - folder to export.
	 * @return zip file as byte array
	 * @throws IOException
	 */
	void createExportFile(final ZipOutStreamTaskAdapter zip, 
			final FolderEntity folder) throws IOException, TaskTimeoutException;

	/**
	 * Create resources export file for all folders except /page, /theme, /tmp.
	 * Inside task execution.
	 * @param folder - folder to export.
	 * @return zip file as byte array
	 * @throws IOException
	 */
	void createResourcesExportFile(final ZipOutStreamTaskAdapter zip) 
			throws IOException, TaskTimeoutException;
	
}
