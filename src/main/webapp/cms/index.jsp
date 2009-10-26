<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<html>
<head>
    <title>VOSAO CMS</title>
</head>
<body>

<div id="main-panel">
    <div>
        <img src="/static/images/document_edit.png" />
        <a href="/cms/pages.jsp">Content pages</a>
        <p>Here you can edit site content. All content viewed as a tree of pages.
           You can change various page properties including design template 
           binding. 
        </p>
    </div>
    <div>
        <img src="/static/images/file_edit.png" />
        <a href="/cms/templates.jsp">Design templates</a>
        <p>Here you can edit design templates. Site can have several design 
           templates. For every page you can select separate template.</p>
    </div>
    <div>
        <img src="/static/images/diskette.png" />
        <a href="/cms/folders.jsp">File resources storage</a>
        <p>Here you can edit site resources. Resource could be any file
           including those used in design templates or referenced from pages</p>
    </div>
    <div>
        <img src="/static/images/computer.png" />
        <a href="/cms/config.jsp">Site configuration</a>
        <p>Here you can change site configuration. Site domain, email,
           Google Analytics Id, comments template, comments email.</p>
    </div>
    <div>
        <img src="/static/images/shoppingcart.png" />
        <a href="/cms/plugins.jsp">Plugins</a>
        <p>Various plugins configuration. Forms.</p>
    </div>
</div>

</body>
</html>
