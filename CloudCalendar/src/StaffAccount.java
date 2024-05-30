import java.time.LocalDateTime;
import java.util.Iterator;

public class StaffAccount extends AbstractAccount {
    
    public StaffAccount(String name) {
        super(name);
    }

    @Override
    public String getType() {
        return "staff";
    }

    public boolean isAvailable(LocalDateTime date){
        return invites.stream().noneMatch(invite -> invite.getDate().equals(date) && invite.getStatus().equals("accepted") && invite.getPriority().equals("high"));
    }

    /**
     * 
     * @param invite to be added
     * @return an invite in case it was removed or null    otherwise. Always adds the invite.
     * 
     TODO: Remove event in CalendarClass
     */
    public Invite addInvite(Invite invite){
        Invite removed = null;
        
        if(invite.getPriority().equals("high")){
            
            Iterator<Invite> iterator = invites.iterator();
            while (iterator.hasNext()) {
                Invite inv = iterator.next();
                
                if (inv.getDate().equals(invite.getDate())) {
  
                    inv.reject();
                    if (inv.getHost().equals(invite.getInvitee())) {
                        // Remove invite
                        removed = inv;
                        //iterator.remove();
                    }
                }
            }
            invite.accept();
        }
    
        invites.remove(removed);
        invites.add(invite);
        return removed;
    }
}
