package ui;

import backend.Session;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class HomePage extends JFrame {
    private JLabel lblWelcome;
    private JButton btnCreate, btnView, btnLogout;

    public HomePage() { init(); }

    private void init() {
        setTitle("Thinkly — Home");
        setSize(760, 460);
        setMinimumSize(new Dimension(680, 420));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Theme.BACKGROUND);
        setLayout(new BorderLayout(12,12));

        // Top bar
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Theme.PASTEL_BLUE);
        top.setBorder(new CompoundBorder(new EmptyBorder(12,12,12,12),
                new MatteBorder(0,0,2,0, new Color(220,220,220))));

        lblWelcome = new JLabel("Welcome, " + (Session.username != null ? Session.username : "Guest"));
        lblWelcome.setFont(Theme.HEADER_FONT);
        lblWelcome.setForeground(Theme.TEXT);
        top.add(lblWelcome, BorderLayout.WEST);

        add(top, BorderLayout.NORTH);

        // Center controls
        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12,12,12,12);

        btnCreate = new JButton("Create Post");
        styleButton(btnCreate, Theme.PASTEL_GREEN);
        btnCreate.setToolTipText("Write a new blog post");

        btnView = new JButton("View Posts");
        styleButton(btnView, Theme.PASTEL_BLUE);
        btnView.setToolTipText("See all posts");

        btnLogout = new JButton("Logout");
        styleButton(btnLogout, Theme.PASTEL_PINK);
        btnLogout.setToolTipText("Log out of Thinkly");

        gbc.gridx = 0; gbc.gridy = 0; center.add(btnCreate, gbc);
        gbc.gridx = 1; center.add(btnView, gbc);
        gbc.gridx = 2; center.add(btnLogout, gbc);

        add(center, BorderLayout.CENTER);

        // Action listeners keep logic unchanged
        btnCreate.addActionListener(e -> new CreatePostPage().setVisible(true));
        btnView.addActionListener(e -> new ViewPostsPage().setVisible(true));
        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to log out?", "Confirm logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                Session.clear();
                JOptionPane.showMessageDialog(this, "You have been logged out.");
                new LoginPage().setVisible(true);
                dispose();
            }
        });
    }

    private void styleButton(JButton btn, Color bg) {
        btn.setBackground(bg); btn.setForeground(Theme.TEXT);
        btn.setFont(Theme.BODY_FONT);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(10,18,10,18));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) { btn.setBackground(bg.darker()); }
            public void mouseExited(java.awt.event.MouseEvent evt) { btn.setBackground(bg); }
        });
    }
}
