<%@ page import="app.model.AnswerStatistics" %>
<%@ page import="java.util.List" %>
<%@ page import="app.model.Questionnaire" %>
<%@ page import="app.model.Question" %><%--
  Created by IntelliJ IDEA.
  User: Anton
  Date: 24.09.2017
  Time: 00:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Questionnaire questionnaire = (Questionnaire) request.getAttribute("questionnaire");
    String questionnaireName = questionnaire.getName();
%>
<html>
<head>
    <%@include file="/views/partials/meta.jsp" %>
    <title><%=questionnaireName%></title>
</head>
<body>
    <%@include file="/views/partials/header.jsp" %>
    <div class="main_area">
        <div class="content">
            <h2><%=questionnaireName%></h2>
            <form action="/questionnaires?type=submit_questionnaire&questionnaire_name=<%=questionnaire.getName()%>" method="POST">
            <%  int questionNumber = 0;
                for (Question question : questionnaire.getQuestions()) {
                int answerNumber = 0; %>
                <label class="question"><%= question.getBody() %></label><br>
                <% for (String answer: question.getAnswers()) {%>
                    <div class="answer_wrap">
                    <input class="radio" type="radio" name="question_<%=questionNumber%>" value="<%=answerNumber%>" required>
                    <label class="answer"><%=answer%></label>
                    </div>
                    <% answerNumber++;%>
                <% } questionNumber++;
            }%>
                <div class="button_container">
                    <input class="button" id="sign_in" type="submit" value="Submit"/>
                    <div class="button" id="sign_up"><a class="button_link" href="/users?type=show_cabinet">Back</a></div>
                </div>
            </form>
        </div>
    </div>
    <%@include file="/views/partials/footer.jsp" %>
</body>
</html>
