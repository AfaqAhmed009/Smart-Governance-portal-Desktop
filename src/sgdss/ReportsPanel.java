package sgdss;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class ReportsPanel extends JPanel {
    private final AppContext ctx;
    private final User user;

    public ReportsPanel(AppContext ctx, User user) {
        this.ctx = ctx;
        this.user = user;
        setLayout(new BorderLayout(12,12));
        setBackground(UiUtil.bg());

        JPanel top = new JPanel(new GridLayout(1, 4, 12, 12));
        top.setOpaque(false);
        top.add(metric("Total Users", ctx.state.getUsers().size()));
        top.add(metric("Total Issues", ctx.state.getIssues().size()));
        top.add(metric("Open Issues", ctx.reportService.openIssues()));
        top.add(metric("Announcements", ctx.state.getAnnouncements().size()));

        JPanel charts = new JPanel(new GridLayout(1, 2, 12, 12));
        charts.setOpaque(false);
        charts.add(chart("By Status", ctx.reportService.issueCountsByStatus()));
        charts.add(chart("By Priority", ctx.reportService.issueCountsByPriority()));

        JButton export = UiUtil.primaryButton("Export CSV Summary");
        export.addActionListener(e -> exportCsv());

        JPanel center = UiUtil.card();
        center.setLayout(new BorderLayout(8,8));
        center.add(top, BorderLayout.NORTH);
        center.add(charts, BorderLayout.CENTER);
        center.add(export, BorderLayout.SOUTH);

        add(center, BorderLayout.CENTER);
    }

    private JComponent metric(String name, int value) {
        JPanel p = UiUtil.card();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        JLabel t = new JLabel(name);
        t.setForeground(Theme.textSecondary());
        t.setFont(new Font("SansSerif", Font.PLAIN, 13));
        JLabel v = new JLabel(String.valueOf(value));
        v.setFont(new Font("SansSerif", Font.BOLD, 28));
        v.setForeground(Theme.text());
        p.add(t);
        p.add(Box.createVerticalStrut(4));
        p.add(v);
        return p;
    }

    private JComponent chart(String title, Map<String, Integer> data) {
        JPanel p = UiUtil.card();
        p.setLayout(new BorderLayout(8,8));
        p.add(UiUtil.label(title, 16, true), BorderLayout.NORTH);
        p.add(new SimpleBarChartPanel(data), BorderLayout.CENTER);
        return p;
    }

    private void exportCsv() {
        try {
            Files.createDirectories(Path.of("exports"));
            Path file = Path.of("exports", "sgdss-summary.csv");
            try (FileWriter w = new FileWriter(file.toFile())) {
                w.write("Metric,Value\n");
                w.write("Users," + ctx.state.getUsers().size() + "\n");
                w.write("Issues," + ctx.state.getIssues().size() + "\n");
                w.write("Open Issues," + ctx.reportService.openIssues() + "\n");
                w.write("Polls," + ctx.state.getPolls().size() + "\n");
                w.write("Feedbacks," + ctx.state.getFeedbacks().size() + "\n");
            }
            JOptionPane.showMessageDialog(this, "CSV exported to " + file.toAbsolutePath(), "Export", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Export failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}
