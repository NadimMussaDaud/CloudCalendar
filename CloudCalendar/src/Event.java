
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;

public interface Event {

    Iterator<String> getTopics();
    String getName();
    LocalDateTime getDate();
    String getPriority();
    Iterator<Invite> getInvitees();
    int getInvitedNumber();
    int getAccepts();
    int getRejections();
    int getUnanswered();
    String getHost();
    void addInvitee(Invite invite);
    /**
     * 
     * @param topics the topics to match with
     * @return true if has at least one topic from @topics
     */
    boolean hasTopic(List<String> topics);
    boolean hasInvite(String invitee);
   // boolean hasResponded(String invitee);
}
