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

package org.vosao.business.mq;

/**
 * 
 * @author Alexander Oleynik
 *
 */
public enum Topic {
	
	/**
	 *  SimpleMessage: message -> user email
	 */
	LOGIN,

	/**
	 *  SimpleMessage: message -> file url
	 */
	FILE_CHANGED,

	/**
	 *  ExportMessage
	 */
	EXPORT,
	
	/**
	 *  ImportMessage
	 */
	IMPORT,
	
	/**
	 * SimpleMessage
	 */
	IMPORT_FILE,
	
	/**
	 * SimpleMessage
	 */
	IMPORT_FOLDER,

	/**
	 *  PageMessage
	 */
	PAGES_CHANGED,

	/**
	 *  PageMessage
	 */
	PAGES_DELETED,

	/**
	 * Full search reindex. Receives IndexMessage.
	 */
	REINDEX,
	
	/**
	 * SimpleMessage: message -> mode
	 */
	SESSION_CLEAN;
	
}
