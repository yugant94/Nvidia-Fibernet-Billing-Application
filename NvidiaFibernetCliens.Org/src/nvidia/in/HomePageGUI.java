package nvidia.in;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class HomePageGUI extends JFrame {

	private JButton existingCustomer, newConnection, adminSignIn;

	public HomePageGUI() {
		setUpFrame();
		initializeComponents();
		addComponents();
		setupListeners();
	}

	private void setUpFrame() {
		setTitle("Nvidia Fibernet");
		setBounds(100, 30, 1366, 768);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setContentPane(createBackgroundImage());
		setVisible(true);
	}
	
	
	private JPanel createBackgroundImage() {
		return new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				ImageIcon icon = new ImageIcon(
						"C://Users//admin//Desktop//NvidiaFibernetCliensOrg//"
						+ "NvidiaFibernetCliens.Org//src//icons//logo.jpg");
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

	private JButton createStyledButton(String buttonTitle, Color borderColor) {
		JButton button = new JButton(buttonTitle);
		button.setFont(new Font("Arial", Font.BOLD, 15));
		button.setBackground(Color.white);
		button.setForeground(Color.BLACK);
		button.setBorder(BorderFactory.createLineBorder(borderColor, 2));
		button.setOpaque(true);
		button.setFocusPainted(false);
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				button.setBackground(new Color(220, 220, 220)); 
			}

			@Override
			public void mouseExited(MouseEvent e) {
				button.setBackground(Color.white); 
			}
		});
		button.setPreferredSize(new Dimension(300, 50));
		button.setMaximumSize(new Dimension(300, 50));
		return button;
	}

	private void initializeComponents() {
		existingCustomer = 
				createStyledButton("Existing Customer",
						new Color(30, 144, 255));
		newConnection = 
				createStyledButton("New Connection",
						new Color(220, 20, 60));
		adminSignIn =
				createStyledButton("Admin Sign-in",
						new Color(50, 205, 50));
	}

	private void addComponents() {
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setOpaque(false);

		JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		titlePanel.setOpaque(false);
		JLabel title = new JLabel("Welcome To Nvidia Fibernet");
		title.setFont(new Font("Arial", Font.BOLD, 24));
		title.setForeground(Color.WHITE);
		titlePanel.add(title);

		JPanel buttonPanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.setColor(new Color(0, 0, 0, 100));
				g.fillRoundRect(0, 0, 10000, 600, 15, 15);
			}
		};
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		buttonPanel.setOpaque(false);
		buttonPanel.add(Box.createVerticalStrut(15));
		buttonPanel.add(existingCustomer);
		buttonPanel.add(Box.createVerticalStrut(15));
		buttonPanel.add(newConnection);
		buttonPanel.add(Box.createVerticalStrut(15));
		buttonPanel.add(adminSignIn);
		buttonPanel.add(Box.createVerticalStrut(15));

		existingCustomer.setAlignmentX(Component.CENTER_ALIGNMENT);
		newConnection.setAlignmentX(Component.CENTER_ALIGNMENT);
		adminSignIn.setAlignmentX(Component.CENTER_ALIGNMENT);

		mainPanel.add(Box.createVerticalStrut(50));
		mainPanel.add(titlePanel);
		mainPanel.add(Box.createVerticalStrut(50));
		mainPanel.add(buttonPanel);

		JPanel contentPane = (JPanel) getContentPane();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		contentPane.add(Box.createVerticalGlue());
		contentPane.add(mainPanel);
		contentPane.add(Box.createVerticalGlue());
	}

	private void setupListeners() {
		existingCustomer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new SignInGUI();
				dispose();
			}
		});

		newConnection.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new UserRegistrationGUI();
				dispose();

			}
		});
		adminSignIn.addActionListener(e -> toAdminSignIn());
	}

	private void toAdminSignIn() {
		dispose();
		new AdminSignInGUI();
	}

	public static void main(String[] args) {
		new HomePageGUI();
	}
}
