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

<script type="text/javascript">
$(function() {
	$('#languageSelect').click(function() {
		$('#languageDiv').show();
		setTimeout(function() {
	        $('#languageDiv').hide();
		}, 5000);
	});
});

</script>

<a id="languageSelect" href="#"><fmt:message key="language"/></a>
<div id="languageDiv">
    <a href="#" onclick="Vosao.changeLanguage('en')">English</a>
    <a href="#" onclick="Vosao.changeLanguage('ru')">Русский</a>
</div>