package app.controllers;

import app.config.GlobalVariables;
import app.model.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Anton on 23.09.2017.
 */
public class QuestionnairesController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String requestType = req.getParameter("type");
        switch (requestType) {
            case "pass_questionnaire":
                passQuestionnaire(req, resp);
                break;
            case "show_statistics":
                showStatistics(req, resp);
                break;
            case "submit_questionnaire":
                submitQuestionnaire(req, resp);
                break;
        }
    }

    private void passQuestionnaire(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String questionnaireName = req.getParameter("questionnaire_name");
        Questionnaire questionnaire = Questionnaires.getInstance().getNameQuestionnaireMap().get(questionnaireName);
        req.setAttribute("questionnaire", questionnaire);
        req.getRequestDispatcher("/WEB-INF/views/questionnaire.jsp").forward(req, resp);
    }

    private void showStatistics(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String questionnaireName = req.getParameter("questionnaire_name");
        HttpSession httpSession = req.getSession();
        String userName = (String) httpSession.getAttribute("user_name");
        List<List<AnswerStatistics>> questionnaireStatistics;
        Questionnaire questionnaire;
        int totallyVotes;
        synchronized (GlobalVariables.GLOBAL_LOCK) {
            questionnaire = Questionnaires.getInstance().getNameQuestionnaireMap().get(questionnaireName);
            questionnaireStatistics = Statistics.getInstance().getQuestionnaireStatistics(questionnaireName);
            totallyVotes = Statistics.getInstance().getTotallyVotes(questionnaireName);
        }
        List<Double> topAnswerNumbers = questionnaireStatistics.stream().map((List<AnswerStatistics> answersList) ->
                Collections.max(answersList, Comparator.comparing(AnswerStatistics::getVotes)).getPercent()).collect(
                Collectors.toList());
        req.setAttribute("questionnaire", questionnaire);
        req.setAttribute("questionnaire_statistics", questionnaireStatistics);
        req.setAttribute("totally_votes", totallyVotes);
        req.setAttribute("top_answers", topAnswerNumbers);
        req.getRequestDispatcher("/WEB-INF/views/show_statistics.jsp").forward(req, resp);
    }

    private void submitQuestionnaire(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String questionnaireName = req.getParameter("questionnaire_name");
        HttpSession httpSession = req.getSession();
        String userName = (String) httpSession.getAttribute("user_name");
        List<Integer> answers = new LinkedList<>();
        Enumeration<String> parameterNames = req.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String parameterName = parameterNames.nextElement();
            if (parameterName.contains("question_")) {
                String parameterValue = req.getParameter(parameterName);
                answers.add(Integer.parseInt(parameterValue.substring(parameterValue.indexOf("_") + 1)));
            }
        }
        Statistics.getInstance().updateAnswers(questionnaireName, userName, answers);
        showStatistics(req, resp);
    }
}
