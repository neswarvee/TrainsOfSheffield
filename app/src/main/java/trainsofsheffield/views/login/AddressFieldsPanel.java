package trainsofsheffield.views.login;

import javax.swing.*;

import trainsofsheffield.views.LoginFrame;

import java.awt.*;

public class AddressFieldsPanel extends JPanel{
    private JTextField streetLineField = new JTextField();
    private JTextField cityField = new JTextField();
    private JTextField postcodeField = new JTextField();

    public JTextField getStreetLineField() {
        return streetLineField;
    }
    public JTextField getCityField() {
        return cityField;
    }
    public JTextField getPostcodeField() {
        return postcodeField;
    }

    public AddressFieldsPanel(LoginFrame frame) {

        //Colouring elements and setting layout
        setLayout(new GridLayout(4, 2));
        setBackground(Color.decode("#D9D9D9"));
        streetLineField.setBackground(Color.decode("#AC9F9F")); 
        cityField.setBackground(Color.decode("#AC9F9F")); 
        postcodeField.setBackground(Color.decode("#AC9F9F")); 
        streetLineField.setBackground(Color.decode("#AC9F9F")); 

        //Adding fields and labels
        add(new JLabel("Address Line:"){{setForeground(Color.decode("#7F3333"));}});
        add(streetLineField);
        add(new JLabel("City:"){{setForeground(Color.decode("#7F3333"));}});
        add(cityField);
        add(new JLabel("Postcode:"){{setForeground(Color.decode("#7F3333"));}});
        add(postcodeField);
    }
}