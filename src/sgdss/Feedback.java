package sgdss;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public class Feedback implements Serializable {
    @Serial private static final long serialVersionUID = 1L;
    private final String id;
    private String userId;
    private String text;
    private int rating;
    private LocalDateTime createdAt = LocalDateTime.now();

    public Feedback(String userId, String text, int rating) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.text = text;
        this.rating = rating;
    }

    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getText() { return text; }
    public int getRating() { return rating; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
