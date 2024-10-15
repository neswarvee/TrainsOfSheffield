package trainsofsheffield.controllers;

import trainsofsheffield.models.Product;
import trainsofsheffield.models.UserDatabaseOperations;
import trainsofsheffield.models.Product.*;
import trainsofsheffield.views.MainFrame;
import trainsofsheffield.views.customerarea.homepage.ProductCategoryPanel;
import trainsofsheffield.views.customerarea.productpage.ProductTablePanel;
import trainsofsheffield.views.customerarea.homepage.CustomerScreenNavigationPanel;
import trainsofsheffield.views.staffarea.ManagerPanel;
import trainsofsheffield.views.staffarea.ManagerTablePanel;
import trainsofsheffield.views.staffarea.StaffBackButtonPanel;
import trainsofsheffield.views.staffarea.StaffScreenNavigationPanel;
import trainsofsheffield.views.staffarea.orderhistorypage.StaffOrderHistoryPanel;
import trainsofsheffield.views.staffarea.orderqueuepage.OrderQueuePanel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Arrays;

public class StaffAreaController implements ActionListener {
    private UserDatabaseOperations userDatabaseOperations;
    private MainFrame frame;
    private ManagerTablePanel managerTablePanel;

    public StaffAreaController(MainFrame pFrame) {frame = pFrame;}
    public StaffAreaController(MainFrame frame,ManagerTablePanel managerTablePanel){
        this.managerTablePanel = managerTablePanel;
        this.frame = frame;
        
    }
    public void actionPerformed(ActionEvent e) throws IllegalArgumentException {
        userDatabaseOperations = new UserDatabaseOperations();
        Container contentPane = frame.getContainerContentPane();
        contentPane.removeAll(); //clear panels from CustomerScreenFrame content pane
        String command = e.getActionCommand();

        //if one of the product category buttons is pressed - change panels to that categories page
        if (Arrays.asList(Product.getProductCategoriesList()).contains(command)) {
            contentPane.add(new StaffBackButtonPanel(frame), BorderLayout.NORTH);
            ProductCategory category = ProductCategory.stringToEnum(command);
            contentPane.add(new ProductTablePanel(category, frame.getSession(), true), BorderLayout.CENTER);
            //TODO add content pane for product managment page
        }
        else if (command.equals("Order Queue")) {
            contentPane.add(new StaffBackButtonPanel(frame), BorderLayout.NORTH);
            //TODO add order queue panel
            try {
                contentPane.add(new OrderQueuePanel(), BorderLayout.CENTER);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        else if (command.equals("All Order History")) {
            contentPane.add(new StaffBackButtonPanel(frame), BorderLayout.NORTH);
            try {
                contentPane.add(new StaffOrderHistoryPanel(), BorderLayout.CENTER);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
        else if (command.equals("Manager Screen")) {
            frame.setTitle("Manager Area");
            contentPane.add(new ManagerPanel((frame),""),BorderLayout.CENTER);
            //TODO add manager screen panels
        }
        else if (command.equals("Back to Customer Screen")) {
            frame.setTitle("Customer Area");
            contentPane.add(new ProductCategoryPanel(frame, false), BorderLayout.CENTER);
            contentPane.add(new CustomerScreenNavigationPanel(frame), BorderLayout.NORTH);
        }
        else if (command.equals("Back to Staff Screen") || command.equals("Back")) {
            frame.setTitle("Staff Area");

            contentPane.add(new ProductCategoryPanel(frame, true), BorderLayout.CENTER);
            contentPane.add(new StaffScreenNavigationPanel(frame), BorderLayout.NORTH);
        }
        else if (command.equals("Make Staff")) {
            String message = userDatabaseOperations.makeStaff(frame,managerTablePanel.getManagerTable());
            contentPane.add(new ManagerPanel(frame,message), BorderLayout.CENTER);

        }
        else if (command.equals("Make Customer")) {
            String message = userDatabaseOperations.makeCustomer(frame,managerTablePanel.getManagerTable());
            contentPane.add(new ManagerPanel(frame, message), BorderLayout.CENTER);
        }
        else {
            throw new IllegalArgumentException("StaffAreaController received invalid Action Command");
        }

        //Renders the new panels on the frame
        frame.revalidate();
        frame.repaint();
    }
}
