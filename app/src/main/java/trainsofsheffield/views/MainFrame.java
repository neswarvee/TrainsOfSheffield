package trainsofsheffield.views;

import javax.swing.*;
import java.awt.*;

import trainsofsheffield.models.Session;
import trainsofsheffield.views.customerarea.homepage.*;

public class MainFrame extends JFrame{
    final private Toolkit tk = Toolkit.getDefaultToolkit();
    final private int frameWidth = tk.getScreenSize().width*3/4;
    final private int frameHeight = tk.getScreenSize().height*3/4;
    private Container contentPane = getContentPane();
    private Session session;

    public MainFrame(Session session) throws HeadlessException {
        //setting up frame
        super("Customer Area");
        this.session = session;
        setSize(frameWidth, frameHeight);
        setResizable(false);
        setLocationRelativeTo(null); //centres frame
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        //setting up container
        contentPane.setBackground(Color.decode("#D9D9D9"));
        contentPane.setLayout(new BorderLayout());
        contentPane.add(new ProductCategoryPanel(this, false), BorderLayout.CENTER);
        contentPane.add(new CustomerScreenNavigationPanel(this), BorderLayout.NORTH);

        setVisible(true);
    }

    public int getFrameWidth() {
        return frameWidth;
    }

    public int getFrameHeight() {
        return frameHeight;
    }

    public Container getContainerContentPane() {
        return contentPane;
    }

    public Session getSession() {
        return session;
    }
}
