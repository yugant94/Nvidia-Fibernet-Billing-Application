package nvidia.in;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.geom.RoundRectangle2D;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.JTextComponent;

public class BuyConnection extends JFrame {

	private static long accountNo;
	static long getPhoneNo;

	Plan selectedPlan;

	static final Color NVIDIA_GREEN = new Color(118, 185, 0);
	static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 28);
	static final Font SUBTITLE_FONT = new Font("Segoe UI", Font.PLAIN, 14);
	static final Font PRICE_FONT = new Font("Segoe UI", Font.BOLD, 24);

	private void generateAccountNo() {
		double max = 1000000000;
		double min = 1;

		accountNo = (long) (Math.random() * (max - min) + 1);
	}

	public BuyConnection() {
		setTitle("Nvidia Fibernet Broadband");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(1000, 800);
		setBackground(Color.WHITE);
		setVisible(true);
		
		JPanel mainPanel = new GradientPanel("/icons/admin-signup-signin.jpg");
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		addHeader(mainPanel);
		addPlans(mainPanel);
		addRegistrationForm(mainPanel);
		add(mainPanel);

		JScrollPane scrollPane = new JScrollPane(mainPanel);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		add(scrollPane);
	}

	public static void main(String[] args) {
		new BuyConnection();
	}

	public void addHeader(JPanel mainPanel) {
		JPanel headerPanel = new JPanel();
		headerPanel.setOpaque(false);
		headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

		JLabel titleLabel = new JLabel("Nvidia Fibernet");
		titleLabel.setFont(TITLE_FONT);
		titleLabel.setForeground(NVIDIA_GREEN);
		titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel subTitle = new JLabel("Experience Lightning- Fast Internet");
		subTitle.setFont(SUBTITLE_FONT);
		subTitle.setForeground(Color.DARK_GRAY);
		subTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

		headerPanel.add(titleLabel);
		headerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		headerPanel.add(subTitle);
		headerPanel.add(Box.createRigidArea(new Dimension(0, 5)));

		mainPanel.add(headerPanel);
	}

	public void handleSubmission(JTextField mobileField, JTextField usernameField, JComboBox<String> cityCombo) {
		if (selectedPlan == null) {
			showErrorMessage("Please Select The Plan Firstly!");
			return;
		}

		if (mobileField.getText().trim().isEmpty() || usernameField.getText().trim().isEmpty()
				|| cityCombo.getSelectedIndex() == 0) {
			showErrorMessage("Please Fill The Required Fields!");
			return;
		}

		long phoneNo = Long.parseLong(mobileField.getText().trim());
		if (phoneNo != getPhoneNo) {
			showErrorMessage("Please Use The Same Phone Number To Register");
			return;
		}

		String query = null;

		String planType = selectedPlan.planType;
		double planPrice = selectedPlan.planPrice;
		String planData = selectedPlan.planData;
		String planDuration = selectedPlan.planDuration;
		String speed = selectedPlan.speed;
		
		try {
		    ConnectionJDBC con = new ConnectionJDBC();
		    String checkQuery = "SELECT * FROM broaddband_plans WHERE mobilenumber = '" + phoneNo + "'";
		    ResultSet rs = con.s.executeQuery(checkQuery);
		    
		    if (rs.next()) {
		        
		        query = "UPDATE broaddband_plans SET planType='" + planType + "', planPrice='" + planPrice +
		                "', planData='" + planData + "', speed='" + speed + "', planDuration='" + planDuration + 
		                "' WHERE mobilenumber='" + phoneNo + "'";
		        con.s.executeUpdate(query);
		        showSuccessMessage("Plan updated successfully!");
		    } else {
		        
		        generateAccountNo();
		        query = "INSERT INTO broaddband_plans (mobilenumber, planType, planPrice, planData, speed, planDuration, accountNo) "
		                + "VALUES ('" + phoneNo + "', '" + planType + "', '" + planPrice + "', '" + planData + "', '" + speed + "', '" + planDuration + "', '" + accountNo + "')";
		        con.s.executeUpdate(query);
		        showSuccessMessage("Registration successful!/nPlan: " + selectedPlan.planType + "/nCity: "
						+ cityCombo.getSelectedItem() + "/n" + "Please Re-Login To Load The New Plan");
		    }
		    
		} catch (SQLException e) {
		    e.printStackTrace();
		    showErrorMessage("Database error! Please try again.");
		}

		dispose();


	}

	private void showErrorMessage(String message) {
		JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
	}

	private void showSuccessMessage(String message) {
		JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
	}

	public void addRegistrationForm(JPanel mainPanel) {
		JPanel formPanel = new RoundedPanel(Color.WHITE);
		formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
		formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

		JLabel formTitle = new JLabel("Complete Your Registration");
		formTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
		formTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		formTitle.setForeground(NVIDIA_GREEN);

		JTextField mobileField = createStyledTextField("Enter Mobile Number");
		JTextField userNameField = createStyledTextField("Enter Username");
		JComboBox<String> cityCombo = createStyledComboBox();

		JButton submitBtn = new JButton("Register Now");
		submitBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
		submitBtn.setForeground(Color.BLACK);
		submitBtn.setBackground(NVIDIA_GREEN);
		submitBtn.setBorder(new EmptyBorder(10, 30, 10, 30));
		submitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		submitBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		submitBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				handleSubmission(mobileField, userNameField, cityCombo);
			}
		});

		formPanel.add(formTitle);
		formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		formPanel.add(mobileField);
		formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		formPanel.add(userNameField);
		formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		formPanel.add(cityCombo);
		formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		formPanel.add(submitBtn);

		mainPanel.add(formPanel);
	}

	private JComboBox<String> createStyledComboBox() {
		String[] cities = { "Select City", "Austin", "Texas", "Raleigh, North Carolina", "Kansas City, Missouri",
				"New York City, Brooklyn", "Manhattan - New York City" };
		JComboBox<String> combo = new JComboBox<>(cities);
		combo.setMaximumSize(new Dimension(300, 40));
		combo.setAlignmentX(Component.CENTER_ALIGNMENT);
		combo.setBorder(
				BorderFactory.createCompoundBorder(new LineBorder(Color.LIGHT_GRAY), new EmptyBorder(5, 5, 5, 5)));
		return combo;
	}

	private JTextField createStyledTextField(String placeHolder) {
		JTextField field = new JTextField(20);
		field.setFont(new Font("Arial", Font.PLAIN, 16));
		field.setForeground(Color.LIGHT_GRAY);
		field.setBorder(
				BorderFactory.createCompoundBorder(new LineBorder(Color.LIGHT_GRAY), new EmptyBorder(10, 10, 10, 10)));
		field.setMaximumSize(new Dimension(300, 40));
		field.setAlignmentX(Component.CENTER_ALIGNMENT);
		setPlaceHolder(field, placeHolder);
		return field;
	}

	private void setPlaceHolder(final JTextComponent textComponent, String placeholder) {
		textComponent.setText(placeholder);
		textComponent.setForeground(Color.GRAY);

		textComponent.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (textComponent.getText().equals(placeholder)) {
					textComponent.setText("");
					textComponent.setForeground(Color.DARK_GRAY);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (textComponent.getText().isEmpty()) {
					textComponent.setText(placeholder);
					textComponent.setForeground(Color.GRAY);
				}
			}
		});
	}

	private void addFormField(JPanel formPanel, String labelText) {
		JPanel fieldPanel = new JPanel();
		fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.Y_AXIS));
		fieldPanel.setOpaque(false);
		fieldPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel label = new JLabel(labelText);
		label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		fieldPanel.add(label);

		JTextField textField = new JTextField(20);
		textField.setMaximumSize(new Dimension(200, 30));
		textField.setAlignmentX(Component.CENTER_ALIGNMENT);
		fieldPanel.add(textField);

		formPanel.add(fieldPanel);
		formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
	}

	private ArrayList<Plan> createPlans() {
		ArrayList<Plan> plans = new ArrayList<>();

		plans.add(new Plan("Basic", 49.99, "500GB", "100 Mbps", "30 Days",
				new ArrayList<>(Arrays.asList("Peacock", "Amazon Prime Basic")), new Color(135, 206, 235)));
		plans.add(new Plan("Standard", 69.99, "1000GB", "300 Mbps", "30 Days",
				new ArrayList<>(Arrays.asList("Peacock", "Amazon Prime", "Hulu Standard")), new Color(100, 149, 237)));
		plans.add(new Plan("Premium", 89.99, "2000GB", "500 Mbps", "30 Days",
				new ArrayList<>(Arrays.asList("Peacock", "Amazon Prime", "Hulu Premium", "HBO Max")),
				new Color(65, 105, 225)));
		plans.add(new Plan("Ultra", 119.99, "4000GB", "1 Gbps", "30 Days",
				new ArrayList<>(Arrays.asList("Peacock", "Amazon Prime", "Hulu Premium", "HBO Max", "ESPN+")),
				new Color(0, 0, 139)));
		plans.add(new Plan("Gamer Pro", 149.99, "Unlimited", "2 Gbps", "30 Days",
				new ArrayList<>(Arrays.asList("All Streaming Apps", "Gaming Server Priority", "Cloud Gaming")),
				NVIDIA_GREEN));

		return plans;
	}

	private void addPlans(JPanel mainPanel) {
		JPanel plansPanel = new JPanel();
		plansPanel.setLayout(new GridLayout(0, 3, 20, 20));
		plansPanel.setOpaque(false);
		plansPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 30, 10));

		ArrayList<Plan> plans = createPlans();
		ButtonGroup planGroup = new ButtonGroup();

		for (int i = 0; i < plans.size(); i++) {
			Plan plan = plans.get(i);

			RoundedPanel planCard = new RoundedPanel(Color.WHITE);
			planCard.setLayout(new BoxLayout(planCard, BoxLayout.Y_AXIS));
			planCard.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
			// Plan type header
			JLabel typeLabel = new JLabel(plan.planType);
			typeLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
			typeLabel.setForeground(plan.themeColor);
			typeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
			// Price
			JLabel priceLabel = new JLabel("\u0024" + String.format("%.2f", plan.planPrice) + "/month");
			priceLabel.setFont(PRICE_FONT);
			priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
			// Features panel
			JPanel featuresPanel = new JPanel();
			featuresPanel.setLayout(new BoxLayout(featuresPanel, BoxLayout.Y_AXIS));
			featuresPanel.setOpaque(false);
			addFeature(featuresPanel, "\uD83D\uDE80 " + plan.speed);
			addFeature(featuresPanel, "\uD83D\uDCCA " + plan.planData);
			addFeature(featuresPanel, "\u23F1\uFE0F " + plan.planDuration);
			addFeature(featuresPanel, "\uD83D\uDCF1 Apps: " + String.join(", ", plan.appSubscriptions));
			// Radio button
			JRadioButton radioBtn = new JRadioButton("Select Plan");
			radioBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
			radioBtn.setForeground(plan.themeColor);
			radioBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					selectedPlan = plan;
				}
			});
			planGroup.add(radioBtn);
			// Add components to card
			planCard.add(typeLabel);
			planCard.add(Box.createRigidArea(new Dimension(0, 10)));
			planCard.add(priceLabel);
			planCard.add(Box.createRigidArea(new Dimension(0, 15)));
			planCard.add(featuresPanel);
			planCard.add(Box.createRigidArea(new Dimension(0, 15)));
			planCard.add(radioBtn);
			plansPanel.add(planCard);
		}

		mainPanel.add(plansPanel);

	}

	private void addFeature(JPanel panel, String feature) {
		JLabel title = new JLabel(feature);
		title.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		title.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
		panel.add(title);
	}

}

class RoundedPanel extends JPanel {
	private Color backgroundColor;
	private int radius = 15;

	public RoundedPanel(Color backgroundColor) {

		this.backgroundColor = backgroundColor;
		setOpaque(false);
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2.setColor(backgroundColor);
		g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radius, radius));

	}

}

class GradientPanel extends JPanel {
	private Image backgroundImage;

	public GradientPanel(String imagePath) {
		java.net.URL imageURL = getClass().getResource(imagePath);

		if (imageURL != null) {
			backgroundImage = new ImageIcon(imageURL).getImage();
		} else {
			System.err.println("Image Not Found " + imagePath);
		}

		setOpaque(false);
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

		if (backgroundImage != null) {
			g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
		}

		int w = getWidth();
		int h = getHeight();
		Color color1 = new Color(76, 161, 175, 200); 
		Color color2 = new Color(196, 224, 229, 200); 
		GradientPaint gp = new GradientPaint(0, 0, color1, w, h, color2);
		g2.setPaint(gp);
		g2.fillRect(0, 0, w, h);
	}

}

class Plan {
	String planType;
	String planData;
	String speed;
	String planDuration;
	double planPrice;
	ArrayList<String> appSubscriptions;
	Color themeColor;

	public Plan(String planType, double planPrice, String planData, String speed, String planDuration,
			ArrayList<String> appSubscriptions, Color themeColor) {

		this.planType = planType;
		this.planData = planData;
		this.speed = speed;
		this.planDuration = planDuration;
		this.planPrice = planPrice;
		this.appSubscriptions = appSubscriptions;
		this.themeColor = themeColor;
	}

}
