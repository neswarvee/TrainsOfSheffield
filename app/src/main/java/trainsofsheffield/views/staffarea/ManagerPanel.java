package trainsofsheffield.views.staffarea;

import java.awt.BorderLayout;

import javax.swing.JOptionPane;
import javax.swing.JPanel;


import trainsofsheffield.views.MainFrame;

public class ManagerPanel extends JPanel {
    private ManagerTablePanel managerTablePanel; 
    private ManagerBackButtonPanel managerBackButtonPanel;
    private ManagerButtonsPanel managerButtonsPanel;
    private JOptionPane errorMessage;

    public ManagerPanel(MainFrame frame,String message){
        setLayout(new BorderLayout());
        errorMessage = new JOptionPane(message);
        //popping up error message
        if(!message.isEmpty()){
            if (message == ""){
                JOptionPane.showMessageDialog(frame, message, "Message", JOptionPane.PLAIN_MESSAGE);         
            }
            else{
                JOptionPane.showMessageDialog(frame, message, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

    
        //Instantiating panels
        managerBackButtonPanel = new ManagerBackButtonPanel(frame);
        managerTablePanel = new ManagerTablePanel(frame);
        managerButtonsPanel = new ManagerButtonsPanel(frame,managerTablePanel);
        
        //Adding to panel
        add(managerBackButtonPanel,BorderLayout.NORTH);
        add(managerTablePanel,BorderLayout.CENTER);
        add(managerButtonsPanel,BorderLayout.SOUTH);
    }

}
