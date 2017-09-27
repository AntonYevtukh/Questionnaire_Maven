<%--
  Created by IntelliJ IDEA.
  User: Anton
  Date: 23.09.2017
  Time: 19:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <%@include file="/views/partials/meta.jsp" %>
    <title>Sign Up</title>
</head>
<body>
<%@include file="/views/partials/header.jsp" %>
<div class="main_area">
    <div class="login_form_container">
        <h2 class="login_form_header">Create User</h2>
        <form class="login_form" action="/users?type=create" method="POST">
        <label class="input_label">Login:</label>
        <input class="text_input" type="text" name="login" value="${prev_login}"><br>
        <label class="error">${login_error}</label><br>
        <label class="input_label">Password:</label>
        <input class="text_input" type="password" name="password" value="${prev_password}"><br>
        <label class="error">${password_error}</label><br>
        <label class="input_label">Retype Password:</label>
        <input class="text_input" type="password" name="retype" value="${prev_password}"><br>
        <label class="error">${password_error}</label><br>
        <div class="button_container">
            <input class="button" id="sign_in" type="submit" value="Create"/>
            <div class="button" id="sign_up"><a class="button_link" href="/index.jsp">Back</a></div>
        </div>
    </form>
    </div>
</div>
<%@include file="/views/partials/footer.jsp" %>
</body>
</html>
