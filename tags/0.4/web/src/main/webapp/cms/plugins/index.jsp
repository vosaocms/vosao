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
    <title>Plugins</title>
</head>
<body>

<div id="main-panel">
    <div>
        <img src="/static/images/config.png" />
        <a href="/cms/plugins/config.jsp">Plugins Configuration</a>
        <p>You can install and configure existing plugins.
        </p>
    </div>
    <div>
        <img src="/static/images/form.png" />
        <a href="/cms/plugins/forms.jsp">Forms</a>
        <p>Forms plugin will help you to create different kinds of forms. 
           User entered data will be emailed to specified address.
        </p>
    </div>
    <div>
        <img src="/static/images/seo_urls.png" />
        <a href="/cms/plugins/seo-urls.jsp">SEO Urls</a>
        <p>This plugin will help you to save many spent for SEO advertising
           and indexing of your previous site. Here you can enter a list of
           indexed urls and list of corresponding Vosao site urls.
        </p>
    </div>
</div>

</body>
</html>