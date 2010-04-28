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
<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<%@ page import="org.vosao.common.VosaoContext" %>
<%@ page import="org.vosao.entity.UserEntity" %>
<%@ page import="org.vosao.business.SetupBean" %>
<% 
    UserEntity user = VosaoContext.getInstance().getUser();
%>
<html>
<head>
    <title><fmt:message key="login.title"/></title>
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
                Vosao.errorMessage('#login-messages', messages['email_is_empty']);
            }
            else {
            	Vosao.jsonrpc.loginFrontService.login(function (r, e) {
                    if (Vosao.serviceFailed(e)) return;
                    if (r.result == 'success') {
                    	Vosao.infoMessage('#login-messages', messages['success_logging_in']);
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
    <p><fmt:message key="login.sign_in"/></p>
    <p><fmt:message key="login.visit"/></p>
    <p><fmt:message key="login.bug"/></p>
</div>

<div id="login-form">
<form onsubmit="onLogin(); return false;">

<h4><fmt:message key="login.sign"/></h4>
<h3><fmt:message key="login.sign_account"/></h3>

<div class="form-row">
    <label><fmt:message key="email"/></label>
    <input type="text" id="loginEmail" />
</div>
<div class="form-row">
    <label><fmt:message key="password"/></label>
    <input type="password" id="loginPassword" />
</div>
<div id="login-messages"> </div>
<div class="buttons-dlg">
    <input type="submit" value="<fmt:message key="login"/>" />
</div>    

</form>
</div>

<div class="clear"> </div>

<div class="version">
  <span>Vosao CMS <fmt:message key="version"/> <%= SetupBean.FULLVERSION %></span>
</div>

</div>

</body>
</html>