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

package org.vosao.business;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import com.google.gdata.client.photos.PicasawebService;
import com.google.gdata.data.photos.AlbumEntry;
import com.google.gdata.data.photos.PhotoEntry;
import com.google.gdata.util.ServiceException;

/**
 * 
 * @author Alexander Oleynik
 *
 */
public interface PicasaBusiness {

	PicasawebService getPicasawebService();

	List<AlbumEntry> selectAlbums() 
			throws MalformedURLException, IOException, ServiceException;

	List<PhotoEntry> selectPhotos(String albumId) 
			throws MalformedURLException, IOException, ServiceException;

	AlbumEntry addAlbum(AlbumEntry album) 
			throws MalformedURLException, IOException, ServiceException;

	void removeAlbum(String albumId) 
			throws IOException, ServiceException;

	void removePhoto(String albumId, String photoId) 
			throws MalformedURLException, IOException, ServiceException;

	AlbumEntry findAlbum(String albumId) 
			throws MalformedURLException, IOException, ServiceException;
		
	PhotoEntry upload(String albumId, byte[] data, String name) 
			throws MalformedURLException, IOException, ServiceException;
	
	AlbumEntry findAlbumByTitle(String title)
		throws MalformedURLException, IOException, ServiceException;
	
	List<PhotoEntry> findPhotos(String title, int count)
		throws MalformedURLException, IOException, ServiceException;
	
}
