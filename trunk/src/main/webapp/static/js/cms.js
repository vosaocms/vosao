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

//******************************************************************************
// This is CMS related JS code.
//******************************************************************************


// ****************************** Constants ************************************

/**
 * Autosave timeout in seconds.
 */
var AUTOSAVE_TIMEOUT = 60;

var ENGLISH_CODE = 'en';

//************************** Utility functions *********************************

function info(msg) {
	infoMessage('#wrapper .messages', msg);
    $('#wrapper .messages').fadeIn();
    setTimeout(function() {
    	$('#wrapper .messages').fadeOut();
    }, 5000);
}

function error(msg) {
    errorMessage('#wrapper .messages', msg);	
    $('#wrapper .messages').fadeIn();
    setTimeout(function() {
    	$('#wrapper .messages').fadeOut();
    }, 5000);
}

function infoMessage(widget, msg) {
	$(widget).html('<div class="ui-widget">\
		<div class="ui-state-highlight ui-corner-all" style="padding: 0.5em 0.7em;margin: 4px;"><p>\
		<span class="ui-icon ui-icon-info" style="float:left;margin-right:0.3em" />\
		<strong>Hey!</strong> ' + msg + '</p></div></div>');
}

function errorMessage(widget, msg) {
	$(widget).html('<div class="ui-widget">\
		<div class="ui-state-error ui-corner-all" style="padding: 0.5em 0.7em;margin: 4px;"><p>\
		<span class="ui-icon ui-icon-alert" style="float:left;margin-right:0.3em" />\
		<strong>Alert:</strong> ' + msg + '</p></div></div>');
}

function errorMessages(widget, errors) {
    var msg = '';
    $.each(errors, function (i, m) {
        msg += (i == 0 ? '' : '<br />') + m;
    });
    errorMessage(widget, msg);
}

function showServiceMessages(r) {
	if (r.result == 'success') {
		info(r.message);
		if (r.messages.list.length > 0) {
			$.each(r.messages.list, function(n,value) { info(value) });
		}
	}
	else {
		error(r.message);
		if (r.messages.list.length > 0) {
			$.each(r.messages.list, function(n,value) { error(value) });
		}
	}
}

/**
 * Create map from list of object with id field as key.
 * @param list - list of objects.
 * @return - map of objects.
 */
function idMap(list) {
	var map = {};
	$.each(list, function (i, value) {
		map[value.id] = value;
	});
	return map;
}
