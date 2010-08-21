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

var loggedIn = false;

$(function() {
    $("#forgot-dialog").dialog({ width: 400, autoOpen: false });
	
    $('#forgot').click(function() {
		$('#email').val('');
		$('#forgot-dialog').dialog('open');
	});
	
    $('#forgotCancelButton').click(function() {
		$('#forgot-dialog').dialog('close');
	});
    
    $('#forgotForm').submit(function() {
    	var email = $('#email').val();
    	if (!email) {
    		Vosao.error(messages('email_is_empty'));
    		return false;
    	}
    	$('#loading').show();
    	Vosao.jsonrpc.loginFrontService.forgotPassword(function(r) {
    		$('#loading').hide();
    		if (r.result == 'success') {
    	    	Vosao.info(messages('login.password_letter_success'));
    		}
    		else {
    			Vosao.error(e.message);
    		}
			$('#forgot-dialog').dialog('close');
    	}, email);
    	return false;
    });
    
});

function onLogin() {
	if (loggedIn) {
		location.href = '/cms/index.vm';
		return;
	}
	var email = $('#loginEmail').val();
	var password = $('#loginPassword').val();
	if (email == '') {
		Vosao.errorMessage('#login-messages', messages('email_is_empty'));
	} else {
		Vosao.jsonrpc.loginFrontService.login(function(r, e) {
			if (Vosao.serviceFailed(e))
				return;
			if (r.result == 'success') {
				Vosao.infoMessage('#login-messages',
						messages('success_logging_in'));
				document.location.href = r.message;
			} else {
				Vosao.errorMessage('#login-messages', r.message);
			}
		}, email, password);
	}
}
