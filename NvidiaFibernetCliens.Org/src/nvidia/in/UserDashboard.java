package nvidia.in;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.FlatteningPathIterator;
import java.security.interfaces.RSAKey;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.DefaultTextUI;

public class UserDashboard extends JFrame {
	private static String status;
	public static String planType;
	private static double planPrice;
	private static String planData;
	private static String speed;
	private static String planDuration;
	static long accountNo;
	public static long mobNo;

	public UserDashboard() throws SQLException {
		setUpFrame();
		JPanel headerP = createHeaderPanel();
		add(headerP, BorderLayout.NORTH);

		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		JPanel rightPanel = createAccountDetailsPanel();
		mainPanel.add(rightPanel, BorderLayout.CENTER);
		JPanel leftPanel = createPromotionalPanel();
		mainPanel.add(leftPanel, BorderLayout.WEST);

		add(mainPanel, BorderLayout.CENTER);
		getContentPane().setBackground(new Color(240, 240, 240));
	}

	public JPanel createPromotionalPanel() {
		JPanel promotionPanel = new JPanel();
		promotionPanel.setLayout(new BoxLayout(promotionPanel, BoxLayout.Y_AXIS));
		promotionPanel.setBackground(Color.white);

		JPanel gradientPanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g;
				GradientPaint gradient = new GradientPaint(0, 0, new Color(255, 100, 150), getWidth(), getHeight(),
						new Color(255, 150, 200));
				g2d.setPaint(gradient);
				g2d.fillRect(0, 0, getWidth(), getHeight());
			}
		};

		JLabel promoText = new JLabel("<html><div style='text-align: center;'>"
				+ "Pay your Nvidia Fibernet<br/>bill via CRED UPI and Get<br/>"
				+ "<span style='font-size: 24px;'>up to Rs. 250*</span><br/>" + "Cashback</div></html>");
		promoText.setHorizontalAlignment(JLabel.CENTER);
		promoText.setFont(new Font("Arial", Font.BOLD, 18));
		promoText.setForeground(Color.WHITE);
		gradientPanel.setLayout(new GridBagLayout());
		gradientPanel.add(promoText);
		promotionPanel.add(gradientPanel);
		return promotionPanel;
	}

	private void setUpFrame() {
		setTitle("Nvidia Fibernet");
		setBounds(0, 0, 1920, 1080);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	private JButton createStyledButton(String text) {
		JButton button = new JButton(text);
		button.setFont(new Font("Arial", Font.PLAIN, 14));
		button.setForeground(new Color(51, 51, 51));
		button.setBackground(Color.WHITE);
		button.setFocusPainted(false);
		button.setContentAreaFilled(false);
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));
		button.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseEntered(MouseEvent e) {
				button.setFont(new Font("Arial", Font.BOLD, 14));
				button.setForeground(new Color(255, 51, 85));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				button.setFont(new Font("Arial", Font.BOLD, 14));
				button.setForeground(new Color(51, 51, 51));
			}
		});

		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				switch (text) {
				case "Pay Bills":
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							try {
								UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
							} catch (Exception ex) {
								ex.printStackTrace();
							}
							new PaymentGUI().setVisible(true);
						}
					});
					break;
				case "Service Requests":
					if (UserDashboard.status == "INACTIVE") {
						JOptionPane.showMessageDialog(UserDashboard.this,
								"Inactive Plan. Service Request Cannot Be Raised", "Failed",
								JOptionPane.INFORMATION_MESSAGE);
					} else {
						try {
							UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						new ServiceRequest();
					}
					break;
				case "New Connection":
					try {
						UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
							| UnsupportedLookAndFeelException ex) {
						ex.printStackTrace();
					}
					BuyConnection.getPhoneNo = mobNo;
					new BuyConnection();
					break;
				case "FAQ":
					try {
						UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
							| UnsupportedLookAndFeelException ex) {
						ex.printStackTrace();
					}
					new FAQSection();
					break;
				}

			}
		});
		return button;
	}

	public JPanel createAccountDetailsPanel() throws SQLException {
		JPanel detailsPanel = new JPanel();
		detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
		detailsPanel.setBackground(Color.white);
		detailsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		if (getStatus()) {
			status = "ACTIVE";
		} else {
			status = "INACTIVE";
			planType = "NA";
			speed = "NA";
			planData = "NA";
		}

		JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		statusPanel.setBackground(Color.white);

		JLabel statusLabel = new JLabel(UserDashboard.status);
		statusLabel.setBackground(Color.white);
		statusLabel.setFont(new Font("Arial", Font.BOLD, 12));
		statusLabel.setForeground(new Color(40, 167, 69));
		statusPanel.add(statusLabel);

		JPanel planPanel = new JPanel();
		planPanel.setLayout(new BoxLayout(planPanel, BoxLayout.Y_AXIS));
		planPanel.setBackground(Color.white);

		JLabel planName = new JLabel(UserDashboard.planType);
		planName.setFont(new Font("Arial", Font.BOLD, 18));
		planPanel.add(planName);

		JPanel duePanel = new JPanel();
		duePanel.setLayout(new BoxLayout(duePanel, BoxLayout.Y_AXIS));
		duePanel.setBackground(Color.white);

		JLabel dueDate = new JLabel("27th Feb 2025");
		dueDate.setFont(new Font("Arial", Font.BOLD, 14));
		duePanel.add(dueDate);

		JLabel dueDateLabel = new JLabel("Due Date");
		dueDateLabel.setFont(new Font("Arial", Font.BOLD, 12));
		duePanel.add(dueDateLabel);

		JPanel usagePanel = new JPanel();
		usagePanel.setBackground(Color.white);

		JLabel usageLabel = new JLabel("45.6 GB of 150GB");
		usageLabel.setFont(new Font("Arial", Font.BOLD, 12));
		usagePanel.add(usageLabel);

		JPanel upgradePanel = new JPanel();
		upgradePanel.setBackground(new Color(255, 240, 240));
		upgradePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JLabel upgradeLabel = new JLabel("Upgrade To Enjoy Netflix all No Extra Cost Higher Speeds and Great Offers");
		upgradeLabel.setFont(new Font("Arial", Font.BOLD, 12));
		upgradePanel.add(upgradeLabel);

		JButton upgradeButton = new JButton("Upgrade");
		upgradeButton.setBackground(Color.white);
		upgradeButton.setFocusPainted(false);
		upgradePanel.add(upgradeButton);

		detailsPanel.add(statusPanel);
		detailsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		detailsPanel.add(planPanel);
		detailsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		detailsPanel.add(duePanel);
		detailsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		detailsPanel.add(usagePanel);
		detailsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		detailsPanel.add(upgradePanel);

		return detailsPanel;
	}

	public boolean getStatus() throws SQLException {
		ConnectionJDBC con = new ConnectionJDBC();

		String query = "SELECT * FROM broaddband_plans WHERE mobilenumber='" + mobNo + "';";
		ResultSet rs = con.s.executeQuery(query);

		if (rs.next()) {
			planType = rs.getString("planType");
			planPrice = rs.getDouble("planPrice");
			planData = rs.getString("planData");
			speed = rs.getString("speed");
			planDuration = rs.getString("planDuration");
			accountNo = rs.getLong("accountNo");
		} else {
			rs.close();
			con.s.close();
			return false;
		}

		rs.close();

		// Second query execution
		String query2 = "SELECT billAmount FROM bill WHERE accountNumber='" + accountNo + "';";
		ResultSet rs2 = con.s.executeQuery(query2);

		if (rs2.next()) {
			double billAmount = rs2.getDouble("billAmount");
			BillLogics.billAmount = billAmount;
		}

		rs2.close();
		con.s.close();
		return true;
	}

	public JPanel createHeaderPanel() {
		JPanel headerPanel = new JPanel();
		headerPanel.setLayout(new BorderLayout());
		headerPanel.setBackground(Color.white);
		headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

		JLabel titleJLabel = new JLabel("Nvidia-Fibernet");
		titleJLabel.setForeground(new Color(255, 51, 85));
		titleJLabel.setFont(new Font("Arial", Font.BOLD, 24));

		JPanel navMenu = new JPanel();
		navMenu.setLayout(new FlowLayout(FlowLayout.CENTER));
		navMenu.setBackground(Color.white);

		String[] menuItems = { "Pay Bills", "Service Requests", "New Connection", "FAQ" };

		for (String items : menuItems) {
			JButton menuButtons = createStyledButton(items);
			navMenu.add(menuButtons);
		}

		JPanel dropDownPanel = new JPanel();
		dropDownPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
		dropDownPanel.setBackground(Color.white);

		JButton dropdownButton = new JButton("Options");
		dropdownButton.setAlignmentY(CENTER_ALIGNMENT);
		dropdownButton.setFont(new Font("Arial", Font.BOLD, 15));
		dropdownButton.setForeground(new Color(51, 51, 51));
		dropdownButton.setBackground(Color.white);
		dropdownButton.setFocusPainted(false);

		dropdownButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

		JPopupMenu menus = new JPopupMenu();
		JMenuItem profileOptionItem = new JMenuItem("Profile");
		JMenuItem logOutOption = new JMenuItem("Logout");

		profileOptionItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Profile.mobNo = mobNo;
				new Profile();
			}
		});

		logOutOption.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to logout? ", "Logout",
						JOptionPane.YES_NO_OPTION);
				if (choice == JOptionPane.YES_OPTION) {
					dispose();
					new SignInGUI();
				}
			}
		});

		menus.add(profileOptionItem);
		menus.add(logOutOption);

		dropdownButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				menus.show(dropdownButton, 0, dropdownButton.getHeight());
			}
		});

		dropDownPanel.add(dropdownButton);
		headerPanel.add(titleJLabel, BorderLayout.WEST);
		headerPanel.add(navMenu, BorderLayout.CENTER);
		headerPanel.add(dropDownPanel, BorderLayout.EAST);

		return headerPanel;

	}

	public static void main(String[] args) {
		try {
			new UserDashboard();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
