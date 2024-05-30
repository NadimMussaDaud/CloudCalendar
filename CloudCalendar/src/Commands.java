

public enum Commands {

    REGISTER("register", "registers a new account"),
    ACCOUNTS("accounts", "lists all registered accounts"),
    CREATE("create", "creates a new event"),
    EVENTS("events", "lists all events of an account"),
    INVITE("invite", "invites an user to an event"),
    RESPONSE("response", "response to an invitation"),
    EVENT("event", "shows detailed information of an event"),
    TOPICS("topics", "shows all events that cover a list of topics"),
    HELP("help", "shows the available commands"),
    EXIT("exit", "terminates the execution of the program"),
    UNKNOWN("Unknown", "Type help to see available commands.");

    private final String name;
    private final String description;

    Commands(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
