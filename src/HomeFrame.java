import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class HomeFrame extends JFrame {

    public HomeFrame() {

        setTitle("Expense Tracker — Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(true);

        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(new Color(245, 247, 250));

        // ── Top header bar ───────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(37, 99, 235));
        header.setBorder(new EmptyBorder(14, 20, 14, 20));

        JLabel titleLbl = new JLabel("  Dashboard");
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLbl.setForeground(Color.WHITE);

        JLabel dateLbl = new JLabel(java.time.LocalDate.now().toString());
        dateLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dateLbl.setForeground(new Color(191, 219, 254));

        header.add(titleLbl, BorderLayout.WEST);
        header.add(dateLbl, BorderLayout.EAST);

        // ── Cards row — centred with max width ────────────────────
        JPanel cardsOuter = new JPanel(new GridBagLayout());
        cardsOuter.setBackground(new Color(245, 247, 250));
        cardsOuter.setBorder(new EmptyBorder(40, 0, 20, 0));

        JPanel cards = new JPanel(new GridLayout(1, 2, 24, 0));
        cards.setBackground(new Color(245, 247, 250));
        cards.setPreferredSize(new Dimension(700, 220));

        cards.add(makeCard("➕", "Add Expense", "Record a new transaction",
                new Color(37, 99, 235), e -> new AddExpenseFrame()));
        cards.add(makeCard("📋", "View Expenses", "Browse & manage records",
                new Color(16, 185, 129), e -> new ViewExpenseFrame()));

        cardsOuter.add(cards);

        // ── Bottom button row ─────────────────────────────────────
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        bottom.setBackground(new Color(245, 247, 250));
        bottom.setBorder(new EmptyBorder(0, 24, 40, 24));

        JButton chartBtn = makeTextButton("View Chart", new Color(139, 92, 246));
        chartBtn.setPreferredSize(new Dimension(260, 44));
        chartBtn.addActionListener(e -> new ChartFrame());
        bottom.add(chartBtn);

        outer.add(header, BorderLayout.NORTH);
        outer.add(cardsOuter, BorderLayout.CENTER);
        outer.add(bottom, BorderLayout.SOUTH);
        add(outer);

        setVisible(true);
    }

    // ── Clickable card ───────────────────────────────────────────
    private JPanel makeCard(String iconText, String title, String subtitle,
                             Color accent, java.awt.event.ActionListener action) {
        JPanel card = new JPanel(new BorderLayout(0, 6));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1, true),
            new EmptyBorder(18, 18, 18, 18)
        ));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // top: icon circle + title
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        top.setOpaque(false);

        JLabel iconCircle = new JLabel(iconText);
        iconCircle.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        iconCircle.setOpaque(true);
        iconCircle.setBackground(accent);
        iconCircle.setForeground(Color.WHITE);
        iconCircle.setBorder(new EmptyBorder(6, 8, 6, 8));
        // rounded via putClientProperty
        iconCircle.putClientProperty("arc", 10);

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLbl.setForeground(new Color(17, 24, 39));

        top.add(iconCircle);
        top.add(titleLbl);

        JLabel subLbl = new JLabel(subtitle);
        subLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subLbl.setForeground(new Color(107, 114, 128));

        JButton btn = makeTextButton("Open →", accent);
        btn.addActionListener(action);

        card.add(top, BorderLayout.NORTH);
        card.add(subLbl, BorderLayout.CENTER);
        card.add(btn, BorderLayout.SOUTH);

        return card;
    }

    private JButton makeTextButton(String text, Color accent) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(accent);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.putClientProperty("JButton.buttonType", "roundRect");
        return btn;
    }
}