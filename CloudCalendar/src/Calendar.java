public interface Calendar {

    void listAccounts();

    void register(String accountName, String type);

    void create(String accountName, String type);

    boolean hasAccount();
    
}
