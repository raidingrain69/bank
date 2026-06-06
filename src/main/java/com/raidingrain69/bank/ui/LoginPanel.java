package com.raidingrain69.bank.ui;

import com.raidingrain69.bank.auth.DeviceType;
import com.raidingrain69.bank.controller.BankController;
import com.raidingrain69.bank.domain.User;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.Font;
import java.util.Optional;
import java.util.function.Consumer;

public final class LoginPanel extends JPanel {
    private final JTextField usernameField = new JTextField();
    private final JPasswordField credentialField = new JPasswordField();
    private final JComboBox<DeviceType> deviceCombo = new JComboBox<>(DeviceType.values());

    public LoginPanel(BankController controller, Consumer<User> onLogin) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Theme.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(48, 220, 48, 220));

        JLabel heading = new JLabel("Secure Banking Login");
        heading.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
        heading.setForeground(Theme.PRIMARY);

        JButton loginButton = new JButton("Sign In");
        loginButton.setBackground(Theme.PRIMARY);
        loginButton.setForeground(Theme.PANEL);

        JButton registerButton = new JButton("Quick Register");

        loginButton.addActionListener(event -> {
            String username = usernameField.getText().trim();
            String credential = new String(credentialField.getPassword());
            DeviceType deviceType = (DeviceType) deviceCombo.getSelectedItem();

            Optional<User> user = controller.login(username, deviceType, credential);
            if (user.isPresent()) {
                onLogin.accept(user.get());
            } else {
                JOptionPane.showMessageDialog(this, "Authentication failed", "Login", JOptionPane.ERROR_MESSAGE);
            }
        });

        registerButton.addActionListener(event -> {
            String fullName = JOptionPane.showInputDialog(this, "Full name");
            String username = JOptionPane.showInputDialog(this, "Username");
            String password = JOptionPane.showInputDialog(this, "Password");
            try {
                if (fullName != null && username != null && password != null) {
                    controller.registerCustomer(fullName, username, password);
                    JOptionPane.showMessageDialog(this, "Customer registered with starter savings account");
                }
            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Registration", JOptionPane.ERROR_MESSAGE);
            }
        });

        add(heading);
        add(Box.createVerticalStrut(24));
        add(new JLabel("Username"));
        add(usernameField);
        add(Box.createVerticalStrut(12));
        add(new JLabel("Device Type"));
        add(deviceCombo);
        add(Box.createVerticalStrut(12));
        add(new JLabel("Credential"));
        add(credentialField);
        add(Box.createVerticalStrut(20));
        add(loginButton);
        add(Box.createVerticalStrut(8));
        add(registerButton);
    }

    public void reset() {
        usernameField.setText("");
        credentialField.setText("");
    }
}
