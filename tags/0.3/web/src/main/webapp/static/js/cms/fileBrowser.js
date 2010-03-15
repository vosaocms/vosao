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

var rootFolder;
var browserMode = 'ckeditor';

$(function(){
    $("#tabs").tabs();
	Vosao.initJSONRpc(loadData);
    if (Vosao.getQueryParam('mode')) {
    	browserMode = Vosao.getQueryParam('mode');
    }
    window.scrollbars.visible = true;
});

function loadData() {
	loadFolderTree();
	loadTree();
}

function loadFolderTree() {
	Vosao.jsonrpc.folderService.getTree(function(rootItem) {
        rootFolder = rootItem;
        $("#folders-tree").html(renderFolderList(rootItem));        
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
   	var titleLink = "<a href=\"#\" onclick=\"onFolderSelected(" 
        	+ item.entity.id + ")\">" + item.entity.title
       	    + "</a>";
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
                var onClick = " onclick=\"onFileSelected(" + file.id + ")\"";
                var thumbnail = '';
                if (Vosao.isImage(file.filename)) {
                    thumbnail = '<img width="160" height="120" src="/file' 
                        + folderPath + '/' + file.filename + '" />';
                }
                result += "<li><div class=\"file-border\"" + onClick 
                    + title 
                    + "><div class=\"file-thumbnail\">" + thumbnail 
                    + "</div>" + file.filename 
                    + "</div></li>\n";
            }
            $("#files").html(result);            	
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
       		window.opener.setResource(path);
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
   		window.opener.setResource(path);
   		window.close();
   	}
} 

function loadTree() {
	Vosao.jsonrpc.pageService.getTree(function(r) {
		$('#pages-tree').html(renderPage(r));
		$("#pages-tree").treeview({
			animated: "fast",
			collapsed: true
		});
	});
}

function renderPage(vo) {
	var pageUrl = vo.entity.friendlyURL;
	var p = vo.entity.hasPublishedVersion ? 'published' : 'unpublished';
	var html = '<li> <img src="/static/images/'+ p +'.png" title="' + p 
			+ '" width="16px" />' 
			+ ' <a href="#" onclick="onPageSelected(\'' + pageUrl + '\')">'
			+ vo.entity.title
			+ '</a>';
	if (vo.children.list.length > 0) {
		html += '<ul>';
		$.each(vo.children.list, function(n, value) {
			html += renderPage(value);
		});
		html += '</ul>';
	}
	return html + '</li>';
}
