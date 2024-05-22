import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private static final String AVAILABLE_COMMANDS = "Available commands:\n";
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
    private static final String NO_NEW_EVENTS = "Guest account %s cannot create events.\n";
    private static final String NO_HIGH_PRIORITY_EVENTS = "Account %s cannot create high priority events.\n";
    private static final String DUPLICATE_EVENT = "%s already exists in account %s.\n";
    private static final String PROMOTER_OCCUPIED = "Account %s is busy.\n";
    private static final String SCHEDULED_EVENT = "%s is scheduled.\n";
    private static final String EVENTS_FORMAT = "Account %s events:\n";
    private static final String EVENTS_ACCOUNTS_FORMAT = "%s status [invited %d] [accepted %d] [rejected %d] [unanswered %d]\n";
    private static final String NO_EVENTS_ON_TOPICS = "No events on these topics.";
    private static final String EVENTS_ON_TOPICS_FORMAT = "%s promoted by %s on %s\n";
    private static final String EVENTS_ON_TOPICS = "Events on topics %s:\n";
    private static final String NO_EVENT_IN_ACCOUNT = "%s does not exist in account %s\n";
    private static final String NO_EVENTS = "Account %s has no events.\n";
    private static final String EVENT_INFO_FORMAT = "%s occurs on %s:\n";
    private static final String EVENT_INFO = "%s [%s]\n";
    private static final String UNKNOWN_RESPONSE = "Unknown event response.\n";
    private static final String NOT_IN_LIST = "Account %s is not on the invitation list.\n";
    private static final String ALREADY_RESPONDED = "Account %s has already responded.\n";
    private static final String INVITATION_RESPONSE = "Account %s has replied %s to the invitation.\n";
    private static final String INVITATION_RESPONSE_FORMAT = "%s promoted by %s was rejected\n";
    private static final String INVITATION_ACCEPTED = "%s accepted the invitation.\n";
    private static final String INVITED_MESSAGE = "%s was invited.\n";
    private static final String EVENT_REMOVED = "%s promoted by %s was removed.\n";
    private static final String ALREADY_INVITED = "%s was already invited.\n";
    private static final String IN_OTHER_EVENT = "%s is already attending another event.\n";
    public static final String STAFF = "staff";
    public static final String MANAGER = "manager";
    public static final String GUEST = "guest";
    public static final String HIGH = "high";
    public static final String MID = "mid";
    public static final String ACCEPT = "accept";
    public static final String REJECT = "reject";
    public static final String REJECTED = "rejected";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH'h'");

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
            case REGISTER -> register(in);
            case ACCOUNTS -> list();
            case CREATE   -> create(in);
            case EVENTS   -> events(in);
            case INVITE   -> invite(in);
            case RESPONSE -> response(in);
            case EVENT    -> event(in);
            case TOPICS   -> topics(in);
            case HELP     -> help();
            case EXIT     -> exitMessage();
            case UNKNOWN  -> invalidCommand();
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
        String event = in.nextLine().trim();

        try {
            Event e = calendar.getEvent(promoter,event);
            Iterator<Invite> it = e.getInvitees();
    
            System.out.printf(EVENT_INFO_FORMAT, event, e.getDate().format(formatter));
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

        try {
            Iterator<Invite> it = calendar.response(invitee, promoter, event, response);

            System.out.printf(INVITATION_RESPONSE,invitee,response);

            while (it.hasNext()) {
                Invite invite = it.next();
                System.out.printf(INVITATION_RESPONSE_FORMAT, invite.getEvent(), invite.getHost());
            }


        } catch (NonExistentAccountException e) {
            System.out.printf(NO_EXISTENT_ACCOUNT, e.getName());        
        } catch (UnknownResponseException e){
            System.out.printf(UNKNOWN_RESPONSE);
        } catch (NoEventInAccountException e){
            System.out.printf(NO_EVENT_IN_ACCOUNT, event, promoter);
        } catch (NotInInvitationListException e){
            System.out.printf(NOT_IN_LIST, invitee);
        } catch (AlreadyRespondedException e){
            System.out.printf(ALREADY_RESPONDED, invitee );
        }

    }

    private static void invite(Scanner in) {
        String invitee = in.nextLine().trim();
        String promoter = in.next();
        String event = in.nextLine();

        try {
            Iterator<Invite> it = calendar.invite(invitee,promoter,event);

            if(it.hasNext()){
                System.out.printf(INVITATION_ACCEPTED, invitee);

                while (it.hasNext()) {
                    Invite invite = it.next();

                    if(invite.getHost().equals(invitee)) 
                        System.out.printf(EVENT_REMOVED, event, invitee);
                    else
                        System.out.printf(INVITATION_RESPONSE_FORMAT, invite.getEvent(), invite.getHost());
                }
            }else{
                System.out.printf(INVITED_MESSAGE, invitee);
            }
            
            

        } catch (NonExistentAccountException e) {
            System.out.printf(NO_EXISTENT_ACCOUNT, e.getName()); 
        } catch (NoEventInAccountException e){
            System.out.printf(NO_EVENT_IN_ACCOUNT, event, promoter);
        } catch (AlreadyInvitedException e){
            System.out.printf(ALREADY_INVITED,invitee);
        } catch (AttendingOtherEventException e){
            System.out.printf(IN_OTHER_EVENT, invitee);
        }

    }

    private static void events(Scanner in) {
        String accountName = in.nextLine().trim();

        try {
            Iterator<Event> it = calendar.eventsFrom(accountName);
            if(it.hasNext()){
                System.out.printf(EVENTS_FORMAT, accountName);
                while(it.hasNext()){
                    Event event = it.next();
                    System.out.printf(EVENTS_ACCOUNTS_FORMAT, event.getName(), event.getInvitedNumber(), event.getAccepts(), event.getRejections(), event.getUnanswered());
                }
            }else{
                System.out.printf(NO_EVENTS, accountName);
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
        System.out.printf(AVAILABLE_COMMANDS);
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