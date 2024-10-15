package trainsofsheffield.views.customerarea.homepage;

import trainsofsheffield.controllers.CustomerAreaController;
import trainsofsheffield.models.User.*;
import trainsofsheffield.views.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class CustomerScreenNavigationPanel extends JPanel {
    private CustomerAreaController controller;

    public CustomerScreenNavigationPanel(MainFrame frame) {
        this.controller = new CustomerAreaController(frame);
        
        ArrayList<String> navigationButtonLabels =  new ArrayList<>(Arrays.asList("Order Page", "Details"));
        Role roleOfUser = frame.getSession().getUser().getRole();
        if (roleOfUser == Role.STAFF || roleOfUser == Role.MANAGER) {
            navigationButtonLabels.add("Staff Screen");
        }
        setBorder(BorderFactory.createEmptyBorder(10,10,0,10));

        setLayout(new GridLayout(1,3, 10, 0));
        for (String label : navigationButtonLabels) {
            JButton button = new JButton(label);
            button.addActionListener(controller);
            button.setActionCommand(label);
            add(button);
        }
    }
}
