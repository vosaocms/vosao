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
<html>
<head>
    <title>Site configuration</title>
    <script src="/static/js/jquery.form.js" language="javascript"></script>
    <link rel="stylesheet" href="/static/css/config.css" type="text/css" />
    <script src="/static/js/cms/config/comments.js" type="text/javascript"></script>
</head>
<body>

<div id="tabs" class="ui-tabs ui-widget ui-widget-content ui-corner-all">

<%@ include file="tab.jsp" %>

<div id="tab-2" class="ui-tabs-panel ui-widget-content ui-corner-bottom">
  <form id="commentsForm">
    <div class="form-row">
        <label>Comments notification email (or coma separated list)</label>
        <input id="commentsEmail" type="text"/>
    </div>
    <div class="form-row">
        <label>Comments template</label>
        <textarea id="commentsTemplate" cols="80" rows="20"></textarea>
    </div>
    <div class="buttons">
        <input id="commentsSaveButton" type="submit" value="Save" />
        <input id="restoreButton" type="button" value="Restore default" />
    </div>
  </form>
</div>

</div>

</body>
</html>