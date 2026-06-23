package sgdss;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

/**
 * IssuesPanel provides a comprehensive issue management workspace.
 * Features search, filtering, status updates, comments, and smart suggestions.
 */
public class IssuesPanel extends JPanel {
    private final AppContext ctx;
    private final User user;
    private final IssueTableModel model;
    private final JTable table;
    private final JTextArea details = new JTextArea();
    private final JLabel smartLabel = new JLabel(" ");
    private final JComboBox<City> cityBox;
    private final JComboBox<Department> deptBox;
    private final JComboBox<Category> catBox;
    private final JTextField titleField = UiUtil.field(18);
    private final JTextField locationField = UiUtil.field(18);
    private final JTextArea descriptionField = new JTextArea(4, 18);
    private final JSpinner severity = new JSpinner(new SpinnerNumberModel(3, 1, 5, 1));
    private final JComboBox<Status> statusBox = new JComboBox<>(Status.values());
    private final JTextField noteField = UiUtil.field(18);
    private final JTextField officerField = UiUtil.field(18);
    private final JTextField searchField = UiUtil.field(15);
    private final JComboBox<String> filterBox = new JComboBox<>(new String[]{"All", "New", "Assigned", "In Progress", "Resolved", "Closed", "Rejected", "Overdue"});

    public IssuesPanel(AppContext ctx, User user) {
        this.ctx = ctx;
        this.user = user;
        setLayout(new BorderLayout(12, 12));
        setBackground(UiUtil.bg());

        model = new IssueTableModel(ctx, user);
        table = new JTable(model);
        UiUtil.styleTable(table);
        table.setAutoCreateRowSorter(true);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) showIssue();
        });

        // Search and filter toolbar
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        toolbar.setOpaque(false);
        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setForeground(Theme.text());
        toolbar.add(searchLabel);
        toolbar.add(searchField);
        JLabel filterLabel = new JLabel("Filter:");
        filterLabel.setForeground(Theme.text());
        toolbar.add(filterLabel);
        toolbar.add(filterBox);
        JButton searchBtn = UiUtil.secondaryButton("Search");
        searchBtn.addActionListener(e -> doSearch());
        toolbar.add(searchBtn);
        JButton clearBtn = UiUtil.secondaryButton("Clear");
        clearBtn.addActionListener(e -> clearSearch());
        toolbar.add(clearBtn);

        JPanel left = UiUtil.card();
        left.setLayout(new BorderLayout(8, 8));
        left.add(UiUtil.label("Issue Workspace", 18, true), BorderLayout.NORTH);
        left.add(toolbar, BorderLayout.NORTH);
        left.add(UiUtil.wrap(table), BorderLayout.CENTER);

        JPanel right = UiUtil.card();
        right.setLayout(new BorderLayout(8, 8));

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4, 4, 4, 4);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.gridx = 0;
        g.gridy = 0;

        cityBox = new JComboBox<>(ctx.state.getCities().toArray(new City[0]));
        deptBox = new JComboBox<>(ctx.state.getDepartments().toArray(new Department[0]));
        catBox = new JComboBox<>(ctx.state.getCategories().toArray(new Category[0]));
        UiUtil.styleComboBox(cityBox);
        UiUtil.styleComboBox(deptBox);
        UiUtil.styleComboBox(catBox);
        UiUtil.styleTextArea(descriptionField);
        UiUtil.styleSpinner(severity);
        UiUtil.styleComboBox(statusBox);
        UiUtil.styleComboBox(filterBox);

        addRow(form, g, "City", cityBox);
        addRow(form, g, "Department", deptBox);
        addRow(form, g, "Category", catBox);
        addRow(form, g, "Title", titleField);
        addRow(form, g, "Location", locationField);
        addRow(form, g, "Severity (1-5)", severity);
        addRow(form, g, "Description", new JScrollPane(descriptionField));

        JButton submit = UiUtil.primaryButton("Submit Issue");
        JButton refresh = UiUtil.secondaryButton("Refresh");
        JButton comment = UiUtil.secondaryButton("Add Comment");
        JButton update = UiUtil.secondaryButton("Update Selected");
        JButton resolve = UiUtil.successButton("Resolve");

        JPanel actionRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        actionRow.setOpaque(false);
        actionRow.add(submit);
        actionRow.add(refresh);
        actionRow.add(comment);
        actionRow.add(update);
        actionRow.add(resolve);

        JPanel updatePanel = new JPanel(new GridLayout(0, 1, 4, 4));
        updatePanel.setOpaque(false);
        JLabel statusLabel = new JLabel("Status");
        statusLabel.setForeground(Theme.text());
        updatePanel.add(statusLabel);
        updatePanel.add(statusBox);
        JLabel officerLabel = new JLabel("Officer ID (optional)");
        officerLabel.setForeground(Theme.text());
        updatePanel.add(officerLabel);
        updatePanel.add(officerField);
        JLabel noteLabel = new JLabel("Note / Resolution");
        noteLabel.setForeground(Theme.text());
        updatePanel.add(noteLabel);
        updatePanel.add(noteField);
        updatePanel.add(smartLabel);

        UiUtil.styleTextArea(details);
        details.setRows(8);

        right.add(form, BorderLayout.NORTH);
        right.add(updatePanel, BorderLayout.CENTER);
        right.add(actionRow, BorderLayout.SOUTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, right);
        split.setResizeWeight(0.52);
        split.setBorder(null);
        add(split, BorderLayout.CENTER);
        add(UiUtil.wrap(details), BorderLayout.SOUTH);

        submit.addActionListener(e -> submitIssue());
        refresh.addActionListener(e -> refresh());
        comment.addActionListener(e -> addComment());
        update.addActionListener(e -> updateSelected());
        resolve.addActionListener(e -> resolveIssue());

        refresh();
    }

    private void addRow(JPanel p, GridBagConstraints g, String label, Component c) {
        g.gridx = 0;
        JLabel l = new JLabel(label);
        l.setForeground(Theme.text());
        p.add(l, g);
        g.gridx = 1;
        p.add(c, g);
        g.gridy++;
    }

    private void refresh() {
        cityBox.setModel(new DefaultComboBoxModel<>(ctx.state.getCities().toArray(new City[0])));
        deptBox.setModel(new DefaultComboBoxModel<>(ctx.state.getDepartments().toArray(new Department[0])));
        catBox.setModel(new DefaultComboBoxModel<>(ctx.state.getCategories().toArray(new Category[0])));
        UiUtil.styleComboBox(cityBox);
        UiUtil.styleComboBox(deptBox);
        UiUtil.styleComboBox(catBox);
        model.refresh();
        updateVisibleFields();
    }

    private void doSearch() {
        String query = searchField.getText().trim();
        String filter = (String) filterBox.getSelectedItem();
        model.refresh(query, filter);
    }

    private void clearSearch() {
        searchField.setText("");
        filterBox.setSelectedItem("All");
        model.refresh();
    }

    private void submitIssue() {
        try {
            City c = (City) cityBox.getSelectedItem();
            Department d = (Department) deptBox.getSelectedItem();
            Category cat = (Category) catBox.getSelectedItem();
            Issue issue = ctx.issueService.submitIssue(user,
                    c == null ? null : c.getId(),
                    d == null ? null : d.getId(),
                    cat == null ? null : cat.getId(),
                    titleField.getText().trim(),
                    descriptionField.getText().trim(),
                    locationField.getText().trim(),
                    (Integer) severity.getValue());
            StringBuilder msg = new StringBuilder();
            msg.append("Issue submitted successfully!\n");
            msg.append("Priority: ").append(issue.getPredictedPriority().displayName()).append("\n");
            msg.append("SLA: ").append(issue.getEstimatedSlaHours()).append(" hours\n");
            msg.append("Assigned to: ").append(
                issue.getAssignedOfficerId() != null
                    ? ctx.issueService.findUser(issue.getAssignedOfficerId()).map(User::getDisplayName).orElse("Auto-assigned")
                    : "Pending");
            JOptionPane.showMessageDialog(this, msg.toString(), "Issue Submitted", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            refresh();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Issue Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        titleField.setText("");
        locationField.setText("");
        descriptionField.setText("");
        severity.setValue(3);
    }

    private Issue selectedIssue() {
        int row = table.getSelectedRow();
        if (row < 0) return null;
        int modelRow = table.convertRowIndexToModel(row);
        return model.issueAt(modelRow);
    }

    private void showIssue() {
        Issue i = selectedIssue();
        if (i == null) {
            details.setText("Select an issue to view details.");
            return;
        }
        String city = ctx.issueService.findCity(i.getCityId()).map(City::getName).orElse("-");
        String dept = ctx.issueService.findDepartment(i.getDepartmentId()).map(Department::getName).orElse("-");
        String cat = ctx.issueService.findCategory(i.getCategoryId()).map(Category::getName).orElse("-");
        String submitter = ctx.issueService.findUser(i.getSubmitterId()).map(User::getDisplayName).orElse("-");
        String officer = ctx.issueService.findUser(i.getAssignedOfficerId()).map(User::getDisplayName).orElse("-");

        StringBuilder sb = new StringBuilder();
        sb.append("Issue: ").append(i.getTitle()).append("\n");
        sb.append("ID: ").append(i.getId()).append("\n");
        sb.append("Status: ").append(i.getStatus().displayName());
        if (i.isEscalated()) sb.append(" [ESCALATED]");
        sb.append("\n");
        sb.append("Priority: ").append(i.getPredictedPriority().displayName()).append("\n");
        sb.append("Severity: ").append(i.getSeverity()).append("/5\n");
        sb.append("SLA: ").append(i.getEstimatedSlaHours()).append(" hours");
        if (i.isSlaBreached()) sb.append(" [BREACHED]");
        else if (i.getHoursUntilDeadline() > 0) sb.append(" (").append(i.getHoursUntilDeadline()).append("h remaining)");
        sb.append("\n");
        sb.append("City: ").append(city).append("\n");
        sb.append("Department: ").append(dept).append("\n");
        sb.append("Category: ").append(cat).append("\n");
        sb.append("Submitter: ").append(submitter).append("\n");
        sb.append("Assigned Officer: ").append(officer).append("\n");
        sb.append("Location: ").append(UiUtil.safe(i.getLocation())).append("\n");
        sb.append("Created: ").append(i.getCreatedAt()).append("\n");
        sb.append("Updated: ").append(i.getUpdatedAt()).append("\n\n");
        sb.append("Description: ").append(UiUtil.safe(i.getDescription())).append("\n\n");
        sb.append("Smart Note: ").append(UiUtil.safe(i.getSmartNote())).append("\n");
        if (i.getDuplicateOfIssueId() != null) sb.append("\nDuplicate Of: ").append(i.getDuplicateOfIssueId());
        if (i.getResolutionNotes() != null) sb.append("\n\nResolution Notes: ").append(i.getResolutionNotes());

        details.setText(sb.toString());
        smartLabel.setText("Smart: " + UiUtil.safe(i.getSmartNote()));
        statusBox.setSelectedItem(i.getStatus());
        officerField.setText(UiUtil.safe(i.getAssignedOfficerId()));
    }

    private void addComment() {
        Issue i = selectedIssue();
        if (i == null) {
            JOptionPane.showMessageDialog(this, "Select an issue first.");
            return;
        }
        String text = JOptionPane.showInputDialog(this, "Enter your comment:");
        if (text != null && !text.isBlank()) {
            ctx.issueService.addComment(user, i, text);
            refresh();
            showIssue();
        }
    }

    private void updateSelected() {
        Issue i = selectedIssue();
        if (i == null) {
            JOptionPane.showMessageDialog(this, "Select an issue first.");
            return;
        }
        ctx.issueService.updateIssue(user, i, (Status) statusBox.getSelectedItem(),
            noteField.getText().trim(), officerField.getText().trim());
        refresh();
        showIssue();
    }

    private void resolveIssue() {
        Issue i = selectedIssue();
        if (i == null) {
            JOptionPane.showMessageDialog(this, "Select an issue first.");
            return;
        }
        String notes = JOptionPane.showInputDialog(this, "Enter resolution notes:");
        if (notes != null) {
            ctx.issueService.resolveIssue(user, i, notes);
            refresh();
            showIssue();
        }
    }

    private void updateVisibleFields() {
        boolean canCreate = user.getRole() == Role.CITIZEN || user.getRole().atLeast(Role.DEPT_ADMIN);
        titleField.setEnabled(canCreate);
        descriptionField.setEnabled(canCreate);
        locationField.setEnabled(canCreate);
        severity.setEnabled(canCreate);
        cityBox.setEnabled(canCreate);
        deptBox.setEnabled(canCreate);
        catBox.setEnabled(canCreate);
        if (user.getRole() == Role.CITIZEN) {
            statusBox.setEnabled(false);
            officerField.setEnabled(false);
            noteField.setEnabled(false);
        } else {
            statusBox.setEnabled(true);
            officerField.setEnabled(true);
            noteField.setEnabled(true);
        }
    }

    class IssueTableModel extends AbstractTableModel {
        private final AppContext ctx;
        private final User user;
        private List<Issue> rows = new ArrayList<>();
        private final String[] cols = {"ID", "Title", "Status", "Priority", "City", "Department", "Assigned", "SLA", "Age"};

        IssueTableModel(AppContext ctx, User user) {
            this.ctx = ctx;
            this.user = user;
            refresh();
        }

        void refresh() {
            rows = new ArrayList<>(ctx.issueService.visibleIssues(user));
            fireTableDataChanged();
        }

        void refresh(String query, String filter) {
            rows = new ArrayList<>();
            List<Issue> base = ctx.issueService.visibleIssues(user);
            for (Issue i : base) {
                boolean matchesQuery = query.isEmpty() ||
                    (i.getTitle() != null && i.getTitle().toLowerCase().contains(query.toLowerCase())) ||
                    (i.getDescription() != null && i.getDescription().toLowerCase().contains(query.toLowerCase())) ||
                    i.getId().toLowerCase().contains(query.toLowerCase());

                boolean matchesFilter = "All".equals(filter) ||
                    ("Overdue".equals(filter) && i.isSlaBreached()) ||
                    i.getStatus().displayName().equals(filter);

                if (matchesQuery && matchesFilter) rows.add(i);
            }
            fireTableDataChanged();
        }

        Issue issueAt(int row) { return rows.get(row); }

        @Override public int getRowCount() { return rows.size(); }
        @Override public int getColumnCount() { return cols.length; }
        @Override public String getColumnName(int c) { return cols[c]; }

        @Override public Object getValueAt(int r, int c) {
            Issue i = rows.get(r);
            return switch (c) {
                case 0 -> i.getId().substring(0, 8);
                case 1 -> i.getTitle();
                case 2 -> i.getStatus().displayName() + (i.isEscalated() ? " !" : "");
                case 3 -> i.getPredictedPriority().displayName();
                case 4 -> ctx.issueService.findCity(i.getCityId()).map(City::getName).orElse("-");
                case 5 -> ctx.issueService.findDepartment(i.getDepartmentId()).map(Department::getName).orElse("-");
                case 6 -> ctx.issueService.findUser(i.getAssignedOfficerId()).map(User::getDisplayName).orElse("-");
                case 7 -> i.isSlaBreached() ? "BREACH" : i.getEstimatedSlaHours() + "h";
                case 8 -> java.time.Duration.between(i.getCreatedAt(), java.time.LocalDateTime.now()).toHours() + "h";
                default -> "";
            };
        }
    }
}
