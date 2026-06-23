package sgdss;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;

public class AuditPanel extends JPanel {
    private final AppContext ctx;
    private final User user;
    private final Model model = new Model();
    private final JTable table = new JTable(model);

    public AuditPanel(AppContext ctx, User user) {
        this.ctx = ctx;
        this.user = user;
        setLayout(new BorderLayout(12,12));
        setBackground(UiUtil.bg());

        UiUtil.styleTable(table);
        table.setRowHeight(26);

        JPanel p = UiUtil.card();
        p.setLayout(new BorderLayout(8,8));
        p.add(UiUtil.label("Audit log", 18, true), BorderLayout.NORTH);
        p.add(UiUtil.wrap(table), BorderLayout.CENTER);
        add(p, BorderLayout.CENTER);

        JButton refresh = UiUtil.secondaryButton("Refresh");
        refresh.addActionListener(e -> model.refresh());
        p.add(refresh, BorderLayout.SOUTH);

        model.refresh();
    }

    class Model extends AbstractTableModel {
        java.util.List<AuditEntry> rows = java.util.Collections.emptyList();
        void refresh() { rows = ctx.state.getAudits(); fireTableDataChanged(); }
        @Override public int getRowCount() { return rows.size(); }
        @Override public int getColumnCount() { return 4; }
        @Override public String getColumnName(int c) { return new String[]{"Time","Actor","Action","Details"}[c]; }
        @Override public Object getValueAt(int r, int c) {
            AuditEntry a = rows.get(r);
            return switch (c) {
                case 0 -> a.getTime();
                case 1 -> a.getActorName();
                case 2 -> a.getAction();
                case 3 -> a.getDetails();
                default -> "";
            };
        }
    }
}
