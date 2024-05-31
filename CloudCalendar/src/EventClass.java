
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EventClass implements Event{

    private String name;
    private String priority;
    private String host;
    private LocalDateTime date;
    private List<String> topics;
    private List<Invite> invitees;

    private final String REJECTED = "rejected";
    private final String ACCEPTED = "accepted";
    private final String UNANSWERED = "unanswered";


    public EventClass(String host, String name, String priority, LocalDateTime date, List<String> topics) {
        this.name = name;
        this.priority = priority;
        this.date = date;
        this.host = host;
        this.topics = topics;
        this.invitees = new ArrayList<>();
    }
    @Override
    public String getName() {
        return  name;   
    }
    @Override
    public LocalDateTime getDate() {
        return date;
    }
    public String getPriority() {
        return priority;
    }
    @Override
    public Iterator<Invite> getInvitees() {
        return invitees.iterator();
    }
    @Override
    public int getAccepts() {
        int count = 0;
        for (Invite invite : invitees) {
            if(invite.getStatus().equals(ACCEPTED)){
                count++;
            }
        }
        return count;
    }
    @Override
    public int getRejections() {
        int count = 0;
        for (Invite invite : invitees) {
            if(invite.getStatus().equals(REJECTED)){
                count++;
            }
        }
        return count;
    }
    @Override
    public int getUnanswered() {
        int count = 0;
        for (Invite invite : invitees) {
            if(invite.getStatus().equals(UNANSWERED)){
                count++;
            }
        }
        return count;
    }
    @Override
    public Iterator<String> getTopics() {
        return topics.iterator();
    }
    
    @Override
    public String getHost() {
       return host;
    }
    @Override
    public boolean hasTopic(List<String> topics) {
        return topics.stream().anyMatch(this.topics::contains);
    }
    @Override
    public void addInvitee(Invite invite) {
        invitees.add(invite);
    }
    @Override
    public int getInvitedNumber() {
        return invitees.size();
    }
    @Override
    public boolean hasInvite(String invitee) {
        for (Invite invite : invitees) {
            if (invite.getInvitee().equals(invitee))
                return true;
        }
        return false;
    }
}
