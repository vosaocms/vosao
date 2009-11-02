<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<html>
<head>
    <title>SEO Urls</title>

    <link rel="stylesheet" href="/static/css/form.css" type="text/css" />

<script type="text/javascript">

    var seoUrl = null;

    $(function() {
        $("#url-dialog").dialog({ width :500, autoOpen :false });
        initJSONRpc(loadUrls);
    });

    function loadUrls() {
        seoUrlService.select(function (r) {
            var html = '<table class="form-table"><tr><td></td><td>From</td><td>To</td></tr>';
            $.each(r.list, function (i, url) {
                html += '<tr><td><input type="checkbox" value="' 
                    + url.id + '"/></td><td><a href="#" onclick="onEdit(\''
                    + url.id +'\')">' + url.fromLink + '</a></td><td>' 
                    + url.toLink + '</td></tr>';
            });
            $('#urls').html(html + '</table>');
        });
    }

    function onEdit(id) {
        clearMessages();
        seoUrlService.getById(function(r) {
            seoUrl = r;
            urlDialogInit();
            $("#url-dialog").dialog("open");
        }, id);
    }

    function onAdd() {
        seoUrl = null;
        urlDialogInit();
        $("#url-dialog").dialog("open");
    }

    function urlDialogInit() {
        if (seoUrl == null) {
            $('#fromLink').val('');
            $('#toLink').val('');
        }
        else {
            $('#fromLink').val(seoUrl.fromLink);
            $('#toLink').val(seoUrl.toLink);
        }
    }

    function onRemove() {
        var ids = new Array();
        $('#urls input:checked').each(function() {
            ids.push(this.value);
        });
        if (ids.length == 0) {
            info('Nothing selected.');
            return;
        }
        seoUrlService.remove(function(r) {
            showServiceMessages(r);
            loadUrls();
        }, javaList(ids));
    }

    function onSaveAndAdd() {
        onSave(false);
        onAdd();
    }

    function validateSeoUrl(vo) {
        var errors = new Array();
        if (vo.fromLink == '') {
            errors.push('From link is empty');
        }
        if (vo.toLink == '') {
            errors.push('Site redirect link is empty');
        }
        return errors;
    }

    function onSave(closeFlag) {
        var vo = {
            id : seoUrl != null ? seoUrl.id : '',
            fromLink : $('#fromLink').val(),
            toLink : $('#toLink').val(),
        };
        var errors = validateSeoUrl(vo);
        if (errors.length == 0) {
            seoUrlService.save(function(r) {
                if (r.result == 'success') {
                    if (closeFlag) {
                        $("#url-dialog").dialog("close");
                    }
                    loadUrls();
                } else {
                    errorMessages(r.messages.list);
                }
            }, javaMap(vo));
        } else {
            errorMessages(errors);
        }
    }

    function onCancel() {
        $("#url-dialog").dialog("close");
    }

    function clearMessages() {
        $('#url-messages').html('');
    }

    function errorMessages(messages) {
        var h = '<ul>';
        $.each(messages, function(i, msg) {
            h += '<li class="error-msg">' + msg + '</li>';
        });
        $("#url-messages").html(h + '</ul>');
    }
    
</script>    
</head>
<body>

<h1>SEO Urls</h1>

<div id="urls"><img src="/static/images/ajax-loader.gif" /></div>

<div class="buttons">
    <input type="button" value="Add" onclick="onAdd()" />
    <input type="button" value="Remove" onclick="onRemove()" />
</div>

<div id="url-dialog" style="display:none" title="SEO Url details">
    <div id="url-messages" class="messages"> </div>
    <div class="form-row">
        <label>From Link URL</label>
        <input id="fromLink" type="text" />
    </div>
    <div class="form-row">
        <label>On site redirect link URL</label>
        <input id="toLink" type="text" />
    </div>
    <div class="buttons-dlg">
        <input type="button" value="Save and Add" onclick="onSaveAndAdd()" />
        <input type="button" value="Save" onclick="onSave(true)" />
        <input type="button" value="Cancel" onclick="onCancel()" />
    </div>
</div>

</body>
</html>