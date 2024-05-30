
import java.time.LocalDateTime;

public interface Invite {
    String getEvent();
    LocalDateTime getDate();
    String getInvitee();
    String getHost();
    String getStatus();
    String getPriority();
    void accept();
    void reject();
    void respond();
    boolean hasResponded();
}
