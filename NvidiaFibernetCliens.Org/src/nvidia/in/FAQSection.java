package nvidia.in;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FAQSection extends JFrame {
    private JButton lastOpenedQuestion = null;
    private JTextArea lastOpenedAnswer = null;

    public FAQSection() {
        setTitle("FAQ - Nvidia Fibernet");
        setBounds(500,100,600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel gradientPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int width = getWidth();
                int height = getHeight();
                Color color2 = new Color(192,118,118);
                Color color1 = new Color(178,176,176);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, height, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, width, height);
            }
        };
        gradientPanel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("FAQ - Frequently Asked Questions", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(titleLabel, BorderLayout.NORTH);
        
        JPanel faqPanel = new JPanel();
        faqPanel.setLayout(new BoxLayout(faqPanel, BoxLayout.Y_AXIS));
        faqPanel.setOpaque(false);

        String[][] faqs = {
        		{"Why is my internet speed slow?", "Try restarting your router and check if multiple devices are using bandwidth."},
                {"How do I check my data usage?", "Log in to your account dashboard to see real-time data usage."},
                {"How do I pay my bill?", "Go to the 'Pay Bills' section in your dashboard and choose a payment method."},
                {"Can I upgrade my plan?", "Yes, visit the 'Upgrade Plan' section in your account settings."},
                {"Why is my connection unstable?", "Ensure your router is in an open space and not obstructed by walls."},
                {"How do I reset my router?", "Press and hold the reset button for 10 seconds until the lights blink."},
                {"What should I do if my connection is down?", "Contact customer support or check for scheduled maintenance updates."},
                {"How do I change my Wi-Fi password?", "Log in to your router settings and update the password under security settings."},
                {"Are there any additional charges on late bill payments?", "Yes, a late fee may apply as per company policy."},
                {"How can I contact customer support?", "Call our helpline or use the live chat option on our website."},
                {"How do I set up auto-pay?", "Go to 'Billing Settings' and enable auto-pay with your preferred payment method."},
                {"Can I temporarily pause my connection?", "Yes, you can request a temporary hold on your account settings."},
                {"How do I get a refund for extra charges?", "Contact support with your billing details for verification and refund."},
                {"What happens if I exceed my data limit?", "Your speed may be reduced, or extra charges may apply based on your plan."},
                {"How do I switch to a different plan?", "Go to 'Plans & Subscriptions' and select the new plan you wish to switch to."},
                {"Why is my bill higher this month?", "Check your bill breakdown to see any additional usage or extra charges."},
                {"How do I update my contact details?", "Go to 'Profile Settings' and update your phone number or email."},
                {"Can I add multiple users to my plan?", "Yes, you can add additional users under 'Family Sharing' settings."},
                {"What is the fair usage policy?", "Our plans have an FUP limit after which speeds may be reduced."},
                {"How do I cancel my subscription?", "You can cancel your plan by raising a cancellation request under account settings."}
        };

        for (String[] faq : faqs) {
            JPanel card = new JPanel(new BorderLayout());
            card.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            card.setBackground(new Color(255, 255, 255, 200)); 

            JButton question = new JButton(faq[0]);
            question.setFocusPainted(false);
            question.setBackground(Color.WHITE);
            question.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(1, 1, 1, 1, Color.GRAY),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)));
            question.setFont(new Font("Segoe UI", Font.BOLD, 16));

            JTextArea answer = new JTextArea(faq[1]);
            answer.setWrapStyleWord(true);
            answer.setLineWrap(true);
            answer.setEditable(false);
            answer.setBackground(new Color(240, 240, 240));
            answer.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            answer.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            answer.setVisible(false);

            card.add(question, BorderLayout.NORTH);
            card.add(answer, BorderLayout.CENTER);

            question.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (lastOpenedAnswer != null && lastOpenedAnswer != answer) {
                        lastOpenedAnswer.setVisible(false);
                    }
                    answer.setVisible(!answer.isVisible());
                    lastOpenedAnswer = answer.isVisible() ? answer : null;
                    card.revalidate();
                    card.repaint();
                }
            });

            faqPanel.add(card);
            faqPanel.add(Box.createVerticalStrut(10)); 
        }

        JScrollPane scrollPane = new JScrollPane(faqPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        
        gradientPanel.add(scrollPane, BorderLayout.CENTER);
        add(gradientPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FAQSection::new);
    }
}
