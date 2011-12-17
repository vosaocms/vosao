// file TagsView.js
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

define(['text!template/config/tags.html', 'jquery.treeview'],
function(tmpl) {
	
	console.log('Loading TagsView.js');
	
	var tag = null;
	var parentId = null;
	var tags = null;
	var pages = null;

	function postRender() {
	    $("#tag-dialog").dialog({ width: 460, autoOpen: false });
	    Vosao.initJSONRpc(loadData);
	    $('#addButton').click(function() {onAddTag('')});
	    $('#tagForm').submit(function() {onTagSave(); return false;});
	    $('#tagCancelDlgButton').click(onTagCancel);
	    $('#tagDeleteDlgButton').click(onTagDelete);
	    $('ul.ui-tabs-nav li:nth-child(7)').addClass('ui-state-active')
	    		.addClass('ui-tabs-selected')
				.removeClass('ui-state-default');
	    $('#title').change(onTitleChange);
	}

	function loadData() {
		loadTags();
	}

	function onAddTag(id) {
	    parentId = id;
		tag = null;
	    initTagForm();
	    $('#tag-dialog').dialog('open');
	}

	function loadTags() {
		Vosao.jsonrpc.tagService.getTree(function (r) {
			$('#tags').html(renderTags(r.list));
			$('#tags .tagEdit').click(function() {
				onTagEdit($(this).attr('data-id'));
			});
			$('#tags .addTag').click(function() {
				onAddTag($(this).attr('data-id'));
			});
	        $("#tags").treeview({
				animated: "fast",
				collapsed: true,
				unique: true,
				persist: "cookie",
				cookieId: "tagTree"
			});
	    });
	}

	function renderTags(list) {
		var html = ''
		$.each(list, function (i, value) {
			html += renderTag(value);
		});
		return html;
	}

	function renderTag(vo) {
		var html = '<li><a class="tagEdit" data-id="' + vo.entity.id + '">' 
	        + vo.entity.name + '</a> <a class="addTag" title="' + messages('add_child') 
	        + '" data-id="' + vo.entity.id + '">+</a>';
		
	    if (vo.children.list.length > 0) {
	        html += '<ul>';
	        $.each(vo.children.list, function(n, value) {
	            html += renderTag(value);
	        });
	        html += '</ul>';
	    }
	    return html + '</li>';
	}

	function onTagEdit(id) {
		Vosao.jsonrpc.tagService.getById(function (r) {
	        tag = r;
	        parentId = tag.parent;
	        initTagForm();
	        $('#tag-dialog').dialog('open');
	    }, id);
	}

	function initTagForm() {
		if (tag == null) {
	        $('#tagName').val('');
	        $('#title').val('');
	        $('#pages').html('');
		}
		else {
	        $('#tagName').val(tag.name);
	        $('#title').val(tag.title);
	        loadPages();
		}
	    $('#tag-dialog .messages').html('');
	}

	function loadPages() {
		Vosao.jsonrpc.tagService.getPages(function(r) {
			pages = r.list;
			showPages();
		}, tag.id);
	}

	function showPages() {
	    $('#pages').html('');
		if (pages.length > 0) {
			var h = '';
			$.each(pages, function(i, value) {
				h += '<span class="tag">' + value.title + ' <a data-id="' 
					+ i	+ '"><img src="/static/images/02_x.png" /></a></span>';
			});
	        $('#pages').html(h);
	        $('#pages a').click(function() {
	        	onPageRemove($(this).attr('data-id'));
	        })
		}
	}

	function validateTag(vo) {
	    var errors = [];
	    if (vo.name == '') {
	        errors.push(messages('config.tag_is_empty'));
	    }
	    return errors;
	}

	function onTagSave() {
	    var vo = {
	    	id : tag != null ? String(tag.id) : '',
	    	parent : parentId == null ? '' : String(parentId),
	        name : $('#tagName').val(),
	        title : $('#title').val()
	    };
	    var errors = validateTag(vo);
	    if (errors.length == 0) {
	    	Vosao.jsonrpc.tagService.save(function (r) {
	            if (r.result == 'success') {
	                $('#tag-dialog').dialog('close');
	                Vosao.info(r.message);
	                loadTags();
	            }
	            else {
	                tagError(r.message);
	            }
	        }, Vosao.javaMap(vo));
	    }
	    else {
	        tagErrors(errors);
	    }
	}

	function onTagCancel() {
	    $('#tag-dialog').dialog('close');
	}

	function tagError(msg) {
		Vosao.errorMessage('#tag-dialog .messages', msg);
	}

	function tagErrors(errors) {
		Vosao.errorMessages('#tag-dialog .messages', errors);
	}

	function onTagDelete() {
		if (confirm(messages('are_you_sure'))) {
			Vosao.jsonrpc.tagService.remove(function(r) {
				Vosao.showServiceMessages(r);
				$('#tag-dialog').dialog('close');
				loadTags();
			}, tag.id);
		}
	}

	function onPageRemove(i) {
		if (confirm(messages('are_you_sure'))) {
			Vosao.jsonrpc.tagService.removeTag(function (r) {
				Vosao.showServiceMessages(r);
				pages.splice(i,1);
				showPages();
			}, pages[i].friendlyURL, tag.id);
		}
	}

	function onTitleChange() {
		if (tag != null) {
			return;
		}
		var name = $("#tagName").val();
		var title = $("#title").val();
		if (name == '') {
			$("#tagName").val(Vosao.urlFromTitle(title));
		}
	}


	return Backbone.View.extend({
		
		css: '/static/css/jquery.treeview.css',
		
		el: $('#tab-1'),
		
		render: function() {
			Vosao.addCSSFile(this.css);
			this.el = $('#tab-1');
			this.el.html(_.template(tmpl, {messages:messages}));
			postRender();
		},
		
		remove: function() {
		    $("#tag-dialog").dialog('destroy').remove();
			this.el.html('');
			Vosao.removeCSSFile(this.css);
		}
		
	});
	
});