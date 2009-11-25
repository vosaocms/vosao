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
<html>
<head>
    <title>Login page</title>
    <link rel="stylesheet" href="/static/css/login.css" type="text/css" />
    <script type="text/javascript">
        function onLogin() {
            var email = $('#loginEmail').val();
            var password = $('#loginPassword').val();
            if (email == '') {
                error('Email is empty!');
            }
            else {
                jsonrpc.loginFrontService.login(function (r, e) {
                    if (serviceFailed(e)) return;
                    if (r.result == 'success') {
                        info('Success. Logging in...');
                        document.location.href = r.message;
                    }
                    else {
                        error(r.message);
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
</div>

<div id="login-form">
<form>

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
<div class="messages"> </div>
<div>
    <label></label>
    <input type="button" value="Login" onclick="onLogin()" />
</div>    

</form>
</div>

<div class="clear"> </div>

<div class="version">
  <span>Vosao CMS Version 0.0.4</span>
</div>

</div>

</body>
</html>