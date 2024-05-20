import java.time.LocalDateTime;
import java.util.Iterator;

public interface Account {

    String getName();

    String getType();

    boolean hasEvent(String eventName);

    boolean isAvailable(LocalDateTime date);

    void addEvent(Invite invite);

    Iterator<Invite> getEvents();

    Object getStatus(String event);
    
}
