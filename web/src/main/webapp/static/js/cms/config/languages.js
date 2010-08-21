/**
 * Vosao CMS. Simple CMS for Google App Engine.
 * 
 * Copyright (C) 2009-2010 Vosao development team.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * email: vosao.dev@gmail.com
 */

var language = null;
var languages = null;

$(function(){
    $("#language-dialog").dialog({ width: 400, autoOpen: false });
    Vosao.initJSONRpc(loadData);
    initLanguagesList();
    $('#selectFromListRadio').click(function() { onShowLanguageSelect(true); });
    $('#notInListRadio').click(function() { onShowLanguageSelect(false); });
    $('#selectLanguage').change(onSelectLanguageChange);
    $('#languageForm').submit(function() {onLanguageSave(); return false});
    $('#languageCancelButton').click(onLanguageCancel);
    $('#addLanguageButton').click(onAddLanguage);
    $('#removeLanguageButton').click(onRemoveLanguage);
    $('ul.ui-tabs-nav li:nth-child(3)').addClass('ui-state-active')
    		.addClass('ui-tabs-selected')
			.removeClass('ui-state-default');
});

function loadData() {
	loadLanguages();
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
    	Vosao.info(messages('nothing_selected'));
        return;
    }
    if (confirm(messages('are_you_sure'))) {
    	Vosao.jsonrpc.languageService.remove(function (r) {
    		Vosao.info(r.message);
            loadLanguages();
        }, Vosao.javaList(ids));
    }
}

function languageInfo(msg) {
	Vosao.infoMessage('#language-dialog .messages', msg);
}

function languageError(msg) {
	Vosao.errorMessage('#language-dialog .messages', msg);
}

function languageErrors(errors) {
	Vosao.errorMessages('#language-dialog .messages', errors);
}

function languageValidate(vo) {
    var errors = [];
	if (vo.code == '') {
	    errors.push(messages('config.code_is_empty'));
	}
    if (vo.title == '') {
        errors.push(messages('title_is_empty'));
    }
    return errors;
}

function onLanguageSave() {
    var vo = {
    	id : language != null ? String(language.id) : '',
       	code : $('#languageCode').val(),
        title : $('#languageTitle').val()
    };
    var errors = languageValidate(vo);
    if (errors.length == 0) {
    	Vosao.jsonrpc.languageService.save(function (r) {
            if (r.result == 'success') {
                $('#language-dialog').dialog('close');
                loadLanguages();
            }
            else {
                languageErrors(r.messages.list);
            }
        }, Vosao.javaMap(vo));
    }	
    else {
        languageErrors(errors);
    }       
}

function onLanguageCancel() {
    $('#language-dialog').dialog('close');
}

function loadLanguages() {
	Vosao.jsonrpc.languageService.select(function (r) {
        languages = r.list;
        var h = '<table class="form-table"><tr><th></th><th>' 
        	+ messages('code') + '</th><th>' + messages('title') + '</th></tr>';
        $.each(r.list, function (i, lang) {
            h += '<tr><td><input type="checkbox" value="' + lang.id 
                + '"/></td><td>' + lang.code + '</td><td>\
                <a href="#" onclick="onLanguageEdit(' + lang.id 
                + ')">' + lang.title + '</a></td></tr>';
        });
        $('#languages').html(h + '</table>');
        $('#languages tr:even').addClass('even');
    });
}

function onLanguageEdit(id) {
	Vosao.jsonrpc.languageService.getById(function (r) {
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
	bs: 'Bosnian',
	
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
