package nvidia.in;

import java.awt.*;
import javax.swing.*;

public class AdminDashboard extends JFrame {
    private JPanel rightPanel;
    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setBounds(160,70,1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setVisible(true);

        JPanel leftPanel = createLeftPanel();
        rightPanel = new JPanel();
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setLayout(new BorderLayout());

        // 🔹 Show AccountDetailsPanel by Default
        rightPanel.add(new AccountDetailsPanel(), BorderLayout.CENTER);

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
    }


    private JPanel createLeftPanel() {
        // 🔹 Main Left Panel (Holds Button Panel)
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.BLACK);
        leftPanel.setPreferredSize(new Dimension(250, getHeight()));
        leftPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2)); // Panel border

        // 🔹 Separate Button Panel (Fixed Position)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS)); // Vertical Layout
        buttonPanel.setOpaque(false); // Transparent background
//        buttonPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1)); // Border for clarity

        // 🔹 Add Buttons with Custom Spacing
        String[] buttonLabels = {"Bill Generator", "Account Details", "Service Requests", "Profile"};
        int[] spacing = {40, 20, 20, 20}; // Adjust space between buttons

        for (int i = 0; i < buttonLabels.length; i++) {
            if (i > 0) buttonPanel.add(Box.createRigidArea(new Dimension(0, spacing[i]))); // Adds space between buttons
            JButton button = createStyledButton(buttonLabels[i]);
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1)); // Button border
            buttonPanel.add(button);
        }

        // 🔹 Add Button Panel with Fixed Positioning
        JPanel wrapperPanel = new JPanel();
//        wrapperPanel.setBorder(BorderFactory.createLineBorder(Color.blue, 1));
        wrapperPanel.setLayout(new BorderLayout());
        wrapperPanel.setOpaque(false);
        wrapperPanel.add(Box.createVerticalStrut(50), BorderLayout.NORTH); // Adjust '50' to move the button panel up/down
        wrapperPanel.add(buttonPanel, BorderLayout.CENTER);

        leftPanel.add(wrapperPanel, BorderLayout.NORTH); // Positions at the top

        return leftPanel;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(200, 50)); 
        button.setMaximumSize(new Dimension(200, 50));  
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(50, 50, 50)); 
        button.setOpaque(true); 
        button.setBorderPainted(false); 
        button.setContentAreaFilled(true); 
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(70, 70, 70)); 
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(50, 50, 50)); 
            }
        });

        button.addActionListener(e -> switchPanel(text));

        return button;
    }



    private void switchPanel(String buttonText) {
        rightPanel.removeAll();

        if (buttonText.equals("Bill Generator")) {
            rightPanel.add(new BillGeneratorPanel(), BorderLayout.CENTER);
        } else if (buttonText.equals("Account Details")) {
            rightPanel.add(new AccountDetailsPanel(), BorderLayout.CENTER);
        } else if (buttonText.equals("Service Requests")) { // Fixed condition
            rightPanel.add(new ServiceRequestsPanel(), BorderLayout.CENTER);
        } else if (buttonText.equals("Profile")) { // Fixed condition
            rightPanel.add(new AdminProfilePanel(), BorderLayout.CENTER);
        } else {
            rightPanel.add(new JLabel(buttonText + " Section", SwingConstants.CENTER), BorderLayout.CENTER);
        }

        rightPanel.revalidate();
        rightPanel.repaint();
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminDashboard().setVisible(true));
    }
}