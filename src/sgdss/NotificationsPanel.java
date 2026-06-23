package sgdss;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * NotificationsPanel displays system notifications for the current user.
 * Supports marking notifications as read and filtering by status.
 */
public class NotificationsPanel extends JPanel {
    private final AppContext ctx;
    private final User user;
    private final Model model = new Model();
    private final JTable table = new JTable(model);

    public NotificationsPanel(AppContext ctx, User user) {
        this.ctx = ctx;
        this.user = user;
        setLayout(new BorderLayout(12, 12));
        setBackground(UiUtil.bg());

        UiUtil.styleTable(table);
        table.setRowHeight(28);

        JButton markRead = UiUtil.primaryButton("Mark All Read");
        JButton refresh = UiUtil.secondaryButton("Refresh");

        markRead.addActionListener(e -> {
            ctx.notificationService.markAllRead(user.getId());
            model.refresh();
        });
        refresh.addActionListener(e -> model.refresh());

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        toolbar.setOpaque(false);
        toolbar.add(markRead);
        toolbar.add(refresh);

        JPanel p = UiUtil.card();
        p.setLayout(new BorderLayout(8, 8));
        p.add(UiUtil.label("Notifications", 18, true), BorderLayout.NORTH);
        p.add(toolbar, BorderLayout.SOUTH);
        p.add(UiUtil.wrap(table), BorderLayout.CENTER);
        add(p, BorderLayout.CENTER);

        model.refresh();
    }

    class Model extends AbstractTableModel {
        List<Notification> rows = new ArrayList<>();

        void refresh() {
            rows = ctx.notificationService.getNotificationsForUser(user.getId());
            fireTableDataChanged();
        }

        @Override public int getRowCount() { return rows.size(); }
        @Override public int getColumnCount() { return 4; }
        @Override public String getColumnName(int c) {
            return new String[]{"Time", "Type", "Title", "Status"}[c];
        }
        @Override public Object getValueAt(int r, int c) {
            Notification n = rows.get(r);
            return switch (c) {
                case 0 -> n.getCreatedAt().toLocalDate() + " " + n.getCreatedAt().toLocalTime().withSecond(0).withNano(0);
                case 1 -> n.getType();
                case 2 -> n.getTitle();
                case 3 -> n.getStatus();
                default -> "";
            };
        }
    }
}
