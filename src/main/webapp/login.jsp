<%@ page contentType="text/html; charset=UTF-8"%>
<html>
<head>
    <title>Login page</title>
    <link rel="stylesheet" href="/static/css/login.css" type="text/css" />
    <script type="text/javascript">
        function onLogin() {
            var email = $('#loginEmail').val();
            var password = $('#loginPassword').val();
            if (email == '') {
                error('Email is empty!');
            }
            else {
                loginService.login(function (r) {
                    if (r.result == 'success') {
                        document.location.href = r.message;
                    }
                    else {
                        error(r.message);
                    }
                }, email, password);
            }                
        }
    </script>
</head>
<body>

<div id="content">
<div id="memo">
    <img src="/static/images/login-logo.jpg" />
    <p>Sign in to change site content.</p>
</div>

<div id="login-form">
<form>

<h4>Sign in with your</h4>
<h3>Vosao CMS account</h3>

<div>
    <label>Email</label>
    <input type="text" id="loginEmail" />
</div>
<div>
    <label>Password</label>
    <input type="password" id="loginPassword" />
</div>
<div class="messages"> </div>
<div>
    <label></label>
    <input type="button" value="Login" onclick="onLogin()" />
</div>    

</form>
</div>

<div class="clear"> </div>

<div class="version">
  <span>Vosao CMS Version 0.0.4</span>
</div>

</div>

</body>
</html>