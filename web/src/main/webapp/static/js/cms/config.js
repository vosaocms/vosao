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

var config = '';
var language = null;
var languages = null;
var user = null;
var group = null;
var groups = null;
var users = null;

$(function(){
    $("#import-dialog").dialog({ width: 400, autoOpen: false });
    $("#language-dialog").dialog({ width: 400, autoOpen: false });
    $("#message-dialog").dialog({ width: 400, autoOpen: false });
    $("#user-dialog").dialog({ width: 460, autoOpen: false });
    $("#group-dialog").dialog({ width: 460, autoOpen: false });
    $("#user-group-dialog").dialog({ width: 300, autoOpen: false });
    $("#tabs").tabs();
    $('#upload').ajaxForm(afterUpload);
    initJSONRpc(loadData);
    $('#enableRecaptcha').click(toggleRecaptcha);
    initLanguagesList();
    $('#saveButton').click(onSave);
    $('#exportButton').click(onExport);
    $('#importButton').click(onImport);
    $('#commentsSaveButton').click(onSave);
    $('#restoreButton').click(onRestore);
    $('#importCancelButton').click(onImportCancel);
    $('#okButton').click(onAfterUploadOk);
    
    $('#selectFromListRadio').click(function() { onShowLanguageSelect(true); });
    $('#notInListRadio').click(function() { onShowLanguageSelect(false); });
    $('#selectLanguage').change(onSelectLanguageChange);
    $('#languageSaveButton').click(onLanguageSave);
    $('#languageCancelButton').click(onLanguageCancel);
    $('#addLanguageButton').click(onAddLanguage);
    $('#removeLanguageButton').click(onRemoveLanguage);

    $('#addMessageButton').click(onAddMessage);
    $('#removeMessageButton').click(onRemoveMessage);
    $('#saveMessageDlgButton').click(onMessageSave);
    $('#cancelMessageDlgButton').click(onMessageCancel);

    $('#addUserButton').click(onAddUser);
    $('#removeUserButton').click(onRemoveUser);
    $('#userSaveDlgButton').click(onUserSave);
    $('#userCancelDlgButton').click(onUserCancel);

    $('#addGroupButton').click(onAddGroup);
    $('#removeGroupButton').click(onRemoveGroup);
    $('#groupSaveDlgButton').click(onGroupSave);
    $('#groupCancelDlgButton').click(onGroupCancel);
    $('#userGroupSaveDlgButton').click(onUserGroupSave);
    $('#userGroupCancelDlgButton').click(onUserGroupCancel);
});

function loadData() {
    loadConfig();
	loadLanguages();
	loadUsers();
	loadGroups();
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
	jsonrpc.configService.getConfig(function (r) {
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
    $('#siteUserLoginUrl').val(config.siteUserLoginUrl);
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
        commentsTemplate : $('#commentsTemplate').val(),        
        siteUserLoginUrl : $('#siteUserLoginUrl').val(),        
    });
    jsonrpc.configService.saveConfig(function(r) {
        showServiceMessages(r);
    }, vo); 
}

function onExport() {
    location.href = '/cms/export?type=site';
}

function onRestore() {
	jsonrpc.configService.restoreCommentsTemplate(function (r) {
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
    if (confirm('Are you sure?')) {
    	jsonrpc.languageService.remove(function (r) {
            info(r.message);
            loadLanguages();
        }, javaList(ids));
    }
}

function languageInfo(msg) {
    infoMessage('#language-dialog .messages', msg);
}

function languageError(msg) {
    errorMessage('#language-dialog .messages', msg);
}

function languageErrors(errors) {
    errorMessages('#language-dialog .messages', errors);
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
    	jsonrpc.languageService.save(function (r) {
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
	jsonrpc.languageService.select(function (r) {
        languages = r.list;
        var h = '<table class="form-table"><tr><th></th><th>Code</th><th>Title</th></tr>';
        $.each(r.list, function (i, lang) {
            h += '<tr><td><input type="checkbox" value="' + lang.id 
                + '"/></td><td>' + lang.code + '</td><td>\
                <a href="#" onclick="onLanguageEdit(\'' + lang.id 
                + '\')">' + lang.title + '</a></td></tr>';
        });
        $('#languages').html(h + '</table>');
        $('#languages tr:even').addClass('even');
        loadMessages();
    });
}

function onLanguageEdit(id) {
	jsonrpc.languageService.getById(function (r) {
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
    if (codes.length == 0) {
        info('Nothing selected');
        return;
    }
    if (confirm('Are you sure?')) {
    	jsonrpc.messageService.remove(function (r) {
            info(r.message);
            loadMessages();
        }, javaList(codes));
    }
}

function loadMessages() {
	jsonrpc.messageService.select(function (r) {
        var h = '<table class="form-table"><tr><th></th><th>Code</th>';
        $.each(languages, function (i, lang) {
            h += '<th>' + lang.title + '</th>';
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
        $('#messageBundle tr:even').addClass('even');
	});
}

function onMessageEdit(code) {
	jsonrpc.messageService.selectByCode(function (r) {
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
    jsonrpc.messageService.save(function (r) {
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
    errorMessage('#message-dialog .messages', msg);
}

function messageErrors(errors) {
	errorsMessages('#message-dialog .messages', errors);
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
    if (ids.length == 0) {
        info('Nothing selected');
        return;
    }
    if (confirm('Are you sure?')) {
    	jsonrpc.userService.remove(function (r) {
            info(r.message);
            loadUsers();
        }, javaList(ids));
    }
}

function loadUsers() {
	jsonrpc.userService.select(function (r) {
        var h = '<table class="form-table"><tr><th></th><th>Name</th><th>Email</th><th>Role</th></tr>';
        $.each(r.list, function (i, user) {
            h += '<tr><td><input type="checkbox" value="' + user.id 
                + '"/></td><td>' + user.name + '</td><td>\
                <a href="#" onclick="onUserEdit(' + user.id + ')">' 
                + user.email + '</a></td><td>'
                + getRole(user.role) + '</td></tr>';
        });
        $('#users').html(h + '</table>');
        $('#users tr:even').addClass('even');
    });
}

function getRole(role) {
    if (role == 'ADMIN') return 'Administrator';
    if (role == 'USER') return 'User';
    if (role == 'SITE_USER') return 'Site user';
}

function onUserEdit(id) {
	jsonrpc.userService.getById(function (r) {
        user = r;
        initUserForm();
        $('#user-dialog').dialog('open');
    }, id);
}

function initUserForm() {
	if (user == null) {
        $('#userName').val('');
        $('#userEmail').val('');
        $('#userEmail').removeAttr('disabled');
        $('#userRole').val('');
	}
	else {
        $('#userName').val(user.name);
        $('#userEmail').val(user.email);
        $('#userEmail').attr('disabled', true);
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
    	jsonrpc.userService.save(function (r) {
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
    errorMessage('#user-dialog .messages', msg);
}

function userErrors(errors) {
    errorMessages('#user-dialog .messages', errors);
}

// Group

function loadGroups() {
	jsonrpc.groupService.select(function (r) {
    	groups = idMap(r.list);
        var h = '<table class="form-table"><tr><th></th><th>Name</th><th>Users</th></tr>';
        $.each(r.list, function (i, group) {
        	if (group.name == 'guests') {
        		return;
        	}
        	var users = 'add users';
        	if (group.users.list.length > 0) {
        		users = '';
        		$.each(group.users.list, function (i, user) {
        			users += (i==0 ? '' : ', ') + user.name;
        		});
        	}
        	var editLink = '<a href="#" onclick="onGroupEdit(' + group.id + ')">' 
                + group.name + '</a>';
        	var userGroupLink = '<a href="#" onclick="onEditUserGroup(\'' 
                + group.id + '\')">' + users + '</a>';
            h += '<tr><td><input type="checkbox" value="' + group.id 
                + '"/></td><td>' + editLink + '</td><td>' + userGroupLink 
                + '</td></tr>';
        });
        $('#groups').html(h + '</table>');
        $('#groups tr:even').addClass('even');
    });
}

function onAddGroup() {
    group = null;
    initGroupForm();
    $('#group-dialog').dialog('open');
}

function onRemoveGroup() {
    var ids = [];
    $('#groups input:checked').each(function () {
        ids.push(String(this.value));
    });
    if (ids.length == 0) {
        info('Nothing selected');
        return;
    }
    if (confirm('Are you sure?')) {
    	jsonrpc.groupService.remove(function (r) {
            info(r.message);
            loadGroups();
        }, javaList(ids));
    }
}

function onGroupEdit(id) {
	jsonrpc.groupService.getById(function (r) {
        group = r;
        initGroupForm();
        $('#group-dialog').dialog('open');
    }, id);
}

function initGroupForm() {
	if (group == null) {
        $('#groupName').val('');
	}
	else {
        $('#groupName').val(group.name);
	}
    $('#group-dialog .messages').html('');
}

function validateGroup(vo) {
    var errors = [];
    if (vo.name == '') {
        errors.push('Name is empty');
    }
    return errors;
}

function onGroupSave() {
    var vo = {
    	id : group != null ? String(group.id) : '',
        name : $('#groupName').val(),
    };
    var errors = validateGroup(vo);
    if (errors.length == 0) {
    	jsonrpc.groupService.save(function (r) {
            if (r.result == 'success') {
                $('#group-dialog').dialog('close');
                info(r.message);
                loadGroups();
            }
            else {
                groupErrors(r.messages.list);
            }
        }, javaMap(vo));
    }
    else {
        groupErrors(errors);
    }
}

function onGroupCancel() {
    $('#group-dialog').dialog('close');
}

function groupError(msg) {
    errorMessage('#group-dialog .messages', msg);
}

function groupErrors(errors) {
    errorMessages('#group-dialog .messages', errors);
}

function onEditUserGroup(id) {
	group = groups[id];
	jsonrpc.userService.select(function (r) {
		users = idMap(r.list);
		var groupUsers = idMap(group.users.list);
		var h = '';
		$.each(users, function (i, value) {
			var checked = '';
			if (groupUsers[value.id] != undefined) {
				checked = 'checked = "checked"';
			}
			h += '<div class="form-row"><input type="checkbox" ' + checked 
				+ ' value="' + value.id + '"> ' + value.name + '</div>';
		});
		$('#groupUsers').html(h);
		$('#user-group-dialog').dialog('open');
	});
}

function onUserGroupCancel() {
    $('#user-group-dialog').dialog('close');
}

function onUserGroupSave() {
	var usersId = [];
	$('#user-group-dialog input:checked').each(function () {
		usersId.push(this.value);
	});
	jsonrpc.groupService.setGroupUsers(function (r) {
		showServiceMessages(r);
	    $('#user-group-dialog').dialog('close');
	    loadGroups();
	}, group.id, javaList(usersId));
}
