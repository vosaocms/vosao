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

package org.vosao.business.impl.mq.subscriber;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.ZipInputStream;

import javax.servlet.ServletException;

import org.dom4j.DocumentException;
import org.vosao.business.ImportExportBusiness;
import org.vosao.business.imex.task.DaoTaskAdapter;
import org.vosao.business.impl.imex.task.DaoTaskTimeoutException;
import org.vosao.business.impl.mq.AbstractSubscriber;
import org.vosao.business.mq.Message;
import org.vosao.business.mq.message.ImportMessage;
import org.vosao.common.RequestTimeoutException;
import org.vosao.common.UploadException;
import org.vosao.common.VosaoContext;
import org.vosao.entity.helper.UserHelper;
import org.vosao.utils.FolderUtil;

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
public class ImportTaskSubscriber extends AbstractSubscriber {

	public void onMessage(Message message) {
		ImportMessage msg = (ImportMessage)message;
		String ext = FolderUtil.getFileExt(msg.getFilename());
		try {
			if (ext.equals("zip")) {
				doImport1(msg);
			}
			if (ext.equals("vz")) {
				//doImport2(msg);
				doImport3(msg);
			}
		}
		catch (IOException e) {
			logger.error(e.getMessage());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void doImport1(ImportMessage msg) throws ServletException, 
			IOException {
		try {
			VosaoContext.getInstance().setUser(UserHelper.ADMIN);
			String currentFile = msg.getCurrentFile();
			getDaoTaskAdapter().setStart(msg.getStart());
			getDaoTaskAdapter().setCurrentFile(currentFile);
			getDaoTaskAdapter().setFileCounter(msg.getFileCounter());
			currentFile = currentFile == null ? "" : currentFile;
			logger.info("Import " + msg.getFilename() + " " + msg.getStart() 
					+ " " + currentFile	+ " " + msg.getFileCounter());
			byte[] data = getSystemService().getCache().getBlob(
					msg.getFilename());
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
			getSystemService().getCache().clear();
			logger.info("Import finished. " + getDaoTaskAdapter().getFileCounter());
		} catch (DaoTaskTimeoutException e) {
			getMessageQueue().publish(new ImportMessage.Builder()
					.setStart(getDaoTaskAdapter().getEnd())
					.setFilename(msg.getFilename())
					.setCurrentFile(getDaoTaskAdapter().getCurrentFile())
					.setFileCounter(getDaoTaskAdapter().getFileCounter())
					.create());
			logger.info("Added new import task " 
					+ getDaoTaskAdapter().getCurrentFile() + " "
					+ getDaoTaskAdapter().getFileCounter());
		} catch (UploadException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}

	private ImportExportBusiness getImportExportBusiness() {
		return getBusiness().getImportExportBusiness();
	}

	private DaoTaskAdapter getDaoTaskAdapter() {
		return getImportExportBusiness().getDaoTaskAdapter();
	}

	public void doImport2(ImportMessage msg) throws ServletException, 
			IOException {
		try {
			VosaoContext.getInstance().setUser(UserHelper.ADMIN);
			String currentFile = msg.getCurrentFile();
			getDaoTaskAdapter().setStart(msg.getStart());
			getDaoTaskAdapter().setCurrentFile(currentFile);
			getDaoTaskAdapter().setFileCounter(msg.getFileCounter());
			currentFile = currentFile == null ? "" : currentFile;
			logger.info("Import " + msg.getFilename() + " " + msg.getStart() 
					+ " " + currentFile + " " + msg.getFileCounter());
			byte[] data = getSystemService().getCache().getBlob(
					msg.getFilename());
			if (data == null) {
				return;
			}
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
			getSystemService().getCache().clear();
			logger.info("Import finished. " + getDaoTaskAdapter().getFileCounter());
		} catch (DaoTaskTimeoutException e) {
			getMessageQueue().publish(new ImportMessage.Builder()
					.setStart(getDaoTaskAdapter().getEnd())
					.setFilename(msg.getFilename())
					.setCurrentFile(getDaoTaskAdapter().getCurrentFile())
					.setFileCounter(getDaoTaskAdapter().getFileCounter())
					.create());
			logger.info("Added new import task " 
					+ getDaoTaskAdapter().getCurrentFile() + " "
					+ getDaoTaskAdapter().getFileCounter());
		} catch (UploadException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}

	public void doImport3(ImportMessage msg) 
			throws ServletException, IOException {
		byte[] data = getSystemService().getCache().getBlob(msg.getFilename());
		if (data == null) {
			logger.error("Imported file not found in memcache. " 
					+ msg.getFilename());
			return;
		}
		ByteArrayInputStream inputData = new ByteArrayInputStream(data);
		ZipInputStream in = new ZipInputStream(inputData);
		try {
			getImportExportBusiness().importUnzip(in, msg.getCurrentFile());
		}
		catch (RequestTimeoutException e) {
			getBusiness().getMessageQueue().publish(new ImportMessage(
					msg.getFilename(), 0, e.getMessage(), 0));
		}
		in.close();
	}	
	
}
