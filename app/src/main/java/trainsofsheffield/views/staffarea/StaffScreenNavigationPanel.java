package trainsofsheffield.views.staffarea;

import trainsofsheffield.controllers.StaffAreaController;
import trainsofsheffield.models.User.*;
import trainsofsheffield.views.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class StaffScreenNavigationPanel extends JPanel {
    private StaffAreaController controller;

    public StaffScreenNavigationPanel(MainFrame frame) {
        this.controller = new StaffAreaController(frame);

        ArrayList<String> navigationButtonLabels = new ArrayList<>(Arrays.asList("Order Queue", "All Order History"));
        Role roleOfUser = frame.getSession().getUser().getRole();
        if (roleOfUser == Role.MANAGER) {
            navigationButtonLabels.add("Manager Screen");
        }
        navigationButtonLabels.add("Back to Customer Screen");

        setBorder(BorderFactory.createEmptyBorder(10,10,0,10));

        setLayout(new GridLayout(1,4, 10, 0));
        for (String label : navigationButtonLabels) {
            JButton button = new JButton(label);
            button.addActionListener(controller);
            button.setActionCommand(label);
            add(button);
        }
    }
}
