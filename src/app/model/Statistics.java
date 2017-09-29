package app.model;

import app.config.ConfigClass;
import app.utils.adapters.XmlStatisticsMapAdapter;
import app.utils.adapters.XmlVotedMapAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.File;
import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by Anton on 22.09.2017.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "statistics")
public class Statistics implements Serializable {

    private static Statistics instance;

    @XmlJavaTypeAdapter(XmlVotedMapAdapter.class)
    @XmlElement(name = "voted_users_statistics")
    private Map<String, Set<String>> votedUsersStatistics = new HashMap<>();
    @XmlJavaTypeAdapter(XmlStatisticsMapAdapter.class)
    @XmlElement(name = "questionnaire_statistics")
    private Map<String, List<List<AnswerStatistics>>> statisticsStorage = new HashMap<>();

    public static Statistics getInstance() {

        if (instance == null) {
            //TODO
            File file = new File(ConfigClass.STORAGE_PATH + "\\statistics" + ConfigClass.SAVE_FILE_EXTENSION);
            if (file.exists()) {
                instance = ConfigClass.SERIALIZER.deserialize(Statistics.class, file);
                if (instance.statisticsStorage.isEmpty() || instance.votedUsersStatistics.isEmpty())
                    instance = new Statistics();
            }
            else
                instance = new Statistics();
            instance.addEmptyStatistics();
        }
        return instance;
    }

    private Statistics() {

    }

    public synchronized void addEmptyStatistics() {

        Set<String> questionnaireNames = new HashSet(Questionnaires.getInstance().getNameQuestionnaireMap().keySet());
        questionnaireNames.removeAll(statisticsStorage.keySet());

        for (String questionnaireName : questionnaireNames) {
            votedUsersStatistics.put(questionnaireName, new HashSet<>());
        }

        Questionnaires questionnaires = Questionnaires.getInstance();
        Map<String, List<List<AnswerStatistics>>> emptyStatisticsMap;

        //Через Stream API, но ничего не понятно
        emptyStatisticsMap = questionnaireNames.stream().collect(
                Collectors.toMap(Function.identity(), (String questionnaireName) ->
                        questionnaires.getNameQuestionnaireMap().get(questionnaireName).
                                getQuestions().stream().map((Question question) ->
                                question.getAnswers().stream().map((String answer) -> new AnswerStatistics()).
                                        collect(Collectors.toCollection(ArrayList::new))
                        ).collect(Collectors.toCollection(ArrayList::new))
                ));
        statisticsStorage.putAll(emptyStatisticsMap);

        /*Через for, но дольше
        for (String questionnaireName : questionnaireNames) {
            Questionnaire questionnaire = questionnaires.getNameQuestionnaireMap().get(questionnaireName);
            List<List<AnswerStatistics>> questionnaireStatistics = new ArrayList<>();
            for (Question question : questionnaire.getQuestions()) {
                List<AnswerStatistics> questionStatistics = new ArrayList<>();
                for (String answer : question.getAnswers()) {
                    questionStatistics.add(new AnswerStatistics());
                }
                questionnaireStatistics.add(questionStatistics);
            }
            statisticsStorage.put(questionnaireName, questionnaireStatistics);
        }*/
    }

    public synchronized Set<String> getCompletedByUser(String userName) {
        Set<String> result = new HashSet<>();
        for (String questionnaireName : votedUsersStatistics.keySet())
            if (votedUsersStatistics.get(questionnaireName).contains(userName))
                result.add(questionnaireName);
        return result;
    }

    public synchronized void updateAnswers(String questionnaireName, String userName, List<Integer> answers) {
        List<List<AnswerStatistics>> questionnaireStatistics = statisticsStorage.get(questionnaireName);
        System.out.println(questionnaireName);
        System.out.println(userName);
        System.out.println(votedUsersStatistics.get(questionnaireName));
        votedUsersStatistics.get(questionnaireName).add(userName);
        int totallyVoted = votedUsersStatistics.get(questionnaireName).size();
        int questionNumber = 0;
        for (List<AnswerStatistics> questionStatistics : questionnaireStatistics) {
            int answerNumber = 0;
            for (AnswerStatistics answerStatistics : questionStatistics) {
                answerStatistics.setUserSelection(userName, answers.get(questionNumber) == answerNumber, totallyVoted);
                answerNumber++;
            }
            questionNumber++;
        }
    }

    //При смене имени пользователя
    public synchronized void replaceUserName(String oldUserName, String newUserName) {

        for (Set<String> questionnaireUsers : votedUsersStatistics.values())
            if (questionnaireUsers.contains(oldUserName)) {
                questionnaireUsers.remove(oldUserName);
                questionnaireUsers.add(newUserName);
            }

        for (List<List<AnswerStatistics>> questionnaireStatistics : statisticsStorage.values())
            for (List<AnswerStatistics> questionStatistics : questionnaireStatistics)
                for (AnswerStatistics answerStatistics : questionStatistics)
                    answerStatistics.replaceUserName(oldUserName, newUserName);
    }

    //При удалении пользователя
    public synchronized void removeUserStatistics(String userName) {

        for (Set<String> questionnaireUsers : votedUsersStatistics.values())
            questionnaireUsers.remove(userName);

        for (String questionnaireName : statisticsStorage.keySet()) {
            List<List<AnswerStatistics>> questionnaireStatistics = statisticsStorage.get(questionnaireName);
            int totallyVoted = votedUsersStatistics.get(questionnaireName).size();
            for (List<AnswerStatistics> questionStatistics : questionnaireStatistics)
                for (AnswerStatistics answerStatistics : questionStatistics)
                    answerStatistics.removeUserAnswer(userName, totallyVoted);
        }
    }

    public synchronized int getTotallyVotes(String questionnaireName) {
        System.out.println(questionnaireName);
        System.out.println();
        return votedUsersStatistics.get(questionnaireName).size();
    }

    //Лист листов объектов, которые могут измениться, пока отрисовывается jsp страница, куда он будет передан
    //в качестве источника данных. Я вижу 2 выхода - синхронизация при отрисовке страницы, т.е. синхронизирующий
    //блок, запиленный QuestionnairesController, внутри которого вызывается этот метод, а потом метод
    //req.getRequestDispatcher("/questionnaire_statistics.jsp").forward(req, resp); - я так понимаю он вызывает отрисовку
    //Наверное это убъет многопоточность
    //Второй, который здесь - глубокое копирование, но тоже убивает многопоточность
    //Кажется, еще дольше, чем отрисовка страницы, но я в нем больше уверен
    public synchronized List<List<AnswerStatistics>> getQuestionnaireStatistics(String questionnaireName) {
        /*return statisticsStorage.get(questionnaireName).stream().map((List<AnswerStatistics> questionStatistics) ->
        questionStatistics.stream().map((AnswerStatistics answerStatistics) ->
                new AnswerStatistics(answerStatistics)).collect(Collectors.toCollection(ArrayList::new))).
                collect(Collectors.toCollection(ArrayList::new));

        List<List<AnswerStatistics>> questionnaireStatisticsCopy = new ArrayList<>();
        for (List<AnswerStatistics> questionStatistics : statisticsStorage.get(questionnaireName)) {
            List<AnswerStatistics> questionStatisticsCopy = new ArrayList();
            for(AnswerStatistics answerStatistics : questionStatistics)
                questionStatisticsCopy.add(new AnswerStatistics(answerStatistics));
            questionnaireStatisticsCopy.add(questionStatisticsCopy);
        }*/

        return statisticsStorage.get(questionnaireName);
    }
}
