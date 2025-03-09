package nvidia.in;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
//import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminSignInGUI extends JFrame {
	private JTextField userNameField;
	private JPasswordField passwordField;
	private JCheckBox termsBox;
	private JButton signInButton, goBackButton, signUpButton;

	public AdminSignInGUI() {
		setupFrame();
		initializeComponents();
		addComponents();
		setVisible(true);
	}

	private void setupFrame() {
		setTitle("Admin Sign-In");
		setSize(1000, 700);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setContentPane(createBackgroundImage());
	}

	private JPanel createBackgroundImage() {
		return new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				ImageIcon icon = new ImageIcon(
						"C://Users//admin//Desktop//NvidiaFibernetCliensOrg//NvidiaFibernetCliens.Org//src//icons//admin-signup-signin.jpg");
				Image image = icon.getImage();
				double pWidth = getWidth();
				double pHeight = getHeight();
				double imageWidth = image.getWidth(this);
				double imageHeight = image.getHeight(this);
				double scale = Math.max(pWidth / imageWidth, pHeight / imageHeight);
				int scaledWidth = (int) (imageWidth * scale);
				int scaledHeight = (int) (imageHeight * scale);
				int p = (int) (pWidth - scaledWidth) / 2;
				int q = (int) (pHeight - scaledHeight) / 2;
				g.drawImage(image, p, q, scaledWidth, scaledHeight, this);
				g.setColor(new Color(0, 0, 0, 150));
				g.fillRect(0, 0, getWidth(), getHeight());
			}
		};
	}

	private void initializeComponents() {
		userNameField = createStyledTextField(20);
		passwordField = createStyledPasswordField(20);
		termsBox = createStyledCheckBox("I agree to the terms & conditions");
		signInButton = createStyledButton("Sign In", new Color(30, 144, 255));
		goBackButton = createStyledButton("Go Back", new Color(220, 20, 60));
		signUpButton=createStyledButton("Admin Sign-Up", Color.green);
	}
	
	private JPasswordField createStyledPasswordField(int columns) {
		JPasswordField field = new JPasswordField(columns);
		field.setFont(new Font("Arial", Font.PLAIN, 14));
		field.setBackground(new Color(255, 255, 255));
		field.setForeground(new Color(33, 33, 33));
		field.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)),
				BorderFactory.createEmptyBorder(5, 10, 5, 10)));
		return field;
	}

	private JTextField createStyledTextField(int columns) {
		JTextField field = new JTextField(columns);
		field.setFont(new Font("Arial", Font.PLAIN, 14));
		field.setBackground(new Color(255, 255, 255));
		field.setForeground(new Color(33, 33, 33));
		field.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)),
				BorderFactory.createEmptyBorder(5, 10, 5, 10)));
		return field;
	}

	private JCheckBox createStyledCheckBox(String text) {
		JCheckBox checkBox = new JCheckBox(text);
		checkBox.setFont(new Font("Arial", Font.PLAIN, 14));
		checkBox.setForeground(Color.WHITE);
		checkBox.setOpaque(false);
		return checkBox;
	}

	private JButton createStyledButton(String text, Color background) {
		JButton button = new JButton(text);
		button.setFont(new Font("Arial", Font.BOLD, 15));
		button.setBackground(background);
		button.setForeground(Color.BLACK);
		button.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.WHITE, 2),
				BorderFactory.createEmptyBorder(12, 25, 12, 25)));
		button.setOpaque(true);
		button.setFocusPainted(false);
		button.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				button.setBackground(background.brighter());
			}

			public void mouseExited(MouseEvent e) {
				button.setBackground(background);
			}
		});
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnsupportedLookAndFeelException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		return button;
	}

	private void addComponents() {
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		contentPanel.setOpaque(false);
		contentPanel.add(Box.createVerticalStrut(230));

		JLabel title = new JLabel("Admin Sign-In");
		title.setFont(new Font("Arial", Font.BOLD, 24));
		title.setForeground(Color.WHITE);
		title.setAlignmentX(Component.CENTER_ALIGNMENT);
		contentPanel.add(title);
		contentPanel.add(Box.createRigidArea(new Dimension(0, 50)));

		JPanel formWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
		formWrapper.setOpaque(false);
//		formWrapper.setBorder(BorderFactory.createLineBorder(Color.RED, 3));

		formWrapper.setPreferredSize(new Dimension(450, 150)); 

		JPanel formPanel = new JPanel();
		formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
		formPanel.setOpaque(false);
		formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

		addFormRow("Username:", userNameField, formPanel);
		formPanel.add(Box.createVerticalStrut(20));
		addFormRow("Password:", passwordField, formPanel);
		formWrapper.add(formPanel);
		contentPanel.add(formWrapper);
		contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 15));

		buttonPanel.setOpaque(false);
		buttonPanel.add(signInButton);
		buttonPanel.add(goBackButton);
		buttonPanel.add(signUpButton);
		contentPanel.add(buttonPanel);

		add(contentPanel);

		signInButton.addActionListener(e -> handleSignIn());
		goBackButton.addActionListener(e -> handleGoBack());
		signUpButton.addActionListener(e -> toSignUp());

	}

	private void toSignUp() {
	
		dispose();
		new AdminSignUp();
	}

	private void addFormRow(String labelText, JComponent component, JPanel formPanel) {
		JPanel rowPanel = new JPanel();
		rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.X_AXIS));
		rowPanel.setOpaque(false);

		JLabel label = new JLabel(labelText);
		label.setFont(new Font("Arial", Font.PLAIN, 14));
		label.setForeground(Color.WHITE);
		label.setPreferredSize(new Dimension(120, 25));
		rowPanel.add(label);
		rowPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		rowPanel.add(component);
		formPanel.add(rowPanel);
		formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
	}

	private boolean isValidMobile(String mobile) {
		return mobile.matches("\\d{10}");
	}

	private void showError(String message) {
		JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
	}

	private void handleSignIn() {
		
		String username = userNameField.getText().trim();
		String password = passwordField.getText().trim();

		if (username.isEmpty() || password.isEmpty()) {
			showError("Please fill all fields correctly.");
			return;
		}
		
		String query = "SELECT id FROM admin WHERE name='" + username + "' AND password='" + password + "'";
		ConnectionJDBC con = new ConnectionJDBC();
		ResultSet rs;
		try {
			rs = con.s.executeQuery(query);
			if (rs.next()) {
				
				AdminProfilePanel.adminID=rs.getInt("id");
				JOptionPane.showMessageDialog(this, "Welcome Back " + username, "Success",
						JOptionPane.INFORMATION_MESSAGE);
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							new AdminDashboard();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				
				dispose();
			} else {
				showError("Invalid Credentials! Login Unsuccessful");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			showError("Database Error: " + e.getMessage());
		}
		
		userNameField.setText("");
		passwordField.setText("");
	}

	private void showSuccessMsg(String text) {
		JOptionPane.showMessageDialog(this, text, "Success", JOptionPane.OK_OPTION);
	}

	private void showErrorMsg(String text) {
		JOptionPane.showMessageDialog(this, text, "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	
	private void handleGoBack() {
		int choice = JOptionPane.showConfirmDialog(this, "Go Back To Main Menu?", "Confirm", JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE);
		if (choice == JOptionPane.YES_OPTION) {
			SwingUtilities.invokeLater(() -> new HomePageGUI());
			dispose();
		}
	}

	public static void main(String[] args) {
		new AdminSignInGUI();
	}
}
