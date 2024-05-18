import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import Exceptions.*;

/**
 * Main Skeleton provided by ClaudeAI
 */

public class Main {
    private static final String INVALID_COMMAND = "Invalid command.";
    private static final String HELP_MESSAGE_FORMAT = "%s - %s\n";
    private static final String QUIT_MESSAGE = "Bye.";
    private static final String ACCOUNT_REGISTERED_MESSAGE = "%s was registered.\n";
    private static final String DUPLICATE_ACCOUTN_MESSAGE = "%s already exists.\n";
    private static final String NON_EXISTENT_TYPE_MESSAGE = "Unknown account type.\n";
    private static final String NO_ACCOUNTS = "No accounts registered.\n";
    private static final String ALL_ACCOUNTS = "All accounts:\n";
    private static final String LIST_ACCOUNTS_FORMAT = "%s [%s]\n";
    private static Calendar calendar;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        calendar = new CalendarClass();
        processCommands(scanner);
        scanner.close();
    }

    private static void processCommands(Scanner in){
        Commands command;

        do {
            String input = in.next().trim().toUpperCase();

            command = parseCommand(input);
            processCommand(command, in);
        } while (!command.equals(Commands.EXIT));
    }

    private static void processCommand(Commands command, Scanner in) {
        switch (command) {
            case REGISTER:
                register(in);
                break;
            case ACCOUNTS:
                list();
                break;
            case CREATE:
                create(in);
                break;
            case EVENTS:
                events(in);
                break;
            case INVITE:
                invite(in);
                break;
            case RESPONSE:
                response(in);
                break;
            case EVENT:
                event(in);
                break;
            case TOPICS:
                topics(in);
                break;
            case HELP:
                help();
                break;
            case EXIT:
                exitMessage();
                break;
            case UNKNOWN:
                invalidCommand();
                break;
        }
    }

    private static void list() {
        try {
            Iterator<Account> it = calendar.listAccounts();
            System.out.printf(ALL_ACCOUNTS);
            while (it.hasNext()) {
                Account account = it.next();
                System.out.printf(LIST_ACCOUNTS_FORMAT, account.getName(), account.getType());
            }
        } catch (NoAccountsException e) {
            System.out.printf(NO_ACCOUNTS);
        }
    }

    private static void topics(Scanner in) {
    List<String> topics = new ArrayList<>();
       while (in.hasNext()) 
        topics.add(in.next());
       in.nextLine();
    }

    private static void event(Scanner in) {
        String promoter = in.next();
        String event = in.nextLine();
    }

    private static void response(Scanner in) {
        String invitee = in.nextLine().trim();
        String promoter = in.next();
        String event = in.nextLine();
        String response = in.nextLine();

    }

    private static void invite(Scanner in) {
        String invitee = in.nextLine().trim();
        String promoter = in.next();
        String event = in.nextLine();

    }

    private static void events(Scanner in) {
        String accountName = in.nextLine().trim();


    }

    private static void create(Scanner in) {
       String accountName = in.nextLine().trim();
       String eventName = in.nextLine().trim();
       String priority = in.next(); 
       DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MM dd HH");
       LocalDateTime now = LocalDateTime.now();
       String date = now.format(formatter);
       List<String> topics = new ArrayList<>();
       while (in.hasNext()) 
        topics.add(in.next());
       in.nextLine();

    }

    private static void register(Scanner in) {
       String accountName = in.next();
       String type = in.nextLine().trim();

        try{
            calendar.register(accountName, type);
            System.out.printf(ACCOUNT_REGISTERED_MESSAGE,accountName);
        }
        catch(DuplicateAccountException e){
            System.out.printf(DUPLICATE_ACCOUTN_MESSAGE,accountName);
        }
        catch(NonExistentTypeException e){
            System.out.printf(NON_EXISTENT_TYPE_MESSAGE);
        }
        
    }


    private static void help() {
        for (Commands command : Commands.values()) {
            if (!command.equals(Commands.UNKNOWN)) {
                System.out.printf(HELP_MESSAGE_FORMAT, command.getName(), command.getDescription());
            }
        }
    }

    private static Commands parseCommand(String commandString) {
        try {
            return Commands.valueOf(commandString);
        } catch (IllegalArgumentException e) {
            return Commands.UNKNOWN;
        }
    }
   
    private static void invalidCommand() {
        System.out.println(INVALID_COMMAND);
    }

    private static void exitMessage() {
        System.out.println(QUIT_MESSAGE);
    }
   
}