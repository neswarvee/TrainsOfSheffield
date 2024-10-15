package trainsofsheffield.views.customerarea;

import trainsofsheffield.controllers.CustomerAreaController;
import trainsofsheffield.views.MainFrame;

import javax.swing.*;
import java.awt.*;

public class CustomerLogoutButtonPanel extends JPanel {
    private CustomerAreaController controller;

    public CustomerLogoutButtonPanel(MainFrame frame) {
        this.controller = new CustomerAreaController(frame);

        setBorder(BorderFactory.createEmptyBorder(10,10,0,10));

        setLayout(new GridLayout(1, 1));
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(controller);
        add(logoutButton);
    }
}
