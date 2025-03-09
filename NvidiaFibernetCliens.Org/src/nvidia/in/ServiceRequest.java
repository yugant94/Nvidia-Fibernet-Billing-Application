package nvidia.in;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;

public class ServiceRequest extends JFrame {
	private JTextField userNameField;
	private JButton signInButton, goBackButton, clearButton;
	private JComboBox<String> issue;
	private static final HashSet<Integer> generatedIds = new HashSet<>();
	private JTextField requestIdField;
	private JSpinner dateSpinner;
	private static final Random random = new Random();
	public static String user;
	public static long mobNo;

	public ServiceRequest() {
		setupFrame();
		initializeComponents();
		addComponents();
		setVisible(true);
	}

	private void setupFrame() {
		setTitle("Service Request Form");
		setSize(800, 600);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
	}

	class GradientPanel extends JPanel {
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			int width = getWidth();
			int height = getHeight();

			GradientPaint gradient = new GradientPaint(0, 0, new Color(89, 147, 240), 0, height, new Color(30, 30, 31));
			g2d.setPaint(gradient);
			g2d.fillRect(0, 0, width, height);
		}
	}

	private void initializeComponents() {
		userNameField = createStyledTextField(50);
		dateSpinner = createDateSpinner();
		issue = createStyledComboBox(new String[] { "Select an issue", "Connection issue", "Speed Problem",
				"Bill related", "Technical support", "Plan Upgrade", "General Enquiry" });
		signInButton = createStyledButton("Submit Request", new Color(89, 147, 240));
		goBackButton = createStyledButton("Clear Form", new Color(89, 147, 240));
		clearButton = createStyledButton("Go Back", new Color(89, 147, 240));
	}

	private JComboBox<String> createStyledComboBox(String[] items) {
		JComboBox<String> comboBox = new JComboBox<>(items);
		comboBox.setFont(new Font("Arial", Font.PLAIN, 14));
		comboBox.setBackground(new Color(255, 255, 255));
		comboBox.setForeground(new Color(33, 33, 33));
		comboBox.setPreferredSize(new Dimension(250, 35));
		return comboBox;
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
		button.setForeground(Color.white);
		button.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.WHITE, 2),
				BorderFactory.createEmptyBorder(10, 20, 10, 20)));
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
		JPanel contentPanel = new GradientPanel();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		contentPanel.setOpaque(false);

		contentPanel.add(Box.createVerticalStrut(10));
		JLabel title = new JLabel("Service Request Form");
		title.setFont(new Font("Arial", Font.BOLD, 24));
		title.setForeground(Color.WHITE);
		title.setAlignmentX(Component.CENTER_ALIGNMENT);
		contentPanel.add(title);
		contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		contentPanel.add(Box.createVerticalStrut(90));

		JPanel formWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
		formWrapper.setOpaque(false);

		formWrapper.setPreferredSize(new Dimension(700, 250)); // Increased width

		JPanel formPanel = new JPanel();
		formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
		formPanel.setOpaque(false);
//		formPanel.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));

		formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		requestIdField = new JTextField(10);
		requestIdField.setText(String.valueOf(generateUniqueRequestId())); // Set initial request ID
		requestIdField.setEditable(false);
		dateSpinner = createDateSpinner();

		addFormRow("Request ID:", requestIdField, formPanel);
		formPanel.add(Box.createVerticalStrut(20));
		addFormRow("User name:", userNameField, formPanel);
		userNameField.setText(user);
		formPanel.add(Box.createVerticalStrut(20));
		addFormRow("Request Type:", issue, formPanel);
		formPanel.add(Box.createVerticalStrut(20));
		addFormRow("Date:", dateSpinner, formPanel);
		formPanel.add(Box.createRigidArea(new Dimension(0, 10)));

		formWrapper.add(formPanel);
		contentPanel.add(formWrapper);
		contentPanel.add(Box.createRigidArea(new Dimension(0, 50)));

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 15));

		buttonPanel.setOpaque(false);
		buttonPanel.add(signInButton);
		signInButton.addActionListener(e -> {
			handleRequestSubmission();
			requestIdField.setText(String.valueOf(generateUniqueRequestId())); 
		});
		buttonPanel.add(clearButton);
		clearButton.addActionListener(e -> {
			requestIdField.setText(String.valueOf(generateUniqueRequestId())); 
			userNameField.setText("");
			issue.setSelectedIndex(0);
			dateSpinner.setValue(new java.util.Date());
		});
		buttonPanel.add(goBackButton);
		goBackButton.addActionListener(e -> {
			UserDashboard.mobNo = mobNo;
		});
		contentPanel.add(buttonPanel);
		add(contentPanel);
	}

	private void handleRequestSubmission() {
		String username = userNameField.getText().trim();
		String requestType = (String) issue.getSelectedItem(); 
		java.util.Date selectedDate = (java.util.Date) dateSpinner.getValue();

		if (username.isEmpty() || "Select an issue".equals(requestType)) {
			showError("Please fill all fields correctly.");
			return;
		}

		int requestId = generateUniqueRequestId(); // Generate unique request ID
		String query = "INSERT INTO vice_request (request_id, user, type, request_date) VALUES (" + requestId + ", '"
				+ username + "', '" + requestType + "', '" + new java.sql.Date(selectedDate.getTime()) + "')";

		ConnectionJDBC con = new ConnectionJDBC();
		try {
			int ret = con.s.executeUpdate(query);
			if (ret == 1) {
				showSuccessMsg("Request Submitted Successfully! Your Request ID: " + requestId);
				requestIdField.setText(String.valueOf(generateUniqueRequestId())); 
			} else {
				showError("Unable to submit request!");
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		dispose();
		try {
			UserDashboard.mobNo = mobNo;
			new UserDashboard();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private int generateUniqueRequestId() {
		int requestId;
		do {
			requestId = random.nextInt(100000 - 1000) + 1000; 
		} while (generatedIds.contains(requestId)); 

		generatedIds.add(requestId);
		return requestId;
	}

	private JSpinner createDateSpinner() {
		JSpinner spinner = new JSpinner(new SpinnerDateModel());
		JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner, "yyyy-MM-dd");
		spinner.setEditor(editor);
		spinner.setValue(new Date());
		return spinner;
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

	private void showSuccessMsg(String text) {
		JOptionPane.showMessageDialog(this, text, "Success", JOptionPane.OK_OPTION);
	}

	private void handleGoBack() {
		int choice = JOptionPane.showConfirmDialog(this, "Go Back To Dashboard?", "Confirm", JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE);
		if (choice == JOptionPane.YES_OPTION) {
			SwingUtilities.invokeLater(() -> {
				try {
					UserDashboard.mobNo = mobNo;
					new UserDashboard();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			});
			dispose();
		}
	}

	public static void main(String[] args) {
		new ServiceRequest();
	}
}
