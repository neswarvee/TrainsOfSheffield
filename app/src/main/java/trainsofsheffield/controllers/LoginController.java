package trainsofsheffield.controllers;

import trainsofsheffield.views.MainFrame;
import trainsofsheffield.views.login.RegisterPanel;
import trainsofsheffield.views.LoginFrame;
import trainsofsheffield.models.Login;
import trainsofsheffield.models.Register;
import trainsofsheffield.models.Session;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The LoginController class handles user actions related to login and registration.
 * It implements ActionListener to handle user actions.
 */
public class LoginController implements ActionListener {
    
    private LoginFrame frame;
    private Login loginModel;
    private Register registerModel;
    
    /**
     * Constructor for LoginController.
     * @param pframe The LoginFrame object to be controlled.
     */
    public LoginController(LoginFrame pframe) {
        frame = pframe;
        loginModel = new Login();
        registerModel = new Register();
    }

    /**
     * Method to handle user actions.
     * @param e The ActionEvent object representing the user action.
     */
    public void actionPerformed(ActionEvent e){
        // Setup
        Container contentPane = frame.getContentPane();
        contentPane.removeAll();
        String command = e.getActionCommand();
        switch (command) {
            case "Login":
                String loginEmail = frame.getLoginEmailField().getText();
                char[] loginPasswordChars = frame.getLoginPasswordField().getPassword();

                // Verify login using the loginModel
                Login loginResult = loginModel.verifyLogin(loginEmail, loginPasswordChars);
                String loginMessage = loginResult.getMessage();
                Session loginSession = loginResult.getSession();

                // If login is successful, dispose the frame and open CustomerScreenFrame
                if (loginMessage.equals("Login successful")) {
                    frame.dispose();
                    new MainFrame(loginSession);
                } 
                // If login is unsuccessful, dispose the frame and open LoginFrame with the "error" message
                else {
                    frame.dispose();
                    new LoginFrame("Login", loginMessage,true);
                }
                break;
            case "Register?":
                frame.add(new RegisterPanel(frame,null));
                break;
            case "Register":
                String firstname = frame.getFirstnameField().getText();
                String surname = frame.getSurnameField().getText();
                String email = frame.getEmailField().getText();
                char[] registerPasswordChars = frame.getRegisterPasswordField().getPassword();
                String streetLine = frame.getStreetLineField().getText();
                String city = frame.getCityField().getText();
                String postcode = frame.getPoscodeField().getText();
                System.out.print(postcode);
                //Verify Register using the registerModel
                Register registerResult = registerModel.verifyRegister(firstname,surname,email,registerPasswordChars,streetLine,city,postcode);
                String registerMessage = registerResult.getMessage();
                // If registration is successful, dispose the frame and open CustomerScreenFrame
                if (registerMessage.equals("Registration successful")) {
                    frame.dispose();
                    new LoginFrame("Login", registerMessage,false);
                } 
                // If registration is unsuccessful, dispose the frame and open RegisterPanel with the "error" message
                else {
                    frame.dispose();
                    new LoginFrame("Login", registerMessage,false);
                }
                break;
        }
        frame.revalidate();
        frame.repaint();
    }
}

