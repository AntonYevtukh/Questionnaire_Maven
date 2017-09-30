package app.model;

import app.config.GlobalVariables;

import java.io.File;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by Anton on 22.09.2017.
 */
public class Questionnaires implements Serializable {

    private static Questionnaires instance;
    private Map<String, Questionnaire> nameQuestionnaireMap;

    public static Questionnaires getInstance() {

        if (instance == null) {
            //TODO
            File directory = new File(GlobalVariables.QUESTIONNAIRE_PATH);
            if (directory.exists() && directory.isDirectory()) {
                instance = new Questionnaires(directory.listFiles((File dir, String name) ->
                        name.endsWith(GlobalVariables.SAVE_FILE_EXTENSION)));
                                //name.toLowerCase().indexOf("questionnaire") != -1));
            }
            else
                instance = new Questionnaires();
        }
        return instance;
    }

    private Questionnaires() {
        nameQuestionnaireMap = new HashMap<>();
    }

    private Questionnaires(File[] files) {
        nameQuestionnaireMap = Arrays.stream(files).map(file -> GlobalVariables.SERIALIZER.deserialize(
                Questionnaire.class, file)).collect(Collectors.toMap(Questionnaire::getName, Function.identity()));
    }

    public Map<String, Questionnaire> getNameQuestionnaireMap() {
        return nameQuestionnaireMap;
    }
}