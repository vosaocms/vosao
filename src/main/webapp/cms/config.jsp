<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<html>
<head>
    <title>Site configuration</title>

    <script src="/static/js/jquery.form.js" language="javascript"></script>
    <link rel="stylesheet" href="/static/css/config.css" type="text/css" />

<script type="text/javascript">

var config = '';
var language = null;

$(function(){
    $("#import-dialog").dialog({ width: 400, autoOpen: false });
    $("#language-dialog").dialog({ width: 400, autoOpen: false });
    $("#tabs").tabs();
    $('#upload').ajaxForm(afterUpload);
    initJSONRpc(loadData);
    $('#enableRecaptcha').click(toggleRecaptcha);
});

function loadData() {
    loadConfig();
	loadLanguages();
}

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

function onRestore() {
    configService.restoreCommentsTemplate(function (r) {
        showServiceMessages(r);
        loadConfig();
    });
}

function onAddLanguage() {
    language = null;
    $('#languageCode').val('');
    $('#languageTitle').val('');
    $('#language-dialog .message').html('');
	$('#language-dialog').dialog('open');
}

function onRemoveLanguage() {
    var ids = [];
    $('#languages input:checked').each(function () {
        ids.push(this.value);
    });
    if (ids.length == 0) {
        info('Nothing selected');
        return;
    }
    languageService.remove(function (r) {
        info(r.message);
        loadLanguages();
    }, javaList(ids));
}

function languageInfo(msg) {
    $('#language-dialog .messages').html('<ul><li class="info-msg">' 
            + msg + '</li></ul>');
}

function languageError(msg) {
    $('#language-dialog .messages').html('<ul><li class="error-msg">' 
    	    + msg + '</li></ul>');
}

function languageErrors(errors) {
    var h = '<ul>';
    $.each(errors, function (i, msg) {
        h += '<li class="error-msg">' + msg + '</li>';
    });
    $('#language-dialog .messages').html(h + '</ul>');
}

function languageValidate(vo) {
    var errors = [];
	if (vo.code == '') {
	    errors.push('Code is empty');
	}
    if (vo.title == '') {
        errors.push('Title is empty');
    }
    return errors;
}

function onLanguageSave() {
    var vo = {
    	id : language != null ? language.id : '',
       	code : $('#languageCode').val(),
        title : $('#languageTitle').val(),
    };
    var errors = languageValidate(vo);
    if (errors.length == 0) {
        languageService.save(function (r) {
            if (r.result == 'success') {
                $('#language-dialog').dialog('close');
                loadLanguages();
            }
            else {
                languageErrors(r.messages.list);
            }
        }, javaMap(vo));
    }	
    else {
        languageErrors(errors);
    }       
}

function onLanguageCancel() {
    $('#language-dialog').dialog('close');
}

function loadLanguages() {
    languageService.select(function (r) {
        var h = '<table class="form-table"><tr><td></td><td>Code</td><td>Title</td></tr>';
        $.each(r.list, function (i, lang) {
            h += '<tr><td><input type="checkbox" value="' + lang.id 
                + '"/></td><td>' + lang.code + '</td><td>\
                <a href="#" onclick="onLanguageEdit(\'' + lang.id 
                + '\')">' + lang.title + '</a></td></tr>';
        });
        $('#languages').html(h + '</table>');
    });
}

function onLanguageEdit(id) {
    languageService.getById(function (r) {
        language = r;
        $('#languageCode').val(r.code);
        $('#languageTitle').val(r.title);
        $('#language-dialog .message').html('');
        $('#language-dialog').dialog('open');
    }, id);
}

</script>

</head>
<body>

<div id="tabs">

<ul>
    <li><a href="#tab-1">Site configuration</a></li>
    <li><a href="#tab-2">Comments</a></li>
    <li><a href="#tab-3">Languages</a></li>
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
    <input type="button" value="Restore default" onclick="onRestore()" />
</div>

</div>

<div id="tab-3">

<div id="languages"> </div>

<div class="buttons">
    <input type="button" value="Add" onclick="onAddLanguage()" />
    <input type="button" value="Remove" onclick="onRemoveLanguage()" />
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

<div id="language-dialog" style="display:none" title="Site language">
    <div class="messages"> </div>
    <div class="form-row">
        <label>Language <a href="http://www.loc.gov/standards/iso639-2/php/English_list.php">iso639-2</a> 3 letter code</label>
        <input id="languageCode" type="text"/>
    </div>
    <div class="form-row">
        <label>Language title</label>
        <input id="languageTitle" type="text"/>
    </div>
    <div class="buttons-dlg">
        <input type="button" onclick="onLanguageSave()" value="Save" />
        <input type="button" onclick="onLanguageCancel()" value="Cancel" />
    </div>
</div>



</body>
</html>