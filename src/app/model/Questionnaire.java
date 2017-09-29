package app.model;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Anton on 22.09.2017.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "questionnaire")
public class Questionnaire implements Serializable{

    @XmlAttribute(name = "name")
    private String name;

    @XmlElement(name = "question")
    private List<Question> questions;

    //Для сериализатора
    private Questionnaire() {

    }

    public Questionnaire(String name, List<Question> questions) {
        this.name = name;
        this.questions = questions;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Questionnaire{" +
                "questions=" + questions +
                '}';
    }
}
