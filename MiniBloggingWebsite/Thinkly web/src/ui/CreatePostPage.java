package ui;

import backend.*;
import javax.swing.*;
import java.awt.*;

public class CreatePostPage extends JFrame {

    private JTextField txtTitle;
    private JTextArea txtContent;
    private JButton btnPublish;

    public CreatePostPage() {
        init();
    }

    private void init() {
        setTitle("Create Post");
        setSize(650, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout());
        getContentPane().setBackground(Theme.BACKGROUND);

        // ---------- HEADER ----------
        JLabel header = new JLabel("Create a New Post", SwingConstants.CENTER);
        header.setFont(Theme.HEADER_FONT);
        header.setForeground(Theme.TEXT);
        header.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(header, BorderLayout.NORTH);

        // ---------- CENTER PANEL ----------
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Theme.BACKGROUND);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 12, 10, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // TITLE LABEL 
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        p.add(createLabel("Title:"), gbc);

        // TITLE FIELD
        gbc.gridx = 1;
        gbc.weightx = 1;
        txtTitle = new JTextField(25);
        txtTitle.setFont(Theme.BODY_FONT);
        txtTitle.setBackground(Color.WHITE);
        txtTitle.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.PASTEL_BLUE, 1),
                BorderFactory.createEmptyBorder(6, 6, 6, 6)
        ));
        p.add(txtTitle, gbc);

        // CONTENT LABEL
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NORTH;
        p.add(createLabel("Content:"), gbc);

        // CONTENT AREA (WIDE + TALL)
        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;

        txtContent = new JTextArea();
        txtContent.setFont(Theme.BODY_FONT);
        txtContent.setLineWrap(true);
        txtContent.setWrapStyleWord(true);
        txtContent.setBackground(Color.WHITE);

        txtContent.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.PASTEL_GREEN, 1),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));

        txtContent.setPreferredSize(new Dimension(450, 280)); // makes it wide + tall

        JScrollPane sp = new JScrollPane(txtContent);
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        p.add(sp, gbc);

        add(p, BorderLayout.CENTER);

        // ---------- BOTTOM BUTTON ----------
        JPanel bottom = new JPanel();
        bottom.setBackground(Theme.BACKGROUND);

        btnPublish = new JButton("Publish Post");
        btnPublish.setFont(Theme.BODY_FONT);
        btnPublish.setBackground(Theme.PASTEL_BLUE);
        btnPublish.setForeground(Color.WHITE);
        btnPublish.setFocusPainted(false);
        btnPublish.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        bottom.add(btnPublish);
        add(bottom, BorderLayout.SOUTH);

        btnPublish.addActionListener(e -> publishPost());
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(Theme.BODY_FONT);
        lbl.setForeground(Theme.TEXT);
        return lbl;
    }

    private void publishPost() {
        String title = txtTitle.getText().trim();
        String content = txtContent.getText().trim();

        if (title.isEmpty() || content.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Title and Content cannot be empty.");
            return;
        }

        if (Session.userId < 1) {
            JOptionPane.showMessageDialog(this, "Please login first.");
            return;
        }

        User author = UserFactory.createUser(
                "normal",
                Session.username != null ? Session.username : "",
                Session.email != null ? Session.email : "",
                ""
        );

        Post post = new Post(title, content, author);
        PostDAO pdao = new PostDAO();

        boolean ok = pdao.insertPost(post, Session.userId);

        if (ok) {
            JOptionPane.showMessageDialog(this, "Post created successfully!");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Error creating post.");
        }
    }
}
