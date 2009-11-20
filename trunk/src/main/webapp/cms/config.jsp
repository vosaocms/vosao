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
    <script src="/static/js/cms/config.js" type="text/javascript"></script>
</head>
<body>

<div id="tabs">

<ul>
    <li><a href="#tab-1">Site configuration</a></li>
    <li><a href="#tab-2">Comments</a></li>
    <li><a href="#tab-3">Languages</a></li>
    <li><a href="#tab-4">Message bundle</a></li>
    <li><a href="#tab-5">Users</a></li>
    <li><a href="#tab-6">Groups</a></li>
</ul>

<div id="tab-1">
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
    <div class="buttons">
        <input id="saveButton" type="button" value="Save" />
        <input id="exportButton" type="button" value="Export" />
        <input id="importButton" type="button" value="Import" />
    </div>
</div>

<div id="tab-2">
    <div class="form-row">
        <label>Comments notification email</label>
        <input id="commentsEmail" type="text"/>
    </div>
    <div class="form-row">
        <label>Comments template</label>
        <textarea id="commentsTemplate" cols="80" rows="20"></textarea>
    </div>
    <div class="buttons">
        <input id="commentsSaveButton" type="button" value="Save" />
        <input id="restoreButton" type="button" value="Restore default" />
    </div>
</div>

<div id="tab-3">
    <div id="languages"> </div>
    <div class="buttons">
        <input id="addLanguageButton" type="button" value="Add" />
        <input id="removeLanguageButton" type="button" value="Remove" />
    </div>
</div>

<div id="tab-4">
    <div id="messageBundle"> </div>
    <div class="buttons">
        <input id="addMessageButton" type="button" value="Add" />
        <input id="removeMessageButton" type="button" value="Remove" />
    </div>
</div>

<div id="tab-5">
    <div id="users"> </div>
    <div class="buttons">
        <input id="addUserButton" type="button" value="Add" />
        <input id="removeUserButton" type="button" value="Remove" />
    </div>
</div>

<div id="tab-6">
    <div id="groups"> </div>
    <div class="buttons">
        <input id="addGroupButton" type="button" value="Add" />
        <input id="removeGroupButton" type="button" value="Remove" />
    </div>
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
    <p class="message"></p>
    <div class="buttons-dlg">
        <input id="okButton" type="button" value="OK" />
    </div>
</div>

<div id="language-dialog" style="display:none" title="Site language">
    <div class="messages"> </div>
    <div id="languageSelection" style="padding-bottom: 10px;">
        <input id="selectFromListRadio" type="radio" name="select" 
            checked="checked" /> Select from list
        <input id="notInListRadio" type="radio" name="select" />
            Not in the list
    </div>        
    <div id="listed-language">
        <select id="selectLanguage"></select>
    </div>
    <div id="not-listed-language" style="display:none">
    <div class="form-row">
        <label>Language <a href="http://ftp.ics.uci.edu/pub/ietf/http/related/iso639.txt">iso639</a> 2 letter code</label>
        <input id="languageCode" type="text"/>
    </div>
    <div class="form-row">
        <label>Language title</label>
        <input id="languageTitle" type="text"/>
    </div>
    </div>
    <div class="buttons-dlg">
        <input id="languageSaveButton" type="button" value="Save" />
        <input id="languageCancelButton" type="button" value="Cancel" />
    </div>
</div>

<div id="message-dialog" style="display:none" title="Localized message">
    <div class="messages"> </div>
    <div class="form-row">
        <label>Message code</label>
        <input id="messageCode" type="text"/>
    </div>
    <div id="messagesInput"> </div>
    <div class="buttons-dlg">
        <input id="saveMessageDlgButton" type="button" value="Save" />
        <input id="cancelMessageDlgButton" type="button" value="Cancel" />
    </div>
</div>

<div id="user-dialog" style="display:none" title="User details">
    <div class="messages"> </div>
    <div class="form-row">
        <label>User name</label>
        <input id="userName" type="text"/>
    </div>
    <div class="form-row">
        <label>User email</label>
        <input id="userEmail" type="text"/>
    </div>
    <div>
        <label>User role</label>
        <select id="userRole">
            <option value="USER">User</option>
            <option value="ADMIN">Administrator</option>
        </select>
    </div>
    <div class="form-row">
        <label>Password</label>
        <input id="userPassword1" type="password"/>
    </div>
    <div class="form-row">
        <label>Retype password</label>
        <input id="userPassword2" type="password"/>
    </div>
    <div class="buttons-dlg">
        <input id="userSaveDlgButton" type="button" value="Save" />
        <input id="userCancelDlgButton" type="button" value="Cancel" />
    </div>
</div>

<div id="group-dialog" style="display:none" title="Group details">
    <div class="messages"> </div>
    <div class="form-row">
        <label>Group name</label>
        <input id="groupName" type="text"/>
    </div>
    <div class="buttons-dlg">
        <input id="groupSaveDlgButton" type="button" value="Save" />
        <input id="groupCancelDlgButton" type="button" value="Cancel" />
    </div>
</div>

<div id="user-group-dialog" style="display:none" title="Group users">
    <div id="groupUsers"> </div>
    <div class="buttons-dlg">
        <input id="userGroupSaveDlgButton" type="button" value="Save" />
        <input id="userGroupCancelDlgButton" type="button" value="Cancel" />
    </div>
</div>


</body>
</html>