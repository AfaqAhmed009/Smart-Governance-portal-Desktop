package sgdss;

import javax.swing.*;
import java.awt.*;

/**
 * SettingsPanel allows Global Admins to configure system preferences.
 * Includes theme selection, notification settings, and SLA thresholds.
 */
public class SettingsPanel extends JPanel {
    private final AppContext ctx;
    private final User user;

    public SettingsPanel(AppContext ctx, User user) {
        this.ctx = ctx;
        this.user = user;
        setLayout(new BorderLayout(12, 12));
        setBackground(UiUtil.bg());

        JPanel p = UiUtil.card();
        p.setLayout(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 8, 8, 8);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.gridx = 0;
        g.gridy = 0;

        JLabel themeLabel = new JLabel("UI Theme:");
        themeLabel.setForeground(Theme.text());
        JComboBox<String> themeBox = new JComboBox<>(new String[]{"light", "dark"});
        themeBox.setSelectedItem(getSetting("theme", "light"));
        UiUtil.styleComboBox(themeBox);
        p.add(themeLabel, g);
        g.gridx = 1;
        p.add(themeBox, g);
        g.gridy++;
        g.gridx = 0;

        JLabel notifLabel = new JLabel("Notifications Enabled:");
        notifLabel.setForeground(Theme.text());
        JCheckBox notifBox = new JCheckBox("Enable system notifications",
            Boolean.parseBoolean(getSetting("notifications.enabled", "true")));
        UiUtil.styleCheckBox(notifBox);
        p.add(notifLabel, g);
        g.gridx = 1;
        p.add(notifBox, g);
        g.gridy++;
        g.gridx = 0;

        JLabel slaLabel = new JLabel("SLA Warning Threshold (hours):");
        slaLabel.setForeground(Theme.text());
        JTextField slaField = UiUtil.field(5);
        slaField.setText(getSetting("sla.warning.threshold", "6"));
        p.add(slaLabel, g);
        g.gridx = 1;
        p.add(slaField, g);
        g.gridy++;
        g.gridx = 0;

        JLabel dataLabel = new JLabel("Data Management:");
        dataLabel.setForeground(Theme.text());
        JButton backupBtn = UiUtil.secondaryButton("Create Backup");
        JButton restoreBtn = UiUtil.secondaryButton("Restore from Backup");
        p.add(dataLabel, g);
        g.gridx = 1;
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        btnPanel.setOpaque(false);
        btnPanel.add(backupBtn);
        btnPanel.add(restoreBtn);
        p.add(btnPanel, g);

        backupBtn.addActionListener(e -> {
            try {
                java.nio.file.Path backupPath = java.nio.file.Paths.get("data", "sgdss-backup-" +
                    java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss")) + ".dat");
                java.nio.file.Files.createDirectories(backupPath.getParent());
                java.nio.file.Files.copy(java.nio.file.Paths.get("data", "sgdss.dat"), backupPath);
                JOptionPane.showMessageDialog(this, "Backup created at: " + backupPath.toAbsolutePath().toString());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Backup failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        restoreBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser("data");
            chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Backup Files", "dat"));
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    java.nio.file.Files.copy(chooser.getSelectedFile().toPath(),
                        java.nio.file.Paths.get("data", "sgdss.dat"),
                        java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                    JOptionPane.showMessageDialog(this, "Restore successful! Please restart the application.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Restore failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton save = UiUtil.primaryButton("Save Settings");
        save.addActionListener(e -> {
            setSetting("theme", (String) themeBox.getSelectedItem());
            setSetting("notifications.enabled", String.valueOf(notifBox.isSelected()));
            setSetting("sla.warning.threshold", slaField.getText());
            ctx.store.save();
            JOptionPane.showMessageDialog(this, "Settings saved successfully!\nSome changes may require restart.");
        });

        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setOpaque(false);
        wrap.add(p, BorderLayout.NORTH);
        wrap.add(save, BorderLayout.SOUTH);

        add(UiUtil.wrap(wrap), BorderLayout.CENTER);
    }

    private String getSetting(String key, String defaultValue) {
        for (SystemSetting s : ctx.state.getSettings()) {
            if (s.getKey().equals(key)) return s.getValue();
        }
        return defaultValue;
    }

    private void setSetting(String key, String value) {
        for (SystemSetting s : ctx.state.getSettings()) {
            if (s.getKey().equals(key)) { s.setValue(value); return; }
        }
        ctx.state.getSettings().add(new SystemSetting(key, value, ""));
    }
}
