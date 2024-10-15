package trainsofsheffield.views.login;

import javax.swing.*;

import trainsofsheffield.controllers.LoginController;
import trainsofsheffield.views.LoginFrame;

import java.awt.*;

public class LoginPanel extends JPanel{
    private JTextField emailField = new JTextField(20);
    private JPasswordField passwordField = new JPasswordField(20);

    public JTextField getLoginEmailField() {
        return emailField;
    }

    public JPasswordField getLoginPasswordField() {
        return passwordField;
    }
    public LoginPanel(LoginFrame frame, String message) {
        super(new BorderLayout());
        //setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.decode("#D9D9D9"));

        JLabel emailLabel = new JLabel("Email");
        JLabel passwordLabel = new JLabel("Password");

        JPanel buttonPanel = new JPanel(new BorderLayout());
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new LoginController(frame));
        buttonPanel.add(loginButton);

        JPanel fieldPanel = new JPanel(new GridLayout(2,2));
        fieldPanel.setBackground(Color.decode("#D9D9D9"));
        emailField.setBackground(Color.decode("#AC9F9F"));
        passwordField.setBackground(Color.decode("#AC9F9F"));
        emailField.setBackground(Color.decode("#AC9F9F"));
        passwordField.setBackground(Color.decode("#AC9F9F"));
        emailLabel.setForeground(Color.decode("#7F3333"));
        passwordLabel.setForeground(Color.decode("#7F3333")); 
        fieldPanel.add(emailLabel);
        fieldPanel.add(emailField);
        fieldPanel.add(passwordLabel);
        fieldPanel.add(passwordField);
        fieldPanel.setBorder(BorderFactory.createEmptyBorder(120,50,120,50));

        JPanel loginPanel = new JPanel(new BorderLayout());
        loginPanel.add(fieldPanel, BorderLayout.CENTER);
        loginPanel.add(buttonPanel, BorderLayout.SOUTH);
        if (message == "Login Unsuccessful"){
                JOptionPane.showMessageDialog(frame, message, "Error", JOptionPane.ERROR_MESSAGE);
        }
        

        add(Box.createVerticalStrut(10));
        add(loginPanel, BorderLayout.CENTER);
    }

}
