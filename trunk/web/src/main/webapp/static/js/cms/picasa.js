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

var config = null;
var albums = null;
var photos = null;

$(function(){
	Vosao.initJSONRpc(loadConfig);
});

function loadConfig() {
	Vosao.jsonrpc.configService.getConfig(function(r) {
		config = r;
		if (config.enablePicasa) {
			loadAlbums();
		}
		else {
			$('#albums').html('Picasa is not enabled.');
		}
	});
}

function loadAlbums() {
	Vosao.jsonrpc.picasaService.selectAlbums(function(r) {
		albums = r.list;
		showAlbums();
	});
}

function showAlbums() {
	var h = '';
	$.each(albums, function(i,value) {
		h += '<a class="album" onclick="onAlbumSelect(' + i + ')">'
			+ '<img src="/static/images/Photos.png" /><p>' 
			+ value.title + '</p></a>';
	});
	$('#albums').html(h);
}

function onAlbumSelect(i) {
	$('#album-location').text(albums[i].title);
	Vosao.jsonrpc.picasaService.selectPhotos(function(r) {
		photos = r.list;
		showPhotos();
	}, albums[i].id)
}

function showPhotos() {
	var h = '';
	$.each(photos, function(i,value) {
		h += '<div><a class="photo" onclick="onPhotoSelect(' + i + ')">'
			+ '<img src="' + value.thumbnailURL + '" />'
			+ '<p>' + value.title + '</p></a></div>';
	});
	$('#photos').html(h);
}

function onPhotoSelect(i) {
	alert('TODO');
}