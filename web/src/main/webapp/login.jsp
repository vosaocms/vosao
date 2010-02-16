<%
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
%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="org.vosao.business.CurrentUser" %>
<%@ page import="org.vosao.entity.UserEntity" %>
<%@ page import="org.vosao.business.impl.SetupBeanImpl" %>
<% 
    UserEntity user = CurrentUser.getInstance();
%>
<html>
<head>
    <title>Login page</title>
    <link rel="stylesheet" href="/static/css/login.css" type="text/css" />
    <script type="text/javascript">
        var loggedIn = <%= user != null %>;
    
        function onLogin() {
            if (loggedIn) {
                location.href = '/cms';
                return;
            }
            var email = $('#loginEmail').val();
            var password = $('#loginPassword').val();
            if (email == '') {
                Vosao.errorMessage('#login-messages', 'Email is empty!');
            }
            else {
            	Vosao.jsonrpc.loginFrontService.login(function (r, e) {
                    if (Vosao.serviceFailed(e)) return;
                    if (r.result == 'success') {
                    	Vosao.infoMessage('#login-messages', 'Success. Logging in...');
                        document.location.href = r.message;
                    }
                    else {
                    	Vosao.errorMessage('#login-messages', r.message);
                    }
                }, email, password);
            }                
        }
    </script>
</head>
<body>

<div id="content">
<div id="memo">
    <img src="/static/images/login-logo.jpg" />
    <p>Sign in to change site content.</p>
    <p>Visit <a href="http://www.vosao.org">www.vosao.org</a> to 
    get more information about Vosao CMS. </p>
    <p>If you found a bug please open an issue on project   
    <a href="http://code.google.com/p/vosao/issues">Issue Tracker</a> </p>
</div>

<div id="login-form">
<form onsubmit="onLogin(); return false;">

<h4>Sign in with your</h4>
<h3>Vosao CMS account</h3>

<div class="form-row">
    <label>Email</label>
    <input type="text" id="loginEmail" />
</div>
<div class="form-row">
    <label>Password</label>
    <input type="password" id="loginPassword" />
</div>
<div id="login-messages"> </div>
<div class="buttons-dlg">
    <input type="submit" value="Login" />
</div>    

</form>
</div>

<div class="clear"> </div>

<div class="version">
  <span>Vosao CMS Version <%= SetupBeanImpl.FULLVERSION %></span>
</div>

</div>

</body>
</html>