package trainsofsheffield.models;
import trainsofsheffield.views.MainFrame;
import javax.swing.*;

import java.util.List;
import java.sql.*;
import java.util.ArrayList;

public class UserDatabaseOperations {
    public List<User> getUsersList(){
        List<User> userList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team010", "team010", "AhKeh5IeV")) {
            //Selects all users
            String getUsersQuery = "SELECT * FROM Users ";
            Statement statement = connection.createStatement();
            ResultSet userResult = statement.executeQuery(getUsersQuery);
            while(userResult.next()){
                //Gets the email and creates a user which is added to the return list
                String email = userResult.getString("email");
                User currentUser = new User(email);      
                userList.add(currentUser);          
            }
            return userList;
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    public String makeStaff(MainFrame frame,JTable managerTablePanel){
        //Gets selected user
        int selectedRow = managerTablePanel.getSelectedRow();
        if(selectedRow == -1){
            return "No user selected";
        }

        if(managerTablePanel.getValueAt(selectedRow, 3).toString() == "Staff" ||  managerTablePanel.getValueAt(selectedRow, 3).toString() == "Manager"){
            return "User is already staff";
        }

        
        String userID = managerTablePanel.getValueAt(selectedRow, 0).toString();
        //Updates users role
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team010", "team010", "AhKeh5IeV")) {
            String giveStaffRole = "UPDATE Users SET role = 'Staff' WHERE userID =?";
            PreparedStatement pstmt = connection.prepareStatement(giveStaffRole);
            pstmt.setString(1, userID);
            pstmt.executeUpdate();
            
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return "Role updated";
    }
    public String makeCustomer(MainFrame frame,JTable managerTablePanel){
        //Gets selected user
        int selectedRow = managerTablePanel.getSelectedRow();
        
        //Determines return message
        if(selectedRow == -1){
            return "No user selected";
        }
        
        if(managerTablePanel.getValueAt(selectedRow, 3).toString() == "Customer"){
            return "User is already customer";
        }
        else if(managerTablePanel.getValueAt(selectedRow,3).toString() == "Manager"){
            return "Cannot demote managers";
        }

        
        //Updates users role
        String userID = managerTablePanel.getValueAt(selectedRow, 0).toString();
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team010", "team010", "AhKeh5IeV")) {
            String giveStaffRole = "UPDATE Users SET role = 'Customer' WHERE userID =?";
            PreparedStatement pstmt = connection.prepareStatement(giveStaffRole);
            pstmt.setString(1, userID);
            pstmt.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return "Role updated";
    }
}