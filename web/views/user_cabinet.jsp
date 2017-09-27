<%@ page import="app.model.Questionnaire" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.Enumeration" %><%--
  Created by IntelliJ IDEA.
  User: Anton
  Date: 23.09.2017
  Time: 19:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="/views/partials/meta.jsp" %>
    <title>Questionnaires</title>
</head>
<body>
    <%@include file="/views/partials/header.jsp" %>
    <div class="main_area">
        <div class="content">
            <h2>Questionnaires</h2>
            <table class="questionnaire_table">
            <%
                Set<String> questionnaires = (Set<String>)request.getAttribute("questionnaires");
                Set<String> completedQuestionnaires = (Set<String>)request.getAttribute("completed_questionnaires");
                for (String questionnaire : questionnaires) {
            %>
                <tr>
                    <td class="questionnaire">
                        <%= questionnaire %>
                    </td>
                    <td class="button_cell">
                        <div class="button">
                            <a href="/questionnaires?type=pass_questionnaire&questionnaire_name=<%= questionnaire%>"
                               class="button_link">Start</a>
                        </div>
                    </td>
                    <td class="button_cell">
                        <div class="button">
                            <a href="/questionnaires?type=show_statistics&questionnaire_name=<%= questionnaire%>"
                               class="button_link">Statistics</a>
                        </div>
                    </td>

                    <td class="completed">
                        <%if (completedQuestionnaires.contains(questionnaire)) {%>
                        <i class="glyphicon glyphicon-ok" style="color: forestgreen; font-size: 24px;"></i>
                        <%}%>
                    </td>
                </tr>
                <%}%>
                </table>
            </div>
    </div>
    <%@include file="/views/partials/footer.jsp" %>
</body>
</html>
