/**
 * Vosao CMS. Simple CMS for Google App Engine.
 * 
 * Copyright (C) 2009-2010 Vosao development team.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * email: vosao.dev@gmail.com
 */

var languages = null;

$(function(){
    $("#message-dialog").dialog({ width: 400, autoOpen: false });
    Vosao.initJSONRpc(loadData);
    $('#addMessageButton').click(onAddMessage);
    $('#removeMessageButton').click(onRemoveMessage);
    $('#messageForm').submit(function() {onMessageSave(); return false;});
    $('#cancelMessageDlgButton').click(onMessageCancel);
    $('ul.ui-tabs-nav li:nth-child(4)').addClass('ui-state-active')
    		.addClass('ui-tabs-selected')
			.removeClass('ui-state-default');
});

function loadData() {
	loadLanguages();
}

// Language

function loadLanguages() {
	Vosao.jsonrpc.languageService.select(function (r) {
        languages = r.list;
        loadMessages();
    });
}

// Message 

function onAddMessage() {
	clearDialogMessages();
    var h = '';
	$.each(languages, function (i, lang) {
        h += '<div class="form-row"><label>' + lang.title 
            + '</label><input type="text" id="message_' + lang.code + '" /></div>';
    });
    $('#messageCode').val('');
	$('#messagesInput').html(h);
	$('#message-dialog').dialog('open');
}

function onRemoveMessage() {
    var codes = [];
    $('#messageBundle input:checked').each(function () {
        codes.push(this.value);
    });
    if (codes.length == 0) {
    	Vosao.info(messages('nothing_selected'));
        return;
    }
    if (confirm(messages('are_you_sure'))) {
    	Vosao.jsonrpc.messageService.remove(function (r) {
    		Vosao.info(r.message);
            loadMessages();
        }, Vosao.javaList(codes));
    }
}

function loadMessages() {
	Vosao.jsonrpc.messageService.select(function (r) {
        var h = '<table class="form-table"><tr><th></th><th>' + messages('code') 
        	+ '</th>';
        $.each(languages, function (i, lang) {
            h += '<th>' + lang.title + '</th>';
        });
        h += '</tr>';
        $.each(r.list, function (i, msg) {
            h += '<tr><td><input type="checkbox" value="' + msg.code + '"/></td>\
                <td><a href="#" onclick="onMessageEdit(\'' + msg.code + '\')">'
                + msg.code + '</a></td>';
            $.each(languages, function (i, lang) {
                h += '<td>' + msg.values.map[lang.code] + '</td>';
            });
            h += '</tr>';
        });
        $('#messageBundle').html(h + '</table>');
        $('#messageBundle tr:even').addClass('even');
	});
}

function onMessageEdit(code) {
	Vosao.jsonrpc.messageService.selectByCode(function (r) {
        onAddMessage();
        $('#messageCode').val(code);
        $.each(r.list, function (i, msg) {
            $('#message_' + msg.languageCode).val(msg.value);
        });
    }, code);
}

function onMessageSave() {
    var vo = {code : $('#messageCode').val() };
    $.each(languages, function (i, lang) {
        vo[lang.code] = $('#message_' + lang.code).val();
    });
    Vosao.jsonrpc.messageService.save(function (r) {
        if (r.result == 'success') {
            loadMessages();
            $('#message-dialog').dialog('close');
        }
        else {
            messageErrors(r.messages.list);
        }
    }, Vosao.javaMap(vo));
    
}

function onMessageCancel() {
    $('#message-dialog').dialog('close');
}

function messageError(msg) {
	Vosao.errorMessage('#message-dialog .messages', msg);
}

function messageErrors(errors) {
	Vosao.errorMessages('#message-dialog .messages', errors);
}

function clearDialogMessages() {
	$('#message-dialog .messages').html();
}
