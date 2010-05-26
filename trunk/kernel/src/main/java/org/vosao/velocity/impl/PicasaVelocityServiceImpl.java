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

package org.vosao.velocity.impl;

import java.util.Collections;
import java.util.List;

import org.vosao.business.Business;
import org.vosao.common.AbstractServiceBeanImpl;
import org.vosao.service.vo.AlbumVO;
import org.vosao.service.vo.PhotoVO;
import org.vosao.velocity.PicasaVelocityService;

/**
 * 
 * @author Alexander Oleynik
 *
 */
public class PicasaVelocityServiceImpl extends AbstractServiceBeanImpl 
		implements PicasaVelocityService {

	public PicasaVelocityServiceImpl(Business business) {
		super(business);
	}

	@Override
	public AlbumVO findAlbumById(String albumId) {
		try {
			return new AlbumVO(getBusiness().getPicasaBusiness().findAlbum(
					albumId));
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public AlbumVO findAlbumByTitle(String title) {
		try {
			return new AlbumVO(getBusiness().getPicasaBusiness()
					.findAlbumByTitle(title));
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<AlbumVO> findAlbums() {
		try {
			return AlbumVO.create(getBusiness().getPicasaBusiness()
					.selectAlbums());
		}
		catch (Exception e) {
			logger.error(e.getMessage());
			return Collections.EMPTY_LIST;
		}
	}

	@Override
	public List<PhotoVO> findPhotosInAlbum(String albumId) {
		try {
			return PhotoVO.create(getBusiness().getPicasaBusiness().selectPhotos(
					albumId));
		}
		catch (Exception e) {
			logger.error(e.getMessage());
			return Collections.EMPTY_LIST;
		}
	}

	@Override
	public List<PhotoVO> findPhotos(String title, int count) {
		try {
			return PhotoVO.create(getBusiness().getPicasaBusiness().findPhotos(
					title, count));
		}
		catch (Exception e) {
			logger.error(e.getMessage());
			return Collections.EMPTY_LIST;
		}
	}
}
