import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;

public class ViewExpenseFrame extends JFrame {

    JTable table;
    DefaultTableModel model;
    JLabel totalLabel;

    public ViewExpenseFrame() {

        setTitle("View Expenses");
        setSize(680, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(new Color(245, 247, 250));

        // HEADER
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(37, 99, 235));
        header.setBorder(new EmptyBorder(12, 20, 12, 20));

        JLabel hLbl = new JLabel("  Expense Records");
        hLbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        hLbl.setForeground(Color.WHITE);

        totalLabel = new JLabel("Total: calculating...");
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        totalLabel.setForeground(new Color(191, 219, 254));

        header.add(hLbl, BorderLayout.WEST);
        header.add(totalLabel, BorderLayout.EAST);

        // TABLE MODEL (UPDATED)
        model = new DefaultTableModel(
                new String[]{"ID", "No", "Amount (₹)", "Category", "Date", "Description"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(34);

        // 🔒 HIDE ID COLUMN
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setWidth(0);

        // COLUMN WIDTHS
        table.getColumnModel().getColumn(1).setMaxWidth(50); // No
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(110);
        table.getColumnModel().getColumn(4).setPreferredWidth(100);
        table.getColumnModel().getColumn(5).setPreferredWidth(260);

        // HEADER STYLE
        JTableHeader th = table.getTableHeader();
        th.setFont(new Font("Segoe UI", Font.BOLD, 12));
        th.setBackground(new Color(248, 250, 252));
        th.setForeground(new Color(71, 85, 105));
        th.setPreferredSize(new Dimension(0, 36));

        JScrollPane scroll = new JScrollPane(table);

        JPanel tableWrap = new JPanel(new BorderLayout());
        tableWrap.setBorder(new EmptyBorder(14, 14, 0, 14));
        tableWrap.add(scroll, BorderLayout.CENTER);

        // DELETE BUTTON
        JButton deleteBtn = new JButton("Delete Selected");
        deleteBtn.setBackground(new Color(220, 38, 38));
        deleteBtn.setForeground(Color.WHITE);

        JPanel bottomBar = new JPanel();
        bottomBar.add(deleteBtn);

        outer.add(header, BorderLayout.NORTH);
        outer.add(tableWrap, BorderLayout.CENTER);
        outer.add(bottomBar, BorderLayout.SOUTH);

        add(outer);

        loadData();

        deleteBtn.addActionListener(e -> deleteExpense());

        setVisible(true);
    }

    // LOAD DATA (FIXED)
    void loadData() {

        double total = 0;
        int count = 1;

        try {
            Connection con = DBConnection.getConnection();

            // CLEAR OLD DATA
            model.setRowCount(0);

            ResultSet rs = con.createStatement()
                    .executeQuery("SELECT * FROM expenses ORDER BY expense_date DESC");

            while (rs.next()) {

                double amt = rs.getDouble("amount");
                total += amt;

                model.addRow(new Object[]{
                        rs.getInt("expense_id"), // hidden
                        count++,                 // serial number
                        String.format("%.2f", amt),
                        rs.getString("category"),
                        rs.getDate("expense_date"),
                        rs.getString("description")
                });
            }

            totalLabel.setText(String.format("Total: ₹%.2f", total));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // DELETE (FIXED)
    void deleteExpense() {

        int row = table.getSelectedRow();

        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a row ❗");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete this expense?",
                "Confirm",
                JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            int id = Integer.parseInt(model.getValueAt(row, 0).toString());

            PreparedStatement ps = DBConnection.getConnection()
                    .prepareStatement("DELETE FROM expenses WHERE expense_id=?");

            ps.setInt(1, id);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Deleted ✅");

            // 🔥 REFRESH TABLE
            loadData();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}