package trainsofsheffield.views;

import javax.swing.*;
import java.awt.*;

import trainsofsheffield.views.login.LoginPanel;
import trainsofsheffield.views.login.RegisterPanel;

public class LoginFrame  extends JFrame {
    final private Toolkit tk = Toolkit.getDefaultToolkit();
    final private int frameWidth = tk.getScreenSize().width*3/4;
    final private int frameHeight = tk.getScreenSize().height*3/4;

    private LoginPanel loginPanel;
    private RegisterPanel registerPanel;
    
    public JTextField getLoginEmailField() {
        return loginPanel.getLoginEmailField();
    }
    public JPasswordField getLoginPasswordField() {
        return loginPanel.getLoginPasswordField();
    }
    public JTextField getFirstnameField() {
        return registerPanel.getFirstnameField();
    }
    public JTextField getSurnameField() {
        return registerPanel.getSurnameField();
    }
    public JTextField getEmailField() {
        return registerPanel.getEmailField();
    }
    public JPasswordField getRegisterPasswordField() {
        return registerPanel.getRegisterPasswordField();
    }

    public JTextField getStreetLineField() {
        return registerPanel.getStreetLineField();
    }
    public JTextField getCityField() {
        return registerPanel.getCityField();
    }
    public JTextField getPoscodeField() {
        return registerPanel.getPostcodeField();
    }
    


    public LoginFrame(String title, String message, Boolean isLogin) throws HeadlessException {
        super(title);
        JTabbedPane tabbedPane = new JTabbedPane();
        setResizable(false);
        setSize(frameWidth, frameHeight);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        loginPanel = new LoginPanel(this, message);
        registerPanel = new RegisterPanel(this,message);
        Container loginContainer = getContentPane();
    
        loginContainer.setBackground(Color.decode("#D9D9D9"));
        loginContainer.setLayout(new FlowLayout(FlowLayout.CENTER)); 

        if (isLogin) {
            tabbedPane.addTab("Login",loginPanel);
            tabbedPane.addTab("Register",registerPanel);
        }
        else{
            tabbedPane.addTab("Register",registerPanel);
            tabbedPane.addTab("Login",loginPanel);
        }

        loginContainer.add(tabbedPane);
        setVisible(true);
    }
}
