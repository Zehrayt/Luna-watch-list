
package main; 

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;      // YENİ
import java.sql.PreparedStatement; // YENİ
import java.sql.ResultSet;       // YENİ
import java.sql.SQLException;    // YENİ
import org.mindrot.jbcrypt.BCrypt; // YENİ (jbcrypt kütüphanesinden)

public class AuthForm extends JFrame {

    private CardLayout cardLayout;
    private JPanel cardPanel;


    private JTextField signInUsernameField;
    private JPasswordField signInPasswordField;

    private JTextField signUpUsernameField;
    private JTextField signUpFirstNameField;
    private JTextField signUpLastNameField;
    private JTextField signUpEmailField;
    private JTextField signUpPhoneField;
    private JPasswordField signUpPasswordField;



    private static final Color BACKGROUND_COLOR = new Color(50, 50, 50);
  
    private static final Color PANEL_BACKGROUND_COLOR = new Color(65, 65, 65);
    private static final Color TEXT_COLOR_PRIMARY = new Color(220, 220, 220);
    private static final Color TEXT_COLOR_SECONDARY = new Color(180, 180, 180);
    private static final Color TEXT_COLOR_LINK = new Color(200, 200, 255); // A slightly bluish white for links
    private static final Color BORDER_COLOR = new Color(100, 100, 100);
    private static final Color BUTTON_BACKGROUND_COLOR = new Color(80, 80, 80);
    private static final Color BUTTON_TEXT_COLOR = new Color(220, 220, 220);


    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 32);
    private static final Font LABEL_FONT = new Font("Arial", Font.PLAIN, 14);
    private static final Font INPUT_FONT = new Font("Arial", Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 16);
    private static final Font LINK_FONT = new Font("Arial", Font.PLAIN, 12);
    private static final Font ICON_FONT = new Font("Segoe UI Symbol", Font.PLAIN, 16); // Font that supports symbols

    // Placeholders
    private static final String USERNAME_PLACEHOLDER = "Username";
    private static final String FIRST_NAME_PLACEHOLDER = "First Name";
    private static final String LAST_NAME_PLACEHOLDER = "Last Name";
    private static final String EMAIL_PLACEHOLDER = "Email";
    private static final String PHONE_PLACEHOLDER = "Phone Number"; // Bu sütun DB'de yok, ekleyebilir veya kaldırabiliriz
    private static final String PASSWORD_PLACEHOLDER = "Password";

    public AuthForm() {
        setTitle("Authentication");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(400, 550));
        setPreferredSize(new Dimension(450, 500));
        getContentPane().setBackground(BACKGROUND_COLOR);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(BACKGROUND_COLOR);

        JPanel signInPanel = createSignInPanel();
        JPanel signUpPanel = createSignUpPanel();

        cardPanel.add(signInPanel, "SIGN_IN");
        cardPanel.add(signUpPanel, "SIGN_UP");

        add(cardPanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        cardLayout.show(cardPanel, "SIGN_IN");
    }

    private JPanel createSignInPanel() {

        JPanel panel = new JPanel();
        panel.setBackground(PANEL_BACKGROUND_COLOR);
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BACKGROUND_COLOR, 20),
                BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);
        JLabel titleLabel = new JLabel("Sign in");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TEXT_COLOR_PRIMARY);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.insets = new Insets(0, 0, 40, 0);
        panel.add(titleLabel, gbc);
        gbc.insets = new Insets(10, 0, 10, 0);


        JPanel usernameGroup = createInputGroup("\uD83D\uDC64", USERNAME_PLACEHOLDER, false);
        signInUsernameField = (JTextField) ((BorderLayout)usernameGroup.getLayout()).getLayoutComponent(BorderLayout.CENTER);
        panel.add(usernameGroup, gbc);


        JPanel passwordGroup = createInputGroup("\uD83D\uDD12", PASSWORD_PLACEHOLDER, true);
        signInPasswordField = (JPasswordField) ((BorderLayout)passwordGroup.getLayout()).getLayoutComponent(BorderLayout.CENTER);
        panel.add(passwordGroup, gbc);


        JLabel forgotPasswordLabel = new JLabel("Forgot password?");
        forgotPasswordLabel.setFont(LINK_FONT);
        forgotPasswordLabel.setForeground(TEXT_COLOR_SECONDARY);
        forgotPasswordLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        forgotPasswordLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.insets = new Insets(5, 0, 20, 0);
        panel.add(forgotPasswordLabel, gbc);
        gbc.insets = new Insets(10, 0, 10, 0);



        JButton loginButton = createStyledButton("LOGIN");
        loginButton.addActionListener(e -> handleSignIn()); 
        gbc.insets = new Insets(20, 0, 20, 0);
        gbc.ipady = 10;
        panel.add(loginButton, gbc);

        gbc.ipady = 0;
        gbc.insets = new Insets(10, 0, 10, 0);
        JPanel signUpLinkPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        signUpLinkPanel.setOpaque(false);
        JLabel dontHaveAccountLabel = new JLabel("Don't have an account? ");
        dontHaveAccountLabel.setFont(LINK_FONT);
        dontHaveAccountLabel.setForeground(TEXT_COLOR_SECONDARY);
        JLabel signUpLink = new JLabel("Sign up");
        signUpLink.setFont(LINK_FONT.deriveFont(Font.BOLD));
        signUpLink.setForeground(TEXT_COLOR_LINK);
        signUpLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signUpLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(cardPanel, "SIGN_UP");
            }
        });
        signUpLinkPanel.add(dontHaveAccountLabel);
        signUpLinkPanel.add(signUpLink);
        gbc.insets = new Insets(10, 0, 0, 0);
        panel.add(signUpLinkPanel, gbc);
        gbc.weighty = 1.0;
        panel.add(Box.createGlue(), gbc);

        return panel;
    }

    private JPanel createSignUpPanel() {

        JPanel panel = new JPanel();
        panel.setBackground(PANEL_BACKGROUND_COLOR);
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BACKGROUND_COLOR, 20),
                BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);
        JLabel titleLabel = new JLabel("Sign up");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TEXT_COLOR_PRIMARY);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.insets = new Insets(0, 0, 40, 0);
        panel.add(titleLabel, gbc);
        gbc.insets = new Insets(10, 0, 10, 0);



        JPanel namePanel = new JPanel(new GridLayout(1, 2, 15, 0));
        namePanel.setOpaque(false);
        JPanel firstNameGroup = createInputGroup("\uD83D\uDC64", FIRST_NAME_PLACEHOLDER, false);
        signUpFirstNameField = (JTextField) ((BorderLayout)firstNameGroup.getLayout()).getLayoutComponent(BorderLayout.CENTER);
        namePanel.add(firstNameGroup);

        JPanel lastNameGroup = createInputGroup("\uD83D\uDC64", LAST_NAME_PLACEHOLDER, false);
        signUpLastNameField = (JTextField) ((BorderLayout)lastNameGroup.getLayout()).getLayoutComponent(BorderLayout.CENTER);
        namePanel.add(lastNameGroup);
        panel.add(namePanel, gbc);


        JPanel usernameGroup = createInputGroup("\uD83D\uDC64", USERNAME_PLACEHOLDER, false); // Farklı bir ikon olabilir (opsiyonel)
        signUpUsernameField = (JTextField) ((BorderLayout)usernameGroup.getLayout()).getLayoutComponent(BorderLayout.CENTER);
        panel.add(usernameGroup, gbc);


        JPanel emailGroup = createInputGroup("\u2709\uFE0F", EMAIL_PLACEHOLDER, false);
        signUpEmailField = (JTextField) ((BorderLayout)emailGroup.getLayout()).getLayoutComponent(BorderLayout.CENTER);
        panel.add(emailGroup, gbc);


        JPanel phoneGroup = createInputGroup("\uD83D\uDCDE", PHONE_PLACEHOLDER, false);
        signUpPhoneField = (JTextField) ((BorderLayout)phoneGroup.getLayout()).getLayoutComponent(BorderLayout.CENTER);


        JPanel passwordGroup = createInputGroup("\uD83D\uDD12", PASSWORD_PLACEHOLDER, true);
        signUpPasswordField = (JPasswordField) ((BorderLayout)passwordGroup.getLayout()).getLayoutComponent(BorderLayout.CENTER);
        panel.add(passwordGroup, gbc);


        JButton signUpButton = createStyledButton("SIGN UP");
        signUpButton.addActionListener(e -> handleSignUp()); // YENİ: Action Listener
        gbc.insets = new Insets(20, 0, 20, 0);
        gbc.ipady = 10;
        panel.add(signUpButton, gbc);

        gbc.ipady = 0;
        gbc.insets = new Insets(10, 0, 10, 0);
        JPanel signInLinkPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        signInLinkPanel.setOpaque(false);
        JLabel alreadyHaveAccountLabel = new JLabel("Already have an account? ");
        alreadyHaveAccountLabel.setFont(LINK_FONT);
        alreadyHaveAccountLabel.setForeground(TEXT_COLOR_SECONDARY);
        JLabel signInLink = new JLabel("Sign in");
        signInLink.setFont(LINK_FONT.deriveFont(Font.BOLD));
        signInLink.setForeground(TEXT_COLOR_LINK);
        signInLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signInLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(cardPanel, "SIGN_IN");
            }
        });
        signInLinkPanel.add(alreadyHaveAccountLabel);
        signInLinkPanel.add(signInLink);
        gbc.insets = new Insets(10, 0, 0, 0);
        panel.add(signInLinkPanel, gbc);
        gbc.weighty = 1.0;
        panel.add(Box.createGlue(), gbc);

        return panel;
    }


    private JPanel createInputGroup(String iconText, String placeholder, boolean isPassword) {
        JPanel groupPanel = new JPanel(new BorderLayout(10, 0));
        groupPanel.setOpaque(false);
        JLabel iconLabel = new JLabel(iconText);
        iconLabel.setFont(ICON_FONT);
        iconLabel.setForeground(TEXT_COLOR_SECONDARY);
        iconLabel.setBorder(new EmptyBorder(0,0,3,0));
        groupPanel.add(iconLabel, BorderLayout.WEST);
        JTextField textField;
        if (isPassword) {
            textField = new JPasswordField();
        } else {
            textField = new JTextField();
        }
        textField.setText(placeholder);
        textField.setFont(INPUT_FONT);
        textField.setForeground(TEXT_COLOR_SECONDARY);
        textField.setBackground(PANEL_BACKGROUND_COLOR);
        textField.setCaretColor(TEXT_COLOR_PRIMARY);
        textField.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(0, 0, 1, 0, BORDER_COLOR),
            new EmptyBorder(5, 0, 5, 0)
        ));
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(TEXT_COLOR_PRIMARY);
                     if (isPassword) {
                        ((JPasswordField)textField).setEchoChar('\u2022');
                    }
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    if (isPassword) {
                        ((JPasswordField)textField).setEchoChar((char)0);
                    }
                    textField.setText(placeholder);
                    textField.setForeground(TEXT_COLOR_SECONDARY);
                }
            }
        });
        if (isPassword && textField.getText().equals(placeholder)) {
            ((JPasswordField)textField).setEchoChar((char)0);
        }
        groupPanel.add(textField, BorderLayout.CENTER);
        return groupPanel;
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


    private void handleSignUp() {
        String username = signUpUsernameField.getText().trim();
        String firstName = signUpFirstNameField.getText().trim();
        String lastName = signUpLastNameField.getText().trim(); 
        String email = signUpEmailField.getText().trim();
        String password = new String(signUpPasswordField.getPassword());


        if (username.isEmpty() || username.equals(USERNAME_PLACEHOLDER) ||
            firstName.isEmpty() || firstName.equals(FIRST_NAME_PLACEHOLDER) ||
            email.isEmpty() || email.equals(EMAIL_PLACEHOLDER) ||
            password.isEmpty() || password.equals(PASSWORD_PLACEHOLDER)) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields.", "Sign Up Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        


        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());


        String sql = "INSERT INTO users (username, password_hash, email, profile_name) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            if (conn == null) {
                 JOptionPane.showMessageDialog(this, "Database connection error.", "Sign Up Error", JOptionPane.ERROR_MESSAGE);
                 return;
            }
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, username);
                pstmt.setString(2, hashedPassword);
                pstmt.setString(3, email);
                pstmt.setString(4, firstName + " " + lastName); 

                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(this, "Sign up successful! Please sign in.", "Sign Up Success", JOptionPane.INFORMATION_MESSAGE);
                    cardLayout.show(cardPanel, "SIGN_IN"); 
                    
                    signUpUsernameField.setText(USERNAME_PLACEHOLDER); signUpUsernameField.setForeground(TEXT_COLOR_SECONDARY);
                    signUpFirstNameField.setText(FIRST_NAME_PLACEHOLDER); signUpFirstNameField.setForeground(TEXT_COLOR_SECONDARY);
                    signUpLastNameField.setText(LAST_NAME_PLACEHOLDER); signUpLastNameField.setForeground(TEXT_COLOR_SECONDARY);
                    signUpEmailField.setText(EMAIL_PLACEHOLDER); signUpEmailField.setForeground(TEXT_COLOR_SECONDARY);
                    signUpPasswordField.setEchoChar((char)0); signUpPasswordField.setText(PASSWORD_PLACEHOLDER); signUpPasswordField.setForeground(TEXT_COLOR_SECONDARY);

                } else {
                    JOptionPane.showMessageDialog(this, "Sign up failed. Please try again.", "Sign Up Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            if (ex.getSQLState().equals("23000")) { 
                 JOptionPane.showMessageDialog(this, "Username or Email already exists.", "Sign Up Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Database error during sign up: " + ex.getMessage(), "Sign Up Error", JOptionPane.ERROR_MESSAGE);
            }
            ex.printStackTrace();
        } finally {
        }
    }

    
    private void handleSignIn() {
        String username = signInUsernameField.getText().trim();
        String password = new String(signInPasswordField.getPassword());

        if (username.isEmpty() || username.equals(USERNAME_PLACEHOLDER) ||
            password.isEmpty() || password.equals(PASSWORD_PLACEHOLDER)) {
            JOptionPane.showMessageDialog(this, "Please enter username and password.", "Sign In Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "SELECT password_hash, user_id, profile_name FROM users WHERE username = ?";
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (conn == null) {
                 JOptionPane.showMessageDialog(this, "Database connection error.", "Sign In Error", JOptionPane.ERROR_MESSAGE);
                 return;
            }

            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String hashedPasswordFromDB = rs.getString("password_hash");
                    int userId = rs.getInt("user_id");
                    String profileName = rs.getString("profile_name");

                    if (hashedPasswordFromDB != null && BCrypt.checkpw(password, hashedPasswordFromDB)) {
                        JOptionPane.showMessageDialog(this, "Sign in successful! Welcome " + profileName, "Sign In Success", JOptionPane.INFORMATION_MESSAGE);
                        this.dispose();
                        SwingUtilities.invokeLater(() -> {
                            StreamingAppUI streamingApp = new StreamingAppUI(userId, profileName);
                            streamingApp.setVisible(true);
                        });
                    } else {
                        JOptionPane.showMessageDialog(this, "Invalid username or password.", "Sign In Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid username or password.", "Sign In Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error during sign in: " + ex.getMessage(), "Sign In Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.setProperty("awt.useSystemAAFontSettings","on");
        System.setProperty("swing.aatext", "true");

        SwingUtilities.invokeLater(AuthForm::new); 
    }
    
}