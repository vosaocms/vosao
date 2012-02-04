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

package org.vosao.business.impl.mq.subscriber;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.vosao.business.ImportExportBusiness;
import org.vosao.business.imex.task.TaskTimeoutException;
import org.vosao.business.imex.task.ZipOutStreamTaskAdapter;
import org.vosao.business.impl.imex.task.ZipOutStreamTaskAdapterImpl;
import org.vosao.business.impl.mq.AbstractSubscriber;
import org.vosao.business.mq.Message;
import org.vosao.business.mq.message.ExportMessage;
import org.vosao.business.mq.message.ExportMessage.Builder;
import org.vosao.common.VosaoContext;
import org.vosao.entity.FolderEntity;
import org.vosao.entity.StructureEntity;
import org.vosao.entity.TemplateEntity;
import org.vosao.entity.helper.UserHelper;

/**
 * In 9min task exports data to file located in /tmp folder with name stored in
 * request parameters. Parameters: 
 * filename - file for export located in /tmp folder. 
 * exportType - type of export. 
 * currentFile - file to export.
 * 
 * @author Alexander Oleynik
 */
public class ExportTaskSubscriber extends AbstractSubscriber {

	public final static String TYPE_PARAM_THEME = "theme";
	public final static String TYPE_PARAM_FOLDER = "folder";
	public final static String TYPE_PARAM_FULL = "full";
	public final static String TYPE_PARAM_SITE = "site";
	public final static String TYPE_PARAM_RESOURCES = "resources";

	public void onMessage(Message message) {
		ExportMessage msg = (ExportMessage)message;
		VosaoContext.getInstance().setUser(UserHelper.ADMIN);
		ZipOutStreamTaskAdapter zipOutStreamTaskAdapter = 
			new ZipOutStreamTaskAdapterImpl(getBusiness());
		if (msg.getCurrentFile() == null) {
			removeExportFile(msg.getFilename());
			getBusiness().getSystemService().getCache().remove(
					msg.getFilename());
		}
		zipOutStreamTaskAdapter.setFileCounter(Integer.valueOf(
				msg.getFileCounter()));
		try {
			openStream(zipOutStreamTaskAdapter, msg.getFilename());
			zipOutStreamTaskAdapter.setStartFile(msg.getCurrentFile());
			logger.info("Export " + msg.getExportType() + " " 
					+ msg.getCurrentFile() + " " + msg.getFileCounter());
	        if (msg.getExportType().equals(TYPE_PARAM_THEME)) {
	        	List<TemplateEntity> templates = getDao().getTemplateDao()
	        			.getById(msg.getIds());
	        	
	        	List<StructureEntity> structures = getDao().getStructureDao()
	        		.getById(msg.getStructureIds());
	        	
	        	getImportExportBusiness().createTemplateExportFile(
	        			zipOutStreamTaskAdapter, templates, structures);
	        }
	        if (msg.getExportType().equals(TYPE_PARAM_FOLDER)) {
	        	FolderEntity folder = getDao().getFolderDao().getById(
	        			msg.getFolderId());
	        	getImportExportBusiness().createExportFile(
	        			zipOutStreamTaskAdapter, folder);
	        }
	        if (msg.getExportType().equals(TYPE_PARAM_SITE)) {
	    		getImportExportBusiness().createSiteExportFile(
	    				zipOutStreamTaskAdapter);
	        }
	        if (msg.getExportType().equals(TYPE_PARAM_FULL)) {
	    		getImportExportBusiness().createFullExportFile(
	    				zipOutStreamTaskAdapter);
	        }
	        if (msg.getExportType().equals(TYPE_PARAM_RESOURCES)) {
	        	getImportExportBusiness().createResourcesExportFile(
	        			zipOutStreamTaskAdapter);
	        }
			saveZip(zipOutStreamTaskAdapter, msg.getFilename(), true);
    		getBusiness().getFileBusiness().saveFile("/tmp/" 
    				+ msg.getFilename() + ".txt", "OK".getBytes());
			logger.info("Export finished. " + zipOutStreamTaskAdapter
					.getFileCounter());
		} catch (TaskTimeoutException e) {
			try {
				saveZip(zipOutStreamTaskAdapter, msg.getFilename(), false);
			}
			catch (Exception e2) {
				e2.printStackTrace();
			}
			getMessageQueue().publish(new ExportMessage.Builder()
					.setFilename(msg.getFilename())
					.setCurrentFile(zipOutStreamTaskAdapter.getCurrentFile())
					.setFileCounter(zipOutStreamTaskAdapter.getFileCounter()) 
					.setExportType(msg.getExportType()).create());
			logger.info("Added new export task "
					+ zipOutStreamTaskAdapter.getCurrentFile());
		} catch (Exception e) {
			logger.error(e.toString() + " " + e.getMessage() + "\n"
					+ ExceptionUtils.getFullStackTrace(e));
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
		return getBusiness().getImportExportBusiness();
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
						IOUtils.copy(in, out);
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
		if (exportType.equals(TYPE_PARAM_SITE)) {
			return "exportSite.vz";
		}
		if (exportType.equals(TYPE_PARAM_THEME)) {
			return "exportTheme.vz";
		}
		if (exportType.equals(TYPE_PARAM_FOLDER)) {
			return "exportFolder.vz";
		}
		if (exportType.equals(TYPE_PARAM_FULL)) {
			return "exportFull.vz";
		}
		if (exportType.equals(TYPE_PARAM_RESOURCES)) {
			return "exportResources.vz";
		}
		return null;
	}

}
