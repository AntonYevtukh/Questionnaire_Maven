package app.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Anton on 22.09.2017.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "answer")
public class AnswerStatistics implements Serializable {

    @XmlElement(name = "percent")
    private double percent;
    @XmlElement(name = "voted_users")
    private Set<String> votedUsers = new HashSet<>();

    public AnswerStatistics(AnswerStatistics originStatistics) {
        this.percent = originStatistics.percent;
        this.votedUsers = new HashSet<>(originStatistics.votedUsers);
    }

    public AnswerStatistics() {
        percent = 0.0;
        votedUsers = new HashSet<>();
    }

    public void setUserSelection(String userName, boolean isSelected, int totallyVoted) {
        if (isSelected)
            votedUsers.add(userName);
        else
            votedUsers.remove(userName);
        percent = (double) votedUsers.size() / totallyVoted * 100;
    }

    public int getVotes() {
        return votedUsers.size();
    }

    public double getPercent() {
        return percent;
    }

    public boolean isUserVoted(String userName) {
        return votedUsers.contains(userName);
    }

    public void replaceUserName(String oldUserName, String newUserName) {
        if (votedUsers.contains(oldUserName)) {
            votedUsers.remove(oldUserName);
            votedUsers.add(newUserName);
        }
    }

    public void removeUserAnswer(String userName, int totallyVoted) {
        votedUsers.remove(userName);
        percent = (totallyVoted == 0) ? 0 : (double) votedUsers.size() / totallyVoted;
    }

    @Override
    public String toString() {
        return "AnswerStatistics{" +
                "percent=" + percent +
                ", votedUsers=" + votedUsers +
                '}';
    }
}
