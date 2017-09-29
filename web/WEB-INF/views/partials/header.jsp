<%--
  Created by IntelliJ IDEA.
  User: Anton
  Date: 24.09.2017
  Time: 20:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="header">
    <div class="logo">
        <img src="/assets/img/slowpoke.png" id="logo">
    </div>
    <h1>Funny Questions</h1>
    <% if (session.getAttribute("user_name") != null) {%>
    <div class="user_panel">
        <i class="glyphicon glyphicon-user" style="color: whitesmoke; font-size: 16px"></i>
        <label class="user"><%=session.getAttribute("user_name")%></label>
        <a href="/users?type=edit" class="header_link">
            <i class="glyphicon glyphicon-edit" style="color: whitesmoke; font-size: 16px; margin: 0 0 0 4px"></i>
        </a>
        <a href="/users?type=remove" class="header_link">
            <i class="glyphicon glyphicon-remove" style="color: whitesmoke; font-size: 16px; margin: 0 0 0 4px"></i>
        </a>
        <a href="/users?type=exit" class="header_link">
            <i class="glyphicon glyphicon-log-out" style="color: whitesmoke; font-size: 16px; margin: 0 0 0 4px"></i>
        </a>
    </div>
    <%}%>
</div>

