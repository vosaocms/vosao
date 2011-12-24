// file PageRouter.js
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

define(['view/page/context',
        'view/page/ContentView', 'view/page/IndexView', 'view/page/ChildrenView',
        'view/page/CommentsView', 'view/page/SecurityView', 
        'view/page/AttributesView'], 
function(ctx, ContentView, IndexView, ChildrenView, CommentsView, SecurityView, 
		 AttributesView) {
    
    console.log('Loading PageRouter.js');
    
    return Backbone.Router.extend({
        
        pageView: null,
        
        currentView: null,
        contentView: new ContentView(),
        indexView: new IndexView(),
        childrenView: new ChildrenView(),
        commentsView: new CommentsView(),
        securityView: new SecurityView(),
        attributesView: new AttributesView(),

        initialize: function(options) {
            this.pageView = options.view;
        },

        show: function(view) {
        	if (Vosao.app.currentView != this.pageView) {
        		Vosao.app.show(this.pageView);
        	}
            if (this.currentView) {
                this.currentView.remove();
                this.currentView = null;
            }
    	    $('ul.ui-tabs-nav li').removeClass('ui-state-active')
    			.removeClass('ui-tabs-selected')
    			.addClass('ui-state-default');
    	    
            view.render();
            this.currentView = view;
        },
        
        routes: {
            'page/children/:id':	'children',
            'page/comments/:id':	'comments',
            'page/security/:id':	'security',
            'page/attributes/:id':	'attributes',
            'page/resources/:id':	'resources'
        },

        showCmd: function(cmd) {
            if (cmd == 'editContent') this.editContent(ctx.pageId);
            if (cmd == 'editPage') this.editPage(ctx.pageId);
        },
        
        editContent: function(id) {
        	ctx.pageId = id;
            this.show(this.contentView);
        },
        
        editPage: function(id) {
        	ctx.pageId = id;
            this.show(this.indexView);
        },
            
        children: function(id) {
        	ctx.pageId = id;
            this.show(this.childrenView);
        },
        
        comments: function(id) {
        	ctx.pageId = id;
            this.show(this.commentsView);
        },

        security: function(id) {
        	ctx.pageId = id;
            this.show(this.securityView);
        },

        attributes: function(id) {
        	ctx.pageId = id;
            this.show(this.attributesView);
        },
        
        resources: function(id) {
    		Vosao.jsonrpc.pageService.getPageRequest(function(r) {
    			ctx.pageRequest = r;
    			ctx.pageId = id;
    			ctx.page = ctx.pageRequest.page;
    			
            	Vosao.jsonrpc.folderService.createFolderByPath(function(r) {
            		jQuery.cookie('folderReturnPath', '#page/content/' + ctx.pageId, 
            		    {path:'/', expires: 10});
            		location.href = '#folder/' + r.id;
            	}, '/page' + ctx.page.friendlyURL);

    		}, id, '');
        }
    });
    
});