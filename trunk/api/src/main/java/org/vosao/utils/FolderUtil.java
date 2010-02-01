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

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.zip.ZipEntry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.business.decorators.TreeItemDecorator;
import org.vosao.entity.FolderEntity;

/**
 * Folder utils.
 * 
 * @author Aleksandr Oleynik
 */
public class FolderUtil {
	
	private static Log logger = LogFactory.getLog(FolderUtil.class);
	
	public static String[] getPathChain(final String path) 
			throws UnsupportedEncodingException {
        if (path == null) {
        	return new String[]{};
        }
		String[] chain = path.split("/");
		return Arrays.copyOfRange(chain, 1, chain.length);
	}
	
	public static String[] getPathChain(final ZipEntry entry) 
			throws UnsupportedEncodingException {
		return getPathChain("/" + entry.getName());
	}

	public static String[] getFolderChain(final String[] chain) {
		return Arrays.copyOfRange(chain, 0, chain.length - 1);
	}
	
	private static FolderEntity getFolder(final String[] chain, final int index, 
			final TreeItemDecorator<FolderEntity> tree) {

		if (chain.length == 0) {
			return tree.getEntity();
		}
		String folderName = chain[index];
		for(TreeItemDecorator<FolderEntity> folder : tree.getChildren()) {
			if (folder.getEntity().getName().equals(folderName)) {
				if (index == chain.length-1) {
					return folder.getEntity();
				}
				else {
					return getFolder(chain, index + 1, folder);
				}
			}
		}
		return null;
	}

	public static String getFolderName(final String path) {
		String[] chain;
		try {
			chain = getPathChain(path);
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage());
			return null;
		}
		if (chain.length > 0) {
			return chain[chain.length-1];
		}
		return null;
	}
	
	public static String getFilePath(final String path) {
		int s = path.lastIndexOf("/");
		return path.substring(0, s);
	}
	
	public static String getFileName(final String path) {
		int s = path.lastIndexOf("/");
		return path.substring(s + 1, path.length());
	}

	public static String getFilePath(final ZipEntry entry) {
		return getFilePath("/" + entry.getName());
	}

	public static String getFileExt(final String path) {
		int s = path.lastIndexOf(".");
		return path.substring(s + 1, path.length()).toLowerCase();
	}
	
}