package nvidia.in;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class ServiceRequestsPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JButton refreshButton;
    private ConnectionJDBC con;

    public ServiceRequestsPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        con = new ConnectionJDBC();

        String[] columnNames = {"Request ID", "User", "Type", "Status", "Request Date"};
        model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; 
            }
        };

        table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 16));
        table.setRowHeight(30);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        table.addMouseListener(new MouseAdapter() {
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

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 18));
        header.setBackground(new Color(50, 50, 50)); 
        header.setForeground(Color.black);

        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setBackground(new Color(230, 230, 230));
        cellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }

        String[] statuses = {"Pending", "In Progress", "Resolved", "Completed"};
        JComboBox<String> statusComboBox = new JComboBox<>(statuses);
        table.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(statusComboBox));

        JScrollPane scrollPane = new JScrollPane(table);
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
        
        
        
        
        refreshButton.addActionListener(e -> loadData());
        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.SOUTH);

        table.getModel().addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 3) {
                updateStatus(e.getFirstRow());
            }
        });

        loadData();
    }

    private void loadData() {
        try {
            model.setRowCount(0); 
            String query = "SELECT request_id, user, type, status, request_date FROM vice_request";
            ResultSet rs = con.s.executeQuery(query);
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("request_id"),
                    rs.getString("user"),
                    rs.getString("type"),
                    rs.getString("status"),
                    rs.getDate("request_date")
                });
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data!", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateStatus(int row) {
        try {
            int requestId = (int) table.getValueAt(row, 0);
            String newStatus = (String) table.getValueAt(row, 3);

            String updateQuery = "UPDATE vice_request SET status = ? WHERE request_id = ?";
            PreparedStatement pst = con.c.prepareStatement(updateQuery);
            pst.setString(1, newStatus);
            pst.setInt(2, requestId);
            pst.executeUpdate();
            pst.close();

            JOptionPane.showMessageDialog(this, "Status updated successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating status!", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
