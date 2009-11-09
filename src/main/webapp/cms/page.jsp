<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<%@ page import="org.vosao.servlet.FileUploadServlet" %>
<html>
<head>
  <title>Page</title>
  <script type="text/javascript" src="/static/ckeditor/ckeditor.js"></script>
  
<%
    if (request.getParameter("id") != null) {    
        session.setAttribute(FileUploadServlet.IMAGE_UPLOAD_PAGE_ID, 
        		request.getParameter("id"));
    }
%>  
  
  <script type="text/javascript">

    var pageId = '<c:out value="${param.id}"/>';
    var pageParentId = '<c:out value="${param.parent}"/>';

    //<!--
    
    var page = null;
    var contents = null;
    var currentLanguage = '';
    var children = {list:[]};
    var parentPage = '';
    var editMode = pageId != '';
    var contentEditor;
    var etalonContent = '';
    var autosaveTimer = '';
    
    $(function(){
        contentEditor = CKEDITOR.replace('content', {
            height: 480,
            filebrowserUploadUrl : '/cms/upload',
            filebrowserBrowseUrl : '/cms/fileBrowser.jsp'
        });
        $("#tabs").tabs();
        $("#tabs").bind('tabsselect', tabSelected);       
        $(".datepicker").datepicker({dateFormat:'dd.mm.yy'});
        initJSONRpc(loadData);
    });

    function loadData() {
        loadPage();
        loadTemplates();
        if (!editMode) {
            loadParent();
        }
        else {
            loadChildren();
            loadComments();
            loadContents();
        }
        loadLanguages();
    }

    function loadPage() {
        pageService.getPage(function (r) {
            page = r;
            if (editMode) {
                pageId = page.id;
                pageParentId = page.parent;
            }
            initPageForm();
        }, pageId);            
    }

    function loadParent() {
        pageService.getPage(function (r) {
            if (r != null) {
                var urlEnd = r.parent == null ? '' : '/'; 
                $('#parentFriendlyUrl').html(r.friendlyURL + urlEnd);
                parentPage = r;
            }
        }, pageParentId);
    }
    
    function loadTemplates() {
        templateService.getTemplates(function (r) {
            var html = '';
            $.each(r.list, function (n,value) {
                html += '<option value="' + value.id + '">' 
                    + value.title + '</option>';
            });
            $('#templates').html(html);
            if (page != null) {
                $('#templates').val(page.template);
            }
        });
    }

    function loadLanguages() {
        languageService.select(function (r) {
            var h = '';
            $.each(r.list, function (i, value) {
                var sel = '';
                if (value.code == ENGLISH_CODE) {
                    sel = 'selected="selected"';
                }
                h += '<option value="' + value.code + '" ' + sel + '>' 
                    + value.title + '</option>';
            });
            $('#language').html(h);
        });
    }
    
    function initPageForm() {
        if (page != null) {
            $('#title').val(page.title);
            if (page.parent == null) {
                $('#friendlyUrl').hide();
                $('#friendlyUrl').val('');
                $('#parentFriendlyUrl').html('/');
            }
            else {
                $('#friendlyUrl').show();
                $('#friendlyUrl').val(page.pageFriendlyURL);
                $('#parentFriendlyUrl').html(page.parentFriendlyURL + '/');
            }
            $('#publishDate').val(page.publishDateString);
            $('#commentsEnabled').each(function() {this.checked = page.commentsEnabled});
            $('#templates').val(page.template);
            $('.contentTab').show();
            $('.childrenTab').show();
            $('.commentsTab').show();
        }
        else {
            $('#title').val('');
            $('#friendlyUrl').show();
            $('#friendlyUrl').val('');
            $('#parentFriendlyUrl').html('');
            $('#publishDate').val(formatDate(new Date()));
            $('#commentsEnabled').each(function() {this.checked = false});
            $('.contentTab').hide();
            $('.childrenTab').hide();
            $('.commentsTab').hide();
        }
    }

    function onPageUpdate() {
        var pageVO = javaMap({
            id : pageId,
            parent: pageParentId,
            title : $('#title').val(),
            friendlyUrl : $('#parentFriendlyUrl').text() + $('#friendlyUrl').val(),
            publishDate : $('#publishDate').val(),
            commentsEnabled : String($('#commentsEnabled:checked').size() > 0),
            content : getEditorContent(),
            template : $('#templates option:selected').val()
        });
        pageService.savePage(function (r) {
            if (r.result == 'success') {
                location.href = '/cms/pages.jsp';
            }
            else {
                showServiceMessages(r);
            }
        }, pageVO);        
    }
    
    function onTitleChange() {
    	if (editMode) {
        	return;
    	}
        var url = $("#friendlyUrl").val();
        var title = $("#title").val();
        if (url == '') {
            $("#friendlyUrl").val(urlFromTitle(title));
        }
    }

    function tabSelected(event, ui) {
        if (ui.index == 1 && $('#autosave:checked').size() > 0) {
            startAutosave();
        }
        else {
            stopAutosave();
        }            
    }
    
    function startAutosave() {
     	if (editMode) {
     	    if (autosaveTimer == '') {
     	        autosaveTimer = setInterval(saveContent, AUTOSAVE_TIMEOUT * 1000);
     	    }
        }
    }
    
    function stopAutosave() {
        if (autosaveTimer != '') {
            clearInterval(autosaveTimer);
            autosaveTimer = '';
        }
    }

    function getEditorContent() {
        return contentEditor.getData();
    }

    function setEditorContent(data) {
    	contentEditor.setData(data);
    }

    function isContentChanged() {
        return contents[currentLanguage] != getEditorContent();
    }
    
    function saveContent() {
        var content = getEditorContent();
        contents[currentLanguage] = content;
        pageService.updateContent(function(r) {
            if (r.result == 'success') {
                var now = new Date();
                info(r.message + " " + now);
            }
            else {
                error(r.message);
            }            
        }, pageId, content, currentLanguage);        
    }

    function onAutosave() {
        if ($("#autosave:checked").length > 0) {
            startAutosave(); 
        }
        else {
            stopAutosave();
        }
    }

    function onPagePreview() {
        window.open(page.friendlyURL, "preview");
    }

    function onPageCancel() {
        location.href = '/cms/pages.jsp';
    }

    function loadChildren() {
        pageService.getChildren(function (r) {
            var html = '<table class="form-table"><tr><th></th><th>Title</th>\
                <th>Friendly URL</th></tr>';
            $.each(r.list, function (n, value) {
                html += '<tr><td><input type="checkbox" value="' + value.id 
                + '"/></td><td><a href="/cms/page.jsp?id=' + value.id 
                +'">' + value.title + '</a></td><td>' + value.friendlyURL
                + '</td></tr>';
            });
            $('#children').html(html + '</table>');
            $('#children tr:even').addClass('even');
        }, pageId);
    }

    function onAddChild() {
        location.href = '/cms/page.jsp?parent=' + pageId;
    }

    function onDelete() {
        var ids = [];
        $('#children input:checked').each(function () {
            ids.push(this.value);
        });
        if (ids.length == 0) {
            info('Nothing selected.');
            return;
        }
        if (confirm('Are you sure?')) {
            pageService.deletePages(function(r) {
                showServiceMessages(r);
                loadChildren();
            }, javaList(ids));
        }
    }

    function loadComments() {
        commentService.getByPage(function (r) {
            var html = '<table class="form-table"><tr><th></th><th>Status</th>\
                <th>Name</th><th>Content</th></tr>';
            $.each(r.list, function (n, value) {
                var status = value.disabled ? 'Disabled' : 'Enabled';
                html += '<tr><td><input type="checkbox" value="' + value.id 
                + '"/></td><td>' + status + '</a></td><td>' + value.name
                + '</td><td>' + value.content + '</td></tr>';
            });
            $('#comments').html(html + '</table>');
            $('#comments tr:even').addClass('even'); 
        }, pageId);
    }

    function onEnableComments() {
        var ids = [];
        $('#comments input:checked').each(function () {
            ids.push(this.value);
        });
        if (ids == []) {
            info('Nothing selected.');
            return;
        }
        commentService.enableComments(function(r) {
            showServiceMessages(r);
            loadComments();
        }, javaList(ids));
    }

    function onDisableComments() {
        var ids = [];
        $('#comments input:checked').each(function () {
            ids.push(this.value);
        });
        if (ids == []) {
            info('Nothing selected.');
            return;
        }
        commentService.disableComments(function(r) {
            showServiceMessages(r);
            loadComments();
        }, javaList(ids));
    }
    
    function onDeleteComments() {
        var ids = [];
        $('#comments input:checked').each(function () {
            ids.push(this.value);
        });
        if (ids.length == 0) {
            info('Nothing selected.');
            return;
        }
        if (confirm('Are you sure?')) {
            commentService.deleteComments(function(r) {
                showServiceMessages(r);
                loadComments();
            }, javaList(ids));
        }
    }

    function onLanguageChange() {
    	if (!isContentChanged() 
    	    || confirm('Are you sure? All changes will be lost.')) {
    	    currentLanguage = $('#language').val();
    	    if (contents[currentLanguage] == undefined) {
    	   	    contents[currentLanguage] = '';
    	    }
    	    setEditorContent(contents[currentLanguage]);
    	}
    	else {
        	$('#language').val(currentLanguage);
    	}
    }

    function loadContents() {
        if (editMode) {
            pageService.getContents(function (r) {
                contents = [];
                $.each(r.list, function (i, value) {
                    contents[value.languageCode] = value.content;                    
                });
                currentLanguage = ENGLISH_CODE;
                setEditorContent(contents[ENGLISH_CODE]);
            }, pageId);
        }
        else {
            setEditorContent('');
        }
    }
    
    // -->    
  </script>
    
</head>
<body>

<div id="tabs">
<ul>
    <li><a href="#tab-1">Page</a></li>
    <li class="contentTab"><a href="#tab-2">Content</a></li>
    <li class="childrenTab"><a href="#tab-3">Children pages</a></li>
    <li class="commentsTab"><a href="#tab-4">Comments</a></li>
</ul>

<div id="tab-1">

<div class="form-row">
    <label>Title</label>
    <input id="title" type="text" class="form-title" size="40" 
        onchange="onTitleChange()"/>
</div>
<div class="form-row">
    <label>Friendly URL </label>
    <span id="parentFriendlyUrl"> </span>
    <input id="friendlyUrl" type="text" />
</div>
<div class="form-row">
    <label>Template</label>
    <select id="templates"></select>
</div>
<div class="form-row">
    <label>Publication date</label>
    <input id="publishDate" type="text" class="datepicker" size="8"/>
</div>
<div class="form-row">
    <label>Enable comments</label>
    <input id="commentsEnabled" type="checkbox" />
</div>

<div class="buttons">
    <input type="button" value="Save" onclick="onPageUpdate()" />
    <input id="pagePreview" type="button" value="Preview" onclick="onPagePreview()" />
    <input type="button" value="Cancel" onclick="onPageCancel()" />
</div>    

</div>

<div id="tab-2" class="contentTab">

<div style="float:right">
  <input id="autosave" type="checkbox" onchange="onAutosave()"> Autosave</input>
</div>
<div>
  Select content language: <select id="language" onchange="onLanguageChange()"></select>
</div>

<div class="form-row" id="ckeditor-form-row">
    <textarea id="content" rows="20" cols="80"></textarea>
</div>

<div class="buttons">
    <input type="button" value="Save and continue" onclick="saveContent()" />
    <input type="button" value="Save" onclick="onPageUpdate()" />
    <input type="button" value="Preview" onclick="onPagePreview()" />
    <input type="button" value="Cancel" onclick="onPageCancel()" />
</div>    

</div>

<div id="tab-3" class="childrenTab">
    <div id="children"> </div>
    <div class="buttons">
        <input type="button" value="Add child page" onclick="onAddChild()" />
        <input type="button" value="Delete pages" onclick="onDelete()" />
    </div>    
</div>

<div id="tab-4" class="commentsTab">
    <div id="comments"> </div>
    <div class="buttons">
        <input type="button" value="Enable comments" onclick="onEnableComments()" />
        <input type="button" value="Disable comments" onclick="onDisableComments()" />
        <input type="button" value="Delete comments" onclick="onDeleteComments()" />
    </div>    
</div>

</div>

</body>
</html>