package sgdss;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * AppState holds all application data in memory and is serialized to disk.
 * This is the central data model for the entire SGDSS application.
 */
public class AppState implements Serializable {
    @Serial private static final long serialVersionUID = 2L;

    private final List<User> users = new ArrayList<>();
    private final List<City> cities = new ArrayList<>();
    private final List<Department> departments = new ArrayList<>();
    private final List<Category> categories = new ArrayList<>();
    private final List<Issue> issues = new ArrayList<>();
    private final List<Poll> polls = new ArrayList<>();
    private final List<Announcement> announcements = new ArrayList<>();
    private final List<Feedback> feedbacks = new ArrayList<>();
    private final List<AuditEntry> audits = new ArrayList<>();
    private final List<Notification> notifications = new ArrayList<>();
    private final List<SystemSetting> settings = new ArrayList<>();

    public List<User> getUsers() { return users; }
    public List<City> getCities() { return cities; }
    public List<Department> getDepartments() { return departments; }
    public List<Category> getCategories() { return categories; }
    public List<Issue> getIssues() { return issues; }
    public List<Poll> getPolls() { return polls; }
    public List<Announcement> getAnnouncements() { return announcements; }
    public List<Feedback> getFeedbacks() { return feedbacks; }
    public List<AuditEntry> getAudits() { return audits; }
    public List<Notification> getNotifications() { return notifications; }
    public List<SystemSetting> getSettings() { return settings; }
}
