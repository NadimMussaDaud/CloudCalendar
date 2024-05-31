
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

abstract class AbstractAccount implements Account{
    
    private String name ;
    protected List<Invite> invites; // All events in which he is invited an others which he is hosting

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
    @Override
    public boolean isAvailable(LocalDateTime date){
        return invites.stream().noneMatch(invite -> invite.getDate().equals(date) && invite.getStatus().equals("accepted"));
    }

    public void addEvent(Invite invite) {
        //reject all the other that causes conflict
        for (Invite inv : invites) {
            if (inv.getDate().equals(invite.getDate())) {
                inv.reject();
                inv.respond(); 
            }
        }
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

    public Iterator<Invite> inviteResponse(String event, String response){
        List<Invite> responses = new ArrayList<>();
        for (Invite invite : invites) {
            if(invite.getEvent().equals(event)){
                invite.respond();
                if(response.equals("accept"))
                    invite.accept();
                else 
                    invite.reject();
            }
            else if(response.equals("accept")){
                //Reject all the others
                if(!invite.getStatus().equals("rejected") && !invite.hasResponded()){
                    invite.reject();
                    responses.add(invite);
                }
                    
            }
        }
        return responses.iterator();
    }
    public Iterator<Invite> addInvite(Invite invite){
        invites.add(invite);
        return Collections.emptyIterator();
    }

    public void removeInvite(Invite invite){
        Invite remove = null;
        for (Invite inv : invites) {
            if(invite.getEvent().equals(inv.getEvent()))
                remove = inv;
        }
        invites.remove(remove);
    }


    public boolean hasResponded(String invite){
        for (Invite inv : invites) {
            if (inv.getEvent().equals(invite)) {
                return inv.hasResponded();
            }
        }
        return false;
    }


    @Override
    public int compareTo(Account other) {
        return this.name.compareTo(other.getName());
    }

    public abstract String getType();
    
}
