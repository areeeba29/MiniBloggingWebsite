package ui;

import backend.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.sql.*;

public class LoginPage extends JFrame {
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnSignup;

    public LoginPage() { init(); }

    private void init() {
        setTitle("Thinkly — Login");
        setSize(460, 360);
        setMinimumSize(new Dimension(420, 340));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Theme.BACKGROUND);
        setLayout(new BorderLayout());

        // Header
        JLabel header = new JLabel("Welcome to Thinkly", SwingConstants.CENTER);
        header.setFont(Theme.HEADER_FONT);
        header.setForeground(Theme.TEXT);
        header.setBorder(BorderFactory.createEmptyBorder(18, 12, 8, 12));
        add(header, BorderLayout.NORTH);

        // Card panel (center)
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(new EmptyBorder(16,16,16,16),
                new LineBorder(new Color(220,220,220), 1, true)));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 6, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Email label + field
        JLabel lblEmail = new JLabel("Email address");
        lblEmail.setFont(Theme.BODY_FONT);
        lblEmail.setForeground(Theme.TEXT);
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.0; card.add(lblEmail, gbc);

        txtEmail = new JTextField(20);
        txtEmail.setFont(Theme.BODY_FONT);
        txtEmail.setToolTipText("Enter your registered email (for example: you@example.com)");
        txtEmail.setName("emailField");
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 1.0;
        card.add(txtEmail, gbc);

        // Password label + field
        JLabel lblPass = new JLabel("Password");
        lblPass.setFont(Theme.BODY_FONT);
        lblPass.setForeground(Theme.TEXT);
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.0; card.add(lblPass, gbc);

        txtPassword = new JPasswordField(20);
        txtPassword.setFont(Theme.BODY_FONT);
        txtPassword.setToolTipText("Your account password (min 5 characters)");
        txtPassword.setName("passwordField");
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 1.0;
        card.add(txtPassword, gbc);

        // Make Enter key from password trigger login
        txtPassword.addActionListener(e -> loginAction());

        // Buttons row
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        buttons.setBackground(Color.WHITE);

        btnLogin = new JButton("Login");
        styleButton(btnLogin, Theme.PASTEL_BLUE);
        btnLogin.setMnemonic('L');
        btnLogin.setToolTipText("Sign in to your account (Alt+L)");

        btnSignup = new JButton("Sign up");
        styleButton(btnSignup, Theme.PASTEL_PINK);
        btnSignup.setMnemonic('S');
        btnSignup.setToolTipText("Create a new account (Alt+S)");

        buttons.add(btnLogin);
        buttons.add(btnSignup);

        gbc.gridx = 0; gbc.gridy = 4; gbc.insets = new Insets(14,10,10,10);
        card.add(buttons, gbc);

        // Accessibility: default focus
        txtEmail.requestFocusInWindow();

        add(card, BorderLayout.CENTER);

        // Action listeners
        btnLogin.addActionListener(e -> {
            // disable button to show progress & avoid double clicks
            btnLogin.setEnabled(false);
            loginAction();
            btnLogin.setEnabled(true);
        });

        btnSignup.addActionListener(e -> {
            new SignupPage().setVisible(true);
            dispose();
        });
    }

    private void styleButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Theme.TEXT);
        btn.setFont(Theme.BODY_FONT);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(8,16,8,16));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) { btn.setBackground(bg.darker()); }
            public void mouseExited(java.awt.event.MouseEvent evt) { btn.setBackground(bg); }
        });
    }

    private void loginAction() {
        String email = txtEmail.getText().trim();
        String pass = new String(txtPassword.getPassword());

        // validation + feedback
        if (email.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Both fields are required.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // call existing backend (unchanged)
        UserDAO udao = new UserDAO();
        boolean ok = udao.checkLogin(email, pass);

        if (ok) {
            int id = udao.getUserId(email);
            String username = fetchUsernameByEmail(email);
            Session.set(id, username, email);
            JOptionPane.showMessageDialog(this, "Login successful — welcome " + (username != null ? username : "") + "!");
            new HomePage().setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid email or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String fetchUsernameByEmail(String email) {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT username FROM users WHERE email=?");
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("username");
        } catch (SQLException ex) { System.out.println("fetchUsername error: " + ex.getMessage()); }
        return "";
    }
}
