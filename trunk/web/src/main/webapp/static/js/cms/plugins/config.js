/**
 * Vosao CMS. Simple CMS for Google App Engine. Copyright (C) 2009 Vosao
 * development team
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 * 
 * email: vosao.dev@gmail.com
 */

var plugins = '';

$(function() {
    $("#tabs").tabs();
    $("#import-dialog").dialog({ width: 400, autoOpen: false });
    Vosao.initJSONRpc(loadData);
    $('#upload').ajaxForm(afterUpload);
    $('#installButton').click(onInstall);
    $('#importCancelButton').click(onImportCancel);
    $('#okButton').click(onAfterUploadOk);
});

function loadData() {
	loadPlugins();
}
    
function loadPlugins() {
	Vosao.jsonrpc.pluginService.select(function (r) {
		plugins = r.list;
		showPlugins();
	});
}

function showPlugins() {
    var html = '<table class="form-table"><th>Title</th>\
       <th>Name</th><th>Description</th><th>Website</th><th></th></tr>';
    $.each(plugins, function(i, plugin) {
        var configURL = '/cms/plugins/plugin.jsp?id=' + plugin.id;
    	if (plugin.configURL) {
        	configURL = '/file/plugins/' + plugin.name + '/' + plugin.configURL;
        }
    	html += '<tr><td><a href="' + configURL + '">' 
    		+ plugin.title + '</a></td><td>' + plugin.name 
            + '</td><td>' + plugin.description + '</td>'
            + '<td>' + plugin.website + '</td>'
            + '<td><a href="#" onclick="onRemove(' + i + ')">\
            <img src="/static/images/02_x.png"/></a></td></tr>';
    });
    $('#plugins').html(html + '</table>');
    $('#plugins tr:even').addClass('even');
}

function onInstall() {
    $("#import-dialog").dialog("open");
}

function onImportCancel() {
    $("#import-dialog").dialog("close");
}

function afterUpload(data) {
    var s = data.split('::');
    var result = s[1];
    var msg = s[2]; 
    if (result == 'success') {
        msg = 'Success. Plugin was successfully installed.';
    }
    else {
        msg = "Error. " + msg;
    }   
    $("#import-dialog").dialog("close");
    $("#afterUpload-dialog .message").text(msg);
    $("#afterUpload-dialog").dialog();
}

function onAfterUploadOk() {
    window.location.reload();
}

function onRemove(i) {
	if (confirm('Are you shure?')) {
		Vosao.jsonrpc.pluginService.remove(function(r) {
			Vosao.showServiceMessages(r);
			if (r.result == 'success') {
				plugins.splice(i, 1);
				showPlugins();
			}
		}, plugins[i].id);
	}
}