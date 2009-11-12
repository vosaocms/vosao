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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="pragma" content="no-cache" />
    <meta http-equiv="expires" content="0" />
    <title>Vosao CMS - <decorator:title default="default title" />
    </title>

    <link rel="stylesheet" href="/static/css/jquery-ui/redmond/jquery-ui.css" type="text/css" />

    <script src="/static/js/jquery.js" language="javascript"></script>
    <script src="/static/js/jquery-ui.js" language="javascript"></script>
    <script src="/static/js/jsonrpc.js" language="javascript"></script>
    <script src="/static/js/vosao.js" language="javascript"></script>
    <script src="/static/js/cms.js" language="javascript"></script>
    <script src="/static/js/back-services.js" language="javascript"></script>

    <link rel="stylesheet" href="/static/css/style.css" type="text/css" />
    
    <decorator:head />

</head>

<body>

<div id="wrapper">
    <div class="messages"> </div>

    <decorator:body />
</div>

</body>

</html>
