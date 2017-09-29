package app.utils.adapters;

import app.model.AnswerStatistics;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Anton on 28.09.2017.
 */
public class XmlStatisticsMapAdapter extends
        XmlAdapter<XmlStatisticsMapAdapter.QuestionnaireStatisticsEntrySet, Map<String, List<List<AnswerStatistics>>>> {

    @Override
    public Map<String, List<List<AnswerStatistics>>> unmarshal(QuestionnaireStatisticsEntrySet questionnaireStatisticsEntrySet)
            throws Exception {
        return questionnaireStatisticsEntrySet.toMap();
    }

    @Override
    public QuestionnaireStatisticsEntrySet marshal(Map<String, List<List<AnswerStatistics>>> statisticsStorage) throws Exception {
        return new QuestionnaireStatisticsEntrySet(statisticsStorage);
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlRootElement
    static class QuestionnaireStatisticsEntrySet {

        @XmlElement
        private Set<Entry> entrySet = new HashSet<>();

        private QuestionnaireStatisticsEntrySet() {

        }

        private QuestionnaireStatisticsEntrySet(Map<String, List<List<AnswerStatistics>>> statisticsMap) {
            for (Map.Entry mapEntry : statisticsMap.entrySet())
                entrySet.add(new Entry((String)mapEntry.getKey(), (List<List<AnswerStatistics>>)mapEntry.getValue()));
        }

        private Map<String, List<List<AnswerStatistics>>> toMap() {
            Map<String, List<List<AnswerStatistics>>> statisticsStorage = new HashMap<>();
            for (Entry entry : entrySet)
                statisticsStorage.put(entry.questionnaireName, entry.questionStatistics.toListOfList());
            return statisticsStorage;
        }

        @XmlAccessorType(XmlAccessType.FIELD)
        private static class Entry{

            @XmlElement
            private String questionnaireName;

            @XmlElement
            private QuestionStatisticsWrapper questionStatistics;

            private Entry() {

            }

            private Entry(String questionnaireName, List<List<AnswerStatistics>> answerStatistics) {
                this.questionnaireName = questionnaireName;
                this.questionStatistics = new QuestionStatisticsWrapper(answerStatistics);
            }

            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlRootElement
            private static class QuestionStatisticsWrapper {

                @XmlElement
                private List<AnswerStatisticsWrapper> questionStatisticsList = new ArrayList<>();

                private QuestionStatisticsWrapper() {

                }

                private QuestionStatisticsWrapper(List<List<AnswerStatistics>> questionStatisticsList) {
                    this.questionStatisticsList = questionStatisticsList.stream().map(
                            (List<AnswerStatistics> answerStatisticsList) -> new AnswerStatisticsWrapper(answerStatisticsList)).
                            collect(Collectors.toList());
                }

                private List<List<AnswerStatistics>> toListOfList() {
                    return questionStatisticsList.stream().map((AnswerStatisticsWrapper answerStatisticsWrapper) ->
                            answerStatisticsWrapper.answerStatisticsList).collect(Collectors.toList());
                }

                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlRootElement
                private static class AnswerStatisticsWrapper {

                    @XmlElement
                    List<AnswerStatistics> answerStatisticsList = new ArrayList<>();

                    private AnswerStatisticsWrapper() {

                    }

                    private AnswerStatisticsWrapper(List<AnswerStatistics> answerStatisticsList) {
                        this.answerStatisticsList = answerStatisticsList;
                    }
                }
            }
        }
    }
}
