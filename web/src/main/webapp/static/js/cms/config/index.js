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
var exportTimer = null;
var clockTimer = null;
var clockSeconds = 0;
var exportFilename = null;
var exportType = null;

$(function(){
    $("#import-dialog").dialog({ width: 400, autoOpen: false });
    $("#export-dialog").dialog({ width: 400, autoOpen: false });
    $('#upload').ajaxForm(afterUpload);
    Vosao.initJSONRpc(loadData);
    $('#enableRecaptcha').click(toggleRecaptcha);
    $('#configForm').submit(function() {onSave(); return false;});
    $('#exportButton').click(onExport);
    $('#importButton').click(onImport);
    $('#importCancelButton').click(onImportCancel);
    $('#resetButton').click(onReset);
    $('#cacheResetButton').click(onCacheReset);
    $('#reindexButton').click(onReindex);
    $('#okForm').submit(function() {onAfterUploadOk(); return false;});
    $('#exportForm').submit(function() {onStartExport(); return false;});
    $('#exportCancelButton').click(onExportCancel);
    $('ul.ui-tabs-nav li:nth-child(1)').addClass('ui-state-active')
			.removeClass('ui-state-default');
    $('#enablePicasa').click(togglePicasa);
});

function loadData() {
    loadConfig();
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

function togglePicasa() {
    var picasa = $('#enablePicasa:checked').size() > 0;
    if (picasa) {
        $('#picasa').show();
    }
    else {
        $('#picasa').hide();
    }
}

function afterUpload(data) {
    var s = data.split('::');
    var result = s[1];
    var msg = s[2]; 
    if (result == 'success') {
        msg = 'Success. File was successfully saved for import. Please wait the import process can take long time.';
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
	Vosao.jsonrpc.configService.getConfig(function (r) {
        config = r;
        initFormFields();
    }); 
}

function initFormFields() {
	$('#version').html(config.version);
	$('#googleAnalyticsId').val(config.googleAnalyticsId);
    $('#siteEmail').val(config.siteEmail);
    $('#siteDomain').val(config.siteDomain);
    $('#enableRecaptcha').each(function () {this.checked = config.enableRecaptcha});
    $('#recaptchaPublicKey').val(config.recaptchaPublicKey);
    $('#recaptchaPrivateKey').val(config.recaptchaPrivateKey);
    toggleRecaptcha();
    $('#editExt').val(config.editExt);
    $('#siteUserLoginUrl').val(config.siteUserLoginUrl);
    $('#enablePicasa').each(function () {this.checked = config.enablePicasa});
    $('#picasaUser').val(config.picasaUser);
    $('#picasaPassword').val(config.picasaPassword);
    togglePicasa();
}

function onSave() {
    var vo = Vosao.javaMap({
    	googleAnalyticsId : $('#googleAnalyticsId').val(),
        siteEmail : $('#siteEmail').val(),
        siteDomain : $('#siteDomain').val(),
        enableRecaptcha : String($('#enableRecaptcha:checked').size() > 0),
        recaptchaPublicKey : $('#recaptchaPublicKey').val(),
        recaptchaPrivateKey : $('#recaptchaPrivateKey').val(),
        enablePicasa : String($('#enablePicasa:checked').size() > 0),
        picasaUser : $('#picasaUser').val(),
        picasaPassword : $('#picasaPassword').val(),
        editExt : $('#editExt').val(),
        siteUserLoginUrl : $('#siteUserLoginUrl').val()        
    });
    Vosao.jsonrpc.configService.saveConfig(function(r) {
    	Vosao.showServiceMessages(r);
    }, vo); 
}

function onExport() {
    $("#export-dialog").dialog("open");
}

function onExportCancel() {
    $("#export-dialog").dialog("close");
	clearInterval(exportTimer);
	clearInterval(clockTimer);
}

function onStartExport() {
	$('#exportDialogButton').attr('disabled', true);
    clockSeconds = 0;
    showClock();
    exportType = $('input[name=exportType]:checked').val();
    Vosao.jsonrpc.configService.startExportTask(function(r) {
    	if (r.result == 'success') {
    		exportFilename = r.message;
    	    Vosao.infoMessage('#exportInfo', 'Creating export file...');
            exportTimer = setInterval(checkExport, 10 * 1000);
            clockTimer = setInterval(showClock, 1000);
    	}
    	else {
    		Vosao.showServiceMessages(r);
    	}
    }, exportType);
}

function checkExport() {
	Vosao.jsonrpc.configService.isExportTaskFinished(function(r) {
		if (r) {
			clearInterval(exportTimer);
			clearInterval(clockTimer);
			$("#export-dialog").dialog("close");
			$('#exportDialogButton').attr('disabled', false);
			location.href = '/file/tmp/' + exportFilename;
		}
	}, exportType);
}

function showClock() {
	$('#timer').html(clockSeconds++ + ' sec.');
}

function onReset() {
	if (confirm('Are you going to REMOVE ALL DATA from application?')) {
		if (confirm("ALL DATA WILL BE LOST! Are you shure?")) {
			Vosao.jsonrpc.configService.reset(function(r) {
				Vosao.showServiceMessages(r);
				if (r.result == 'success') {
					location.href = '/';
				}
			});
		}
	}
}

function onReindex() {
	if (confirm('Are you shure?')) {
		Vosao.jsonrpc.configService.reindex(function(r) {
			Vosao.showServiceMessages(r);
		});
	}
}

function onCacheReset() {
	if (confirm('Are you shure?')) {
		Vosao.jsonrpc.configService.cacheReset(function(r) {
			Vosao.showServiceMessages(r);
		});
	}
}
