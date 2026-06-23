package sgdss;

/**
 * AppContext is the central dependency container for the SGDSS application.
 * It wires together all services and provides a single point of access
 * to the application's business logic layer.
 */
public class AppContext {
    public final DataStore store;
    public final AppState state;
    public final AuthService authService;
    public final IssueService issueService;
    public final PollService pollService;
    public final AdminService adminService;
    public final ReportService reportService;
    public final NotificationService notificationService;

    public AppContext() {
        this.store = new DataStore();
        this.state = store.loadOrCreate();
        this.authService = new AuthService(state, store);
        this.issueService = new IssueService(state, store);
        this.pollService = new PollService(state, store);
        this.adminService = new AdminService(state, store);
        this.reportService = new ReportService(state);
        this.notificationService = new NotificationService(state, store);
    }
}
