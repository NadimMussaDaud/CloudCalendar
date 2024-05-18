import Exceptions.*;

public interface Calendar {

    void listAccounts();

    void register(String accountName, String type) throws NonExistentTypeException, DuplicateAccountException;

    void create(String accountName, String type);

    boolean hasAccount();
    
}
