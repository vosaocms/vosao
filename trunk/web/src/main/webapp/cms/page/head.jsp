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
  <%@ page import="org.vosao.servlet.FileUploadServlet" %>
  
  <title><fmt:message key="page" /></title>
  <link rel="stylesheet" href="/static/css/page.css" type="text/css" />
  <script type="text/javascript" src="/static/ckeditor/ckeditor.js"></script>
  
<%
    if (request.getParameter("id") != null) {    
        session.setAttribute(FileUploadServlet.IMAGE_UPLOAD_PAGE_ID, 
        		request.getParameter("id"));
    }
%>  
  
  <script type="text/javascript">
    var pageId = '<c:out value="${param.id}"/>';
    var pageParentUrl = decodeURIComponent('<c:out value="${param.parent}"/>');
  </script>

  <script type="text/javascript" src="/static/js/cms/page/version.js"></script>
 