package trainsofsheffield.views.staffarea;

import trainsofsheffield.controllers.StaffAreaController;
import trainsofsheffield.views.MainFrame;

import javax.swing.*;
import java.awt.*;

public class ManagerBackButtonPanel extends JPanel {
    private StaffAreaController controller;

    public ManagerBackButtonPanel(MainFrame frame) {
        this.controller = new StaffAreaController(frame);
        setBorder(BorderFactory.createEmptyBorder(10,10,0,10));
        setLayout(new GridLayout(1, 1));
        JButton backButton = new JButton("Back to Staff Screen");
        backButton.addActionListener(controller);
        add(backButton);
    }
}
