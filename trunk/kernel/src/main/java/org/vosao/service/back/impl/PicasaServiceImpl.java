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

package org.vosao.service.back.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.vosao.i18n.Messages;
import org.vosao.service.ServiceResponse;
import org.vosao.service.back.PicasaService;
import org.vosao.service.impl.AbstractServiceImpl;
import org.vosao.service.vo.AlbumVO;
import org.vosao.service.vo.PhotoVO;

import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.photos.AlbumEntry;
import com.google.gdata.data.photos.PhotoEntry;

/**
 * 
 * @author Alexander Oleynik
 *
 */
public class PicasaServiceImpl extends AbstractServiceImpl 
		implements PicasaService {

	@Override
	public List<AlbumVO> selectAlbums() {
		try {
			List<AlbumVO> result = new ArrayList<AlbumVO>();
			for (AlbumEntry album : getBusiness().getPicasaBusiness()
					.selectAlbums()) {
				result.add(new AlbumVO(album));
			}
			return result;
		}
		catch (Exception e) {
			logger.error(e.getMessage());
			return Collections.EMPTY_LIST;
		}
	}

	@Override
	public List<PhotoVO> selectPhotos(String albumId) {
		try {
			List<PhotoVO> result = new ArrayList<PhotoVO>();
			for (PhotoEntry photo : getBusiness().getPicasaBusiness()
					.selectPhotos(albumId)) {
				result.add(new PhotoVO(photo));
			}
			return result;
		}
		catch (Exception e) {
			logger.error(e.getMessage());
			return Collections.EMPTY_LIST;
		}
	}
	
	@Override
	public ServiceResponse addAlbum(String title) {
		try {
			AlbumEntry myAlbum = new AlbumEntry();
			myAlbum.setTitle(new PlainTextConstruct(title));
			myAlbum.setDescription(new PlainTextConstruct(title));
			getBusiness().getPicasaBusiness().addAlbum(myAlbum);
			return ServiceResponse.createSuccessResponse(
					Messages.get("success"));
		}
		catch (Exception e) {
			return ServiceResponse.createErrorResponse(e.getMessage());
		}
	}

	@Override
	public ServiceResponse removeAlbum(String albumId) {
		try {
			getBusiness().getPicasaBusiness().removeAlbum(albumId);
			return ServiceResponse.createSuccessResponse(Messages.get("success"));
		}
		catch (Exception e) {
			return ServiceResponse.createErrorResponse(e.getMessage());
		}
	}

	@Override
	public ServiceResponse removePhoto(String albumId, String photoId) {
		try {
			getBusiness().getPicasaBusiness().removePhoto(albumId, photoId);
			return ServiceResponse.createSuccessResponse(Messages.get("success"));
		}
		catch (Exception e) {
			return ServiceResponse.createErrorResponse(e.getMessage());
		}
	}
	
}
