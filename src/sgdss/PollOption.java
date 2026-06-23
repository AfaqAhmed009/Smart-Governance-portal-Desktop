package sgdss;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

public class PollOption implements Serializable {
    @Serial private static final long serialVersionUID = 1L;
    private final String id;
    private String text;
    private int voteCount;

    public PollOption(String text) {
        this.id = UUID.randomUUID().toString();
        this.text = text;
    }

    public String getId() { return id; }
    public String getText() { return text; }
    public int getVoteCount() { return voteCount; }
    public void setText(String text) { this.text = text; }
    public void addVote() { this.voteCount++; }
    @Override public String toString() { return text + " (" + voteCount + ")"; }
}
