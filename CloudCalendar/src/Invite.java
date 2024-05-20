import java.time.LocalDateTime;

public interface Invite {
    String getEvent();
    LocalDateTime getDate();
    String getInvitee();
    String getHost();
    String getStatus();
    void accept();
    void reject();
}
