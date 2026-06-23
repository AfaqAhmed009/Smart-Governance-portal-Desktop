package sgdss;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * User represents a system user with role-based access control.
 * Supports account lockout after failed login attempts for security.
 */
public class User implements Serializable {
    @Serial private static final long serialVersionUID = 1L;

    private final String id;
    private String username;
    private String displayName;
    private String email;
    private String phone;
    private Role role;
    private String cityId;
    private String departmentId;
    private String passwordSalt;
    private String passwordHash;
    private String securityQuestion;
    private String securityAnswerHash;
    private boolean active = true;
    private LocalDateTime createdAt = LocalDateTime.now();
    private int failedLoginAttempts = 0;
    private LocalDateTime lockedUntil;
    private LocalDateTime lastLogin;
    private String specialization;
    private int workload;

    public User(String username, String displayName, String email, String phone, Role role) {
        this.id = UUID.randomUUID().toString();
        this.username = username;
        this.displayName = displayName;
        this.email = email;
        this.phone = phone;
        this.role = role;
    }

    public String getId() { return id; }
    public String getUsername() { return username; }
    public String getDisplayName() { return displayName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public Role getRole() { return role; }
    public String getCityId() { return cityId; }
    public String getDepartmentId() { return departmentId; }
    public String getPasswordSalt() { return passwordSalt; }
    public String getPasswordHash() { return passwordHash; }
    public String getSecurityQuestion() { return securityQuestion; }
    public String getSecurityAnswerHash() { return securityAnswerHash; }
    public boolean isActive() { return active; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public int getFailedLoginAttempts() { return failedLoginAttempts; }
    public LocalDateTime getLockedUntil() { return lockedUntil; }
    public LocalDateTime getLastLogin() { return lastLogin; }
    public String getSpecialization() { return specialization; }
    public int getWorkload() { return workload; }

    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setRole(Role role) { this.role = role; }
    public void setCityId(String cityId) { this.cityId = cityId; }
    public void setDepartmentId(String departmentId) { this.departmentId = departmentId; }
    public void setPasswordSalt(String passwordSalt) { this.passwordSalt = passwordSalt; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public void setSecurityQuestion(String securityQuestion) { this.securityQuestion = securityQuestion; }
    public void setSecurityAnswerHash(String securityAnswerHash) { this.securityAnswerHash = securityAnswerHash; }
    public void setActive(boolean active) { this.active = active; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setFailedLoginAttempts(int failedLoginAttempts) { this.failedLoginAttempts = failedLoginAttempts; }
    public void setLockedUntil(LocalDateTime lockedUntil) { this.lockedUntil = lockedUntil; }
    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    public void setWorkload(int workload) { this.workload = workload; }

    public void recordFailedLogin() {
        failedLoginAttempts++;
        if (failedLoginAttempts >= 5) {
            lockedUntil = LocalDateTime.now().plusMinutes(30);
        }
    }

    public void recordSuccessfulLogin() {
        failedLoginAttempts = 0;
        lockedUntil = null;
        lastLogin = LocalDateTime.now();
    }

    public boolean isLocked() {
        if (lockedUntil == null) return false;
        if (LocalDateTime.now().isAfter(lockedUntil)) {
            lockedUntil = null;
            failedLoginAttempts = 0;
            return false;
        }
        return true;
    }

    public String summary() {
        return displayName + " (" + username + ", " + role.displayName() + ")";
    }

    @Override public String toString() { return summary(); }
}
