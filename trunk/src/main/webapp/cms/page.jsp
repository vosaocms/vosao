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
    
    var page = '';
    var children = {list:[]};
    var parentPage = '';
    var editMode = pageId != '';
    var contentEditor;
    var autosaveTimer = '';
    
    $(function(){
        $("#tabs").tabs();
        $("#tabs").bind('tabsselect', tabSelected);       
        $(".datepicker").datepicker({dateFormat:'dd.mm.yy'});
        initJSONRpc(loadData);
        if (!editMode) {
            $('.childrenTab').hide();
        }
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
        }
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
            $('#content').val(page.content);
            $('#templates').val(page.template);
        }
        else {
            $('#title').val('');
            $('#friendlyUrl').show();
            $('#friendlyUrl').val('');
            $('#parentFriendlyUrl').html('');
            $('#publishDate').val(formatDate(new Date()));
            $('#commentsEnabled').each(function() {this.checked = false});
            $('#content').val('');
        }
        contentEditor = CKEDITOR.replace('content', {
            height: 480,
            filebrowserUploadUrl : '/cms/upload',
            filebrowserBrowseUrl : '/cms/fileBrowser.jsp'
        });
    }

    function onPageUpdate() {
        var pageVO = javaMap({
            id : pageId,
            parent: pageParentId,
            title : $('#title').val(),
            friendlyUrl : $('#parentFriendlyUrl').text() + $('#friendlyUrl').val(),
            publishDate : $('#publishDate').val(),
            commentsEnabled : String($('#commentsEnabled:checked').size() > 0),
            content : getContent(),
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
        var url = $("#friendlyUrl").val();
        var title = $("#title").val();
        if (url == '') {
            $("#friendlyUrl").val(urlFromTitle(title));
        }
    }

    function tabSelected(event, ui) {
        if (ui.index == 1) {
            startAutosave();
        }
        else {
            stopAutosave();
        }            
    }
    
    function startAutosave() {
     	if (pageId != 'null') {
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

    function getContent() {
        return $("#ckeditor-form-row iframe").contents().find("body").html();
    }

    function saveContent() {
        var content = getContent();
        pageService.updateContent(function(r) {
            if (r.result == 'success') {
                var now = new Date();
                info(r.message + " " + now);
            }
            else {
                error(r.message);
            }            
        }, pageId, content);        
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
            var html = '<table class="form-table"><tr><td></td><td>Title</td>\
                <td>Friendly URL</td></tr>';
            $.each(r.list, function (n, value) {
                html += '<tr><td><input type="checkbox" value="' + value.id 
                + '"/></td><td><a href="/cms/page.jsp?id=' + value.id 
                +'">' + value.title + '</a></td><td>' + value.friendlyURL
                + '</td></tr>';
            });
            $('#children').html(html + '</table>'); 
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
        if (ids == []) {
            info('Nothing selected.');
            return;
        }
        pageService.deletePages(function(r) {
            showServiceMessages(r);
            loadChildren();
        }, javaList(ids));
    }

    function loadComments() {
        commentService.getByPage(function (r) {
            var html = '<table class="form-table"><tr><td></td><td>Status</td>\
                <td>Name</td><td>Content</td></tr>';
            $.each(r.list, function (n, value) {
                var status = value.disabled ? 'Disabled' : 'Enabled';
                html += '<tr><td><input type="checkbox" value="' + value.id 
                + '"/></td><td>' + status + '</a></td><td>' + value.name
                + '</td><td>' + value.content + '</td></tr>';
            });
            $('#comments').html(html + '</table>'); 
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
        if (ids == []) {
            info('Nothing selected.');
            return;
        }
        commentService.deleteComments(function(r) {
            showServiceMessages(r);
            loadComments();
        }, javaList(ids));
    }
            
    // -->    
  </script>
    
</head>
<body>

<div id="tabs">
<ul>
    <li><a href="#tab-1">Page</a></li>
    <li><a href="#tab-2">Content</a></li>
    <li class="childrenTab"><a href="#tab-3">Children pages</a></li>
    <li><a href="#tab-4">Comments</a></li>
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

<div id="tab-2">

<div>
  <input id="autosave" type="checkbox" checked="checked" 
      onchange="onAutosave()"> Autosave</input>
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

<div id="tab-4">
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