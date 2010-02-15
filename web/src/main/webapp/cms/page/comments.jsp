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

  <%@ include file="head.jsp" %>

  <script type="text/javascript" src="/static/js/cms/page/comments.js"></script>
    
</head>
<body>

<%@ include file="versionsBox.jsp" %>

<div id="tabs" class="ui-tabs ui-widget ui-widget-content ui-corner-all">

<%@ include file="tab.jsp" %>

<div id="tab-1" class="commentsTab ui-tabs-panel ui-widget-content ui-corner-bottom">
    <div id="comments"> </div>
    <div class="buttons">
        <input id="enableCommentsButton" type="button" value="Enable comments" />
        <input id="disableCommentsButton" type="button" value="Disable comments" />
        <input id="deleteCommentsButton" type="button" value="Delete comments" />
    </div>    
</div>

</div>

<%@ include file="versionDialog.jsp" %>

</body>
</html>