package nvidia.in;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Profile extends JFrame {
    private JLabel nameLabel, mobileLabel, cityLabel, profileImageLabel;
    public static long mobNo;

    public Profile() {
        setupFrame();
        initializeComponents();
        fetchUserData();
        setVisible(true);
    }

    private void setupFrame() {
        setTitle("User Profile");
        setSize(600, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(createGradientBackground());
    }

    private JPanel createGradientBackground() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int width = getWidth();
                int height = getHeight();

                // Blueish-White Vertical Gradient
                Color endColor = new Color(240, 248, 255); // Light Blueish White
                Color startColor = new Color(135, 206, 250);   // Soft Sky Blue
                
                GradientPaint gp = new GradientPaint(0, 0, startColor, 0, height, endColor);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, width, height);
            }
        };
        
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());

        // Create a panel for the profile image with vertical spacing
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setOpaque(false);
        imagePanel.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0)); // Adds 20px space from the top

        // Load and resize the profile image
        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/icons/user.png"));
        Image scaledImage = originalIcon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH); // Resize to 100x100
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        profileImageLabel = new JLabel(scaledIcon);
        profileImageLabel.setHorizontalAlignment(JLabel.CENTER);

        imagePanel.add(profileImageLabel, BorderLayout.CENTER);
        add(imagePanel, BorderLayout.NORTH);

        // Create details panel with GridBagLayout for center alignment
        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setOpaque(false);
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(-50, 0, 0, 0)); // Moves details upward


        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(5, 0, 5, 0); // Add spacing between labels
        gbc.anchor = GridBagConstraints.CENTER; // Center align text

        nameLabel = new JLabel("Name: ");
        mobileLabel = new JLabel("Mobile: ");
        cityLabel = new JLabel("City: ");

        Font labelFont = new Font("Arial", Font.BOLD, 16);
        nameLabel.setFont(labelFont);
        mobileLabel.setFont(labelFont);
        cityLabel.setFont(labelFont);

        // Add labels to the panel with constraints
        detailsPanel.add(nameLabel, gbc);
        detailsPanel.add(mobileLabel, gbc);
        detailsPanel.add(cityLabel, gbc);

        add(detailsPanel, BorderLayout.CENTER);
    }




    private void fetchUserData() {
        try {
        	ConnectionJDBC con = new ConnectionJDBC();
            String query = "SELECT name, mobile, city FROM sign_in WHERE mobile='" + mobNo + "'";
            ResultSet rs;

            rs = con.s.executeQuery(query);
            if (rs.next()) {
                nameLabel.setText("Name: " + rs.getString("name"));
                mobileLabel.setText("Mobile: " + rs.getString("mobile"));
                cityLabel.setText("City: " + rs.getString("city"));
            } else {
                JOptionPane.showMessageDialog(this, "User not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            con.c.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new Profile();
    }
}
