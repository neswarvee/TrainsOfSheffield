package trainsofsheffield.views.customerarea.userdetails;

import javax.swing.*;

import trainsofsheffield.models.Session;

import java.awt.*;

public class UserPagePanel  extends JPanel {

    private void setTextColorAndBorder(Container container, Color color) {
        for (Component component : container.getComponents()) {
            if (component instanceof Container) {
                setTextColorAndBorder((Container) component, color);
            }
            if (component instanceof JLabel || component instanceof JTextField ||
                    component instanceof JButton) {
                component.setForeground(color);
            }
            if (component instanceof JPanel) {
                ((JPanel) component).setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            }
        }
    }

    public UserPagePanel(Session session) throws HeadlessException {
        setLayout(new BorderLayout());

        // Calculate dimensions for the initial visible dashboard
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int dashboardWidth = screenSize.width / 2;
        int dashboardHeight = screenSize.height / 2;

        // Set the size and location of the dashboard
        setSize(dashboardWidth, dashboardHeight);
        setLocation(screenSize.width / 4, screenSize.height / 4);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome");
        welcomeLabel.setForeground(Color.decode("#D9D9D9"));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setVerticalAlignment(SwingConstants.CENTER);
        headerPanel.add(welcomeLabel);

        // User Details
        UserDetailsPanel userDetailsPanel = new UserDetailsPanel(session);

        // User Bank Details
        BankDetailsPanel userBankDetailsPanel = new BankDetailsPanel(session);

        // Add components to the main dashboard panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 25, 25, 25));
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(userDetailsPanel, BorderLayout.CENTER);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5))); // Margin
        mainPanel.add(userBankDetailsPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Set text color and margin for all components
        setTextColorAndBorder(mainPanel, Color.decode("#7F3333"));
    }
}