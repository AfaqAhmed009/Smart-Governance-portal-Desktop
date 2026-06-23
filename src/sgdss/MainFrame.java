package sgdss;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainFrame extends JFrame {
    private final AppContext ctx;
    private final User currentUser;
    private JLabel headerInfo;
    private JLabel notificationBadge;
    private JTabbedPane tabs;

    public MainFrame(AppContext ctx, User currentUser) {
        this.ctx = ctx;
        this.currentUser = currentUser;
        setTitle("SGDSS - " + currentUser.getRole().displayName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1450, 900);
        setLocationRelativeTo(null);
        UiUtil.applyBase(this);
        setLayout(new BorderLayout(12, 12));

        add(buildTopBar(), BorderLayout.NORTH);
        add(buildContent(), BorderLayout.CENTER);

        startSlaMonitor();
    }

    private JComponent buildTopBar() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(UiUtil.accent());
        p.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));

        JLabel title = new JLabel("SGDSS Control Center");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        p.add(title, BorderLayout.WEST);

        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        centerPanel.setOpaque(false);

        headerInfo = new JLabel();
        headerInfo.setForeground(new Color(220, 230, 250));
        headerInfo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        updateHeaderInfo();
        centerPanel.add(headerInfo);

        int unreadCount = ctx.notificationService.getUnreadCount(currentUser.getId());
        notificationBadge = UiUtil.badge(String.valueOf(unreadCount), Theme.danger(), Color.WHITE);
        if (unreadCount == 0) notificationBadge.setVisible(false);
        JButton notifBtn = UiUtil.secondaryButton("Notifications");
        notifBtn.setBackground(new Color(40, 90, 160));
        notifBtn.setForeground(Color.WHITE);
        notifBtn.setOpaque(true);
        notifBtn.setContentAreaFilled(true);
        notifBtn.setBorderPainted(false);
        notifBtn.addActionListener(e -> showNotifications());
        JPanel notifPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        notifPanel.setOpaque(false);
        notifPanel.add(notifBtn);
        notifPanel.add(notificationBadge);
        centerPanel.add(notifPanel);

        p.add(centerPanel, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setOpaque(false);

        JButton themeBtn = UiUtil.secondaryButton(Theme.isDark() ? "Light Mode" : "Dark Mode");
        themeBtn.setBackground(new Color(40, 90, 160));
        themeBtn.setForeground(Color.WHITE);
        themeBtn.setOpaque(true);
        themeBtn.setContentAreaFilled(true);
        themeBtn.addActionListener(e -> toggleTheme());
        rightPanel.add(themeBtn);

        JButton helpBtn = UiUtil.secondaryButton("Help");
        helpBtn.setBackground(new Color(40, 90, 160));
        helpBtn.setForeground(Color.WHITE);
        helpBtn.setOpaque(true);
        helpBtn.setContentAreaFilled(true);
        helpBtn.setBorderPainted(false);
        helpBtn.addActionListener(e -> showHelp());
        rightPanel.add(helpBtn);

        JButton logout = UiUtil.secondaryButton("Logout");
        logout.setBackground(new Color(180, 50, 50));
        logout.setForeground(Color.WHITE);
        logout.setOpaque(true);
        logout.setContentAreaFilled(true);
        logout.setBorderPainted(false);
        logout.addActionListener(e -> {
            dispose();
            new LoginFrame(ctx).setVisible(true);
        });
        rightPanel.add(logout);

        p.add(rightPanel, BorderLayout.EAST);
        return p;
    }

    private void updateHeaderInfo() {
        headerInfo.setText("Logged in as: " + currentUser.summary() + "  |  " + roleHint(currentUser.getRole()));
    }

    private String roleHint(Role role) {
        return switch (role) {
            case CITIZEN -> "Report issues, track status, vote in polls, give feedback";
            case OFFICER -> "Handle assigned civic issues and update status";
            case DEPT_ADMIN -> "Manage department operations and officers";
            case CITY_ADMIN -> "Oversee city-level governance and departments";
            case GLOBAL_ADMIN -> "Full system administration and analytics";
        };
    }

    private JComponent buildContent() {
        tabs = new JTabbedPane();
        tabs.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tabs.setBackground(Theme.bg());
        tabs.setForeground(Theme.text());

        addTab("Dashboard", new DashboardPanel(ctx, currentUser));
        addTab("Issues", new IssuesPanel(ctx, currentUser));
        addTab("Polls", new PollPanel(ctx, currentUser));
        addTab("Announcements", new AnnouncementsPanel(ctx, currentUser));
        addTab("Feedback", new FeedbackPanel(ctx, currentUser));
        addTab("Reports", new ReportsPanel(ctx, currentUser));
        addTab("Audit Log", new AuditPanel(ctx, currentUser));
        addTab("Notifications", new NotificationsPanel(ctx, currentUser));

        if (currentUser.getRole().atLeast(Role.DEPT_ADMIN)) {
            addTab("Admin Tools", new AdminPanel(ctx, currentUser));
        }
        if (currentUser.getRole().atLeast(Role.GLOBAL_ADMIN)) {
            addTab("Settings", new SettingsPanel(ctx, currentUser));
        }

        // Apply custom tab components for proper dark mode coloring
        applyTabStyling();

        return tabs;
    }

    private void addTab(String title, JComponent content) {
        tabs.addTab(title, content);
        int index = tabs.getTabCount() - 1;
        // Set custom tab component for full color control
        JLabel tabLabel = new JLabel(title);
        tabLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tabLabel.setForeground(Theme.text());
        tabLabel.setOpaque(true);
        tabLabel.setBackground(Theme.panel());
        tabLabel.setBorder(new EmptyBorder(8, 14, 8, 14));
        tabs.setTabComponentAt(index, tabLabel);
    }

    private void applyTabStyling() {
        // Style the tabbed pane itself
        tabs.setBackground(Theme.bg());
        // Style each tab's custom component
        for (int i = 0; i < tabs.getTabCount(); i++) {
            Component comp = tabs.getTabComponentAt(i);
            if (comp instanceof JLabel label) {
                label.setBackground(Theme.panel());
                label.setForeground(Theme.text());
            }
        }
    }

    private void toggleTheme() {
        Theme.setMode(Theme.isDark() ? Theme.Mode.LIGHT : Theme.Mode.DARK);
        Theme.configureUIManager();
        getContentPane().removeAll();
        add(buildTopBar(), BorderLayout.NORTH);
        add(buildContent(), BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void showNotifications() {
        for (int i = 0; i < tabs.getTabCount(); i++) {
            if (tabs.getTitleAt(i).equals("Notifications")) {
                tabs.setSelectedIndex(i);
                break;
            }
        }
    }

    private void showHelp() {
        StringBuilder sb = new StringBuilder();
        sb.append("SGDSS Help Guide:\n\n");
        sb.append("Dashboard - View system overview and key metrics\n");
        sb.append("Issues - Submit, track, and manage civic issues\n");
        sb.append("Polls - Participate in public polls and surveys\n");
        sb.append("Announcements - View city and department announcements\n");
        sb.append("Feedback - Submit ratings and comments about services\n");
        sb.append("Reports - Generate analytics and export data\n");
        sb.append("Audit Log - View system activity history\n");
        sb.append("Notifications - View your alerts and updates\n");
        sb.append("Admin Tools - Manage cities, departments, users (Admin only)\n");
        sb.append("Settings - Configure system preferences (Global Admin only)\n\n");
        sb.append("For more help, contact the system administrator.");
        JOptionPane.showMessageDialog(this, sb.toString(), "SGDSS Help", JOptionPane.INFORMATION_MESSAGE);
    }

    private void startSlaMonitor() {
        Timer timer = new Timer(60000, e -> {
            ctx.issueService.checkAndEscalateSLAs();
            int unread = ctx.notificationService.getUnreadCount(currentUser.getId());
            if (unread > 0) {
                notificationBadge.setText(String.valueOf(unread));
                notificationBadge.setVisible(true);
            } else {
                notificationBadge.setVisible(false);
            }
        });
        timer.start();
    }
}
