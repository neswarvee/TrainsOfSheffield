package trainsofsheffield.controllers;

import trainsofsheffield.models.Product;
import trainsofsheffield.models.Product.ProductCategory;
import trainsofsheffield.views.LoginFrame;
import trainsofsheffield.views.MainFrame;
import trainsofsheffield.views.customerarea.CustomerBackButtonPanel;
import trainsofsheffield.views.customerarea.CustomerLogoutButtonPanel;
import trainsofsheffield.views.customerarea.homepage.ProductCategoryPanel;
import trainsofsheffield.views.customerarea.homepage.CustomerScreenNavigationPanel;
import trainsofsheffield.views.customerarea.orderpage.CurrentOrderPanel;
import trainsofsheffield.views.customerarea.orderpage.OrderHistoryPanel;
import trainsofsheffield.views.customerarea.productpage.ProductTablePanel;
import trainsofsheffield.views.customerarea.userdetails.UserPagePanel;
import trainsofsheffield.views.staffarea.StaffScreenNavigationPanel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Arrays;

public class CustomerAreaController implements ActionListener {
    private MainFrame frame;
    public CustomerAreaController(MainFrame pframe) {frame = pframe;}
    public void actionPerformed(ActionEvent e) throws IllegalArgumentException{
        Container contentPane = frame.getContainerContentPane();
        contentPane.removeAll(); //clear panels from CustomerScreenFrame content pane
        String command = e.getActionCommand();

        //if one of the product category buttons is pressed - change panels to that categories page
        if (Arrays.asList(Product.getProductCategoriesList()).contains(command)) {
            contentPane.add(new CustomerBackButtonPanel(frame), BorderLayout.NORTH);
            ProductCategory category = ProductCategory.stringToEnum(command);
            contentPane.add(new ProductTablePanel(category, frame.getSession(), false), BorderLayout.CENTER);
        }
        else if (command.equals("Order Page")) {
            contentPane.add(new CustomerBackButtonPanel(frame), BorderLayout.NORTH);
            try {
                contentPane.add(new CurrentOrderPanel(frame.getSession()), BorderLayout.CENTER);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            try {
                contentPane.add(new OrderHistoryPanel(frame.getSession()), BorderLayout.SOUTH);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
        else if (command.equals("Details")) {
            contentPane.add(new CustomerBackButtonPanel(frame), BorderLayout.NORTH);
            contentPane.add(new UserPagePanel(frame.getSession()), BorderLayout.CENTER);
            contentPane.add(new CustomerLogoutButtonPanel(frame), BorderLayout.SOUTH);
        }
        else if (command.equals("Staff Screen")) {
            frame.setTitle("Staff Area");

            contentPane.add(new ProductCategoryPanel(frame, true), BorderLayout.CENTER);
            contentPane.add(new StaffScreenNavigationPanel(frame), BorderLayout.NORTH);
        }
        else if (command.equals("Back")) {
            contentPane.add(new ProductCategoryPanel(frame, false), BorderLayout.CENTER);
            contentPane.add(new CustomerScreenNavigationPanel(frame), BorderLayout.NORTH);
        }
        else if (command.equals("Logout")) {
            frame.dispose();
            new LoginFrame("Login", null,true);
        }
        else {
            throw new IllegalArgumentException("CustomerAreaController received invalid Action Command");
        }

        //Renders the new panels on the frame
        frame.revalidate();
        frame.repaint();
    }
}
