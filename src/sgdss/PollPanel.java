package sgdss;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * PollPanel allows users to view, vote in, and create polls.
 * Admins can create new polls with multiple options.
 */
public class PollPanel extends JPanel {
    private final AppContext ctx;
    private final User user;
    private final PollTableModel model;
    private final JTable table;
    private final JTextArea info = new JTextArea();
    private final JPanel chartPanel;

    public PollPanel(AppContext ctx, User user) {
        this.ctx = ctx;
        this.user = user;
        setLayout(new BorderLayout(12, 12));
        setBackground(UiUtil.bg());

        model = new PollTableModel();
        table = new JTable(model);
        UiUtil.styleTable(table);
        table.getSelectionModel().addListSelectionListener(e -> { if (!e.getValueIsAdjusting()) showPoll(); });

        JPanel left = UiUtil.card();
        left.setLayout(new BorderLayout(8, 8));
        left.add(UiUtil.label("Active Polls", 18, true), BorderLayout.NORTH);
        left.add(UiUtil.wrap(table), BorderLayout.CENTER);

        JPanel right = UiUtil.card();
        right.setLayout(new BorderLayout(8, 8));

        JButton vote = UiUtil.primaryButton("Vote");
        JButton create = UiUtil.secondaryButton("Create Poll");
        JButton refresh = UiUtil.secondaryButton("Refresh");
        JButton closePoll = UiUtil.dangerButton("Close Selected");

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        top.setOpaque(false);
        top.add(vote);
        top.add(create);
        top.add(refresh);
        if (user.getRole().atLeast(Role.DEPT_ADMIN)) top.add(closePoll);

        UiUtil.styleTextArea(info);
        info.setLineWrap(true);
        info.setWrapStyleWord(true);

        chartPanel = new JPanel(new BorderLayout());
        chartPanel.setOpaque(false);
        chartPanel.setPreferredSize(new Dimension(350, 250));

        right.add(top, BorderLayout.NORTH);
        right.add(UiUtil.wrap(info), BorderLayout.CENTER);
        right.add(chartPanel, BorderLayout.SOUTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, right);
        split.setResizeWeight(0.5);
        split.setBorder(null);
        add(split, BorderLayout.CENTER);

        vote.addActionListener(e -> doVote());
        create.addActionListener(e -> doCreate());
        refresh.addActionListener(e -> refresh());
        closePoll.addActionListener(e -> closeSelected());

        create.setEnabled(user.getRole().atLeast(Role.DEPT_ADMIN));
        refresh();
    }

    private void refresh() {
        model.refresh();
        showPoll();
    }

    private Poll selected() {
        int row = table.getSelectedRow();
        if (row < 0) return null;
        return model.pollAt(table.convertRowIndexToModel(row));
    }

    private void showPoll() {
        Poll p = selected();
        chartPanel.removeAll();
        if (p == null) {
            info.setText("Select a poll to view details and results.");
            chartPanel.revalidate();
            chartPanel.repaint();
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(p.getQuestion()).append("\n\n");
        sb.append("Status: ").append(p.isActive() && !p.expired() ? "Active" : "Closed").append("\n");
        sb.append("Expires: ").append(p.getExpiresOn()).append("\n");
        sb.append("Total Votes: ").append(p.getOptions().stream().mapToInt(PollOption::getVoteCount).sum()).append("\n\n");
        for (PollOption opt : p.getOptions()) {
            sb.append(" - ").append(opt.getText()).append(" | votes: ").append(opt.getVoteCount()).append("\n");
        }
        info.setText(sb.toString());

        Map<String, Integer> data = new LinkedHashMap<>();
        for (PollOption opt : p.getOptions()) {
            data.put(opt.getText(), opt.getVoteCount());
        }
        if (!data.isEmpty() && data.values().stream().mapToInt(Integer::intValue).sum() > 0) {
            chartPanel.add(new PieChartPanel(data, "Poll Results"), BorderLayout.CENTER);
        }
        chartPanel.revalidate();
        chartPanel.repaint();
    }

    private void doVote() {
        Poll p = selected();
        if (p == null) return;
        if (p.getOptions().isEmpty()) return;
        if (!p.isActive() || p.expired()) {
            JOptionPane.showMessageDialog(this, "This poll is closed.");
            return;
        }
        PollOption[] options = p.getOptions().toArray(new PollOption[0]);
        PollOption sel = (PollOption) JOptionPane.showInputDialog(this, "Choose an option:", "Vote",
            JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        if (sel != null) {
            boolean ok = ctx.pollService.vote(user, p.getId(), sel.getId());
            if (ok) {
                JOptionPane.showMessageDialog(this, "Vote recorded successfully!");
            }
            refresh();
        }
    }

    private void doCreate() {
        if (!user.getRole().atLeast(Role.DEPT_ADMIN)) return;
        JTextField q = UiUtil.field(26);
        JTextField o1 = UiUtil.field(20);
        JTextField o2 = UiUtil.field(20);
        JTextField o3 = UiUtil.field(20);
        JTextField o4 = UiUtil.field(20);
        JTextField exp = UiUtil.field(10);
        Object[] fields = {"Question", q, "Option 1", o1, "Option 2", o2, "Option 3", o3, "Option 4", o4, "Expires in days", exp};
        int r = JOptionPane.showConfirmDialog(this, fields, "Create Poll", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (r == JOptionPane.OK_OPTION) {
            List<String> opts = new ArrayList<>();
            if (!o1.getText().trim().isEmpty()) opts.add(o1.getText().trim());
            if (!o2.getText().trim().isEmpty()) opts.add(o2.getText().trim());
            if (!o3.getText().trim().isEmpty()) opts.add(o3.getText().trim());
            if (!o4.getText().trim().isEmpty()) opts.add(o4.getText().trim());
            if (opts.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please provide at least one option.");
                return;
            }
            int days = 7;
            try { days = Integer.parseInt(exp.getText().trim()); } catch (Exception ignored) {}
            Poll poll = ctx.pollService.createPoll(user, q.getText().trim(),
                LocalDate.now().plusDays(Math.max(1, days)), opts);
            ctx.notificationService.notifyPollCreated(poll);
            refresh();
        }
    }

    private void closeSelected() {
        Poll p = selected();
        if (p == null) return;
        p.setActive(false);
        ctx.store.save();
        refresh();
    }

    class PollTableModel extends AbstractTableModel {
        private List<Poll> rows = new ArrayList<>();
        private final String[] cols = {"Question", "Status", "Expires", "Options", "Votes"};

        void refresh() { rows = ctx.pollService.activePolls(); fireTableDataChanged(); }
        Poll pollAt(int row) { return rows.get(row); }

        @Override public int getRowCount() { return rows.size(); }
        @Override public int getColumnCount() { return cols.length; }
        @Override public String getColumnName(int c) { return cols[c]; }
        @Override public Object getValueAt(int r, int c) {
            Poll p = rows.get(r);
            return switch (c) {
                case 0 -> p.getQuestion();
                case 1 -> p.isActive() && !p.expired() ? "Active" : "Closed";
                case 2 -> p.getExpiresOn();
                case 3 -> p.getOptions().size();
                case 4 -> p.getOptions().stream().mapToInt(PollOption::getVoteCount).sum();
                default -> "";
            };
        }
    }
}
