package sgdss;

import java.awt.Color;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;

/**
 * Theme provides color schemes for light and dark modes.
 * All UI components use these colors for consistent theming.
 */
public class Theme {

    public enum Mode { LIGHT, DARK }

    private static Mode currentMode = Mode.LIGHT;

    public static void setMode(Mode mode) { currentMode = mode; }
    public static Mode getMode() { return currentMode; }
    public static boolean isDark() { return currentMode == Mode.DARK; }

    public static Color bg() {
        return isDark() ? new Color(30, 30, 40) : new Color(245, 248, 252);
    }

    public static Color panel() {
        return isDark() ? new Color(45, 45, 60) : Color.WHITE;
    }

    public static Color accent() {
        return new Color(33, 90, 160);
    }

    public static Color text() {
        return isDark() ? new Color(230, 230, 240) : new Color(40, 40, 50);
    }

    public static Color textSecondary() {
        return isDark() ? new Color(160, 160, 180) : new Color(90, 100, 120);
    }

    public static Color border() {
        return isDark() ? new Color(70, 70, 90) : new Color(210, 210, 210);
    }

    public static Color cardBg() {
        return isDark() ? new Color(50, 50, 70) : Color.WHITE;
    }

    public static Color chartBg() {
        return isDark() ? new Color(40, 40, 55) : Color.WHITE;
    }

    public static Color success() { return new Color(40, 160, 80); }
    public static Color warning() { return new Color(240, 160, 40); }
    public static Color danger() { return new Color(220, 60, 60); }
    public static Color info() { return new Color(60, 140, 220); }

    /**
     * Configures UIManager defaults BEFORE any Swing components are created.
     * This must be called at application startup and after theme toggle.
     */
    public static void configureUIManager() {
        boolean dark = isDark();
        Color bg = bg();
        Color fg = text();
        Color panel = panel();
        Color border = border();
        Color accent = accent();

        // Use ColorUIResource so L&F doesn't override
        UIManager.put("Panel.background", new ColorUIResource(panel));
        UIManager.put("Panel.foreground", new ColorUIResource(fg));

        UIManager.put("OptionPane.background", new ColorUIResource(bg));
        UIManager.put("OptionPane.messageForeground", new ColorUIResource(fg));

        // TabbedPane - critical for dark mode tabs
        UIManager.put("TabbedPane.background", new ColorUIResource(bg));
        UIManager.put("TabbedPane.foreground", new ColorUIResource(fg));
        UIManager.put("TabbedPane.selected", new ColorUIResource(panel));
        UIManager.put("TabbedPane.contentAreaColor", new ColorUIResource(bg));
        UIManager.put("TabbedPane.highlight", new ColorUIResource(border));
        UIManager.put("TabbedPane.shadow", new ColorUIResource(border));
        UIManager.put("TabbedPane.darkShadow", new ColorUIResource(border));
        UIManager.put("TabbedPane.light", new ColorUIResource(panel));
        UIManager.put("TabbedPane.focus", new ColorUIResource(panel));
        UIManager.put("TabbedPane.unselectedBackground", new ColorUIResource(bg));
        UIManager.put("TabbedPane.borderColor", new ColorUIResource(border));
        UIManager.put("TabbedPane.tabAreaBackground", new ColorUIResource(bg));
        UIManager.put("TabbedPane.selectedForeground", new ColorUIResource(fg));
        UIManager.put("TabbedPane.font", new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 13));

        // ComboBox - critical for dropdown visibility
        UIManager.put("ComboBox.background", new ColorUIResource(panel));
        UIManager.put("ComboBox.foreground", new ColorUIResource(fg));
        UIManager.put("ComboBox.selectionBackground", new ColorUIResource(accent));
        UIManager.put("ComboBox.selectionForeground", new ColorUIResource(Color.WHITE));
        UIManager.put("ComboBox.buttonBackground", new ColorUIResource(panel));
        UIManager.put("ComboBox.buttonDarkShadow", new ColorUIResource(border));
        UIManager.put("ComboBox.buttonHighlight", new ColorUIResource(panel));
        UIManager.put("ComboBox.buttonShadow", new ColorUIResource(border));

        // Text components
        UIManager.put("TextField.background", new ColorUIResource(panel));
        UIManager.put("TextField.foreground", new ColorUIResource(fg));
        UIManager.put("TextField.caretForeground", new ColorUIResource(fg));
        UIManager.put("TextField.inactiveBackground", new ColorUIResource(panel));
        UIManager.put("TextField.inactiveForeground", new ColorUIResource(textSecondary()));

        UIManager.put("TextArea.background", new ColorUIResource(panel));
        UIManager.put("TextArea.foreground", new ColorUIResource(fg));
        UIManager.put("TextArea.caretForeground", new ColorUIResource(fg));

        UIManager.put("PasswordField.background", new ColorUIResource(panel));
        UIManager.put("PasswordField.foreground", new ColorUIResource(fg));
        UIManager.put("PasswordField.caretForeground", new ColorUIResource(fg));

        UIManager.put("FormattedTextField.background", new ColorUIResource(panel));
        UIManager.put("FormattedTextField.foreground", new ColorUIResource(fg));
        UIManager.put("FormattedTextField.caretForeground", new ColorUIResource(fg));

        // Spinner
        UIManager.put("Spinner.background", new ColorUIResource(panel));
        UIManager.put("Spinner.foreground", new ColorUIResource(fg));

        // Table - critical for header visibility
        UIManager.put("Table.background", new ColorUIResource(panel));
        UIManager.put("Table.foreground", new ColorUIResource(fg));
        UIManager.put("Table.selectionBackground", new ColorUIResource(accent));
        UIManager.put("Table.selectionForeground", new ColorUIResource(Color.WHITE));
        UIManager.put("Table.gridColor", new ColorUIResource(border));
        UIManager.put("TableHeader.background", new ColorUIResource(dark ? new Color(55, 55, 75) : new Color(220, 225, 240)));
        UIManager.put("TableHeader.foreground", new ColorUIResource(fg));
        UIManager.put("TableHeader.font", new java.awt.Font("SansSerif", java.awt.Font.BOLD, 12));

        // List (used by combo dropdowns)
        UIManager.put("List.background", new ColorUIResource(panel));
        UIManager.put("List.foreground", new ColorUIResource(fg));
        UIManager.put("List.selectionBackground", new ColorUIResource(accent));
        UIManager.put("List.selectionForeground", new ColorUIResource(Color.WHITE));

        // ScrollPane/ScrollBar
        UIManager.put("ScrollPane.background", new ColorUIResource(bg));
        UIManager.put("ScrollBar.background", new ColorUIResource(panel));
        UIManager.put("ScrollBar.track", new ColorUIResource(panel));
        UIManager.put("ScrollBar.thumb", new ColorUIResource(dark ? new Color(80, 80, 100) : new Color(200, 200, 210)));

        // CheckBox/RadioButton
        UIManager.put("CheckBox.background", new ColorUIResource(bg));
        UIManager.put("CheckBox.foreground", new ColorUIResource(fg));
        UIManager.put("RadioButton.background", new ColorUIResource(bg));
        UIManager.put("RadioButton.foreground", new ColorUIResource(fg));

        // Label
        UIManager.put("Label.foreground", new ColorUIResource(fg));
        UIManager.put("Label.background", new ColorUIResource(bg));

        // ToolTip
        UIManager.put("ToolTip.background", new ColorUIResource(dark ? new Color(60, 60, 80) : new Color(255, 255, 230)));
        UIManager.put("ToolTip.foreground", new ColorUIResource(fg));

        // Menu
        UIManager.put("Menu.background", new ColorUIResource(panel));
        UIManager.put("Menu.foreground", new ColorUIResource(fg));
        UIManager.put("Menu.selectionBackground", new ColorUIResource(accent));
        UIManager.put("Menu.selectionForeground", new ColorUIResource(Color.WHITE));
        UIManager.put("MenuItem.background", new ColorUIResource(panel));
        UIManager.put("MenuItem.foreground", new ColorUIResource(fg));
        UIManager.put("MenuItem.selectionBackground", new ColorUIResource(accent));
        UIManager.put("MenuItem.selectionForeground", new ColorUIResource(Color.WHITE));
        UIManager.put("MenuBar.background", new ColorUIResource(panel));
        UIManager.put("MenuBar.foreground", new ColorUIResource(fg));

        UIManager.put("PopupMenu.background", new ColorUIResource(panel));
        UIManager.put("PopupMenu.foreground", new ColorUIResource(fg));

        // Viewport
        UIManager.put("Viewport.background", new ColorUIResource(panel));
        UIManager.put("Viewport.foreground", new ColorUIResource(fg));
    }
}
