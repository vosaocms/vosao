// file FolderView.js
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

define(['text!template/folder.html', 'jquery.form'], 
function(html) {
	
	console.log('Loading FolderView.js');
	
	var folderId = null;
	var folderParentId = null;

	var files = '';
	var folder = null;
	var editMode = folderId != '';
	var folderRequest = null;
	var permissions = null;

	var exportTimer = null;
	var clockTimer = null;
	var clockSeconds = 0;
	var exportFilename = null;

	function postRender() {
	    $("#export-dialog").dialog({ width: 400, autoOpen: false });
	    $("#file-upload").dialog({ width: 400, autoOpen: false });
	    $("#permission-dialog").dialog({ width: 400, autoOpen: false });
	    editMode = folderId != '';
	    var tab = $("#tabs").tabs();
	    Vosao.selectTabFromQueryParam(tab);
	    $('#upload').ajaxForm(afterUpload);
	    Vosao.initJSONRpc(loadData);
	    $('#title').change(onTitleChange);
	    $('#folderForm').submit(function() {onUpdate(); return false;});
	    $('#cancelButton').click(onCancel);
	    $('#exportButton').click(onExport);
	    $('#exportCancelButton').click(onExportCancel);
	    $('#createFileButton').click(onCreateFile);
	    $('#uploadButton').click(onUpload);
	    $('#deleteFilesButton').click(onDeleteFiles);
	    $('#fileCancelButton').click(onCancel);
	    $('#addChildButton').click(onAddChild);
	    $('#deleteFoldersButton').click(onDelete);
	    $('#folderCancelButton').click(onCancel);
	    $('#fileUploadCancelButton').click(onFileUploadCancel);
	    $('#addPermissionButton').click(onAddPermission);
	    $('#deletePermissionButton').click(onDeletePermission);
	    $('#permissionForm').submit(function() {onPermissionSave(); return false;});
	    $('#permissionCancelButton').click(onPermissionCancel);
	    $('#file-upload input[name=folderId]').val(folderId);
	}

	function loadData() {
		Vosao.jsonrpc.folderService.getFolderRequest(function (r) {
			folderRequest = r;
		    loadFolder();
		    loadGroups();
		    showBreadcrumbs();
		    if (!editMode) {
		        $('.filesTab').hide();
		        $('.childrenTab').hide();
		    }
		    else {
			    loadPermissions(folderRequest.permissions);
			    loadChildren();
			    loadFiles();
		        $('.filesTab').show();
		        $('.childrenTab').show();
		        if (folder.root) {
		        	$('#saveButton').attr('disabled', 'disabled');
		        	$('#title').attr('disabled', 'disabled');
		        	$('#name').attr('disabled', 'disabled');
		        }
		        else {
		        	$('#saveButton').removeAttr('disabled');
		        	$('#title').removeAttr('disabled');
		        	$('#name').removeAttr('disabled');
		        }
		    }
		}, folderId, folderParentId);
	}

	function loadFolder() {
		folder = folderRequest.folder;
		if (editMode) {
			folderParentId = String(folder.parent);
		}
		initFormFields();
		loadFolderPermission();
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
	    	Vosao.info(messages('folder.file_success_upload'));
	        callLoadFiles();
	    }
	    else {
	    	Vosao.error(messages('folder.error_during_upload') + ' ' + msg);
	    }
	    $("#file-upload").dialog("close");
	}

	function callLoadFiles() {
		Vosao.jsonrpc.fileService.getByFolder(function (r) {
			folderRequest.files = r;
			loadFiles();
		}, folderId);
	}

	function loadFiles() {
	    files = folderRequest.files;
	    var h = '<table class="form-table"><tr><th></th><th>' 
	    	+ messages('title') + '</th><th>' + messages('filename') + '</th><th>'
	    	+ messages('mimetype') + '</th><th>' + messages('size') + '</th></tr>';
	    $.each(files.list, function(i, file) { 
	        h += '<tr>\
	<td><input type="checkbox" name="item' + i + '" value="' + file.id + '"/></td>\
	<td><a href="#file/' + file.id + '">' + file.title + '</a></td>\
	<td>' + file.filename + '</td>\
	<td>' + file.mimeType + '</td>\
	<td>' + file.size + ' ' + messages('bytes') + '</td></tr>';
	    });
	    h += '</table>';   
	    $('#filesTable').html(h);
	    $('#filesTable tr:even').addClass('even');
	}

	function getCheckedFilesIds() {
	    var result = [];
		$("#filesTable input[@type=checkbox]:checked").each(function() {
	        result.push(this.value);
	    });
	    return result; 
	}

	function onDeleteFiles() {
	    var ids = Vosao.javaList(getCheckedFilesIds());
	    if (ids.list.length == 0) {
	    	Vosao.info(messages('nothing_selected'));
	        return;
	    }
	    if (confirm(messages('are_you_sure'))) {
	    	Vosao.jsonrpc.fileService.deleteFiles(function (r) {
	            if (r.result == "success") {
	                callLoadFiles();
	                Vosao.info(messages('folder.files_success_delete'));
	            }
	            else {
	            	Vosao.error(messages('folder.error_deleting_files') + ' ' 
	            			+ r.messsage);
	            }
	        }, ids);
	    }
	}

	function onCreateFile() {
	    location.href = "#addFile/" + folderId;
	}

	function onTitleChange() {
		if (editMode) {
			return;
		}
	    var url = $("#name").val();
	    var title = $("#title").val();
	    if (url == "") {
	        $("#name").val(Vosao.urlFromTitle(title));
	    }
	}

	function callLoadChildren() {
		Vosao.jsonrpc.folderService.getByParent(function(r) {
			folderRequest.children = r;
			loadChildren();
		}, folderId);
	}

	function loadChildren() {
		if (!editMode) {
			return;
		}
		var html = '<table class="form-table"><tr><th></th><th>' 
			+ messages('title') + '</th><th>' + messages('name') + '</th></tr>';
	    $.each(folderRequest.children.list, function (i, child) {
	        html += '<tr><td><input type="checkbox" value="' + child.id
	            + '"/></td><td><a href="#folder/' + child.id 
	            + '">' + child.title + '</td><td>' + child.name + '</td></tr>';
	    });
	    $('#children').html(html + '</table>');
	    $('#children tr:even').addClass('even');
	}

	function onUpdate() {
	    var vo = Vosao.javaMap({
	        id : folderId,
	        parent : folderParentId,
	        name : $('#name').val(),
	        title : $('#title').val()
	    });
	    Vosao.jsonrpc.folderService.saveFolder(function (r) {
	        if (r.result == 'success') {
	        	Vosao.info(messages('folder.success_save'));
	            if (folderId == '') {
	            	folderId = r.message;
	            	editMode = true;
	            	$('#file-upload input[name="folderId"]').val(folderId);
	            	loadData();
	            }
	        }
	        else {
	        	Vosao.showServiceMessages(r);
	        }
	    }, vo);
	}

	function onCancel() {
	    if ($.cookie("folderReturnPath")) {
	    	location.href = $.cookie("folderReturnPath");
	    }
	    else {
	    	location.href = '#folders';
	    }
	}

	function onExport() {
		clockSeconds = 0;
		showClock();
	    $("#export-dialog").dialog("open");
	    Vosao.jsonrpc.configService.startExportFolderTask(function(r) {
	    	if (r.result == 'success') {
	    		exportFilename = r.message;
	    	    Vosao.infoMessage('#exportInfo', messages('creating_export_file'));
	            exportTimer = setInterval(checkExport, 10 * 1000);
	            clockTimer = setInterval(showClock, 1000);
	    	}
	    	else {
	    		Vosao.showServiceMessages(r);
	    	}
	    }, folder.id);
	}

	function checkExport() {
		Vosao.jsonrpc.configService.isExportTaskFinished(function(r) {
			if (r) {
				clearInterval(exportTimer);
				clearInterval(clockTimer);
				$("#export-dialog").dialog("close");
				$('#exportDialogButton').attr('disabled', false);
				location.href = '/file/tmp/' + exportFilename;
			}
		}, 'folder');
	}

	function showClock() {
		$('#timer').html(clockSeconds++ + ' ' + messages('sec') + '.');
	}

	function onAddChild() {
	    location.href = '#addFolder/' + folderId;
	}

	function getParentFolderIds() {
	    var result = [];
	    $("#children input[@type=checkbox]:checked").each(function() {
	        result.push(this.value);
	    });
	    return result; 
	}

	function onDelete() {
	    var ids = Vosao.javaList(getParentFolderIds());
	    if (ids.list.length == 0) {
	    	Vosao.info(messages('nothing_selected'));
	        return;
	    }
	    if (confirm(messages('are_you_sure'))) {
	    	Vosao.jsonrpc.folderService.deleteFolder(function (r) {
	            if (r.result == "success") {
	                callLoadChildren();
	                Vosao.info(messages('folder.success_delete'));
	            }
	            else {
	            	Vosao.error(messages('folder.error_deleting') + ' ' 
	            			+ r.messsage);
	            }
	        }, ids);
	    }
	}

	// Permissions security

	function getPermissionName(perm) {
		if (perm == 'DENIED') {
			return messages('denied');
		}
		if (perm == 'READ') {
			return messages('read');
		}
		if (perm == 'WRITE') {
			return messages('read_write');
		}
		if (perm == 'ADMIN') {
			return messages('read_write_grant');
		}
	}

	function loadPermissions(r) {
		permissions = Vosao.idMap(r.list);
		var h = '<table class="form-table"><tr><th></th><th>' + messages('group') 
		    + '</th><th>' + messages('permission') + '</th></tr>';
		$.each(permissions, function(i,value) {
			var checkbox = '';
			var editLink = value.group.name;
			if (!value.inherited) {
				checkbox = '<input type="checkbox" value="' + value.id + '">';
				editLink = '<a data-id="' + value.id + '"> ' + value.group.name + '</a>';
			}
			h += '<tr><td>' + checkbox + '</td><td>' + editLink + '</td><td>'
				+ getPermissionName(value.permission) + '</td></tr>';
		});
		$('#permissions').html(h + '</table>');
	    $('#permissions tr:even').addClass('even');
	    $('#permissions a').click(function() {
	    	onPermissionEdit($(this).attr('data-id'));
	    });
	}

	function callLoadPermissions() {
		Vosao.jsonrpc.folderPermissionService.selectByFolder(function (r) {
			loadPermissions(r);
		}, folderId);
	}

	function loadGroups() {
		var r = folderRequest.groups;
		groups = Vosao.idMap(r.list);
		var h = '';
		$.each(groups, function(i,value) {
			h += '<option value="' + value.id + '">' + value.name + '</option>';
		});
		$('#groupSelect').html(h);
	}

	function onPermissionEdit(id) {
		permission = permissions[id];
		initPermissionForm();
		$('#permission-dialog').dialog('open');
	}

	function initPermissionForm() {
		$('#permission-dialog input[type=radio]').removeAttr('checked');
		if (permission == null) {
			$('#permission-dialog input[value=READ]').attr('checked', 'checked');
			$('#groupSelect').show();
			$('#groupName').hide();
		}
		else {
			$('#permissionList input[value=' + permission.permission 
					+ ']').attr('checked', 'checked');
			$('#groupSelect').hide();
			$('#groupName').show();
			$('#groupName').text(permission.group.name);
		}
	}

	function onPermissionSave() {
		var vo = {
			folderId: folderId,
			groupId: permission == null ? $('#groupSelect').val() : 
				String(permission.group.id),
			permission: $('#permissionList input:checked')[0].value
		};
		Vosao.jsonrpc.folderPermissionService.save(function(r) {
			Vosao.showServiceMessages(r);
			$('#permission-dialog').dialog('close');
			if (r.result == 'success') {
				callLoadPermissions();
			}
		}, Vosao.javaMap(vo));
	}

	function onAddPermission() {
		permission = null;
		initPermissionForm();
		$('#permission-dialog').dialog('open');
	}

	function onDeletePermission() {
		var ids = [];
		$('#permissions input:checked').each(function() {
			ids.push(this.value);
		});
		if (ids.length == 0) {
			Vosao.info(messages('nothing_selected'));
			return;
		}
		if (confirm(messages('are_you_sure'))) {
			Vosao.jsonrpc.folderPermissionService.remove(function(r) {
				Vosao.showServiceMessages(r);
				callLoadPermissions();
			}, Vosao.javaList(ids));
		}
	}

	function onPermissionCancel() {
		$('#permission-dialog').dialog('close');
	}

	function loadFolderPermission() {
	    var r = folderRequest.folderPermission;
	   	if (r.changeGranted) {
	   		$('#saveButton').show();
	   		$('#createFileButton').show();
	   		$('#uploadButton').show();
	   		$('#deleteFilesButton').show();
	   		$('#addChildButton').show();
	   		$('#deleteFoldersButton').show();
	   	}
	   	else {
	   		$('#saveButton').hide();
	   		$('#createFileButton').hide();
	   		$('#uploadButton').hide();
	   		$('#deleteFilesButton').hide();
	   		$('#addChildButton').hide();
	   		$('#deleteFoldersButton').hide();
	   	}
	   	if (r.admin && editMode) {
	   		$('.securityTab').show();
	   	}
	   	else {
	   		$('.securityTab').hide();
	   	}
	}

	function onExportCancel() {
		$('#export-dialog').dialog('close');
		clearInterval(exportTimer);
		clearInterval(clockTimer);
	}

	function showBreadcrumbs() {
		var h = '';
		var parent = folderRequest.parent;
		$.each(folderRequest.ancestors.list, function(i,value) {
			var name = value.name == '/' ? 'file' : value.name;
			h += '<a href="#folder/' + value.id + '">' + name + '</a> / ';
		});
		if (editMode) {
			h += ' ' + ( folder.name == '/' ? 'file' : folder.name );
		}
		else {
			h += '<a href="#folder/' + parent.id + '">' + parent.name 
				+ '</a>';
		}
		$('#crumbs').html(h);
	}
	
	return Backbone.View.extend({
		
		el: $('#content'),
		
		tmpl: _.template(html),
		
		render: function() {
			this.el.html(this.tmpl({messages:messages}));
			postRender();
		},
		
		remove: function() {
		    $("#export-dialog").dialog('destroy').remove();
		    $("#file-upload").dialog('destroy').remove();
		    $("#permission-dialog").dialog('destroy').remove();
			this.el.html('');
		},
		
		setFolderId: function(id) {
			folderId = id;
		},
		
		setFolderParentId: function(id) {
			folderParentId = id;
		}
	});
	
});