// file CommentsView.js
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

define(['text!template/config/comments.html',
        'order!cm', 'order!cm-css', 'order!cm-js', 'order!cm-xml', 'order!cm-html'],
function(tmpl) {
	
	console.log('Loading CommentsView.js');
	
	var config = '',
		editor = null;
	
	function postRender() {
	    Vosao.initJSONRpc(loadData);
	    $('#commentsForm').submit(function() {onSave(); return false;});
	    $('#restoreButton').click(onRestore);
	    $('ul.ui-tabs-nav li:nth-child(2)').addClass('ui-state-active')
	    		.addClass('ui-tabs-selected')
				.removeClass('ui-state-default');
	}

	function loadData() {
	    loadConfig();
	}

	function loadConfig() {
		Vosao.jsonrpc.configService.getConfig(function (r) {
	        config = r;
	        initFormFields();
	    }); 
	}

	function initFormFields() {
	    $('#commentsEmail').val(config.commentsEmail);
	    $('#commentsTemplate').val(config.commentsTemplate);
		editor = CodeMirror.fromTextArea(document.getElementById('commentsTemplate'), {
			lineNumbers: true,
			theme: 'eclipse',
			mode: 'htmlmixed'
		});
		editor.focus();
		$(editor.getScrollerElement())
			.css('height', (0.6 * $(window).height()) + 'px')
			.css('border', '1px solid silver');
		editor.refresh();
	    
	}

	function onSave() {
		editor.save();
	    var vo = Vosao.javaMap({
	        commentsEmail : $('#commentsEmail').val(),
	        commentsTemplate : $('#commentsTemplate').val()       
	    });
	    Vosao.jsonrpc.configService.saveConfig(function(r) {
	    	Vosao.showServiceMessages(r);
	    }, vo); 
	}

	function onRestore() {
		Vosao.jsonrpc.configService.restoreCommentsTemplate(function (r) {
			Vosao.showServiceMessages(r);
	        loadConfig();
	    });
	}

	return Backbone.View.extend({
		
		css: ['/static/js/codemirror/codemirror.css',
		      '/static/js/codemirror/eclipse.css'],

		el: $('#tab-1'),
		
		render: function() {
			Vosao.addCSSFiles(this.css);
			this.el = $('#tab-1');
			this.el.html(_.template(tmpl, {messages:messages}));
			postRender();
		},
		
		remove: function() {
			this.el.html('');
			Vosao.removeCSSFiles(this.css);
		}
		
	});
	
});