
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import Exceptions.*;

public class CalendarClass implements Calendar{
    
    private static final String STAFF = "staff";
    private static final String MANAGER = "manager";
    private static final String GUEST = "guest";
    private static final String HIGH = "high";
    private static final String MID = "mid";
    private static final String ACCEPT = "accept";
    private static final String REJECT = "reject";
    private static final String REJECTED = "rejected";

    private Map<String, Account> accounts;
    private Map<Integer, Event> events; // A key its an Hash between promoter and event name

    public CalendarClass(){
        this.accounts = new HashMap<>();
        this.events = new HashMap<>();
    }

    @Override
    public Iterator<Account> listAccounts() throws NoAccountsException {
        if (accounts.isEmpty()) {
            throw new NoAccountsException();
        }
        List<Account> list = new ArrayList<>(accounts.values());
        Collections.sort(list);
        return list.iterator();
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
            case STAFF ->  {
                Account acc = new StaffAccount(accountName);
                accounts.put(accountName, acc);
            }
            case MANAGER -> {
                Account acc = new ManagerAccount(accountName);
                accounts.put(accountName, acc);
            }
            case GUEST ->  {
                Account acc = new GuestAccount(accountName);
                accounts.put(accountName, acc);
            }
        }
    }

    @Override
    public void create(String accountName, String eventName, String priority, LocalDateTime date, List<String> topics) throws NonExistentAccountException, NonExistentPriorityException, NoNewEventsException, NoHighPriorityEventsException , DuplicateEventException, PromoterOccupiedException {
        Account acc = accounts.get(accountName);

        if(acc == null) {
            throw new NonExistentAccountException();
        }
        if(!isPriority(priority)) {
            throw new NonExistentPriorityException();
        }
        if(accounts.get(accountName).getType().equals(GUEST)) {
            throw  new NoNewEventsException();
        }
        if(priority.equals(HIGH) && !accounts.get(accountName).getType().equals(MANAGER)) {
            throw new NoHighPriorityEventsException();
        }
        if(hasEvent(accountName, eventName)) {
            throw new DuplicateEventException();
        }

        if (!acc.isAvailable(date)) {
            throw new PromoterOccupiedException();
        }
        Invite invite = new InviteClass(eventName, priority, date, accountName, accountName);
        accounts.get(accountName).addEvent( invite );

        int eventKey = Objects.hash(eventName, accountName);
        events.put(eventKey, new EventClass(accountName, eventName, priority, date, topics));
        events.get(eventKey).addInvitee(invite);        
    }

    @Override
    public Iterator<Event> eventsFrom(String accountName) throws NonExistentAccountException{
        Account acc = accounts.get(accountName);
        if(acc == null)
            throw new NonExistentAccountException();
        
        Iterator<Invite> it = acc.getEvents();
        List<Event> events = new ArrayList<>();
        while(it.hasNext()){
            Invite inv = it.next();
            int key = Objects.hash(inv.getEvent(), inv.getHost());
            events.add(this.events.get(key));
        }

        return events.iterator();
    }

    @Override
    public Iterator<Event> eventsByTopics(List<String> topics) throws NoEventsOnTopicsException {
        List<Event> eventsWithTopics = new ArrayList<>();
        for (Event event : events.values()) {
            if (event.hasTopic(topics)) {
                eventsWithTopics.add(event);
            }
        }

        if(eventsWithTopics.isEmpty()) throw new NoEventsOnTopicsException();

        //Comparator provided by AI
        Collections.sort(eventsWithTopics, new Comparator<Event>() {
            @Override
            public int compare(Event e1, Event e2) {
                int commonTopicsCount1 = getCommonTopicsCount(e1, topics);
                int commonTopicsCount2 = getCommonTopicsCount(e2, topics);

                if (commonTopicsCount1 != commonTopicsCount2) {
                    return Integer.compare(commonTopicsCount2, commonTopicsCount1); // Descending order
                } else {
                    int eventDescriptionCompare = e1.getName().compareTo(e2.getName());
                    if (eventDescriptionCompare != 0) {
                        return eventDescriptionCompare;
                    } else {
                        return e1.getHost().compareTo(e2.getHost());
                    }
                }
            }
        });

        return eventsWithTopics.iterator();
    }

private int getCommonTopicsCount(Event event, List<String> topics) {
    int count = 0;
    for (String topic : topics) {
        List<String> list = new ArrayList<>();
        list.add(topic);
        if (event.hasTopic(list)) {
            count++;
        }
    }
    return count;
}
 
    @Override
    public Event getEvent(String promoter, String event) throws NonExistentAccountException, NoEventInAccountException{
        Account acc = accounts.get(promoter);
        
        if(acc == null)
            throw new NonExistentAccountException();
        if (!hasEvent(promoter, event))
            throw new NoEventInAccountException();
        
        int key = Objects.hash(event,promoter);

        return events.get(key);
    }

    @Override
    public Iterator<Invite> response(String invitee, String promoter, String event, String response) throws NonExistentAccountException, UnknownResponseException, NoEventInAccountException, NotInInvitationListException, AlreadyRespondedException{
        Account acc1 = accounts.get(invitee);
        Account acc2 = accounts.get(promoter);
        if(acc1 == null)
            throw new NonExistentAccountException(invitee);
        if(acc2 == null)
            throw new NonExistentAccountException(promoter);
        if(!isResponse(response))
            throw new UnknownResponseException();
        if (!hasEvent(promoter, event))
            throw new NoEventInAccountException();

        int key = Objects.hash(event,promoter);    
        if(!events.get(key).hasInvite(invitee))
            throw new NotInInvitationListException();
        if(acc1.hasResponded(event))
            throw new AlreadyRespondedException();

        return acc1.inviteResponse(event, response);

        /*Iterator<Invite> it = acc1.getEvents(); 
        List<Invite> rejected = new ArrayList<>();
        while (it.hasNext()) {
            Invite invite = it.next();
            if(invite.getStatus().equals(REJECTED) && !invite.hasResponded())
                rejected.add(invite);
        }

        return rejected.iterator();*/
    }

    @Override
    public Iterator<Invite> invite(String invitee, String promoter, String event) throws NonExistentAccountException,
            NoEventInAccountException, AlreadyInvitedException, AttendingOtherEventException {
        Account acc1 = accounts.get(invitee);
        Account acc2 = accounts.get(promoter);
        

        if(acc1 == null)
            throw new NonExistentAccountException(invitee);
        if(acc2 == null)
            throw new NonExistentAccountException(promoter);
        if (!hasEvent(promoter, event))
            throw new NoEventInAccountException();
        if(acc1.hasEvent(event))
            throw new AlreadyInvitedException();

        int key = Objects.hash(event,promoter);    
        Event e = events.get(key);
        //não tem nenhum evento nesta data em que é o promotor
        if(!acc1.isAvailable(e.getDate()) ) 
            throw new AttendingOtherEventException();

        // Adds the new invite. In case something is removed it returns a iterator for it,
        // Otherwise gets the rejected invites in case it's a staff member
        List<Invite> oldInvites = new ArrayList<>();
        
        Invite newInvite = new InviteClass(event, e.getPriority(), e.getDate(), invitee, promoter);
        e.addInvitee(newInvite);
        
        // TODO: Se evento não for HIGH então só adicionar, se for adicionar e rejeitar os outros 
        Iterator<Invite> removed = acc1.addInvite(newInvite);
        while (removed.hasNext()) {
            oldInvites.add(removed.next());
        }
        /*if(removed == null && acc1.getType().equals(STAFF) && e.getPriority().equals(HIGH)){
            Iterator<Invite> it = acc1.getEvents();
            while (it.hasNext()) {
                Invite invite = it.next();
                if(invite.getStatus().equals(REJECTED))
                    oldInvites.add(invite);
            }
        }else */
        if(acc1.getType().equals(STAFF) && e.getPriority().equals(HIGH)){
            //oldInvites.add(removed);
            for (Invite invite : oldInvites) {
                
                if(invite.getHost().equals(invitee)){
                    key = Objects.hash(invite.getEvent(),invitee);    
                    events.remove(key);
                    for (Account acc : accounts.values()) 
                        acc.removeInvite(invite);
                }
            }
        }

        return oldInvites.iterator();
    }

    private boolean isResponse(String response) {
        return (response.equals(ACCEPT) || response.equals(REJECT));
    }

    private boolean isAvailable(String accountName, LocalDateTime date) {
        return accounts.get(accountName).isAvailable(date);
    }

    private boolean hasEvent(String accountName, String eventName) {
        return accounts.get(accountName).hasEvent(eventName);
    }

    private boolean isPriority(String priority) {
        return (priority.equals(HIGH) || priority.equals(MID));
    }

    private boolean isType(String type) {
        return (type.equals(STAFF) || type.equals(MANAGER) || type.equals(GUEST));
    }
}

    
