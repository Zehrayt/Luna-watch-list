
package main;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;

import static main.StreamingAppUI.*;

public class FilmCategoryDrawerDialog extends JDialog {

    private Frame ownerFrame;
    private Map<String, List<String>> movieDataByCategory; 

    public FilmCategoryDrawerDialog(Frame owner, String title, Map<String, List<String>> movieData) {
        super(owner, title, false); 
        this.ownerFrame = owner;
        this.movieDataByCategory = movieData;

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setBackground(PANEL_ITEM_BG_COLOR);
        setLayout(new BorderLayout(0, 10));
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(20, 15, 20, 15));

        JLabel drawerTitleLabel = new JLabel("Movie Categories");
        drawerTitleLabel.setFont(TITLE_FONT.deriveFont(20f));
        drawerTitleLabel.setForeground(TEXT_COLOR);
        drawerTitleLabel.setHorizontalAlignment(SwingConstants.LEFT);
        drawerTitleLabel.setBorder(new CompoundBorder(
                new MatteBorder(0,0,1,0, BORDER_COLOR_SUBTLE),
                new EmptyBorder(0,5,10,0)
        ));
        add(drawerTitleLabel, BorderLayout.NORTH);

        JPanel categoriesListPanel = new JPanel();
        categoriesListPanel.setBackground(getContentPane().getBackground());
        categoriesListPanel.setLayout(new BoxLayout(categoriesListPanel, BoxLayout.Y_AXIS));
        
        String[] filmCategories = {
            "All Movies", "Science Fiction", "Adventure",
            "Family", "Drama", "Music",
            "Action", "Fantastic", "Romantic Comedy",
            "Animation", "Thriller", "Turkish Film",
            "Love", "Comedy", "Life",
            "Documentary", "Horror","sdf","asd","wer","asdz","dff"
        };

        for (String category : filmCategories) {
            categoriesListPanel.add(createCategoryItemPanel(category));
            categoriesListPanel.add(Box.createRigidArea(new Dimension(0, 5))); 
        }
        
        if (categoriesListPanel.getComponentCount() > 0) {
            categoriesListPanel.remove(categoriesListPanel.getComponentCount() - 1);
        }


        JScrollPane scrollPane = new JScrollPane(categoriesListPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(getContentPane().getBackground());
        scrollPane.getVerticalScrollBar().setUnitIncrement(12);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        StreamingAppUI.styleScrollBar(scrollPane.getVerticalScrollBar());
        add(scrollPane, BorderLayout.CENTER);
        

        int drawerWidth = 280; 
        setSize(drawerWidth, owner.getHeight() - owner.getInsets().top - owner.getInsets().bottom - ((StreamingAppUI)owner).getTopNavBarHeight());
        Point ownerLocation = owner.getLocationOnScreen();
        int navBarHeight = ((StreamingAppUI)owner).getTopNavBarHeight();
        setLocation(ownerLocation.x, ownerLocation.y + navBarHeight);
    }

    private JPanel createCategoryItemPanel(String categoryName) {
    	final String currentCategoryName = categoryName;
    	
    	JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10,0));
        itemPanel.setBackground(getContentPane().getBackground());
        itemPanel.setBorder(new EmptyBorder(8, 10, 8, 10));
        itemPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        itemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JLabel categoryLabel = new JLabel(currentCategoryName);
        categoryLabel.setFont(DEFAULT_FONT.deriveFont(14f)); 
        categoryLabel.setForeground(TEXT_COLOR);
        itemPanel.add(categoryLabel);

        itemPanel.addMouseListener(new MouseAdapter() {
            private final Color originalBackground = itemPanel.getBackground();

            @Override
            public void mouseEntered(MouseEvent e) {
                itemPanel.setBackground(PANEL_ITEM_HOVER_BG_COLOR);
                categoryLabel.setForeground(ACCENT_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                itemPanel.setBackground(originalBackground);
                categoryLabel.setForeground(TEXT_COLOR);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Film category clicked in drawer: " + currentCategoryName);
                if (movieDataByCategory == null) {
                    System.err.println("ERROR: movieDataByCategory is NULL in FilmCategoryDrawerDialog!");
                    JOptionPane.showMessageDialog(FilmCategoryDrawerDialog.this, "Error: Movie data not loaded.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                List<String> items = movieDataByCategory.getOrDefault(currentCategoryName, List.of("No movies available for " + currentCategoryName));
                System.out.println("Movies for " + currentCategoryName + ": " + items.size());

                CategoryContentDialog contentDialog = new CategoryContentDialog(ownerFrame, currentCategoryName, items, "Movies");
                contentDialog.setVisible(true);
                dispose();
            }
        
        });
        return itemPanel;
    }
}