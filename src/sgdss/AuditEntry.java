package sgdss;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public class AuditEntry implements Serializable {
    @Serial private static final long serialVersionUID = 1L;
    private final String id;
    private LocalDateTime time;
    private String actorUserId;
    private String actorName;
    private String action;
    private String details;

    public AuditEntry(String actorUserId, String actorName, String action, String details) {
        this.id = UUID.randomUUID().toString();
        this.time = LocalDateTime.now();
        this.actorUserId = actorUserId;
        this.actorName = actorName;
        this.action = action;
        this.details = details;
    }

    public String getId() { return id; }
    public LocalDateTime getTime() { return time; }
    public String getActorUserId() { return actorUserId; }
    public String getActorName() { return actorName; }
    public String getAction() { return action; }
    public String getDetails() { return details; }
}
