package sgdss;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

public class Category implements Serializable {
    @Serial private static final long serialVersionUID = 1L;
    private final String id;
    private String departmentId;
    private String name;

    public Category(String departmentId, String name) {
        this.id = UUID.randomUUID().toString();
        this.departmentId = departmentId;
        this.name = name;
    }

    public String getId() { return id; }
    public String getDepartmentId() { return departmentId; }
    public String getName() { return name; }
    public void setDepartmentId(String departmentId) { this.departmentId = departmentId; }
    public void setName(String name) { this.name = name; }
    @Override public String toString() { return name; }
}
