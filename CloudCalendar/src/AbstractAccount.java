import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

abstract class AbstractAccount implements Account {
    
    private String name ;
    private List<Invite> invites; // All events in which he is invited an others which he is hosting

    public AbstractAccount(String name){
        this.name = name;
        this.invites = new ArrayList<>();
    }

    public String getName() {
        return name;
    }
    /**
     * Method built with AI
     */
    public boolean hasEvent(String eventName){
        return invites.stream().anyMatch(invite -> invite.getEvent().equals(eventName));
    }
    
    /**
     * Method built with AI
     */
    public boolean isAvailable(LocalDateTime date){
        return invites.stream().noneMatch(invite -> invite.getDate().equals(date));
    }

    public void addEvent(Invite invite) {
        invites.add(invite);
        if (invite.getHost().equals(this.name)) 
            invite.accept();
    }
    public Iterator<Invite> getEvents() {
        return invites.iterator();
    }
    
    public String getStatus(String event){
        for (Invite invite : invites) {
            if (invite.getEvent().equals(event)) {
                return invite.getStatus();
            }
        }
        return null;
    }

    public abstract String getType();
    
}
