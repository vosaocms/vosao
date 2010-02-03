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

var plugin = null;
var properties = null;

$(function() {
    $("#tabs").tabs();
    Vosao.initJSONRpc(loadData);
    $('#cancelButton').click(onCancel);
});

function loadData() {
	loadPlugin();
	loadProperties();
}
    
function loadPlugin() {
	Vosao.jsonrpc.pluginService.getById(function(r) {
		plugin = r;
		$('#plugin-title').text('Plugin ' + plugin.name + ' config');
		showPlugin();
	}, pluginId);
}

function loadProperties() {
	Vosao.jsonrpc.pluginService.getProperties(function(r) {
		properties = r.list;
		showPlugin();
	}, pluginId);
}

function onCancel() {
	location.href = 'config.jsp';
}

function showPlugin() {
	if (plugin == null || properties == null) {
		return;
	}
	var h = '';
	var configData = getConfigData();
	$.each(properties, function(i,value) {
		var data = configData[value.name] ? configData[value.name] : '';
		if (value.type == 'String' || value.type == 'Integer'
			|| value.type == 'Date') {
			h += '<div class="form-row"><label>' + value.title + '</label>'
				+ '<input id="property-' + value.name + '" value="' + data + '"/></div>';
		}
		if (value.type == 'Boolean') {
			h += '<div class="form-row"><label>' + value.title + '</label>'
				+ '<input type="checkbox" id="property-' + value.name 
				+ '"/></div>';
		}
		if (value.type == 'Text') {
			h += '<div class="form-row"><label>' + value.title + '</label>'
				+ '<textarea id="property-' + value.name + '" cols="80" rows="25">'
				+ data + '</textarea></div>';
		}
	});
	$('#properties').html(h);
	$.each(properties, function(i,value) {
		var data = configData[value.name] ? configData[value.name] : '';
		if (value.type == 'Date') {
		    $("#property-" + value.name).datepicker({dateFormat:'dd.mm.yy'});
		}
		if (value.type == 'Boolean') {
		    var checked = data == 'true';
			$("#property-" + value.name).each(function() {this.checked = checked});
		}
	});
}

/**
 * Extract config data from plugin configData XML.
 * @return map with param name as a key
 */
function getConfigData() {
	var result = {};
	$(plugin.configData).each(function() {
		result[this.tagName] = $(this).text();  
	});
	return result;
}