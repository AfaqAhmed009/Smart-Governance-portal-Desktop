package sgdss;

import java.util.List;
import java.util.Optional;

/**
 * AdminService provides administrative operations for managing
 * cities, departments, categories, officers, and user roles.
 */
public class AdminService {
    private final AppState state;
    private final DataStore store;

    public AdminService(AppState state, DataStore store) {
        this.state = state;
        this.store = store;
    }

    public City addCity(User actor, String name, String code) {
        City c = new City(name, code);
        state.getCities().add(c);
        audit(actor, "CITY_CREATED", name);
        store.save();
        return c;
    }

    public Department addDepartment(User actor, String cityId, String name, String code) {
        Department d = new Department(cityId, name, code);
        state.getDepartments().add(d);
        audit(actor, "DEPARTMENT_CREATED", name);
        store.save();
        return d;
    }

    public Category addCategory(User actor, String departmentId, String name) {
        Category c = new Category(departmentId, name);
        state.getCategories().add(c);
        audit(actor, "CATEGORY_CREATED", name);
        store.save();
        return c;
    }

    public User addOfficer(User actor, String username, String displayName, String email, String phone, String password, String cityId, String departmentId) {
        User u = new User(username, displayName, email, phone, Role.OFFICER);
        u.setCityId(cityId);
        u.setDepartmentId(departmentId);
        String salt = PasswordUtil.newSalt();
        u.setPasswordSalt(salt);
        u.setPasswordHash(PasswordUtil.hash(password.toCharArray(), salt));
        String ansSalt = PasswordUtil.newSalt();
        u.setSecurityQuestion("Office?");
        u.setSecurityAnswerHash(ansSalt + ":" + PasswordUtil.hash("office".toCharArray(), ansSalt));
        state.getUsers().add(u);
        audit(actor, "OFFICER_CREATED", username);
        store.save();
        return u;
    }

    public void promote(User actor, User user, Role role) {
        user.setRole(role);
        audit(actor, "ROLE_CHANGED", user.getUsername() + " -> " + role.displayName());
        store.save();
    }

    public void deactivateUser(User actor, User user) {
        user.setActive(false);
        audit(actor, "USER_DEACTIVATED", user.getUsername());
        store.save();
    }

    public void activateUser(User actor, User user) {
        user.setActive(true);
        user.setFailedLoginAttempts(0);
        user.setLockedUntil(null);
        audit(actor, "USER_ACTIVATED", user.getUsername());
        store.save();
    }

    public List<User> users() { return state.getUsers(); }
    public List<City> cities() { return state.getCities(); }
    public List<Department> departments() { return state.getDepartments(); }
    public List<Category> categories() { return state.getCategories(); }

    private void audit(User actor, String action, String details) {
        state.getAudits().add(new AuditEntry(actor.getId(), actor.getDisplayName(), action, details));
    }
}
