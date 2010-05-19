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
    <script type="text/javascript" src="/static/js/cms/login.js"></script>
    <script type="text/javascript">
        loggedIn = <%= user != null %>;
    </script>
</head>
<body>

<div id="content">
<div id="memo">
    <img src="/static/images/login-logo.jpg" />
    <p><fmt:message key="login.sign_in"/></p>
    <p><fmt:message key="login.visit"/></p>
    <p><fmt:message key="login.bug"/></p>
    <p><fmt:message key="login.forum"/></p>
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
    <br/>
    <a href="#" id="forgot"><fmt:message key="forgot_password"/></a>
</div>    

</form>
</div>

<div class="clear"> </div>

<div class="version">
  <span>Vosao CMS <fmt:message key="version"/> <%= SetupBean.FULLVERSION %></span>
</div>

</div>

<div class="messages"> </div>

<div id="forgot-dialog" style="display:none" title="<fmt:message key="forgot_password"/>">
  <form id="forgotForm">
    <p style="margin-bottom: 20px;"><fmt:message key="login.forgot_dialog"/></p>
    <div class="form-row">
        <label style="width: 80px;"><fmt:message key="email"/></label>
        <input type="text" id="email" />
    </div>
    <div class="buttons-dlg">
        <input type="submit" value="OK" />
        <input id="forgotCancelButton" type="button" value="<fmt:message key="cancel" />" />
    </div>
  </form>
</div>


</body>
</html>