import java.util.Iterator;

public class StaffAccount extends AbstractAccount {
    
    public StaffAccount(String name) {
        super(name);
    }

    @Override
    public String getType() {
        return "staff";
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
                        invites.remove(inv); // Remove usando o iterador para evitar ConcurrentModificationException
                    }
                }
            }
        }
    
        invites.add(invite);
        return removed;
    }
}
