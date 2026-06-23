package sgdss;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Notification represents a system notification sent to users.
 * Supports multiple notification types and delivery statuses.
 */
public class Notification implements Serializable {
    @Serial private static final long serialVersionUID = 1L;

    public enum Type { ISSUE_ASSIGNED, ISSUE_UPDATED, SLA_WARNING, SLA_BREACH, 
                       POLL_CREATED, ANNOUNCEMENT, ESCALATION, SYSTEM }
    public enum Status { PENDING, SENT, READ, FAILED }

    private final String id;
    private String userId;
    private String title;
    private String message;
    private Type type;
    private Status status = Status.PENDING;
    private String relatedEntityId;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime readAt;

    public Notification(String userId, String title, String message, Type type, String relatedEntityId) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.title = title;
        this.message = message;
        this.type = type;
        this.relatedEntityId = relatedEntityId;
    }

    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getTitle() { return title; }
    public String getMessage() { return message; }
    public Type getType() { return type; }
    public Status getStatus() { return status; }
    public String getRelatedEntityId() { return relatedEntityId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getReadAt() { return readAt; }

    public void setStatus(Status status) { this.status = status; }
    public void markRead() { 
        this.status = Status.READ; 
        this.readAt = LocalDateTime.now(); 
    }
}
