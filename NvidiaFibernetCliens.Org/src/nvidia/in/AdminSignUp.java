package nvidia.in;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

public class AdminSignUp extends JFrame {
	private JTextField userNameField;
	private JPasswordField passwordField;
	private JCheckBox termsBox;
	private JButton signUpButton, signInButton, goToHomePageButton;

	public AdminSignUp() {
		setupFrame();
		initializeComponents();
		addComponents();
		setVisible(true);
	}

	private void setupFrame() {
		setTitle("Admin Sign-Up");
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
						"C://Users//admin//Desktop//"
						+ "NvidiaFibernetCliensOrg//"
						+ "NvidiaFibernetCliens.Org//src//icons"
						+ "//logo.jpg");
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
		userNameField = createStyledTextField(15);
		passwordField = createStyledPasswordField(15);
		termsBox = createStyledCheckBox("I agree to the terms & conditions");
		signUpButton = createStyledButton("Sign Up", new Color(30, 144, 255));
		goToHomePageButton = createStyledButton("Back to Homapage", Color.green);
		signInButton = createStyledButton("Sign In", new Color(220, 20, 60));
	}

	private JTextField createStyledTextField(int columns) {
		JTextField field = new JTextField(columns);
		field.setFont(new Font("Arial", Font.PLAIN, 14));
		field.setBackground(new Color(255, 255, 255));
		field.setForeground(new Color(33, 33, 33));
		field.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2),
				BorderFactory.createEmptyBorder(10, 10, 10, 10) // Increased padding for height
		));
		field.setPreferredSize(new Dimension(250, 35)); // Increased height
		return field;
	}

	private JPasswordField createStyledPasswordField(int columns) {
		JPasswordField field = new JPasswordField(columns);
		field.setFont(new Font("Arial", Font.PLAIN, 14));
		field.setBackground(new Color(255, 255, 255));
		field.setForeground(new Color(33, 33, 33));
		field.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2),
				BorderFactory.createEmptyBorder(10, 10, 10, 10) // Increased padding for height
		));
		field.setPreferredSize(new Dimension(250, 35)); // Increased height
		return field;
	}
	
	private JCheckBox createStyledCheckBox(String text) {
		JCheckBox checkBox = new JCheckBox(text);
		checkBox.setFont(new Font("Arial", Font.PLAIN, 14));
		checkBox.setForeground(Color.WHITE);
		checkBox.setOpaque(false);
		return checkBox;
	}

	private JButton createStyledButton(String text, Color borderColor) {
		JButton button = new JButton(text);
		button.setFont(new Font("Arial", Font.BOLD, 15));
		button.setBackground(Color.WHITE); // Background always white
		button.setForeground(Color.BLACK);
		button.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(borderColor, 2),
				BorderFactory.createEmptyBorder(12, 25, 12, 25)));
		button.setOpaque(true);
		button.setFocusPainted(false);

		button.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				button.setBorder(
						BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(borderColor.brighter(), 2),
								BorderFactory.createEmptyBorder(12, 25, 12, 25)));
			}

			public void mouseExited(MouseEvent e) {
				button.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(borderColor, 2),
						BorderFactory.createEmptyBorder(12, 25, 12, 25)));
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
		contentPanel.add(Box.createVerticalStrut(200)); // Reduced to make UI balanced

		JLabel title = new JLabel("Admin Sign-Up");
		title.setFont(new Font("Arial", Font.BOLD, 24));
		title.setForeground(Color.WHITE);
		title.setAlignmentX(Component.CENTER_ALIGNMENT);
		contentPanel.add(title);
		contentPanel.add(Box.createRigidArea(new Dimension(0, 30))); // Increased spacing below title

		JPanel formWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
		formWrapper.setOpaque(false);
		formWrapper.setPreferredSize(new Dimension(450, 200)); // Adjusted height

		JPanel formPanel = new JPanel();
		formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
		formPanel.setOpaque(false);
		formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Removed colored border

		addFormRow("Username:", userNameField, formPanel);
		formPanel.add(Box.createVerticalStrut(25)); // More gap between rows
		addFormRow("Password:", passwordField, formPanel);
		formPanel.add(Box.createVerticalStrut(35));
		formPanel.add(termsBox);
		termsBox.setAlignmentX(CENTER_ALIGNMENT);
		formPanel.add(Box.createVerticalStrut(35));
		contentPanel.add(formPanel);
		contentPanel.add(Box.createRigidArea(new Dimension(0, 30))); // Increased spacing below form

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 10));
		buttonPanel.setOpaque(false);
		buttonPanel.add(signUpButton);
		buttonPanel.add(goToHomePageButton);
		buttonPanel.add(signInButton);

		contentPanel.add(buttonPanel);

		add(contentPanel);

		signUpButton.addActionListener(e -> handleSignUp());
		goToHomePageButton.addActionListener(e -> backToHomepage());
		signUpButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				signUpButton.setBackground(new Color(230, 230, 255));
			}

			public void mouseExited(java.awt.event.MouseEvent evt) {
				signUpButton.setBackground(Color.WHITE);
			}
		});

		signInButton.addActionListener(e -> handleSignIn());
	}

	private void backToHomepage() {
		dispose();
		new HomePageGUI();
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

	private void handleSignUp() {

		String username = userNameField.getText().trim();
		String password = passwordField.getText().trim();

		if (username.isEmpty() || password.isEmpty()) {
			showErrorMsg(password);
			return;
		}

		String query = "insert into admin(name,password) values('" + username + "','" + password + "')";

		ConnectionJDBC con = new ConnectionJDBC();
		try {
			int ret = con.s.executeUpdate(query);
			if (ret == 1) {
				showSuccessMsg("Account Created Successfully!");

			} else {
				showErrorMsg("Unable to create account!");
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		userNameField.setText("");
		passwordField.setText("");
        dispose();
        new SignInGUI();
	}

	private void showSuccessMsg(String text) {
		JOptionPane.showMessageDialog(this, text, "Success", JOptionPane.OK_OPTION);
	}

	private void showErrorMsg(String text) {
		JOptionPane.showMessageDialog(this, text, "Error", JOptionPane.ERROR_MESSAGE);
	}

	private void handleSignIn() {
		dispose();
		new AdminSignInGUI();
	}

//	private void showError(String message) {
//		JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
//	}

	public static void main(String[] args) {
		new AdminSignUp();
	}
}
