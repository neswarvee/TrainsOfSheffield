package trainsofsheffield.views.staffarea;

import trainsofsheffield.models.User;
import trainsofsheffield.models.UserDatabaseOperations;
import trainsofsheffield.views.MainFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.util.List;


public class ManagerTablePanel extends JPanel {
    
    private JTable managerTable; 
    
    public ManagerTablePanel(MainFrame frame) {
        //Sets layout and adds components
        setLayout(new GridLayout(1, 1));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        managerTable = makeManagerTable();
        add(managerTable);
    }
    //Getter method for the table 
    public JTable getManagerTable() {
        return managerTable;
    }

    private JTable makeManagerTable(){
        DefaultTableModel model = new DefaultTableModel(); 
        JTable table;
        //Adding column headings
        model.addColumn("UserID");
        model.addColumn("Firstname");
        model.addColumn("Surname");
        model.addColumn("Role");
        table = new JTable(model);

        //Gets list of users
        UserDatabaseOperations operations = new UserDatabaseOperations();
        List<User> userList = operations.getUsersList();

        //Iterating through users and adding to the table
        for (int x = 0;x < userList.size();x++){
            model.addRow(new Object[]{userList.get(x).getUserID(),userList.get(x).getFirstName(),userList.get(x).getSurname(),userList.get(x).getRole()});
        }
        return table;
    }
}
