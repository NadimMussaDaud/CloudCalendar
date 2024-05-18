public class ManagerAccount extends AbstractAccount {
    
    public ManagerAccount(String name) {
        super(name);
    }

    @Override
    public String getType() {
        return "manager";
    }
}
