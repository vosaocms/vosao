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
var languages = null;
var user = null;

$(function(){
    $("#import-dialog").dialog({ width: 400, autoOpen: false });
    $("#language-dialog").dialog({ width: 400, autoOpen: false });
    $("#message-dialog").dialog({ width: 400, autoOpen: false });
    $("#user-dialog").dialog({ width: 400, autoOpen: false });
    $("#tabs").tabs();
    $('#upload').ajaxForm(afterUpload);
    initJSONRpc(loadData);
    $('#enableRecaptcha').click(toggleRecaptcha);
    initLanguagesList();
});

function loadData() {
    loadConfig();
	loadLanguages();
	loadUsers();
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

// Language

function onAddLanguage() {
    language = null;
    $('#languageCode').val('');
    $('#languageTitle').val('');
    $('#language-dialog .message').html('');
	$('#language-dialog').dialog('open');
    $('#languageSelection').show();
    $('#listed-language').show();
    $('#not-listed-language').hide();
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
        languages = r.list;
        var h = '<table class="form-table"><tr><td></td><td>Code</td><td>Title</td></tr>';
        $.each(r.list, function (i, lang) {
            h += '<tr><td><input type="checkbox" value="' + lang.id 
                + '"/></td><td>' + lang.code + '</td><td>\
                <a href="#" onclick="onLanguageEdit(\'' + lang.id 
                + '\')">' + lang.title + '</a></td></tr>';
        });
        $('#languages').html(h + '</table>');
        loadMessages();
    });
}

function onLanguageEdit(id) {
    languageService.getById(function (r) {
        language = r;
        $('#languageCode').val(r.code);
        $('#languageTitle').val(r.title);
        $('#language-dialog .message').html('');
        $('#language-dialog').dialog('open');
        $('#languageSelection').hide();
        $('#listed-language').hide();
        $('#not-listed-language').show();
    }, id);
}

var isoLanguages = {
	aa: 'Afar',
	ab: 'Abkhazian',
	af: 'Afrikaans',
	am: 'Amharic',
	ar: 'Arabic',
	as: 'Assamese',
	ay: 'Aymara',
	az: 'Azerbaijani',

	ba: 'Bashkir',
	be: 'Byelorussian',
	bg: 'Bulgarian',
	bh: 'Bihari',
	bi: 'Bislama',
	bn: 'Bengali; Bangla',
	bo: 'Tibetan',
	br: 'Breton',

	ca: 'Catalan',
	co: 'Corsican',
	cs: 'Czech',
	cy: 'Welsh',

	da: 'Danish',
	de: 'German',
	dz: 'Bhutani',

	el: 'Greek',
	en: 'English',
	eo: 'Esperanto',
	es: 'Spanish',
	et: 'Estonian',
	eu: 'Basque',

	fa: 'Persian',
	fi: 'Finnish',
	fj: 'Fiji',
	fo: 'Faroese',
	fr: 'French',
	fy: 'Frisian',

	ga: 'Irish',
	gd: 'Scots Gaelic',
	gl: 'Galician',
	gn: 'Guarani',
	gu: 'Gujarati',

	ha: 'Hausa',
	he: 'Hebrew (formerly iw)',
	hi: 'Hindi',
	hr: 'Croatian',
	hu: 'Hungarian',
	hy: 'Armenian',

	ia: 'Interlingua',
	id: 'Indonesian (formerly in)',
	ie: 'Interlingue',
	ik: 'Inupiak',
	is: 'Icelandic',
	it: 'Italian',
	iu: 'Inuktitut',

	ja: 'Japanese',
	jw: 'Javanese',

	ka: 'Georgian',
	kk: 'Kazakh',
	kl: 'Greenlandic',
	km: 'Cambodian',
	kn: 'Kannada',
	ko: 'Korean',
	ks: 'Kashmiri',
	ku: 'Kurdish',
	ky: 'Kirghiz',

	la: 'Latin',
	ln: 'Lingala',
	lo: 'Laothian',
	lt: 'Lithuanian',
	lv: 'Latvian, Lettish',

	mg: 'Malagasy',
	mi: 'Maori',
	mk: 'Macedonian',
	ml: 'Malayalam',
	mn: 'Mongolian',
	mo: 'Moldavian',
	mr: 'Marathi',
	ms: 'Malay',
	mt: 'Maltese',
	my: 'Burmese',

	na: 'Nauru',
	ne: 'Nepali',
	nl: 'Dutch',
	no: 'Norwegian',

	oc: 'Occitan',
	om: '(Afan) Oromo',
	or: 'Oriya',

	pa: 'Punjabi',
	pl: 'Polish',
	ps: 'Pashto, Pushto',
	pt: 'Portuguese',

	qu: 'Quechua',

	rm: 'Rhaeto-Romance',
	rn: 'Kirundi',
	ro: 'Romanian',
	ru: 'Russian',
	rw: 'Kinyarwanda',

	sa: 'Sanskrit',
	sd: 'Sindhi',
	sg: 'Sangho',
	sh: 'Serbo-Croatian',
	si: 'Sinhalese',
	sk: 'Slovak',
	sl: 'Slovenian',
	sm: 'Samoan',
	sn: 'Shona',
	so: 'Somali',
	sq: 'Albanian',
	sr: 'Serbian',
	ss: 'Siswati',
	st: 'Sesotho',
	su: 'Sundanese',
	sv: 'Swedish',
	sw: 'Swahili',

	ta: 'Tamil',
	te: 'Telugu',
	tg: 'Tajik',
	th: 'Thai',
	ti: 'Tigrinya',
	tk: 'Turkmen',
	tl: 'Tagalog',
	tn: 'Setswana',
	to: 'Tonga',
	tr: 'Turkish',
	ts: 'Tsonga',
	tt: 'Tatar',
	tw: 'Twi',

	ug: 'Uighur',
	uk: 'Ukrainian',
	ur: 'Urdu',
	uz: 'Uzbek',

	vi: 'Vietnamese',
	vo: 'Volapuk',

	wo: 'Wolof',

	xh: 'Xhosa',

	yi: 'Yiddish (formerly ji)',
	yo: 'Yoruba',

	za: 'Zhuang',
	zh: 'Chinese',
	zu: 'Zulu',
};

function initLanguagesList() {
    var h = '';
	for (var code in isoLanguages) {
		h += '<option value="' + code + '">' + isoLanguages[code] 
		    + '</option>\n';
	}
	$('#selectLanguage').html(h);
}

function onShowLanguageSelect(show) {
	if (show) {
		$('#listed-language').show();
        $('#not-listed-language').hide();
	}
	else {
        $('#listed-language').hide();
        $('#not-listed-language').show();
	}
}

function onSelectLanguageChange() {
	var code = $('#selectLanguage').val();
	$('#languageCode').val(code);
    $('#languageTitle').val(isoLanguages[code]);
}

// Message 

function onAddMessage() {
    var h = '';
	$.each(languages, function (i, lang) {
        h += '<div class="form-row"><label>' + lang.title 
            + '</label><input type="text" id="message_' + lang.code + '" /></div>';
    });
    $('#messageCode').val('');
	$('#messagesInput').html(h);
	$('#message-dialog').dialog('open');
}

function onRemoveMessage() {
    var codes = [];
    $('#messageBundle input:checked').each(function () {
        codes.push(this.value);
    });
    messageService.remove(function (r) {
        info(r.message);
        loadMessages();
    }, javaList(codes));
}

function loadMessages() {
	messageService.select(function (r) {
        var h = '<table class="form-table"><tr><td><td>Code</td>';
        $.each(languages, function (i, lang) {
            h += '<td>' + lang.title + '</td>';
        });
        h += '</tr>';
        $.each(r.list, function (i, msg) {
            h += '<tr><td><input type="checkbox" value="' + msg.code + '"/></td>\
                <td><a href="#" onclick="onMessageEdit(\'' + msg.code + '\')">'
                + msg.code + '</a></td>';
            $.each(languages, function (i, lang) {
                h += '<td>' + msg.values.map[lang.code] + '</td>';
            });
            h += '</tr>';
        });
        $('#messageBundle').html(h + '</table>');
	});
}

function onMessageEdit(code) {
    messageService.selectByCode(function (r) {
        onAddMessage();
        $('#messageCode').val(code);
        $.each(r.list, function (i, msg) {
            $('#message_' + msg.languageCode).val(msg.value);
        });
    }, code);
}

function onMessageSave() {
    var vo = {code : $('#messageCode').val() };
    $.each(languages, function (i, lang) {
        vo[lang.code] = $('#message_' + lang.code).val();
    });
    messageService.save(function (r) {
        if (r.result == 'success') {
            loadMessages();
            $('#message-dialog').dialog('close');
        }
        else {
            messageErrors(r.messages.list);
        }
    }, javaMap(vo));
    
}

function onMessageCancel() {
    $('#message-dialog').dialog('close');
}

function messageError(msg) {
    $('#message-dialog .messages').html('<ul><li class="error-msg">' 
            + msg + '</li></ul>');
}

function messageErrors(errors) {
    var h = '<ul>';
    $.each(errors, function (i, msg) {
        h += '<li class="error-msg">' + msg + '</li>';
    });
    $('#message-dialog .messages').html(h + '</ul>');
}

// User 

function onAddUser() {
    user = null;
    initUserForm();
    $('#user-dialog').dialog('open');
}

function onRemoveUser() {
    var ids = [];
    $('#users input:checked').each(function () {
        ids.push(String(this.value));
    });
    userService.remove(function (r) {
        info(r.message);
        loadUsers();
    }, javaList(ids));
}

function loadUsers() {
    userService.select(function (r) {
        var h = '<table class="form-table"><tr><td></td><td>Name</td><td>Email</td><td>Role</td></tr>';
        $.each(r.list, function (i, user) {
            h += '<tr><td><input type="checkbox" value="' + user.id 
                + '"/></td><td>' + user.name + '</td><td>\
                <a href="#" onclick="onUserEdit(' + user.id + ')">' 
                + user.email + '</a></td><td>'
                + getRole(user.roleString) + '</td></tr>';
        });
        $('#users').html(h + '</table>');
    });
}

function getRole(role) {
    return role == 'ADMIN' ? 'Administrator' : 'User';
}

function onUserEdit(id) {
    userService.getById(function (r) {
        user = r;
        initUserForm();
        $('#user-dialog').dialog('open');
    }, id);
}

function initUserForm() {
	if (user == null) {
        $('#userName').val('');
        $('#userEmail').val('');
        $('#userRole').val('');
	}
	else {
        $('#userName').val(user.name);
        $('#userEmail').val(user.email);
        $('#userRole').val(user.roleString);
	}
    $('#userPassword1').val('');
    $('#userPassword2').val('');
    $('#user-dialog .messages').html('');
}

function validateUser(vo) {
    var errors = [];
    if (vo.email == '') {
        errors.push('Email is empty');
    }
    if (vo.password1 != vo.password2) {
        errors.push('Passwords don\'t match');
    }
    return errors;
}

function onUserSave() {
    var vo = {
    	id : user != null ? String(user.id) : '',
        name : $('#userName').val(),
        email : $('#userEmail').val(),
        role : $('#userRole').val(),
        password : $('#userPassword1').val(),
        password1 : $('#userPassword1').val(),
        password2 : $('#userPassword2').val(),
    };
    var errors = validateUser(vo);
    if (errors.length == 0) {
        userService.save(function (r) {
            if (r.result == 'success') {
                $('#user-dialog').dialog('close');
                info(r.message);
                loadUsers();
            }
            else {
                userErrors(r.messages.list);
            }
        }, javaMap(vo));
    }
    else {
        userErrors(errors);
    }
}

function onUserCancel() {
    $('#user-dialog').dialog('close');
}

function userError(msg) {
    $('#user-dialog .messages').html('<ul><li class="error-msg">' 
            + msg + '</li></ul>');
}

function userErrors(errors) {
    var h = '<ul>';
    $.each(errors, function (i, msg) {
        h += '<li class="error-msg">' + msg + '</li>';
    });
    $('#user-dialog .messages').html(h + '</ul>');
}

</script>

</head>
<body>

<div id="tabs">

<ul>
    <li><a href="#tab-1">Site configuration</a></li>
    <li><a href="#tab-2">Comments</a></li>
    <li><a href="#tab-3">Languages</a></li>
    <li><a href="#tab-4">Message bundle</a></li>
    <li><a href="#tab-5">Users</a></li>
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

<div id="tab-4">
    <div id="messageBundle"> </div>
    <div class="buttons">
        <input type="button" value="Add" onclick="onAddMessage()" />
        <input type="button" value="Remove" onclick="onRemoveMessage()" />
    </div>
</div>

<div id="tab-5">
    <div id="users"> </div>
    <div class="buttons">
        <input type="button" value="Add" onclick="onAddUser()" />
        <input type="button" value="Remove" onclick="onRemoveUser()" />
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
    <div id="languageSelection" style="padding-bottom: 10px;">
        <input type="radio" name="select" checked="checked" 
            onclick="onShowLanguageSelect(true)"/> Select from list
        <input type="radio" name="select" onclick="onShowLanguageSelect(false)"/>
            Not in the list
    </div>        
    <div id="listed-language">
        <select id="selectLanguage" onchange="onSelectLanguageChange()"></select>
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
        <input type="button" onclick="onLanguageSave()" value="Save" />
        <input type="button" onclick="onLanguageCancel()" value="Cancel" />
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
        <input type="button" onclick="onMessageSave()" value="Save" />
        <input type="button" onclick="onMessageCancel()" value="Cancel" />
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
        <input type="button" onclick="onUserSave()" value="Save" />
        <input type="button" onclick="onUserCancel()" value="Cancel" />
    </div>
</div>


</body>
</html>
