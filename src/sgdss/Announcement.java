package sgdss;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public class Announcement implements Serializable {
    @Serial private static final long serialVersionUID = 1L;
    private final String id;
    private String createdByUserId;
    private String title;
    private String content;
    private String cityId;
    private String departmentId;
    private LocalDateTime createdAt = LocalDateTime.now();

    public Announcement(String createdByUserId, String title, String content, String cityId, String departmentId) {
        this.id = UUID.randomUUID().toString();
        this.createdByUserId = createdByUserId;
        this.title = title;
        this.content = content;
        this.cityId = cityId;
        this.departmentId = departmentId;
    }

    public String getId() { return id; }
    public String getCreatedByUserId() { return createdByUserId; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getCityId() { return cityId; }
    public String getDepartmentId() { return departmentId; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setCreatedByUserId(String createdByUserId) { this.createdByUserId = createdByUserId; }
    public void setTitle(String title) { this.title = title; }
    public void setContent(String content) { this.content = content; }
    public void setCityId(String cityId) { this.cityId = cityId; }
    public void setDepartmentId(String departmentId) { this.departmentId = departmentId; }

    @Override public String toString() { return title; }
}
