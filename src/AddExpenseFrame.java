import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.*;

public class AddExpenseFrame extends JFrame {

    JTextField amountField, categoryField, dateField, descField;

    // Category quick-pick options
    private static final String[] CATEGORIES = {
        "Food", "Transport", "Shopping", "Utilities",
        "Health", "Entertainment", "Education", "Other"
    };

    public AddExpenseFrame() {

        setTitle("Add Expense");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(true);

        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(new Color(245, 247, 250));

        // ── Header ───────────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(37, 99, 235));
        header.setBorder(new EmptyBorder(14, 20, 14, 20));
        JLabel hLbl = new JLabel("  Add New Expense");
        hLbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        hLbl.setForeground(Color.WHITE);
        header.add(hLbl, BorderLayout.WEST);

        // ── Form card ────────────────────────────────────────────
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1, true),
            new EmptyBorder(20, 24, 20, 24)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(4, 0, 4, 0);

        // Amount
        addLabel(card, gbc, "Amount (₹)", 0);
        amountField = styledField("e.g. 450.00");
        addField(card, gbc, amountField, 1);

        // Category — editable combo: user can type OR pick from dropdown
        addLabel(card, gbc, "Category", 2);
        JComboBox<String> categoryCombo = new JComboBox<>(CATEGORIES);
        categoryCombo.setEditable(true);                          // enables free typing
        categoryCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        categoryCombo.setPreferredSize(new Dimension(0, 36));
        // style the inner editor field to match other inputs
        Component editor = categoryCombo.getEditor().getEditorComponent();
        editor.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        if (editor instanceof JTextField) {
            ((JTextField) editor).putClientProperty(
                "JTextField.placeholderText", "Type or select category");
        }
        gbc.gridy = 3; card.add(categoryCombo, gbc);
        // categoryField stays in sync whether user typed or picked
        categoryField = new JTextField();
        categoryCombo.addActionListener(e -> {
            Object sel = categoryCombo.getSelectedItem();
            if (sel != null) categoryField.setText(sel.toString().trim());
        });
        categoryField.setText(CATEGORIES[0]);

        // Date
        addLabel(card, gbc, "Date (YYYY-MM-DD)", 4);
        dateField = styledField("e.g. 2026-04-04");
        addField(card, gbc, dateField, 5);

        // Description
        addLabel(card, gbc, "Description", 6);
        descField = styledField("Optional note");
        addField(card, gbc, descField, 7);

        // ── Button row ───────────────────────────────────────────
        JPanel btnRow = new JPanel(new GridLayout(1, 3, 10, 0));
        btnRow.setOpaque(false);
        btnRow.setBorder(new EmptyBorder(16, 0, 0, 0));

        JButton saveBtn  = accentBtnWithIcon("Save",  new Color(37, 99, 235),  saveSvgIcon());
        JButton viewBtn  = accentBtnWithIcon("View",  new Color(16, 185, 129), viewSvgIcon());
        JButton chartBtn = accentBtnWithIcon("Chart", new Color(139, 92, 246), chartSvgIcon());

        btnRow.add(saveBtn);
        btnRow.add(viewBtn);
        btnRow.add(chartBtn);

        gbc.gridy = 8; gbc.insets = new Insets(0, 0, 0, 0);
        card.add(btnRow, gbc);

        // ── Wrap card centred on full screen ─────────────────────
        card.setPreferredSize(new Dimension(500, 420));
        JPanel centreWrap = new JPanel(new GridBagLayout());
        centreWrap.setBackground(new Color(245, 247, 250));
        centreWrap.add(card);

        outer.add(header, BorderLayout.NORTH);
        outer.add(centreWrap, BorderLayout.CENTER);
        add(outer);

        // ── Actions ──────────────────────────────────────────────
        saveBtn.addActionListener(e -> saveExpense());
        viewBtn.addActionListener(e -> new ViewExpenseFrame());
        chartBtn.addActionListener(e -> new ChartFrame());

        getRootPane().setDefaultButton(saveBtn);
        setVisible(true);
    }

    // ── Helpers ──────────────────────────────────────────────────
    private void addLabel(JPanel p, GridBagConstraints g, String text, int row) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(new Color(55, 65, 81));
        g.gridy = row; g.insets = new Insets(row == 0 ? 0 : 8, 0, 2, 0);
        p.add(lbl, g);
    }

    private void addField(JPanel p, GridBagConstraints g, JComponent f, int row) {
        g.gridy = row; g.insets = new Insets(0, 0, 4, 0);
        p.add(f, g);
    }

    private JTextField styledField(String placeholder) {
        JTextField f = new JTextField();
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.setPreferredSize(new Dimension(0, 36));
        f.putClientProperty("JTextField.placeholderText", placeholder);
        return f;
    }

    // ── Button with SVG icon ──────────────────────────────────────
    private JButton accentBtnWithIcon(String text, Color bg, Icon icon) {
        JButton b = new JButton(text, icon);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setPreferredSize(new Dimension(0, 36));
        b.putClientProperty("JButton.buttonType", "roundRect");
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setIconTextGap(5);
        b.setHorizontalTextPosition(SwingConstants.RIGHT);
        return b;
    }

    // ── Inline SVG-style icons drawn via BufferedImage ────────────
    private Icon saveSvgIcon() {
        return new javax.swing.Icon() {
            public int getIconWidth()  { return 14; }
            public int getIconHeight() { return 14; }
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.setStroke(new java.awt.BasicStroke(1.5f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND));
                // floppy disk outline
                g2.drawRoundRect(x+1, y+1, 12, 12, 2, 2);
                // top slot
                g2.drawRect(x+3, y+1, 6, 4);
                // bottom box
                g2.drawRect(x+2, y+7, 10, 5);
                g2.dispose();
            }
        };
    }

    private Icon viewSvgIcon() {
        return new javax.swing.Icon() {
            public int getIconWidth()  { return 14; }
            public int getIconHeight() { return 14; }
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.setStroke(new java.awt.BasicStroke(1.5f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND));
                // doc outline
                g2.drawRoundRect(x+1, y+1, 12, 12, 2, 2);
                // lines
                g2.drawLine(x+4, y+5, x+10, y+5);
                g2.drawLine(x+4, y+8, x+10, y+8);
                g2.drawLine(x+4, y+11, x+8,  y+11);
                g2.dispose();
            }
        };
    }

    private Icon chartSvgIcon() {
        return new javax.swing.Icon() {
            public int getIconWidth()  { return 14; }
            public int getIconHeight() { return 14; }
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.setStroke(new java.awt.BasicStroke(1.5f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND));
                // pie circle
                g2.drawOval(x+1, y+1, 12, 12);
                // slice lines from centre
                g2.drawLine(x+7, y+7, x+7,  y+1);
                g2.drawLine(x+7, y+7, x+13, y+7);
                g2.dispose();
            }
        };
    }

    // ── Save logic (unchanged) ────────────────────────────────────
    void saveExpense() {
        try {
            // categoryField is synced from combo (typed or selected)
            String category = categoryField.getText().trim();
            if (category.isEmpty()) category = "Other";

            Connection con = DBConnection.getConnection();
            String sql = "INSERT INTO expenses(amount, category, expense_date, description) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setDouble(1, Double.parseDouble(amountField.getText().trim()));
            ps.setString(2, category);
            ps.setDate(3, java.sql.Date.valueOf(dateField.getText().trim()));
            ps.setString(4, descField.getText().trim());
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this,
                "Expense saved successfully!", "Saved",
                JOptionPane.INFORMATION_MESSAGE);

            amountField.setText("");
            dateField.setText("");
            descField.setText("");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error saving expense.\nCheck your inputs and try again.",
                "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}