package ui;

import backend.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.List;

public class ViewPostsPage extends JFrame {
    private JTable table;
    private DefaultTableModel model;

    public ViewPostsPage() { init(); loadPosts(); }

    private void init() {
        setTitle("Thinkly — Posts");
        setSize(820, 520);
        setMinimumSize(new Dimension(700, 460));
        setLocationRelativeTo(null);
        getContentPane().setBackground(Theme.BACKGROUND);
        setLayout(new BorderLayout(8,8));

        JLabel header = new JLabel("All posts", SwingConstants.LEFT);
        header.setFont(Theme.HEADER_FONT);
        header.setBorder(BorderFactory.createEmptyBorder(12,12,6,12));
        header.setForeground(Theme.TEXT);
        add(header, BorderLayout.NORTH);

        model = new DefaultTableModel(new Object[]{"Title", "Content", "Author"}, 0) {
            // make table read-only
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(model);
        table.setFont(Theme.BODY_FONT);
        table.setRowHeight(28);
        table.getTableHeader().setFont(Theme.BODY_FONT);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(new CompoundBorder(new EmptyBorder(8,8,8,8), new LineBorder(new Color(220,220,220))));

        add(sp, BorderLayout.CENTER);
    }

    private void loadPosts() {
        PostDAO pdao = new PostDAO();
        List<Post> posts = pdao.getAllPosts(); // unchanged backend
        model.setRowCount(0);
        for (Post p : posts) {
            model.addRow(new Object[]{ p.getTitle(), p.getContent(), p.getAuthor().getUsername() });
        }
    }
}
