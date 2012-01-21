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


if ($.browser.msie) {
	$('.splash h3').html('We are sorry but Internet Explorer is not supported.');
	$('.splash img').attr('src', '/static/images/no_ie.jpg');
	throw new Error('IE is not supported.');
}

// Configure shortcut aliases
require.config({
	paths : {
		text : 'libs/text',
		order : 'libs/order',
		"jquery.cookie" : '/static/js/jquery.cookie',
		"jquery-ui" : '/static/js/jquery-ui',
		"jquery.xmldom" : '/static/js/jquery.xmldom',
		"jquery.form" : '/static/js/jquery.form',
		"jquery.treeview" : '/static/js/jquery.treeview',
		"jquery.jquote2" : 'libs/jquery.jquote2.min',
		jsonrpc : '/static/js/jsonrpc',
		vosao : '/static/js/vosao',
		cms : '/static/js/cms',
		i18n : '/i18n',
		"back-services" : '/static/js/back-services',
		
	    'cm'				: "/static/js/codemirror/codemirror", 
	    'cm-css'			: "/static/js/codemirror/css", 
	    'cm-html'			: "/static/js/codemirror/htmlmixed",
	    'cm-js'				: "/static/js/codemirror/javascript",
	    'cm-xml'			: "/static/js/codemirror/xml"
		
	}
});

require([

	// Load our app module and pass it to our definition function
	'app', 
	'jquery.cookie', 'jquery-ui', 'jquery.xmldom', 'jquery.jquote2', 
	'order!i18n', 'order!jsonrpc', 'order!vosao', 'order!cms', 'order!back-services'

], function(App) {
	
	Vosao.initJSONRpc(function() {
		Vosao.jsonrpc.loginFrontService.getSystemProperties(function(p) {
			Vosao.version = p.map.version;
			Vosao.fullVersion = p.map.fullVersion;
			Vosao.loggedIn = p.map.loggedIn === 'true';
			Vosao.app = new App();
		});
	});
	
});