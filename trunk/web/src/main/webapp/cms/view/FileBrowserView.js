// file FileBrowserView.js
/*
 Vosao CMS. Simple CMS for Google App Engine.
 
 Copyright (C) 2009-2011 Vosao development team.

 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA

 email: vosao.dev@gmail.com
*/

define(['text!template/fileBrowser.html', 'jquery.treeview', 'jquery.cookie'], 
function(html) {
	
	console.log('Loading FileBrowserView.js');
	
	var rootFolder;
	var browserMode = 'ckeditor';

	var config = null;
	var albums = null;
	var photos = null;


	function postRender() {
	    $("#tabs").tabs();
		Vosao.initJSONRpc(loadData);
	    if (Vosao.getQueryParam('mode')) {
	    	browserMode = Vosao.getQueryParam('mode');
	    }
		window.scrollbars.visible = true;
	    adjustFilesDivHeight();
	    $(window).resize(adjustFilesDivHeight);
	}

	function adjustFilesDivHeight() {
		$('#filesDiv').css('height', ($(window).height() - 120) + 'px');
		$('#filesDiv').css('width', ($(window).width() - 400) + 'px');
	}

	function loadData() {
		loadFolderTree();
		loadTree();
		loadConfig();
	}

	function loadFolderTree() {
		Vosao.jsonrpc.folderService.getTree(function(rootItem) {
	        rootFolder = rootItem;
	        $("#folders-tree").html(renderFolderList(rootItem));
	        $("#folders-tree a").click(function() {
	        	onFolderSelected($(this).attr('data-id'));    	
	        });
	       	$("#folders-tree").treeview();
	       	selectFolder();
	    });
	}

	function selectFolder() {
	   	if ($.cookie('fileBrowserPath')) {
	   		Vosao.jsonrpc.folderService.getFolderByPath(function(r) {
	   			if (r) {
	   		   		onFolderSelected(r.entity.id);
	   			}
	   			else {
	   		   		onFolderSelected(rootFolder.entity.id);
	   			}
	   		}, $.cookie('fileBrowserPath'));
			$.cookie('fileBrowserPath', null, {path:'/', expires: 10});
	   	}
	   	else {
	   		onFolderSelected(rootFolder.entity.id);
	   	}	
	}

	/**
	 * Render ul/li list for folder item and subitems.
	 * @param item - java class TreeItemDecorator<FolderEntity>
	 */
	function renderFolderList(item) {
	   	var titleLink = "<a data-id=\""	+ item.entity.id + "\">" 
	   		+ item.entity.title + "</a>";
	   	
	    var s = "<li>" + titleLink;
	    if (item.children.list.length > 0) {
	        s += "<ul>\n";
	        for (var i = 0; i < item.children.list.length; i++) {
	            var childItem = item.children.list[i];
	            s += renderFolderList(childItem);
	        }
	        s += "</ul>\n";
	    }
	    s += "</li>\n"
	    return s;
	}

	function onFolderSelected(folderId) {
		Vosao.jsonrpc.folderService.getFolderPath(function(folderPath) {
			if (folderPath) {
				$('#currentFolder').html(folderPath);
			}
			else {
				$('#currentFolder').html('/');
			}	
			Vosao.jsonrpc.fileService.getByFolder(function(files) {
	            var result = "";
	            for (i = 0; i < files.list.length; i++) {
	                var file = files.list[i];
	                var title = " title=\"" + file.title + "\"";
	                var thumbnail = '';
	                if (Vosao.isImage(file.filename)) {
	                    thumbnail = '<img width="160" height="120" src="/file' 
	                        + folderPath + '/' + file.filename + '" />';
	                }
	                result += '<li><div class="file-border" data-id="' + file.id 
	                	+ '"' + title 
	                    + "><div class=\"file-thumbnail\">" + thumbnail 
	                    + "</div>" + file.filename 
	                    + "</div></li>\n";
	            }
	            $("#files").html(result);
	            $('#files .file-border').click(function() {
	            	onFileSelected($(this).attr('data-id'));
	            });
	        }, folderId);    
	    }, folderId);
	}

	function onFileSelected(fileId) {
		Vosao.jsonrpc.fileService.getFilePath(function(path) {
	       	if (browserMode == 'ckeditor') {
	       		var funcNum = Vosao.getQueryParam('CKEditorFuncNum');
	       		window.opener.CKEDITOR.tools.callFunction(funcNum, path);
	       		window.close();
	       	}
	       	if (browserMode == 'page') {
	       		window.opener.Vosao.app.pageView.setResource(path);
	       		window.close();
	       	}
	    }, fileId);
	} 

	function onPageSelected(path) {
	   	if (browserMode == 'ckeditor') {
	   		var funcNum = Vosao.getQueryParam('CKEditorFuncNum');
	   		window.opener.CKEDITOR.tools.callFunction(funcNum, path);
       		window.close();
	   	}
	   	if (browserMode == 'page') {
	   		window.opener.Vosao.app.pageView.setResource(path);
       		window.close();
	   	}
	} 

	function loadTree() {
		Vosao.jsonrpc.pageService.getTree(function(r) {
			$('#pages-tree').html(renderPage(r));
			$('#pages-tree a').click(function() {
				onPageSelected($(this).attr('data-url'));
			});
			$("#pages-tree").treeview({
				animated: "fast",
				collapsed: true
			});
		});
	}

	function renderPage(vo) {
		var pageUrl = vo.entity.friendlyURL;
		var p = vo.entity.hasPublishedVersion ? 'published' : 'unpublished';
		var published = messages(p);
		var html = '<li> <img src="/static/images/'+ p +'.png" title="' + published 
				+ '" width="16px" />' 
				+ ' <a data-url="' + pageUrl + '">' + vo.entity.title + '</a>';
		if (vo.children.list.length > 0) {
			html += '<ul>';
			$.each(vo.children.list, function(n, value) {
				html += renderPage(value);
			});
			html += '</ul>';
		}
		return html + '</li>';
	}

	// Picasa

	function loadConfig() {
		Vosao.jsonrpc.configService.getConfig(function(r) {
			config = r;
			if (config.enablePicasa) {
				loadAlbums();
			}
			else {
				$('#albums').html(messages('picasa_not_enabled'));
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
			h += '<a class="album" data-i="' + i + '">'
				+ '<img src="/static/images/Photos.png" /><p>' 
				+ value.title + '</p></a>';
		});
		$('#albums').html(h);
		$('#albums a').click(function() {
			onAlbumSelect($(this).attr('data-i'));
		});
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
			h += '<div class="photo"><a data-i="' + i + '">'
				+ '<img src="' + value.thumbnailURL + '" />'
				+ '<p>' + value.title + '</p></a></div>';
		});
		$('#photos').html(h);
		$('#photos a').click(function() {
			onPhotoSelect($(this).attr('data-i'));
		});
	}

	function onPhotoSelect(i) {
	   	var path = photos[i].URL;
		if (browserMode == 'ckeditor') {
	   		var funcNum = Vosao.getQueryParam('CKEditorFuncNum');
	   		window.opener.CKEDITOR.tools.callFunction(funcNum, path);
	   		window.close();
	   	}
	   	if (browserMode == 'page') {
	   		window.opener.Vosao.app.pageView.setResource(path);
	   		window.close();
	   	}
	}

	
	return Backbone.View.extend({
		
		css: ['/static/css/jquery.treeview.css', '/static/css/fileBrowser.css'],
		
		el: $('#content'),
		
		tmpl: _.template(html),
		
		render: function() {
			Vosao.addCSSFiles(this.css);
			this.el.html(this.tmpl({messages:messages}));
			postRender();
			this.el.fadeIn();
		},
		
		remove: function() {
			this.el.html('').hide();
			Vosao.removeCSSFiles(this.css);
		}
		
	});
	
});