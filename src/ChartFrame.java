import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.*;
import java.sql.*;
import java.util.*;

public class ChartFrame extends JFrame {

    Map<String, Integer> categoryData  = new LinkedHashMap<>();
    Map<String, Color>   categoryColors = new HashMap<>();

    // FlatLaf-friendly palette (vivid, accessible)
    private static final Color[] PALETTE = {
        new Color(59,  130, 246),   // blue
        new Color(16,  185, 129),   // emerald
        new Color(245, 158,  11),   // amber
        new Color(239,  68,  68),   // red
        new Color(139,  92, 246),   // violet
        new Color(236,  72, 153),   // pink
        new Color(20,  184, 166),   // teal
        new Color(251, 146,  60),   // orange
    };

    public ChartFrame() {

        setTitle("Expense Chart");
        setSize(780, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(new Color(245, 247, 250));

        // ── Header ───────────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(37, 99, 235));
        header.setBorder(new EmptyBorder(12, 20, 12, 20));
        JLabel hLbl = new JLabel("  Expense Breakdown — Pie Chart");
        hLbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        hLbl.setForeground(Color.WHITE);
        header.add(hLbl, BorderLayout.WEST);

        // ── Chart panel ──────────────────────────────────────────
        PiePanel piePanel = new PiePanel();
        piePanel.setBackground(Color.WHITE);
        piePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1, true),
            new EmptyBorder(20, 20, 20, 20)
        ));

        JPanel chartWrap = new JPanel(new BorderLayout());
        chartWrap.setBackground(new Color(245, 247, 250));
        chartWrap.setBorder(new EmptyBorder(14, 14, 14, 14));
        chartWrap.add(piePanel, BorderLayout.CENTER);

        outer.add(header,    BorderLayout.NORTH);
        outer.add(chartWrap, BorderLayout.CENTER);
        add(outer);

        // ── Load data async ───────────────────────────────────────
        new SwingWorker<Void, Void>() {
            protected Void doInBackground() { fetchData(); return null; }
            protected void done()           { piePanel.repaint(); setVisible(true); }
        }.execute();
    }

    // ── DB fetch ─────────────────────────────────────────────────
    void fetchData() {
        Map<String, Integer> temp = new LinkedHashMap<>();
        try (Connection con = DBConnection.getConnection();
             ResultSet rs = con.createStatement().executeQuery(
                 "SELECT category, SUM(amount) total FROM expenses GROUP BY category")) {

            int i = 0;
            while (rs.next()) {
                String cat = rs.getString("category");
                int    amt = (int) rs.getDouble("total");
                temp.put(cat, amt);
                if (!categoryColors.containsKey(cat))
                    categoryColors.put(cat, PALETTE[i++ % PALETTE.length]);
            }
            synchronized (categoryData) {
                categoryData.clear();
                categoryData.putAll(temp);
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    // ── Pie panel ─────────────────────────────────────────────────
    class PiePanel extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,   RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            Map<String, Integer> data;
            synchronized (categoryData) { data = new LinkedHashMap<>(categoryData); }

            int total = data.values().stream().mapToInt(i -> i).sum();

            if (total == 0) {
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 15));
                g2.setColor(new Color(156, 163, 175));
                String msg = "No data available";
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(msg,
                    (getWidth()  - fm.stringWidth(msg)) / 2,
                    (getHeight() + fm.getAscent())       / 2);
                return;
            }

            // ── Pie geometry ─────────────────────────────────────
            int pieSize = Math.min(getHeight() - 60, 300);
            int pieX    = 40;
            int pieY    = (getHeight() - pieSize) / 2;

            double startAngle = 0;

            for (Map.Entry<String, Integer> entry : data.entrySet()) {
                double sweep = 360.0 * entry.getValue() / total;

                g2.setColor(categoryColors.get(entry.getKey()));
                g2.fill(new Arc2D.Double(pieX, pieY, pieSize, pieSize,
                        startAngle, sweep, Arc2D.PIE));

                // thin white separator
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2f));
                g2.draw(new Arc2D.Double(pieX, pieY, pieSize, pieSize,
                        startAngle, sweep, Arc2D.PIE));

                startAngle += sweep;
            }

            // ── Legend ───────────────────────────────────────────
            int legendX = pieX + pieSize + 40;
            int legendY = pieY + 10;
            int maxLegendW = getWidth() - legendX - 16;

            g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
            FontMetrics fm = g2.getFontMetrics();

            // legend card background
            int legendH = data.size() * 34 + 16;
            g2.setColor(new Color(248, 250, 252));
            g2.fillRoundRect(legendX - 12, legendY - 12, maxLegendW, legendH, 10, 10);
            g2.setColor(new Color(226, 232, 240));
            g2.setStroke(new BasicStroke(1f));
            g2.drawRoundRect(legendX - 12, legendY - 12, maxLegendW, legendH, 10, 10);

            for (Map.Entry<String, Integer> entry : data.entrySet()) {
                String cat     = entry.getKey();
                int    val     = entry.getValue();
                int    percent = val * 100 / total;

                Color c = categoryColors.get(cat);

                // color swatch (rounded rect)
                g2.setColor(c);
                g2.fillRoundRect(legendX, legendY, 16, 16, 4, 4);

                // category name
                g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                g2.setColor(new Color(30, 41, 59));
                g2.drawString(cat, legendX + 24, legendY + 13);

                // right-aligned percent
                String pctStr = percent + "%";
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                g2.setColor(new Color(100, 116, 139));
                g2.drawString(pctStr,
                    legendX + maxLegendW - 30 - fm.stringWidth(pctStr),
                    legendY + 13);

                // amount below
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                g2.setColor(new Color(100, 116, 139));
                g2.drawString(String.format("₹%,d", val), legendX + 24, legendY + 28);

                legendY += 34;
            }

            // ── Centre total label ─────────────────────────────────
            g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
            g2.setColor(new Color(30, 41, 59));
            String totalStr = String.format("Total: ₹%,d", total);
            int tw = g2.getFontMetrics().stringWidth(totalStr);
            g2.drawString(totalStr, pieX + (pieSize - tw) / 2, pieY + pieSize + 24);
        }
    }
}