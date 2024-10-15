package trainsofsheffield.views.customerarea.homepage;

import trainsofsheffield.controllers.CustomerAreaController;
import trainsofsheffield.controllers.StaffAreaController;
import trainsofsheffield.views.MainFrame;

import javax.swing.*;
import java.awt.*;

public class ProductCategoryPanel extends JPanel{

    public ProductCategoryPanel(MainFrame frame, Boolean isStaffArea) {
        setLayout(new GridLayout(3, 3, 7, 7));
        setBorder(BorderFactory.createEmptyBorder(200,200,200,200));
        String[] productCategories = {"Train Sets", "Track Packs", "Locomotives", "Rolling Stocks", "Tracks", "Controllers"};

        add(new JLabel(""));

        JPanel labelPanel = new JPanel(new GridBagLayout());
        if (isStaffArea) {
            labelPanel.add(new JLabel("<html><u>Product/Stock Management</u></html>"), SwingConstants.CENTER);
        }
        else {
            labelPanel.add(new JLabel("<html><u>Browse Products</u></html>"), SwingConstants.CENTER);
        }
        add(labelPanel);

        add(new JLabel(""));

        for (String category : productCategories) {
            JButton button = new JButton(category);
            button.setBackground(Color.decode("#A37C7C"));
            button.setForeground(Color.decode("#D9D9D9"));
            button.setPreferredSize(new Dimension(frame.getFrameWidth()/4, frame.getFrameHeight()/4));
            if (isStaffArea) {
                button.addActionListener(new StaffAreaController(frame));
            } else {
                button.addActionListener(new CustomerAreaController(frame));
            }
            add(button);
        }
    }

}
