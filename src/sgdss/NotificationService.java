package sgdss;

import java.util.ArrayList;
import java.util.List;

/**
 * NotificationService handles creation and management of system notifications.
 * Provides methods for sending notifications to users about various events.
 */
public class NotificationService {
    private final AppState state;
    private final DataStore store;

    public NotificationService(AppState state, DataStore store) {
        this.state = state;
        this.store = store;
    }

    public void notifyUser(String userId, String title, String message, 
                           Notification.Type type, String relatedEntityId) {
        Notification n = new Notification(userId, title, message, type, relatedEntityId);
        state.getNotifications().add(n);
        store.save();
    }

    public void notifyIssueAssigned(Issue issue, User officer) {
        notifyUser(officer.getId(), 
            "New Issue Assigned", 
            "You have been assigned to issue: " + issue.getTitle(),
            Notification.Type.ISSUE_ASSIGNED, issue.getId());
    }

    public void notifySlaWarning(Issue issue) {
        if (issue.getAssignedOfficerId() != null) {
            notifyUser(issue.getAssignedOfficerId(),
                "SLA Warning",
                "Issue '" + issue.getTitle() + "' deadline is approaching (" + 
                issue.getHoursUntilDeadline() + " hours remaining).",
                Notification.Type.SLA_WARNING, issue.getId());
        }
        notifyUser(issue.getSubmitterId(),
            "Issue Update",
            "Your issue '" + issue.getTitle() + "' is being processed.",
            Notification.Type.ISSUE_UPDATED, issue.getId());
    }

    public void notifySlaBreach(Issue issue) {
        if (issue.getAssignedOfficerId() != null) {
            notifyUser(issue.getAssignedOfficerId(),
                "SLA BREACH - Escalation",
                "Issue '" + issue.getTitle() + "' has breached SLA and been escalated.",
                Notification.Type.SLA_BREACH, issue.getId());
        }
        // Notify department admin
        for (User u : state.getUsers()) {
            if (u.getRole() == Role.DEPT_ADMIN && 
                u.getDepartmentId() != null && 
                u.getDepartmentId().equals(issue.getDepartmentId())) {
                notifyUser(u.getId(),
                    "Escalation Alert",
                    "Issue '" + issue.getTitle() + "' in your department has been escalated due to SLA breach.",
                    Notification.Type.ESCALATION, issue.getId());
            }
        }
    }

    public void notifyAnnouncement(Announcement a, User creator) {
        for (User u : state.getUsers()) {
            boolean shouldNotify = false;
            if (a.getCityId() == null && a.getDepartmentId() == null) {
                shouldNotify = true;
            } else if (a.getDepartmentId() != null && a.getDepartmentId().equals(u.getDepartmentId())) {
                shouldNotify = true;
            } else if (a.getCityId() != null && a.getCityId().equals(u.getCityId())) {
                shouldNotify = true;
            }
            if (shouldNotify && !u.getId().equals(creator.getId())) {
                notifyUser(u.getId(),
                    "New Announcement: " + a.getTitle(),
                    a.getContent().substring(0, Math.min(100, a.getContent().length())) + "...",
                    Notification.Type.ANNOUNCEMENT, a.getId());
            }
        }
    }

    public void notifyPollCreated(Poll poll) {
        for (User u : state.getUsers()) {
            if (u.getRole() == Role.CITIZEN) {
                notifyUser(u.getId(),
                    "New Poll Available",
                    "A new poll is available: " + poll.getQuestion(),
                    Notification.Type.POLL_CREATED, poll.getId());
            }
        }
    }

    public List<Notification> getNotificationsForUser(String userId) {
        List<Notification> result = new ArrayList<>();
        for (Notification n : state.getNotifications()) {
            if (n.getUserId().equals(userId)) result.add(n);
        }
        return result;
    }

    public List<Notification> getUnreadNotifications(String userId) {
        List<Notification> result = new ArrayList<>();
        for (Notification n : state.getNotifications()) {
            if (n.getUserId().equals(userId) && n.getStatus() != Notification.Status.READ) {
                result.add(n);
            }
        }
        return result;
    }

    public void markAllRead(String userId) {
        for (Notification n : state.getNotifications()) {
            if (n.getUserId().equals(userId) && n.getStatus() != Notification.Status.READ) {
                n.markRead();
            }
        }
        store.save();
    }

    public int getUnreadCount(String userId) {
        int count = 0;
        for (Notification n : state.getNotifications()) {
            if (n.getUserId().equals(userId) && n.getStatus() != Notification.Status.READ) count++;
        }
        return count;
    }
}
