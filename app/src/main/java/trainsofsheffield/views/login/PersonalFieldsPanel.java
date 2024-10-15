package trainsofsheffield.views.login;

import javax.swing.*;

import trainsofsheffield.views.LoginFrame;

import java.awt.*;

public class PersonalFieldsPanel extends JPanel{
    private JTextField firstnameField = new JTextField();
    private JTextField surnameField = new JTextField();
    private JTextField emailField = new JTextField();
    private JPasswordField passwordField = new JPasswordField(); 

    public JTextField getFirstnameField() {
        return firstnameField;
    }
    public JTextField getSurnameField() {
        return surnameField;
    }
    public JTextField getEmailField() {
        return emailField;
    }
    public JPasswordField getRegisterPasswordField() {
        return passwordField;
    }

    public PersonalFieldsPanel(LoginFrame frame) {

        //Coloring all elements and setting layout
        setLayout(new GridLayout(4, 2));
        setBackground(Color.decode("#D9D9D9"));
        firstnameField.setBackground(Color.decode("#AC9F9F")); 
        surnameField.setBackground(Color.decode("#AC9F9F")); 
        emailField.setBackground(Color.decode("#AC9F9F")); 
        passwordField.setBackground(Color.decode("#AC9F9F"));   

        //Adding fields and labels
        add(new JLabel("Name:"){{setForeground(Color.decode("#7F3333"));}});
        add(firstnameField);
        add(new JLabel("Surname:"){{setForeground(Color.decode("#7F3333"));}});
        add(surnameField);
        add(new JLabel("Email:"){{setForeground(Color.decode("#7F3333"));}});
        add(emailField);
        add(new JLabel("Password:"){{setForeground(Color.decode("#7F3333"));}});
        add(passwordField);
    }
}