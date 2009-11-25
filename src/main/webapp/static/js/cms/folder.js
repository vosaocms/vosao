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

var files = '';
var folder = null;
var editMode = folderId != '';

$(function() {
    $("#tabs").tabs();
    $("#file-upload").dialog({ width: 400, autoOpen: false });
    $('#upload').ajaxForm(afterUpload);
    initJSONRpc(loadData);
    $('#title').change(onTitleChange);
    $('#saveButton').click(onUpdate);
    $('#cancelButton').click(onCancel);
    $('#exportButton').click(onExport);
    $('#createFileButton').click(onCreateFile);
    $('#uploadButton').click(onUpload);
    $('#deleteFilesButton').click(onDeleteFiles);
    $('#fileCancelButton').click(onCancel);
    $('#addChildButton').click(onAddChild);
    $('#deleteFoldersButton').click(onDelete);
    $('#folderCancelButton').click(onCancel);
    $('#fileUploadCancelButton').click(onFileUploadCancel);
});

function loadData() {
    loadFolder();
    loadChildren();
    loadFiles();
    if (!editMode) {
        $('.filesTab').hide();
        $('.childrenTab').hide();
    }
    else {
        $('.filesTab').show();
        $('.childrenTab').show();
    }
}

function loadFolder() {
	jsonrpc.folderService.getFolder(function (r) {
		folder = r;
		initFormFields();
	}, folderId);
}

function initFormFields() {
    if (editMode) {
        $('#title').val(folder.title);   
        $('#name').val(folder.name);   
    }
    else {
        $('#title').val('');   
        $('#name').val('');   
    }
}

function onUpload() {
    $("#file-upload").dialog("open");
}
function onFileUploadCancel() {
	$("#file-upload").dialog("close");
}

function afterUpload(data) {
    var s = data.split('::');
    var result = s[1];
    var msg = s[2]; 
    if (result == 'success') {
        info('File was successfully uploaded.');
        loadFiles();
    }
    else {
        error("Error during uploading file. " + msg);
    }
    $("#file-upload").dialog("close");
}

function loadFiles() {
	jsonrpc.fileService.getByFolder(function (r) {
	    files = r.list;
	    var h = '<table class="form-table"><tr><th></th><th>Title</th>\
		    <th>Filename</th><th>Mime type</th><th>Size</th></tr>';
	    $.each(files, function(i, file) { 
	        h += '<tr>\
<td><input type="checkbox" name="item' + i + '" value="' + file.id + '"/></td>\
<td><a href="/cms/file.jsp?id=' + file.id + '">' + file.title + '</a></td>\
<td>' + file.filename + '</td>\
<td>' + file.mimeType + '</td>\
<td>' + file.size + ' bytes</td>\
</tr>';
        });
	    h += '</table>';   
	    $('#filesTable').html(h);
	    $('#filesTable tr:even').addClass('even');
	}, folderId);
}

function getCheckedFilesIds() {
    var result = [];
	$("#filesTable input[@type=checkbox]:checked").each(function() {
        result.push(this.value);
    });
    return result; 
}

function onDeleteFiles() {
    var ids = javaList(getCheckedFilesIds());
    if (ids.list.length == 0) {
        info('Nothing selected.');
        return;
    }
    if (confirm('Are you sure?')) {
    	jsonrpc.fileService.deleteFiles(function (r) {
            if (r.result == "success") {
                loadFiles();
                info("Files was successfully deleted.");
                }
            else {
                error("Error during deleting files. " + r.messsage);
                        }
        }, ids);
    }
}

function onCreateFile() {
    location.href = "/cms/file.jsp?folderId=" + folderId;
}

function onTitleChange() {
	if (editMode) {
		return;
	}
    var url = $("#name").val();
    var title = $("#title").val();
    if (url == "") {
        $("#name").val(urlFromTitle(title));
    }
}

function loadChildren() {
	if (!editMode) {
		return;
	}
	jsonrpc.folderService.getByParent(function (r) {
		var html = '<table class="form-table">\
<tr><th></th><th>Title</th><th>Name</th></tr>';
        $.each(r.list, function (i, child) {
            html += '<tr><td><input type="checkbox" value="' + child.id
                + '"/></td><td><a href="/cms/folder.jsp?id=' + child.id 
                + '">' + child.title + '</td><td>' + child.name + '</td></tr>';
        });
        $('#children').html(html + '</table>');
        $('#children tr:even').addClass('even');
	}, folderId);
}

function onUpdate() {
    var vo = javaMap({
        id : folderId,
        parent : folderParentId,
        name : $('#name').val(),
        title : $('#title').val(),
    });
    jsonrpc.folderService.saveFolder(function (r) {
        if (r.result == 'success') {
            info("Folder was successfully saved.");
            if (folderId == '') {
            	folderId = r.message;
            	editMode = true;
            	loadData();
            }
        }
        else {
        	showServiceMessages(r);
        }
    }, vo);
}

function onCancel() {
    location.href = '/cms/folders.jsp';
}

function onExport() {
    location.href = '/cms/export?type=folder&id=' + folderId;
}

function onAddChild() {
    location.href = '/cms/folder.jsp?parent=' + folderParentId;
}

function getParentFolderIds() {
    var result = [];
    $("#children input[@type=checkbox]:checked").each(function() {
        result.push(this.value);
    });
    return result; 
}

function onDelete() {
    var ids = javaList(getParentFolderIds());
    if (ids.list.length == 0) {
        info('Nothing selected.');
        return;
    }
    if (confirm('Are you sure?')) {
    	jsonrpc.folderService.deleteFolder(function (r) {
            if (r.result == "success") {
                loadChildren();
                info("Folders was successfully deleted.");
            }
            else {
                error("Error during deleting folders. " + r.messsage);
            }
        }, ids);
    }
}

