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

import static com.google.appengine.api.labs.taskqueue.TaskOptions.Builder.url;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.vosao.business.ImportExportBusiness;
import org.vosao.business.imex.task.TaskTimeoutException;
import org.vosao.business.imex.task.ZipOutStreamTaskAdapter;
import org.vosao.business.impl.imex.task.ZipOutStreamTaskAdapterImpl;
import org.vosao.common.VosaoContext;
import org.vosao.entity.FolderEntity;
import org.vosao.entity.TemplateEntity;
import org.vosao.entity.helper.UserHelper;
import org.vosao.utils.StrUtil;

import com.bradmcevoy.io.StreamUtils;
import com.google.appengine.api.labs.taskqueue.Queue;

/**
 * In 25sec task exports data to file located in /tmp folder with name stored in
 * request parameters. Parameters: 
 * filename - file for export located in /tmp folder. 
 * exportType - type of export. 
 * currentFile - file to export.
 * 
 * @author Alexander Oleynik
 */
public class ExportTaskServlet extends BaseSpringServlet {

	public static final String EXPORT_TASK_URL = "/_ah/queue/export";
	public final static String TYPE_PARAM_THEME = "theme";
	public final static String TYPE_PARAM_FOLDER = "folder";
	public final static String TYPE_PARAM_FULL = "full";
	public final static String TYPE_PARAM_SITE = "site";
	public final static String TYPE_PARAM_RESOURCES = "resources";

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doExport(request, response);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doExport(request, response);
	}

	public void doExport(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		VosaoContext.getInstance().setUser(UserHelper.ADMIN);
		ZipOutStreamTaskAdapter zipOutStreamTaskAdapter = 
			new ZipOutStreamTaskAdapterImpl(getBusiness());
		String filename = request.getParameter("filename");
		String currentFile = request.getParameter("currentFile");
		if (currentFile == null) {
			removeExportFile(filename);
			getBusiness().getSystemService().getCache().remove(filename);
		}
		String fileCounter = request.getParameter("fileCounter");
		if (fileCounter != null) {
			zipOutStreamTaskAdapter.setFileCounter(Integer.valueOf(fileCounter));
		}
		else {
			fileCounter = "";
		}
		String exportType = request.getParameter("exportType");
		try {
			openStream(zipOutStreamTaskAdapter, filename);
			zipOutStreamTaskAdapter.setStartFile(currentFile);
			logger.info("Export " + exportType + " " + currentFile + " " 
					+ fileCounter);
	        if (exportType.equals(TYPE_PARAM_THEME)) {
	        	List<Long> ids = StrUtil.toLong(StrUtil.fromCSV(
	        			request.getParameter("ids")));
	        	List<TemplateEntity> templates = getDao().getTemplateDao()
	        			.getById(ids);
	        	getImportExportBusiness().createTemplateExportFile(
	        			zipOutStreamTaskAdapter, templates);
	        }
	        if (exportType.equals(TYPE_PARAM_FOLDER)) {
	        	FolderEntity folder = getDao().getFolderDao().getById(
	        			Long.valueOf(request.getParameter("folderId")));
	        	getImportExportBusiness().createExportFile(
	        			zipOutStreamTaskAdapter, folder);
	        }
	        if (exportType.equals(TYPE_PARAM_SITE)) {
	    		getImportExportBusiness().createSiteExportFile(
	    				zipOutStreamTaskAdapter);
	        }
	        if (exportType.equals(TYPE_PARAM_FULL)) {
	    		getImportExportBusiness().createFullExportFile(
	    				zipOutStreamTaskAdapter);
	        }
	        if (exportType.equals(TYPE_PARAM_RESOURCES)) {
	        	getImportExportBusiness().createResourcesExportFile(
	        			zipOutStreamTaskAdapter);
	        }
			saveZip(zipOutStreamTaskAdapter, filename, true);
    		getBusiness().getFileBusiness().saveFile("/tmp/" + filename + ".txt", 
    				"OK".getBytes());
			logger.info("Export finished. " + zipOutStreamTaskAdapter
					.getFileCounter());
		} catch (TaskTimeoutException e) {
			saveZip(zipOutStreamTaskAdapter, filename, false);
			Queue queue = getSystemService().getQueue("export");
			queue.add(url(EXPORT_TASK_URL)
					.param("filename", filename)
					.param("exportType", exportType)
					.param("currentFile", 
							zipOutStreamTaskAdapter.getCurrentFile())
					.param("fileCounter", String.valueOf(
							zipOutStreamTaskAdapter.getFileCounter())));
			logger.info("Added new export task "
					+ zipOutStreamTaskAdapter.getCurrentFile());
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}

	private void saveZip(ZipOutStreamTaskAdapter zipOutStreamTaskAdapter,
			String filename, boolean disk) throws IOException {
		zipOutStreamTaskAdapter.getOutStream().close();
		if (disk) {
			getBusiness().getFileBusiness().saveFile("/tmp/" + filename, 
				zipOutStreamTaskAdapter.getOutData().toByteArray());
		}
		else {
			getBusiness().getSystemService().getCache().putBlob(filename, 
				zipOutStreamTaskAdapter.getOutData().toByteArray());
		}
	}

	private void removeExportFile(String currentFile) {
		getBusiness().getFileBusiness().remove("/tmp/" + currentFile);
		getBusiness().getFileBusiness().remove("/tmp/" + currentFile + ".txt");
	}

	private ImportExportBusiness getImportExportBusiness() {
		return (ImportExportBusiness) getSpringBean("importExportBusiness");
	}

	private void openStream(ZipOutStreamTaskAdapter zip, String filename) {
		byte[] savedZip = getBusiness().getSystemService().getCache()
				.getBlob(filename);
		ByteArrayOutputStream outData = new ByteArrayOutputStream();
		ZipOutputStream out = new ZipOutputStream(outData);
		zip.setOutStream(out);
		zip.setOutData(outData);
		if (savedZip != null) {
			ByteArrayInputStream inputData = new ByteArrayInputStream(savedZip);
			try {
				ZipInputStream in = new ZipInputStream(inputData);
				ZipEntry entry;
				while ((entry = in.getNextEntry()) != null) {
					out.putNextEntry(entry);
					if (!entry.isDirectory()) {
		                StreamUtils.readTo(in, out, false, false);
		            }
		            out.closeEntry();
				}
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static String getExportFilename(String exportType) {
		if (exportType.equals(ExportTaskServlet.TYPE_PARAM_SITE)) {
			return "exportSite.vz";
		}
		if (exportType.equals(ExportTaskServlet.TYPE_PARAM_THEME)) {
			return "exportTheme.vz";
		}
		if (exportType.equals(ExportTaskServlet.TYPE_PARAM_FOLDER)) {
			return "exportFolder.vz";
		}
		if (exportType.equals(ExportTaskServlet.TYPE_PARAM_FULL)) {
			return "exportFull.vz";
		}
		if (exportType.equals(ExportTaskServlet.TYPE_PARAM_RESOURCES)) {
			return "exportResources.vz";
		}
		return null;
	}
	
}
