package sgdss;

import java.util.*;

/**
 * SmartEngine provides AI-powered features for the SGDSS system.
 * Includes priority prediction, duplicate detection, SLA estimation,
 * officer assignment, and intelligent recommendations.
 */
public class SmartEngine {

    public Priority predictPriority(Issue issue, Category category, Department dept) {
        String text = ((issue.getTitle() == null ? "" : issue.getTitle()) + " " +
                (issue.getDescription() == null ? "" : issue.getDescription()) + " " +
                (issue.getLocation() == null ? "" : issue.getLocation())).toLowerCase(Locale.ROOT);

        int score = issue.getSeverity() * 2;
        score += keywordBoost(text, "fire", "flood", "gas leak", "danger", "accident", "blocked", "overflow", "burst", "collapse") * 2;
        score += keywordBoost(text, "sewage", "garbage", "pothole", "water", "light", "road", "traffic", "electric") ;
        score += categoryBoost(category);
        if (text.contains("urgent") || text.contains("critical") || text.contains("immediately")) score += 4;

        if (score >= 11) return Priority.URGENT;
        if (score >= 8) return Priority.HIGH;
        if (score >= 5) return Priority.MEDIUM;
        return Priority.LOW;
    }

    private int keywordBoost(String text, String... terms) {
        int total = 0;
        for (String t : terms) {
            if (text.contains(t)) total++;
        }
        return total;
    }

    private int categoryBoost(Category category) {
        if (category == null) return 0;
        String c = category.getName().toLowerCase(Locale.ROOT);
        if (c.contains("water") || c.contains("sewage") || c.contains("road")) return 2;
        if (c.contains("electric") || c.contains("garbage")) return 1;
        return 0;
    }

    public Optional<Issue> detectDuplicate(Issue incoming, List<Issue> issues, Category category) {
        String a = normalize(incoming.getTitle() + " " + incoming.getDescription());
        Issue best = null;
        double bestScore = 0.0;
        for (Issue old : issues) {
            if (old.getId().equals(incoming.getId())) continue;
            if (old.getCategoryId() != null && !old.getCategoryId().equals(incoming.getCategoryId())) continue;
            double score = similarity(a, normalize(old.getTitle() + " " + old.getDescription()));
            if (score > bestScore) {
                bestScore = score;
                best = old;
            }
        }
        if (best != null && bestScore >= 0.58) return Optional.of(best);
        return Optional.empty();
    }

    public int estimateSlaHours(Priority priority, int severity) {
        return switch (priority) {
            case URGENT -> Math.max(4, 12 - severity);
            case HIGH -> Math.max(12, 24 - severity * 2);
            case MEDIUM -> Math.max(24, 48 - severity * 2);
            case LOW -> 72;
        };
    }

    public String suggestOfficer(Issue issue, List<User> users) {
        List<User> candidates = new ArrayList<>();
        for (User u : users) {
            if (u.isActive() && u.getRole() == Role.OFFICER) candidates.add(u);
        }
        if (candidates.isEmpty()) return null;

        // Prefer officer in same department with lowest workload
        User best = null;
        int bestWorkload = Integer.MAX_VALUE;
        for (User u : candidates) {
            if (issue.getDepartmentId() != null && issue.getDepartmentId().equals(u.getDepartmentId())) {
                if (u.getWorkload() < bestWorkload) {
                    bestWorkload = u.getWorkload();
                    best = u;
                }
            }
        }
        if (best != null) {
            best.setWorkload(best.getWorkload() + 1);
            return best.getId();
        }
        // Fallback: any officer with lowest workload
        bestWorkload = Integer.MAX_VALUE;
        for (User u : candidates) {
            if (u.getWorkload() < bestWorkload) {
                bestWorkload = u.getWorkload();
                best = u;
            }
        }
        if (best != null) {
            best.setWorkload(best.getWorkload() + 1);
            return best.getId();
        }
        return candidates.get(0).getId();
    }

    public String smartNote(Issue issue, Priority p, Optional<Issue> dup) {
        StringBuilder sb = new StringBuilder();
        sb.append("Priority predicted as ").append(p.displayName()).append(" based on title, severity, and keyword signals.");
        if (dup.isPresent()) {
            sb.append(" Possible duplicate of issue #").append(dup.get().getId().substring(0, 8)).append(".");
        } else {
            sb.append(" No strong duplicate found.");
        }
        return sb.toString();
    }

    public String suggestResolution(Issue issue) {
        String text = (issue.getTitle() + " " + issue.getDescription()).toLowerCase(Locale.ROOT);
        if (text.contains("pothole") || text.contains("road damage")) {
            return "Suggested: Schedule road repair crew. Estimated time: 24-48 hours.";
        }
        if (text.contains("light") || text.contains("street")) {
            return "Suggested: Replace bulb/fix wiring. Estimated time: 4-12 hours.";
        }
        if (text.contains("water") || text.contains("leak")) {
            return "Suggested: Dispatch water department team. Estimated time: 12-24 hours.";
        }
        if (text.contains("garbage") || text.contains("waste")) {
            return "Suggested: Schedule waste collection pickup. Estimated time: 24 hours.";
        }
        return "Suggested: Review issue details and assign appropriate department resources.";
    }

    private String normalize(String s) {
        return s == null ? "" : s.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9 ]", " ").replaceAll("\s+", " ").trim();
    }

    private double similarity(String a, String b) {
        if (a.isBlank() || b.isBlank()) return 0.0;
        Set<String> sa = new HashSet<>(Arrays.asList(a.split(" ")));
        Set<String> sb = new HashSet<>(Arrays.asList(b.split(" ")));
        sa.remove("");
        sb.remove("");
        if (sa.isEmpty() || sb.isEmpty()) return 0.0;
        int inter = 0;
        for (String t : sa) if (sb.contains(t)) inter++;
        int union = sa.size() + sb.size() - inter;
        return union == 0 ? 0.0 : (double) inter / union;
    }
}
