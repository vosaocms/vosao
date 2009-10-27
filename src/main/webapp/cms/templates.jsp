<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<html>
<head>

    <title>Templates</title>
    
    <script src="/static/js/jquery.form.js" language="javascript"></script>

<script type="text/javascript">
// <!--
$(function(){
    $("#import-dialog").dialog({ width: 400, autoOpen: false });
    $('#upload').ajaxForm(afterUpload);
    initJSONRpc(loadTemplates);
});

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

function loadTemplates() {
    templateService.getTemplates(function (r) {
        var html = '<table><tr><td></td><td>Title</td></tr>';
        $.each(r.list, function (n, value) {
            html += '<tr><td><input type="checkbox" value="' + value.id 
                + '" /></td><td><a href="/cms/template.jsp?id=' + value.id
                +'">' + value.title + '</a></td></tr>';
        });
        $('#templates').html(html + '</table>');
    });
}

function onAdd() {
	location.href = '/cms/template.jsp';
}

function onDelete() {
    var ids = new Array();
    $('#templates input:checked').each(function () {
        ids.push(this.value);
    });
    if (ids.length == 0) {
        info('Nothing selected.');
        return;
    }
    templateService.deleteTemplates(function(r) {
        showServiceMessages(r);
        loadTemapltes();
    }, javaList(ids));
}

function onExport() {
    var ids = new Array();
    $('#templates input:checked').each(function () {
        ids.push(this.value);
    });
    if (ids.length == 0) {
        info('Nothing selected.');
        return;
    }
    var link = '/cms/export?type=theme&ids=';
    $.each(ids, function (n, value) {
        if (n == 0) {
            link += value;
        }
        else {
            link += '::' + value;
        }
    });
    location.href = link;
    $('#templates input:checked').each(function () {
        this.checked = false;
    });
    info('Themes were successfully exported.');
}

// -->
</script>

</head>
<body>

<h1>Templates</h1>
<div id="templates"> </div>
<div class="buttons">
    <input type="button" value="Add" onclick="onAdd()" />
    <input type="button" value="Delete" onclick="onDelete()" />
    <input type="button" value="Export" onclick="onExport()" />
    <input type="button" value="Import" onclick="onImport()" />
</div>

<div id="import-dialog" title="Import themes" style="display:none">
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

</body>
</html>