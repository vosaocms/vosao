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

import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.vosao.dao.DaoTaskException;
import org.vosao.entity.PageEntity;

public interface PageExporter {

	String createPageContentXML(PageEntity page);
	
	String createPageCommentsXML(String pageURL);

	String createPagePermissionsXML(String pageURL);

	void readPages(Element pages) throws DaoTaskException;
	
	/**
	 * Read and import data from _content.xml file.
	 * @param folderPath - _content.xml file path.
	 * @param xml - _content.xml file content.
	 * @return
	 * @throws DocumentException 
	 * @throws DaoTaskException 
	 */
	boolean readContentFile(String folderPath, String xml) 
			throws DocumentException, DaoTaskException;
	
	/**
	 * Read and import data from _comments.xml file.
	 * @param folderPath - _comments.xml file path.
	 * @param xml - _comments.xml file content.
	 * @return
	 * @throws DocumentException 
	 * @throws DaoTaskException 
	 */
	boolean readCommentsFile(String folderPath, String xml) 
			throws DocumentException, DaoTaskException;
	
	/**
	 * Read and import data from _permissions.xml file.
	 * @param folderPath - _permissions.xml file path.
	 * @param xml - _permissions.xml file content.
	 * @return
	 * @throws DocumentException 
	 */
	boolean readPermissionsFile(String folderPath, String xml) 
			throws DocumentException;
	
}
