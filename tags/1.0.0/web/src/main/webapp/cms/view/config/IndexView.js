// file IndexView.js
/*
 Vosao CMS. Simple CMS for Google App Engine.
 
 Copyright (C) 2009-2011 Vosao development team.

 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA

 email: vosao.dev@gmail.com
*/

define(['text!template/config/index.html'],
function(tmpl) {
	
	console.log('Loading IndexView.js');
	
	var config = '';
	var exportTimer = null;
	var clockTimer = null;
	var clockSeconds = 0;
	var exportFilename = null;
	var exportType = null;
	var timezones = null;
	var languages = null;

	function postRender() {
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
	    $('#loadDefaultSiteButton').click(onLoadDefaultSite);
	    $('#okForm').submit(function() {onAfterUploadOk(); return false;});
	    $('#exportForm').submit(function() {onStartExport(); return false;});
	    $('#exportCancelButton').click(onExportCancel);
	    $('ul.ui-tabs-nav li:nth-child(1)').addClass('ui-state-active')
	    		.addClass('ui-tabs-selected')
				.removeClass('ui-state-default');
	    $('#enablePicasa').click(togglePicasa);
	    $('#statButton').click(onStat);
	    $('#statOKButton').click(onOKStat);
	}

	function loadData() {
	    loadConfig();
	    loadTimezones();
	    loadLanguages();
	}

	function loadTimezones() {
		Vosao.jsonrpc.userService.getTimezones(function (r) {
	        timezones = r.list;
	        showTimezones();
	    });
	}

	function loadLanguages() {
		Vosao.jsonrpc.languageService.select(function (r) {
	        languages = r.list;
	        showLanguages();
	    });
	}

	function showTimezones() {
		var h = '';
		$.each(timezones, function(i, value) {
			h += '<option value="' + value + '">' + value + '</option>';
		});
		$('#timezone').html(h);
	    $('#timezone').val(config.defaultTimezone);
	}

	function showLanguages() {
		var h = '';
		$.each(languages, function(i, value) {
			h += '<option value="' + value.code + '" ' + '>' 
				+ value.title + '</option>';
		});
		$('#language').html(h);
	    $('#language').val(config.defaultLanguage);
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
	        msg = messages('config.saved_for_import');
	    }
	    else {
	        msg = messages('error') + ". " + msg;
	    }   
	    $("#import-dialog").dialog("close");
	    $("#afterUpload-dialog .message").text(msg);
	    $("#afterUpload-dialog").dialog('open');
	}

	function onImport() {
	    $("#import-dialog").dialog("open");
	}

	function onImportCancel() {
	    $("#import-dialog").dialog("close");
	}

	function onAfterUploadOk() {
		$('#afterUpload-dialog').dialog('close');
	    loadData();
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
	    $('#site404Url').val(config.site404Url);
	    $('#enablePicasa').each(function () {this.checked = config.enablePicasa});
	    $('#enableCkeditor').each(function () {this.checked = config.enableCkeditor});
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
	        enableCkeditor : String($('#enableCkeditor:checked').size() > 0),
	        picasaUser : $('#picasaUser').val(),
	        picasaPassword : $('#picasaPassword').val(),
	        editExt : $('#editExt').val(),
	        defaultTimezone : $('#timezone').val(),
	        defaultLanguage : $('#language').val(),
	        siteUserLoginUrl : $('#siteUserLoginUrl').val(),        
	        site404Url : $('#site404Url').val()        
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
	    	    Vosao.infoMessage('#exportInfo', messages('creating_export_file'));
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
		if (confirm(messages('config.reset_warning1'))) {
			if (confirm(messages('config.reset_warning2'))) {
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
		if (confirm(messages('are_you_sure'))) {
			Vosao.jsonrpc.configService.reindex(function(r) {
				Vosao.showServiceMessages(r);
			});
		}
	}

	function onCacheReset() {
		if (confirm(messages('are_you_sure'))) {
			Vosao.jsonrpc.configService.cacheReset(function(r) {
				Vosao.showServiceMessages(r);
			});
		}
	}

	function onLoadDefaultSite() {
		if (confirm(messages('are_you_sure'))) {
			Vosao.jsonrpc.configService.loadDefaultSite(function(r) {
				Vosao.showServiceMessages(r);
			});
		}
	}

	function onStat() {
		Vosao.jsonrpc.configService.getSiteStat(function(r) {
			$('#statPages').text(r.pages);
			$('#statPagePermissions').text(r.pagePermissions);
			$('#statStructures').text(r.structures);
			$('#statStructureTemplates').text(r.structureTemplates);
			$('#statTemplates').text(r.templates);
			$('#statFolders').text(r.folders);
			$('#statFolderPermissions').text(r.folderPermissions);
			$('#statFiles').text(r.files);
			$('#statLanguages').text(r.languages);
			$('#statMessages').text(r.messages);
			$('#statUsers').text(r.users);
			$('#statGroups').text(r.groups);
			$('#statTags').text(r.tags);
			$('#statTotal').text(r.total);
			$('#stat-dialog').dialog('open');
		});
	}

	function onOKStat() {
		$('#stat-dialog').dialog('close');
	}
	
	return Backbone.View.extend({
		
		el: $('#tab-1'),
		
		render: function() {
			this.el = $('#tab-1');
			this.el.html(_.template(tmpl, {messages:messages}));
			postRender();
		},
		
		remove: function() {
		    $("#import-dialog, #afterUpload-dialog, #export-dialog, #stat-dialog")
		    		.dialog('destroy').remove();
		    
			this.el.html('');
		}
		
	});
	
});