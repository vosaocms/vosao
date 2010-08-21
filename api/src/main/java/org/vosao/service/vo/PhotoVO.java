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

package org.vosao.service.vo;

import java.util.ArrayList;
import java.util.List;

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

	public String getId() {
		return photo.getGphotoId();
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

	public static List<PhotoVO> create(List<PhotoEntry> photos) {
		List<PhotoVO> result = new ArrayList<PhotoVO>();
		for (PhotoEntry photo : photos) {
			result.add(new PhotoVO(photo));
		}
		return result;
	}
	
}
