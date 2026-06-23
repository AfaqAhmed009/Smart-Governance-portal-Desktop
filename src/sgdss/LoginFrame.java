package sgdss;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Optional;

public class LoginFrame extends JFrame {
    private final AppContext ctx;
    private final JTextField username = UiUtil.field(20);
    private final JPasswordField password = UiUtil.passwordField(20);
    private int failedAttempts = 0;
    private JPanel right;
    private JLabel welcomeLabel;
    private JLabel signinLabel;
    private JLabel unameLabel;
    private JLabel passLabel;
    private JButton themeBtn;
    private JButton loginBtn;
    private JButton regBtn;
    private JButton resetBtn;

    public LoginFrame(AppContext ctx) {
        this.ctx = ctx;
        setTitle("SGDSS - Smart Governance & Decision Support System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1080, 680);
        setLocationRelativeTo(null);
        UiUtil.applyBase(this);
        setLayout(new BorderLayout());

        add(buildLeftPanel(), BorderLayout.WEST);
        buildRightPanel();
        add(right, BorderLayout.CENTER);

        loginBtn.addActionListener(e -> doLogin());
        regBtn.addActionListener(e -> showRegister());
        resetBtn.addActionListener(e -> showReset());
        themeBtn.addActionListener(e -> toggleTheme());
        getRootPane().setDefaultButton(loginBtn);
    }

    private JPanel buildLeftPanel() {
        JPanel left = new JPanel(new BorderLayout());
        left.setPreferredSize(new Dimension(460, 0));
        left.setBackground(UiUtil.accent());

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        content.setBorder(BorderFactory.createEmptyBorder(200, 50, 0, 40));

        JLabel titleLabel = new JLabel("SGDSS");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 52));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextArea subtitle = new JTextArea("Smart Governance &\nDecision Support System");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 20));
        subtitle.setForeground(new Color(234, 242, 255));
        subtitle.setBackground(UiUtil.accent());
        subtitle.setEditable(false);
        subtitle.setFocusable(false);
        subtitle.setLineWrap(false);
        subtitle.setBorder(null);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextArea tagline = new JTextArea("Empowering cities through\nintelligent civic management");
        tagline.setFont(new Font("SansSerif", Font.ITALIC, 14));
        tagline.setForeground(new Color(176, 196, 224));
        tagline.setBackground(UiUtil.accent());
        tagline.setEditable(false);
        tagline.setFocusable(false);
        tagline.setLineWrap(false);
        tagline.setBorder(null);
        tagline.setAlignmentX(Component.LEFT_ALIGNMENT);

        content.add(titleLabel);
        content.add(Box.createVerticalStrut(16));
        content.add(subtitle);
        content.add(Box.createVerticalStrut(12));
        content.add(tagline);
        content.add(Box.createVerticalGlue());

        left.add(content, BorderLayout.CENTER);
        return left;
    }

    private void buildRightPanel() {
        right = new JPanel(new GridBagLayout());
        right.setBackground(UiUtil.bg());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 8, 8, 8);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.gridx = 0;
        g.gridy = 0;
        g.gridwidth = 2;

        welcomeLabel = UiUtil.label("Welcome Back", 30, true);
        right.add(welcomeLabel, g);
        g.gridy++;
        signinLabel = new JLabel("Sign in to access the SGDSS Control Center");
        signinLabel.setForeground(Theme.textSecondary());
        right.add(signinLabel, g);
        g.gridwidth = 1;
        g.gridy++;

        g.gridx = 0;
        g.gridwidth = 2;
        unameLabel = new JLabel("Username");
        unameLabel.setForeground(Theme.text());
        right.add(unameLabel, g);
        g.gridy++;
        right.add(username, g);
        g.gridy++;
        passLabel = new JLabel("Password");
        passLabel.setForeground(Theme.text());
        right.add(passLabel, g);
        g.gridy++;
        right.add(password, g);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        btns.setOpaque(false);
        loginBtn = UiUtil.primaryButton("Sign In");
        regBtn = UiUtil.secondaryButton("Register");
        resetBtn = UiUtil.secondaryButton("Forgot Password?");
        themeBtn = UiUtil.secondaryButton(Theme.isDark() ? "Light Mode" : "Dark Mode");
        btns.add(loginBtn);
        btns.add(regBtn);
        btns.add(resetBtn);
        btns.add(themeBtn);
        g.gridy++;
        right.add(btns, g);
    }

    private void toggleTheme() {
        Theme.setMode(Theme.isDark() ? Theme.Mode.LIGHT : Theme.Mode.DARK);
        Theme.configureUIManager();
        applyThemeColors();
    }

    private void applyThemeColors() {
        right.setBackground(UiUtil.bg());
        welcomeLabel.setForeground(Theme.text());
        signinLabel.setForeground(Theme.textSecondary());
        unameLabel.setForeground(Theme.text());
        passLabel.setForeground(Theme.text());

        username.setBackground(Theme.panel());
        username.setForeground(Theme.text());
        username.setCaretColor(Theme.text());
        username.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.border()),
                new EmptyBorder(8, 10, 8, 10)
        ));

        password.setBackground(Theme.panel());
        password.setForeground(Theme.text());
        password.setCaretColor(Theme.text());
        password.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.border()),
                new EmptyBorder(8, 10, 8, 10)
        ));

        themeBtn.setText(Theme.isDark() ? "Light Mode" : "Dark Mode");
        if (Theme.isDark()) {
            regBtn.setBackground(new Color(70, 70, 95));
            regBtn.setForeground(new Color(230, 230, 245));
            resetBtn.setBackground(new Color(70, 70, 95));
            resetBtn.setForeground(new Color(230, 230, 245));
            themeBtn.setBackground(new Color(70, 70, 95));
            themeBtn.setForeground(new Color(230, 230, 245));
        } else {
            regBtn.setBackground(new Color(210, 220, 235));
            regBtn.setForeground(new Color(30, 30, 50));
            resetBtn.setBackground(new Color(210, 220, 235));
            resetBtn.setForeground(new Color(30, 30, 50));
            themeBtn.setBackground(new Color(210, 220, 235));
            themeBtn.setForeground(new Color(30, 30, 50));
        }

        right.revalidate();
        right.repaint();
    }

    private void doLogin() {
        String u = username.getText().trim();
        char[] p = password.getPassword();
        if (u.isEmpty() || p.length == 0) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password.", "Login Error", JOptionPane.WARNING_MESSAGE);
            java.util.Arrays.fill(p, '\0');
            return;
        }
        Optional<User> user = ctx.authService.login(u, p);
        java.util.Arrays.fill(p, '\0');
        if (user.isPresent()) {
            failedAttempts = 0;
            dispose();
            new MainFrame(ctx, user.get()).setVisible(true);
        } else {
            failedAttempts++;
            JOptionPane.showMessageDialog(this,
                    "Login failed. Please check your credentials.\nAttempts: " + failedAttempts + "/5\nTip: Use demo credentials (admin / Admin@123)",
                    "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showRegister() {
        JTextField uname = UiUtil.field(18);
        JTextField dname = UiUtil.field(18);
        JTextField email = UiUtil.field(18);
        JTextField phone = UiUtil.field(18);
        JPasswordField pass = UiUtil.passwordField(18);
        JTextField q = UiUtil.field(18);
        JTextField a = UiUtil.field(18);

        JComboBox<City> city = new JComboBox<>(ctx.state.getCities().toArray(new City[0]));
        JComboBox<Department> dept = new JComboBox<>(ctx.state.getDepartments().toArray(new Department[0]));
        UiUtil.styleComboBox(city);
        UiUtil.styleComboBox(dept);

        Object[] fields = {
                "Username", uname,
                "Display name", dname,
                "Email", email,
                "Phone", phone,
                "Password (min 8 chars, upper/lower/digit)", pass,
                "Security question", q,
                "Security answer", a,
                "City", city,
                "Department", dept
        };
        int r = JOptionPane.showConfirmDialog(this, fields, "Citizen Registration", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (r == JOptionPane.OK_OPTION) {
            try {
                City c = (City) city.getSelectedItem();
                Department d = (Department) dept.getSelectedItem();
                ctx.authService.registerCitizen(uname.getText().trim(), dname.getText().trim(),
                        email.getText().trim(), phone.getText().trim(),
                        new String(pass.getPassword()), q.getText().trim(), a.getText().trim(),
                        c == null ? null : c.getId(), d == null ? null : d.getId());
                JOptionPane.showMessageDialog(this, "Registration successful! You can now login with your new account.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Registration Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showReset() {
        JTextField uname = UiUtil.field(18);
        JTextField answer = UiUtil.field(18);
        JPasswordField np = UiUtil.passwordField(18);
        Object[] fields = {"Username", uname, "Security answer", answer, "New password (min 8 chars)", np};
        int r = JOptionPane.showConfirmDialog(this, fields, "Reset Password", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (r == JOptionPane.OK_OPTION) {
            boolean ok = ctx.authService.resetPassword(uname.getText().trim(), answer.getText().trim(), new String(np.getPassword()));
            JOptionPane.showMessageDialog(this, ok ? "Password reset successful!" : "Reset failed. Check your username and security answer.",
                    "Reset", ok ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
        }
    }
}