// file StructuresView.js
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

define(['text!template/structures.html'], function(tmpl) {
	
	console.log('Loading StructuresView.js');
	
	function loadStructures() {
		Vosao.jsonrpc.structureService.select(function (r) {
	        var html = '<table class="form-table"><tr><th></th><th>'  
	        	+ messages('title') + '</th></tr>';
	        $.each(r.list, function (n, value) {
	            html += '<tr><td><input type="checkbox" value="' + value.id 
	                + '" /></td><td><a href="#structure/' + value.id
	                +'">' + value.title + '</a></td></tr>';
	        });
	        $('#structures').html(html + '</table>');
	        $('#structures tr:even').addClass('even');
	    });
	}

	function onAdd() {
		location.href = '#structure';
	}

	function onDelete() {
	    var ids = new Array();
	    $('#structures input:checked').each(function () {
	        ids.push(this.value);
	    });
	    if (ids.length == 0) {
	    	Vosao.info(messages('nothing_selected'));
	        return;
	    }
	    if (confirm(messages('are_you_sure'))) {
	    	Vosao.jsonrpc.structureService.remove(function(r) {
	    		Vosao.showServiceMessages(r);
	            loadStructures();
	        }, Vosao.javaList(ids));
	    }
	}

	
	return Backbone.View.extend({
		
		el: $('#content'),
		
		render: function() {
			this.el.html(_.template(tmpl, {messages:messages}));
			loadStructures();
		    $('#addButton').click(onAdd);
		    $('#deleteButton').click(onDelete);
		},
		
		remove: function() {
			this.el.html('');
		}
		
	});
	
});