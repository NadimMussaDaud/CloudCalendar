import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
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
    private static final String NO_EXISTENT_ACCOUNT = "Account %s does not exist.\n";
    private static final String UNKNOWN_PRIORITY = "Unknown priority type.\n";
    private static final String NO_NEW_EVENTS = "Guest account %s cannot create new events.\n";
    private static final String NO_HIGH_PRIORITY_EVENTS = "Account %s cannot create high priority events.\n";
    private static final String DUPLICATE_EVENT = "%s already exists in account %s.\n";
    private static final String PROMOTER_OCCUPIED = "Account %s is busy.\n";
    private static final String SCHEDULED_EVENT = "%s is scheduled.\n";
    private static final String EVENTS_FORMAT = "Account %s events:\n";
    private static final String EVENTS_ACCOUNTS_FORMAT = "%s status [invited %d] [accepted %d] [rejected %d] [unanswered %d]\n";
    private static final String NO_EVENTS_ON_TOPICS = "No events on these topics.";
    private static final String EVENTS_ON_TOPICS_FORMAT = "%s promoted by %s on %s\n";
    private static final String EVENTS_ON_TOPICS = "Events on topics %s:\n";
    private static final String NO_EVENT_IN_ACCOUNT = "%s does not exist in account %s.\n";
    private static final String EVENT_INFO_FORMAT = "%s occurs on %s:\n";
    private static final String EVENT_INFO = "%s [%s]\n";
    
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
        List<String> topics = Arrays.asList(in.nextLine().trim().split(" "));

        try{
            Iterator<Event> it = calendar.eventsByTopics(topics);

            System.out.printf(EVENTS_ON_TOPICS, String.join(" ", topics));
            while(it.hasNext()){
                Event event = it.next();
                List<String> eventTopics = new ArrayList<>();
                event.getTopics().forEachRemaining(eventTopics :: add);

                System.out.printf(EVENTS_ON_TOPICS_FORMAT,event.getName(),event.getHost(), String.join(" ", eventTopics) );
            }
        }catch(NoEventsOnTopicsException e){
            System.out.printf(NO_EVENTS_ON_TOPICS);
        }
    }

    private static void event(Scanner in) {
        String promoter = in.next();
        String event = in.nextLine();

        try {
            Event e = calendar.getEvent(promoter,event);
            Iterator<Invite> it = e.getInvitees();
    
            System.out.printf(EVENT_INFO_FORMAT, event, e.getDate());
            while (it.hasNext()) {
                Invite i = it.next();
                System.out.printf(EVENT_INFO, i.getInvitee(), i.getStatus());
            }
        } catch (NonExistentAccountException e) {
            System.out.printf(NO_EXISTENT_ACCOUNT, promoter);        
        } catch (NoEventInAccountException e){
            System.out.printf(NO_EVENT_IN_ACCOUNT, event, promoter);
        }
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

        try {
            Iterator<Event> it = calendar.eventsFrom(accountName);
            System.out.printf(EVENTS_FORMAT, accountName);
            while(it.hasNext()){
                Event event = it.next();
                System.out.printf(EVENTS_ACCOUNTS_FORMAT, event.getName(), event.getInvitedNumber(), event.getAccepts(), event.getRejections(), event.getUnanswered());
            }
        } catch (NonExistentAccountException e) {
            System.out.printf(NO_EXISTENT_ACCOUNT, accountName);
        } catch (NoNewEventsException e){
            System.out.printf(NO_NEW_EVENTS,accountName);
        }
    }

    private static void create(Scanner in) {
       String accountName = in.nextLine().trim();
       String eventName = in.nextLine().trim();
       String priority = in.next(); 
    
       LocalDateTime date;
       date = LocalDateTime.of(in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt() ,0);
       in.nextLine();

       List<String> topics = Arrays.asList(in.nextLine().trim().split(" "));
       

       try{
            calendar.create(accountName, eventName, priority, date, topics);
            System.out.printf(SCHEDULED_EVENT,eventName);
        }
        catch(NonExistentAccountException e){
            System.out.printf(NO_EXISTENT_ACCOUNT, accountName);
        }
        catch(NonExistentPriorityException e){
            System.out.printf(UNKNOWN_PRIORITY);
        }
        catch(NoNewEventsException e){
            System.out.printf(NO_NEW_EVENTS,accountName);
        }
        catch(NoHighPriorityEventsException e){
            System.out.printf(NO_HIGH_PRIORITY_EVENTS, accountName);
        }
        catch(DuplicateEventException e){
            System.out.printf(DUPLICATE_EVENT, eventName, accountName);
        }
        catch(PromoterOccupiedException e){
            System.out.printf(PROMOTER_OCCUPIED, accountName);
        }

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