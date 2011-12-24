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

define(['text!template/page/index.html',
        'view/page/context', 'view/page/version', 'view/page/breadcrumbs',
        'jquery.treeview'], 
function(indexHtml, ctx, version, breadcrumbs) {
	
	console.log('Loading IndexView.js');
	
	var structureTemplates = null;
	 
	function postRender() {
		ctx.loadData = loadData;
		ctx.editMode = ctx.pageId != '';
	    $("#tag-dialog").dialog({ width: 400, autoOpen: false });
	    $(".datepicker").datepicker({dateFormat:'dd.mm.yy'});
	    Vosao.initJSONRpc(loadData);
	    // hover states on the link buttons
	    $('a.button').hover(
	     	function() { $(this).addClass('ui-state-hover'); },
	       	function() { $(this).removeClass('ui-state-hover'); }
	    ); 
	    version.initVersionDialog();
	    $('#title').change(onTitleChange);
	    $('#pageType').change(onPageTypeChange);
	    $('#structure').change(onStructureChange);
	    $('#pageForm').submit(function() {onPageUpdate(); return false;});
	    $('#pagePreview').click(onPagePreview);
	    $('#pageCancelButton').click(onPageCancel);
	    $('#approveButton').click(onPageApprove);
	    $('ul.ui-tabs-nav li:nth-child(1)').addClass('ui-state-active')
	    		.addClass('ui-tabs-selected')
	    		.removeClass('ui-state-default');
	    $('#metadata').click(function() {
	    	$('#meta').toggle();
	    });
	    $('#addTag').click(onAddTag);
	    $('#cached').click(function() { $('#dependenciesDiv').toggle(); });
	}

	function loadData() {
		Vosao.jsonrpc.pageService.getPageRequest(function(r) {
			ctx.pageRequest = r;
			ctx.page = ctx.pageRequest.page;
			loadTemplates();
			loadStructures();
			loadPage();
			breadcrumbs.breadcrumbsShow();
		}, ctx.pageId, ctx.pageParentUrl);
	}

	function checkDefault() {
		if (ctx.page != null && ctx.page.friendlyURL.endsWith('/_default')) {
			ctx.isDefault = true;
			$('.securityTab, .commentsTab, .childrenTab, #approveOnPageSaveDiv'
				+ ', #pagePreview, #versions, #tagsDiv').hide();
			$('#title, #friendlyUrl').attr('disabled', true);
		}
	}

	function callLoadPage() {
		Vosao.jsonrpc.pageService.getPageRequest(function(r) {
			ctx.pageRequest = r;
			ctx.page = ctx.pageRequest.page;
			ctx.editMode = ctx.pageId != null;
			loadPage();
		}, ctx.pageId, ctx.pageParentUrl);
	}

	function loadPage() {
		if (ctx.editMode) {
			ctx.pageId = String(ctx.page.id);
			ctx.pageParentUrl = ctx.page.parentUrl;
			version.loadVersions();
			loadLanguages();
			loadContents();
			showTags();
		} else {
			ctx.pages['1'] = ctx.page;
		}
		initPageForm();
		loadPagePermission();
	}

	function loadTemplates() {
		var r = ctx.pageRequest.templates;
		var html = '';
	    $.each(r.list, function (n,value) {
	        html += '<option value="' + value.id + '">' 
	            + value.title + '</option>';
	    });
	    $('#templates').html(html);
	    if (ctx.page.id != null) {
	        $('#templates').val(ctx.page.template);
	    }
	    else {
	        $('#templates').val($.cookie("page_template"));
	    }
	}

	function initPageForm() {
		var urlEnd = ctx.pageParentUrl == '/' ? '' : '/';
		$('#title').val(ctx.page.title);
		$('#titleDiv').hide();
		if (ctx.page.parentUrl == '' || ctx.page.parentUrl == null) {
			$('#friendlyUrl').hide();
			$('#friendlyUrl').val('');
			$('#parentFriendlyUrl').html('/');
		} else {
			$('#friendlyUrl').show();
			$('#friendlyUrl').val(ctx.page.pageFriendlyURL);
			$('#parentFriendlyUrl').html(ctx.page.parentFriendlyURL + urlEnd);
		}
	    if (ctx.pageRequest.children.list.length > 0) {
	     	$('#parentFriendlyUrl').hide();
	       	$('#friendlyUrl').hide();
	       	$('#friendlyUrlSpan').html(ctx.page.friendlyURL);
	    }
		$('#pageType').val(ctx.page.pageTypeString);
		$('#publishDate').val(ctx.page.publishDateString);
		$('#publishTime').val(ctx.page.publishTimeString);
		$('#endPublishDate').val(ctx.page.endPublishDateString);
		$('#endPublishTime').val(ctx.page.endPublishTimeString);
		$('#commentsEnabled').each(function() {
			this.checked = ctx.page.commentsEnabled;
		});
		$('#searchable').each(function() {
			this.checked = ctx.page.searchable;
		});
		$('#velocityProcessing').each(function() {
			this.checked = ctx.page.velocityProcessing;
		});
		$('#wikiProcessing').each(function() {
			this.checked = ctx.page.wikiProcessing;
		});
		$('#enableCkeditor').each(function() {
			this.checked = ctx.page.enableCkeditor;
		});
		$('#skipPostProcessing').each(function() {
			this.checked = ctx.page.skipPostProcessing;
		});
		$('#cached').each(function() {
			this.checked = ctx.page.cached;
		});
		$('#restful').each(function() {
			this.checked = ctx.page.restful;
		});
		$('#templates').val(ctx.page.template);
		$('#pageState').html(ctx.page.stateString == 'EDIT' ? 
				messages('edit') : messages('approved'));
		$('#pageCreateDate').html(ctx.page.createDateTimeString);
		$('#pageModDate').html(ctx.page.modDateTimeString);
		$('#pageCreateUser').html(ctx.page.createUserEmail);
		$('#pageModUser').html(ctx.page.modUserEmail);
		$('#keywords').val(ctx.page.keywords);
		$('#description').val(ctx.page.description);
		$('#headHtml').val(ctx.page.headHtml);
		$('#dependencies').val(ctx.pageRequest.dependencies);
		$('#contentType').val(ctx.page.contentType);
		if (ctx.page.cached) {
			$('#dependenciesDiv').show();
		}
		else {
			$('#dependenciesDiv').hide();
		}
		if (ctx.page.id != null) {
			$('.contentTab').show();
			$('.childrenTab').show();
			$('.commentsTab').show();
			$('.securityTab').show();
			$('#pagePreview').show();
			$('#versions').show();
			$('#tagsDiv').show();
		} else {
			$('.contentTab').hide();
			$('.childrenTab').hide();
			$('.commentsTab').hide();
			$('.securityTab').hide();
			$('#pagePreview').hide();
			$('#versions').hide();
			$('#tagsDiv').hide();
		}
		checkDefault();
		onPageTypeChange();
	}

	function getPublishDatetime() {
		return Vosao.strip($('#publishDate').val()) 
			+ ' ' + Vosao.strip($('#publishTime').val()) + ':00';	
	}

	function getEndPublishDatetime() {
		if ($('#endPublishDate').val()) {
			return Vosao.strip($('#endPublishDate').val()) 
				+ ' ' + Vosao.strip($('#endPublishTime').val()) + ':00';
		}
		return '';
	}

	function onPageUpdate() {
		var pageVO = Vosao.javaMap( {
			id : ctx.pageId,
			titles : getTitles(),
			friendlyUrl : $('#parentFriendlyUrl').text() + $('#friendlyUrl').val(),
			publishDate : getPublishDatetime(),
			endPublishDate : getEndPublishDatetime(),
			commentsEnabled : String($('#commentsEnabled:checked').size() > 0),
			searchable : String($('#searchable:checked').size() > 0),
			velocityProcessing : String($('#velocityProcessing:checked').size() > 0),
			wikiProcessing : String($('#wikiProcessing:checked').size() > 0),
			enableCkeditor : String($('#enableCkeditor:checked').size() > 0),
			skipPostProcessing : String($('#skipPostProcessing:checked').size() > 0),
			cached : String($('#cached:checked').size() > 0),
			restful : String($('#restful:checked').size() > 0),
			template : $('#templates option:selected').val(),
			approve : String($('#approveOnPageSave:checked, #approveOnContentSave:checked').size() > 0),
			pageType: $('#pageType').val(),
			structureId: $('#structure').val(),
			structureTemplateId: $('#structureTemplate').val(),
			keywords: $('#keywords').val(),
			description: $('#description').val(),
			dependencies: $('#dependencies').val(),
			contentType: $('#contentType').val(),
			headHtml: $('#headHtml').val()
		});
		$.cookie("page_template", pageVO.map.template, {path:'/', expires: 10});
		Vosao.jsonrpc.pageService.savePage(function(r) {
			if (r.result == 'success') {
				if (ctx.editMode) {
					location.href = '#pages';
				}
				ctx.pageId = r.message;
				Vosao.info(messages('page.success_save'));
				location.href = '#page/content/' + ctx.pageId;
			} else {
				Vosao.showServiceMessages(r);
			}
		}, pageVO);
	}
	    
	function onTitleChange() {
		if (ctx.editMode) {
			return;
		}
		var url = $("#friendlyUrl").val();
		var title = $("#title").val();
		if (url == '') {
			$("#friendlyUrl").val(Vosao.urlFromTitle(title));
		}
	}

	function onPageTypeChange() {
		if ($('#pageType').val() == 'SIMPLE') {
			$('#structuredControls').hide();
		}
		if ($('#pageType').val() == 'STRUCTURED') {
			$('#structuredControls').show();
			if (ctx.page != null) {
				$('#structure').val(ctx.page.structureId);
			}
			onStructureChange();
		}
	}

	function onPagePreview() {
		var url = ctx.page.friendlyURL + '?language=' + ctx.currentLanguage 
			+ '&version=' + ctx.page.version;
		window.open(url, "preview");
	}

	function onPageCancel() {
		location.href = '#pages';
	}

	function onPageApprove() {
		Vosao.jsonrpc.pageService.approve(function(r) {
			Vosao.showServiceMessages(r);
			callLoadPage();
		}, ctx.pageId);
	}

	function loadPagePermission() {
	    var r = ctx.pageRequest.pagePermission;
	   	if (r.publishGranted) {
	   		$('#approveButton').show();
	   		$('#approveOnPageSaveDiv').show();
	    	}
	   	else {
	   		$('#approveButton').hide();
	   		$('#approveOnPageSaveDiv').hide();
	   	}
	   	if (r.changeGranted) {
	   		$('#pageSaveButton').show();
	   	}
	   	else {
	   		$('#pageSaveButton').hide();
	   	}
	   	if (r.admin && ctx.editMode) {
	   		$('.securityTab').show();
	   	}
	   	else {
	   		$('.securityTab').hide();
	   	}
	   	checkDefault();
	}

	function loadStructures() {
		var h = '';
		$.each(ctx.pageRequest.structures.list, function(i, struct) {
			var sel = i == 0 ? 'selected="selected"' : '';
			h += '<option ' + sel + ' value="' + struct.id + '">' + struct.title 
				+ '</option>';
		});
		$('#structure').html(h);
	}

	function onStructureChange() {
		var structureId = $('#structure').val();
		var h = '';
		Vosao.jsonrpc.structureTemplateService.selectByStructure(function(r) {
			var h = '';
			$.each(r.list, function(i, template) {
				var sel = i == 0 ? 'selected="selected"' : '';
				h += '<option ' + sel + ' value="' + template.id + '">' + template.title 
					+ '</option>';
			});
			$('#structureTemplate').html(h);
			if (ctx.page != null) {
				$('#structureTemplate').val(ctx.page.structureTemplateId);
			}
		}, structureId)
	}

	function getTitles() {
		if (!ctx.editMode) {
			return '{en:"' + $('#title').val() + '"}';
		}
	}

	function loadLanguages() {
		var r = ctx.pageRequest.languages;
		languages = {};
		var h = '';
		$.each(r.list, function(i, value) {
			languages[value.code] = value;
		});
	}

	function loadContents() {
		if (ctx.editMode) {
			var r = ctx.pageRequest.contents;
			var allowedLangs = {};
			if (ctx.pageRequest.pagePermission.allLanguages) {
				allowedLangs = languages;
			}
			else {
				$.each(ctx.pageRequest.pagePermission.languagesList.list, 
						function(i, value) {
					allowedLangs[value] = languages[value];
				});
			}
			if (allowedLangs[Vosao.ENGLISH_CODE] != undefined) {
				ctx.currentLanguage = Vosao.ENGLISH_CODE;
			}
			else {
				ctx.currentLanguage = r.list[0].languageCode;
			}
		}
	}

	// Tags

	function onAddTag() {
		Vosao.jsonrpc.tagService.getTree(function(r) {
			$('#tagTree').html(renderTags(r.list));
			
			$('#tagTree li > a').click(function() {
				var id = $(this).attr('data-id');
				if (id) {
					onTagSelect(id);
				}
			});
			
	        $("#tagTree").treeview({
				animated: "fast",
				collapsed: true,
				unique: true,
				persist: "cookie",
				cookieId: "tagTree"
			});
	        $('#tag-dialog').dialog('open');
		});
	}

	function renderTags(list) {
		var html = ''
		$.each(list, function (i, value) {
			html += renderTag(value);
		});
		return html;
	}

	function renderTag(vo) {
		var html = '<li><a data-id="' + vo.entity.id + '">' + vo.entity.name + '</a>';
	    if (vo.children.list.length > 0) {
	        html += '<ul>';
	        $.each(vo.children.list, function(n, value) {
	            html += renderTag(value);
	        });
	        html += '</ul>';
	    }
	    return html + '</li>';
	}

	function onTagSelect(id) {
		Vosao.jsonrpc.tagService.addTag(function(r){
			Vosao.showServiceMessages(r);
		    $('#tag-dialog').dialog('close');
			callLoadTags();
		}, ctx.page.friendlyURL, id);
	}

	function callLoadTags() {
		Vosao.jsonrpc.pageService.getPageTags(function(r) {
			ctx.pageRequest.tags = r;
			showTags();
		}, ctx.page.friendlyURL);
	}

	function showTags() {
		var h = '';
		$.each(ctx.pageRequest.tags.list, function (i,value) {
			h += '<span class="tag">' + value.name + ' <a data-id="' + value.id 
			+ '"><img src="/static/images/02_x.png"/></a></span>';
		});
		$('#tags').html(h);
		$('#tags .tag > a').click(function() {
			var id = $(this).attr('data-id');
			if (id) {
				onTagRemove(id);
			}
		});
	}

	function onTagRemove(id) {
		Vosao.jsonrpc.tagService.removeTag(function(r) {
			Vosao.showServiceMessages(r);
			callLoadTags();
		}, ctx.page.friendlyURL, id);
	}
	
	
	return Backbone.View.extend({
		
		css: '/static/css/jquery.treeview.css',
		
		el: $('#tab-1'),

		tmpl: _.template(indexHtml),
		
		render: function() {
			Vosao.addCSSFile(this.css);
			this.el = $('#tab-1');
			this.el.html(this.tmpl({messages:messages}));
			postRender();
		},
		
		remove: function() {
		    $("#tag-dialog").dialog('destroy').remove();
			this.el.html('');
			Vosao.removeCSSFile(this.css);
		}
		
	});
	
});