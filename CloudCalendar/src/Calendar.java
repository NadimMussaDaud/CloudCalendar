import java.util.Iterator;

import Exceptions.*;

public interface Calendar {

    Iterator<Account> listAccounts() throws NoAccountsException;

    void register(String accountName, String type) throws NonExistentTypeException, DuplicateAccountException;

    void create(String accountName, String type);

    
}
