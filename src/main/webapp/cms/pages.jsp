<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<html>
<head>

    <title>Pages</title>
    <script src="/static/js/jquery.treeview.pack.js" type="text/javascript"></script>
    <link rel="stylesheet" href="/static/css/jquery.treeview.css" type="text/css" />

    <script type="text/javascript">

    $(function(){
        initJSONRpc(loadTree);
    });

    function loadTree() {
        pageService.getTree(function(r) {
            $('#pages-tree').html(renderPage(r));
            $("#pages-tree").treeview();
        });
    }

    function renderPage(vo) {
        var html = '<li><a href="page.jsp?id=' + vo.entity.id + '">' 
            + vo.entity.title + '</a> <a title="Add child" href="page.jsp?parent=' 
            + vo.entity.id + '">+</a>';
        if (vo.children.list.length > 0) {
            html += '<ul>';
            $.each(vo.children.list, function(n, value) {
                html += renderPage(value);
            });
            html += '</ul>';
        }
        return html + '</li>';
    }
    
    </script>
    
</head>
<body>

<h1>Pages</h1>
<ul id="pages-tree"> </ul>

</body>
</html>