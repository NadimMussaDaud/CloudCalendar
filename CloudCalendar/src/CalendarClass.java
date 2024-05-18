import java.util.HashMap;
import java.util.Map;
import Exceptions.*;

public class CalendarClass implements Calendar{

    private Map<String, Account> accounts;
    

    public CalendarClass(){
        this.accounts = new HashMap<>();
    }

    @Override
    public void listAccounts() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listAccounts'");
    }

    @Override
    public void register(String accountName, String type) throws NonExistentTypeException , DuplicateAccountException {
        
        if (accounts.containsKey(accountName)) {
            throw new DuplicateAccountException();
        }
        if (!isType(type)) {
            throw new NonExistentTypeException();
        }
        switch (type) {
            case "staff" ->  accounts.put(accountName, new StaffAccount(accountName));
            case "manager" -> accounts.put(accountName, new ManagerAccount(accountName));
            case "guest" ->  accounts.put(accountName, new GuestAccount(accountName));
        }
    }

    private boolean isType(String type) {
        return (type.equals("staff") || type.equals("manager") || type.equals("guest"));
    }

    @Override
    public void create(String accountName, String type) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'create'");
    }

    @Override
    public boolean hasAccount() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'hasAccount'");
    }
}
