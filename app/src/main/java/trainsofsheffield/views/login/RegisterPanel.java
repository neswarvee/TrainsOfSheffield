package trainsofsheffield.views.login;

import javax.swing.*;

import trainsofsheffield.controllers.LoginController;
import trainsofsheffield.views.LoginFrame;

import java.awt.*;

public class RegisterPanel extends JPanel{
    private PersonalFieldsPanel personalFieldsPanel;
    private AddressFieldsPanel addressFieldsPanel;
   

    public JTextField getFirstnameField() {
        return personalFieldsPanel.getFirstnameField();
    }
    public JTextField getSurnameField() {
        return personalFieldsPanel.getSurnameField();
    }
    public JTextField getEmailField() {
        return personalFieldsPanel.getEmailField();
    }
    public JPasswordField getRegisterPasswordField() {
        return personalFieldsPanel.getRegisterPasswordField();
    }
    public JTextField getStreetLineField() {
        return addressFieldsPanel.getStreetLineField();
    }
    public JTextField getCityField() {
        return addressFieldsPanel.getCityField();
    }
    public JTextField getPostcodeField() {
        return addressFieldsPanel.getPostcodeField();
    }
    
    public RegisterPanel(LoginFrame frame,String message) {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.decode("#D9D9D9"));

        personalFieldsPanel = new PersonalFieldsPanel(frame);
        addressFieldsPanel = new AddressFieldsPanel(frame);

        //Labelled personal details
        JPanel personalDetails = new JPanel();
        personalDetails.setLayout(new GridLayout(1,2));
        personalDetails.setBackground(Color.decode("#D9D9D9"));
        personalDetails.add(new JLabel("Personal Details"){{setForeground(Color.decode("#7F3333"));}},BorderLayout.CENTER);
        personalDetails.add(personalFieldsPanel);

        //Labelled address detail
        JPanel address = new JPanel();
        address.setForeground(Color.decode("#7F3333"));
        address.setLayout(new GridLayout(1,2));
        address.setBackground(Color.decode("#D9D9D9"));
        address.add(new JLabel("Address"){{setForeground(Color.decode("#7F3333"));}},BorderLayout.CENTER);
        address.add(addressFieldsPanel);

        //Buttons and listeners
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); 
        JButton registerButton = new JButton("Register");
        buttonPanel.setBackground(Color.decode("#D9D9D9"));
        registerButton.addActionListener(new LoginController(frame));
        registerButton.setBackground(Color.decode("#A37C7C"));
        registerButton.setForeground(Color.decode("#D9D9D9")); 
        buttonPanel.add(registerButton);

        //Adding all elements to the window
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25)); // Setting the border
        if (message != "Login Unsuccessful" & message != null) {
            if (message == "Registration Successful"){
                JOptionPane.showMessageDialog(frame, message, "Message", JOptionPane.PLAIN_MESSAGE);         
            }
            else{
                JOptionPane.showMessageDialog(frame, message, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        mainPanel.add(personalDetails);
        mainPanel.add(Box.createRigidArea(new Dimension(500,30))); // Margin
        mainPanel.add(address);
        mainPanel.add(Box.createRigidArea(new Dimension(500, 30))); // Margin
        mainPanel.add(buttonPanel);
        add(mainPanel);
    }

}
