import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import Exceptions.*;

public class CalendarClass implements Calendar{

    private Map<String, Account> accounts;
    private Map<String, Event> events; //Lista de eventos indexada por nome do evento
    

    public CalendarClass(){
        this.accounts = new HashMap<>();
        this.events = new HashMap<>();
    }

    @Override
    public Iterator<Account> listAccounts() throws NoAccountsException {
        if (accounts.isEmpty()) {
            throw new NoAccountsException();
        }
        return accounts.values().iterator();
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

    @Override
    public void create(String accountName, String eventName, String priority, LocalDateTime date, List<String> topics) throws NonExistentAccountException, NonExistentPriorityException, NoNewEventsException, NoHighPriorityEventsException , DuplicateEventException, PromoterOccupiedException {
        if(!accounts.containsKey(accountName)) {
            throw new NonExistentAccountException();
        }
        if(!isPriority(priority)) {
            throw new NonExistentPriorityException();
        }
        if(accounts.get(accountName).getType().equals("guest")) {
            throw  new NoNewEventsException();
        }
        if(priority.equals("high") && !accounts.get(accountName).getType().equals("manager")) {
            throw new NoHighPriorityEventsException();
        }
        if(hasEvent(accountName, eventName)) {
            throw new DuplicateEventException();
        }
        if (!isAvailable(accountName, date)) {
            throw new PromoterOccupiedException();
        }
        Invite invite = new InviteClass(eventName, date, accountName, accountName);
        accounts.get(accountName).addEvent( invite );
        events.put(eventName, new EventClass(accountName, eventName, priority, date, topics));
        events.get(eventName).addInvitee(invite);        
    }

   

    private boolean isAvailable(String accountName, LocalDateTime date) {
        return accounts.get(accountName).isAvailable(date);
    }

    private boolean hasEvent(String accountName, String eventName) {
        return accounts.get(accountName).hasEvent(eventName);
    }

    private boolean isPriority(String priority) {
        return (priority.equals("high") || priority.equals("mid"));
    }

    //TODO: Ir para uma conta e iterar todos os invites
    //TODO: Não esquecer de adicionar os invite do promotor ao mesmo quando ele cria evento e dar como aceite.
    @Override
    public Iterator<Event> eventsFrom(String accountName) throws NonExistentAccountException, NoNewEventsException{
        Account acc = accounts.get(accountName);
        if(acc == null)
            throw new NonExistentAccountException();
        if(acc.getType().equals("guest"))
            throw new NoNewEventsException();

        Iterator<Invite> it = acc.getEvents();
        List<Event> events = new ArrayList<>();
        while(it.hasNext())
            events.add(this.events.get(it.next().getEvent()));

        return events.iterator();
    }

    @Override
    public Iterator<Event> eventsByTopics(List<String> topics) {
        List<Event> events = new ArrayList<>();
        for (Event e : this.events.values()) {
            if(e.hasTopic(topics))
                events.add(e);
        }
        return events.iterator();
    }
 
    private boolean isType(String type) {
        return (type.equals("staff") || type.equals("manager") || type.equals("guest"));
    }

    @Override
    public Event getEvent(String promoter, String event) throws NonExistentAccountException, NoEventInAccountException{
        Account acc = accounts.get(promoter);
        
        if(acc == null)
            throw new NonExistentAccountException();
        if (!hasEvent(promoter, event))
        throw new NoEventInAccountException();
        
        return events.get(event);
    }
}

    
