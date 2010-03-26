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
import java.util.List;
import java.util.zip.ZipEntry;

import org.dom4j.DocumentException;
import org.vosao.business.imex.task.TaskTimeoutException;
import org.vosao.business.imex.task.ZipOutStreamTaskAdapter;
import org.vosao.dao.DaoTaskException;
import org.vosao.entity.TemplateEntity;

public interface ThemeExporter {

	static final String THEME_FOLDER = "theme/";
	
	String createThemeExportXML(final TemplateEntity theme);

	void exportThemes(final ZipOutStreamTaskAdapter out, 
			final List<TemplateEntity> themes) throws IOException,
			TaskTimeoutException;
	
	void exportTheme(final ZipOutStreamTaskAdapter out, 
			final TemplateEntity theme) throws IOException, 
			TaskTimeoutException;
	
	boolean isThemeDescription(final ZipEntry entry)
			throws UnsupportedEncodingException;

	void createThemeByDescription(final ZipEntry entry, String xml)
			throws UnsupportedEncodingException, DocumentException, 
			DaoTaskException;
	
	boolean isThemeContent(final ZipEntry entry)
			throws UnsupportedEncodingException;
	
	void createThemeByContent(final ZipEntry entry, final String content) 
			throws UnsupportedEncodingException, DocumentException, 
			DaoTaskException;

	/**
	 * Read and import data from _template.xml file.
	 * @param path - path to _temlate.xml file.
	 * @param xml - _template.xml content.
	 * @return true if successfully imported.
	 * @throws DocumentException
	 * @throws DaoTaskException 
	 */
	boolean readTemplateFile(String path, String xml) 
			throws DocumentException, DaoTaskException;
}
