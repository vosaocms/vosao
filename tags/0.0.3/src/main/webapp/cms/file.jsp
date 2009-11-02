<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<html>
<head>
    <title>File view</title>

<script type="text/javascript">

    var fileId = '<c:out value="${param.id}" />';
    var folderId = '<c:out value="${param.folderId}" />';

    // <!--

    var file = '';
    var editMode = fileId != '';
    var autosaveTimer = '';

    $(function() {
        $("#tabs").tabs();
        $("#tabs").bind('tabsselect', tabSelected);       
        initJSONRpc(loadFile);
    });

    function tabSelected(event, ui) {
        if (ui.index == 1) {
            startAutosave();
        }
        else {
            stopAutosave();
        }            
    }

    function startAutosave() {
        if (fileId != 'null') {
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

    function saveContent() {
        var content = $("textarea").val();
        fileService.updateContent(function(r) {
            if (r.result == 'success') {
                var now = new Date();
                info(r.message + " " + now);
            }
            else {
                error(r.message);
            }            
        }, fileId, content);        
    }

    function onAutosave() {
        if ($("#autosave:checked").length > 0) {
            startAutosave(); 
        }
        else {
            stopAutosave();
        }
    }

    function loadFile() {
        fileService.getFile(function (r) {
            file = r;
            if (editMode) {
                folderId = file.folderId;
            }
            initFormFields();
        }, fileId);
    }

    function initFormFields() {
        if (editMode) {
            $('#title').val(file.title);
            $('#name').val(file.name);
            $('#fileEditDiv').show();
            $('#mimeType').html(file.mimeType);
            $('#size').html(file.size);
            $('#fileLink').html(file.link);
            $('#download').html('<a href="' + file.link + '">Download</a>');
            if (file.textFile) {
                $('.contentTab').show();
                $('#content').val(file.content);
            }
            else {
                $('.contentTab').hide();
            }                
            if (file.imageFile) {
                $('#imageContent').html('<img src="' + file.link + '" />');
            }
            else {
                $('#imageContent').html('');
            }                
        }
        else {
            $('#title').val('');
            $('#name').val('');
            $('#fileEditDiv').hide();
            $('.contentTab').hide();
            $('#imageContent').html('');
        }
    }

    function onUpdate() {
        var vo = javaMap({
            id : fileId,
            folderId : folderId,
            title : $('#title').val(),
            name : $('#name').val()
        });
        fileService.saveFile(function (r) {
            if (r.result == 'success') {
                location.href = '/cms/folder.jsp?id=' + folderId;
            }
            else {
                showServiceMessages(r);
            }
        }, vo);
    }
    
    function onCancel() {
        location.href = '/cms/folder.jsp?id=' + folderId;
    }

    //-->
</script>

</head>
<body>

<div id="tabs">
<ul>
    <li><a href="#tab-1">File</a></li>
    <li class="contentTab"><a href="#tab-2">Content</a></li>
</ul>

<div id="tab-1">

<div style="float:left">
 <div class="form-row">
    <label>Title</label>
    <input id="title" type="text" />
 </div>
 <div class="form-row">
    <label>Name</label>
    <input id="name" type="text" />
 </div>
 
<div id="fileEditDiv">
 <div class="form-row">
    <label>Content type</label>
    <span id="mimeType"> </span>
 </div>
 <div class="form-row">
    <label>Size</label>
    <span id="size"> </span>
 </div>
 <div class="form-row">
    <label>Exrernal link</label>
    <span id="fileLink"> </span>
 </div>
 <div class="form-row">
    <label> </label>
    <span id="download"> </span>
 </div>
 </div>
  
 <div class="buttons">
    <input type="button" value="Save" onclick="onUpdate()" />
    <input type="button" value="Cancel" onclick="onCancel()" />
 </div>    
</div>

<div id="imageContent" style="float:left;margin-left: 20px;"> </div>
<div style="clear:both"> </div>

</div>

<div id="tab-2" class="contentTab">
    <div>
        <input id="autosave" type="checkbox" checked="checked" 
            onchange="onAutosave()"> Autosave</input>
    </div>
    <div class="form-row">
        <textarea id="content" rows="20" cols="80"></textarea>
    </div>
    <div class="buttons">
        <input type="button" value="Save and continue" onclick="saveContent()" />
        <input type="button" value="Cancel" onclick="onCancel()" />
    </div>    
</div>

</div>

</body>
</html>