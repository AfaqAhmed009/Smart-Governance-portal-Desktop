package sgdss;

import java.time.LocalDate;

/**
 * DemoData bootstraps the application with sample data for demonstration.
 * Creates cities, departments, categories, users, issues, polls, and announcements.
 */
public final class DemoData {
    private DemoData() {}

    public static AppState bootstrap() {
        AppState s = new AppState();

        City c1 = new City("Islamabad", "ISB");
        City c2 = new City("Rawalpindi", "RWP");
        City c3 = new City("Lahore", "LHR");
        s.getCities().add(c1);
        s.getCities().add(c2);
        s.getCities().add(c3);

        Department d1 = new Department(c1.getId(), "Sanitation", "SAN");
        Department d2 = new Department(c1.getId(), "Roads & Infrastructure", "RDI");
        Department d3 = new Department(c1.getId(), "Water Supply", "WTR");
        Department d4 = new Department(c2.getId(), "Public Works", "PWK");
        Department d5 = new Department(c3.getId(), "Traffic Management", "TRF");
        s.getDepartments().add(d1);
        s.getDepartments().add(d2);
        s.getDepartments().add(d3);
        s.getDepartments().add(d4);
        s.getDepartments().add(d5);

        Category cat1 = new Category(d1.getId(), "Garbage Collection");
        Category cat2 = new Category(d2.getId(), "Pothole / Road Damage");
        Category cat3 = new Category(d3.getId(), "Water Leakage");
        Category cat4 = new Category(d4.getId(), "Street Lights");
        Category cat5 = new Category(d5.getId(), "Traffic Signal Repair");
        s.getCategories().add(cat1);
        s.getCategories().add(cat2);
        s.getCategories().add(cat3);
        s.getCategories().add(cat4);
        s.getCategories().add(cat5);

        User admin = new User("admin", "Global Admin", "admin@sgdss.local", "0000", Role.GLOBAL_ADMIN);
        User cityAdmin = new User("cityadmin", "City Admin Islamabad", "cityadmin@sgdss.local", "0001", Role.CITY_ADMIN);
        User dept = new User("deptadmin", "Department Admin", "dept@sgdss.local", "0002", Role.DEPT_ADMIN);
        User officer1 = new User("officer1", "Field Officer Ali", "officer1@sgdss.local", "0003", Role.OFFICER);
        User officer2 = new User("officer2", "Field Officer Sara", "officer2@sgdss.local", "0004", Role.OFFICER);
        User citizen = new User("citizen", "Ahmed Citizen", "citizen@sgdss.local", "0005", Role.CITIZEN);
        User citizen2 = new User("fatima", "Fatima Citizen", "fatima@sgdss.local", "0006", Role.CITIZEN);
        User citizen3 = new User("omar", "Omar Citizen", "omar@sgdss.local", "0007", Role.CITIZEN);

        fillCreds(admin, "Admin@123", "Blue?", "Blue");
        fillCreds(cityAdmin, "City@123", "City?", "Islamabad");
        fillCreds(dept, "Dept@123", "City?", "Islamabad");
        fillCreds(officer1, "Officer@123", "Zone?", "North");
        fillCreds(officer2, "Officer@123", "Zone?", "South");
        fillCreds(citizen, "Citizen@123", "Pet?", "Cat");
        fillCreds(citizen2, "Citizen@123", "Pet?", "Dog");
        fillCreds(citizen3, "Citizen@123", "Pet?", "Bird");

        admin.setCityId(c1.getId());
        cityAdmin.setCityId(c1.getId());
        dept.setCityId(c1.getId()); dept.setDepartmentId(d1.getId());
        officer1.setCityId(c1.getId()); officer1.setDepartmentId(d2.getId()); officer1.setSpecialization("Road Repair");
        officer2.setCityId(c1.getId()); officer2.setDepartmentId(d3.getId()); officer2.setSpecialization("Water Systems");
        citizen.setCityId(c1.getId()); citizen.setDepartmentId(d2.getId());
        citizen2.setCityId(c2.getId()); citizen2.setDepartmentId(d4.getId());
        citizen3.setCityId(c3.getId()); citizen3.setDepartmentId(d5.getId());

        s.getUsers().add(admin);
        s.getUsers().add(cityAdmin);
        s.getUsers().add(dept);
        s.getUsers().add(officer1);
        s.getUsers().add(officer2);
        s.getUsers().add(citizen);
        s.getUsers().add(citizen2);
        s.getUsers().add(citizen3);

        Issue i1 = new Issue(citizen.getId(), c1.getId(), d2.getId(), cat2.getId(), 
            "Pothole outside sector F-10", 
            "A deep pothole is causing vehicle damage and traffic delay on the main road.", 
            "F-10 Markaz", 4);
        i1.setPredictedPriority(Priority.HIGH); 
        i1.setEstimatedSlaHours(24); 
        i1.setAssignedOfficerId(officer1.getId()); 
        i1.setStatus(Status.IN_PROGRESS); 
        i1.setSmartNote("High road safety impact detected. Immediate attention required.");

        Issue i2 = new Issue(citizen2.getId(), c2.getId(), d4.getId(), cat4.getId(), 
            "Broken street light on main road", 
            "Street light is not working near the park entrance. Dark area poses safety risk.", 
            "Committee Chowk", 3);
        i2.setPredictedPriority(Priority.MEDIUM); 
        i2.setEstimatedSlaHours(48); 
        i2.setSmartNote("Matches recurring lighting incident pattern.");

        Issue i3 = new Issue(citizen3.getId(), c3.getId(), d5.getId(), cat5.getId(),
            "Traffic signal malfunction at Liberty Chowk",
            "The traffic signal at Liberty Chowk is stuck on red causing major congestion during rush hour.",
            "Liberty Chowk", 5);
        i3.setPredictedPriority(Priority.URGENT);
        i3.setEstimatedSlaHours(4);
        i3.setAssignedOfficerId(officer2.getId());
        i3.setStatus(Status.ASSIGNED);
        i3.setSmartNote("Critical traffic disruption. Urgent response needed.");

        Issue i4 = new Issue(citizen.getId(), c1.getId(), d1.getId(), cat1.getId(),
            "Garbage pileup in sector G-11",
            "Garbage has not been collected for 3 days. Pileup is attracting pests and creating foul smell.",
            "G-11/2", 3);
        i4.setPredictedPriority(Priority.MEDIUM);
        i4.setEstimatedSlaHours(24);
        i4.setSmartNote("Health hazard risk. Sanitation crew dispatch recommended.");

        Issue i5 = new Issue(citizen2.getId(), c1.getId(), d3.getId(), cat3.getId(),
            "Water supply disruption in sector I-8",
            "No water supply for the past 12 hours in the entire sector. Residents facing severe inconvenience.",
            "I-8/3", 4);
        i5.setPredictedPriority(Priority.HIGH);
        i5.setEstimatedSlaHours(12);
        i5.setAssignedOfficerId(officer2.getId());
        i5.setStatus(Status.IN_PROGRESS);
        i5.setSmartNote("Critical water supply issue affecting multiple households.");

        s.getIssues().add(i1); 
        s.getIssues().add(i2);
        s.getIssues().add(i3);
        s.getIssues().add(i4);
        s.getIssues().add(i5);

        Poll p = new Poll(admin.getId(), "Which civic service should receive highest budget priority?", LocalDate.now().plusDays(10));
        p.addOption("Road repair");
        p.addOption("Water supply");
        p.addOption("Waste management");
        p.addOption("Street lighting");
        s.getPolls().add(p);

        Poll p2 = new Poll(cityAdmin.getId(), "Should public parks have extended evening hours?", LocalDate.now().plusDays(7));
        p2.addOption("Yes, until 10 PM");
        p2.addOption("Yes, until 11 PM");
        p2.addOption("No, current hours are fine");
        s.getPolls().add(p2);

        s.getAnnouncements().add(new Announcement(admin.getId(), "City Maintenance Notice",
                "Major cleaning and repair work will start this weekend in selected sectors. Please cooperate with the municipal teams.", c1.getId(), null));
        s.getAnnouncements().add(new Announcement(cityAdmin.getId(), "New Traffic Rules",
                "Updated traffic regulations will be enforced from next Monday. Please review the new guidelines on the city website.", c1.getId(), null));

        s.getFeedbacks().add(new Feedback(citizen.getId(), "The dashboard is clear and easy to understand. Great work!", 5));
        s.getFeedbacks().add(new Feedback(citizen2.getId(), "Issue tracking works well but could use mobile notifications.", 4));
        s.getFeedbacks().add(new Feedback(citizen3.getId(), "Response time for urgent issues has improved significantly.", 5));

        s.getAudits().add(new AuditEntry(admin.getId(), admin.getDisplayName(), "SYSTEM_BOOTSTRAP", "Demo data initialized with 8 users, 5 issues, 2 polls"));

        // Initialize default settings
        s.getSettings().add(new SystemSetting("theme", "light", "UI theme: light or dark"));
        s.getSettings().add(new SystemSetting("notifications.enabled", "true", "Enable system notifications"));
        s.getSettings().add(new SystemSetting("sla.warning.threshold", "6", "Hours before SLA deadline to send warning"));

        return s;
    }

    private static void fillCreds(User u, String password, String q, String a) {
        String salt = PasswordUtil.newSalt();
        u.setPasswordSalt(salt);
        u.setPasswordHash(PasswordUtil.hash(password.toCharArray(), salt));
        u.setSecurityQuestion(q);
        String ansSalt = PasswordUtil.newSalt();
        u.setSecurityAnswerHash(ansSalt + ":" + PasswordUtil.hash(a.toLowerCase().toCharArray(), ansSalt));
    }
}
