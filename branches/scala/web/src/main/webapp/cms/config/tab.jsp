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

<ul class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
    <li class="ui-corner-top ui-state-default">
        <a href="index.jsp"><fmt:message key="config.title" /></a>
    </li>
    <li class="ui-corner-top ui-state-default">
        <a href="comments.jsp"><fmt:message key="comments" /></a>
    </li>
    <li class="ui-corner-top ui-state-default">
        <a href="languages.jsp"><fmt:message key="languages" /></a>
    </li>
    <li class="ui-corner-top ui-state-default">
        <a href="messages.jsp"><fmt:message key="message_bundle" /></a>
    </li>
    <li class="ui-corner-top ui-state-default">
        <a href="users.jsp"><fmt:message key="users" /></a>
    </li>
    <li class="ui-corner-top ui-state-default">
        <a href="groups.jsp"><fmt:message key="groups" /></a>
    </li>
    <li class="ui-corner-top ui-state-default">
        <a href="tags.jsp"><fmt:message key="tags" /></a>
    </li>
</ul>
