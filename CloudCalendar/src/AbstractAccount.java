
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

abstract class AbstractAccount implements Account{
    

    //TODO: Eliminar addEvent e deixar só addInvite
    //TODO: isAvailable diferente aqui e em Staff. Tentar tornar igual

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

    public void inviteResponse(String event, String response){
        for (Invite invite : invites) {
            if(invite.getEvent().equals(event)){
                invite.respond();
                if(response.equals("accept"))
                    invite.accept();
                else 
                    invite.reject();
            }
            else {
                //Reject all the others
                invite.reject();
            }
        }
    }
    public Invite addInvite(Invite invite){
        invites.add(invite);
        return null;
    }

    public void removeInvite(Invite invite){
        invites.remove(invite);
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
