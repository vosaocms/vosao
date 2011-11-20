/**
 * Vosao CMS. Simple CMS for Google App Engine.
 * 
 * Copyright (C) 2009-2010 Vosao development team.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * email: vosao.dev@gmail.com
 */

$(function(){
	Vosao.initJSONRpc(loadTree);
    $("#tabs").tabs();
	$.cookie("folderReturnPath", null, {path:'/', expires: 10});
});

function loadTree() {
	Vosao.jsonrpc.folderService.getTree(function(r) {
        $('#folders-tree').html(renderFolder(r));
        $("#folders-tree").treeview({
			animated: "fast",
			collapsed: true,
			unique: true,
			persist: "cookie",
			cookieId: "folderTree"
		});
    });
}

function renderFolder(vo) {
    var html = '<li><a href="folder.vm?id=' + vo.entity.id + '">' 
        + vo.entity.title + '</a> <a title="' + messages('add_child') 
        + '" href="folder.vm?parent=' 
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
