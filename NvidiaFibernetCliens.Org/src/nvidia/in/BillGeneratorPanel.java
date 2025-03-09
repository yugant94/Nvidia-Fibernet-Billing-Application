package nvidia.in;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class BillGeneratorPanel extends JPanel {
	private JTextField accountIdField, planBillField, dueFineField, stateTaxField, totalAmountField;
	private JButton calculateBillButton, generateBillButton;

	public BillGeneratorPanel() {
	    setLayout(new BorderLayout());
	    setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50)); 

	    JPanel mainPanel = new JPanel();
//	    mainPanel.setBorder(BorderFactory.createLineBorder(Color.blue, 1));
	    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
	    mainPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

	    JPanel formWrapperPanel = new JPanel();
	    formWrapperPanel.setLayout(new BoxLayout(formWrapperPanel, BoxLayout.Y_AXIS));
	    formWrapperPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); 
	    formWrapperPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

	    JPanel formPanel = new JPanel();
	    formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
//	    formPanel.setBorder(BorderFactory.createLineBorder(Color.green, 2));
	    formPanel.setPreferredSize(new Dimension(550, 350)); 
	    formPanel.setMaximumSize(new Dimension(500, 500));  

	    addFormRow("Account Id:", accountIdField = createTextField(), formPanel);
	    formPanel.add(Box.createVerticalStrut(25));
	    addFormRow("Plan Bill (Monthly):", planBillField = createTextField(), formPanel);
	    formPanel.add(Box.createVerticalStrut(25));
	    addFormRow("Due Fine ($):", dueFineField = createTextField(), formPanel);
	    formPanel.add(Box.createVerticalStrut(25));
	    addFormRow("State Tax:", stateTaxField = createTextField(), formPanel);
	    formPanel.add(Box.createVerticalStrut(25));
	    addFormRow("Total Amount ($):", totalAmountField = createTextField(), formPanel);

	    JPanel buttonPanel = new JPanel();
	    buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
	    calculateBillButton = createButton("Calculate Bill");
	    generateBillButton = createButton("Generate Bill");

	    buttonPanel.add(calculateBillButton);
	    buttonPanel.add(generateBillButton);

	    formWrapperPanel.add(formPanel);

	    mainPanel.add(Box.createVerticalGlue());
	    mainPanel.add(formWrapperPanel); 
	    mainPanel.add(Box.createVerticalStrut(20));
	    mainPanel.add(buttonPanel);
	    mainPanel.add(Box.createVerticalGlue());

	    add(mainPanel, BorderLayout.CENTER);

	    calculateBillButton.addActionListener(e -> calculateBill());
	    generateBillButton.addActionListener(e -> generateBill());
	}

	

	
	private void addFormRow(String labelText, JComponent component, JPanel formPanel) {
        JPanel rowPanel = new JPanel();
        rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.X_AXIS));
        rowPanel.setOpaque(false);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(Color.black);
        label.setPreferredSize(new Dimension(150, 25));
        rowPanel.add(label);
        rowPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        rowPanel.add(component);
        formPanel.add(rowPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
    }

	private JLabel createLabel(String text) {
		JLabel label = new JLabel(text, SwingConstants.RIGHT);
		label.setFont(new Font("Arial", Font.BOLD, 18));
		return label;
	}

	private JTextField createTextField() {
		JTextField field = new JTextField();
		field.setFont(new Font("Arial", Font.PLAIN, 18));
		field.setPreferredSize(new Dimension(250, 40));
		return field;
	}

	private JButton createButton(String text) {
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


        return button;
    }

	private void calculateBill() {
		double dueFine;
		double tax;
		try {
			String accountText = accountIdField.getText().trim();

			if (accountText.isEmpty() || !accountText.matches("\\d+")) {
				JOptionPane.showMessageDialog(this, "Please enter a valid numeric account number!", "Input Error",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			long accountNo = Long.parseLong(accountText); 

			ConnectionJDBC con = new ConnectionJDBC();

			dueFine = dueFineField.getText().trim().isEmpty() ? 0.0 : Double.parseDouble(dueFineField.getText().trim());
			tax = stateTaxField.getText().trim().isEmpty() ? 0.0 : Double.parseDouble(stateTaxField.getText().trim());

			String query2 = "SELECT planPrice FROM broaddband_plans WHERE accountNo =" + accountNo + "";
			ResultSet rs2 = con.s.executeQuery(query2);

			if (rs2.next()) {
				double planPrice = rs2.getDouble("planPrice");
				double totalAmount = planPrice + dueFine + tax;

				planBillField.setText(String.valueOf(planPrice));
				totalAmountField.setText(String.valueOf(totalAmount));
			} else {
				JOptionPane.showMessageDialog(this, "Account number not found!", "Error", JOptionPane.ERROR_MESSAGE);
			}

			rs2.close();
			con.s.close();
		} catch (NumberFormatException e) {
			e.printStackTrace(); // Debugging
			JOptionPane.showMessageDialog(this, "Please enter a valid numeric account number!",
					"Number Format Exception Error", JOptionPane.WARNING_MESSAGE);
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Database error while fetching bill details!", "Database Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void generateBill() {
		double dueFine;
		double tax;
		try {
			double billAmount = Double.parseDouble(totalAmountField.getText().trim());
			long accountNo = Long.parseLong(accountIdField.getText().trim());

			dueFine = dueFineField.getText().trim().isEmpty() ? 0.0 : Double.parseDouble(dueFineField.getText().trim());
			tax = stateTaxField.getText().trim().isEmpty() ? 0.0 : Double.parseDouble(stateTaxField.getText().trim());

			LocalDate currentDate = LocalDate.now();

			ConnectionJDBC con = new ConnectionJDBC();

			String checkQuery = "SELECT COUNT(*) FROM bill WHERE accountNumber = " + accountNo;
			ResultSet rs = con.s.executeQuery(checkQuery);
			rs.next();
			int count = rs.getInt(1);
			rs.close();

			String query;
			if (count > 0) {
				query = "UPDATE bill SET billAmount = " + billAmount + ", dueFine = " + dueFine + ", tax = " + tax
						+ ", date = '" + currentDate + "'" + ", transactionId = NULL" + 
						", paymentType = ''" + 
						" WHERE accountNumber = " + accountNo;

				int rowsAffected = con.s.executeUpdate(query);

				if (rowsAffected > 0) {
					JOptionPane.showMessageDialog(this, "Bill Updated Successfully for Account Number: " + accountNo);
				} else {
					JOptionPane.showMessageDialog(this, "Error updating bill. Please try again.", "Update Error",
							JOptionPane.ERROR_MESSAGE);
				}

			} else {
				query = "INSERT INTO bill (accountNumber, billAmount, dueFine, tax, date) VALUES (" + accountNo + ", "
						+ billAmount + ", " + dueFine + ", " + tax + ", '" + currentDate + "')";

				int rowsInserted = con.s.executeUpdate(query);

				if (rowsInserted > 0) {
					JOptionPane.showMessageDialog(this,
							"New Bill Generated Successfully for Account Number: " + accountNo);
				} else {
					JOptionPane.showMessageDialog(this, "Error generating bill. Check account details.",
							"Insertion Error", JOptionPane.ERROR_MESSAGE);
				}
			}

			con.s.close();

			accountIdField.setText("");
			planBillField.setText("");
			dueFineField.setText("");
			stateTaxField.setText("");
			totalAmountField.setText("");

		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "No bill calculated to generate!", "Error",
					JOptionPane.WARNING_MESSAGE);
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Database error while assigning bill!", "Database Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

}
