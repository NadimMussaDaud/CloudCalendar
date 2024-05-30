
import java.time.LocalDateTime;

public class InviteClass implements Invite{

    private static final String ACCEPTED = "accepted";
    private static final String REJECTED = "rejected";
    private static final String UNANSWERED = "unanswered";
    
    private String event, priority, invitee, host, status;
    private LocalDateTime date;
    private boolean responded;

    public InviteClass(String event, String priority, LocalDateTime date, String invitee, String host){
        this.event = event;
        this.priority = priority;
        this.date = date;
        this.invitee = invitee;
        this.host = host;
        this.status = UNANSWERED;
        this.responded = false;
    }
    
    @Override
    public String getEvent(){
        return event;
    }
    @Override
    public LocalDateTime getDate(){
        return date;
    }
    @Override
    public String getInvitee(){
        return invitee;
    }
    @Override
    public String getHost(){
        return host;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public String getPriority(){
        return priority;
    }

    @Override
    public void accept(){
        status = ACCEPTED;
    }
    
    @Override
    public void reject(){
        status = REJECTED;
    }
    
    @Override
    public void respond(){
        responded = true;
    }

    @Override
    public boolean hasResponded(){
        return responded;
    }

}
