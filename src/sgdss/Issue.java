package sgdss;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Issue represents a civic complaint or service request submitted by a citizen.
 * It includes smart features: priority prediction, duplicate detection,
 * SLA tracking, and escalation management.
 */
public class Issue implements Serializable {
    @Serial private static final long serialVersionUID = 1L;

    private final String id;
    private String submitterId;
    private String cityId;
    private String departmentId;
    private String categoryId;
    private String title;
    private String description;
    private String location;
    private int severity;
    private Priority predictedPriority = Priority.MEDIUM;
    private Status status = Status.NEW;
    private String assignedOfficerId;
    private String duplicateOfIssueId;
    private String smartNote;
    private int estimatedSlaHours;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
    private LocalDateTime resolvedAt;
    private LocalDateTime slaDeadline;
    private boolean slaBreached = false;
    private boolean escalated = false;
    private String escalationReason;
    private final List<IssueComment> comments = new ArrayList<>();
    private List<String> attachments = new ArrayList<>();
    private int resolutionTimeHours;
    private String resolutionNotes;

    public Issue(String submitterId, String cityId, String departmentId, 
                 String categoryId, String title, String description, 
                 String location, int severity) {
        this.id = UUID.randomUUID().toString();
        this.submitterId = submitterId;
        this.cityId = cityId;
        this.departmentId = departmentId;
        this.categoryId = categoryId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.severity = severity;
    }

    public String getId() { return id; }
    public String getSubmitterId() { return submitterId; }
    public String getCityId() { return cityId; }
    public String getDepartmentId() { return departmentId; }
    public String getCategoryId() { return categoryId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getLocation() { return location; }
    public int getSeverity() { return severity; }
    public Priority getPredictedPriority() { return predictedPriority; }
    public Status getStatus() { return status; }
    public String getAssignedOfficerId() { return assignedOfficerId; }
    public String getDuplicateOfIssueId() { return duplicateOfIssueId; }
    public String getSmartNote() { return smartNote; }
    public int getEstimatedSlaHours() { return estimatedSlaHours; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public LocalDateTime getResolvedAt() { return resolvedAt; }
    public LocalDateTime getSlaDeadline() { return slaDeadline; }
    public boolean isSlaBreached() { return slaBreached; }
    public boolean isEscalated() { return escalated; }
    public String getEscalationReason() { return escalationReason; }
    public List<IssueComment> getComments() { return comments; }
    public List<String> getAttachments() { return attachments; }
    public int getResolutionTimeHours() { return resolutionTimeHours; }
    public String getResolutionNotes() { return resolutionNotes; }

    public void setCityId(String cityId) { this.cityId = cityId; updatedAt = LocalDateTime.now(); }
    public void setDepartmentId(String departmentId) { this.departmentId = departmentId; updatedAt = LocalDateTime.now(); }
    public void setCategoryId(String categoryId) { this.categoryId = categoryId; updatedAt = LocalDateTime.now(); }
    public void setTitle(String title) { this.title = title; updatedAt = LocalDateTime.now(); }
    public void setDescription(String description) { this.description = description; updatedAt = LocalDateTime.now(); }
    public void setLocation(String location) { this.location = location; updatedAt = LocalDateTime.now(); }
    public void setSeverity(int severity) { this.severity = severity; updatedAt = LocalDateTime.now(); }
    public void setPredictedPriority(Priority predictedPriority) { this.predictedPriority = predictedPriority; updatedAt = LocalDateTime.now(); }
    public void setStatus(Status status) { 
        this.status = status; 
        updatedAt = LocalDateTime.now();
        if (status == Status.RESOLVED || status == Status.CLOSED) {
            resolvedAt = LocalDateTime.now();
            resolutionTimeHours = (int) java.time.Duration.between(createdAt, resolvedAt).toHours();
        }
    }
    public void setAssignedOfficerId(String assignedOfficerId) { this.assignedOfficerId = assignedOfficerId; updatedAt = LocalDateTime.now(); }
    public void setDuplicateOfIssueId(String duplicateOfIssueId) { this.duplicateOfIssueId = duplicateOfIssueId; updatedAt = LocalDateTime.now(); }
    public void setSmartNote(String smartNote) { this.smartNote = smartNote; updatedAt = LocalDateTime.now(); }
    public void setEstimatedSlaHours(int estimatedSlaHours) { 
        this.estimatedSlaHours = estimatedSlaHours; 
        this.slaDeadline = LocalDateTime.now().plusHours(estimatedSlaHours);
        updatedAt = LocalDateTime.now(); 
    }
    public void setSlaDeadline(LocalDateTime slaDeadline) { this.slaDeadline = slaDeadline; }
    public void setSlaBreached(boolean slaBreached) { this.slaBreached = slaBreached; }
    public void setEscalated(boolean escalated) { this.escalated = escalated; }
    public void setEscalationReason(String escalationReason) { this.escalationReason = escalationReason; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public void setResolutionNotes(String resolutionNotes) { this.resolutionNotes = resolutionNotes; }
    public void setAttachments(List<String> attachments) { this.attachments = attachments; }

    public void addComment(IssueComment c) {
        comments.add(c);
        updatedAt = LocalDateTime.now();
    }

    public void addAttachment(String path) {
        attachments.add(path);
        updatedAt = LocalDateTime.now();
    }

    public void checkSlaStatus() {
        if (slaDeadline != null && !slaBreached && 
            (status != Status.RESOLVED && status != Status.CLOSED && status != Status.REJECTED)) {
            if (LocalDateTime.now().isAfter(slaDeadline)) {
                slaBreached = true;
                escalated = true;
                escalationReason = "SLA deadline breached. Issue was not resolved within " + estimatedSlaHours + " hours.";
            }
        }
    }

    public String shortDisplay() {
        return "#" + id.substring(0, 8) + " " + title;
    }

    public long getHoursUntilDeadline() {
        if (slaDeadline == null) return -1;
        return java.time.Duration.between(LocalDateTime.now(), slaDeadline).toHours();
    }
}
