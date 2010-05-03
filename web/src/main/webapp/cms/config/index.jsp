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
    <script src="/static/js/cms/config/index.js" type="text/javascript"></script>
</head>
<body>

<div id="tabs" class="ui-tabs ui-widget ui-widget-content ui-corner-all">

<%@ include file="tab.jsp" %>

<div id="tab-1" class="ui-tabs-panel ui-widget-content ui-corner-bottom">
  <form id="configForm">
    <div class="form-row">
        <label><fmt:message key="config.db_version" /></label>
        <span id="version" />
    </div>
    <div class="form-row">
        <label>Google Analytics ID</label>
        <input id="googleAnalyticsId" type="text" />
    </div>
    <div class="form-row">
        <label><fmt:message key="config.site_owner_email" /></label>
        <input id="siteEmail" type="text" />
    </div>
    <div class="form-row">
        <label><fmt:message key="site_domain" /></label>
        <input id="siteDomain" type="text" />
    </div>
    <div class="form-row">
        <label><fmt:message key="config.enable_recaptcha" /></label>
        <input id="enableRecaptcha" type="checkbox" />
    </div>
    <div id="recaptcha">
        <div class="form-row">
            <label><fmt:message key="config.recaptcha_public_key" /></label>
            <input id="recaptchaPublicKey" type="text" size="40"/>
        </div>
        <div class="form-row">
            <label><fmt:message key="config.recaptcha_private_key" /></label>
            <input id="recaptchaPrivateKey" type="text" size="40"/>
        </div>
    </div>
    <div class="form-row">
        <label><fmt:message key="config.edit_extension" /></label>
        <input id="editExt" type="text"/>
    </div>
    <div class="form-row">
        <label><fmt:message key="config.login_url" /></label>
        <input id="siteUserLoginUrl" type="text"/>
    </div>
    <div class="form-row">
        <label><fmt:message key="config.enable_picasa" /></label>
        <input id="enablePicasa" type="checkbox" />
    </div>
    <div id="picasa">
        <div class="form-row">
            <label>Picasa <fmt:message key="user_email" /></label>
            <input id="picasaUser" type="text" />
        </div>
        <div class="form-row">
            <label>Picasa <fmt:message key="password" /></label>
            <input id="picasaPassword" type="text" />
        </div>
    </div>
    <div class="buttons">
        <input id="saveButton" type="submit" value="<fmt:message key="save" />" />
        <input id="exportButton" type="button" value="<fmt:message key="export" />" />
        <input id="importButton" type="button" value="<fmt:message key="import" />" />
        <input id="reindexButton" type="button" value="<fmt:message key="config.create_search_index" />" />
        <input id="resetButton" type="button" value="<fmt:message key="reset" />" />
        <input id="cacheResetButton" type="button" value="<fmt:message key="config.cache_reset" />" />
    </div>
  </form>
</div>

</div>

<div id="import-dialog" title="<fmt:message key="config.import_site" />" style="display:none">
<form id="upload" action="/cms/upload" method="post" enctype="multipart/form-data">
    <fmt:message key="file_upload" />:
    <input type="hidden" name="fileType" value="import" />
    <input type="file" name="uploadFile" />
    <div class="buttons-dlg">
        <input type="submit" value="<fmt:message key="send" />" />
        <input id="importCancelButton" type="button" value="<fmt:message key="cancel" />" />
    </div>    
</form>
</div>

<div id="afterUpload-dialog" style="display:none" title="<fmt:message key="status_window" />">
  <form id="okForm">
    <p class="message"></p>
    <div class="buttons-dlg">
        <input id="okButton" type="submit" value="OK" />
    </div>
  </form>
</div>

<div id="export-dialog" style="display:none" title="<fmt:message key="export_window" />">
  <form id="exportForm">
    <div class="form-row">
    <input type="radio" name="exportType" value="full" />
        <fmt:message key="config.full_export" /> <br/>
    <input type="radio" name="exportType" value="site" checked="checked"/>
        <fmt:message key="config.export_site" /> <br/>
    <input type="radio" name="exportType" value="resources"/>
        <fmt:message key="resources" /> <br/>
    </div>
    <div id="exportInfo" class="form-row"></div>    
    <div id="timer" class="form-row"></div>
    <div class="buttons-dlg">
        <input id="exportDialogButton" type="submit" value="<fmt:message key="export" />" />
        <input id="exportCancelButton" type="button" value="<fmt:message key="cancel" />" />
    </div>
  </form>
</div>

</body>
</html>