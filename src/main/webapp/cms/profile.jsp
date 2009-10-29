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
