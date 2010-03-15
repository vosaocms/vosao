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
    <script src="/static/js/cms/config/index.js" type="text/javascript"></script>
</head>
<body>

<div id="tabs" class="ui-tabs ui-widget ui-widget-content ui-corner-all">

<%@ include file="tab.jsp" %>

<div id="tab-1" class="ui-tabs-panel ui-widget-content ui-corner-bottom">
  <form id="configForm">
    <div class="form-row">
        <label>Google Analytics ID</label>
        <input id="googleAnalyticsId" type="text" />
    </div>
    <div class="form-row">
        <label>Site owner email</label>
        <input id="siteEmail" type="text" />
    </div>
    <div class="form-row">
        <label>Site domain</label>
        <input id="siteDomain" type="text" />
    </div>
    <div class="form-row">
        <label>Enable reCaptcha use on the site</label>
        <input id="enableRecaptcha" type="checkbox" />
    </div>
    <div id="recaptcha">
        <div class="form-row">
            <label>reCaptcha service public key</label>
            <input id="recaptchaPublicKey" type="text" size="40"/>
        </div>
        <div class="form-row">
            <label>reCaptcha service private key</label>
            <input id="recaptchaPrivateKey" type="text" size="40"/>
        </div>
    </div>
    <div class="form-row">
        <label>Editable resource files extensions</label>
        <input id="editExt" type="text"/>
    </div>
    <div class="form-row">
        <label>Site users login page url</label>
        <input id="siteUserLoginUrl" type="text"/>
    </div>
    <div class="buttons">
        <input id="saveButton" type="submit" value="Save" />
        <input id="exportButton" type="button" value="Export" />
        <input id="importButton" type="button" value="Import" />
        <input id="reindexButton" type="button" value="Create search index" />
        <input id="resetButton" type="button" value="Reset" />
        <input id="cacheResetButton" type="button" value="Cache reset" />
    </div>
  </form>
</div>

</div>

<div id="import-dialog" title="Import site" style="display:none">
<form id="upload" action="/cms/upload" method="post" enctype="multipart/form-data">
    File upload:
    <input type="hidden" name="fileType" value="import" />
    <input type="file" name="uploadFile" />
    <div class="buttons-dlg">
        <input type="submit" value="Send" />
        <input id="importCancelButton" type="button" value="Cancel" />
    </div>    
</form>
</div>

<div id="afterUpload-dialog" style="display:none" title="Status window">
  <form id="okForm">
    <p class="message"></p>
    <div class="buttons-dlg">
        <input id="okButton" type="submit" value="OK" />
    </div>
  </form>
</div>

<div id="export-dialog" style="display:none" title="Export window">
  <form id="exportForm">
    <input type="radio" name="exportType" value="full" />
        Full export <br/>
    <input type="radio" name="exportType" value="site" checked="checked"/>
        Site, pages & resources <br/>
    <input type="radio" name="exportType" value="resources"/>
        Resources <br/>
    <div class="buttons-dlg">
        <input type="submit" value="Export" />
        <input id="exportCancelButton" type="button" value="Cancel" />
    </div>
  </form>
</div>

</body>
</html>