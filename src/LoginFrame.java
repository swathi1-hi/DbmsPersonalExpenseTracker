import com.formdev.flatlaf.FlatIntelliJLaf;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginFrame extends JFrame {

    JTextField userField;
    JPasswordField passField;

    public LoginFrame() {

        setTitle("Expense Tracker — Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(true);

        // ── Outer panel — fills full screen, centres the login card ─
        JPanel outer = new JPanel(new GridBagLayout());
        outer.setBackground(new Color(245, 247, 250));

        // ── Login card (fixed width, centred) ────────────────────
        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(420, 360));
        card.setBackground(new Color(245, 247, 250));

        // ── Header ───────────────────────────────────────────────
        JPanel header = new JPanel();
        header.setBackground(new Color(37, 99, 235));
        header.setBorder(new EmptyBorder(28, 0, 22, 0));
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

        JLabel icon = new JLabel("💰");
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("Expense Tracker");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        header.add(icon);
        header.add(Box.createVerticalStrut(8));
        header.add(title);

        // ── Form panel ───────────────────────────────────────────
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(new EmptyBorder(28, 36, 28, 36));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.weightx = 1.0;

        // Username
        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        userLabel.setForeground(new Color(55, 65, 81));
        gbc.gridx = 0; gbc.gridy = 0;
        form.add(userLabel, gbc);

        userField = new JTextField();
        userField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userField.setPreferredSize(new Dimension(0, 40));
        userField.putClientProperty("JTextField.placeholderText", "Enter username");
        gbc.gridy = 1;
        form.add(userField, gbc);

        // Password
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        passLabel.setForeground(new Color(55, 65, 81));
        gbc.gridy = 2; gbc.insets = new Insets(12, 0, 5, 0);
        form.add(passLabel, gbc);

        passField = new JPasswordField();
        passField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passField.setPreferredSize(new Dimension(0, 40));
        passField.putClientProperty("JTextField.placeholderText", "Enter password");
        gbc.gridy = 3; gbc.insets = new Insets(5, 0, 5, 0);
        form.add(passField, gbc);

        // Login button
        JButton loginBtn = new JButton("Login");
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginBtn.setBackground(new Color(37, 99, 235));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginBtn.setPreferredSize(new Dimension(0, 42));
        loginBtn.putClientProperty("JButton.buttonType", "roundRect");
        gbc.gridy = 4; gbc.insets = new Insets(20, 0, 0, 0);
        form.add(loginBtn, gbc);

        card.add(header, BorderLayout.NORTH);
        card.add(form, BorderLayout.CENTER);
        outer.add(card);

        // ── Action ───────────────────────────────────────────────
        loginBtn.addActionListener(e -> {
            if (userField.getText().equals("admin") &&
                new String(passField.getPassword()).equals("admin")) {
                new HomeFrame();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Invalid username or password.", "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
            }
        });               

        add(outer);

        getRootPane().setDefaultButton(loginBtn);
        setVisible(true);
    }

    public static void main(String[] args) {
        // ── Apply FlatLaf ─────────────────────────────────────────
        try {
            FlatIntelliJLaf.setup();
            UIManager.put("Button.arc", 8);
            UIManager.put("Component.arc", 8);
            UIManager.put("TextComponent.arc", 8);
            UIManager.put("Component.focusWidth", 1);
            UIManager.put("defaultFont", new Font("Segoe UI", Font.PLAIN, 13));
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(LoginFrame::new);
    }
}