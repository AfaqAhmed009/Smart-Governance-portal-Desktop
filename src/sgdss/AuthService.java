package sgdss;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * AuthService handles all authentication operations including login,
 * registration, password reset, and account lockout after failed attempts.
 */
public class AuthService {
    private final AppState state;
    private final DataStore store;

    public AuthService(AppState state, DataStore store) {
        this.state = state;
        this.store = store;
    }

    public Optional<User> login(String username, char[] password) {
        for (User user : state.getUsers()) {
            if (!user.isActive()) continue;
            if (user.isLocked()) {
                log(null, "LOGIN_BLOCKED", "Account locked for user: " + username);
                store.save();
                return Optional.empty();
            }
            if (Objects.equals(user.getUsername(), username)) {
                if (PasswordUtil.verify(password, user.getPasswordSalt(), user.getPasswordHash())) {
                    user.recordSuccessfulLogin();
                    log(user, "LOGIN_SUCCESS", "User logged in");
                    store.save();
                    return Optional.of(user);
                } else {
                    user.recordFailedLogin();
                    log(null, "LOGIN_FAILURE", "Invalid password for " + username + " (attempt " + user.getFailedLoginAttempts() + ")");
                    store.save();
                    return Optional.empty();
                }
            }
        }
        store.getState().getAudits().add(new AuditEntry(null, "SYSTEM", "LOGIN_FAILURE", "Invalid login attempt for " + username));
        store.save();
        return Optional.empty();
    }

    public User registerCitizen(String username, String displayName, String email, String phone, String password, String securityQuestion, String securityAnswer, String cityId, String departmentId) {
        if (findByUsername(username).isPresent()) throw new IllegalArgumentException("Username already exists");
        validatePassword(password);
        User user = new User(username, displayName, email, phone, Role.CITIZEN);
        user.setCityId(cityId);
        user.setDepartmentId(departmentId);
        setCredentials(user, password, securityQuestion, securityAnswer);
        state.getUsers().add(user);
        log(user, "REGISTER", "Citizen account created");
        store.save();
        return user;
    }

    public boolean resetPassword(String username, String securityAnswer, String newPassword) {
        Optional<User> opt = findByUsername(username);
        if (opt.isEmpty()) return false;
        User user = opt.get();
        if (user.getSecurityAnswerHash() == null) return false;
        String[] parts = user.getSecurityAnswerHash().split(":", 2);
        if (parts.length != 2) return false;
        String expected = parts[1];
        String actual = PasswordUtil.hash(securityAnswer.toLowerCase().toCharArray(), parts[0]);
        if (!expected.equals(actual)) return false;
        validatePassword(newPassword);
        setCredentials(user, newPassword, user.getSecurityQuestion(), securityAnswer);
        log(user, "PASSWORD_RESET", "Password updated after security verification");
        store.save();
        return true;
    }

    public void updateCredentials(User user, String newPassword) {
        validatePassword(newPassword);
        setCredentials(user, newPassword, user.getSecurityQuestion(), "updated");
        log(user, "PASSWORD_CHANGE", "Password changed");
        store.save();
    }

    private void setCredentials(User user, String password, String q, String a) {
        String salt = PasswordUtil.newSalt();
        user.setPasswordSalt(salt);
        user.setPasswordHash(PasswordUtil.hash(password.toCharArray(), salt));
        if (q != null) user.setSecurityQuestion(q);
        if (a != null) {
            String ansSalt = PasswordUtil.newSalt();
            user.setSecurityAnswerHash(ansSalt + ":" + PasswordUtil.hash(a.toLowerCase().toCharArray(), ansSalt));
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }
        boolean hasUpper = false, hasLower = false, hasDigit = false, hasSpecial = false;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            if (Character.isLowerCase(c)) hasLower = true;
            if (Character.isDigit(c)) hasDigit = true;
            if (!Character.isLetterOrDigit(c)) hasSpecial = true;
        }
        if (!hasUpper || !hasLower || !hasDigit) {
            throw new IllegalArgumentException("Password must contain uppercase, lowercase, and digit characters");
        }
    }

    public Optional<User> findByUsername(String username) {
        return state.getUsers().stream().filter(u -> u.getUsername().equalsIgnoreCase(username)).findFirst();
    }

    public List<User> users() { return state.getUsers(); }

    private void log(User user, String action, String details) {
        state.getAudits().add(new AuditEntry(user == null ? null : user.getId(), user == null ? "SYSTEM" : user.getDisplayName(), action, details));
    }
}
