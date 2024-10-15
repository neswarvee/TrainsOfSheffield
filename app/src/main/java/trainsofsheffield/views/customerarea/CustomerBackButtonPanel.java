package trainsofsheffield.views.customerarea;

import trainsofsheffield.controllers.CustomerAreaController;
import trainsofsheffield.views.MainFrame;

import javax.swing.*;
import java.awt.*;

public class CustomerBackButtonPanel extends JPanel {
    private CustomerAreaController controller;

    public CustomerBackButtonPanel(MainFrame frame) {
        this.controller = new CustomerAreaController(frame);

        setBorder(BorderFactory.createEmptyBorder(10,10,0,10));

        setLayout(new GridLayout(1, 1));
        JButton backButton = new JButton("Back");
        backButton.addActionListener(controller);
        add(backButton);
    }
}
