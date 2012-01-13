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

//******************************************************************************
// Global Vosao namespace.
// For frontend and backend use.
//******************************************************************************

var Vosao = {};

Vosao.javaList = function(array) {
	return {javaClass: 'java.util.ArrayList', list: array};
};

Vosao.javaMap = function(aMap) {
	return {javaClass: 'java.util.HashMap', map: aMap};
};

Vosao.accents = { 'ÀÁÂÃÄÅ':'A',	'Æ':'AE', 'Ç':'C','ÈÉÊË':'E', 'ÌÍÎÏ':'I',
	'Ð':'Th', 'Ñ':'N', 'ÒÓÔÕÖØ':'O', 'ÙÚÛÜ':'U', 'Ý':'Y', 'Þ':'Th',
	'ß':'ss', 'àáâãäå':'a', 'æ':'ae', 'ç':'c', 'èéêë':'e', 'ìíîï':'i',
	'ð':'o', 'ñ':'n', 'òóôõöø':'o', 'ùúûü':'u', 'ýÿ':'y', 'þ':'th'};

Vosao.replaceAccents = function(s) {
	$.each(Vosao.accents, function(n, value) {
		for (var i=0; i < n.length; i++) {
			s = s.replace(n.charAt(i), value);
		}
	});
	return s.toLowerCase();
};

Vosao.urlFromTitle = function(title) {
    return Vosao.replaceAccents(title.toLowerCase()).replace(/\W/g, '-');
};

Vosao.nameFromTitle = function(title) {
    return title.toLowerCase().replace(/\W/g, '_');
};

Vosao.isImage = function(filename) {
    var ext = Vosao.getFileExt(filename);
	return ext.toLowerCase().match(/gif|jpg|jpeg|png|ico/) != null;
};

Vosao.getFileExt = function(filename) {
	return filename.substring(filename.lastIndexOf('.') + 1, filename.length);
};

Vosao.getFileName = function(path) {
	var slash = path.lastIndexOf('\\');
	if (slash == -1) {
		slash = path.lastIndexOf('/');
	}	
	return slash == -1 ? path : path.substring(slash + 1, path.length);
};

Vosao.formatDate = function(date) {
	return $.datepicker.formatDate('dd.mm.yy', date);
};

Vosao.formatTime = function(date) {
	return $.datepicker.formatDate('HH:MM', date);
};

Vosao.identifier_regex = /^[a-zA-Z_][a-zA-Z0-9_]*$/;

Vosao.isValidIdentifier = function(s) {
	return Vosao.identifier_regex.test(s);
};

Vosao.strip = function(s) {
	var i = 0;
	while (i < s.length && s[i] == ' ') i++;
	var s1 = s.substring(i);
	i = s1.length - 1;
	while (i >= 0 && s1[i] == ' ') i--;
	return s1.slice(0, i + 1);
};

Vosao.getQueryParam = function(param) {
    var result =  window.location.search.match(
        new RegExp("(\\?|&)" + param + "(\\[\\])?=([^&]*)")
    );
    return Vosao.escapeHtml(result ? result[3] : '');
};

Vosao.selectTabFromQueryParam = function(tab) {
	if (Vosao.getQueryParam('tab')) {
		tab.tabs('select', Number(Vosao.getQueryParam('tab')));
	}
};

Vosao.escapeHtml = function(s) {
	return s.replace(/&/g,'&amp;')                                         
		.replace(/>/g,'&gt;')                                           
		.replace(/</g,'&lt;')                                           
		.replace(/"/g,'&quot;')
		.replace(/`/g,'&lsquo;')
		.replace(/'/g,'&rsquo;');
}

Vosao.unescapeHtml = function(s) {
	return s.replace(/&amp;/g,'&')                                         
		.replace(/&gt;/g,'>')                                           
		.replace(/&lt;/g,'<')                                           
		.replace(/&quot;/g,'"')
		.replace(/&lsquo;/g,'`')
		.replace(/&rsquo;/g,"'");
}

Vosao.generateGUID = function() {
	function S4() {
		return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
	}
	return (S4()+S4()+"-"+S4()+"-"+S4()+"-"+S4()+"-"+S4()+S4()+S4());
}

/**
 * Global JSON-RPC entry point.
 */

Vosao.jsonrpc = '';
Vosao.jsonrpcListeners = [];
Vosao.jsonrpcSystemListeners = [];
Vosao.jsonrpcInitialized = false;
Vosao.jsonrpcInitError = null;

/**
 * Don't call this function directly.
 */
Vosao.createJSONRpc = function() {
	Vosao.jsonrpc = new JSONRpcClient(function(result, e) {
		if (e) {
			Vosao.jsonrpcInitError = "Error during JSON-RPC initialization " + e 
				+ ' ' + e.message;
		}
		else {
			while (Vosao.jsonrpcSystemListeners.length > 0) {
				var func = Vosao.jsonrpcSystemListeners.pop();
				func();
			}
			while (Vosao.jsonrpcListeners.length > 0) {
				var func = Vosao.jsonrpcListeners.pop();
				func();
			}
		}
		Vosao.jsonrpcInitialized = true;
	}, '/json-rpc/');
};

Vosao.createJSONRpc();

/**
 * Add application JSON-RPC initialization callback.
 * @param func - callback to run after successful initialization.
 */
Vosao.initJSONRpc = function(func) {
	if (func == undefined) {
		return;
	}
	if (Vosao.jsonrpcInitialized) {
        func();
    } else {
    	Vosao.jsonrpcListeners.push(func);
    }
};

/**
 * Add system (high priority) JSON-RPC initialization callback.
 * @param func - callback to run after successful initialization.
 */
Vosao.initJSONRpcSystem = function(func) {
	if (func == undefined) {
		return;
	}
	if (Vosao.jsonrpcInitialized) {
        func();
    } else {
    	Vosao.jsonrpcSystemListeners.push(func);
    }
};

Vosao.serviceFailed = function(e) {
	if (e != null) {
		alert("JSON-RPC service fail " + e + ' ' + e.message);
		return true;
	}
	return false;
};

Vosao.changeLanguage = function(lang) {
    var url = location.href.replace('#', '');
    var langIndex = url.indexOf('language=');
    var sign = location.search.indexOf('?') == -1 ? '?' : '&';
    if (langIndex > 0) {
        var langAfter = url.substr(langIndex+1);
        var langAndIndex = langAfter.indexOf('&');
        url = url.substr(0, langIndex) + 'language=' + lang;
        if(langAndIndex > 0) {
            url = url + langAfter.substr(langAndIndex+1);
        }
        if(url.indexOf('&') < 0) {
            sign = '';
        }
    } else {
        url = url + sign + 'language=' + lang;
    }
    location.href = url;
};

// String enhancements 

String.prototype.trim = function() {
	return this.replace(/^\s+|\s+$/g,"");
}
String.prototype.ltrim = function() {
	return this.replace(/^\s+/,"");
}
String.prototype.rtrim = function() {
	return this.replace(/\s+$/,"");
}
String.prototype.startsWith = function(str) {
	return (this.match("^"+str)==str)
}
String.prototype.endsWith = function(str) { 
	return (this.match(str+"$")==str)
}
