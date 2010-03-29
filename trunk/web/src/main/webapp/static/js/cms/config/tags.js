/**
 * Vosao CMS. Simple CMS for Google App Engine.
 * Copyright (C) 2009 Vosao development team
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * email: vosao.dev@gmail.com
 */

var tag = null;
var parentId = null;
var tags = null;
var pages = null;

$(function(){
    $("#tag-dialog").dialog({ width: 460, autoOpen: false });
    Vosao.initJSONRpc(loadData);
    $('#addButton').click(function() {onAddTag('')});
    $('#tagForm').submit(function() {onTagSave(); return false;});
    $('#tagCancelDlgButton').click(onTagCancel);
    $('#tagDeleteDlgButton').click(onTagDelete);
    $('ul.ui-tabs-nav li:nth-child(7)').addClass('ui-state-active')
			.removeClass('ui-state-default');
});

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
	var html = '<li><a href="#" onclick="onTagEdit(' + vo.entity.id + ')">' 
        + vo.entity.name + '</a> <a title="Add child" href="#" onclick="onAddTag(' 
        + vo.entity.id + ')">+</a>';
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
        $('#pages').html('');
	}
	else {
        $('#tagName').val(tag.name);
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
	if (pages.length > 0) {
		var h = '';
		$.each(pages, function(i, value) {
			h += '<span class="tag">' + value.title + ' <a href="#" onclick="onPageRemove(' 
				+ i	+ ')"><img src="/static/images/02_x.png" /></a></span>';
		});
        $('#pages').html(h);
	}
}

function validateTag(vo) {
    var errors = [];
    if (vo.email == '') {
        errors.push('Tag is empty');
    }
    return errors;
}

function onTagSave() {
    var vo = {
    	id : tag != null ? String(tag.id) : '',
    	parent : parentId == null ? '' : String(parentId),
        name : $('#tagName').val()
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
                tagErrors(r.messages.list);
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
	if (confirm('Are you shure?')) {
		Vosao.jsonrpc.tagService.remove(function(r) {
			Vosao.showServiceMessages(r);
			$('#tag-dialog').dialog('close');
			loadTags();
		}, tag.id);
	}
}

function onPageRemove(i) {
	if (confirm('Are you shure?')) {
		Vosao.jsonrpc.tagService.removeTag(function (r) {
			Vosao.showServiceMessages(r);
			pages.splice(i,1);
			showPages();
		}, pages[i].friendlyURL, tag.id);
	}
}