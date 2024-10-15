package trainsofsheffield.views.staffarea;

import trainsofsheffield.controllers.StaffAreaController;
import trainsofsheffield.views.MainFrame;

import javax.swing.*;
import java.awt.*;

public class StaffBackButtonPanel extends JPanel {
    private StaffAreaController controller;

    public StaffBackButtonPanel(MainFrame frame) {
        this.controller = new StaffAreaController(frame);

        setBorder(BorderFactory.createEmptyBorder(10,10,0,10));

        setLayout(new GridLayout(1, 1));
        JButton backButton = new JButton("Back");
        backButton.addActionListener(controller);
        add(backButton);
    }
}
