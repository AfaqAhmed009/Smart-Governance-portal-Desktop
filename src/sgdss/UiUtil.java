package sgdss;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import java.awt.*;

/**
 * UiUtil provides utility methods for creating consistent UI components
 * across the SGDSS application with theme support.
 */
public final class UiUtil {
    private UiUtil() {}

    public static Color bg() { return Theme.bg(); }
    public static Color panel() { return Theme.panel(); }
    public static Color accent() { return Theme.accent(); }
    public static Color text() { return Theme.text(); }
    public static Color textSecondary() { return Theme.textSecondary(); }

    public static void applyBase(JFrame frame) {
        frame.getContentPane().setBackground(bg());
    }

    public static JPanel card() {
        JPanel p = new JPanel();
        p.setBackground(Theme.cardBg());
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.border()),
                new EmptyBorder(12, 12, 12, 12)
        ));
        return p;
    }

    public static JButton primaryButton(String text) {
        JButton b = new JButton(text);
        b.setFocusPainted(false);
        b.setBackground(new Color(25, 80, 150));
        b.setForeground(Color.WHITE);
        b.setFont(new Font("SansSerif", Font.BOLD, 13));
        b.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setOpaque(true);
        b.setContentAreaFilled(true);
        b.setBorderPainted(false);
        return b;
    }

    public static JButton successButton(String text) {
        JButton b = new JButton(text);
        b.setFocusPainted(false);
        b.setBackground(new Color(30, 140, 70));
        b.setForeground(Color.WHITE);
        b.setFont(new Font("SansSerif", Font.BOLD, 13));
        b.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setOpaque(true);
        b.setContentAreaFilled(true);
        b.setBorderPainted(false);
        return b;
    }

    public static JButton secondaryButton(String text) {
        JButton b = new JButton(text);
        b.setFocusPainted(false);
        if (isDark()) {
            b.setBackground(new Color(70, 70, 95));
            b.setForeground(new Color(230, 230, 245));
        } else {
            b.setBackground(new Color(210, 220, 235));
            b.setForeground(new Color(30, 30, 50));
        }
        b.setFont(new Font("SansSerif", Font.BOLD, 13));
        b.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setOpaque(true);
        b.setContentAreaFilled(true);
        b.setBorderPainted(false);
        return b;
    }

    public static JButton dangerButton(String text) {
        JButton b = new JButton(text);
        b.setFocusPainted(false);
        b.setBackground(new Color(200, 50, 50));
        b.setForeground(Color.WHITE);
        b.setFont(new Font("SansSerif", Font.BOLD, 13));
        b.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setOpaque(true);
        b.setContentAreaFilled(true);
        b.setBorderPainted(false);
        return b;
    }

    public static JTextField field(int columns) {
        JTextField tf = new JTextField(columns);
        tf.setBackground(Theme.panel());
        tf.setForeground(Theme.text());
        tf.setCaretColor(Theme.text());
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.border()),
                new EmptyBorder(8, 10, 8, 10)
        ));
        tf.setFont(new Font("SansSerif", Font.PLAIN, 13));
        return tf;
    }

    public static JPasswordField passwordField(int columns) {
        JPasswordField pf = new JPasswordField(columns);
        pf.setBackground(Theme.panel());
        pf.setForeground(Theme.text());
        pf.setCaretColor(Theme.text());
        pf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.border()),
                new EmptyBorder(8, 10, 8, 10)
        ));
        pf.setFont(new Font("SansSerif", Font.PLAIN, 13));
        return pf;
    }

    public static JScrollPane wrap(Component c) {
        JScrollPane sp = new JScrollPane(c);
        sp.setBorder(BorderFactory.createLineBorder(Theme.border()));
        sp.getViewport().setBackground(Theme.panel());
        sp.setBackground(Theme.panel());
        return sp;
    }

    public static JLabel label(String text, int size, boolean bold) {
        JLabel l = new JLabel(text);
        l.setForeground(Theme.text());
        l.setFont(new Font("SansSerif", bold ? Font.BOLD : Font.PLAIN, size));
        return l;
    }

    public static JLabel badge(String text, Color bg, Color fg) {
        JLabel l = new JLabel("  " + text + "  ");
        l.setOpaque(true);
        l.setBackground(bg);
        l.setForeground(fg);
        l.setFont(new Font("SansSerif", Font.BOLD, 11));
        l.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        return l;
    }

    public static String safe(String s) { return s == null ? "" : s; }

    public static boolean isDark() { return Theme.isDark(); }

    public static void styleTable(JTable table) {
        table.setBackground(Theme.panel());
        table.setForeground(Theme.text());
        table.setGridColor(Theme.border());
        table.setSelectionBackground(new Color(25, 80, 150));
        table.setSelectionForeground(Color.WHITE);
        table.setFont(new Font("SansSerif", Font.PLAIN, 12));
        table.setRowHeight(28);

        // Custom header renderer so Windows L&F cannot override the colors
        Color headerBg = isDark() ? new Color(55, 55, 75) : new Color(220, 225, 240);
        Color headerFg = Theme.text();
        Font headerFont = new Font("SansSerif", Font.BOLD, 12);

        table.getTableHeader().setDefaultRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(
                    javax.swing.JTable t, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                java.awt.Component c = super.getTableCellRendererComponent(
                        t, value, isSelected, hasFocus, row, column);
                if (c instanceof JLabel label) {
                    label.setOpaque(true);
                    label.setBackground(headerBg);
                    label.setForeground(headerFg);
                    label.setFont(headerFont);
                    label.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createMatteBorder(0, 0, 1, 1, Theme.border()),
                            new EmptyBorder(6, 8, 6, 8)
                    ));
                    label.setHorizontalAlignment(SwingConstants.LEFT);
                }
                return c;
            }
        });

        table.getTableHeader().setBackground(headerBg);
        table.getTableHeader().setForeground(headerFg);
        table.getTableHeader().setFont(headerFont);
        table.getTableHeader().setReorderingAllowed(false);
    }

    public static void styleComboBox(JComboBox<?> combo) {
        combo.setBackground(Theme.panel());
        combo.setForeground(Theme.text());
        combo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        combo.setRenderer(new DarkComboRenderer());
        if (combo.getEditor() != null && combo.getEditor().getEditorComponent() instanceof JTextField tf) {
            tf.setBackground(Theme.panel());
            tf.setForeground(Theme.text());
            tf.setCaretColor(Theme.text());
        }
    }

    public static void styleTextArea(JTextArea area) {
        area.setBackground(Theme.panel());
        area.setForeground(Theme.text());
        area.setCaretColor(Theme.text());
        area.setFont(new Font("SansSerif", Font.PLAIN, 13));
        area.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
    }

    public static void styleSpinner(JSpinner spinner) {
        spinner.setBackground(Theme.panel());
        spinner.setForeground(Theme.text());
        JComponent editor = spinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor de) {
            de.getTextField().setBackground(Theme.panel());
            de.getTextField().setForeground(Theme.text());
            de.getTextField().setCaretColor(Theme.text());
            de.getTextField().setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Theme.border()),
                    new EmptyBorder(6, 8, 6, 8)
            ));
        }
    }

    public static void styleCheckBox(JCheckBox cb) {
        cb.setBackground(Theme.bg());
        cb.setForeground(Theme.text());
        cb.setOpaque(false);
        cb.setFont(new Font("SansSerif", Font.PLAIN, 13));
    }

    /**
     * Creates a JTabbedPane that properly respects dark mode.
     * The Windows L&F hardcodes tab colors, so we override updateUI
     * to re-apply our theme colors after the L&F sets its defaults.
     */
    public static JTabbedPane createTabbedPane() {
        return new JTabbedPane() {
            @Override
            public void updateUI() {
                super.updateUI();
                // After L&F installs its UI, force our colors
                setBackground(Theme.bg());
                setForeground(Theme.text());
                for (int i = 0; i < getTabCount(); i++) {
                    setBackgroundAt(i, Theme.panel());
                    setForegroundAt(i, Theme.text());
                }
            }
        };
    }

    /**
     * Forces theme colors on a container and all its children.
     * Call this after creating any complex panel.
     */
    public static void forceThemeColors(Container root) {
        for (Component c : root.getComponents()) {
            if (c instanceof JTable t) {
                styleTable(t);
            } else if (c instanceof JComboBox<?> cb) {
                styleComboBox(cb);
            } else if (c instanceof JSpinner s) {
                styleSpinner(s);
            } else if (c instanceof JTextArea ta) {
                styleTextArea(ta);
            } else if (c instanceof JTextField tf) {
                tf.setBackground(Theme.panel());
                tf.setForeground(Theme.text());
                tf.setCaretColor(Theme.text());
            } else if (c instanceof JCheckBox cb) {
                styleCheckBox(cb);
            } else if (c instanceof JLabel lbl && !lbl.isOpaque()) {
                lbl.setForeground(Theme.text());
            }
            if (c instanceof Container container) {
                forceThemeColors(container);
            }
        }
    }

    /**
     * Custom combo box renderer that respects dark mode for both
     * the selected item and dropdown list items.
     */
    static class DarkComboRenderer extends BasicComboBoxRenderer {
        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (c instanceof JLabel label) {
                label.setOpaque(true);
                if (isSelected) {
                    label.setBackground(Theme.accent());
                    label.setForeground(Color.WHITE);
                } else {
                    label.setBackground(Theme.panel());
                    label.setForeground(Theme.text());
                }
            }
            return c;
        }
    }
}