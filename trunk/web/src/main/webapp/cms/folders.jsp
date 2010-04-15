<%
/**
 * Vosao CMS. Simple CMS for Google App Engine.
 * Copyright (C) 2009 Vosao development team
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * email: vosao.dev@gmail.com
 */
%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<html>
<head>
    <title>Folders</title>
    <script src="/static/js/jquery.form.js" language="javascript"></script>
    <script src="/static/js/jquery.treeview.pack.js" type="text/javascript"></script>
    <link rel="stylesheet" href="/static/css/jquery.treeview.css" type="text/css" />
    <link rel="stylesheet" href="/static/css/picasa.css" type="text/css" />
    <script src="/static/js/cms/folders.js" type="text/javascript"></script>
    <script src="/static/js/cms/picasa.js" type="text/javascript"></script>
</head>
<body>

<div id="tabs">

<ul>
    <li><a href="#tab-1">Folders</a></li>
    <li><a href="#tab-2">Picasa</a></li>
</ul>

<div id="tab-1">
    <p id="folder-location"></p>
    <ul id="folders-tree"><img src="/static/images/ajax-loader.gif" /></ul>
</div>

<div id="tab-2">
    <div id="albums"><img src="/static/images/ajax-loader.gif" /></div>
    <div class="clear">
        <a id="createAlbumLink" href="#">Create album</a>
    </div> 
    <div id="albumDetails">
        <hr class="clear picasa-hr" />
        <p class="album-name">
            Album : <span id="album-location"></span>
            <a id="deleteAlbumLink" href="#">Delete album</a>
        </p>
        <div id="photos"></div>
        <div class="clear">
            <a id="uploadPhotoLink" href="#">Upload image</a>
        </div>
    </div> 
    <div class="clear"></div>
</div>

</div>

<div id="album-dialog" style="display:none" title="New album">
  <form id="albumForm">
    <div class="form-row">
        <label>Album title</label>
        <input id="title"/>
    </div>
    <div id="albumMessages"></div>
    <div class="buttons-dlg">
        <input type="submit" value="Save" />
        <input id="cancelAlbumButton" type="button" value="Cancel" />
    </div>
  </form>  
</div>

<div id="upload-dialog" style="display:none" title="Upload photo">
  <form id="upload" action="/cms/upload" method="post" enctype="multipart/form-data">
    <input type="hidden" name="fileType" value="picasa" />
    <input type="hidden" name="albumId" />
    <input type="file" name="uploadFile" />
    <div class="buttons-dlg">
        <input type="submit" value="Upload" />
        <input id="uploadCancelButton" type="button" value="Cancel" />
    </div>    
  </form>
</div>


</body>
</html>