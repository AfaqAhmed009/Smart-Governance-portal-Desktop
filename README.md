# SGDSS - Smart Governance & Decision Support System

> *Empowering cities through intelligent civic management*

---

## What is SGDSS?

**SGDSS** (Smart Governance & Decision Support System) is a desktop-based civic issue tracking and management platform built with **Java Swing**. It connects citizens, field officers, department admins, city admins, and global administrators on a single platform to report, track, and resolve civic issues efficiently.

Whether it is a pothole on your street, a broken traffic signal, or a water supply disruption - SGDSS ensures your voice reaches the right people and gets resolved within a committed timeline.

---

## Who Can Use This?

The system supports **5 user roles**, each with specific responsibilities:

| Role | Who Are They? | What Can They Do? |
|------|--------------|-------------------|
| **Citizen** | Regular city residents | Report issues, track their complaints, vote in public polls, give feedback |
| **Field Officer** | Ground-level workers (e.g., road repair crew, electricians) | View assigned issues, update status, add resolution notes |
| **Department Admin** | Heads of specific departments (Sanitation, Water, Roads, etc.) | Manage officers, oversee department issues, create polls and announcements |
| **City Admin** | Municipal-level administrators | Oversee all departments in a city, manage city-wide operations |
| **Global Admin** | System super-administrator | Full system control - users, cities, departments, settings, analytics |

---

## Key Features

### For Citizens
- **Report civic issues** with location, severity, and description
- **Track issue status** in real-time (New -> Assigned -> In Progress -> Resolved)
- **Vote in public polls** about city improvements
- **Submit feedback** and ratings about civic services
- **View announcements** relevant to your city or department

### For Officers & Admins
- **Smart issue assignment** - AI-powered officer allocation based on workload and specialization
- **SLA monitoring** - Every issue gets a deadline. Breaches trigger automatic escalation
- **Priority prediction** - System automatically predicts urgency (Low/Medium/High/Urgent) based on keywords and severity
- **Duplicate detection** - Prevents multiple reports of the same issue
- **Add comments and resolution notes** to keep everyone informed

### For Management
- **Real-time dashboard** with key metrics (total issues, open issues, SLA breaches, active polls)
- **Visual analytics** - Bar charts for status and priority distribution
- **Audit logging** - Every action is recorded for transparency and compliance
- **Export reports** to CSV for offline analysis
- **Theme support** - Switch between Light and Dark mode

---

## Getting Started

### Prerequisites
- **Java 17 or higher** installed on your system
- Any operating system (Windows, macOS, Linux)

### How to Run

1. **Download the project** and extract the ZIP file
2. **Navigate to the project folder** in your terminal/command prompt
3. **Compile the project:**
   ```bash
   javac -d out src/sgdss/*.java
   ```
4. **Run the application:**
   ```bash
   java -cp out sgdss.Main
   ```

That's it! The login screen will appear.

---

## Demo Accounts

No need to register first. Use any of these pre-configured accounts to explore the system:

| Username | Password | Role | Description |
|----------|----------|------|-------------|
| `admin` | `Admin@123` | Global Admin | Full system access - manage everything |
| `cityadmin` | `City@123` | City Admin | Manage city-level operations |
| `deptadmin` | `Dept@123` | Department Admin | Manage department and officers |
| `officer1` | `Officer@123` | Field Officer | Handle assigned issues |
| `officer2` | `Officer@123` | Field Officer | Handle assigned issues |
| `citizen` | `Citizen@123` | Citizen | Report issues and engage |
| `fatima` | `Citizen@123` | Citizen | Another citizen account |
| `omar` | `Citizen@123` | Citizen | Another citizen account |

> **Tip:** The security question for demo accounts is simple (e.g., "Blue?" answer: "Blue", "City?" answer: "Islamabad"). In production, users set their own security questions during registration.

---

## How to Use

### Login Screen
When you launch SGDSS, you will see a clean split-screen login page:
- **Left side:** Project branding (SGDSS)
- **Right side:** Login form with username and password fields
- **Buttons:** Sign In, Register, Forgot Password, Dark Mode toggle

Click **Dark Mode** to switch between light and dark themes instantly.

### Forgot Password?
1. Click **Forgot Password?** on the login screen
2. Enter your **username**
3. Answer your **security question** (the one you set during registration)
4. Enter a **new password** (minimum 8 characters, must include uppercase, lowercase, and a digit)
5. Your password is reset and you can log in immediately

### Register as a Citizen
1. Click **Register** on the login screen
2. Fill in your details: username, display name, email, phone, password
3. Set a **security question and answer** (used for password recovery)
4. Select your **city and department** (optional)
5. Click OK and you are ready to log in

---

## Navigating the Main Dashboard

After logging in, you will see a tabbed interface. Here is what each tab does:

### Dashboard
Your command center. See:
- Total users, total issues, open issues, SLA breaches, active polls
- Issue status overview (bar chart)
- Issue priority distribution (bar chart)
- Recent activity feed
- Quick statistics (avg resolution time, feedback rating, etc.)

### Issues
The heart of the system:
- **Search** issues by keyword
- **Filter** by status (All, New, Assigned, In Progress, Resolved, Closed, Rejected, Overdue)
- **Submit new issues** by selecting city, department, category, title, location, severity, and description
- **Update status** of existing issues
- **Add comments** to collaborate
- **Resolve issues** with resolution notes
- Smart suggestions appear automatically (priority, SLA, duplicate detection)

### Polls
- View active polls created by admins
- Cast your vote on civic matters
- Admins can create new polls with multiple options and expiry dates
- Visual pie charts show poll results

### Announcements
- Read city and department-specific announcements
- Admins can create new announcements targeting specific cities or departments

### Feedback
- Citizens can submit ratings (1-5 stars) and text feedback
- Helps administrators understand public sentiment

### Reports
- View summary metrics
- See issue distribution by status and priority
- **Export CSV** summary for external reporting

### Audit Log
- Complete history of every action in the system
- Who did what, when, and with what details
- Essential for transparency and compliance

### Notifications
- Your personal alert center
- Get notified when issues are assigned to you
- SLA warnings and breach alerts
- New poll and announcement notifications
- Mark all as read with one click

### Admin Tools (Admin only)
- Add new **cities** with name and code
- Add new **departments** linked to cities
- Add new **categories** linked to departments
- Add new **officers** with username, contact info, city, and department
- **Change user roles** (promote/demote)

### Settings (Global Admin only)
- Switch between **Light and Dark themes**
- Enable/disable **system notifications**
- Adjust **SLA warning threshold** (default: 6 hours before deadline)
- **Create backups** of your data
- **Restore** from previous backups

---

## Demo Data Included

The system comes pre-loaded with realistic demo data so you can explore immediately:

### Cities
| City | Code |
|------|------|
| Islamabad | ISB |
| Rawalpindi | RWP |
| Lahore | LHR |

### Departments
| Department | Code | City |
|------------|------|------|
| Sanitation | SAN | Islamabad |
| Roads & Infrastructure | RDI | Islamabad |
| Water Supply | WTR | Islamabad |
| Public Works | PWK | Rawalpindi |
| Traffic Management | TRF | Lahore |

### Categories
| Category | Department |
|----------|------------|
| Garbage Collection | Sanitation |
| Pothole / Road Damage | Roads & Infrastructure |
| Water Leakage | Water Supply |
| Street Lights | Public Works |
| Traffic Signal Repair | Traffic Management |

### Sample Issues
| Issue | City | Department | Priority | Status | Assigned To |
|-------|------|------------|----------|--------|-------------|
| Pothole outside sector F-10 | Islamabad | Roads & Infra | High | In Progress | officer1 |
| Broken street light on main road | Rawalpindi | Public Works | Medium | New | - |
| Traffic signal malfunction at Liberty Chowk | Lahore | Traffic Mgmt | Urgent | Assigned | officer2 |
| Garbage pileup in sector G-11 | Islamabad | Sanitation | Medium | New | - |
| Water supply disruption in sector I-8 | Islamabad | Water Supply | High | In Progress | officer2 |

### Sample Polls
1. **"Which civic service should receive highest budget priority?"**
   - Options: Road repair, Water supply, Waste management, Street lighting
   - Created by: admin
   - Expires in: 10 days

2. **"Should public parks have extended evening hours?"**
   - Options: Yes until 10 PM, Yes until 11 PM, No current hours are fine
   - Created by: cityadmin
   - Expires in: 7 days

### Sample Announcements
1. **"City Maintenance Notice"** - Major cleaning and repair work this weekend (Islamabad)
2. **"New Traffic Rules"** - Updated regulations from next Monday (Islamabad)

### Sample Feedback
- Ahmed: "The dashboard is clear and easy to understand. Great work!" - 5 stars
- Fatima: "Issue tracking works well but could use mobile notifications." - 4 stars
- Omar: "Response time for urgent issues has improved significantly." - 5 stars

---

## Smart Features Explained

### AI-Powered Priority Prediction
When a citizen submits an issue, the system analyzes:
- **Severity level** (1-5) provided by the user
- **Keywords** in title and description (e.g., "fire", "flood", "urgent" boost priority)
- **Category type** (water/road issues get higher priority)

Result: Automatic priority assignment (Low / Medium / High / Urgent)

### Duplicate Detection
The system compares new issues with existing ones using text similarity. If a match above 58% is found, the new issue is flagged as a possible duplicate with a reference to the original.

### Smart Officer Assignment
When an issue is submitted, the system:
1. Finds all active officers
2. Prefers officers in the **same department**
3. Picks the one with the **lowest current workload**
4. Updates the officer's workload count automatically

### SLA Management
Every issue gets an estimated resolution time based on priority:
- **Urgent:** 4-12 hours
- **High:** 12-24 hours
- **Medium:** 24-48 hours
- **Low:** 72 hours

The system monitors deadlines every minute. If a deadline passes:
- The issue is **flagged as breached**
- It gets **automatically escalated**
- **Notifications** are sent to the assigned officer and department admin

---

## Data Storage

All data is stored locally in a serialized file:
```
data/sgdss.dat
```

- **No external database required** - everything runs offline
- Data persists between sessions
- You can create **backups** from the Settings panel
- **Restore** from backups if needed

---

## Theme Support

Switch between **Light Mode** and **Dark Mode** anytime:
- From the **login screen** (Dark Mode button)
- From the **main dashboard** (Light Mode/Dark Mode button in the top bar)

All components adapt instantly: tables, forms, buttons, charts, dropdowns, and dialogs.

---

## Security Features

- **PBKDF2 password hashing** with random salts (industry standard)
- **Account lockout** after 5 failed login attempts (30-minute cooldown)
- **Security questions** for password recovery
- **Role-based access control** - users only see what they are allowed to see
- **Full audit logging** - every action is traceable

---

## Password Requirements

When registering or resetting passwords:
- Minimum **8 characters**
- At least **one uppercase** letter
- At least **one lowercase** letter
- At least **one digit**

---

## Troubleshooting

### Application won't start?
- Make sure **Java 17+** is installed: `java -version`
- Check that all `.java` files are compiled properly

### Login not working?
- Use the **demo credentials** listed above
- Check Caps Lock is off
- If account is locked, wait 30 minutes or restart with fresh data

### Data got corrupted?
- Delete the `data/sgdss.dat` file - the system will recreate fresh demo data on next launch
- Or restore from a backup if you created one

### Theme colors look wrong?
- The system uses Windows Look & Feel by default
- Theme switching should work instantly
- If colors seem off, try switching theme twice (Light -> Dark -> Light)

---

## Project Structure

```
sgdss/
├── Main.java                 # Application entry point
├── LoginFrame.java           # Login and registration screen
├── MainFrame.java            # Main dashboard with tabs
├── UiUtil.java               # UI styling utilities (buttons, tables, themes)
├── Theme.java                # Light/Dark color schemes
│
├── AppContext.java           # Central dependency container
├── AppState.java             # In-memory data model
├── DataStore.java            # File persistence layer
├── DemoData.java             # Pre-loaded demo data
│
├── AuthService.java          # Login, registration, password reset
├── IssueService.java         # Issue CRUD, SLA monitoring
├── AdminService.java         # City/Dept/Category/User management
├── PollService.java          # Poll creation and voting
├── ReportService.java        # Analytics and statistics
├── NotificationService.java  # Alert system
├── SmartEngine.java          # AI features (priority, duplicate, assignment)
│
├── User.java                 # User entity
├── Issue.java                # Issue entity with SLA tracking
├── IssueComment.java         # Comment on issues
├── City.java                 # City entity
├── Department.java           # Department entity
├── Category.java             # Issue category entity
├── Poll.java                 # Poll entity
├── PollOption.java           # Poll choice entity
├── Announcement.java         # Announcement entity
├── Feedback.java             # Citizen feedback entity
├── Notification.java         # Notification entity
├── AuditEntry.java           # Audit log entry
├── SystemSetting.java        # Configuration key-value pairs
│
├── Role.java                 # User roles enum
├── Status.java               # Issue statuses enum
├── Priority.java             # Priority levels enum
│
├── DashboardPanel.java       # Overview dashboard
├── IssuesPanel.java          # Issue management workspace
├── PollPanel.java            # Polls and voting
├── AnnouncementsPanel.java   # Announcements viewer
├── FeedbackPanel.java        # Feedback submission
├── ReportsPanel.java         # Analytics and export
├── AuditPanel.java           # Audit log viewer
├── NotificationsPanel.java   # Notification inbox
├── AdminPanel.java           # Admin tools
├── SettingsPanel.java        # System settings
│
├── SimpleBarChartPanel.java  # Horizontal bar chart component
├── PieChartPanel.java        # Donut chart component
└── PasswordUtil.java         # Password hashing utilities
```

---

## Technology Stack

| Technology | Purpose |
|------------|---------|
| Java 17 | Core programming language |
| Java Swing | Desktop GUI framework |
| Java Serialization | Data persistence |
| PBKDF2WithHmacSHA256 | Password hashing |
| Base64 | Salt encoding |

---

## Future Enhancements

While SGDSS is fully functional, here are some ideas for expansion:
- **Email/SMS notifications** for real-time alerts
- **Mobile companion app** for on-the-go issue reporting
- **Geolocation integration** for automatic location tagging
- **Photo attachments** for issue evidence
- **Multi-language support** for diverse citizen populations
- **Integration with GIS** for map-based issue visualization
- **Machine learning** for predictive maintenance and trend analysis

---

## About the Project

SGDSS was built as a comprehensive academic/professional project demonstrating:
- Object-oriented design principles
- Role-based access control implementation
- Smart algorithm integration (priority prediction, duplicate detection)
- SLA management and escalation workflows
- Data persistence and backup strategies
- Professional UI/UX with theme support
- Complete audit trails for governance transparency

---

## License

This project is provided as-is for educational and demonstration purposes. Feel free to modify and extend it for your own use.

---

## Support

For issues, questions, or contributions, please contact your system administrator or the project maintainer.

> **Built with care for smarter cities and happier citizens.**

---

*Last updated: June 2026*
