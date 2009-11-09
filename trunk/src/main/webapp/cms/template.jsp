<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<html>
<head>

    <title>Template</title>

<script type="text/javascript">

var templateId = '<c:out value="${param.id}"/>';

//<!--

var template = '';
var editMode = templateId != '';
var autosaveTimer = '';
    
$(function(){
    initJSONRpc(loadTemplate);
    $("#tabs").tabs();
});
    
function startAutosave() {
    if (templateId != 'null') {
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
    var content = $("#content").val();
    templateService.updateContent(function(r) {
        if (r.result == 'success') {
            var now = new Date();
            info(r.message + " " + now);
        }
        else {
            error(r.message);
        }            
    }, templateId, content);        
}

function onAutosave() {
    if ($("#autosave:checked").length > 0) {
        startAutosave(); 
    }
    else {
        stopAutosave();
    }
}

function loadTemplate() {
	if (!editMode) {
		template = null;
        initTemplateForm();
	}
	templateService.getTemplate(function (r) {
		template = r;
		initTemplateForm();
	}, templateId);
}

function initTemplateForm() {
	if (template != null) {
		$('#title').val(template.title);
        $('#url').val(template.url);
        $('#content').val(template.content);
	}
	else {
        $('#title').val('');
        $('#url').val('');
        $('#content').val('');
	}
}

function onCancel() {
    location.href = '/cms/templates.jsp';
}

function onUpdate(cont) {
	var templateVO = javaMap({
	    id : templateId,
	    title : $('#title').val(),
        url : $('#url').val(),
        content : $('#content').val(),
	});
	templateService.saveTemplate(function (r) {
		showServiceMessages(r);
		if (r.result == 'success' && !cont) {
			location.href = '/cms/templates.jsp';
		}
	}, templateVO);
}


// -->    
</script>

</head>
<body>

<div id="tabs">

<ul>
    <li><a href="#tab-1">Template</a></li>
</ul>

<div id="tab-1">
    <div class="form-row">
        <label>Title</label>
        <input id="title" type="text" />
    </div>
    <div class="form-row">
        <label>URL name</label>
        <input id="url" type="text" />
    </div>
    <div class="form-row">
        <div>
            <input id="autosave" type="checkbox" onchange="onAutosave()"> Autosave</input>
        </div>
        <textarea id="content" rows="30" cols="120"></textarea>
    </div>
    <div class="buttons">
        <input type="button" value="Save and continue" onclick="onUpdate(true)" />
        <input type="button" value="Save" onclick="onUpdate(false)" />
        <input type="button" value="Cancel" onclick="onCancel()" />
    </div>
</div>

</div>

</body>
</html>