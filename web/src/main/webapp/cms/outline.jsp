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
    <title>Pages</title>
    <script src="/static/js/jquery.hotkeys.js" type="text/javascript"></script>
    <script src="/static/js/jquery.tree.js" type="text/javascript"></script>
    <script src="/static/js/plugins/jquery.tree.hotkeys.js" type="text/javascript"></script>
    <script src="/static/js/plugins/jquery.tree.cookie.js" type="text/javascript"></script>
    <link rel="stylesheet" href="/static/css/tree/style.css" type="text/css" />
    <script src="/static/js/cms/outline.js" type="text/javascript"></script>
    <link rel="stylesheet" href="/static/css/outline.css" type="text/css" />
</head>
<body>

<div id="tabs" class="ui-tabs ui-widget ui-widget-content ui-corner-all">
    <ul class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
        <li class="ui-corner-top ui-tabs-selected ui-state-active">
            <a href="#tab-1">Pages</a>
        </li>
        <li id="structuresTab" class="ui-corner-top ui-state-default">
            <a href="structures.jsp">Structures</a>
        </li>
    </ul>
    <div id="tab-1" class="ui-tabs-panel ui-widget-content ui-corner-bottom">
        <div class="outline-link">
            <a href="pages.jsp">Classic editor</a>
        </div>
        <div id="legend" class="ui-widget ui-widget-content ui-corner-all">
            <h2>Keyboard shortcuts</h2>
            <div class="form-row">
                <strong>Press UP arrow </strong> to move up
            </div>
            <div class="form-row">
                <strong>Press DOWN arrow </strong> to move down
            </div>
            <div class="form-row">
                <strong>Press LEFT arrow </strong> to close branch
            </div>
            <div class="form-row">
                <strong>Press RIGHT arrow </strong> to open branch
            </div>
            <div class="form-row">
                <strong>Press INS </strong> to create page
            </div>
            <div class="form-row">
                <strong>Press F2 </strong> to rename
            </div>
            <div class="form-row">
                <strong>Press DELETE </strong> to remove page
            </div>
            <div class="form-row">
                <strong>Press CTRL + c </strong> to copy page
            </div>
            <div class="form-row">
                <strong>Press CTRL + x </strong> to cut page
            </div>
            <div class="form-row">
                <strong>Press CTRL + v </strong> to paste page
            </div>
        </div>
        <div id="pages-tree"></div>
        <div class="clear"></div>
        <div class="buttons">
            <input id="saveButton" type="submit" value="Save changes" />
            <input id="restoreButton" type="button" value="Restore saved" />
        </div>
    </div>
</div>

<div id="page-dialog" style="display:none" title="New page">
  <form id="pageForm">
    <div class="form-row">
        <label>Page title</label>
        <input id="title"/>
    </div>
    <div class="form-row">
        <label>Page URL</label>
        <span id="parentURL"></span> <input id="url"/>
    </div>
    <div id="pageMessages"></div>
    <div class="buttons-dlg">
        <input id="saveDlgButton" type="submit" value="Save" />
        <input id="cancelDlgButton" type="button" value="Cancel" />
    </div>
  </form>  
</div>

</body>
</html>