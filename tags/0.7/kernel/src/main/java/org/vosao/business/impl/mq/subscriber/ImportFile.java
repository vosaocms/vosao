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

import java.io.UnsupportedEncodingException;

import org.dom4j.DocumentException;
import org.vosao.business.impl.ImportExportBusinessImpl;
import org.vosao.business.impl.mq.AbstractSubscriber;
import org.vosao.business.mq.Message;
import org.vosao.business.mq.message.SimpleMessage;
import org.vosao.common.VfsNode;
import org.vosao.dao.DaoTaskException;

/**
 * Import file.
 * 
 * @author Alexander Oleynik
 *
 */
public class ImportFile extends AbstractSubscriber {

	public void onMessage(Message message) {
		try {
			SimpleMessage msg = (SimpleMessage)message;
			String path = msg.getMessage();
			if (ImportExportBusinessImpl.isGlobalSequenceImportFile(
					path.substring(1))) {
				return;
			}
			VfsNode node = VfsNode.find(path);
			if (node == null) {
				logger.error("VFS node not found " + path);
				return;
			}
			String xml = new String(node.getData(), "UTF-8");
			if (!getBusiness().getImportExportBusiness().getExporterFactory()
					.getSiteExporter().importSystemFile(path.substring(1), xml)) {
				getBusiness().getImportExportBusiness().getExporterFactory()
					.getResourceExporter().importResourceFile(path, node.getData());
			}
		}
		catch (DocumentException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		catch (DaoTaskException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}

}