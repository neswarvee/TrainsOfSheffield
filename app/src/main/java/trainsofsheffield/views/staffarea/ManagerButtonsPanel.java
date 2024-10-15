package trainsofsheffield.views.staffarea;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import trainsofsheffield.controllers.StaffAreaController;
import trainsofsheffield.views.MainFrame;

public class ManagerButtonsPanel extends JPanel{
    private StaffAreaController controller;
    private JButton makeStaffButton,makeCustomerButton;

    public ManagerButtonsPanel(MainFrame frame,ManagerTablePanel managerTablePanel){
        //Sets layout and adds components
        setLayout(new GridLayout(1,2));
        this.controller = new StaffAreaController(frame,managerTablePanel);
        makeStaffButton = new JButton("Make Staff");
        makeStaffButton.addActionListener(controller);
        makeCustomerButton = new JButton("Make Customer");
        makeCustomerButton.addActionListener(controller);
        add(makeStaffButton);
        add(makeCustomerButton);
    }
}
