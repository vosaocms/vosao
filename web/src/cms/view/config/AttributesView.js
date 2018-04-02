// file AttributesView.js
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

define(['text!template/config/attributes.html'],
function(tmpl) {
	
	console.log('Loading AttributesView.js');
	
	var config = '';

	function postRender() {
	    $('ul.ui-tabs-nav li:nth-child(8)').addClass('ui-state-active')
	    	.addClass('ui-tabs-selected')
	    	.removeClass('ui-state-default');
	    $("#attribute-dialog").dialog({ width: 400, autoOpen: false });
	    Vosao.initJSONRpc(loadData);
	    $('#addButton').click(onAdd);
	    $('#cancelButton').click(function() {
	    	$("#attribute-dialog").dialog('close');
	    });
	    $('#attributeForm').submit(function() {onSave(); return false});
	    $('#removeButton').click(onRemove);
	}

	function loadData() {
	    loadConfig();
	}

	function loadConfig() {
		Vosao.jsonrpc.configService.getConfig(function (r) {
	        config = r;
	        showAttributes();
	    }); 
	}

	function showAttributes() {
	    var h = '<table class="form-table"><tr><th></th><th>' 
	    	+ messages('name') + '</th><th>' + messages('value') + '</th></tr>';
	    $.each(config.attributes.map, function (name, value) {
	        h += '<tr><td><input type="checkbox" value="' + name + '"/></td>'
	            + '<td><a data-name="' + name + '">' + name + '</a></td>'
	            + '<td>' + value + '</td></tr>';
	    });
	    $('#attributes').html(h + '</table>');
	    $('#attributes tr:even').addClass('even');
	    $('#attributes a').click(function() {
	    	onEdit($(this).attr('data-name'));
	    })
	}

	function onAdd() {
		$("#attribute-dialog").dialog('open');
		$('#name').val('');
		$('#value').val('');
	}

	function onEdit(name) {
		$("#attribute-dialog").dialog('open');
		$('#name').val(name);
		$('#value').val(config.attributes.map[name]);
	}

	function onSave() {
	    var name = $('#name').val();
	    var value = $('#value').val();       
	    Vosao.jsonrpc.configService.saveAttribute(function(r) {
	    	Vosao.showServiceMessages(r);
	    	$("#attribute-dialog").dialog('close');
	    	loadData();
	    }, name, value); 
	}

	function onRemove() {
	    var ids = [];
	    $('#attributes input:checked').each(function () {
	    	ids.push(this.value);
	    });
	    if (ids.length == 0) {
	    	Vosao.info(messages('nothing_selected'));
	        return;
	    }
	    if (confirm(messages('are_you_sure'))) {
	    	Vosao.jsonrpc.configService.removeAttributes(function (r) {
	    		Vosao.info(r.message);
	            loadData();
	        }, Vosao.javaList(ids));
	    }
	}
	
	return Backbone.View.extend({
		
		el: $('#tab-1'),
		
		render: function() {
			this.el = $('#tab-1');
			this.el.html(_.template(tmpl, {messages:messages}));
			postRender();
		},
		
		remove: function() {
		    $("#attribute-dialog").dialog('destroy').remove();
			this.el.html('');
		}
		
	});
	
});