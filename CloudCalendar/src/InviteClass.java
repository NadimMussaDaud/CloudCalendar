import java.time.LocalDateTime;

public class InviteClass implements Invite{

    private static final String ACCEPTED = "accepeted";
    private static final String REJECTED = "rejected";
    private static final String UNANSWERED = "unanswered";
    
    private String event, invitee, host, status;
    private LocalDateTime date;

    public InviteClass(String event, LocalDateTime date, String invitee, String host){
        this.event = event;
        this.date = date;
        this.invitee = invitee;
        this.host = host;
        this.status = UNANSWERED;
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
    public void accept(){
        status = ACCEPTED;
    }
    
    @Override
    public void reject(){
        status = REJECTED;
    }
    
}
