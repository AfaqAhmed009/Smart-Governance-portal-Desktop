package sgdss;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;

public class AdminPanel extends JPanel {
    private final AppContext ctx;
    private final User user;
    private final UserModel userModel = new UserModel();
    private final JTable userTable = new JTable(userModel);

    public AdminPanel(AppContext ctx, User user) {
        this.ctx = ctx;
        this.user = user;
        setLayout(new BorderLayout(12,12));
        setBackground(UiUtil.bg());

        UiUtil.styleTable(userTable);
        userTable.setRowHeight(26);

        JButton addCity = UiUtil.primaryButton("Add City");
        JButton addDept = UiUtil.secondaryButton("Add Department");
        JButton addCat = UiUtil.secondaryButton("Add Category");
        JButton addOfficer = UiUtil.secondaryButton("Add Officer");
        JButton promote = UiUtil.secondaryButton("Change Role");
        JButton refresh = UiUtil.secondaryButton("Refresh");

        addCity.addActionListener(e -> addCity());
        addDept.addActionListener(e -> addDepartment());
        addCat.addActionListener(e -> addCategory());
        addOfficer.addActionListener(e -> addOfficer());
        promote.addActionListener(e -> changeRole());
        refresh.addActionListener(e -> userModel.refresh());

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        toolbar.setOpaque(false);
        toolbar.add(addCity);
        toolbar.add(addDept);
        toolbar.add(addCat);
        toolbar.add(addOfficer);
        toolbar.add(promote);
        toolbar.add(refresh);

        JPanel p = UiUtil.card();
        p.setLayout(new BorderLayout(8,8));
        JPanel head = new JPanel();
        head.setOpaque(false);
        head.setLayout(new BoxLayout(head, BoxLayout.Y_AXIS));
        head.add(UiUtil.label("Administration tools", 18, true));
        head.add(Box.createVerticalStrut(8));
        head.add(toolbar);
        p.add(head, BorderLayout.NORTH);
        p.add(UiUtil.wrap(userTable), BorderLayout.CENTER);
        add(p, BorderLayout.CENTER);

        userModel.refresh();
    }

    private User selectedUser() {
        int row = userTable.getSelectedRow();
        if (row < 0) return null;
        return userModel.rows.get(userTable.convertRowIndexToModel(row));
    }

    private void addCity() {
        JTextField n = UiUtil.field(20);
        JTextField c = UiUtil.field(10);
        int r = JOptionPane.showConfirmDialog(this, new Object[]{"City name", n, "Code", c}, "Add City", JOptionPane.OK_CANCEL_OPTION);
        if (r == JOptionPane.OK_OPTION) {
            ctx.adminService.addCity(user, n.getText().trim(), c.getText().trim());
            userModel.refresh();
        }
    }

    private void addDepartment() {
        JTextField n = UiUtil.field(20);
        JTextField c = UiUtil.field(10);
        JComboBox<City> city = new JComboBox<>(ctx.state.getCities().toArray(new City[0]));
        UiUtil.styleComboBox(city);
        int r = JOptionPane.showConfirmDialog(this, new Object[]{"City", city, "Department name", n, "Code", c}, "Add Department", JOptionPane.OK_CANCEL_OPTION);
        if (r == JOptionPane.OK_OPTION) {
            City selected = (City) city.getSelectedItem();
            ctx.adminService.addDepartment(user, selected == null ? null : selected.getId(), n.getText().trim(), c.getText().trim());
            userModel.refresh();
        }
    }

    private void addCategory() {
        JTextField n = UiUtil.field(20);
        JComboBox<Department> dept = new JComboBox<>(ctx.state.getDepartments().toArray(new Department[0]));
        UiUtil.styleComboBox(dept);
        int r = JOptionPane.showConfirmDialog(this, new Object[]{"Department", dept, "Category name", n}, "Add Category", JOptionPane.OK_CANCEL_OPTION);
        if (r == JOptionPane.OK_OPTION) {
            Department selected = (Department) dept.getSelectedItem();
            ctx.adminService.addCategory(user, selected == null ? null : selected.getId(), n.getText().trim());
            userModel.refresh();
        }
    }

    private void addOfficer() {
        JTextField uname = UiUtil.field(18);
        JTextField name = UiUtil.field(18);
        JTextField email = UiUtil.field(18);
        JTextField phone = UiUtil.field(18);
        JPasswordField pass = UiUtil.passwordField(18);
        JComboBox<City> city = new JComboBox<>(ctx.state.getCities().toArray(new City[0]));
        JComboBox<Department> dept = new JComboBox<>(ctx.state.getDepartments().toArray(new Department[0]));
        UiUtil.styleComboBox(city);
        UiUtil.styleComboBox(dept);
        int r = JOptionPane.showConfirmDialog(this, new Object[]{"Username", uname, "Display name", name, "Email", email, "Phone", phone, "Password", pass, "City", city, "Department", dept}, "Add Officer", JOptionPane.OK_CANCEL_OPTION);
        if (r == JOptionPane.OK_OPTION) {
            City c = (City) city.getSelectedItem();
            Department d = (Department) dept.getSelectedItem();
            ctx.adminService.addOfficer(user, uname.getText().trim(), name.getText().trim(), email.getText().trim(), phone.getText().trim(), new String(pass.getPassword()), c == null ? null : c.getId(), d == null ? null : d.getId());
            userModel.refresh();
        }
    }

    private void changeRole() {
        User selected = selectedUser();
        if (selected == null) return;
        Role[] roles = Role.values();
        Role role = (Role) JOptionPane.showInputDialog(this, "Select new role", "Role", JOptionPane.PLAIN_MESSAGE, null, roles, selected.getRole());
        if (role != null) {
            ctx.adminService.promote(user, selected, role);
            userModel.refresh();
        }
    }

    class UserModel extends AbstractTableModel {
        java.util.List<User> rows = java.util.Collections.emptyList();
        void refresh() { rows = ctx.state.getUsers(); fireTableDataChanged(); }
        @Override public int getRowCount() { return rows.size(); }
        @Override public int getColumnCount() { return 5; }
        @Override public String getColumnName(int c) { return new String[]{"Username","Name","Role","City","Department"}[c]; }
        @Override public Object getValueAt(int r, int c) {
            User u = rows.get(r);
            return switch (c) {
                case 0 -> u.getUsername();
                case 1 -> u.getDisplayName();
                case 2 -> u.getRole().displayName();
                case 3 -> ctx.issueService.findCity(u.getCityId()).map(City::getName).orElse("-");
                case 4 -> ctx.issueService.findDepartment(u.getDepartmentId()).map(Department::getName).orElse("-");
                default -> "";
            };
        }
    }
}
