
import java.time.LocalDateTime;
import java.util.Iterator;

public interface Account extends Comparable<Account>{

    String getName();
    String getType();
    boolean hasEvent(String eventName);
    boolean isAvailable(LocalDateTime date);
    void addEvent(Invite invite);
    Iterator<Invite> getEvents();
    String getStatus(String event);
    Iterator<Invite> addInvite(Invite invite);
    Iterator<Invite> inviteResponse(String event, String response);
    void removeInvite(Invite invite);
    boolean hasResponded(String invite);
}
