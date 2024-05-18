 public class StaffAccount extends AbstractAccount {
    
    public StaffAccount(String name) {
        super(name);
    }

    @Override
    public String getType() {
        return "staff";
    }
}
