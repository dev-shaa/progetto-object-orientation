import java.awt.*;
import java.awt.BorderLayout;
import javax.swing.*;
import javax.swing.border.*;

public class MainWindow extends JFrame {
    public MainWindow(User user) {
        setTitle("Pagina principale");
        setMinimumSize(new Dimension(400, 400));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // TODO: messaggio di conferma
        setBounds(100, 100, 720, 540);

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout(5, 5));
        setContentPane(contentPane);

        JLabel userLabel = new JLabel(user.name, SwingConstants.RIGHT);
        userLabel.setHorizontalTextPosition(SwingConstants.LEFT);
        userLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
        userLabel.setIcon(new ImageIcon("images/user.png"));

        contentPane.add(userLabel, BorderLayout.NORTH);
        contentPane.add(new CategoryPanel(user), BorderLayout.WEST);
        contentPane.add(new ReferencePanel(), BorderLayout.CENTER);
    }
}
