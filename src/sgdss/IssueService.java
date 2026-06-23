package sgdss;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * IssueService handles all issue-related operations including submission,
 * updates, comments, SLA monitoring, and smart engine integration.
 */
public class IssueService {
    private final AppState state;
    private final DataStore store;
    private final SmartEngine smartEngine = new SmartEngine();
    private final NotificationService notificationService;

    public IssueService(AppState state, DataStore store) {
        this.state = state;
        this.store = store;
        this.notificationService = new NotificationService(state, store);
    }

    public Issue submitIssue(User user, String cityId, String departmentId, String categoryId, String title, String description, String location, int severity) {
        Issue issue = new Issue(user.getId(), cityId, departmentId, categoryId, title, description, location, severity);
        Category category = findCategory(categoryId).orElse(null);
        Department dept = findDepartment(departmentId).orElse(null);
        Priority predicted = smartEngine.predictPriority(issue, category, dept);
        issue.setPredictedPriority(predicted);
        issue.setEstimatedSlaHours(smartEngine.estimateSlaHours(predicted, severity));
        Optional<Issue> dup = smartEngine.detectDuplicate(issue, state.getIssues(), category);
        dup.ifPresent(d -> issue.setDuplicateOfIssueId(d.getId()));
        issue.setSmartNote(smartEngine.smartNote(issue, predicted, dup));
        String officerId = smartEngine.suggestOfficer(issue, state.getUsers());
        if (officerId != null) {
            issue.setAssignedOfficerId(officerId);
            issue.setStatus(Status.ASSIGNED);
            User officer = findUser(officerId).orElse(null);
            if (officer != null) {
                notificationService.notifyIssueAssigned(issue, officer);
            }
        }
        state.getIssues().add(issue);
        state.getAudits().add(new AuditEntry(user.getId(), user.getDisplayName(), "ISSUE_SUBMITTED", issue.getTitle()));
        store.save();
        return issue;
    }

    public void updateIssue(User actor, Issue issue, Status status, String note, String assignedOfficerId) {
        Status oldStatus = issue.getStatus();
        issue.setStatus(status);
        if (note != null && !note.isBlank()) issue.setSmartNote((issue.getSmartNote() == null ? "" : issue.getSmartNote() + " ") + note);
        if (assignedOfficerId != null && !assignedOfficerId.isBlank()) {
            issue.setAssignedOfficerId(assignedOfficerId);
            if (oldStatus == Status.NEW && status != Status.NEW) {
                issue.setStatus(Status.ASSIGNED);
            }
            User officer = findUser(assignedOfficerId).orElse(null);
            if (officer != null) {
                notificationService.notifyIssueAssigned(issue, officer);
            }
        }
        issue.setUpdatedAt(LocalDateTime.now());
        state.getAudits().add(new AuditEntry(actor.getId(), actor.getDisplayName(), "ISSUE_UPDATED", issue.getTitle() + " -> " + status.displayName()));
        store.save();
    }

    public void addComment(User actor, Issue issue, String text) {
        issue.addComment(new IssueComment(actor.getId(), text));
        state.getAudits().add(new AuditEntry(actor.getId(), actor.getDisplayName(), "ISSUE_COMMENT", issue.getTitle()));
        store.save();
    }

    public void resolveIssue(User actor, Issue issue, String resolutionNotes) {
        issue.setStatus(Status.RESOLVED);
        issue.setResolutionNotes(resolutionNotes);
        issue.setUpdatedAt(LocalDateTime.now());
        state.getAudits().add(new AuditEntry(actor.getId(), actor.getDisplayName(), "ISSUE_RESOLVED", issue.getTitle()));
        store.save();
    }

    public void checkAndEscalateSLAs() {
        for (Issue issue : state.getIssues()) {
            issue.checkSlaStatus();
            if (issue.isEscalated() && issue.getEscalationReason() != null && !issue.getEscalationReason().isEmpty()) {
                notificationService.notifySlaBreach(issue);
                state.getAudits().add(new AuditEntry(null, "SYSTEM", "SLA_ESCALATION", issue.getTitle() + " - " + issue.getEscalationReason()));
            }
        }
        store.save();
    }

    public List<Issue> getOverdueIssues() {
        List<Issue> result = new ArrayList<>();
        for (Issue issue : state.getIssues()) {
            if (issue.getSlaDeadline() != null && !issue.isSlaBreached() &&
                issue.getStatus() != Status.RESOLVED && issue.getStatus() != Status.CLOSED && issue.getStatus() != Status.REJECTED) {
                if (LocalDateTime.now().isAfter(issue.getSlaDeadline())) {
                    result.add(issue);
                }
            }
        }
        return result;
    }

    public List<Issue> getIssuesByStatus(Status status) {
        List<Issue> result = new ArrayList<>();
        for (Issue i : state.getIssues()) if (i.getStatus() == status) result.add(i);
        return result;
    }

    public List<Issue> getIssuesByPriority(Priority priority) {
        List<Issue> result = new ArrayList<>();
        for (Issue i : state.getIssues()) if (i.getPredictedPriority() == priority) result.add(i);
        return result;
    }

    public List<Issue> searchIssues(String query) {
        List<Issue> result = new ArrayList<>();
        String q = query.toLowerCase();
        for (Issue i : state.getIssues()) {
            if ((i.getTitle() != null && i.getTitle().toLowerCase().contains(q)) ||
                (i.getDescription() != null && i.getDescription().toLowerCase().contains(q)) ||
                (i.getLocation() != null && i.getLocation().toLowerCase().contains(q)) ||
                i.getId().toLowerCase().contains(q)) {
                result.add(i);
            }
        }
        return result;
    }

    public List<Issue> visibleIssues(User user) {
        List<Issue> result = new ArrayList<>();
        for (Issue issue : state.getIssues()) {
            if (user.getRole() == Role.GLOBAL_ADMIN || user.getRole() == Role.CITY_ADMIN) {
                result.add(issue);
            } else if (user.getRole() == Role.DEPT_ADMIN) {
                if (user.getDepartmentId() != null && user.getDepartmentId().equals(issue.getDepartmentId())) result.add(issue);
            } else if (user.getRole() == Role.OFFICER) {
                if (user.getId().equals(issue.getAssignedOfficerId()) || 
                    (user.getDepartmentId() != null && user.getDepartmentId().equals(issue.getDepartmentId()))) result.add(issue);
            } else if (user.getRole() == Role.CITIZEN) {
                if (user.getId().equals(issue.getSubmitterId())) result.add(issue);
            }
        }
        return result;
    }

    public Optional<Issue> findIssue(String id) {
        return state.getIssues().stream().filter(i -> i.getId().equals(id)).findFirst();
    }

    public Optional<User> findUser(String id) {
        return state.getUsers().stream().filter(u -> u.getId().equals(id)).findFirst();
    }

    public Optional<Department> findDepartment(String id) {
        return state.getDepartments().stream().filter(d -> d.getId().equals(id)).findFirst();
    }

    public Optional<Category> findCategory(String id) {
        return state.getCategories().stream().filter(c -> c.getId().equals(id)).findFirst();
    }

    public Optional<City> findCity(String id) {
        return state.getCities().stream().filter(c -> c.getId().equals(id)).findFirst();
    }
}
