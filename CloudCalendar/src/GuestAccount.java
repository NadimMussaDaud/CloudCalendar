

public class GuestAccount extends AbstractAccount {
    
    public GuestAccount(String name) {
        super(name);
    }

    @Override
    public String getType() {
        return "guest";
    }
}
