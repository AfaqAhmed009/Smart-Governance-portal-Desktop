package sgdss;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;

public class AnnouncementsPanel extends JPanel {
    private final AppContext ctx;
    private final User user;
    private final Model model = new Model();
    private final JTable table = new JTable(model);
    private final JTextArea content = new JTextArea();

    public AnnouncementsPanel(AppContext ctx, User user) {
        this.ctx = ctx;
        this.user = user;
        setLayout(new BorderLayout(12,12));
        setBackground(UiUtil.bg());

        UiUtil.styleTable(table);
        table.setRowHeight(26);
        table.getSelectionModel().addListSelectionListener(e -> { if (!e.getValueIsAdjusting()) showAnnouncement(); });

        JPanel left = UiUtil.card();
        left.setLayout(new BorderLayout(8,8));
        left.add(UiUtil.label("Announcements", 18, true), BorderLayout.NORTH);
        left.add(UiUtil.wrap(table), BorderLayout.CENTER);

        JPanel right = UiUtil.card();
        right.setLayout(new BorderLayout(8,8));
        content.setEditable(false);
        content.setLineWrap(true);
        content.setWrapStyleWord(true);
        UiUtil.styleTextArea(content);

        JButton create = UiUtil.primaryButton("Create");
        create.setEnabled(user.getRole().atLeast(Role.CITY_ADMIN));
        create.addActionListener(e -> createAnnouncement());

        right.add(create, BorderLayout.NORTH);
        right.add(UiUtil.wrap(content), BorderLayout.CENTER);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, right);
        split.setResizeWeight(0.6);
        split.setBorder(null);
        add(split, BorderLayout.CENTER);

        refresh();
    }

    private void refresh() {
        model.refresh();
        showAnnouncement();
    }

    private void showAnnouncement() {
        int row = table.getSelectedRow();
        if (row < 0 || model.rows.isEmpty()) {
            content.setText("Select an announcement to view details.");
            return;
        }
        Announcement a = model.rows.get(table.convertRowIndexToModel(row));
        content.setText(a.getTitle() + "\n\n" + a.getContent());
    }

    private void createAnnouncement() {
        JTextField title = UiUtil.field(24);
        JTextArea text = new JTextArea(6, 24);
        JComboBox<City> city = new JComboBox<>(ctx.state.getCities().toArray(new City[0]));
        JComboBox<Department> dept = new JComboBox<>(ctx.state.getDepartments().toArray(new Department[0]));
        UiUtil.styleTextArea(text);
        UiUtil.styleComboBox(city);
        UiUtil.styleComboBox(dept);
        Object[] fields = {"Title", title, "Content", new JScrollPane(text), "City", city, "Department", dept};
        int r = JOptionPane.showConfirmDialog(this, fields, "Create Announcement", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (r == JOptionPane.OK_OPTION) {
            City c = (City) city.getSelectedItem();
            Department d = (Department) dept.getSelectedItem();
            ctx.state.getAnnouncements().add(new Announcement(user.getId(), title.getText().trim(), text.getText().trim(), c == null ? null : c.getId(), d == null ? null : d.getId()));
            ctx.state.getAudits().add(new AuditEntry(user.getId(), user.getDisplayName(), "ANNOUNCEMENT_CREATED", title.getText().trim()));
            ctx.store.save();
            refresh();
        }
    }

    class Model extends AbstractTableModel {
        java.util.List<Announcement> rows = java.util.Collections.emptyList();
        void refresh() { rows = ctx.state.getAnnouncements(); fireTableDataChanged(); }
        @Override public int getRowCount() { return rows.size(); }
        @Override public int getColumnCount() { return 3; }
        @Override public String getColumnName(int c) { return new String[]{"Title","City","Department"}[c]; }
        @Override public Object getValueAt(int r, int c) {
            Announcement a = rows.get(r);
            return switch (c) {
                case 0 -> a.getTitle();
                case 1 -> ctx.issueService.findCity(a.getCityId()).map(City::getName).orElse("All Cities");
                case 2 -> ctx.issueService.findDepartment(a.getDepartmentId()).map(Department::getName).orElse("All Departments");
                default -> "";
            };
        }
    }
}
