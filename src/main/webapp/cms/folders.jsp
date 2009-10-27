<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<html>
<head>

    <title>Folders</title>

    <script src="/static/js/jquery.treeview.pack.js" type="text/javascript"></script>
    <link rel="stylesheet" href="/static/css/jquery.treeview.css" type="text/css" />

<script type="text/javascript">
$(function(){
    initJSONRpc(loadTree);
});

function loadTree() {
    folderService.getTree(function(r) {
        $('#folders-tree').html(renderFolder(r));
        $("#folders-tree").treeview();
    });
}

function renderFolder(vo) {
    var html = '<li><a href="folder.jsp?id=' + vo.entity.id + '">' 
        + vo.entity.title + '</a> <a title="Add child" href="folder.jsp?parent=' 
        + vo.entity.id + '">+</a>';
    if (vo.children.list.length > 0) {
        html += '<ul>';
        $.each(vo.children.list, function(n, value) {
            html += renderFolder(value);
        });
        html += '</ul>';
    }
    return html + '</li>';
}

</script>
    
</head>
<body>

<h1>Folders</h1>

<ul id="folders-tree"> </ul>

</body>
</html>