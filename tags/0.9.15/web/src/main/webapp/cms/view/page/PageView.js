// file PageView.js
/**
 * Vosao CMS. Simple CMS for Google App Engine.
 * 
 * Copyright (C) 2009-2011 Vosao development team.
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

define(['text!template/page/page.html', 'view/page/PageRouter', 'view/page/context'], 
function(pageHtml, PageRouter, ctx) {
	
	console.log('Loading PageView.js');
	
	return Backbone.View.extend({
		
		css: ['/static/js/codemirror/codemirror.css',
		      '/static/js/codemirror/eclipse.css',
		      '/static/css/page.css'],
		
		el: $('#content'),

		tmpl: _.template(pageHtml),

		viewCmd: null,
		
		router: null,
		
		initialize: function() {
			this.router = new PageRouter({view:this});
		},
		
		render: function() {
			Vosao.addCSSFiles(this.css);
			this.el.html(this.tmpl({
				messages : messages, 
				id : ctx.pageId
			}));
		    $("#version-dialog").dialog({ width: 400, autoOpen: false });
			this.router.showCmd(this.viewCmd);
		},
		
		remove: function() {
			if (this.router.currentView) {
				this.router.currentView.remove();
			}
			$('#version-dialog').dialog('destroy').remove();
			this.el.html('');
			Vosao.removeCSSFiles(this.css);
		},
		
		editContent: function(id) {
			ctx.pageId = id;
			this.viewCmd = 'editContent';
		},

		editPage: function(id) {
			ctx.pageId = id;
			this.viewCmd = 'editPage';
		},
		
		setResource: function(path) {
			this.router.contentView.setResource(path);
		}
		
	});
	
});