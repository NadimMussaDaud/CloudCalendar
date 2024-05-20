import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;

import Exceptions.*;

public interface Calendar {

    Iterator<Account> listAccounts() throws NoAccountsException;

    void register(String accountName, String type) throws NonExistentTypeException, DuplicateAccountException;


    void create(String accountName, String eventName, String priority, LocalDateTime date, List<String> topics) throws NonExistentAccountException, NonExistentPriorityException, NoNewEventsException, NoHighPriorityEventsException, DuplicateEventException, PromoterOccupiedException;

    Iterator<Event> eventsFrom(String accountName) throws NonExistentAccountException, NoNewEventsException;

    Iterator<Event> eventsByTopics(List<String> topics) throws NoEventsOnTopicsException;

    Event getEvent(String promoter, String event) throws NonExistentAccountException, NoEventInAccountException;
    
}
