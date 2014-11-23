// file ConfigRouter.js
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

define(['view/config/IndexView', 'view/config/CommentsView',
        'view/config/LanguagesView', 'view/config/MessagesView',
        'view/config/UsersView', 'view/config/GroupsView',
        'view/config/TagsView', 'view/config/AttributesView'], 
function(IndexView, CommentsView, LanguagesView, MessagesView, UsersView,
		 GroupsView, TagsView, AttributesView) {
    
    console.log('Loading ConfigRouter.js');
    
    return Backbone.Router.extend({
        
        configView: null,
        
        currentView: null,
        indexView: new IndexView(),
        commentsView: new CommentsView(),
        languagesView: new LanguagesView(),
        messagesView: new MessagesView(),
        usersView: new UsersView(),
        groupsView: new GroupsView(),
        tagsView: new TagsView(),
        attributesView: new AttributesView(),

        initialize: function(options) {
            this.configView = options.view;
        },

        show: function(view) {
        	if (Vosao.app.currentView != this.configView) {
        		Vosao.app.show(this.configView);
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
        	'config/comments':	'comments',
        	'config/languages':	'languages',
        	'config/messages':	'messages',
        	'config/users':		'users',
        	'config/groups':	'groups',
        	'config/tags':		'tags',
        	'config/attributes':'attributes'
        },

        index: function() {
            this.show(this.indexView);
        },
        
        comments: function() {
        	this.show(this.commentsView);
        },
        
        languages: function() {
        	this.show(this.languagesView);
        },
        
        messages: function() {
        	this.show(this.messagesView);
        },
        
        users: function() {
        	this.show(this.usersView);
        },

        groups: function() {
        	this.show(this.groupsView);
        },
        
        tags: function() {
        	this.show(this.tagsView);
        },
        
        attributes: function() {
        	this.show(this.attributesView);
        }
    });
    
});