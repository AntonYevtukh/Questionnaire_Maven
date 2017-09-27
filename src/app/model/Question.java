package app.model;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Created by Anton on 22.09.2017.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "question")
public class Question {

    @XmlAttribute(name = "body")
    private String body;

    @XmlElement(name = "answer")
    private List<String> answers;

    private Question() {

    }

    public Question(String body, List<String> answers) {
        this.body = body;
        this.answers = answers;
    }

    public String getBody() {
        return body;
    }

    public List<String> getAnswers() {
        return answers;
    }

    @Override
    public String toString() {
        return "Question{" +
                "body='" + body + '\'' +
                ", answers=" + answers +
                '}';
    }
}
