package sgdss;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Poll implements Serializable {
    @Serial private static final long serialVersionUID = 1L;
    private final String id;
    private String createdByUserId;
    private String question;
    private final List<PollOption> options = new ArrayList<>();
    private boolean active = true;
    private LocalDate expiresOn;
    private LocalDateTime createdAt = LocalDateTime.now();

    public Poll(String createdByUserId, String question, LocalDate expiresOn) {
        this.id = UUID.randomUUID().toString();
        this.createdByUserId = createdByUserId;
        this.question = question;
        this.expiresOn = expiresOn;
    }

    public String getId() { return id; }
    public String getCreatedByUserId() { return createdByUserId; }
    public String getQuestion() { return question; }
    public List<PollOption> getOptions() { return options; }
    public boolean isActive() { return active; }
    public LocalDate getExpiresOn() { return expiresOn; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setCreatedByUserId(String createdByUserId) { this.createdByUserId = createdByUserId; }
    public void setQuestion(String question) { this.question = question; }
    public void setActive(boolean active) { this.active = active; }
    public void setExpiresOn(LocalDate expiresOn) { this.expiresOn = expiresOn; }
    public void addOption(String text) { this.options.add(new PollOption(text)); }

    public boolean expired() {
        return expiresOn != null && LocalDate.now().isAfter(expiresOn);
    }

    @Override public String toString() { return question; }
}
