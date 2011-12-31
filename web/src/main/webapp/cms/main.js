// Filename: main.js
console.log("main.js");

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