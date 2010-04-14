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

package org.vosao.service.vo;

import com.google.gdata.data.photos.AlbumEntry;
import com.google.gdata.data.photos.PhotoEntry;

/**
 * Value object to be returned from services.
 * @author Alexander Oleynik
 */
public class PhotoVO {

    private PhotoEntry photo;

	public PhotoVO(final PhotoEntry entry) {
		photo = entry;
	}

	public String getTitle() {
		return photo.getTitle().getPlainText();
	}

	public String getThumbnailURL() {
		return photo.getMediaThumbnails().get(1).getUrl();
	}

	public String getURL() {
		return photo.getMediaContents().get(0).getUrl();
	}

}
