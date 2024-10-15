package trainsofsheffield.views.login;

import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class TwoButtonPanel extends JPanel {
    public TwoButtonPanel(JButton button1,JButton button2){
        setLayout(new FlowLayout(FlowLayout.CENTER));
        setSize(20,200);
        setBackground(Color.decode("#D9D9D9"));
        setBorder(null);
        add(button1);
        add(button2);
    }
}
