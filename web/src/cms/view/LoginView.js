// file LoginView.js
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

define(['text!template/login.html', 'text!template/locale.html', 'text!template/login-topbar.html'], 
function(tmpl, localeTmpl, loginTopbarTmpl) {

	// Due to JQuery-UI dialog - Backbone problems we are using this handler.
	function forgotForm_submit(e) {
		e.preventDefault();
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
    	}, email.toLowerCase());
    	return false;
	}
	
	
	return Backbone.View.extend({
		
		el: $('#content'),
		
		events: {
			"click #forgot": 	"forgot_click",
			"submit form#login":"login_submit"
		},
		
		css: '/static/css/login.css', 

		forgot_click : function() {
			this.$('#email').val('');
			$('#forgot-dialog').dialog('open');
		},
		
		login_submit: function(e) {
			e.preventDefault();
	    	if (Vosao.app.loggedIn) {
	    		Vosao.app.trigger("login");
	    		return false;
	    	}
	    	var email = this.$('#loginEmail').val();
	    	var password = this.$('#loginPassword').val();
	    	if (email == '') {
	    		Vosao.errorMessage('#login-messages', messages('email_is_empty'));
	    	} else {
	    		Vosao.jsonrpc.loginFrontService.login(function(r, e) {
	    			if (Vosao.serviceFailed(e))
	    				return false;
	    			if (r.result == 'success') {
	    				Vosao.infoMessage('#login-messages', messages('success_logging_in'));
	    				Vosao.app.loggedIn = true;
	    	    		Vosao.app.trigger('login');
	    			} else {
	    				Vosao.errorMessage('#login-messages', r.message);
	    			}
	    		}, email, password);
	    	}
	    	return false;
		},
		
		render: function() {
			Vosao.addCSSFile(this.css);
			//this.el.html(_.template(tmpl, {messages: messages}));
			this.el.html($.jqote(tmpl, {messages: messages}));
			$("#forgot-dialog").dialog({ width: 400, autoOpen: false });
			
			// defining event handlers here due to JQuery-UI - Backbone issue
			$('#forgotForm').submit(forgotForm_submit);
			$("#forgotCancelButton").click(function() {
				$('#forgot-dialog').dialog('close');
			});
			// filling topbar
			var localeHtml = _.template(localeTmpl, {messages:messages});
			$('#topbar').html(_.template(loginTopbarTmpl, {messages:messages, locale:localeHtml}));
			$('#languageSelect').click(function() {
				$('#languageDiv').show();
				setTimeout(function() {
			        $('#languageDiv').hide();
				}, 5000);
			});
			
			return this;
		},
		
		remove: function() {
    		$('#forgot-dialog').dialog('destroy').remove();
			this.el.html('');
			Vosao.removeCSSFile(this.css);
		}
	
	});
	
});