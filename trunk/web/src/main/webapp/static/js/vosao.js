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
// This is for frontend and backend use.
//******************************************************************************

function javaList(array) {
	return {javaClass: 'java.util.ArrayList', list: array};
}

function javaMap(aMap) {
	return {javaClass: 'java.util.HashMap', map: aMap};
}

function urlFromTitle(title) {
    return title.toLowerCase().replace(/\W/g, '-');
}

function isImage(filename) {
    var ext = getFileExt(filename);
	return ext.toLowerCase().match(/gif|jpg|jpeg|png|ico/) != null;
}

function getFileExt(filename) {
	return filename.substring(filename.lastIndexOf('.') + 1, filename.length);
}

function formatDate(date) {
	return $.datepicker.formatDate('dd.mm.yy', date);
}

var identifier_regex = /^[a-zA-Z_][a-zA-Z0-9_]*$/;

function isValidIdentifier(s) {
	return identifier_regex.test(s);
}

function strip(s) {
	var i = 0;
	while (i < s.length && s[i] == ' ') i++;
	var s1 = s.substring(i);
	i = s1.length - 1;
	while (i >= 0 && s1[i] == ' ') i--;
	return s1.slice(0, i + 1);
}

/**
 * Global JSON-RPC entry point.
 */

var jsonrpc = '';
var jsonrpcListeners = [];
var jsonrpcSystemListeners = [];
var jsonrpcInitialized = false;

/**
 * Don't call this function directly.
 */
function createJSONRpc() {
	jsonrpc = new JSONRpcClient(function(result, e) {
		if (e) {
			alert("Error during initializing JSON-RPC." + e);
		}
		else {
			while (jsonrpcSystemListeners.length > 0) {
				var func = jsonrpcSystemListeners.pop();
				func();
			}
			while (jsonrpcListeners.length > 0) {
				var func = jsonrpcListeners.pop();
				func();
			}
		}
		jsonrpcInitialized = true;
	}, '/JSON-RPC/');
}

createJSONRpc();

/**
 * Add application JSON-RPC initialization callback.
 * @param func - callback to run after successful initialization.
 */
function initJSONRpc(func) {
	if (func == undefined) {
		return;
	}
	if (jsonrpcInitialized) {
        func();
    } else {
    	jsonrpcListeners.push(func);
    }
}

/**
 * Add system (high priority) JSON-RPC initialization callback.
 * @param func - callback to run after successful initialization.
 */
function initJSONRpcSystem(func) {
	if (func == undefined) {
		return;
	}
	if (jsonrpcInitialized) {
        func();
    } else {
    	jsonrpcSystemListeners.push(func);
    }
}

function serviceFailed(e) {
	if (e != null) {
		alert('Can\'t connect to server. ' + e + ' '+ e.message);
		return true;
	}
	return false;
}
