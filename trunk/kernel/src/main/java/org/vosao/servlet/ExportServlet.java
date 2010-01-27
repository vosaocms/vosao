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

package org.vosao.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.business.ImportExportBusiness;
import org.vosao.entity.FolderEntity;
import org.vosao.entity.TemplateEntity;
import org.vosao.utils.MimeType;

/**
 * Servlet for export.
 * 
 * @author Aleksandr Oleynik
 */
public class ExportServlet extends BaseSpringServlet {
	
	private static final Log logger = LogFactory.getLog(ExportServlet.class);

	public final static String TYPE_PARAM = "type";
	public final static String TYPE_PARAM_THEME = "theme";
	public final static String TYPE_PARAM_FOLDER = "folder";
	public final static String TYPE_PARAM_SITE = "site";
	public final static String IDS_PARAM = "ids";
	public final static String ID_PARAM = "id";
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

        String type = request.getParameter(TYPE_PARAM);
        if (type.equals(TYPE_PARAM_THEME)) {
        	exportThemes(request, response);
        }
        if (type.equals(TYPE_PARAM_FOLDER)) {
        	exportFolder(request, response);
        }
        if (type.equals(TYPE_PARAM_SITE)) {
        	exportSite(request, response);
        }
	}

	private void exportThemes(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		
		String[] ids = (request.getParameterValues(IDS_PARAM)[0]).split("::");
		if (ids.length == 0) {
			logger.error("Nothing selected for theme export");
			return;
		}
		List<TemplateEntity> selectedTemplates = new ArrayList<TemplateEntity>();
		for (String id : ids) {
			TemplateEntity t = getDao().getTemplateDao().getById(id);
			if (t != null) {
				selectedTemplates.add(t);
			}
		}
		response.setContentType(MimeType.getContentTypeByExt("zip"));
		String downloadFile = "exportTheme.zip";
		response.addHeader("Content-Disposition", "attachment; filename=\"" 
				+ downloadFile + "\"");
		ServletOutputStream out = response.getOutputStream();
		byte[] file = getImportExportBusiness().createExportFile(
				selectedTemplates);
		response.setContentLength(file.length);
		out.write(file);
		out.flush();
		out.close();
	}

	private void exportFolder(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		
		String id = request.getParameterValues(ID_PARAM)[0];
		if (id == null) {
			logger.error("Folder id not specified");
			return;
		}
		FolderEntity folder = getDao().getFolderDao().getById(id);
		if (folder == null) {
			logger.error("Folder not found with id " + id);
			return;
		}
		logger.debug("Exporting folder " + folder.getName());
		response.setContentType(MimeType.getContentTypeByExt("zip"));
		String downloadFile = "exportFolder.zip";
		response.addHeader("Content-Disposition", "attachment; filename=\"" 
				+ downloadFile + "\"");
		ServletOutputStream out = response.getOutputStream();
		byte[] file = getImportExportBusiness().createExportFile(folder);
		response.setContentLength(file.length);
		out.write(file);
		out.flush();
		out.close();
	}
	
	private void exportSite(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		logger.debug("Exporting site.");
		response.setContentType(MimeType.getContentTypeByExt("zip"));
		String downloadFile = "exportSite.zip";
		response.addHeader("Content-Disposition", "attachment; filename=\"" 
				+ downloadFile + "\"");
		ServletOutputStream out = response.getOutputStream();
		byte[] file = getImportExportBusiness().createExportFile();
		response.setContentLength(file.length);
		out.write(file);
		out.flush();
		out.close();
	}
	
	private ImportExportBusiness getImportExportBusiness() {
		return (ImportExportBusiness) getSpringBean("importExportBusiness");
	}
	
}