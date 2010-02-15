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

<div id="versions" class="ui-tabs ui-widget ui-corner-all ui-widget-content">
    <div class="vertical-buttons-panel"> </div>      
    <a id="addVersionLink" class="button ui-state-default ui-corner-all" 
        href="#">
        <span class="ui-icon ui-icon-plus"></span> Add version
    </a>
    <div id="auditData">
        <div>Page state: <span id="pageState"> </span></div>
        <div>User created: <span id="pageCreateUser"> </span></div>
        <div>Creation date: <span id="pageCreateDate"> </span></div>
        <div>User modified: <span id="pageModUser"> </span></div>
        <div>Modify date: <span id="pageModDate"> </span></div>
    </div>
</div>
