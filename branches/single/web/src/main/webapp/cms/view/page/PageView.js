// file PageView.js
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

define(['text!template/page/page.html', 'view/page/PageRouter', 'order!ckeditor'], 
function(pageHtml, PageRouter) {
	
	console.log('Loading PageView.js');
	
	return Backbone.View.extend({
		
		css: '/static/css/page.css',
		
		el: $('#content'),

		tmpl: _.template(pageHtml),

		pageId: null,
		viewCmd: null,
		
		router: null,
		
		initialize: function() {
			this.router = new PageRouter({view:this});
		},
		
		render: function() {
			Vosao.addCSSFile(this.css);
			this.el.html(this.tmpl({
				messages:messages, 
				id:this.pageId
			}));
			this.router.showCmd(this.viewCmd);
		},
		
		remove: function() {
			this.el.html('');
			Vosao.removeCSSFile(this.css);
		},
		
		editContent: function(id) {
			this.pageId = id;
			this.viewCmd = 'editContent';
		},

		editPage: function(id) {
			this.pageId = id;
			this.viewCmd = 'editPage';
		}
		
	});
	
});