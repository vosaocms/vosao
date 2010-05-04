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
    <title><fmt:message key="plugins" /></title>
</head>
<body>

<div id="main-panel">
    <div>
        <img src="/static/images/config.png" />
        <a href="/cms/plugins/config.jsp"><fmt:message key="plugins.config" /></a>
        <p><fmt:message key="plugins.config_info" /></p>
    </div>
    <div>
        <img src="/static/images/form.png" />
        <a href="/cms/plugins/forms.jsp"><fmt:message key="forms" /></a>
        <p><fmt:message key="plugins.forms_info" /></p>
    </div>
    <div>
        <img src="/static/images/seo_urls.png" />
        <a href="/cms/plugins/seo-urls.jsp"><fmt:message key="plugins.seo_urls" /></a>
        <p><fmt:message key="plugins.seo_urls_info" /></p>
    </div>
</div>

</body>
</html>