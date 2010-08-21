/**
 * Vosao CMS. Simple CMS for Google App Engine.
 * 
 * Copyright (C) 2009-2010 Vosao development team.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * email: vosao.dev@gmail.com
 */

var plugin = null;
var properties = null;

$(function() {
    $("#tabs").tabs();
    Vosao.initJSONRpc(loadData);
    $('#cancelButton').click(onCancel);
    $('#saveButton').click(onSave);
});

function loadData() {
	loadPlugin();
	loadProperties();
}
    
function loadPlugin() {
	Vosao.jsonrpc.pluginService.getById(function(r) {
		plugin = r;
		$('#plugin-title').text(plugin.name + ' ' + messages('plugin.config'));
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
	location.href = 'config.vm';
}

function notNull(s) {
	return s == null ? '' : s;
}

function showPlugin() {
	if (plugin == null || properties == null) {
		return;
	}
	var h = '';
	var configData = getConfigData();
	$.each(properties, function(i,value) {
		var data = notNull(configData[value.name.toLowerCase()] ? 
				configData[value.name.toLowerCase()] : value.defaultValue);
		var title = Vosao.message(value.title);
		if (value.type == 'String' || value.type == 'Integer') {
			h += '<div class="form-row"><label>' + title + '</label>'
				+ '<input id="property-' + value.name + '" value="' 
				+ Vosao.escapeHtml(data) + '"/></div>';
		}
		if (value.type == 'Date') {
			h += '<div class="form-row"><label>' + title + '</label>'
				+ '<input id="property-' + value.name + '"/></div>';
		}
		if (value.type == 'Boolean') {
			h += '<div class="form-row"><label>' + title + '</label>'
				+ '<input type="checkbox" id="property-' + value.name 
				+ '"/></div>';
		}
		if (value.type == 'Text') {
			h += '<div class="form-row"><label>' + title + '</label>'
				+ '<textarea id="property-' + value.name + '" cols="80" rows="25">'
				+ data + '</textarea></div>';
		}
	});
	$('#properties').html(h);
	$.each(properties, function(i,value) {
		var id = "#property-" + value.name;
		var data = configData[value.name.toLowerCase()] ? 
				configData[value.name.toLowerCase()] : '';
		if (value.type == 'Date') {
		    if (data) {
		    	$(id).val(data);
		    }
		    $(id).datepicker({dateFormat:'dd.mm.yy'});
		}
		if (value.type == 'Boolean') {
		    var checked = data == 'true';
			$(id).each(function() {this.checked = checked});
		}
	});
}

/**
 * Extract config data from plugin configData XML.
 * @return map with param name as a key
 */
function getConfigData() {
	var result = {};
	if (plugin.configData != '') {
		var domData = $.xmlDOM(plugin.configData, function(error) {
			Vosao.error(messages('plugin.parsing_error') + ' ' + error);
		});
		$(domData).find('plugin-config').children().each(function() {
			result[this.tagName.toLowerCase()] = $(this).text();
		});
	}
	return result;
}

function validate(vo) {
	$.each(properties, function(i,value) {
		var data = vo[value.name];
		if (value.type == 'Integer' && parseInt(data) == NaN) {
			return messages('plugin.integer_expected') + ' ' + value.name;
		}
	});
}

function onSave() {
	var vo = {};
	var xml = '<plugin-config>\n';
	$.each(properties, function(i,value) {
		var id = '#property-' + value.name;
		if (value.type == 'String' || value.type == 'Integer'
			|| value.type == 'Date' || value.type == 'Text') {
			vo[value.name] = $(id).val();
			xml += '<' + value.name + '>' + Vosao.escapeHtml(vo[value.name]) 
				+ '</' + value.name + '>\n';
		}
		if (value.type == 'Boolean') {
			vo[value.name] = String($(id + ':checked').size() > 0);
			xml += '<' + value.name + '>' + vo[value.name] + '</' + value.name 
				+ '>\n';
		}
	});
	xml += '</plugin-config>\n'
	var error = validate(vo);
	if (error) {
		Vosao.error(error);
	}
	else {
		Vosao.jsonrpc.pluginService.savePluginConfig(function(r) {
			Vosao.showServiceMessages(r);
		}, Number(pluginId), xml);
	}	
}