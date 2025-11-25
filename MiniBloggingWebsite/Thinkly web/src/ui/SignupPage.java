package ui;

import backend.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class SignupPage extends JFrame {
    private JTextField txtName, txtEmail;
    private JPasswordField txtPassword;
    private JButton btnCreate, btnBack;

    public SignupPage() { init(); }

    private void init() {
        setTitle("Thinkly — Signup");
        setSize(480, 420);
        setMinimumSize(new Dimension(420, 380));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Theme.BACKGROUND);
        setLayout(new BorderLayout());

        JLabel header = new JLabel("Create your Thinkly account", SwingConstants.CENTER);
        header.setFont(Theme.HEADER_FONT);
        header.setForeground(Theme.TEXT);
        header.setBorder(BorderFactory.createEmptyBorder(16, 12, 12, 12));
        add(header, BorderLayout.NORTH);

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(new EmptyBorder(14,14,14,14), new LineBorder(new Color(220,220,220),1,true)));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 6, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblName = new JLabel("Username");
        lblName.setFont(Theme.BODY_FONT); lblName.setForeground(Theme.TEXT);
        gbc.gridx = 0; gbc.gridy = 0; card.add(lblName, gbc);

        txtName = new JTextField(20);
        txtName.setFont(Theme.BODY_FONT);
        txtName.setToolTipText("Choose a display name for your account");
        gbc.gridx = 0; gbc.gridy = 1; card.add(txtName, gbc);

        JLabel lblEmail = new JLabel("Email address");
        lblEmail.setFont(Theme.BODY_FONT); lblEmail.setForeground(Theme.TEXT);
        gbc.gridx = 0; gbc.gridy = 2; card.add(lblEmail, gbc);

        txtEmail = new JTextField(20);
        txtEmail.setFont(Theme.BODY_FONT);
        txtEmail.setToolTipText("You will use this to login");
        gbc.gridx = 0; gbc.gridy = 3; card.add(txtEmail, gbc);

        JLabel lblPass = new JLabel("Password (min 5 chars)");
        lblPass.setFont(Theme.BODY_FONT); lblPass.setForeground(Theme.TEXT);
        gbc.gridx = 0; gbc.gridy = 4; card.add(lblPass, gbc);

        txtPassword = new JPasswordField(20);
        txtPassword.setFont(Theme.BODY_FONT);
        txtPassword.setToolTipText("Choose a strong password");
        gbc.gridx = 0; gbc.gridy = 5; card.add(txtPassword, gbc);

        // Buttons
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        btnRow.setBackground(Color.WHITE);

        btnCreate = new JButton("Create account");
        styleButton(btnCreate, Theme.PASTEL_GREEN);
        btnCreate.setToolTipText("Create the account and go to login");

        btnBack = new JButton("Back to login");
        styleButton(btnBack, Theme.PASTEL_PINK);
        btnBack.setToolTipText("Cancel and return to login");

        btnRow.add(btnCreate); btnRow.add(btnBack);

        gbc.gridx = 0; gbc.gridy = 6; gbc.insets = new Insets(14,10,10,10); card.add(btnRow, gbc);

        add(card, BorderLayout.CENTER);

        // Actions (backend logic unchanged)
        btnCreate.addActionListener(e -> {
            btnCreate.setEnabled(false);
            createAccount();
            btnCreate.setEnabled(true);
        });
        btnBack.addActionListener(e -> { new LoginPage().setVisible(true); dispose(); });
    }

    private void styleButton(JButton btn, Color bg) {
        btn.setBackground(bg); btn.setForeground(Theme.TEXT);
        btn.setFont(Theme.BODY_FONT);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(8,14,8,14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) { btn.setBackground(bg.darker()); }
            public void mouseExited(java.awt.event.MouseEvent evt) { btn.setBackground(bg); }
        });
    }

    private void createAccount() {
        String username = txtName.getText().trim();
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            JOptionPane.showMessageDialog(this, "Please provide a valid email address.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (password.length() < 5) {
            JOptionPane.showMessageDialog(this, "Password must be at least 5 characters.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        UserDAO udao = new UserDAO();
        if (udao.checkEmailExists(email)) {
            JOptionPane.showMessageDialog(this, "Email already exists — try logging in.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        User user = UserFactory.createUser("normal", username, email, password);
        if (udao.insertUser(user)) {
            JOptionPane.showMessageDialog(this, "Account created successfully — please login.");
            new LoginPage().setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Error creating account. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
