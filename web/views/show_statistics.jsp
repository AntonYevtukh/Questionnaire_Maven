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
<html>
<head>
    <%@include file="/views/partials/meta.jsp" %>
    <title>Statistics</title>
</head>
<body>
    <%@include file="/views/partials/header.jsp" %>
    <div class="main_area">
        <div class="content">
            <%
                Questionnaire questionnaire = (Questionnaire) request.getAttribute("questionnaire");
                String userName = (String) session.getAttribute("user_name");
                List<List<AnswerStatistics>> questionnaireStatistics =
                        (List<List<AnswerStatistics>>) request.getAttribute("questionnaire_statistics");
                List<Double> topAnswerNumbers = (List<Double>)request.getAttribute("top_answers");
                int totallyVotes = (Integer) request.getAttribute("totally_votes");
            %>
            <h2><%=questionnaire.getName()%></h2>
            <label class="respondents">Total Respondents count: <%= totallyVotes%><label><br>
            <%
                int questionNumber = 0;
                for (Question question : questionnaire.getQuestions()) {
                    int answerNumber = 0;
                    double maxPercent = topAnswerNumbers.get(questionNumber) == null ? 0 : topAnswerNumbers.get(questionNumber);
            %>
            <div class="question_container">
                <label class="question"><%=question.getBody()%></label>
                <% for (String answer: question.getAnswers()) {
                    AnswerStatistics answerStatistics = questionnaireStatistics.get(questionNumber).get(answerNumber);
                %>
                <div class=answer_container>
                    <label class="answer"><%=answer%></label>
                    <div class="answer_statistics">
                        <div class="complete">
                            <%if (answerStatistics.isUserVoted(userName)) {%>
                            <i class="glyphicon glyphicon-ok" style="color: forestgreen; font-size: 20px; margin-top: -2px"></i>
                            <%} %>
                        </div>
                        <label class="percent"><%=answerStatistics.getPercent()%>%</label>
                        <div class="outer">
                            <div class="inner" style="width:<%=maxPercent == 0 ? 0 : 100 * answerStatistics.getPercent() / maxPercent%>%">
                            </div>
                            <label class="count"><%=answerStatistics.getVotes()%></label>
                        </div>
                    </div>
                </div>
                <% answerNumber++; }  %>
            </div>
            <% questionNumber++; }  %>
                <div class="help">

                </div>
                <div class="single_button_container">
                    <div class="button" id="back"><a class="button_link" href="/users?type=show_cabinet">Back</a></div>
                </div>
        </div>
    </div>
    <%@include file="/views/partials/footer.jsp" %>
</body>
</html>
