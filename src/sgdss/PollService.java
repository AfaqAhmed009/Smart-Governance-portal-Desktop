package sgdss;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * PollService manages poll creation, voting, and lifecycle.
 */
public class PollService {
    private final AppState state;
    private final DataStore store;

    public PollService(AppState state, DataStore store) {
        this.state = state;
        this.store = store;
    }

    public Poll createPoll(User actor, String question, LocalDate expiresOn, List<String> options) {
        Poll p = new Poll(actor.getId(), question, expiresOn);
        for (String o : options) {
            if (!o.isBlank()) p.addOption(o.trim());
        }
        state.getPolls().add(p);
        state.getAudits().add(new AuditEntry(actor.getId(), actor.getDisplayName(), "POLL_CREATED", question));
        store.save();
        return p;
    }

    public boolean vote(User user, String pollId, String optionId) {
        Optional<Poll> opt = findPoll(pollId);
        if (opt.isEmpty()) return false;
        Poll poll = opt.get();
        if (!poll.isActive() || poll.expired()) return false;
        for (PollOption po : poll.getOptions()) {
            if (po.getId().equals(optionId)) {
                po.addVote();
                state.getAudits().add(new AuditEntry(user.getId(), user.getDisplayName(), "POLL_VOTE", poll.getQuestion()));
                store.save();
                return true;
            }
        }
        return false;
    }

    public List<Poll> activePolls() {
        List<Poll> result = new ArrayList<>();
        for (Poll p : state.getPolls()) if (p.isActive() && !p.expired()) result.add(p);
        return result;
    }

    public List<Poll> allPolls() {
        return new ArrayList<>(state.getPolls());
    }

    public Optional<Poll> findPoll(String id) {
        return state.getPolls().stream().filter(p -> p.getId().equals(id)).findFirst();
    }
}
