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
    <title>User profile</title>
    
<script type="text/javascript">

    var user = null;

    $(function(){
        initJSONRpc(loadUser);
    });

    function loadUser() {
        userService.getLoggedIn(function (r) {
            user = r;
            $('#name').val(user.name);
            $('#email').val(user.email);
            $('#password1').val('');
            $('#password2').val('');
        });
    }

    function validPasswords() {
        var pass1 = $('#password1').val();
        var pass2 = $('#password2').val();
        if (pass1 || pass2) {
            if (pass1 == pass2) {
                return true;
            }
            return false;
        }
        return true;
    }
    
    function onSave() {
        var pass = '';
        if (validPasswords()) {
            pass = $('#password1').val();
        }
        else {
            error('Passwords don\'t match.');
            return;
        }
        var vo = {
        	id : String(user.id),
            name : $('#name').val(),   
            email : $('#email').val(),   
            password : pass,   
        };
        userService.save(function (r) {
            showServiceMessages(r);
        }, javaMap(vo));
    }

</script>
    
</head>
<body>

<h1>User profile</h1>

<div class="form-row">
    <label>User name</label>
    <input id="name" type="text" />
</div>

<div class="form-row">
    <label>User email</label>
    <input id="email" type="text" />
</div>

<div class="form-row">
    <label>Password</label>
    <input id="password1" type="password" />
</div>
<div class="form-row">
    <label>Retype the password</label>
    <input id="password2" type="password" />
</div>

<div class="buttons">
    <input type="button" value="Save" onclick="onSave()" />
</div>

</body>
</html>
