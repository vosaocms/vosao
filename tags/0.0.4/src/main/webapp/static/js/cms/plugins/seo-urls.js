/**
 * Vosao CMS. Simple CMS for Google App Engine. Copyright (C) 2009 Vosao
 * development team
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 * 
 * email: vosao.dev@gmail.com
 */

var seoUrl = null;

$(function() {
    $("#url-dialog").dialog({ width :500, autoOpen :false });
    initJSONRpc(loadUrls);
    $('#tabs').tabs();
    $('#addButton').click(onAdd);
    $('#removeButton').click(onRemove);
    $('#saveAndAddButton').click(onSaveAndAdd);
    $('#saveDlgButton').click(function(){ onSave(true); });
    $('#cancelDlgButton').click(onCancel);
});

function loadUrls() {
    seoUrlService.select(function (r) {
        var html = '<table class="form-table"><tr><th></th><th>From</th><th>To</th></tr>';
        $.each(r.list, function (i, url) {
            html += '<tr><td><input type="checkbox" value="' 
                + url.id + '"/></td><td><a href="#" onclick="onEdit(\''
                + url.id +'\')">' + url.fromLink + '</a></td><td>' 
                + url.toLink + '</td></tr>';
        });
        $('#urls').html(html + '</table>');
        $('#urls tr:even').addClass('even');
    });
}

function onEdit(id) {
    clearMessages();
    seoUrlService.getById(function(r) {
        seoUrl = r;
        urlDialogInit();
        $("#url-dialog").dialog("open");
    }, id);
}

function onAdd() {
    seoUrl = null;
    urlDialogInit();
    $("#url-dialog").dialog("open");
}

function urlDialogInit() {
	clearMessages();
    if (seoUrl == null) {
        $('#fromLink').val('');
        $('#toLink').val('');
    }
    else {
        $('#fromLink').val(seoUrl.fromLink);
        $('#toLink').val(seoUrl.toLink);
    }
}

function onRemove() {
    var ids = new Array();
    $('#urls input:checked').each(function() {
        ids.push(this.value);
    });
    if (ids.length == 0) {
        info('Nothing selected.');
        return;
    }
    if (confirm('Are you sure?')) {
        seoUrlService.remove(function(r) {
            showServiceMessages(r);
            loadUrls();
        }, javaList(ids));
    }
}

function onSaveAndAdd() {
    onSave(false, true);
}

function validateSeoUrl(vo) {
    var errors = new Array();
    if (vo.fromLink == '') {
        errors.push('From link is empty');
    }
    if (vo.toLink == '') {
        errors.push('Site redirect link is empty');
    }
    return errors;
}

function onSave(closeFlag, addFlag) {
    var vo = {
        id : seoUrl != null ? seoUrl.id : '',
        fromLink : $('#fromLink').val(),
        toLink : $('#toLink').val(),
    };
    var errors = validateSeoUrl(vo);
    if (errors.length == 0) {
        seoUrlService.save(function(r) {
            if (r.result == 'success') {
                if (closeFlag) {
                    $("#url-dialog").dialog("close");
                }
                loadUrls();
                if (addFlag) {
                	onAdd();
                }
            } else {
                errorUrlMessages(r.messages.list);
            }
        }, javaMap(vo));
    } else {
        errorUrlMessages(errors);
    }
}

function onCancel() {
    $("#url-dialog").dialog("close");
}

function clearMessages() {
    $('#url-messages').html('');
}

function errorUrlMessages(messages) {
    errorMessages("#url-messages", messages);
}
