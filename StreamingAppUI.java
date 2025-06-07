package main;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.plaf.basic.BasicProgressBarUI;
import javax.swing.plaf.basic.BasicScrollBarUI;

import org.mindrot.jbcrypt.BCrypt;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL; 
import java.sql.Connection;        
import java.sql.PreparedStatement; 
import java.sql.ResultSet;         
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays; 
import java.util.HashMap; 
import java.util.List;    
import java.util.Map; 


public class StreamingAppUI extends JFrame {
	
	public static final Color BACKGROUND_COLOR = new Color(0x1A1A1A);
    public static final Color TEXT_COLOR = Color.WHITE;
    public static final Color ACCENT_COLOR = new Color(0x00BFFF);
    public static final Color PANEL_ITEM_BG_COLOR = new Color(0x2C2C2C);
    public static final Color PANEL_ITEM_HOVER_BG_COLOR = new Color(0x3C3C3C);
    public static final Color BORDER_COLOR_SUBTLE = new Color(0x404040);
    
    private static final Color PANEL_BACKGROUND_COLOR = new Color(65, 65, 65); 
    private static final Color TEXT_COLOR_PRIMARY = new Color(220, 220, 220);
    private static final Color TEXT_COLOR_SECONDARY = new Color(180, 180, 180);
    private static final Color TEXT_COLOR_LINK = new Color(150, 180, 255);
    private static final Color BORDER_COLOR = new Color(100, 100, 100); 
    private static final Color BUTTON_BACKGROUND_COLOR = new Color(80, 80, 80); 
    private static final Color BUTTON_TEXT_COLOR = new Color(220, 220, 220);

    public static final Font DEFAULT_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font HERO_TITLE_FONT = new Font("Segoe UI Semibold", Font.BOLD, 48);
    public static final Font NAV_LINK_FONT = new Font("Segoe UI Semibold", Font.PLAIN, 15);
	
    private static final Font PROFILE_PAGE_TITLE_FONT = new Font("Segoe UI Semibold", Font.BOLD, 28); 
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 14);    
    private static final Font INPUT_FONT = new Font("Segoe UI", Font.PLAIN, 15);  
    private static final Font BUTTON_FONT = new Font("Segoe UI Semibold", Font.BOLD, 15);
    private static final Font LINK_FONT = new Font("Segoe UI", Font.PLAIN, 12);     
    private static final Font ICON_FONT = new Font("Segoe UI Symbol", Font.PLAIN, 16); 
    
    private static final Color SEARCH_PAGE_BACKGROUND = new Color(0x0D0E11);
    private static final Color SEARCH_BAR_BACKGROUND = new Color(0x1C1E25);
    private static final Color SEARCH_BAR_BORDER_COLOR = new Color(0x30333A);
    private static final Color TAG_BACKGROUND_COLOR = new Color(0x2A2D35);
    private static final Color TAG_TEXT_COLOR = new Color(0xA0A8B2);
    
    private CardLayout heroCardLayout;
    private JPanel heroPanelContainer;
    private JPanel carouselDotsPanel;
    private int currentHeroSlide = 0;
    private final int TOTAL_HERO_SLIDES = 3;
    
    
    private Map<String, List<String>> movieDataByCategory;
    private Map<String, List<String>> sportsDataByCategory;
    
    
    private JPanel topNavPanel;
    
    private SportsCategoryDrawerDialog sportsDrawer = null;
    private FilmCategoryDrawerDialog filmDrawer = null;
    
    private JPanel mainCardPanel;
    private CardLayout mainCardLayout; 
    private JPanel homePagePanel; 
    
    private int currentLoggedInUserId = -1; 
    private String currentLoggedInUserProfileName = "Guest";
    
    private JLabel profileMenuUserNameLabel; 
    private JLabel profileMenuUserIconLabel;

     
    private static final String HOME_PAGE_CARD = "HomePage";
    private static final String KIDS_PAGE_CARD = "KidsPage";
    private static final String SEARCH_PAGE_CARD = "SearchPage";
    private static final String PROFILE_PAGE_CARD = "ProfilePage";
    private static final String MY_LISTS_PAGE_CARD = "MyListsPage";
    
    private JTextField searchFieldOnSearchPage;  
    private JPanel searchResultsPanel;  
    private CardLayout searchPageCardLayout;  
    private JPanel searchPageContentHolder;  
    
    private JPanel myListsPagePanelHolder;  
    private JList<String> userListsDisplayJList;  
    private DefaultListModel<String> userListsModel; 
    private JPanel selectedListContentPanel; 
    private Map<String, Integer> userListNameToIdMap; 

    private static final String SEARCH_TAGS_VIEW = "SearchTagsView";
    private static final String SEARCH_RESULTS_VIEW = "SearchResultsView";

    private JPanel profilePagePanelHolder;
    
    public StreamingAppUI(int loggedInUserId, String userProfileName) {
        this();
        this.currentLoggedInUserId = loggedInUserId;
        this.currentLoggedInUserProfileName = userProfileName;
        System.out.println("StreamingAppUI started for user ID: " + this.currentLoggedInUserId + ", Name: " + this.currentLoggedInUserProfileName);
        
        updateProfileDropdownHeader();
    }
    
    public StreamingAppUI() {
        setTitle("LUNA: Sreaming App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1280, 800)); // Biraz daha geniş
        getContentPane().setBackground(BACKGROUND_COLOR);
        setLayout(new BorderLayout());
        
        loadMovieDataFromDB();

        // 1. Üst Navigasyon Barı
        topNavPanel = createTopNavBar();
        add(topNavPanel, BorderLayout.NORTH);
        
        

        mainCardLayout = new CardLayout();
        mainCardPanel = new JPanel(mainCardLayout);
       
        
        mainCardPanel.setBackground(BACKGROUND_COLOR); 
        
      
        
        homePagePanel = new JPanel(); 
        homePagePanel.setBackground(BACKGROUND_COLOR);
        homePagePanel.setLayout(new BoxLayout(homePagePanel, BoxLayout.Y_AXIS));
        homePagePanel.setBorder(new EmptyBorder(10, 0, 10, 0));

        // Ana sayfa öğelerini homePagePanel'e ekle
        homePagePanel.add(createHeroSection());
        homePagePanel.add(Box.createRigidArea(new Dimension(0, 30)));
        homePagePanel.add(createSectionPanel("Channels", createChannelItems(), true)); // Bu metodlar artık homePagePanel'e ekleniyor
        homePagePanel.add(Box.createRigidArea(new Dimension(0, 30)));
        homePagePanel.add(createSectionPanel("Keep Watching", createContinueWatchingItems(), true));
        homePagePanel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        
        
        JScrollPane homeScrollPane = new JScrollPane(homePagePanel);
        homeScrollPane.setBorder(null);
        homeScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        styleScrollBar(homeScrollPane.getVerticalScrollBar());
        styleScrollBar(homeScrollPane.getHorizontalScrollBar());
        
     // 2.b. Çocuk Sayfası Paneli (Şimdilik boş bir placeholder, sonra dolduracağız)
        JPanel kidsPagePanel = createKidsPagePanel(); 
        JScrollPane kidsScrollPane = new JScrollPane(kidsPagePanel);
        kidsScrollPane.setBorder(null);
        kidsScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        styleScrollBar(kidsScrollPane.getVerticalScrollBar());
        
        //JPanel searchPageContents = createSearchPagePanel();
        
        JPanel searchPageContainer = new JPanel(new BorderLayout()); // YENİ DIŞ PANEL
        searchPageContainer.setBackground(SEARCH_PAGE_BACKGROUND); // Veya BACKGROUND_COLOR
        JPanel searchPageContents = createSearchPagePanel();
        searchPageContainer.add(searchPageContents, BorderLayout.NORTH); // İçeriği üste topla
        searchPageContainer.add(Box.createVerticalGlue(), BorderLayout.CENTER); // Altı boş bırak
        mainCardPanel.add(searchPageContainer, SEARCH_PAGE_CARD); // Dış paneli ekle


        mainCardPanel.add(homeScrollPane, HOME_PAGE_CARD);
        mainCardPanel.add(kidsScrollPane, KIDS_PAGE_CARD);
        mainCardPanel.add(searchPageContents, SEARCH_PAGE_CARD);
        
        profilePagePanelHolder = new JPanel(new BorderLayout()); 
        profilePagePanelHolder.setBackground(BACKGROUND_COLOR); 
        mainCardPanel.add(profilePagePanelHolder, PROFILE_PAGE_CARD);
        
        myListsPagePanelHolder = new JPanel(new BorderLayout());
        myListsPagePanelHolder.setBackground(BACKGROUND_COLOR);
        // Bu holder'ın içeriği, "My Lists" menü öğesine tıklandığında doldurulacak
        mainCardPanel.add(myListsPagePanelHolder, MY_LISTS_PAGE_CARD);

        add(mainCardPanel, BorderLayout.CENTER);
        
        mainCardLayout.show(mainCardPanel, HOME_PAGE_CARD);

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
    }
    
    public int getTopNavBarHeight() {
        if (topNavPanel != null) {
            return topNavPanel.getHeight();
        }
        return 0; 
    }
   

    public static void styleScrollBar(JScrollBar scrollBar) {
        scrollBar.setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = ACCENT_COLOR;
                this.trackColor = BACKGROUND_COLOR.darker(); // veya PANEL_ITEM_BG_COLOR
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                button.setMinimumSize(new Dimension(0, 0));
                button.setMaximumSize(new Dimension(0, 0));
                return button;
            }

            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
                if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
                    return;
                }
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(thumbColor);
                g2.fillRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width -2 , thumbBounds.height -2, 5, 5);  
                g2.dispose();
            }

            @Override
            protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
                 g.setColor(trackColor);
                 g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
            }
        });
        scrollBar.setPreferredSize(new Dimension(8, 8)); 
    }
    
    
    private JPanel createKidsPagePanel() {
        JPanel kidsPanel = new JPanel();
        kidsPanel.setBackground(BACKGROUND_COLOR);
        kidsPanel.setLayout(new BoxLayout(kidsPanel, BoxLayout.Y_AXIS));
        kidsPanel.setBorder(new EmptyBorder(20, 0, 20, 0));  
        
        List<Map<String, Object>> categories = getKidsCategoriesFromDB();
        
        if (categories.isEmpty()) {
            JLabel noCategoriesLabel = new JLabel("No categories available in the Kids section.");
            
            kidsPanel.add(noCategoriesLabel); 
        } else {
            for (Map<String, Object> categoryData : categories) {
                String categoryName = (String) categoryData.get("name");
                int categoryId = (int) categoryData.get("id");
                System.out.println("Processing Kids Category: " + categoryName + " (ID: " + categoryId + ")"); 

                
                List<Map<String, String>> contentForCategory = getContentForKidsCategoryFromDB(categoryId, categoryName);

                if (!contentForCategory.isEmpty()) {
                    JPanel itemsPanel = createKidsContentItemsPanel(contentForCategory);
                    kidsPanel.add(createGenericSectionPanel(categoryName, itemsPanel, true));  
                    kidsPanel.add(Box.createRigidArea(new Dimension(0, 40)));
                } else {
                    System.out.println("No content found for kids category: " + categoryName);  
                    JPanel emptyCategoryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    emptyCategoryPanel.setOpaque(false);
                    emptyCategoryPanel.add(new JLabel("No content yet in " + categoryName + ". Check back soon!"));
                    kidsPanel.add(createGenericSectionPanel(categoryName, emptyCategoryPanel, false));
                    kidsPanel.add(Box.createRigidArea(new Dimension(0, 40)));
                }
            }
        }
        return kidsPanel;
    }
        

    
    private List<Map<String, Object>> getKidsCategoriesFromDB() {
        List<Map<String, Object>> categories = new ArrayList<>();
        String sql = "SELECT kids_category_id, category_name FROM kids_categories ORDER BY display_order ASC";
        System.out.println("Fetching kids categories from DB: " + sql); 

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (conn == null) {
                System.err.println("getKidsCategoriesFromDB: Database connection is NULL!");
                return categories;
            }

            while (rs.next()) {
                Map<String, Object> category = new HashMap<>();
                category.put("id", rs.getInt("kids_category_id"));
                category.put("name", rs.getString("category_name"));
                categories.add(category);
            }
        } catch (SQLException e) {
            System.err.println("SQL error while loading kids categories:");
            e.printStackTrace();
        }
        System.out.println("Loaded " + categories.size() + " kids categories."); 
        return categories;
    }
    
    private List<Map<String, String>> getContentForKidsCategoryFromDB(int kidsCategoryId, String categoryName) {
        List<Map<String, String>> contents = new ArrayList<>();
        String sql;
        final String POSTER_COLUMN_NAME = "poster_image_path"; 
        if ("All Kids Content".equalsIgnoreCase(categoryName)) { 
            sql = "SELECT DISTINCT ci.content_item_id, ci.title, ci."+ POSTER_COLUMN_NAME + ", GROUP_CONCAT(DISTINCT g.genre_name SEPARATOR ', ') AS genres " +
                  "FROM content_items ci " +
                  "JOIN content_types ct ON ci.content_type_id = ct.content_type_id " +
                  "LEFT JOIN content_item_genres cig ON ci.content_item_id = cig.content_item_id " +
                  "LEFT JOIN genres g ON cig.genre_id = g.genre_id " +
                  "WHERE ct.type_name = 'KidsShow' OR " +
                  "      (ct.type_name = 'Movie' AND ci.content_item_id IN (SELECT content_item_id FROM content_item_genres WHERE genre_id IN (SELECT genre_id FROM genres WHERE genre_name IN ('Kids', 'Family', 'Animation')))) " +
                  "GROUP BY ci.content_item_id, ci.title, ci."+ POSTER_COLUMN_NAME +" " +
                  "ORDER BY ci.title ASC LIMIT 20";  
        } else if ("Colorful Adventures".equalsIgnoreCase(categoryName)) { 
            sql = "SELECT ci.content_item_id, ci.title, ci." + POSTER_COLUMN_NAME + ", GROUP_CONCAT(DISTINCT g.genre_name SEPARATOR ', ') AS genres " +
                  "FROM content_items ci " +
                  "JOIN content_item_genres cig ON ci.content_item_id = cig.content_item_id " +
                  "JOIN genres g ON cig.genre_id = g.genre_id " +
                  "WHERE ci.content_item_id IN (SELECT content_item_id FROM content_item_genres WHERE genre_id = (SELECT genre_id FROM genres WHERE genre_name = 'Animation')) " +
                  "  AND ci.content_item_id IN (SELECT content_item_id FROM content_item_genres WHERE genre_id IN (SELECT genre_id FROM genres WHERE genre_name IN ('Adventure', 'Action', 'Fantasy'))) " +
                  "GROUP BY ci.content_item_id, ci.title, ci."+ POSTER_COLUMN_NAME + " " +
                  "ORDER BY ci.title ASC LIMIT 10";
        } else if ("Educational & Learning".equalsIgnoreCase(categoryName)) {
            
            sql = "SELECT ci.content_item_id, ci.title, ci." + POSTER_COLUMN_NAME + ", GROUP_CONCAT(DISTINCT g.genre_name SEPARATOR ', ') AS genres " +
                  "FROM content_items ci " +
                  "JOIN content_item_genres cig ON ci.content_item_id = cig.content_item_id " +
                  "JOIN genres g ON cig.genre_id = g.genre_id " +
                  "WHERE g.genre_name = 'Educational' " +
                  "GROUP BY ci.content_item_id, ci.title, ci." + POSTER_COLUMN_NAME+ " " +
                  "ORDER BY ci.title ASC LIMIT 10";
        } else { 
            sql = "SELECT ci.content_item_id, ci.title, ci." + POSTER_COLUMN_NAME + ", GROUP_CONCAT(DISTINCT g.genre_name SEPARATOR ', ') AS genres " +
                  "FROM content_items ci " +
                  "JOIN kids_category_content kcc ON ci.content_item_id = kcc.content_item_id " +
                  "LEFT JOIN content_item_genres cig ON ci.content_item_id = cig.content_item_id " +
                  "LEFT JOIN genres g ON cig.genre_id = g.genre_id " +
                  "WHERE kcc.kids_category_id = ? " +
                  "GROUP BY ci.content_item_id, ci.title, ci." + POSTER_COLUMN_NAME + " " +
                  "ORDER BY kcc.display_order_in_category ASC, ci.title ASC LIMIT 10";
        }
        System.out.println("Fetching content for kids category ID " + kidsCategoryId + " ("+categoryName+"): " + sql); // DEBUG

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (conn == null) {
                System.err.println("getContentForKidsCategoryFromDB: Database connection is NULL!");
                return contents;
            } 
            if (!"All Kids Content".equalsIgnoreCase(categoryName) &&
                !"Colorful Adventures".equalsIgnoreCase(categoryName) &&
                !"Educational & Learning".equalsIgnoreCase(categoryName) &&
                sql.contains("?")) {
                pstmt.setInt(1, kidsCategoryId);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, String> content = new HashMap<>();
                    content.put("id", rs.getString("content_item_id"));
                    content.put("title", rs.getString("title"));
                    content.put("genres", rs.getString("genres") != null ? rs.getString("genres") : "N/A");
                    content.put("imageName", rs.getString("poster_image_path")); 
                    contents.add(content);
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL error loading content for kids category '" + categoryName + "':");
            e.printStackTrace();
        }
        System.out.println("Loaded " + contents.size() + " items for kids category: " + categoryName); // DEBUG
        return contents;
    }
    
/*
    private JPanel createSearchPagePanel() {
        JPanel searchPageOuterPanel = new JPanel(new BorderLayout(0, 20)); 
        searchPageOuterPanel.setBackground(SEARCH_PAGE_BACKGROUND); 
        searchPageOuterPanel.setBorder(new EmptyBorder(30, 50, 30, 50)); 

        
        JPanel searchBarContainer = new JPanel(new BorderLayout(10, 0)); 
        searchBarContainer.setOpaque(false); 
        searchBarContainer.setBackground(SEARCH_BAR_BACKGROUND); 
        searchBarContainer.setBorder(new CompoundBorder(
                new LineBorder(SEARCH_BAR_BORDER_COLOR, 1, true),
                new EmptyBorder(10, 15, 10, 15) 
        ));

        JLabel searchIconInBar = new JLabel(getIcon("search.png", "Q"));
        searchIconInBar.setForeground(TAG_TEXT_COLOR); 

        final String placeholderText = "Search for movies, series, actors, or directors...";
        
        searchFieldOnSearchPage = new JTextField(placeholderText);
        searchFieldOnSearchPage.setFont(DEFAULT_FONT.deriveFont(16f));
        searchFieldOnSearchPage.setOpaque(false);
        searchFieldOnSearchPage.setBorder(null);
        searchFieldOnSearchPage.setForeground(TAG_TEXT_COLOR);
        searchFieldOnSearchPage.setCaretColor(ACCENT_COLOR);
        searchFieldOnSearchPage.addFocusListener(new FocusAdapter() { // FocusAdapter daha kısa
            @Override
            public void focusGained(FocusEvent e) {
                if (searchFieldOnSearchPage.getText().equals(placeholderText)) {
                    searchFieldOnSearchPage.setText("");
                    searchFieldOnSearchPage.setForeground(TEXT_COLOR_PRIMARY);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (searchFieldOnSearchPage.getText().isEmpty()) {
                    searchFieldOnSearchPage.setText(placeholderText);
                    searchFieldOnSearchPage.setForeground(TAG_TEXT_COLOR);
                }
            }
        });
        
        searchFieldOnSearchPage.addActionListener(e -> performSearch());

        searchBarContainer.add(searchIconInBar, BorderLayout.WEST);
        searchBarContainer.add(searchFieldOnSearchPage, BorderLayout.CENTER);
        searchPageOuterPanel.add(searchBarContainer, BorderLayout.NORTH);
        
        
        JTextField searchField = new JTextField(placeholderText);
        searchField.setFont(DEFAULT_FONT.deriveFont(16f));
        searchField.setOpaque(false);
        searchField.setBorder(null); 
        searchField.setForeground(TAG_TEXT_COLOR); 
        searchField.setCaretColor(ACCENT_COLOR);

        searchField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals(placeholderText)) {
                    searchField.setText("");
                    searchField.setForeground(TEXT_COLOR); 
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText(placeholderText);
                    searchField.setForeground(TAG_TEXT_COLOR); 
                }
            }
        });
        

        searchBarContainer.add(searchIconInBar, BorderLayout.WEST);
        searchBarContainer.add(searchField, BorderLayout.CENTER);
        searchPagePanel.add(searchBarContainer, BorderLayout.NORTH);


        JPanel tagsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10)); 
        tagsPanel.setOpaque(false);

        String[] popularSearches = {
            "Şampiyonlar Ligi", "S Sport", "BluTV", "Anatomy Of A Fall",
            "The Walking Dead", "TRT1", "Disney Junior", "Eurosport",
            "Talk To Me", "Game of Thrones"
        };

        Icon trendingIcon = getIcon("trending.png", "↗"); 

        for (String searchText : popularSearches) {
            JButton tagButton = new JButton(searchText);
            tagButton.setIcon(trendingIcon);
            tagButton.setFont(DEFAULT_FONT.deriveFont(13f));
            tagButton.setForeground(TAG_TEXT_COLOR);
            tagButton.setBackground(TAG_BACKGROUND_COLOR);
            tagButton.setOpaque(true); 
            tagButton.setFocusPainted(false);
            tagButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            tagButton.setBorder(new CompoundBorder(
                    new LineBorder(TAG_BACKGROUND_COLOR, 2, true), 
                    new EmptyBorder(6, 12, 6, 12) 
            ));

            tagButton.addMouseListener(new MouseAdapter() {
                Color originalBg = tagButton.getBackground();
                Color originalFg = tagButton.getForeground();
                @Override
                public void mouseEntered(MouseEvent e) {
                    tagButton.setBackground(originalBg.brighter());
                    tagButton.setForeground(TEXT_COLOR); 
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    tagButton.setBackground(originalBg);
                    tagButton.setForeground(originalFg);
                }
            });

            tagButton.addActionListener(e -> {
                searchField.setText(searchText);
                searchField.setForeground(TEXT_COLOR); ;
                System.out.println("Tag clicked: " + searchText);
            });
            tagsPanel.add(tagButton);
        }
        searchPagePanel.add(tagsPanel, BorderLayout.CENTER);

        return searchPagePanel;
    }*/
    
    private JPanel createSearchPagePanel() {
        JPanel searchPageOuterPanel = new JPanel(new BorderLayout(0, 20));  
        searchPageOuterPanel.setBackground(SEARCH_PAGE_BACKGROUND);
        searchPageOuterPanel.setBorder(new EmptyBorder(30, 50, 30, 50));  
 
        JPanel searchBarContainer = new JPanel(new BorderLayout(10, 0));
        searchBarContainer.setOpaque(false);
        searchBarContainer.setBackground(SEARCH_BAR_BACKGROUND);
        searchBarContainer.setBorder(new CompoundBorder(
                new LineBorder(SEARCH_BAR_BORDER_COLOR, 1, true),
                new EmptyBorder(10, 15, 10, 15)
        ));
        JLabel searchIconInBar = new JLabel(getIcon("search.png", "Q"));
        searchIconInBar.setForeground(TAG_TEXT_COLOR);

        final String placeholderText = "Search for movies, series, actors, or directors..."; 
        searchFieldOnSearchPage = new JTextField(placeholderText);   
        searchFieldOnSearchPage.setFont(DEFAULT_FONT.deriveFont(16f));
        searchFieldOnSearchPage.setOpaque(false);
        searchFieldOnSearchPage.setBorder(null);
        searchFieldOnSearchPage.setForeground(TAG_TEXT_COLOR);
        searchFieldOnSearchPage.setCaretColor(ACCENT_COLOR);
        searchFieldOnSearchPage.addFocusListener(new FocusAdapter() {  
            @Override
            public void focusGained(FocusEvent e) {
                if (searchFieldOnSearchPage.getText().equals(placeholderText)) {
                    searchFieldOnSearchPage.setText("");
                    searchFieldOnSearchPage.setForeground(TEXT_COLOR_PRIMARY);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (searchFieldOnSearchPage.getText().isEmpty()) {
                    searchFieldOnSearchPage.setText(placeholderText);
                    searchFieldOnSearchPage.setForeground(TAG_TEXT_COLOR);
                }
            }
        });
 
        searchFieldOnSearchPage.addActionListener(e -> performSearch());

        searchBarContainer.add(searchIconInBar, BorderLayout.WEST);
        searchBarContainer.add(searchFieldOnSearchPage, BorderLayout.CENTER);
        searchPageOuterPanel.add(searchBarContainer, BorderLayout.NORTH);

 
        searchPageCardLayout = new CardLayout();
        searchPageContentHolder = new JPanel(searchPageCardLayout);
        searchPageContentHolder.setOpaque(false);
 
        JPanel tagsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        tagsPanel.setOpaque(false);
        populatePopularSearchTags(tagsPanel); 
        JScrollPane tagsScrollPane = new JScrollPane(tagsPanel);  
        styleScrollPaneAsPanel(tagsScrollPane);  
        tagsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); 

 
        searchResultsPanel = new JPanel();  
        searchResultsPanel.setLayout(new BoxLayout(searchResultsPanel, BoxLayout.Y_AXIS));
        searchResultsPanel.setOpaque(false);
        JScrollPane resultsScrollPane = new JScrollPane(searchResultsPanel);
        styleScrollPaneAsPanel(resultsScrollPane);

        searchPageContentHolder.add(tagsScrollPane, SEARCH_TAGS_VIEW);
        searchPageContentHolder.add(resultsScrollPane, SEARCH_RESULTS_VIEW);

        searchPageOuterPanel.add(searchPageContentHolder, BorderLayout.CENTER);
        searchPageCardLayout.show(searchPageContentHolder, SEARCH_TAGS_VIEW);  

        return searchPageOuterPanel;
    }

    private void styleScrollPaneAsPanel(JScrollPane scrollPane) {
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        styleScrollBar(scrollPane.getVerticalScrollBar());
        styleScrollBar(scrollPane.getHorizontalScrollBar());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
    }
    
    private void populatePopularSearchTags(JPanel tagsPanel) {
        String sql = "SELECT term FROM popular_searches ORDER BY display_order ASC, term ASC LIMIT 15";
        Icon trendingIcon = getIcon("trending.png", "↗");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (conn == null) {
                System.err.println("populatePopularSearchTags: Database connection NULL");
                tagsPanel.add(new JLabel("Could not load popular searches."){{setForeground(TEXT_COLOR);}});
                return;
            }

            boolean found = false;
            while (rs.next()) {
                found = true;
                String searchTerm = rs.getString("term");
                JButton tagButton = new JButton(searchTerm); 
                tagButton.setIcon(trendingIcon);
                tagButton.setFont(DEFAULT_FONT.deriveFont(13f));
                tagButton.setForeground(TAG_TEXT_COLOR);
                tagButton.setBackground(TAG_BACKGROUND_COLOR);
                tagButton.setOpaque(true);
                tagButton.setFocusPainted(false);
                tagButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                tagButton.setBorder(new CompoundBorder(
                        new LineBorder(TAG_BACKGROUND_COLOR, 2, true),
                        new EmptyBorder(6, 12, 6, 12)
                ));
                 tagButton.addMouseListener(new MouseAdapter() {  });
                tagButton.addActionListener(e -> {
                    searchFieldOnSearchPage.setText(searchTerm);
                    searchFieldOnSearchPage.setForeground(TEXT_COLOR_PRIMARY);
                    performSearch();  
                });
                tagsPanel.add(tagButton);
            }
            if (!found) {
                tagsPanel.add(new JLabel("No popular searches defined."){{setForeground(TEXT_COLOR);}});
            }

        } catch (SQLException e) {
            System.err.println("Error loading popular searches: " + e.getMessage());
            e.printStackTrace();
            tagsPanel.add(new JLabel("Error loading tags."){{setForeground(TEXT_COLOR);}});
        }
    }
 
    private void performSearch() {
        String searchTerm = searchFieldOnSearchPage.getText().trim();
        if (searchTerm.isEmpty() || searchTerm.equals("Search for movies, series, actors, or directors...")) {
            
            searchPageCardLayout.show(searchPageContentHolder, SEARCH_TAGS_VIEW);
            return;
        }
        System.out.println("Performing search for: " + searchTerm); 

        searchResultsPanel.removeAll();  
        String sql = "SELECT DISTINCT ci.content_item_id, ci.title, ci.description, ci.release_year, " +
                     "ct.type_name, GROUP_CONCAT(DISTINCT g.genre_name SEPARATOR ', ') AS genres " +
                     "FROM content_items ci " +
                     "JOIN content_types ct ON ci.content_type_id = ct.content_type_id " +
                     "LEFT JOIN content_item_genres cig ON ci.content_item_id = cig.content_item_id " +
                     "LEFT JOIN genres g ON cig.genre_id = g.genre_id " +
                     "WHERE ci.title LIKE ? OR g.genre_name LIKE ? OR ci.description LIKE ? " +  
                     "GROUP BY ci.content_item_id, ci.title, ci.description, ci.release_year, ct.type_name " +
                     "ORDER BY ci.release_year DESC, ci.title ASC LIMIT 30";  

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (conn == null) {
                System.err.println("performSearch: Database connection NULL");
                searchResultsPanel.add(new JLabel("Database connection error."){{setForeground(TEXT_COLOR);}});
                searchPageCardLayout.show(searchPageContentHolder, SEARCH_RESULTS_VIEW);
                searchResultsPanel.revalidate();
                searchResultsPanel.repaint();
                return;
            }

            String likeTerm = "%" + searchTerm + "%";
            pstmt.setString(1, likeTerm);  
            pstmt.setString(2, likeTerm);  
            pstmt.setString(3, likeTerm); 

            try (ResultSet rs = pstmt.executeQuery()) {
                boolean foundResults = false;
                while (rs.next()) {
                    foundResults = true;
                    String title = rs.getString("title");
                    String type = rs.getString("type_name");
                    String genres = rs.getString("genres");
                    String year = rs.getString("release_year") != null ? " (" + rs.getString("release_year") + ")" : "";
                     
                    JPanel resultItemPanel = new JPanel(new BorderLayout(10,2));
                    resultItemPanel.setOpaque(false);
                    resultItemPanel.setBorder(new CompoundBorder(
                        new MatteBorder(0,0,1,0, BORDER_COLOR_SUBTLE),
                        new EmptyBorder(8,5,8,5)
                    ));
                    resultItemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70)); // Yüksekliği sabitle

                    JLabel titleLabel = new JLabel(title + year);
                    titleLabel.setFont(TITLE_FONT.deriveFont(16f));
                    titleLabel.setForeground(TEXT_COLOR_PRIMARY);

                    JLabel detailsLabel = new JLabel(type + (genres != null ? " - " + genres : ""));
                    detailsLabel.setFont(DEFAULT_FONT.deriveFont(13f));
                    detailsLabel.setForeground(TEXT_COLOR_SECONDARY);

                    resultItemPanel.add(titleLabel, BorderLayout.NORTH);
                    resultItemPanel.add(detailsLabel, BorderLayout.SOUTH);

                    resultItemPanel.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) { 
                            JOptionPane.showMessageDialog(StreamingAppUI.this, "Clicked on: " + title, "Search Result", JOptionPane.INFORMATION_MESSAGE);
                        }
                    });
                    searchResultsPanel.add(resultItemPanel);
                    searchResultsPanel.add(Box.createRigidArea(new Dimension(0,5)));
                }

                if (!foundResults) {
                    JLabel noResultsLabel = new JLabel("No results found for '" + searchTerm + "'");
                    noResultsLabel.setFont(TITLE_FONT.deriveFont(18f));
                    noResultsLabel.setForeground(TEXT_COLOR_SECONDARY);
                    noResultsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                    searchResultsPanel.add(Box.createVerticalGlue());
                    searchResultsPanel.add(noResultsLabel);
                    searchResultsPanel.add(Box.createVerticalGlue());
                }
            }
        } catch (SQLException e) {
            System.err.println("Error during search: " + e.getMessage());
            e.printStackTrace();
            searchResultsPanel.add(new JLabel("Error performing search."){{setForeground(TEXT_COLOR);}});
        }

        searchPageCardLayout.show(searchPageContentHolder, SEARCH_RESULTS_VIEW);  
        searchResultsPanel.revalidate();
        searchResultsPanel.repaint();
    }

    private JPanel createGenericSectionPanel(String title, JPanel itemsPanel, boolean showAllLink) {
        JPanel sectionPanel = new JPanel(new BorderLayout(0, 15));
        sectionPanel.setBackground(BACKGROUND_COLOR);
        sectionPanel.setBorder(new EmptyBorder(0, 30, 0, 30));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(TITLE_FONT.deriveFont(22f));
        titleLabel.setForeground(TEXT_COLOR);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        if (showAllLink) {
            JLabel tumuLabel = new JLabel("Tümü →");
            tumuLabel.setFont(DEFAULT_FONT.deriveFont(15f));
            tumuLabel.setForeground(ACCENT_COLOR);
            tumuLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
             tumuLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    tumuLabel.setForeground(ACCENT_COLOR.brighter());
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    tumuLabel.setForeground(ACCENT_COLOR);
                }
                @Override
                public void mouseClicked(MouseEvent e) {
                    JOptionPane.showMessageDialog(StreamingAppUI.this, "Showing all for: " + title, "All Content", JOptionPane.INFORMATION_MESSAGE);
                }
            });
            headerPanel.add(tumuLabel, BorderLayout.EAST);
        }
        sectionPanel.add(headerPanel, BorderLayout.NORTH);

        JScrollPane itemsScrollPane = new JScrollPane(itemsPanel);
        itemsScrollPane.setBorder(null);
        itemsScrollPane.setOpaque(false);
        itemsScrollPane.getViewport().setOpaque(false);
        itemsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); // VEYA _NEVER ve ok butonları eklenir
        itemsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        itemsScrollPane.getHorizontalScrollBar().setUnitIncrement(16);
        styleScrollBar(itemsScrollPane.getHorizontalScrollBar());
        int scrollPaneHeight = itemsPanel.getPreferredSize().height + (itemsScrollPane.getHorizontalScrollBar().isVisible() ? itemsScrollPane.getHorizontalScrollBar().getPreferredSize().height : 0) + 10;
        itemsScrollPane.setPreferredSize(new Dimension(100, scrollPaneHeight));

        sectionPanel.add(itemsScrollPane, BorderLayout.CENTER);
        return sectionPanel;
    }

   
    
    private JPanel createKidsContentItemsPanel(List<Map<String, String>> contentList) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(0,0,5,0));

        for (Map<String, String> contentData : contentList) {
            String title = contentData.get("title");
            String genres = contentData.get("genres");
            String imageName = contentData.get("imageName"); / 
            int contentItemId = Integer.parseInt(contentData.get("id")); 

            panel.add(createSimpleContentCard(title, genres, imageName, contentItemId));  
        }

        int itemCount = contentList.size();
        int preferredWidth = (200 + 15) * Math.max(itemCount,1) ;  
        int preferredHeight = (itemCount > 0) ? (290 + 5 + 5) : 50;  
        panel.setPreferredSize(new Dimension(preferredWidth, preferredHeight));
        return panel;
    }
    
    
/*
    private JPanel createSimpleContentCard(String title, String genre, String imageNameFromDBorCode, final int contentItemId) {
        
    	JPanel itemPanel = new JPanel(new BorderLayout(0,8));
        itemPanel.setOpaque(false);
        itemPanel.setPreferredSize(new Dimension(200, 290)); 
        itemPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        final int IMAGE_PANEL_WIDTH = 200; 
        final int IMAGE_PANEL_HEIGHT = 220; 

        JPanel imageDisplayPanel = new JPanel(new BorderLayout()); 
        imageDisplayPanel.setPreferredSize(new Dimension(IMAGE_PANEL_WIDTH, IMAGE_PANEL_HEIGHT));
        imageDisplayPanel.setBackground(PANEL_ITEM_BG_COLOR); 
        imageDisplayPanel.setBorder(new LineBorder(PANEL_ITEM_BG_COLOR, 2));
        
        JPanel imagePlaceholder = new JPanel(new GridBagLayout());
        imagePlaceholder.setBackground(PANEL_ITEM_BG_COLOR);
        imagePlaceholder.setPreferredSize(new Dimension(200, 220));
        imagePlaceholder.setBorder(new LineBorder(PANEL_ITEM_BG_COLOR, 2));
        
        addPlaceholderText(imagePlaceholderPanel, title, TEXT_COLOR, HERO_TITLE_FONT.deriveFont(60f));
        itemPanel.add(imagePlaceholderPanel, BorderLayout.NORTH);
        
        JLabel placeholderText = new JLabel(title.substring(0, Math.min(title.length(),1)).toUpperCase()); // Baş harf
        placeholderText.setFont(HERO_TITLE_FONT.deriveFont(60f));

        placeholderText.setForeground(TEXT_COLOR);
        placeholderText.setHorizontalAlignment(SwingConstants.CENTER);
        imagePlaceholder.add(placeholderText, BorderLayout.CENTER);
        imagePlaceholder.setBorder(new LineBorder(PANEL_ITEM_BG_COLOR, 2));

        
        Icon originalIcon = null;
        if (imageNameFromDBorCode != null && !imageNameFromDBorCode.trim().isEmpty()) {
            originalIcon = getIcon(imageNameFromDBorCode, title.substring(0,1)); // Fallback için baş harf
        }

        if (originalIcon instanceof ImageIcon && ((ImageIcon)originalIcon).getIconWidth() > 0) {
            // getScaledImageIcon'u burada kullanıyoruz
            ImageIcon scaledIcon = getScaledImageIcon((ImageIcon)originalIcon, IMAGE_PANEL_WIDTH, IMAGE_PANEL_HEIGHT);
            if (scaledIcon != null) {
                JLabel imageLabel = new JLabel(scaledIcon);
                imageLabel.setHorizontalAlignment(SwingConstants.CENTER); // İkonu label içinde ortala
                imageLabel.setVerticalAlignment(SwingConstants.CENTER);
                imageDisplayPanel.add(imageLabel, BorderLayout.CENTER);
            } else {
                addPlaceholderText(imageDisplayPanel, title, TEXT_COLOR, HERO_TITLE_FONT.deriveFont(60f));
            }
        } else {
            // Orijinal ikon yüklenemedi veya ImageIcon değilse placeholder (fallbackTextIcon dönebilir)
            if (originalIcon != null) { // Eğer getIcon bir fallbackTextIcon döndürdüyse
                JLabel fallbackLabel = new JLabel(originalIcon);
                fallbackLabel.setHorizontalAlignment(SwingConstants.CENTER);
                fallbackLabel.setVerticalAlignment(SwingConstants.CENTER);
                imageDisplayPanel.add(fallbackLabel, BorderLayout.CENTER);
            } else { // Hiçbir şey yüklenemediyse
                addPlaceholderText(imageDisplayPanel, title, TEXT_COLOR, HERO_TITLE_FONT.deriveFont(60f));
            }
            if (imageNameFromDBorCode != null && !imageNameFromDBorCode.trim().isEmpty()){
                // System.err.println("Uyarı: createSimpleContentCard için resim '" + imageNameFromDBorCode + "' düzgün yüklenemedi.");
            }
        }
        itemPanel.add(imageDisplayPanel, BorderLayout.NORTH);
        
        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("<html><body style='width: 180px;'>" + title + "</body></html>"); // Uzun başlıklar için HTML ile satır kırma
        titleLabel.setFont(DEFAULT_FONT.deriveFont(Font.BOLD, 15f));
        titleLabel.setForeground(TEXT_COLOR);
        textPanel.add(titleLabel);

        JLabel genreLabel = new JLabel("<html><body style='width: 180px;'>" + genre + "</body></html>");
        genreLabel.setFont(DEFAULT_FONT.deriveFont(13f));
        genreLabel.setForeground(Color.LIGHT_GRAY);
        textPanel.add(genreLabel);
        
        itemPanel.add(textPanel, BorderLayout.CENTER);
        
        itemPanel.addMouseListener(new MouseAdapter() {
            Border hoverBorder = new LineBorder(ACCENT_COLOR, 2);
            Color originalPanelBg = imagePlaceholderPanel.getBackground(); // PANEL_ITEM_BG_COLOR
            Border originalBorder = imagePlaceholderPanel.getBorder();
            Color hoverBg = PANEL_ITEM_HOVER_BG_COLOR; // PANEL_ITEM_HOVER_BG_COLOR tanımlı olmalı
            @Override
            public void mouseEntered(MouseEvent e) {
                imagePlaceholder.setBorder(hoverBorder);
                imagePlaceholder.setBackground(PANEL_ITEM_HOVER_BG_COLOR);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                imagePlaceholder.setBorder(new LineBorder(PANEL_ITEM_BG_COLOR, 2));
                imagePlaceholder.setBackground(PANEL_ITEM_BG_COLOR);
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                //JOptionPane.showMessageDialog(StreamingAppUI.this, "Selected: " + title, "Content Clicked", JOptionPane.INFORMATION_MESSAGE);
            	if (currentLoggedInUserId == -1) {
                    JOptionPane.showMessageDialog(StreamingAppUI.this, "Please sign in to add items to your lists.", "Sign In Required", JOptionPane.WARNING_MESSAGE);
                    // AuthForm'a yönlendir
                    StreamingAppUI.this.dispose();
                    SwingUtilities.invokeLater(AuthForm::new);
                    return;
                }

                // Kullanıcıya seçenek sun: Detayları Gör veya Listeye Ekle
                Object[] options = {"View Details", "Add to List", "Cancel"};
                int choice = JOptionPane.showOptionDialog(StreamingAppUI.this,
                        "What would you like to do with '" + title + "'?",
                        "Content Action",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null, // İkon yok
                        options,
                        options[0]);

                if (choice == 0) { // View Details
                    JOptionPane.showMessageDialog(StreamingAppUI.this, "Showing details for: " + title, "Content Details", JOptionPane.INFORMATION_MESSAGE);
                    // TODO: Gerçek bir detay sayfası açılabilir
                } else if (choice == 1) { // Add to List
                	System.out.println("Add to List selected for content ID: " + contentItemId + " by user ID: " + currentLoggedInUserId); 
                    // Kullanıcının listelerini al (eğer userListNameToIdMap boşsa veya güncel değilse DB'den çek)
                    if (userListNameToIdMap == null || userListNameToIdMap.isEmpty()) {
                    	System.out.println("userListNameToIdMap is empty or null, loading lists for user: " + currentLoggedInUserId);
                    	loadUserListsForCurrentUser(currentLoggedInUserId); // Bu metot userListNameToIdMap'i doldurur
                    }

                    if (userListNameToIdMap.isEmpty()) { // Hala liste yoksa veya yüklenemediyse
                        // Varsayılan "My Watchlist"i oluşturmayı dene
                    	System.out.println("No lists exist for user, attempting to create 'My Watchlist'"); // DEBUG
                    	int confirmCreate = JOptionPane.showConfirmDialog(StreamingAppUI.this,
                                "You don't have any lists. Create 'My Watchlist' and add this item?",
                                "Create List", JOptionPane.YES_NO_OPTION);
                        if (confirmCreate == JOptionPane.YES_OPTION) {
                            Integer watchlistId = getOrCreateListIdByName(currentLoggedInUserId, "My Watchlist", StreamingAppUI.this);
                            if (watchlistId != null) {
                                addItemToUserList(currentLoggedInUserId, watchlistId, contentItemId, StreamingAppUI.this);
                            } else {
                                JOptionPane.showMessageDialog(StreamingAppUI.this, "Could not create 'My Watchlist'.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        return; // İşlemi bitir
                    	
                    }

                    // Listeleri JComboBox veya JList ile kullanıcıya sun
                    String[] listNames = userListNameToIdMap.keySet().toArray(new String[0]);
                    java.util.Arrays.sort(listNames);
                    //if (listNames.length == 0) { // Bu durum yukarıda ele alınmalıydı ama ekstra kontrol
                        
                    	if (listNames.length == 0) { // Bu durum yukarıdaki isEmpty ile yakalanmalıydı ama ekstra kontrol
                            System.err.println("listNames array is empty despite userListNameToIdMap not being initially empty. This shouldn't happen.");
                            int confirmCreate = JOptionPane.showConfirmDialog(StreamingAppUI.this,
                                    "No lists available. Create 'My Watchlist' and add this item?",
                                    "Create List", JOptionPane.YES_NO_OPTION);
                            if (confirmCreate == JOptionPane.YES_OPTION) {
                                Integer watchlistId = getOrCreateListIdByName(currentLoggedInUserId, "My Watchlist", StreamingAppUI.this);
                                if (watchlistId != null) {
                                    addItemToUserList(currentLoggedInUserId, watchlistId, contentItemId, StreamingAppUI.this);
                                }
                            }
                            
                            //JOptionPane.showMessageDialog(StreamingAppUI.this, "No lists available to add to.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                    	
                    }
                    	java.util.Arrays.sort(listNames);

                    String selectedList = (String) JOptionPane.showInputDialog(
                            StreamingAppUI.this,
                            "Select a list to add '" + title + "':",
                            "Add to List",
                            JOptionPane.PLAIN_MESSAGE,
                            null, // ikon
                            listNames,
                            listNames[0]);
                    
        
        if (selectedListName != null) { // Kullanıcı bir liste seçtiyse (iptal etmediyse)
            Integer selectedListId = userListNameToIdMap.get(selectedListName);
            if (selectedListId != null) {
                System.out.println("Adding content ID " + contentItemId + " to list: " + selectedListName + " (ID: " + selectedListId + ")"); // DEBUG
                addItemToUserList(currentLoggedInUserId, selectedListId, contentItemId, StreamingAppUI.this);
            } else {
                System.err.println("Error: Could not find ID for selected list name: " + selectedListName);
                JOptionPane.showMessageDialog(StreamingAppUI.this, "Error finding the selected list.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            System.out.println("User cancelled adding to list."); // DEBUG
        }
    }
            }
        });
        
        return itemPanel;
    } */

    private JPanel createSimpleContentCard(String title, String genre, String imageNameFromDBorCode, final int contentItemId) {
        JPanel itemPanel = new JPanel(new BorderLayout(0, 8));
        itemPanel.setOpaque(false);
        itemPanel.setPreferredSize(new Dimension(200, 290));
        itemPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        final int IMAGE_PANEL_WIDTH = 200;
        final int IMAGE_PANEL_HEIGHT = 220;

         
        final JPanel imageContainerPanel = new JPanel(new BorderLayout());
        imageContainerPanel.setPreferredSize(new Dimension(IMAGE_PANEL_WIDTH, IMAGE_PANEL_HEIGHT));
        imageContainerPanel.setBackground(PANEL_ITEM_BG_COLOR);
        imageContainerPanel.setBorder(new LineBorder(PANEL_ITEM_BG_COLOR, 2)); // Başlangıç sınırı

        Icon originalIcon = null;
        if (imageNameFromDBorCode != null && !imageNameFromDBorCode.trim().isEmpty()) {
            originalIcon = getIcon(imageNameFromDBorCode, title.substring(0, Math.min(title.length(), 1)).toUpperCase());
        } else {
             
            originalIcon = getIcon(null, title.substring(0, Math.min(title.length(), 1)).toUpperCase());
        }


        if (originalIcon instanceof ImageIcon && ((ImageIcon) originalIcon).getIconWidth() > 0) {
            ImageIcon scaledIcon = getScaledImageIcon((ImageIcon) originalIcon, IMAGE_PANEL_WIDTH, IMAGE_PANEL_HEIGHT);
            if (scaledIcon != null) {
                JLabel imageLabel = new JLabel(scaledIcon);
                imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
                imageLabel.setVerticalAlignment(SwingConstants.CENTER);
                imageContainerPanel.removeAll();  
                imageContainerPanel.add(imageLabel, BorderLayout.CENTER);
            } else {
                
                addPlaceholderText(imageContainerPanel, title, TEXT_COLOR, HERO_TITLE_FONT.deriveFont(60f));
            }
        } else {
             
            if (originalIcon != null) {  
                JLabel fallbackLabel = new JLabel(originalIcon);
                fallbackLabel.setHorizontalAlignment(SwingConstants.CENTER);
                fallbackLabel.setVerticalAlignment(SwingConstants.CENTER);
                imageContainerPanel.removeAll();  
                imageContainerPanel.add(fallbackLabel, BorderLayout.CENTER);
            } else { 
                addPlaceholderText(imageContainerPanel, title, TEXT_COLOR, HERO_TITLE_FONT.deriveFont(60f));
            }
        }
        itemPanel.add(imageContainerPanel, BorderLayout.NORTH);  
        
        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("<html><body style='width: 180px;'>" + title + "</body></html>");
        titleLabel.setFont(DEFAULT_FONT.deriveFont(Font.BOLD, 15f));
        titleLabel.setForeground(TEXT_COLOR);
        textPanel.add(titleLabel);

        JLabel genreLabel = new JLabel("<html><body style='width: 180px;'>" + (genre != null ? genre : "N/A") + "</body></html>");
        genreLabel.setFont(DEFAULT_FONT.deriveFont(13f));
        genreLabel.setForeground(Color.LIGHT_GRAY);
        textPanel.add(genreLabel);

        itemPanel.add(textPanel, BorderLayout.CENTER);

         
        itemPanel.addMouseListener(new MouseAdapter() { 
            Border hoverBorder = new LineBorder(ACCENT_COLOR, 2);
             
            @Override
            public void mouseEntered(MouseEvent e) {
                imageContainerPanel.setBorder(hoverBorder); 
                imageContainerPanel.setBackground(PANEL_ITEM_HOVER_BG_COLOR); 
            }

            @Override
            public void mouseExited(MouseEvent e) {
                
                imageContainerPanel.setBorder(new LineBorder(PANEL_ITEM_BG_COLOR, 2));
                imageContainerPanel.setBackground(PANEL_ITEM_BG_COLOR);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                
                if (currentLoggedInUserId == -1) {
                    JOptionPane.showMessageDialog(StreamingAppUI.this, "Please sign in to add items to your lists.", "Sign In Required", JOptionPane.WARNING_MESSAGE);
                    StreamingAppUI.this.dispose();
                    SwingUtilities.invokeLater(AuthForm::new);
                    return;
                }

                Object[] options = {"View Details", "Add to List", "Cancel"};
                int choice = JOptionPane.showOptionDialog(StreamingAppUI.this,
                        "What would you like to do with '" + title + "'?",
                        "Content Action",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]);

                if (choice == 0) { 
                    JOptionPane.showMessageDialog(StreamingAppUI.this, "Showing details for: " + title, "Content Details", JOptionPane.INFORMATION_MESSAGE);
                } else if (choice == 1) { 
                    System.out.println("Add to List selected for content ID: " + contentItemId + " by user ID: " + currentLoggedInUserId);

                    if (userListNameToIdMap == null || userListNameToIdMap.isEmpty()) {
                        System.out.println("userListNameToIdMap is empty or null, loading lists for user: " + currentLoggedInUserId);
                        loadUserListsForCurrentUser(currentLoggedInUserId);
                    }

                    if (userListNameToIdMap.isEmpty()) {
                        System.out.println("No lists exist for user, attempting to create 'My Watchlist'");
                        int confirmCreate = JOptionPane.showConfirmDialog(StreamingAppUI.this,
                                "You don't have any lists. Create 'My Watchlist' and add this item?",
                                "Create List", JOptionPane.YES_NO_OPTION);
                        if (confirmCreate == JOptionPane.YES_OPTION) {
                            Integer watchlistId = getOrCreateListIdByName(currentLoggedInUserId, "My Watchlist", StreamingAppUI.this);
                            if (watchlistId != null) {
                                addItemToUserList(currentLoggedInUserId, watchlistId, contentItemId, StreamingAppUI.this);
                            } else {
                                JOptionPane.showMessageDialog(StreamingAppUI.this, "Could not create 'My Watchlist'.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        return;
                    }

                    String[] listNames = userListNameToIdMap.keySet().toArray(new String[0]);
                    if (listNames.length == 0) {
                        System.err.println("listNames array is empty despite map not being initially empty. This is unexpected.");
                        int confirmCreate = JOptionPane.showConfirmDialog(StreamingAppUI.this,
                                "No lists available. Create 'My Watchlist' and add this item?",
                                "Create List", JOptionPane.YES_NO_OPTION);
                        if (confirmCreate == JOptionPane.YES_OPTION) {
                            Integer watchlistId = getOrCreateListIdByName(currentLoggedInUserId, "My Watchlist", StreamingAppUI.this);
                            if (watchlistId != null) {
                                addItemToUserList(currentLoggedInUserId, watchlistId, contentItemId, StreamingAppUI.this);
                            }
                        }
                        return;
                    }
                    java.util.Arrays.sort(listNames);

                    String chosenListName = (String) JOptionPane.showInputDialog(
                            StreamingAppUI.this,
                            "Select a list to add '" + title + "':",
                            "Add to List",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            listNames,
                            listNames[0]);

                    if (chosenListName != null) {
                        Integer selectedListId = userListNameToIdMap.get(chosenListName);
                        if (selectedListId != null) {
                            System.out.println("Adding content ID " + contentItemId + " to list: " + chosenListName + " (ID: " + selectedListId + ")");
                            addItemToUserList(currentLoggedInUserId, selectedListId, contentItemId, StreamingAppUI.this);
                        } else {
                            System.err.println("Error: Could not find ID for selected list name: " + chosenListName);
                            JOptionPane.showMessageDialog(StreamingAppUI.this, "Error finding the selected list.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        System.out.println("User cancelled adding to list.");
                    }
                }
            }
        });
        return itemPanel;
    }
        
    
    private void addPlaceholderText(JPanel panel, String text, Color textColor, Font font) {
        panel.removeAll();  
        panel.setLayout(new GridBagLayout());  
        String placeholder = "?";  
        if (text != null && !text.trim().isEmpty()){
            placeholder = text.substring(0, Math.min(text.length(), 1)).toUpperCase();
        }
        JLabel placeholderLabel = new JLabel(placeholder);
        placeholderLabel.setFont(font);
        placeholderLabel.setForeground(textColor);
        placeholderLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(placeholderLabel, new GridBagConstraints());
        panel.revalidate();
        panel.repaint();
    }

    private JPanel createTopNavBar() {
    	
    	this.topNavPanel = new JPanel(new BorderLayout());
        this.topNavPanel.setBackground(new Color(0x101010));
        this.topNavPanel.setBorder(new EmptyBorder(15, 30, 15, 30));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 0)); 
        leftPanel.setOpaque(false);

        JLabel logoLabel = new JLabel("LUNA"); 
        logoLabel.setFont(new Font("Segoe UI Black", Font.BOLD, 28)); 
        logoLabel.setForeground(ACCENT_COLOR);
        logoLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        logoLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainCardLayout.show(mainCardPanel, HOME_PAGE_CARD); 
                if (sportsDrawer != null && sportsDrawer.isVisible()) {
                    sportsDrawer.dispose();
                    sportsDrawer = null;
                }
                if (filmDrawer != null && filmDrawer.isVisible()) {
                    filmDrawer.dispose();
                    filmDrawer = null;
                }
            }

            Color originalColor = logoLabel.getForeground();
            @Override
            public void mouseEntered(MouseEvent e) {
                logoLabel.setForeground(originalColor.brighter()); 
            }
            @Override
            public void mouseExited(MouseEvent e) {
                logoLabel.setForeground(originalColor); 
            }
        });
        
        leftPanel.add(logoLabel);
        leftPanel.add(Box.createHorizontalStrut(10)); 

        leftPanel.add(createNavLink("Special"));
        leftPanel.add(createNavLink("TV"));
        leftPanel.add(createNavLink("Series"));
        
        leftPanel.add(createFilmDrawerButton());   
        
        JButton sportButton = createNavLink("Sport");
        
        
        
        sportButton.addActionListener(e -> {
            if (sportsDrawer != null && sportsDrawer.isVisible()) {
                sportsDrawer.dispose(); 
                sportsDrawer = null;    
            } else {
                if (this.topNavPanel == null || this.topNavPanel.getHeight() == 0) {
                    System.err.println("Warning: topNavPanel might not be fully initialized when creating SportsCategoryDrawerDialog.");
                }
                sportsDrawer = new SportsCategoryDrawerDialog(StreamingAppUI.this, "Sports");
                sportsDrawer.setVisible(true);

                sportsDrawer.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                        sportsDrawer = null;
                    }
                });
            }
        });
        
        leftPanel.add(sportButton);
        
        
        JButton kidsButton = createNavLink("Kids");
        kidsButton.addActionListener(e -> {
            mainCardLayout.show(mainCardPanel, KIDS_PAGE_CARD);
            if (sportsDrawer != null && sportsDrawer.isVisible()) {
                sportsDrawer.dispose();
                sportsDrawer = null;
            }
            if (filmDrawer != null && filmDrawer.isVisible()) {
                filmDrawer.dispose();
                filmDrawer = null;
            }
        });
        leftPanel.add(kidsButton);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        rightPanel.setOpaque(false);

        JLabel searchIconLabel = new JLabel(getIcon("search.png", "Search"));
        searchIconLabel.setForeground(TEXT_COLOR);
        searchIconLabel.setFont(DEFAULT_FONT.deriveFont(22f));
        searchIconLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        
        searchIconLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainCardLayout.show(mainCardPanel, SEARCH_PAGE_CARD);
                searchPageCardLayout.show(searchPageContentHolder, SEARCH_TAGS_VIEW);
                searchFieldOnSearchPage.setText("Search for movies, series, actors, or directors...");
                searchFieldOnSearchPage.setForeground(TAG_TEXT_COLOR);
                
                if (sportsDrawer != null && sportsDrawer.isVisible()) {
                    sportsDrawer.dispose();
                    sportsDrawer = null;
                }
                if (filmDrawer != null && filmDrawer.isVisible()) {
                    filmDrawer.dispose();
                    filmDrawer = null;
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                searchIconLabel.setForeground(ACCENT_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                searchIconLabel.setForeground(TEXT_COLOR);
            }
        });
        
       
        
        rightPanel.add(searchIconLabel);

        rightPanel.add(createProfileDropDownMenu());

        this.topNavPanel.add(leftPanel, BorderLayout.WEST);
        this.topNavPanel.add(rightPanel, BorderLayout.EAST);

        return topNavPanel;
    }

    private JButton createHomePageLink(String text) {
        JButton button = createNavLink(text);
        button.addActionListener(e -> mainCardLayout.show(mainCardPanel, HOME_PAGE_CARD));
        return button;
    }
    
    private JButton createNavLink(String text) {
        JButton button = new JButton(text);
        button.setForeground(TEXT_COLOR);
        button.setFont(NAV_LINK_FONT);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.putClientProperty("originalColor", TEXT_COLOR); // Orijinal rengi sakla

        int underlineHeight = 2;
        Border emptyBorder = new EmptyBorder(0,0,underlineHeight,0);
        Border lineBorder = BorderFactory.createMatteBorder(0, 0, underlineHeight, 0, ACCENT_COLOR);
        Border compoundBorder = new CompoundBorder(emptyBorder, BorderFactory.createEmptyBorder(0,0,0,0));  

        button.setBorder(compoundBorder);


        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setForeground(ACCENT_COLOR);
                button.setBorder(new CompoundBorder(emptyBorder, lineBorder)); 
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setForeground((Color)button.getClientProperty("originalColor"));
                button.setBorder(compoundBorder); 
            }
        });
        return button;
    }
    
    private JComponent createFilmDrawerButton() {
        JButton filmButton = createNavLink("Film");
        filmButton.addActionListener(e -> {
            if (filmDrawer != null && filmDrawer.isVisible()) {
                filmDrawer.dispose();
                filmDrawer = null;
            } else {
                if (this.topNavPanel == null || this.topNavPanel.getHeight() == 0) {
                    System.err.println("Warning: topNavPanel might not be fully initialized when creating FilmCategoryDrawerDialog.");
                }
                filmDrawer = new FilmCategoryDrawerDialog(StreamingAppUI.this, "Movies", movieDataByCategory);
                filmDrawer.setVisible(true);
            }
        });
        return filmButton;
    }
    
    private void loadMovieDataFromDB() {
        movieDataByCategory = new HashMap<>(); 
        String sql = "SELECT g.genre_name, ci.title " +
                     "FROM genres g " +
                     "JOIN content_item_genres cig ON g.genre_id = cig.genre_id " +
                     "JOIN content_items ci ON cig.content_item_id = ci.content_item_id " +
                     "JOIN content_types ct ON ci.content_type_id = ct.content_type_id " +
                     "WHERE ct.type_name = 'Movie' " + 
                     "ORDER BY g.genre_name, ci.title";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (conn == null) { 
                System.err.println("loadMovieDataFromDB: Veritabanı bağlantısı kurulamadı!");
                return;
            }

            while (rs.next()) {
                String genreName = rs.getString("genre_name");
                String movieTitle = rs.getString("title");

                movieDataByCategory.computeIfAbsent(genreName, k -> new ArrayList<>()).add(movieTitle);
            }
            System.out.println("Film verileri veritabanından yüklendi. Toplam tür: " + movieDataByCategory.size());
        } catch (SQLException e) {
            System.err.println("Film verileri yüklenirken SQL hatası oluştu:");
            e.printStackTrace();
        }
      }
    
     private JMenuItem createModernMenuItem(String text) {
        JMenuItem item = new JMenuItem(text);
        item.setForeground(TEXT_COLOR);
        item.setBackground(PANEL_ITEM_BG_COLOR); 
        item.setFont(DEFAULT_FONT.deriveFont(13f));
        item.setBorder(new EmptyBorder(8, 15, 8, 15)); 
        item.setOpaque(true); 

        // Hover efekti
        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                item.setBackground(PANEL_ITEM_HOVER_BG_COLOR); 
            }

            @Override
            public void mouseExited(MouseEvent e) {
                item.setBackground(PANEL_ITEM_BG_COLOR); 
            }
        });
        return item;
    }
    
     private void updateProfileDropdownHeader() {
    	 if (profileMenuUserNameLabel != null) {
             profileMenuUserNameLabel.setText(this.currentLoggedInUserProfileName);
         }
         if (profileMenuUserIconLabel != null && this.currentLoggedInUserProfileName != null && !this.currentLoggedInUserProfileName.isEmpty()) {
             
              profileMenuUserIconLabel.setText(this.currentLoggedInUserProfileName.substring(0,1).toUpperCase());
         }
     }


  private JComponent createProfileDropDownMenu() {
      
      final int PROFILE_BUTTON_ICON_SIZE = 28; 
      Icon originalProfileIcon = getIcon("profile.png", "P");
      ImageIcon scaledProfileIcon = null;
      if (originalProfileIcon instanceof ImageIcon && ((ImageIcon) originalProfileIcon).getIconWidth() > 0) {
          scaledProfileIcon = getScaledImageIcon((ImageIcon) originalProfileIcon, PROFILE_BUTTON_ICON_SIZE, PROFILE_BUTTON_ICON_SIZE);
      }

      JButton profileButton = new JButton();
      if (scaledProfileIcon != null) {
          profileButton.setIcon(scaledProfileIcon);
      } else {
          
          profileButton.setText("P"); 
          profileButton.setFont(ICON_FONT.deriveFont((float)PROFILE_BUTTON_ICON_SIZE - 4)); 
          profileButton.setForeground(TEXT_COLOR);
      }

      profileButton.setPreferredSize(new Dimension(40, 40)); 
      profileButton.setOpaque(false);
      profileButton.setContentAreaFilled(false);
      profileButton.setBorderPainted(false);
      profileButton.setFocusPainted(false);
      profileButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
      

      
      JPopupMenu profileMenu = new JPopupMenu();
      profileMenu.setBackground(PANEL_ITEM_BG_COLOR);
      profileMenu.setBorder(new LineBorder(BORDER_COLOR_SUBTLE, 1));
      
      JPanel userHeaderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
      userHeaderPanel.setBackground(profileMenu.getBackground());
      userHeaderPanel.setBorder(new EmptyBorder(8, 10, 8, 10)); 
      final int USER_AVATAR_ICON_SIZE = 32; 
      String iconTextForAvatar = (this.currentLoggedInUserProfileName != null && !this.currentLoggedInUserProfileName.equals("Guest") && !this.currentLoggedInUserProfileName.isEmpty())
                               ? this.currentLoggedInUserProfileName.substring(0, 1).toUpperCase()
                               : "G"; 

      Icon originalUserAvatar = getIcon("user_avatar_generic.png", iconTextForAvatar); 
      ImageIcon scaledUserAvatar = null;
      if (originalUserAvatar instanceof ImageIcon && ((ImageIcon) originalUserAvatar).getIconWidth() > 0) {
          scaledUserAvatar = getScaledImageIcon((ImageIcon) originalUserAvatar, USER_AVATAR_ICON_SIZE, USER_AVATAR_ICON_SIZE);
      }

     
      if (profileMenuUserIconLabel == null) profileMenuUserIconLabel = new JLabel(); 
      if (scaledUserAvatar != null) {
          profileMenuUserIconLabel.setIcon(scaledUserAvatar);
          profileMenuUserIconLabel.setText(""); 
      } else {
          
          profileMenuUserIconLabel.setIcon(originalUserAvatar); 
          profileMenuUserIconLabel.setText("");
      }
      profileMenuUserIconLabel.setForeground(ACCENT_COLOR); 
      profileMenuUserIconLabel.setFont(DEFAULT_FONT.deriveFont(Font.BOLD, 18f)); 

      if (profileMenuUserNameLabel == null) profileMenuUserNameLabel = new JLabel(); 
      profileMenuUserNameLabel.setText(this.currentLoggedInUserProfileName);
      profileMenuUserNameLabel.setForeground(TEXT_COLOR);
      profileMenuUserNameLabel.setFont(TITLE_FONT);

      userHeaderPanel.add(profileMenuUserIconLabel);
      userHeaderPanel.add(profileMenuUserNameLabel);
      profileMenu.add(userHeaderPanel); 
      profileMenu.add(createModernSeparator()); 

      
      JMenuItem myProfileItem = createModernMenuItem("My Profile");
      myProfileItem.addActionListener(e -> {
          System.out.println("My Profile menu item clicked.");
          System.out.println("Current logged-in user ID: " + currentLoggedInUserId);

          if (currentLoggedInUserId != -1) {
              System.out.println("Proceeding to create and show profile page...");
              JPanel actualProfilePage = createProfilePagePanel(currentLoggedInUserId);
              if (actualProfilePage == null) {
                  System.err.println("Error: createProfilePagePanel returned null!");
                  return;
              }
              profilePagePanelHolder.removeAll();
              JScrollPane profileScrollPane = new JScrollPane(actualProfilePage);
              styleScrollBar(profileScrollPane.getVerticalScrollBar());
              profileScrollPane.setBorder(null);
              profilePagePanelHolder.add(profileScrollPane, BorderLayout.CENTER);
              profilePagePanelHolder.revalidate();
              profilePagePanelHolder.repaint();
              mainCardLayout.show(mainCardPanel, PROFILE_PAGE_CARD);
              System.out.println("Switched to PROFILE_PAGE_CARD.");
          } else {
              System.out.println("User not logged in. Showing warning.");
              JOptionPane.showMessageDialog(StreamingAppUI.this, "Please sign in to view your profile.", "Not Signed In", JOptionPane.WARNING_MESSAGE);
              this.dispose();
              SwingUtilities.invokeLater(AuthForm::new);
          }
      });
      profileMenu.add(myProfileItem);

      profileMenu.add(createModernSeparator());
      //profileMenu.add(createModernMenuItem("My Lists")); 
      //profileMenu.add(createModernSeparator());

      JMenuItem signOutItem = createModernMenuItem("Sign out");
      signOutItem.addActionListener(e -> {
          this.currentLoggedInUserId = -1;
          this.currentLoggedInUserProfileName = "Guest";
          // updateProfileDropdownHeader(); 

          if (sportsDrawer != null && sportsDrawer.isVisible()) sportsDrawer.dispose();
          if (filmDrawer != null && filmDrawer.isVisible()) filmDrawer.dispose();

          this.dispose();
          SwingUtilities.invokeLater(AuthForm::new);
      });
      //profileMenu.add(signOutItem);
      
      
      JMenuItem myListsItem = createModernMenuItem("My Lists");
      myListsItem.addActionListener(e -> {
          System.out.println("My Lists menu item clicked.");
          if (currentLoggedInUserId != -1) {
              JPanel actualMyListsPage = createMyListsPagePanel(currentLoggedInUserId); 
              myListsPagePanelHolder.removeAll();
              myListsPagePanelHolder.add(actualMyListsPage, BorderLayout.CENTER);
              myListsPagePanelHolder.revalidate();
              myListsPagePanelHolder.repaint();
              mainCardLayout.show(mainCardPanel, MY_LISTS_PAGE_CARD);
          } else {

               this.dispose(); SwingUtilities.invokeLater(AuthForm::new);
          }
      });
      profileMenu.add(myListsItem);
      profileMenu.add(createModernSeparator());
      profileMenu.add(signOutItem);


      profileButton.addActionListener(e -> {

         if (profileMenuUserNameLabel != null) {
             profileMenuUserNameLabel.setText(this.currentLoggedInUserProfileName);
         }
         if (profileMenuUserIconLabel != null) {
              String currentIconTextForAvatar = (this.currentLoggedInUserProfileName != null && !this.currentLoggedInUserProfileName.equals("Guest") && !this.currentLoggedInUserProfileName.isEmpty())
                                         ? this.currentLoggedInUserProfileName.substring(0, 1).toUpperCase()
                                         : "G";
              Icon currentOriginalUserAvatar = getIcon("user_avatar_generic.png", currentIconTextForAvatar);
              ImageIcon currentScaledUserAvatar = null;
              if (currentOriginalUserAvatar instanceof ImageIcon && ((ImageIcon) currentOriginalUserAvatar).getIconWidth() > 0) {
                  currentScaledUserAvatar = getScaledImageIcon((ImageIcon) currentOriginalUserAvatar, USER_AVATAR_ICON_SIZE, USER_AVATAR_ICON_SIZE);
              }

              if (currentScaledUserAvatar != null) {
                  profileMenuUserIconLabel.setIcon(currentScaledUserAvatar);
                  profileMenuUserIconLabel.setText("");
              } else {
                  profileMenuUserIconLabel.setIcon(currentOriginalUserAvatar); 
                  profileMenuUserIconLabel.setText("");
              }
         }
         profileMenu.show(profileButton, -profileMenu.getPreferredSize().width + profileButton.getWidth(), profileButton.getHeight() + 5);
     });

      return profileButton;
  }
    
    private JPanel createProfilePagePanel(int userId) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BACKGROUND_COLOR); 
        panel.setBorder(new EmptyBorder(30, 50, 30, 50));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(10, 0, 10, 0);

        JLabel titleLabel = new JLabel("My Profile");
        titleLabel.setFont(HERO_TITLE_FONT.deriveFont(36f));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.insets = new Insets(0, 0, 40, 0);
        panel.add(titleLabel, gbc);
        gbc.insets = new Insets(15, 0, 5, 0); 
        JTextField usernameField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        JLabel profileNameLabel = new JLabel(); 

        styleProfileTextField(usernameField);
        styleProfileTextField(emailField);
        profileNameLabel.setFont(DEFAULT_FONT.deriveFont(16f));
        profileNameLabel.setForeground(TEXT_COLOR_SECONDARY);


        String sqlUser = "SELECT username, email, profile_name FROM users WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlUser)) {
            if (conn == null) throw new SQLException("Database connection failed");
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    usernameField.setText(rs.getString("username"));
                    emailField.setText(rs.getString("email"));
                    profileNameLabel.setText(rs.getString("profile_name"));
                } else {
                    usernameField.setText("N/A");
                    emailField.setText("N/A");
                    profileNameLabel.setText("User Not Found");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            usernameField.setText("Error loading");
            emailField.setText("Error loading");
            profileNameLabel.setText("Error");
            JOptionPane.showMessageDialog(this, "Error loading profile data: " + e.getMessage(), "Profile Error", JOptionPane.ERROR_MESSAGE);
        }

        panel.add(createReadOnlyField("Profile Name:", profileNameLabel), gbc);

        panel.add(createEditableField("Username:", usernameField, "Change Username", newUsername -> {
            if (newUsername.trim().isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Username cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            return updateUserData(userId, "username", newUsername, panel);
        }), gbc);

        
        panel.add(createEditableField("Email:", emailField, "Change Email", newEmail -> {
            if (newEmail.trim().isEmpty() || !newEmail.contains("@")) { // Basit e-posta format kontrolü
                JOptionPane.showMessageDialog(panel, "Please enter a valid email.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            return updateUserData(userId, "email", newEmail, panel);
        }), gbc);
        gbc.insets = new Insets(30, 0, 5, 0);
        JLabel passwordSectionTitle = new JLabel("Change Password");
        passwordSectionTitle.setFont(TITLE_FONT.deriveFont(18f));
        passwordSectionTitle.setForeground(TEXT_COLOR_PRIMARY);
        panel.add(passwordSectionTitle, gbc);
        gbc.insets = new Insets(5, 0, 5, 0);

        JPasswordField currentPasswordField = new JPasswordField(20);
        JPasswordField newPasswordField = new JPasswordField(20);
        JPasswordField confirmPasswordField = new JPasswordField(20);
        styleProfileTextField(currentPasswordField);
        styleProfileTextField(newPasswordField);
        styleProfileTextField(confirmPasswordField);

        panel.add(createLabeledField("Current Password:", currentPasswordField), gbc);
        panel.add(createLabeledField("New Password:", newPasswordField), gbc);
        panel.add(createLabeledField("Confirm New Password:", confirmPasswordField), gbc);

        JButton changePasswordButton = createStyledButton("Update Password");
        changePasswordButton.addActionListener(e -> handleChangePassword(userId, currentPasswordField, newPasswordField, confirmPasswordField));
        gbc.insets = new Insets(15, 0, 30, 0);
        panel.add(changePasswordButton, gbc);


        // Hesap Silme
        JButton deleteAccountButton = createStyledButton("Delete My Account");
        deleteAccountButton.setBackground(new Color(180, 50, 50)); 
        deleteAccountButton.addActionListener(e -> handleDeleteAccount(userId));
        gbc.insets = new Insets(30, 0, 10, 0);
        panel.add(deleteAccountButton, gbc);

        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(Box.createGlue(), gbc);


        return panel;
    }
    private void styleProfileTextField(JTextField textField) {
        textField.setFont(INPUT_FONT);
        textField.setBackground(PANEL_ITEM_BG_COLOR.brighter()); 
        textField.setForeground(TEXT_COLOR_PRIMARY);
        textField.setCaretColor(ACCENT_COLOR);
        textField.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COLOR_SUBTLE, 1),
                new EmptyBorder(5, 8, 5, 8)
        ));
    }
    
    private JPanel createReadOnlyField(String labelText, JLabel valueLabel) {
        JPanel fieldPanel = new JPanel(new BorderLayout(10,0));
        fieldPanel.setOpaque(false);
        JLabel label = new JLabel(labelText);
        label.setFont(LABEL_FONT);
        label.setForeground(TEXT_COLOR_SECONDARY);
        label.setPreferredSize(new Dimension(150, 30)); // Etiket genişliğini sabitle
        fieldPanel.add(label, BorderLayout.WEST);
        fieldPanel.add(valueLabel, BorderLayout.CENTER);
        return fieldPanel;
    }
    
    private JPanel createLabeledField(String labelText, JComponent component) {
        JPanel fieldPanel = new JPanel(new BorderLayout(10,0));
        fieldPanel.setOpaque(false);
        JLabel label = new JLabel(labelText);
        label.setFont(LABEL_FONT);
        label.setForeground(TEXT_COLOR_SECONDARY);
        label.setPreferredSize(new Dimension(150, 30));
        fieldPanel.add(label, BorderLayout.WEST);
        fieldPanel.add(component, BorderLayout.CENTER);
        return fieldPanel;
    }
    
    private JPanel createEditableField(String labelText, JTextField textField, String buttonText, java.util.function.Function<String, Boolean> updateAction) {
        JPanel fieldPanel = new JPanel(new BorderLayout(10, 0));
        fieldPanel.setOpaque(false);

        JLabel label = new JLabel(labelText);
        label.setFont(LABEL_FONT);
        label.setForeground(TEXT_COLOR_SECONDARY);
        label.setPreferredSize(new Dimension(150,30));
        fieldPanel.add(label, BorderLayout.WEST);

        fieldPanel.add(textField, BorderLayout.CENTER);

        JButton changeButton = createStyledButton(buttonText);
        changeButton.setFont(BUTTON_FONT.deriveFont(12f)); 
        changeButton.setBorder(BorderFactory.createEmptyBorder(5,10,5,10));
        changeButton.addActionListener(e -> {
            String newValue = textField.getText();
            String confirmation = JOptionPane.showInputDialog(fieldPanel, "Enter new " + labelText.replace(":", "").toLowerCase() + " to confirm:", newValue);
            if (confirmation != null && !confirmation.trim().isEmpty()) {
                if (updateAction.apply(confirmation.trim())) {
                     textField.setText(confirmation.trim());
                } else {
                }
            }
        });
        fieldPanel.add(changeButton, BorderLayout.EAST);
        return fieldPanel;
    }
    
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT); 
        button.setBackground(BUTTON_BACKGROUND_COLOR);
        button.setForeground(BUTTON_TEXT_COLOR); 
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new MouseAdapter() {
            Color originalBg = button.getBackground();
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(originalBg.brighter());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(originalBg);
            }
        });
        return button;
    }

    private boolean updateUserData(int userId, String columnName, String newValue, Component parent) {
        String sql = "UPDATE users SET " + columnName + " = ? WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (conn == null) throw new SQLException("Database connection failed");
            pstmt.setString(1, newValue);
            pstmt.setInt(2, userId);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(parent, columnName + " updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                if (columnName.equals("profile_name")) { 
                    this.currentLoggedInUserProfileName = newValue;
                    updateProfileDropdownHeader(); 
                }
                return true;
            } else {
                JOptionPane.showMessageDialog(parent, "Failed to update " + columnName + ".", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (SQLException ex) {
             if (ex.getSQLState().equals("23000")) { 
                 JOptionPane.showMessageDialog(parent, columnName + " '" + newValue + "' already exists.", "Update Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(parent, "Database error updating " + columnName + ": " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            ex.printStackTrace();
            return false;
        }
    }
    
    private void handleChangePassword(int userId, JPasswordField currentPassField, JPasswordField newPassField, JPasswordField confirmPassField) {
        String currentPassword = new String(currentPassField.getPassword());
        String newPassword = new String(newPassField.getPassword());
        String confirmPassword = new String(confirmPassField.getPassword());

        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all password fields.", "Password Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!newPassword.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "New passwords do not match.", "Password Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String sqlSelect = "SELECT password_hash FROM users WHERE user_id = ?";
        String sqlUpdate = "UPDATE users SET password_hash = ? WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) throw new SQLException("Database connection failed");

            // 1. Mevcut şifreyi doğrula
            String dbHashedPassword = null;
            try (PreparedStatement pstmtSelect = conn.prepareStatement(sqlSelect)) {
                pstmtSelect.setInt(1, userId);
                try (ResultSet rs = pstmtSelect.executeQuery()) {
                    if (rs.next()) {
                        dbHashedPassword = rs.getString("password_hash");
                    } else {
                        JOptionPane.showMessageDialog(this, "User not found.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
            }
            if (dbHashedPassword == null || !BCrypt.checkpw(currentPassword, dbHashedPassword)) {
                JOptionPane.showMessageDialog(this, "Incorrect current password.", "Password Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String newHashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            try (PreparedStatement pstmtUpdate = conn.prepareStatement(sqlUpdate)) {
                pstmtUpdate.setString(1, newHashedPassword);
                pstmtUpdate.setInt(2, userId);
                int affectedRows = pstmtUpdate.executeUpdate();
                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(this, "Password updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    currentPassField.setText("");
                    newPassField.setText("");
                    confirmPassField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update password.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error changing password: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void handleDeleteAccount(int userId) {
        int confirmation = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete your account?\nThis action cannot be undone.",
                "Delete Account Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirmation == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM users WHERE user_id = ?";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                if (conn == null) throw new SQLException("Database connection failed");

                pstmt.setInt(1, userId);
                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(this, "Account deleted successfully.", "Account Deleted", JOptionPane.INFORMATION_MESSAGE);
                    
                    this.currentLoggedInUserId = -1;
                    this.currentLoggedInUserProfileName = "Guest";
                    this.dispose();
                    SwingUtilities.invokeLater(AuthForm::new);
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete account.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database error deleting account: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    
    private JSeparator createModernSeparator() {
        JSeparator separator = new JSeparator();
        separator.setForeground(BORDER_COLOR_SUBTLE.darker()); 
        separator.setBackground(BACKGROUND_COLOR); 
        return separator;
    }  
    private JPanel createHeroSection() {
        JPanel heroSectionPanel = new JPanel(new BorderLayout());
        heroSectionPanel.setBackground(Color.BLACK);
        heroSectionPanel.setPreferredSize(new Dimension(1200, 550));

        heroCardLayout = new CardLayout();
        heroPanelContainer = new JPanel(heroCardLayout);
        heroPanelContainer.setOpaque(false);

        List<Map<String, String>> slidesData = new ArrayList<>();
   
        String sql = "SELECT hs.title_override, hs.subtitle_override, hs.description_override, ci.title AS content_title " +
                "FROM hero_slides hs " +
                "JOIN content_items ci ON hs.content_item_id = ci.content_item_id " +
                "WHERE hs.is_active = TRUE ORDER BY hs.display_order ASC LIMIT 3";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Map<String, String> slide = new HashMap<>();
                slide.put("title", rs.getString("title_override") != null ? rs.getString("title_override") : rs.getString("content_title"));
                slide.put("subtitle", rs.getString("subtitle_override"));
                slide.put("description", rs.getString("description_override"));
                slidesData.add(slide);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            
            heroPanelContainer.add(createHeroSlide("Error Loading", "Database Connection Issue",
                                    "Could not load hero slides from the database."), "error_slide");
        }

        if (slidesData.isEmpty() && heroPanelContainer.getComponentCount() == 0) { 
             heroPanelContainer.add(createHeroSlide("No Content", "Coming Soon",
                                    "Exciting new content is on its way!"), "default_slide");
        } else {
            for (int i = 0; i < slidesData.size(); i++) {
                Map<String, String> slide = slidesData.get(i);
                heroPanelContainer.add(createHeroSlide(
                        slide.get("title"),
                        slide.get("subtitle"),
                        slide.get("description")
                ), "slide" + (i + 1));
            }
        }

        heroSectionPanel.add(heroPanelContainer, BorderLayout.CENTER);
        final int totalSlides = heroPanelContainer.getComponentCount(); 
        currentHeroSlide = 0; 

        carouselDotsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        carouselDotsPanel.setOpaque(false);
        carouselDotsPanel.setBorder(new EmptyBorder(0,0,10,0));
        ButtonGroup dotsGroup = new ButtonGroup();

        for (int i = 0; i < totalSlides; i++) { 
            JRadioButton dot = new JRadioButton();
            dot.setIcon(createDotIcon(false));
            dot.setSelectedIcon(createDotIcon(true));
            final int slideIndex = i;
            dot.addActionListener(e -> {
                Component cardComponent = heroPanelContainer.getComponent(slideIndex);
                String cardName = null;
               
                if (slidesData.size() > 0 && slideIndex < slidesData.size()) { 
                     cardName = "slide" + (slideIndex + 1);
                } else if (heroPanelContainer.getComponent(slideIndex) != null) { 
                    
                    try {
                         heroCardLayout.show(heroPanelContainer, ((JComponent)cardComponent).getName()); 
                    } catch (Exception ex) {
                        
                        if (heroPanelContainer.getComponentCount() > 0) heroCardLayout.first(heroPanelContainer);
                    }
                }


                currentHeroSlide = slideIndex;
                updateCarouselDots();
            });
            dotsGroup.add(dot);
            carouselDotsPanel.add(dot);
        }
        heroSectionPanel.add(carouselDotsPanel, BorderLayout.SOUTH);
        if (totalSlides > 0) updateCarouselDots();


        if (totalSlides > 1) { 
            Timer timer = new Timer(7000, e -> {
                currentHeroSlide = (currentHeroSlide + 1) % totalSlides;
                heroCardLayout.next(heroPanelContainer);
                int activeCardIndex = 0;
                for(int i=0; i < heroPanelContainer.getComponentCount(); ++i) {
                    if(heroPanelContainer.getComponent(i).isVisible()) {
                        activeCardIndex = i;
                        break;
                    }
                }
                currentHeroSlide = activeCardIndex;
                updateCarouselDots();
            });

        }

        return heroSectionPanel;
    }
    
    

    private Icon createDotIcon(boolean selected) {
        return new Icon() {
            private int size = 8;
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (selected) {
                    g2.setColor(ACCENT_COLOR);
                } else {
                    g2.setColor(Color.GRAY);
                }
                g2.fillOval(x, y + (getIconHeight() - size)/2, size, size);
                g2.dispose();
            }
            @Override public int getIconWidth() { return size + 4; } 
            @Override public int getIconHeight() { return size + 4; }
        };
    }
    
    private void updateCarouselDots() {
        if (carouselDotsPanel.getComponentCount() > currentHeroSlide) {
            Component comp = carouselDotsPanel.getComponent(currentHeroSlide);
            if (comp instanceof JRadioButton) {
                ((JRadioButton) comp).setSelected(true);
            }
        }
    }
    private JPanel createHeroSlide(String title, String subtitle, String description) { 
	     JPanel slidePanel = new JPanel() {
	         @Override
	         protected void paintComponent(Graphics g) {
	             super.paintComponent(g);

	             g.setColor(new Color(0x1E1E1E)); 
	             g.fillRect(0, 0, getWidth(), getHeight());
	
	             if (title != null && !title.isEmpty() && TEXT_COLOR != null && HERO_TITLE_FONT != null) {
	                 g.setColor(TEXT_COLOR);
	                 g.setFont(HERO_TITLE_FONT.deriveFont(150f));
	                 FontMetrics fm = g.getFontMetrics();
	                 String initial = title.substring(0, 1).toUpperCase();
	                 int x = (getWidth() - fm.stringWidth(initial)) / 2;
	                 int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
	                 
	                 g.drawString(initial, x, y - getHeight() / 6); 
	             }
	
	             
	             Graphics2D g2d = (Graphics2D) g.create();
	             GradientPaint gp = new GradientPaint(
	                     0, getHeight() * 0.4f, new Color(0, 0, 0, 0), 
	                     0, getHeight(), new Color(0, 0, 0, 200));   
	             g2d.setPaint(gp);
	             g2d.fillRect(0, 0, getWidth(), getHeight());
	             g2d.dispose();
	         }
	     };
	     slidePanel.setLayout(new GridBagLayout());
	     GridBagConstraints gbc = new GridBagConstraints();
	     
	
	    
	     JPanel textContainer = new JPanel();
	     textContainer.setOpaque(false); 
	     textContainer.setLayout(new BoxLayout(textContainer, BoxLayout.Y_AXIS));
	     
	     if (title != null && !title.isEmpty() && HERO_TITLE_FONT != null && TEXT_COLOR != null) {
	         JLabel titleLabel = new JLabel(title);
	         titleLabel.setFont(HERO_TITLE_FONT);
	         titleLabel.setForeground(TEXT_COLOR);
	         titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	         textContainer.add(titleLabel);
	     }
	
	     if (subtitle != null && !subtitle.isEmpty() && DEFAULT_FONT != null && TEXT_COLOR != null) {
	         textContainer.add(Box.createRigidArea(new Dimension(0, 8)));
	         JLabel subtitleLabel = new JLabel(subtitle);
	         subtitleLabel.setFont(DEFAULT_FONT.deriveFont(Font.BOLD, 20f)); 
	         subtitleLabel.setForeground(new Color(0xDDDDDD)); 
	         subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT); 
	         textContainer.add(subtitleLabel);
	     }
	
	     if (description != null && !description.isEmpty() && DEFAULT_FONT != null) {
	         textContainer.add(Box.createRigidArea(new Dimension(0, 25)));
	         JTextArea descriptionArea = new JTextArea(description);
	         descriptionArea.setFont(DEFAULT_FONT.deriveFont(17f));
	         descriptionArea.setForeground(new Color(0xB0B0B0));
	         descriptionArea.setOpaque(false);
	         descriptionArea.setEditable(false);
	         descriptionArea.setLineWrap(true);
	         descriptionArea.setWrapStyleWord(true);
	         descriptionArea.setMaximumSize(new Dimension(600, 120)); 
	         descriptionArea.setAlignmentX(Component.CENTER_ALIGNMENT); 
	         textContainer.add(descriptionArea);
	     }
	     

	     JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0)); 
	     buttonsPanel.setOpaque(false);
	
	     if (TEXT_COLOR != null && ACCENT_COLOR != null && TITLE_FONT != null) { 
	         JButton playButton = createStyledButton("▶ Watch Now"); 
	        
	         buttonsPanel.add(playButton);
	
	         JButton detailsButton = createStyledButton("ⓘ More Info");
	         
	         buttonsPanel.add(detailsButton);
	     }
	     
	     textContainer.add(Box.createRigidArea(new Dimension(0, 35)));
	     textContainer.add(buttonsPanel);
	
	     
	     gbc.gridx = 0;
	     gbc.gridy = 0;
	     gbc.weightx = 1.0;
	     gbc.weighty = 1.0;
	     gbc.anchor = GridBagConstraints.CENTER; 
	     slidePanel.add(textContainer, gbc);
	
	     return slidePanel;
	 }
    
    private void styleHeroButton(JButton button, Color bgColor, Color fgColor) {
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFont(TITLE_FONT.deriveFont(15f));
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(12, 25, 12, 25)); 
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        // Hover efekti
        button.addMouseListener(new MouseAdapter() {
            Color originalBg = button.getBackground();
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(originalBg.brighter());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(originalBg);
            }
        });
    }
    

 private boolean addItemToUserList(int userId, int listId, int contentItemId, Component parentComponent) {
    
     String checkSql = "SELECT COUNT(*) FROM user_list_items WHERE list_id = ? AND content_item_id = ?";
     String insertSql = "INSERT INTO user_list_items (list_id, user_id, content_item_id) VALUES (?, ?, ?)";
     
     insertSql = "INSERT INTO user_list_items (list_id, content_item_id) VALUES (?, ?)";


     try (Connection conn = DBConnection.getConnection()) {
         if (conn == null) {
             JOptionPane.showMessageDialog(parentComponent, "Database connection error.", "Error", JOptionPane.ERROR_MESSAGE);
             return false;
         }

         
         try (PreparedStatement pstmtCheck = conn.prepareStatement(checkSql)) {
             pstmtCheck.setInt(1, listId);
             pstmtCheck.setInt(2, contentItemId);
             try (ResultSet rs = pstmtCheck.executeQuery()) {
                 if (rs.next() && rs.getInt(1) > 0) {
                     JOptionPane.showMessageDialog(parentComponent, "This item is already in the selected list.", "Already Added", JOptionPane.INFORMATION_MESSAGE);
                     return false; 
                 }
             }
         }

      
         try (PreparedStatement pstmtInsert = conn.prepareStatement(insertSql)) {
             pstmtInsert.setInt(1, listId);
             pstmtInsert.setInt(2, contentItemId);
            
             int affectedRows = pstmtInsert.executeUpdate();
             if (affectedRows > 0) {
                 JOptionPane.showMessageDialog(parentComponent, "Item added to list successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                 return true;
             } else {
                 JOptionPane.showMessageDialog(parentComponent, "Failed to add item to list.", "Error", JOptionPane.ERROR_MESSAGE);
                 return false;
             }
         }
     } catch (SQLException ex) {
         if (ex.getSQLState() != null && ex.getSQLState().equals("23000")) { 
             JOptionPane.showMessageDialog(parentComponent, "This item might already be in the list (DB constraint).", "Error", JOptionPane.ERROR_MESSAGE);
         } else {
             JOptionPane.showMessageDialog(parentComponent, "Database error adding item to list: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
         }
         ex.printStackTrace();
         return false;
     }
 }


 private Integer getOrCreateListIdByName(int userId, String targetListName, Component parentComponent) {
     
     if (userListNameToIdMap != null && userListNameToIdMap.containsKey(targetListName)) {
         return userListNameToIdMap.get(targetListName);
     }

     String selectSql = "SELECT list_id FROM user_lists WHERE user_id = ? AND list_name = ?";
     String insertSql = "INSERT INTO user_lists (user_id, list_name) VALUES (?, ?)";

     try (Connection conn = DBConnection.getConnection()) {
         if (conn == null) {
             JOptionPane.showMessageDialog(parentComponent, "Database connection error.", "Error", JOptionPane.ERROR_MESSAGE);
             return null;
         }

         Integer listId = null;
         
         try (PreparedStatement pstmtSelect = conn.prepareStatement(selectSql)) {
             pstmtSelect.setInt(1, userId);
             pstmtSelect.setString(2, targetListName);
             try (ResultSet rs = pstmtSelect.executeQuery()) {
                 if (rs.next()) {
                     listId = rs.getInt("list_id");
                 }
             }
         }

         
         if (listId == null) {
             System.out.println("List '" + targetListName + "' not found for user " + userId + ". Creating it...");
             try (PreparedStatement pstmtInsert = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                 pstmtInsert.setInt(1, userId);
                 pstmtInsert.setString(2, targetListName);
                 int affectedRows = pstmtInsert.executeUpdate();
                 if (affectedRows > 0) {
                     try (ResultSet generatedKeys = pstmtInsert.getGeneratedKeys()) {
                         if (generatedKeys.next()) {
                             listId = generatedKeys.getInt(1);
                             System.out.println("Created list '" + targetListName + "' with ID: " + listId);
                             
                             if (userListNameToIdMap != null) {
                                 userListNameToIdMap.put(targetListName, listId);
                             }

                             if (userListsModel != null && SwingUtilities.isEventDispatchThread()){ 
                                 userListsModel.addElement(targetListName); 
                             } else if (userListsModel != null) {
                                 SwingUtilities.invokeLater(() -> userListsModel.addElement(targetListName));
                             }


                         }
                     }
                 }
             }
         }
         if (listId != null && userListNameToIdMap != null) { 
              userListNameToIdMap.putIfAbsent(targetListName, listId);
         }
         return listId;

     } catch (SQLException ex) {
         JOptionPane.showMessageDialog(parentComponent, "Database error finding or creating list: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
         ex.printStackTrace();
         return null;
     }
 }


    private JPanel createSectionPanel(String title, JPanel itemsPanel, boolean showAllLink) {
        JPanel sectionPanel = new JPanel(new BorderLayout(0, 15)); 
        sectionPanel.setBackground(BACKGROUND_COLOR);
        sectionPanel.setBorder(new EmptyBorder(0, 30, 0, 30)); 

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel(title);
        if (title.equals("Kanallar")) { 
            titleLabel.setText(title + " ");
        }
        titleLabel.setFont(TITLE_FONT.deriveFont(22f)); 
        titleLabel.setForeground(TEXT_COLOR);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        if (showAllLink) {
            JLabel tumuLabel = new JLabel("Tümü →");
            tumuLabel.setFont(DEFAULT_FONT.deriveFont(15f));
            tumuLabel.setForeground(ACCENT_COLOR);
            tumuLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
             tumuLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    tumuLabel.setForeground(ACCENT_COLOR.brighter());
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    tumuLabel.setForeground(ACCENT_COLOR);
                }
            });
            headerPanel.add(tumuLabel, BorderLayout.EAST);
        }
        sectionPanel.add(headerPanel, BorderLayout.NORTH);

        JScrollPane itemsScrollPane = new JScrollPane(itemsPanel);
        itemsScrollPane.setBorder(null);
        itemsScrollPane.setOpaque(false);
        itemsScrollPane.getViewport().setOpaque(false);
        itemsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        itemsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        itemsScrollPane.getHorizontalScrollBar().setUnitIncrement(16);
        styleScrollBar(itemsScrollPane.getHorizontalScrollBar()); 
        int scrollPaneHeight = itemsPanel.getPreferredSize().height + (itemsScrollPane.getHorizontalScrollBar().isVisible() ? itemsScrollPane.getHorizontalScrollBar().getPreferredSize().height : 0) + 10; // 10 padding
        itemsScrollPane.setPreferredSize(new Dimension(100, scrollPaneHeight));


        sectionPanel.add(itemsScrollPane, BorderLayout.CENTER);
        return sectionPanel;
    }
    
 
/*
    private JPanel createChannelItems() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(0,0,5,0));

        List<Map<String, String>> channelsData = new ArrayList<>();
        
        String sql = "SELECT ci.title " +
                "FROM content_items ci " +
                "JOIN content_types ct ON ci.content_type_id = ct.content_type_id " +
                "WHERE ct.type_name = 'Channel' " +
                "ORDER BY ci.title ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Map<String, String> channel = new HashMap<>();
                channel.put("name", rs.getString("title"));
                channelsData.add(channel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (channelsData.isEmpty()) {
            panel.add(new JLabel("No channels found."){{setForeground(TEXT_COLOR);}});
        } else {
            for (Map<String, String> channelData : channelsData) {
                panel.add(createChannelItem(channelData.get("name")));
            }
        }
        int itemCount = Math.max(1, channelsData.size()); 
        int preferredWidth = (160 + 15) * itemCount;
        panel.setPreferredSize(new Dimension(preferredWidth, 100));
        return panel;
    }

    private JPanel createChannelItem(String channelName) {
        JPanel itemPanel = new JPanel(new GridBagLayout());
        itemPanel.setBackground(PANEL_ITEM_BG_COLOR);
        itemPanel.setPreferredSize(new Dimension(160, 90)); 
        itemPanel.setBorder(new LineBorder(PANEL_ITEM_BG_COLOR, 2)); 
        itemPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel nameLabel = new JLabel(channelName.toUpperCase());
        nameLabel.setForeground(TEXT_COLOR);
        nameLabel.setFont(DEFAULT_FONT.deriveFont(Font.BOLD, 13f));
        
        GridBagConstraints gbc = new GridBagConstraints();
        itemPanel.add(nameLabel, gbc);

        itemPanel.addMouseListener(new MouseAdapter() {
            Border hoverBorder = new LineBorder(ACCENT_COLOR, 2);
            Border defaultBorder = itemPanel.getBorder();
            Color defaultBg = itemPanel.getBackground();

            @Override
            public void mouseEntered(MouseEvent e) {
                itemPanel.setBackground(PANEL_ITEM_HOVER_BG_COLOR);
                itemPanel.setBorder(hoverBorder);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                itemPanel.setBackground(defaultBg);
                itemPanel.setBorder(defaultBorder);
            }
        });
        return itemPanel;
    }

 // StreamingAppUI.java -> createChannelItem
    private JPanel createChannelItem(String channelName, String logoNameFromDBorCode, final int contentItemId) {
        JPanel itemPanel = new JPanel(new GridBagLayout()); // Logoyu ortalamak için
        // ... (itemPanel ayarları) ...
        itemPanel.setBackground(PANEL_ITEM_BG_COLOR);
        itemPanel.setPreferredSize(new Dimension(160, 90)); 
        itemPanel.setBorder(new LineBorder(PANEL_ITEM_BG_COLOR, 2)); 
        itemPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        final int LOGO_TARGET_WIDTH = 140; // Hedef logo genişliği
        final int LOGO_TARGET_HEIGHT = 70; // Hedef logo yüksekliği

        Icon originalIcon = null;
        if (logoNameFromDBorCode != null && !logoNameFromDBorCode.trim().isEmpty()) {
            originalIcon = getIcon(logoNameFromDBorCode, null);
        }

        if (originalIcon instanceof ImageIcon && ((ImageIcon)originalIcon).getIconWidth() > 0) {
            ImageIcon scaledLogo = getScaledImageIcon((ImageIcon)originalIcon, LOGO_TARGET_WIDTH, LOGO_TARGET_HEIGHT);
            if (scaledLogo != null) {
                itemPanel.add(new JLabel(scaledLogo), new GridBagConstraints());
            } else {
                addPlaceholderTextToChannel(itemPanel, channelName);
            }
        } else {
            addPlaceholderTextToChannel(itemPanel, channelName);
             if (logoNameFromDBorCode != null && !logoNameFromDBorCode.trim().isEmpty()){
                System.err.println("Uyarı: Kanal '" + channelName + "' için logo '" + logoNameFromDBorCode + "' yüklenemedi.");
            }
        }
        // ... (MouseListener) ...
        return itemPanel;
    }*/
    
    
    
 
 private JPanel createChannelItems() {
     JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
     panel.setBackground(BACKGROUND_COLOR);
     panel.setBorder(new EmptyBorder(0,0,5,0));

     List<Map<String, Object>> channelsData = new ArrayList<>();

     
     String sql = "SELECT ci.content_item_id, ci.title " +
                  "FROM content_items ci " +
                  "JOIN content_types ct ON ci.content_type_id = ct.content_type_id " +
                  "WHERE ct.type_name = 'Channel' " +
                  "ORDER BY ci.title ASC";

     try (Connection conn = DBConnection.getConnection();
          PreparedStatement pstmt = conn.prepareStatement(sql);
          ResultSet rs = pstmt.executeQuery()) {

         if (conn == null) {
             System.err.println("createChannelItems: Database connection NULL");
             panel.add(new JLabel("Database connection error."){{setForeground(TEXT_COLOR);}});
             return panel;
         }

         while (rs.next()) {
             Map<String, Object> channel = new HashMap<>();
             channel.put("id", rs.getInt("content_item_id")); 
             channel.put("name", rs.getString("title"));

             channelsData.add(channel);
         }
     } catch (SQLException e) {
         System.err.println("Error loading channel items: " + e.getMessage());
         e.printStackTrace();
         panel.add(new JLabel("Error loading channels."){{setForeground(TEXT_COLOR);}});
     }

     if (channelsData.isEmpty()) {
         panel.add(new JLabel("No channels found."){{setForeground(TEXT_COLOR);}});
     } else {
         for (Map<String, Object> channelData : channelsData) {
             
             panel.add(createChannelItem(
                 (String) channelData.get("name"),
                 null, 
                 (int) channelData.get("id")
             ));
         }
     }
     int itemCount = Math.max(1, channelsData.size());
     int preferredWidth = (160 + 15) * itemCount; 
     panel.setPreferredSize(new Dimension(preferredWidth, 110)); 
     return panel;
 }


 
 private JPanel createChannelItem(String channelName, String logoNameFromDBorCode_UNUSED, final int contentItemId) {
    
     JPanel itemPanel = new JPanel(new GridBagLayout());
     itemPanel.setBackground(PANEL_ITEM_BG_COLOR);
     itemPanel.setPreferredSize(new Dimension(160, 90));
     itemPanel.setBorder(new LineBorder(PANEL_ITEM_BG_COLOR, 2));
     itemPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

     
     JLabel nameLabel = new JLabel(channelName.toUpperCase());
     nameLabel.setForeground(TEXT_COLOR);
     nameLabel.setFont(DEFAULT_FONT.deriveFont(Font.BOLD, 13f));
     GridBagConstraints gbc = new GridBagConstraints();
     itemPanel.add(nameLabel, gbc);


     itemPanel.addMouseListener(new MouseAdapter() {
         Border hoverBorder = new LineBorder(ACCENT_COLOR, 2); 
         Border defaultBorder = itemPanel.getBorder();
         Color defaultBg = itemPanel.getBackground();
         Color hoverBg = PANEL_ITEM_HOVER_BG_COLOR; 

         @Override
         public void mouseEntered(MouseEvent e) {
             itemPanel.setBackground(hoverBg);
             itemPanel.setBorder(hoverBorder);
         }

         @Override
         public void mouseExited(MouseEvent e) {
             itemPanel.setBackground(defaultBg);
             itemPanel.setBorder(defaultBorder);
         }
         
         @Override
         public void mouseClicked(MouseEvent e) {
             if (currentLoggedInUserId == -1) { 
                 JOptionPane.showMessageDialog(StreamingAppUI.this, "Please sign in to add channels to your favorites.", "Sign In Required", JOptionPane.WARNING_MESSAGE);
                 StreamingAppUI.this.dispose();
                 SwingUtilities.invokeLater(AuthForm::new);
                 return;
             }

             int choice = JOptionPane.showConfirmDialog(StreamingAppUI.this,
                     "Add '" + channelName + "' to your Favorite Channels?",
                     "Add to Favorites",
                     JOptionPane.YES_NO_OPTION,
                     JOptionPane.QUESTION_MESSAGE);

             if (choice == JOptionPane.YES_OPTION) {

                 Integer favoriteChannelsListId = getOrCreateListIdByName(currentLoggedInUserId, "Favorite Channels", StreamingAppUI.this);
                 if (favoriteChannelsListId != null) {
                  
                     addItemToUserList(currentLoggedInUserId, favoriteChannelsListId, contentItemId, StreamingAppUI.this);
                 } else {
                      JOptionPane.showMessageDialog(StreamingAppUI.this, "Could not access or create 'Favorite Channels' list.", "Error", JOptionPane.ERROR_MESSAGE);
                 }
             }
         }
     });
     return itemPanel;
 }

    
    
    
    private void addPlaceholderTextToChannel(JPanel panel, String channelName){
        JLabel nameLabel = new JLabel(channelName.toUpperCase());
        nameLabel.setForeground(TEXT_COLOR);
        nameLabel.setFont(DEFAULT_FONT.deriveFont(Font.BOLD, 13f));
        panel.add(nameLabel, new GridBagConstraints());
    }
    
    private JPanel createMyListsPagePanel(int userId) {
        System.out.println("Creating My Lists Page for user ID: " + userId); 
        JPanel mainMyListsPanel = new JPanel(new BorderLayout(10, 0)); 
        mainMyListsPanel.setBackground(BACKGROUND_COLOR);
        mainMyListsPanel.setBorder(new EmptyBorder(20, 20, 20, 20)); 


        userListsModel = new DefaultListModel<>();
        userListsDisplayJList = new JList<>(userListsModel);
        userListsDisplayJList.setBackground(PANEL_ITEM_BG_COLOR);
        userListsDisplayJList.setForeground(TEXT_COLOR);
        userListsDisplayJList.setFont(DEFAULT_FONT.deriveFont(16f));
        userListsDisplayJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userListsDisplayJList.setFixedCellHeight(50); 
        userListsDisplayJList.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_COLOR_SUBTLE, 1),
            new EmptyBorder(10,10,10,10)
        ));

        userListsDisplayJList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                Icon listIcon = StreamingAppUI.this.getIcon("list_icon.png", "L:");
                label.setIcon(listIcon);
                
                //label.setIcon(getIcon("list_icon.png", "L:")); 
                label.setIconTextGap(10);
                if (isSelected) {
                    label.setBackground(ACCENT_COLOR.darker());
                    label.setForeground(Color.WHITE);
                } else {
                    label.setBackground(PANEL_ITEM_BG_COLOR);
                    label.setForeground(TEXT_COLOR);
                }
                return label;
            }
        });



        selectedListContentPanel = new JPanel();
        selectedListContentPanel.setLayout(new BoxLayout(selectedListContentPanel, BoxLayout.Y_AXIS)); 
        selectedListContentPanel.setBackground(BACKGROUND_COLOR); 
        selectedListContentPanel.setBorder(new EmptyBorder(0, 10, 0, 0)); 

        JScrollPane contentScrollPane = new JScrollPane(selectedListContentPanel);
        styleScrollPaneAsPanel(contentScrollPane);
        
        userListsDisplayJList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) { 
                String selectedListName = userListsDisplayJList.getSelectedValue();
                if (selectedListName != null) {
                    System.out.println("Selected list: " + selectedListName); 
                    Integer selectedListId = userListNameToIdMap.get(selectedListName);
                    if (selectedListId != null) {
                        displayContentForList(selectedListId, selectedListName);
                    } else {
                        System.err.println("Error: Could not find ID for list name: " + selectedListName);
                    }
                }
            }
        });

        
        loadUserListsForCurrentUser(userId); 

        JScrollPane listScrollPane = new JScrollPane(userListsDisplayJList);
        listScrollPane.setPreferredSize(new Dimension(250, 0));
        styleScrollPaneAsPanel(listScrollPane);

        mainMyListsPanel.add(listScrollPane, BorderLayout.WEST);
        mainMyListsPanel.add(contentScrollPane, BorderLayout.CENTER);


        if (!userListsModel.isEmpty()) {
            userListsDisplayJList.setSelectedIndex(0);
        } else {
            selectedListContentPanel.removeAll();
            JLabel noListsLabel = new JLabel("You don't have any lists yet. Start creating them!");
            noListsLabel.setFont(TITLE_FONT);
            noListsLabel.setForeground(TEXT_COLOR_SECONDARY);
            noListsLabel.setHorizontalAlignment(SwingConstants.CENTER);
            selectedListContentPanel.setLayout(new GridBagLayout()); 
            selectedListContentPanel.add(noListsLabel, new GridBagConstraints());
            selectedListContentPanel.revalidate();
            selectedListContentPanel.repaint();
        }


        return mainMyListsPanel;
    }


    private void loadUserListsForCurrentUser(int userId) {
        if (userListsModel == null) userListsModel = new DefaultListModel<>();
        userListsModel.clear();

        if (userListNameToIdMap == null) userListNameToIdMap = new HashMap<>();
        userListNameToIdMap.clear();

        String sql = "SELECT list_id, list_name FROM user_lists WHERE user_id = ? ORDER BY list_name ASC";
        System.out.println("Loading lists for user ID: " + userId); // DEBUG

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (conn == null) {
                System.err.println("loadUserListsForCurrentUser: Database connection NULL");
                userListsModel.addElement("Error loading lists");
                return;
            }
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                boolean found = false;
                while (rs.next()) {
                    found = true;
                    String listName = rs.getString("list_name");
                    int listId = rs.getInt("list_id");
                    userListsModel.addElement(listName);
                    userListNameToIdMap.put(listName, listId); 
                    System.out.println("Loaded list: " + listName + " (ID: " + listId + ")"); 
                }
                if (!found) {
                    System.out.println("No lists found for user ID: " + userId); 
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL error while loading user lists for user ID " + userId + ":");
            e.printStackTrace();
            userListsModel.addElement("Error fetching lists");
        }
    }

    
    private void displayContentForList(int listId, String listName) {
        selectedListContentPanel.removeAll();
        selectedListContentPanel.setLayout(new BoxLayout(selectedListContentPanel, BoxLayout.Y_AXIS)); 
        
        JLabel listTitleLabel = new JLabel(listName);
        listTitleLabel.setFont(TITLE_FONT.deriveFont(24f)); 
        listTitleLabel.setForeground(TEXT_COLOR); 
        listTitleLabel.setBorder(new EmptyBorder(0,0,15,0));
        listTitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        selectedListContentPanel.add(listTitleLabel);

        String sql = "SELECT ci.content_item_id, ci.title, ct.type_name, " +
                     "GROUP_CONCAT(DISTINCT g.genre_name SEPARATOR ', ') AS genres " +
                     "FROM user_list_items uli " +
                     "JOIN content_items ci ON uli.content_item_id = ci.content_item_id " +
                     "JOIN content_types ct ON ci.content_type_id = ct.content_type_id " +
                     "LEFT JOIN content_item_genres cig ON ci.content_item_id = cig.content_item_id " +
                     "LEFT JOIN genres g ON cig.genre_id = g.genre_id " +
                     "WHERE uli.list_id = ? " +
                     "GROUP BY ci.content_item_id, ci.title, ct.type_name " +
                     "ORDER BY uli.added_at DESC"; 
        System.out.println("Fetching content for list ID: " + listId + " (" + listName + ")"); 

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (conn == null) {
                System.err.println("displayContentForList: Database connection NULL");
                selectedListContentPanel.add(new JLabel("Error: DB Connection."){{setForeground(TEXT_COLOR);}});
                return;
            }
            pstmt.setInt(1, listId);
            try (ResultSet rs = pstmt.executeQuery()) {
                boolean foundContent = false;
                while (rs.next()) {
                    foundContent = true;
                    String contentTitle = rs.getString("title");
                    String contentType = rs.getString("type_name");
                    String contentGenres = rs.getString("genres");
                    int contentItemId = rs.getInt("content_item_id");

                   
                    JPanel contentCard = createListedItemCard(contentTitle, contentType, contentGenres, listId, contentItemId);
                    contentCard.setAlignmentX(Component.LEFT_ALIGNMENT);
                    selectedListContentPanel.add(contentCard);
                    selectedListContentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                }
                if (!foundContent) {
                    JLabel noContentLabel = new JLabel("This list is empty. Add some content!");
                    noContentLabel.setFont(DEFAULT_FONT);
                    noContentLabel.setForeground(TEXT_COLOR_SECONDARY);
                    selectedListContentPanel.add(noContentLabel);
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL error while loading content for list ID " + listId + ":");
            e.printStackTrace();
            selectedListContentPanel.add(new JLabel("Error loading content."){{setForeground(TEXT_COLOR);}});
        }

        selectedListContentPanel.revalidate();
        selectedListContentPanel.repaint();
    }

    
    private JPanel createListedItemCard(String title, String type, String genres, final int listId, final int contentItemId) {
        JPanel cardPanel = new JPanel(new BorderLayout(10, 0));
        cardPanel.setBackground(PANEL_ITEM_BG_COLOR.darker()); 
        cardPanel.setBorder(new CompoundBorder(new LineBorder(BORDER_COLOR_SUBTLE), new EmptyBorder(10,15,10,10)));
        cardPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80)); 

        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(TITLE_FONT.deriveFont(16f)); 
        titleLabel.setForeground(TEXT_COLOR);
        textPanel.add(titleLabel);

        JLabel detailsLabel = new JLabel(type + (genres != null && !genres.isEmpty() ? " - " + genres : ""));
        detailsLabel.setFont(DEFAULT_FONT.deriveFont(13f)); 
        detailsLabel.setForeground(TEXT_COLOR_SECONDARY);
        textPanel.add(detailsLabel);

        cardPanel.add(textPanel, BorderLayout.CENTER);

        JButton removeFromListButton = new JButton("X"); 
        removeFromListButton.setFont(BUTTON_FONT.deriveFont(Font.BOLD, 16f)); 
        removeFromListButton.setForeground(Color.RED.darker());
        removeFromListButton.setOpaque(false);
        removeFromListButton.setContentAreaFilled(false);
        removeFromListButton.setBorderPainted(false);
        removeFromListButton.setToolTipText("Remove from this list");
        removeFromListButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        removeFromListButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to remove '" + title + "' from this list?",
                    "Confirm Removal", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                if (removeItemFromUserList(listId, contentItemId)) {
                    
                    displayContentForList(listId, userListsDisplayJList.getSelectedValue());
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to remove item from list.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        cardPanel.add(removeFromListButton, BorderLayout.EAST);
        return cardPanel;
    }


    private boolean removeItemFromUserList(int listId, int contentItemId) {
        String sql = "DELETE FROM user_list_items WHERE list_id = ? AND content_item_id = ?";
        System.out.println("Removing item " + contentItemId + " from list " + listId); // DEBUG
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (conn == null) throw new SQLException("Database connection failed");

            pstmt.setInt(1, listId);
            pstmt.setInt(2, contentItemId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("SQL error while removing item from list:");
            e.printStackTrace();
            return false;
        }
    }
    //////////////////////////////////////////
    /////////////////////////////////////////////
    private JPanel createContinueWatchingItems() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(0,0,5,0));

        String[] titles = {"Prens S01 B07", "Sherlock Holmes", "Prens S01 B06", "Prens S01 B05", "Eski Kraliçe", "Yüzüklerin Efendisi"};
        String[] genres = {"Komedi", "Aksiyon, Macera", "Komedi", "Komedi", "Dram", "Fantastik"};
        for (int i = 0; i < titles.length; i++) {
            panel.add(createContinueWatchingItem(titles[i], genres[i], (i+1)*18));
        }
        int preferredWidth = (200 + 15) * titles.length; 
        panel.setPreferredSize(new Dimension(preferredWidth, 320)); 
        return panel;
    }

    private JPanel createContinueWatchingItem(String title, String genre, int progressPercent) {
        JPanel itemPanel = new JPanel(new BorderLayout(0,8)); 
        itemPanel.setOpaque(false); 
        itemPanel.setPreferredSize(new Dimension(200, 310)); 
        itemPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JPanel imagePlaceholder = new JPanel();
        imagePlaceholder.setBackground(PANEL_ITEM_BG_COLOR);
        imagePlaceholder.setPreferredSize(new Dimension(200, 220)); 
        imagePlaceholder.setLayout(new BorderLayout());
        JLabel placeholderText = new JLabel(getIcon(title.replaceAll("\\s+", "").toLowerCase()+".jpg", title.substring(0,1))); // Resim veya baş harf
        placeholderText.setForeground(TEXT_COLOR);
        placeholderText.setHorizontalAlignment(SwingConstants.CENTER);
        placeholderText.setFont(HERO_TITLE_FONT.deriveFont(60f)); 
        imagePlaceholder.add(placeholderText, BorderLayout.CENTER);
        imagePlaceholder.setBorder(new LineBorder(PANEL_ITEM_BG_COLOR, 2)); 
        itemPanel.add(imagePlaceholder, BorderLayout.NORTH);


        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(DEFAULT_FONT.deriveFont(Font.BOLD, 15f));
        titleLabel.setForeground(TEXT_COLOR);
        textPanel.add(titleLabel);

        JLabel genreLabel = new JLabel(genre);
        genreLabel.setFont(DEFAULT_FONT.deriveFont(13f));
        genreLabel.setForeground(Color.LIGHT_GRAY);
        textPanel.add(genreLabel);
        
        itemPanel.add(textPanel, BorderLayout.CENTER);

        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setValue(progressPercent);
        progressBar.setPreferredSize(new Dimension(180, 6)); 
        progressBar.setForeground(ACCENT_COLOR);
        progressBar.setBackground(PANEL_ITEM_BG_COLOR.brighter()); 
        progressBar.setBorderPainted(false);
        
        progressBar.setUI(new BasicProgressBarUI() {
            @Override
            protected Color getSelectionBackground() { return TEXT_COLOR; } 
            @Override
            protected Color getSelectionForeground() { return BACKGROUND_COLOR; } 
        });
        itemPanel.add(progressBar, BorderLayout.SOUTH);
        
        itemPanel.addMouseListener(new MouseAdapter() {
            Border hoverBorder = new LineBorder(ACCENT_COLOR, 2);
            @Override
            public void mouseEntered(MouseEvent e) {
                imagePlaceholder.setBorder(hoverBorder);
                imagePlaceholder.setBackground(PANEL_ITEM_HOVER_BG_COLOR);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                imagePlaceholder.setBorder(new LineBorder(PANEL_ITEM_BG_COLOR, 2));
                imagePlaceholder.setBackground(PANEL_ITEM_BG_COLOR);
            }
        });

        return itemPanel;
    }

    public static ImageIcon getScaledImageIcon(ImageIcon srcIcon, int targetWidth, int targetHeight) {
        if (srcIcon == null || srcIcon.getImage() == null || srcIcon.getIconWidth() <= 0 || srcIcon.getIconHeight() <= 0) {
            System.err.println("getScaledImageIcon: Kaynak ikon geçersiz veya boş.");
            return null; 
        }
        Image srcImg = srcIcon.getImage();
        int srcWidth = srcIcon.getIconWidth();
        int srcHeight = srcIcon.getIconHeight();

        double scaleX = (double) targetWidth / srcWidth;
        double scaleY = (double) targetHeight / srcHeight;
        double scale = Math.min(scaleX, scaleY); 

        int newWidth = (int) (srcWidth * scale);
        int newHeight = (int) (srcHeight * scale);

        if (newWidth <= 0 || newHeight <= 0) {
            System.err.println("getScaledImageIcon: Hesaplanan yeni boyutlar geçersiz.");
            return srcIcon; 
        }

        Image scaledImg = srcImg.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImg);
    }
  
    
    static class fallbackTextIcon implements Icon {
        private String textToShow; 
        private int size;
        private Font font;

        public fallbackTextIcon(String inputText, int size) { 
            this.size = size;
            if (inputText == null || inputText.trim().isEmpty()) {
                this.textToShow = "?";
            } else {
                this.textToShow = String.valueOf(inputText.charAt(0)).toUpperCase();
            }
            this.font = new Font("Segoe UI", Font.BOLD, Math.max(8, size - 8));
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(StreamingAppUI.TEXT_COLOR != null ? StreamingAppUI.TEXT_COLOR : Color.WHITE);
            g2.setFont(this.font); 
            FontMetrics fm = g2.getFontMetrics();
            int textX = x + (getIconWidth() - fm.stringWidth(this.textToShow)) / 2;
            int textY = y + (getIconHeight() - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(this.textToShow, textX, textY);
            g2.dispose();
        }

        @Override public int getIconWidth() { return size; }
        @Override public int getIconHeight() { return size; }
    }
/*
    private Icon getIcon(String fileName, String fallbackText) {
        String filePath = "/icons/" + fileName;
        System.out.println("Attempting to load icon from classpath: " + filePath);

        try {
            BufferedImage img = ImageIO.read(getClass().getResourceAsStream(filePath));
            if (img != null) {
                return new ImageIcon(img);
            } else {
                System.err.println("İkon yüklenemedi (getResourceAsStream null döndü): " + filePath);
            }
        } catch (IOException e) {
             System.err.println("İkon yüklenemedi (IOException): " + filePath + " - " + e.getMessage());
        } catch (IllegalArgumentException iae) {
            System.err.println("İkon yüklenemedi (IllegalArgumentException - kaynak bulunamadı veya format hatası): " + filePath + " - " + iae.getMessage());
        }

        String actualFallback = (fallbackText == null || fallbackText.trim().isEmpty()) ? "?" : fallbackText;
        return new fallbackTextIcon(actualFallback, 24);
    }*/
    /*
    private Icon getIcon(String fileName, String fallbackText) {
        // fileName null veya boşsa başında logla ve fallback dön
        if (fileName == null || fileName.trim().isEmpty()) {
            System.out.println("getIcon çağrıldı, ancak fileName null veya boş. Fallback kullanılacak: " + fallbackText);
            String actualFallbackIfNull = (fallbackText == null || fallbackText.trim().isEmpty()) ? "?" : fallbackText;
            return new fallbackTextIcon(actualFallbackIfNull, 24); // Boyutu ihtiyaca göre ayarlayın
        }

        String filePath = "/icons/" + fileName; // Doğru birleştirme
        System.out.println("getIcon: İkon yüklenmeye çalışılıyor: " + filePath);

        URL resourceUrl = getClass().getResource(filePath);
        if (resourceUrl == null) {
            System.err.println("getIcon: Kaynak BULUNAMADI (getResource null döndü): " + filePath);
            String actualFallbackNotFound = (fallbackText == null || fallbackText.trim().isEmpty()) ? "?" : fallbackText;
            return new fallbackTextIcon(actualFallbackNotFound, 24);
        }
        System.out.println("getIcon: Kaynak bulundu: " + resourceUrl.toString());

        try {
            BufferedImage img = ImageIO.read(resourceUrl); // resourceUrl'den oku, getResourceAsStream değil
            if (img != null) {
                System.out.println("getIcon: Resim başarıyla okundu: " + filePath);
                return new ImageIcon(img);
            } else {
                System.err.println("getIcon: Resim okunamadı (ImageIO.read null döndü): " + filePath);
            }
        } catch (IOException e) {
             System.err.println("getIcon: Resim okunurken IOException: " + filePath + " - " + e.getMessage());
             e.printStackTrace();
        } catch (IllegalArgumentException iae) {
            // Bu genellikle resourceUrl null ise veya ImageIO.read ile ilgili bir sorun varsa çıkar,
            // ama resourceUrl null kontrolünü yukarıda yaptık.
            System.err.println("getIcon: Resim okunurken IllegalArgumentException: " + filePath + " - " + iae.getMessage());
            iae.printStackTrace();
        }

        System.err.println("getIcon: '" + filePath + "' yüklenemediği için fallback kullanılacak: " + fallbackText);
        String actualFallbackOnError = (fallbackText == null || fallbackText.trim().isEmpty()) ? "?" : fallbackText;
        return new fallbackTextIcon(actualFallbackOnError, 24);
    }*/
    
    private Icon getIcon(String pathOrUrl, String fallbackText) {
        if (pathOrUrl == null || pathOrUrl.trim().isEmpty()) {
            System.out.println("getIcon: pathOrUrl null veya boş. Fallback kullanılacak: " + fallbackText);
            String actualFallbackIfNull = (fallbackText == null || fallbackText.trim().isEmpty()) ? "?" : fallbackText;
            return new fallbackTextIcon(actualFallbackIfNull, 24);
        }

        if (pathOrUrl.toLowerCase().startsWith("http://") || pathOrUrl.toLowerCase().startsWith("https://")) {
            
            System.out.println("getIcon: URL'den yüklenmeye çalışılıyor: " + pathOrUrl);
            try {
                URL imageUrl = new URL(pathOrUrl);
                BufferedImage img = ImageIO.read(imageUrl);
                if (img != null) {
                    System.out.println("getIcon: URL'den resim başarıyla okundu: " + pathOrUrl);
                    return new ImageIcon(img);
                } else {
                    System.err.println("getIcon: URL'den resim okunamadı (ImageIO.read null döndü): " + pathOrUrl);
                }
            } catch (IOException e) {
                System.err.println("getIcon: URL'den resim okunurken IOException: " + pathOrUrl + " - " + e.getMessage());
                
            }
        } else {
            String filePath = "/icons/" + pathOrUrl;
            System.out.println("getIcon: Classpath'ten yüklenmeye çalışılıyor: " + filePath);
            URL resourceUrl = getClass().getResource(filePath);
            if (resourceUrl == null) {
                System.err.println("getIcon: Classpath'ten kaynak BULUNAMADI: " + filePath);
            } else {
                System.out.println("getIcon: Classpath'ten kaynak bulundu: " + resourceUrl.toString());
                try {
                    BufferedImage img = ImageIO.read(resourceUrl);
                    if (img != null) {
                        System.out.println("getIcon: Classpath'ten resim başarıyla okundu: " + filePath);
                        return new ImageIcon(img);
                    } else {
                        System.err.println("getIcon: Classpath'ten resim okunamadı (ImageIO.read null döndü): " + filePath);
                    }
                } catch (IOException e) {
                    System.err.println("getIcon: Classpath'ten resim okunurken IOException: " + filePath + " - " + e.getMessage());
                    // e.printStackTrace();
                } catch (IllegalArgumentException iae) {
                    System.err.println("getIcon: Classpath'ten resim okunurken IllegalArgumentException: " + filePath + " - " + iae.getMessage());
                    // iae.printStackTrace();
                }
            }
        }

.
        System.err.println("getIcon: '" + pathOrUrl + "' yüklenemediği için fallback kullanılacak: " + fallbackText);
        String actualFallbackOnError = (fallbackText == null || fallbackText.trim().isEmpty()) ? "?" : fallbackText;
        return new fallbackTextIcon(actualFallbackOnError, 24);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Look and Feel ayarlanamadı: " + e.getMessage());
        }
        System.setProperty("awt.useSystemAAFontSettings","on");
        System.setProperty("swing.aatext", "true");


        SwingUtilities.invokeLater(() -> {
            StreamingAppUI app = new StreamingAppUI();
            app.setVisible(true);
        });
    }
}