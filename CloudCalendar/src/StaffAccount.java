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
        for (Invite inv : invites) {
            if(inv.getDate().equals(invite.getDate()))
                inv.reject();
                if(inv.getHost().equals(invite.getInvitee()))
                //remove invite
                removed = inv;
                invites.remove(inv);
        }
        invites.add(invite);
        
       return removed;
    }
}
