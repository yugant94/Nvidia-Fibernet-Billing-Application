package nvidia.in;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AdminProfilePanel extends JPanel {
    private JTextField adminIdField, nameField, emailField, roleField;
    private JButton updateButton;
    ConnectionJDBC con = new ConnectionJDBC();
	public static int adminID;

	public AdminProfilePanel() {
	    setLayout(new BorderLayout());
	    setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30)); 

	    JPanel contentPanel = new JPanel();
	    contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
	    contentPanel.setOpaque(false);
	    contentPanel.add(Box.createVerticalStrut(80)); 

	    JLabel title = new JLabel("Admin Profile");
	    title.setFont(new Font("Arial", Font.BOLD, 28)); 
	    title.setForeground(Color.BLACK);
	    title.setAlignmentX(Component.CENTER_ALIGNMENT);
	    contentPanel.add(title);
	    contentPanel.add(Box.createRigidArea(new Dimension(0, 25)));

	    JPanel formWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
//	    formWrapper.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
	    formWrapper.setOpaque(false);
	    formWrapper.setPreferredSize(new Dimension(500, 100)); 

	    JPanel formPanel = new JPanel();
//	    formPanel.setBorder(BorderFactory.createLineBorder(Color.green, 3));
	    formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
	    formPanel.setOpaque(false);
	    formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

	    addFormRow("Admin ID:", adminIdField = createTextField(), formPanel);
	    formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
	    addFormRow("Name:", nameField = createTextField(), formPanel);
	    formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
	    addFormRow("Email:", emailField = createTextField(), formPanel);
	    formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
	    addFormRow("Role:", roleField = createTextField(), formPanel);

	    formWrapper.add(formPanel);
	    contentPanel.add(formWrapper);
	    contentPanel.add(Box.createRigidArea(new Dimension(0, 25)));

	    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 10));
//	    buttonPanel.setBorder(BorderFactory.createLineBorder(Color.blue, 3));
	    buttonPanel.setOpaque(false);

	    updateButton = createButton("Update Profile");
	    updateButton.setPreferredSize(new Dimension(200, 50)); 
	    updateButton.setMaximumSize(new Dimension(200, 50));  
	    updateButton.setFont(new Font("Arial", Font.BOLD, 14));
	    updateButton.setForeground(Color.WHITE);
	    updateButton.setBackground(new Color(50, 50, 50)); 
	    updateButton.setOpaque(true); 
	    updateButton.setBorderPainted(false); 
	    updateButton.setContentAreaFilled(true); 
	    updateButton.setFocusPainted(false);
	    updateButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

	    updateButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
            	updateButton.setBackground(new Color(70, 70, 70)); 
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
            	updateButton.setBackground(new Color(50, 50, 50)); 
            }
        });
	    
	    buttonPanel.add(updateButton);

	    contentPanel.add(buttonPanel);
	    add(contentPanel);

	    loadAdminData();
	    updateButton.addActionListener(e -> updateAdminData());
	}

	private void addFormRow(String labelText, JTextField textField, JPanel panel) {
	    JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
	    row.setOpaque(false);

	    JLabel label = new JLabel(labelText);
	    label.setFont(new Font("Arial", Font.BOLD, 18)); 
	    label.setPreferredSize(new Dimension(100, 30)); 
	    row.add(label);

	    textField.setPreferredSize(new Dimension(250, 30)); 
	    textField.setFont(new Font("Arial", Font.PLAIN, 16));
	    row.add(textField);

	    panel.add(row);
	}


    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        return label;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField(15);
        field.setFont(new Font("Arial", Font.PLAIN, 18));
        field.setPreferredSize(new Dimension(250, 35));
        return field;
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setPreferredSize(new Dimension(200, 35));
        return button;
    }

    private void loadAdminData() {
        try {
            String query = "SELECT id, name, email, role  FROM admin where id = "+adminID+"";
            ResultSet rs = con.s.executeQuery(query);

            if (rs.next()) {
            	
                adminIdField.setText(String.valueOf(rs.getInt("id")));
                nameField.setText(rs.getString("name"));
                emailField.setText(rs.getString("email"));
                roleField.setText(rs.getString("role"));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading admin data!", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateAdminData() {
        try {
        	int id =Integer.parseInt(adminIdField.getText());
        	String name=nameField.getText();
        	String email= emailField.getText();
        	String role= roleField.getText();
            String query = "UPDATE admin SET id = "+id+", name = '"+name+"', email = '"+email+"', role = '"+role+"' WHERE id = '"+adminID+"'";
            
            Statement stmt = con.c.createStatement(); 
            int rowsAffected = con.s.executeUpdate(query);
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Admin profile updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No changes were made.", "Info", JOptionPane.WARNING_MESSAGE);
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating admin data!", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
