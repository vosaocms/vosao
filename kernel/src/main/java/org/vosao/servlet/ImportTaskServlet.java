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
import java.io.IOException;
import java.util.zip.ZipInputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;
import org.vosao.business.CurrentUser;
import org.vosao.business.ImportExportBusiness;
import org.vosao.business.impl.imex.task.DaoTaskAdapter;
import org.vosao.business.impl.imex.task.TaskFinishedException;
import org.vosao.entity.FileEntity;
import org.vosao.entity.FolderEntity;
import org.vosao.entity.helper.UserHelper;
import org.vosao.utils.FolderUtil;

import com.google.appengine.api.labs.taskqueue.Queue;

/**
 * In 25sec task imports data from file located in /tmp folder with name
 * stored in request parameters.
 * Parameters:
 *   filename - file for import located in /tmp folder.
 *   start - database save counter to start from inside current file
 *   currentFile - file from imported archive to strat import from. 
 * @author oleynik
 *
 */
public class ImportTaskServlet extends BaseSpringServlet {

	public static final String IMPORT_TASK_URL = "/_ah/queue/import";

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doImport(request, response);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doImport(request, response);
	}
	
	public void doImport(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String ext = FolderUtil.getFileExt(request.getParameter("filename"));
		if (ext.equals("zip")) {
			doImport1(request, response);
		}
		if (ext.equals("vz")) {
			doImport2(request, response);
		}
	}
	
	public void doImport1(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String filename = request.getParameter("filename");
		try {
			CurrentUser.setInstance(UserHelper.ADMIN);
			int start = Integer.valueOf(request.getParameter("start"));
			String currentFile = request.getParameter("currentFile");
			getDaoTaskAdapter().setStart(start);
			getDaoTaskAdapter().setCurrentFile(currentFile);
			logger.info("Import " + filename + " " + start + " " + currentFile);
			FolderEntity folder = getBusiness()
					.getFolderBusiness()
					.findFolderByPath(
							getBusiness().getFolderBusiness().getTree(), "/tmp")
					.getEntity();
			FileEntity file = getDao().getFileDao().getByName(folder.getId(),
					filename);
			if (file == null) {
				return;
			}
			byte[] data = getDao().getFileDao().getFileContent(file);
			ByteArrayInputStream inputData = new ByteArrayInputStream(data);
			try {
				ZipInputStream in = new ZipInputStream(inputData);
				getImportExportBusiness().importZip(in);
				in.close();
			} catch (IOException e) {
				throw new UploadException(e.getMessage());
			} catch (DocumentException e) {
				throw new UploadException(e.getMessage());
			}
			getDao().getFileDao().remove(file.getId());
			logger.info("Import finished. " + getDaoTaskAdapter().getEnd());
		} catch (TaskFinishedException e) {
			Queue queue = getSystemService().getQueue("import");
			queue.add(url(IMPORT_TASK_URL).param("start",
					String.valueOf(getDaoTaskAdapter().getEnd())).param(
					"filename", filename).param("currentFile",
					getDaoTaskAdapter().getCurrentFile()));
			logger.info("Added new import task " + getDaoTaskAdapter().getEnd()
					+ " " + getDaoTaskAdapter().getCurrentFile());
		} catch (UploadException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}

	private ImportExportBusiness getImportExportBusiness() {
		return (ImportExportBusiness) getSpringBean("importExportBusiness");
	}

	private DaoTaskAdapter getDaoTaskAdapter() {
		return (DaoTaskAdapter) getSpringBean("daoTaskAdapter");
	}

	public void doImport2(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String filename = request.getParameter("filename");
		try {
			CurrentUser.setInstance(UserHelper.ADMIN);
			int start = Integer.valueOf(request.getParameter("start"));
			String currentFile = request.getParameter("currentFile");
			getDaoTaskAdapter().setStart(start);
			getDaoTaskAdapter().setCurrentFile(currentFile);
			logger.info("Import " + filename + " " + start + " " + currentFile);
			FolderEntity folder = getBusiness().getFolderBusiness()
					.findFolderByPath(
							getBusiness().getFolderBusiness().getTree(), "/tmp")
					.getEntity();
			FileEntity file = getDao().getFileDao().getByName(folder.getId(),
					filename);
			if (file == null) {
				return;
			}
			byte[] data = getDao().getFileDao().getFileContent(file);
			ByteArrayInputStream inputData = new ByteArrayInputStream(data);
			try {
				ZipInputStream in = new ZipInputStream(inputData);
				getImportExportBusiness().importZip2(in);
				in.close();
			} catch (IOException e) {
				throw new UploadException(e.getMessage());
			} catch (DocumentException e) {
				throw new UploadException(e.getMessage());
			}
			getDao().getFileDao().remove(file.getId());
			logger.info("Import finished. " + getDaoTaskAdapter().getEnd());
		} catch (TaskFinishedException e) {
			Queue queue = getSystemService().getQueue("import");
			queue.add(url(IMPORT_TASK_URL).param("start",
					String.valueOf(getDaoTaskAdapter().getEnd())).param(
					"filename", filename).param("currentFile",
					getDaoTaskAdapter().getCurrentFile()));
			logger.info("Added new import task " + getDaoTaskAdapter().getEnd()
					+ " " + getDaoTaskAdapter().getCurrentFile());
		} catch (UploadException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}

}
