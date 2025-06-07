package main;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static main.StreamingAppUI.*;

public class SportsCategoryDrawerDialog extends JDialog {

    private Frame ownerFrame;

    public SportsCategoryDrawerDialog(Frame owner, String title) {
        super(owner, title, false);
        this.ownerFrame = owner;

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setBackground(PANEL_ITEM_BG_COLOR);
        setLayout(new BorderLayout(0, 10));
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(20, 15, 20, 15));

        JLabel drawerTitleLabel = new JLabel("Sports Categories");
        drawerTitleLabel.setFont(TITLE_FONT.deriveFont(20f));
        drawerTitleLabel.setForeground(TEXT_COLOR);
        drawerTitleLabel.setHorizontalAlignment(SwingConstants.LEFT);
        drawerTitleLabel.setBorder(new CompoundBorder(
                new MatteBorder(0, 0, 1, 0, BORDER_COLOR_SUBTLE),
                new EmptyBorder(0, 5, 10, 0)
        ));
        add(drawerTitleLabel, BorderLayout.NORTH);

        JPanel categoriesListPanel = new JPanel();
        categoriesListPanel.setBackground(getContentPane().getBackground());
        categoriesListPanel.setLayout(new BoxLayout(categoriesListPanel, BoxLayout.Y_AXIS));

        List<String> sportCategoriesFromDB = getSportsGenresFromDB(); // Bu metot artık sınıfın içinde tanımlı olmalı

        if (sportCategoriesFromDB.isEmpty()) {
            JLabel noCategoriesLabel = new JLabel("No sports categories found.");
            noCategoriesLabel.setFont(DEFAULT_FONT);
            noCategoriesLabel.setForeground(TEXT_COLOR);
            noCategoriesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            categoriesListPanel.add(Box.createVerticalGlue());
            categoriesListPanel.add(noCategoriesLabel);
            categoriesListPanel.add(Box.createVerticalGlue());
        } else {
            for (String category : sportCategoriesFromDB) {
                categoriesListPanel.add(createCategoryItemPanel(category));
                categoriesListPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            }
            if (categoriesListPanel.getComponentCount() > 1) { 
                if (categoriesListPanel.getComponent(categoriesListPanel.getComponentCount() -1) instanceof Box.Filler) {
                    categoriesListPanel.remove(categoriesListPanel.getComponentCount() - 1);
                }
            }
        }

        JScrollPane scrollPane = new JScrollPane(categoriesListPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(getContentPane().getBackground());
        scrollPane.getVerticalScrollBar().setUnitIncrement(12);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // Eklendi
        StreamingAppUI.styleScrollBar(scrollPane.getVerticalScrollBar());
        add(scrollPane, BorderLayout.CENTER);

        int drawerWidth = 280;
        setSize(drawerWidth, owner.getHeight() - owner.getInsets().top - owner.getInsets().bottom - ((StreamingAppUI) owner).getTopNavBarHeight());
        Point ownerLocation = owner.getLocationOnScreen();
        int navBarHeight = ((StreamingAppUI) owner).getTopNavBarHeight();
        setLocation(ownerLocation.x, ownerLocation.y + navBarHeight);
    }
    
    private List<String> getSportsGenresFromDB() {
        List<String> sportsGenres = new ArrayList<>();
        String sql = "SELECT genre_name FROM genres WHERE genre_name IN " +
                     "('Football', 'Basketball', 'Motorsports', 'Tennis', 'Volleyball', " +
                     "'Athletics', 'Wrestling', 'Boxing', 'Martial Arts', 'Cycling', " +
                     "'Skiing', 'Snowboarding', 'Ice Hockey', 'Golf', 'Skateboarding', 'Surfing') " +
                     "ORDER BY genre_name ASC";
     
        System.out.println("Executing SQL for sports genres: " + sql);

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (conn == null) {
                System.err.println("getSportsGenresFromDB: Database connection is NULL!");
                return sportsGenres;
            }

            while (rs.next()) {
                String genreName = rs.getString("genre_name");
                sportsGenres.add(genreName);
                System.out.println("Found sport genre: " + genreName);
            }
        } catch (SQLException e) {
            System.err.println("SQL error while loading sports genres:");
            e.printStackTrace();
        } catch (NullPointerException npe) {
            System.err.println("NullPointerException in getSportsGenresFromDB, connection might be null before prepareStatement:");
            npe.printStackTrace();
        }
        System.out.println("Total sports genres loaded: " + sportsGenres.size());
        return sportsGenres;
    }


    private List<String> getSportsContentForGenreFromDB(String sportGenreName) {
        List<String> sportItemTitles = new ArrayList<>();
        String sql = "SELECT ci.title " +
                     "FROM content_items ci " +
                     "JOIN content_item_genres cig ON ci.content_item_id = cig.content_item_id " +
                     "JOIN genres g ON cig.genre_id = g.genre_id " +
                     "JOIN content_types ct ON ci.content_type_id = ct.content_type_id " +
                     "WHERE g.genre_name = ? AND (ct.type_name = 'SportsEvent' OR ct.type_name = 'SportsShow') " +
                     "ORDER BY ci.title ASC";
        System.out.println("Executing SQL for sports content for genre '" + sportGenreName + "': " + sql);

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (conn == null) {
                System.err.println("getSportsContentForGenreFromDB: Database connection is NULL!");
                sportItemTitles.add("Error: Database connection issue.");
                return sportItemTitles;
            }

            pstmt.setString(1, sportGenreName);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String title = rs.getString("title");
                    sportItemTitles.add(title);
                    System.out.println("Found sport content: " + title);
                }
            }
            if (sportItemTitles.isEmpty()) {
                System.out.println("No sport content found for genre: " + sportGenreName);
                sportItemTitles.add("No content available for " + sportGenreName);
            }

        } catch (SQLException e) {
            System.err.println("SQL error while loading sports content for genre '" + sportGenreName + "':");
            e.printStackTrace();
            sportItemTitles.add("Error loading content for " + sportGenreName);
        } catch (NullPointerException npe) {
            System.err.println("NullPointerException in getSportsContentForGenreFromDB, connection might be null before prepareStatement:");
            npe.printStackTrace();
            sportItemTitles.add("Error: Internal connection problem.");
        }
        System.out.println("Total sport items loaded for " + sportGenreName + ": " + sportItemTitles.size());
        return sportItemTitles;
    }

    private JPanel createCategoryItemPanel(String sportGenreName) {
        final String currentSportGenre = sportGenreName;

        JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        itemPanel.setBackground(getContentPane().getBackground());
        itemPanel.setBorder(new EmptyBorder(8, 10, 8, 10));
        itemPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        itemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JLabel categoryLabel = new JLabel(currentSportGenre);
        categoryLabel.setFont(DEFAULT_FONT.deriveFont(15f));
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
                System.out.println("Sports category clicked in drawer: " + currentSportGenre);
                List<String> sportItems = getSportsContentForGenreFromDB(currentSportGenre); 

                CategoryContentDialog contentDialog = new CategoryContentDialog(ownerFrame, currentSportGenre, sportItems, "Sports");
                contentDialog.setVisible(true);
                dispose();
            }
        });
        return itemPanel;
    }

} 