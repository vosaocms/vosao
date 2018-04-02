// file FormsView.js
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

define(['text!template/plugins/forms.html',
        'order!cm-css', 'order!cm-js', 'order!cm-xml', 'order!cm-html'], 
function(tmpl) {
	
	console.log('Loading FormsView.js');
	
	var formConfig = '',
		templateEditor = null,
		letterEditor = null;

	function postRender() {
	    $("#tabs").tabs({show: tabSelected});
	    Vosao.initJSONRpc(loadData);
	    $('#addButton').click(onAdd);
	    $('#deleteButton').click(onDelete);
	    $('#restoreFormTemplateLink').click(onFormTemplateRestore);
	    $('#restoreFormLetterLink').click(onFormLetterRestore);
	    $('#saveButton').click(onSaveConfig);
	}

	function tabSelected(event, ui) {
		if (ui.index == 1) {
			templateEditor.refresh();
			letterEditor.refresh();
		}
	}

	function loadData() {
	    loadForms();
	    loadFormConfig();
	}
	    
	function loadForms() {
		Vosao.jsonrpc.formService.select(function (r) {
	        var html = '<table class="form-table"><tr><th></th><th>' + messages('title')
	        	+ '</th><th>' + messages('name') + '</th><th>' + messages('email') 
	        	+ '</th></tr>';
	        $.each(r.list, function(i, form) {
	            html += '<tr><td><input type="checkbox" value="' + form.id
	                + '"/></td><td><a href="#plugins/form/' + form.id 
	                + '">' + form.title + '</a></td><td>' + form.name 
	                + '</td><td>' + form.email + '</td></tr>';
	        });
	        $('#forms').html(html + '</table>');
	        $('#forms tr:even').addClass('even');
	    });
	}

	function onAdd() {
	    location.href = '#plugins/form';
	}
	    
	function onDelete() {
	    var ids = [];
	    $('#forms input:checked').each(function() {
	        ids.push(this.value);
	    });
	    if (ids.length == 0) {
	    	Vosao.info(messages('nothing_selected'));
	        return;
	    }
	    if (confirm(messages('are_you_sure'))) {
	    	Vosao.jsonrpc.formService.deleteForm(function (r) {
	    		Vosao.showServiceMessages(r);
	            loadForms();
	        }, Vosao.javaList(ids));
	    }
	}

	function loadFormConfig() {
		Vosao.jsonrpc.formService.getFormConfig(function (r) {
	        formConfig = r;
	        $('#formTemplate').val(r.formTemplate);
	        $('#letterTemplate').val(r.letterTemplate);
			templateEditor = CodeMirror.fromTextArea(document.getElementById('formTemplate'), {
				lineNumbers: true,
				theme: 'eclipse',
				mode: "htmlmixed"
			});
			$(templateEditor.getScrollerElement()).css('border', '1px solid silver');
			letterEditor = CodeMirror.fromTextArea(document.getElementById('letterTemplate'), {
				lineNumbers: true,
				theme: 'eclipse',
				mode: "htmlmixed"
			});
			$(letterEditor.getScrollerElement()).css('border', '1px solid silver');
	    });
	}
	    
	function onSaveConfig() {
		templateEditor.save();
		letterEditor.save();
	    var vo = Vosao.javaMap({
	   	    formTemplate : $('#formTemplate').val(),
	   	    letterTemplate : $('#letterTemplate').val()
	    });
	    Vosao.jsonrpc.formService.saveFormConfig(function (r) {
	    	Vosao.showServiceMessages(r);
	    }, vo);
	}

	function onFormTemplateRestore() {
		Vosao.jsonrpc.formService.restoreFormTemplate(function (r) {
			Vosao.showServiceMessages(r);
	        $('.CodeMirror').remove();
			loadFormConfig();
	    });
	}

	function onFormLetterRestore() {
		Vosao.jsonrpc.formService.restoreFormLetter(function (r) {
			Vosao.showServiceMessages(r);
	        $('.CodeMirror').remove();
	        loadFormConfig();
	    });
	}
	
	
	return Backbone.View.extend({

		css: ['/static/js/codemirror/codemirror.css',
		      '/static/js/codemirror/eclipse.css'],
		
		el: $('#content'),
		
		render: function() {
			Vosao.addCSSFiles(this.css);
			this.el.html(_.template(tmpl, {messages:messages}));
			postRender();
		},
		
		remove: function() {
			this.el.html('');
			Vosao.removeCSSFiles(this.css);
		}
		
	});
	
});