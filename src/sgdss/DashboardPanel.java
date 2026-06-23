package sgdss;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * DashboardPanel provides an overview of the system with key metrics,
 * charts, and recent activity feed. Adapts to user role.
 */
public class DashboardPanel extends JPanel {
    public DashboardPanel(AppContext ctx, User user) {
        setLayout(new BorderLayout(12, 12));
        setBackground(UiUtil.bg());

        JPanel top = new JPanel(new GridLayout(1, 5, 12, 12));
        top.setOpaque(false);
        top.add(metric("Total Users", ctx.state.getUsers().size(), Theme.info()));
        top.add(metric("Total Issues", ctx.state.getIssues().size(), Theme.accent()));
        top.add(metric("Open Issues", ctx.reportService.openIssues(), Theme.warning()));
        top.add(metric("SLA Breaches", ctx.reportService.slaBreachedIssues(), Theme.danger()));
        top.add(metric("Active Polls", ctx.pollService.activePolls().size(), Theme.success()));
        add(top, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(1, 2, 12, 12));
        center.setOpaque(false);
        center.add(buildStatusCard(ctx));
        center.add(buildPriorityCard(ctx));
        add(center, BorderLayout.CENTER);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setResizeWeight(0.5);
        split.setBorder(null);
        split.setLeftComponent(buildActivityCard(ctx, user));
        split.setRightComponent(buildQuickStatsCard(ctx));
        add(split, BorderLayout.SOUTH);
        split.setPreferredSize(new Dimension(0, 280));
    }

    private JComponent metric(String title, int value, Color accentColor) {
        JPanel p = UiUtil.card();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        JLabel t = new JLabel(title);
        t.setForeground(Theme.textSecondary());
        t.setFont(new Font("SansSerif", Font.PLAIN, 13));
        JLabel v = new JLabel(String.valueOf(value));
        v.setFont(new Font("SansSerif", Font.BOLD, 36));
        v.setForeground(accentColor);
        p.add(t);
        p.add(Box.createVerticalStrut(6));
        p.add(v);
        return p;
    }

    private JComponent buildStatusCard(AppContext ctx) {
        JPanel p = UiUtil.card();
        p.setLayout(new BorderLayout(8, 8));
        p.add(UiUtil.label("Issue Status Overview", 16, true), BorderLayout.NORTH);
        p.add(new SimpleBarChartPanel(ctx.reportService.issueCountsByStatus()), BorderLayout.CENTER);
        return p;
    }

    private JComponent buildPriorityCard(AppContext ctx) {
        JPanel p = UiUtil.card();
        p.setLayout(new BorderLayout(8, 8));
        p.add(UiUtil.label("Issue Priority Distribution", 16, true), BorderLayout.NORTH);
        p.add(new SimpleBarChartPanel(ctx.reportService.issueCountsByPriority()), BorderLayout.CENTER);
        return p;
    }

    private JComponent buildActivityCard(AppContext ctx, User user) {
        JPanel p = UiUtil.card();
        p.setLayout(new BorderLayout(8, 8));
        p.add(UiUtil.label("Recent Activity", 16, true), BorderLayout.NORTH);

        DefaultListModel<String> model = new DefaultListModel<>();
        List<AuditEntry> audits = ctx.state.getAudits();
        int start = Math.max(0, audits.size() - 15);
        for (int i = start; i < audits.size(); i++) {
            AuditEntry a = audits.get(i);
            model.addElement(a.getTime().toLocalDate() + " | " + a.getAction() + " | " + a.getDetails());
        }
        JList<String> list = new JList<>(model);
        list.setBackground(Theme.panel());
        list.setForeground(Theme.text());
        list.setFont(new Font("SansSerif", Font.PLAIN, 12));
        p.add(UiUtil.wrap(list), BorderLayout.CENTER);
        return p;
    }

    private JComponent buildQuickStatsCard(AppContext ctx) {
        JPanel p = UiUtil.card();
        p.setLayout(new BorderLayout(8, 8));
        p.add(UiUtil.label("Quick Statistics", 16, true), BorderLayout.NORTH);

        JPanel stats = new JPanel(new GridLayout(0, 1, 8, 8));
        stats.setOpaque(false);
        stats.add(statRow("Avg Resolution Time", String.format("%.1f hours", ctx.reportService.averageResolutionTime())));
        stats.add(statRow("Avg Feedback Rating", String.format("%.1f / 5.0", ctx.reportService.averageFeedbackRating())));
        stats.add(statRow("Total Departments", String.valueOf(ctx.state.getDepartments().size())));
        stats.add(statRow("Total Cities", String.valueOf(ctx.state.getCities().size())));
        stats.add(statRow("Total Categories", String.valueOf(ctx.state.getCategories().size())));
        stats.add(statRow("Total Announcements", String.valueOf(ctx.state.getAnnouncements().size())));

        p.add(stats, BorderLayout.CENTER);
        return p;
    }

    private JPanel statRow(String label, String value) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        JLabel l = new JLabel(label);
        l.setForeground(Theme.textSecondary());
        l.setFont(new Font("SansSerif", Font.PLAIN, 13));
        JLabel v = new JLabel(value);
        v.setForeground(Theme.text());
        v.setFont(new Font("SansSerif", Font.BOLD, 13));
        p.add(l, BorderLayout.WEST);
        p.add(v, BorderLayout.EAST);
        return p;
    }
}
