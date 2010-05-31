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

package org.vosao.utils;


/**
 * @author Alexander Oleynik
 */
public class UrlUtil {

	/**
	 * Extract parent page friendly URL.
	 * For page /my/test/hope parent url is /my/test
	 * @param friendlyUrl - page friendlyURL
	 * @return parent friendlyURL.
	 */
	static public String getParentFriendlyURL(final String friendlyUrl) {
		if (friendlyUrl == null || friendlyUrl.equals("/")) {
			return "";
		}
		int lastSlash = friendlyUrl.lastIndexOf('/');
		if (lastSlash == 0 || lastSlash == -1) {
			return "/";
		}
		return friendlyUrl.substring(0, lastSlash);
	}

	/**
	 * Extract last name in friendlyURL.
	 * For page url /my/test/hope last name will be "hope"
	 * @param friendlyUrl
	 * @return
	 */
	static public String getNameFromFriendlyURL(final String friendlyUrl) {
		if (friendlyUrl == null || friendlyUrl.equals("/")) {
			return "";
		}
		int lastSlash = friendlyUrl.lastIndexOf('/');
		if (lastSlash == -1) {
			return "";
		}
		return friendlyUrl.substring(lastSlash + 1, friendlyUrl.length());
	}
	
	/**
	 * Convert title to url. Replace spaces to _
	 */
	static public String titleToURL(String title) {
		return title.replace(' ', '_').toLowerCase();
	}
	
}
