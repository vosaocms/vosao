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
    <title><fmt:message key="config.title" /></title>
    <script src="/static/js/jquery.form.js" language="javascript"></script>
    <link rel="stylesheet" href="/static/css/config.css" type="text/css" />
    <script src="/static/js/cms/config/languages.js" type="text/javascript"></script>
</head>
<body>

<div id="tabs" class="ui-tabs ui-widget ui-widget-content ui-corner-all">

<%@ include file="tab.jsp" %>

<div id="tab-3" class="ui-tabs-panel ui-widget-content ui-corner-bottom">
    <div id="languages"> </div>
    <div class="buttons">
        <input id="addLanguageButton" type="button" value="<fmt:message key="add" />" />
        <input id="removeLanguageButton" type="button" value="<fmt:message key="remove" />" />
    </div>
</div>

</div>

<div id="language-dialog" style="display:none" title="<fmt:message key="config.site_language" />">
  <form id="languageForm">
    <div class="messages"> </div>
    <div id="languageSelection" style="padding-bottom: 10px;">
        <input id="selectFromListRadio" type="radio" name="select" 
            checked="checked" /> <fmt:message key="select_from_list" />
        <input id="notInListRadio" type="radio" name="select" />
            <fmt:message key="config.not_in_list" />
    </div>        
    <div id="listed-language">
        <select id="selectLanguage"></select>
    </div>
    <div id="not-listed-language" style="display:none">
    <div class="form-row">
        <label><fmt:message key="language" /> <a href="http://ftp.ics.uci.edu/pub/ietf/http/related/iso639.txt">iso639</a> 2 letter code</label>
        <input id="languageCode" type="text"/>
    </div>
    <div class="form-row">
        <label><fmt:message key="title" /></label>
        <input id="languageTitle" type="text"/>
    </div>
    </div>
    <div class="buttons-dlg">
        <input id="languageSaveButton" type="submit" value="<fmt:message key="save" />" />
        <input id="languageCancelButton" type="button" value="<fmt:message key="cancel" />" />
    </div>
  </form>  
</div>

</body>
</html>