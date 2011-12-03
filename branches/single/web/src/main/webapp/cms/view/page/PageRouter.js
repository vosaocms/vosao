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

define(['view/page/ContentView'], function(ContentView) {
    
    console.log('Loading PageRouter.js');
    
    return Backbone.Router.extend({
        
        pageView: null,
        
        currentView: null,
        contentView: new ContentView(),

        initialize: function(options) {
            this.pageView = options.view;
        },
        
        show: function(view) {
            if (this.currentView) {
                this.currentView.remove();
                this.currentView = null;
            }
            view.render();
            this.currentView = view;
        },
        
        routes: {
            
        },

        showCmd: function(cmd) {
            if (cmd == 'editContent') this.editContent(this.pageView.pageId);
            if (cmd == 'editPage') this.editPage(this.pageView.pageId);
        },
        
        editContent: function(id) {
            this.contentView.edit(id);
            this.show(this.contentView);
        },
        
        editPage: function(id) {
            alert('TODO');
        }
        
    });
    
});