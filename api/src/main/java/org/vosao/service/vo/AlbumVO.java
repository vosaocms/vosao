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

import java.util.ArrayList;
import java.util.List;

import org.vosao.common.Messages;

import com.google.gdata.data.photos.AlbumEntry;

/**
 * Value object to be returned from services.
 * @author Alexander Oleynik
 */
public class AlbumVO {

    private AlbumEntry album;

	public AlbumVO(final AlbumEntry entry) {
		album = entry;
	}

	public String getId() {
		return album != null ? album.getGphotoId(): Messages.get("not_found");
	}

	public String getTitle() {
		return album != null ? album.getTitle().getPlainText(): 
			Messages.get("not_found");
	}

	public String getName() {
		return album != null ? album.getName(): Messages.get("not_found");
	}
	
	public static List<AlbumVO> create(List<AlbumEntry> list) {
		List<AlbumVO> result = new ArrayList<AlbumVO>();
		for (AlbumEntry album : list) {
			result.add(new AlbumVO(album));
		}
		return result;
	}
	
}
