package sgdss;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;

public class FeedbackPanel extends JPanel {
    private final AppContext ctx;
    private final User user;
    private final Model model = new Model();
    private final JTable table = new JTable(model);

    public FeedbackPanel(AppContext ctx, User user) {
        this.ctx = ctx;
        this.user = user;
        setLayout(new BorderLayout(12,12));
        setBackground(UiUtil.bg());

        UiUtil.styleTable(table);
        table.setRowHeight(26);

        JButton add = UiUtil.primaryButton("Add Feedback");
        add.addActionListener(e -> addFeedback());

        JPanel top = UiUtil.card();
        top.setLayout(new BorderLayout(8,8));
        top.add(UiUtil.label("Citizen feedback", 18, true), BorderLayout.NORTH);
        top.add(UiUtil.wrap(table), BorderLayout.CENTER);
        top.add(add, BorderLayout.SOUTH);
        add(top, BorderLayout.CENTER);

        refresh();
    }

    private void refresh() {
        model.refresh();
    }

    private void addFeedback() {
        JTextArea text = new JTextArea(5, 25);
        JSpinner rating = new JSpinner(new SpinnerNumberModel(5, 1, 5, 1));
        UiUtil.styleTextArea(text);
        UiUtil.styleSpinner(rating);
        Object[] fields = {"Feedback", new JScrollPane(text), "Rating", rating};
        int r = JOptionPane.showConfirmDialog(this, fields, "Add Feedback", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (r == JOptionPane.OK_OPTION) {
            ctx.state.getFeedbacks().add(new Feedback(user.getId(), text.getText().trim(), (Integer) rating.getValue()));
            ctx.state.getAudits().add(new AuditEntry(user.getId(), user.getDisplayName(), "FEEDBACK_ADDED", "Rating " + rating.getValue()));
            ctx.store.save();
            refresh();
        }
    }

    class Model extends AbstractTableModel {
        java.util.List<Feedback> rows = java.util.Collections.emptyList();
        void refresh() { rows = ctx.state.getFeedbacks(); fireTableDataChanged(); }
        @Override public int getRowCount() { return rows.size(); }
        @Override public int getColumnCount() { return 4; }
        @Override public String getColumnName(int c) { return new String[]{"User","Rating","Feedback","Time"}[c]; }
        @Override public Object getValueAt(int r, int c) {
            Feedback f = rows.get(r);
            return switch (c) {
                case 0 -> ctx.issueService.findUser(f.getUserId()).map(User::getDisplayName).orElse("-");
                case 1 -> f.getRating();
                case 2 -> f.getText();
                case 3 -> f.getCreatedAt();
                default -> "";
            };
        }
    }
}
