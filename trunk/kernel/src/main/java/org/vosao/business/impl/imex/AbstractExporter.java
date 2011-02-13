/**
 * Vosao CMS. Simple CMS for Google App Engine.
 * 
 * Copyright (C) 2009-2010 Vosao development team.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * email: vosao.dev@gmail.com
 */

package org.vosao.business.impl.imex;

import java.io.IOException;
import java.util.zip.ZipEntry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.business.Business;
import org.vosao.business.imex.ExporterFactory;
import org.vosao.business.imex.task.DaoTaskAdapter;
import org.vosao.business.imex.task.TaskTimeoutException;
import org.vosao.business.imex.task.ZipOutStreamTaskAdapter;
import org.vosao.dao.Dao;

public abstract class AbstractExporter {

	protected static final Log logger = LogFactory.getLog(AbstractExporter.class);

	public static void saveFile(final ZipOutStreamTaskAdapter out, String name, 
			String content) throws IOException, TaskTimeoutException {
		out.putNextEntry(new ZipEntry(name));
		out.write(content.getBytes("UTF-8"));
		out.closeEntry();
		out.nextFile();
	}

	private ExporterFactory exporterFactory;
	
	public AbstractExporter(ExporterFactory factory) {
		exporterFactory = factory;
	}

	public Dao getDao() {
		return getBusiness().getDao();
	}

	public Business getBusiness() {
		return getExporterFactory().getBusiness(); 
	}
	
	public DaoTaskAdapter getDaoTaskAdapter() {
		return getExporterFactory().getDaoTaskAdapter();
	}
	
	public ExporterFactory getExporterFactory() {
		return exporterFactory;
	}
	
}
