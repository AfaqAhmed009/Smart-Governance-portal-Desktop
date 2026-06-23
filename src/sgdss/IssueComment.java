package sgdss;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public class IssueComment implements Serializable {
    @Serial private static final long serialVersionUID = 1L;
    private final String id;
    private final String userId;
    private final String text;
    private final LocalDateTime createdAt;

    public IssueComment(String userId, String text) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.text = text;
        this.createdAt = LocalDateTime.now();
    }

    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getText() { return text; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
