<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<html>
<head>
    <title>Site configuration</title>

    <script src="/static/js/jquery.form.js" language="javascript"></script>
    <link rel="stylesheet" href="/static/css/config.css" type="text/css" />

<script type="text/javascript">

var config = '';

$(function(){
    $("#import-dialog").dialog({ width: 400, autoOpen: false });
    $("#tabs").tabs();
    $('#upload').ajaxForm(afterUpload);
    initJSONRpc(loadConfig);
    $('#enableRecaptcha').click(toggleRecaptcha);
});

function toggleRecaptcha() {
    var recaptcha = $('#enableRecaptcha:checked').size() > 0;
    if (recaptcha) {
        $('#recaptcha').show();
    }
    else {
        $('#recaptcha').hide();
    }
}

function afterUpload(data) {
    var s = data.split('::');
    var result = s[1];
    var msg = s[2]; 
    if (result == 'success') {
        msg = 'Success. File was successfully imported.';
    }
    else {
        msg = "Error. " + msg;
    }   
    $("#import-dialog").dialog("close");
    $("#afterUpload-dialog .message").text(msg);
    $("#afterUpload-dialog").dialog();
}

function onImport() {
    $("#import-dialog").dialog("open");
}

function onImportCancel() {
    $("#import-dialog").dialog("close");
}

function onAfterUploadOk() {
    window.location.reload();
}

function loadConfig() {
    configService.getConfig(function (r) {
        config = r;
        initFormFields();
    }); 
}

function initFormFields() {
	$('#googleAnalyticsId').val(config.googleAnalyticsId);
    $('#siteEmail').val(config.siteEmail);
    $('#siteDomain').val(config.siteDomain);
    $('#enableRecaptcha').each(function () {this.checked = config.enableRecaptcha});
    $('#recaptchaPublicKey').val(config.recaptchaPublicKey);
    $('#recaptchaPrivateKey').val(config.recaptchaPrivateKey);
    toggleRecaptcha();
    $('#editExt').val(config.editExt);
    $('#commentsEmail').val(config.commentsEmail);
    $('#commentsTemplate').val(config.commentsTemplate);
}

function onSave() {
    var vo = javaMap({
    	googleAnalyticsId : $('#googleAnalyticsId').val(),
        siteEmail : $('#siteEmail').val(),
        siteDomain : $('#siteDomain').val(),
        enableRecaptcha : String($('#enableRecaptcha:checked').size() > 0),
        recaptchaPublicKey : $('#recaptchaPublicKey').val(),
        recaptchaPrivateKey : $('#recaptchaPrivateKey').val(),
        editExt : $('#editExt').val(),
        commentsEmail : $('#commentsEmail').val(),
        commentsTemplate : $('#commentsTemplate').val()        
    });
    configService.saveConfig(function(r) {
        showServiceMessages(r);
    }, vo); 
}

function onExport() {
    location.href = '/cms/export?type=site';
}

</script>

</head>
<body>

<div id="tabs">

<ul>
    <li><a href="#tab-1">Site configuration</a></li>
    <li><a href="#tab-2">Comments</a></li>
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
    <input type="button" value="Save" onclick="onSave()" />
    <input type="button" value="Export" onclick="onExport()" />
    <input type="button" value="Import" onclick="onImport()" />
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
    <input type="button" value="Save" onclick="onSave()" />
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
        <input type="button" onclick="onImportCancel()" value="Cancel" />
    </div>    
</form>
</div>

<div id="afterUpload-dialog" style="display:none" title="Status window">
    <p class="message"></p>
    <div class="buttons-dlg">
        <input type="button" onclick="onAfterUploadOk()" value="OK" />
    </div>
</div>

</body>
</html>