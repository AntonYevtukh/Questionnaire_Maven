<%--
  Created by IntelliJ IDEA.
  User: Anton
  Date: 21.09.2017
  Time: 23:15
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
  <%@include file="/views/partials/meta.jsp" %>
  <title>Questionnaire</title>
</head>
<body>
<%@include file="/views/partials/header.jsp" %>
<%
  if(session.getAttribute("user_name") != null) {
    response.sendRedirect("/users?type=show_cabinet");
  }
%>
  <div class="main_area">
    <div class="login_form_container">
      <h2 class="login_form_header">Login</h2>
      <form class="login_form" action="/users?type=sign_in" method="POST">
        <label class="input_label">Login:</label>
        <input class="text_input" type="text" name="login" value="${prev_login}"><br>
        <label class="error">${login_error}</label><br>
        <label class="input_label">Password:</label>
        <input class="text_input" type="password" name="password" value="${prev_password}"><br>
        <label class="error">${password_error}</label><br>
        <div class="button_container">
          <input class="button" id="sign_in" type="submit" value="Sign in"/>
          <div class="button" id="sign_up"><a class="button_link" href="/views/new_user.jsp">Sign Up</a></div>
        </div>
      </form>
    </div>
  </div>
  <%@include file="/views/partials/footer.jsp" %>
  </body>
</html>

