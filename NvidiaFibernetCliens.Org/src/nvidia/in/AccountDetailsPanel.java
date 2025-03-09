package nvidia.in;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class AccountDetailsPanel extends JPanel {
    private JTable accountTable;
    private DefaultTableModel tableModel;
    private JButton refreshButton;

    public AccountDetailsPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] columnNames = {"Mobile Number", "Customer Name", "Plan Type", "Plan Price", "Account Number"};

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };

        accountTable = new JTable(tableModel);
        accountTable.setFont(new Font("Arial", Font.PLAIN, 16));
        accountTable.setRowHeight(30);
        accountTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 
        accountTable.setCellSelectionEnabled(true); 

        accountTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger() || e.getButton() == MouseEvent.BUTTON3) {
                    JTable source = (JTable) e.getSource();
                    int row = source.rowAtPoint(e.getPoint());
                    int column = source.columnAtPoint(e.getPoint());
                    if (row >= 0 && column >= 0) {
                        source.changeSelection(row, column, false, false);
                        String selectedData = source.getValueAt(row, column).toString();
                        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(selectedData), null);
                    }
                }
            }
        });

        JTableHeader header = accountTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 18));
        header.setBackground(new Color(50, 50, 50)); 
        header.setForeground(Color.black);

        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setBackground(new Color(230, 230, 230)); 
        cellRenderer.setForeground(Color.BLACK);
        cellRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        for (int i = 0; i < accountTable.getColumnCount(); i++) {
            accountTable.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(accountTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        refreshButton = new JButton("Refresh");
        
        
        
        refreshButton.setPreferredSize(new Dimension(150, 40));
        refreshButton.setMaximumSize(new Dimension(200, 50));  
        refreshButton.setFont(new Font("Arial", Font.BOLD, 14));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setBackground(new Color(50, 50, 50)); 
        refreshButton.setOpaque(true); 
        refreshButton.setBorderPainted(false); 
        refreshButton.setContentAreaFilled(true); 
        refreshButton.setFocusPainted(false);
        refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        refreshButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
            	refreshButton.setBackground(new Color(70, 70, 70)); 
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
            	refreshButton.setBackground(new Color(50, 50, 50)); 
            }
        });
        

        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.SOUTH);

        loadAccountData();

        refreshButton.addActionListener(e -> loadAccountData());
    }

    private void loadAccountData() {
        try {
            ConnectionJDBC con = new ConnectionJDBC();
            
            String query = "SELECT s.mobile AS mobileno, s.name AS username, " +
                           "b.planType, b.planPrice, b.accountNo " +
                           "FROM sign_in s " +
                           "JOIN broaddband_plans b ON s.mobile = b.mobilenumber";
            
            ResultSet rs = con.s.executeQuery(query);

            tableModel.setRowCount(0);

            while (rs.next()) {
                Object[] row = {
                    rs.getString("mobileno"),
                    rs.getString("username"),
                    rs.getString("planType"),
                    rs.getDouble("planPrice"),
                    rs.getInt("accountNo")
                };
                tableModel.addRow(row);
            }

            rs.close();
            con.s.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data!", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}