package app.utils.adapters;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.namespace.QName;
import java.util.*;

/**
 * Created by Anton on 28.09.2017.
 */
public class XmlVotedMapAdapter extends XmlAdapter<XmlVotedMapAdapter.UserVotesEntrySet, Map<String, Set<String>>> {

    @Override
    public Map<String, Set<String>> unmarshal(UserVotesEntrySet userVotesEntrySet) throws Exception {
        return userVotesEntrySet.toMap();
    }

    @Override
    public UserVotesEntrySet marshal(Map<String, Set<String>> map) throws Exception {
        return new UserVotesEntrySet(map);
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlRootElement
    static class UserVotesEntrySet {

        @XmlElement
        private Set<UserVotesEntry> entrySet = new HashSet<>();

        private UserVotesEntrySet(){

        }

        private UserVotesEntrySet(Map<String, Set<String>> userVotesMap) {
            for (Map.Entry entry : userVotesMap.entrySet())
                entrySet.add(new UserVotesEntry((String)entry.getKey(), (Set<String>)entry.getValue()));
        }

        public Map<String, Set<String>> toMap() {
            Map<String, Set<String>> userVotesMap = new HashMap<>();
            for (UserVotesEntry userVotesEntry : entrySet)
                userVotesMap.put(userVotesEntry.questionnaireName, userVotesEntry.userNames);
            return userVotesMap;
        }

        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlRootElement
        private static class UserVotesEntry {

            @XmlElement
            private String questionnaireName;
            @XmlElement
            private Set<String> userNames = new HashSet<>();

            public UserVotesEntry(){

            }

            public UserVotesEntry(String questionnaireName, Set<String> userNames) {
                this.questionnaireName = questionnaireName;
                this.userNames = userNames;
            }
        }
    }
}
