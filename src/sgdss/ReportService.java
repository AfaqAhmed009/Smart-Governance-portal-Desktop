package sgdss;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * ReportService generates comprehensive analytics and reports
 * for the SGDSS system including issue trends, officer performance,
 * and departmental statistics.
 */
public class ReportService {
    private final AppState state;

    public ReportService(AppState state) {
        this.state = state;
    }

    public Map<String, Integer> issueCountsByStatus() {
        Map<String, Integer> counts = new LinkedHashMap<>();
        for (Status s : Status.values()) counts.put(s.displayName(), 0);
        for (Issue i : state.getIssues()) counts.put(i.getStatus().displayName(), counts.get(i.getStatus().displayName()) + 1);
        return counts;
    }

    public Map<String, Integer> issueCountsByPriority() {
        Map<String, Integer> counts = new LinkedHashMap<>();
        for (Priority p : Priority.values()) counts.put(p.displayName(), 0);
        for (Issue i : state.getIssues()) counts.put(i.getPredictedPriority().displayName(), counts.get(i.getPredictedPriority().displayName()) + 1);
        return counts;
    }

    public Map<String, Integer> issueCountsByDepartment() {
        Map<String, Integer> counts = new LinkedHashMap<>();
        for (Department d : state.getDepartments()) counts.put(d.getName(), 0);
        for (Issue i : state.getIssues()) {
            String deptName = findDepartmentName(i.getDepartmentId());
            counts.put(deptName, counts.getOrDefault(deptName, 0) + 1);
        }
        return counts;
    }

    public Map<String, Integer> issueCountsByCity() {
        Map<String, Integer> counts = new LinkedHashMap<>();
        for (City c : state.getCities()) counts.put(c.getName(), 0);
        for (Issue i : state.getIssues()) {
            String cityName = findCityName(i.getCityId());
            counts.put(cityName, counts.getOrDefault(cityName, 0) + 1);
        }
        return counts;
    }

    public int openIssues() {
        int count = 0;
        for (Issue i : state.getIssues()) if (i.getStatus() == Status.NEW || i.getStatus() == Status.ASSIGNED || i.getStatus() == Status.IN_PROGRESS) count++;
        return count;
    }

    public int slaBreachedIssues() {
        int count = 0;
        for (Issue i : state.getIssues()) if (i.isSlaBreached()) count++;
        return count;
    }

    public double averageResolutionTime() {
        int total = 0, resolved = 0;
        for (Issue i : state.getIssues()) {
            if (i.getStatus() == Status.RESOLVED || i.getStatus() == Status.CLOSED) {
                total += i.getResolutionTimeHours();
                resolved++;
            }
        }
        return resolved == 0 ? 0.0 : (double) total / resolved;
    }

    public Map<String, Double> officerPerformance() {
        Map<String, Integer> assigned = new HashMap<>();
        Map<String, Integer> resolved = new HashMap<>();
        for (Issue i : state.getIssues()) {
            if (i.getAssignedOfficerId() != null) {
                assigned.put(i.getAssignedOfficerId(), assigned.getOrDefault(i.getAssignedOfficerId(), 0) + 1);
                if (i.getStatus() == Status.RESOLVED || i.getStatus() == Status.CLOSED) {
                    resolved.put(i.getAssignedOfficerId(), resolved.getOrDefault(i.getAssignedOfficerId(), 0) + 1);
                }
            }
        }
        Map<String, Double> performance = new LinkedHashMap<>();
        for (String officerId : assigned.keySet()) {
            String name = findUserName(officerId);
            int total = assigned.get(officerId);
            int res = resolved.getOrDefault(officerId, 0);
            performance.put(name, total == 0 ? 0.0 : (double) res / total * 100.0);
        }
        return performance;
    }

    public Map<String, Integer> monthlyIssueTrend() {
        Map<String, Integer> trend = new LinkedHashMap<>();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM");
        for (Issue i : state.getIssues()) {
            String month = i.getCreatedAt().format(fmt);
            trend.put(month, trend.getOrDefault(month, 0) + 1);
        }
        return trend;
    }

    public double averageFeedbackRating() {
        if (state.getFeedbacks().isEmpty()) return 0.0;
        int total = 0;
        for (Feedback f : state.getFeedbacks()) total += f.getRating();
        return (double) total / state.getFeedbacks().size();
    }

    public Map<String, Integer> feedbackByRating() {
        Map<String, Integer> counts = new LinkedHashMap<>();
        for (int i = 1; i <= 5; i++) counts.put(i + " Star", 0);
        for (Feedback f : state.getFeedbacks()) {
            String key = f.getRating() + " Star";
            counts.put(key, counts.getOrDefault(key, 0) + 1);
        }
        return counts;
    }

    public String exportSummary() {
        return "Generated " + DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(LocalDateTime.now()) +
                " | Users=" + state.getUsers().size() + ", Issues=" + state.getIssues().size() + ", Polls=" + state.getPolls().size();
    }

    private String findDepartmentName(String id) {
        if (id == null) return "Unknown";
        for (Department d : state.getDepartments()) if (d.getId().equals(id)) return d.getName();
        return "Unknown";
    }

    private String findCityName(String id) {
        if (id == null) return "Unknown";
        for (City c : state.getCities()) if (c.getId().equals(id)) return c.getName();
        return "Unknown";
    }

    private String findUserName(String id) {
        if (id == null) return "Unknown";
        for (User u : state.getUsers()) if (u.getId().equals(id)) return u.getDisplayName();
        return "Unknown";
    }
}
