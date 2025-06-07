package main;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

//StreamingAppUI'daki static sabitleri ve metodları kullanmak için:
import static main.StreamingAppUI.*;

public class CategoryContentDialog extends JDialog {

 public CategoryContentDialog(Frame owner, String categoryName, List<String> items, String contentType) {
     super(owner, categoryName + " " + contentType, true); // Modal dialog
     setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
     setMinimumSize(new Dimension(700, 500));
     getContentPane().setBackground(BACKGROUND_COLOR);
     setLayout(new BorderLayout(10, 10));
     ((JPanel) getContentPane()).setBorder(new EmptyBorder(15, 15, 15, 15));

     JLabel titleLabel = new JLabel(categoryName + " " + contentType);
     titleLabel.setFont(HERO_TITLE_FONT.deriveFont(32f));
     titleLabel.setForeground(TEXT_COLOR);
     titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
     titleLabel.setBorder(new EmptyBorder(0, 0, 15, 0));
     add(titleLabel, BorderLayout.NORTH);

     JPanel itemsListPanel = new JPanel();
     itemsListPanel.setBackground(BACKGROUND_COLOR);
     itemsListPanel.setLayout(new BoxLayout(itemsListPanel, BoxLayout.Y_AXIS));

     if (items == null || items.isEmpty() || (items.size() == 1 && items.get(0).startsWith("No content"))) {
         JLabel noItemsLabel = new JLabel(items.isEmpty() ? "No " + contentType.toLowerCase() + " found." : items.get(0));
         noItemsLabel.setFont(DEFAULT_FONT.deriveFont(18f));
         noItemsLabel.setForeground(TEXT_COLOR);
         noItemsLabel.setHorizontalAlignment(SwingConstants.CENTER);
         itemsListPanel.add(Box.createVerticalGlue());
         itemsListPanel.add(noItemsLabel);
         itemsListPanel.add(Box.createVerticalGlue());
     } else {
         for (String itemTitle : items) {
             itemsListPanel.add(createContentListItemPanel(itemTitle, contentType));
             itemsListPanel.add(Box.createRigidArea(new Dimension(0, 8)));
         }
     }

     JScrollPane scrollPane = new JScrollPane(itemsListPanel);
     scrollPane.setBorder(null);
     scrollPane.getViewport().setBackground(BACKGROUND_COLOR);
     scrollPane.getVerticalScrollBar().setUnitIncrement(16);
     StreamingAppUI.styleScrollBar(scrollPane.getVerticalScrollBar());
     add(scrollPane, BorderLayout.CENTER);

     pack();
     setSize(Math.max(getMinimumSize().width, getPreferredSize().width),
             Math.max(getMinimumSize().height, getPreferredSize().height + 50));
     setLocationRelativeTo(owner);
 }

 private JPanel createContentListItemPanel(String itemTitle, String contentType) {
     JPanel itemPanel = new JPanel(new BorderLayout(10, 0));
     itemPanel.setBackground(PANEL_ITEM_BG_COLOR);
     itemPanel.setBorder(new EmptyBorder(12, 15, 12, 15));
     itemPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

     JLabel titleLabel = new JLabel(itemTitle);
     titleLabel.setFont(TITLE_FONT.deriveFont(15f));
     titleLabel.setForeground(TEXT_COLOR);
     itemPanel.add(titleLabel, BorderLayout.CENTER);

     JButton actionButton = new JButton("▶ View"); 
     actionButton.setFont(TITLE_FONT.deriveFont(15f));
     actionButton.setForeground(ACCENT_COLOR);
     actionButton.setOpaque(false);
     actionButton.setContentAreaFilled(false);
     actionButton.setBorderPainted(false);
     actionButton.setFocusPainted(false);
     actionButton.addActionListener(e -> JOptionPane.showMessageDialog(this,
             contentType + ": " + itemTitle, contentType + " Detail", JOptionPane.INFORMATION_MESSAGE));
     itemPanel.add(actionButton, BorderLayout.EAST);
     
     itemPanel.addMouseListener(new MouseAdapter() {
         private final Color originalBackground = itemPanel.getBackground();
         @Override
         public void mouseEntered(MouseEvent e) {
             itemPanel.setBackground(PANEL_ITEM_HOVER_BG_COLOR);
         }
         @Override
         public void mouseExited(MouseEvent e) {
             itemPanel.setBackground(originalBackground);
         }
     });
     return itemPanel;
 }
}
