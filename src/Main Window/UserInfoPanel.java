import java.awt.event.*;
import java.awt.BorderLayout;
import javax.swing.*;
import javax.swing.border.*;

/**
 * Pannello che mostra il nome utente e il pulsante per eseguire il logout.
 */
public class UserInfoPanel extends JPanel {
    public UserInfoPanel(Controller controller, User user) {
        setLayout(new BorderLayout(5, 0));
        setBorder(new EmptyBorder(5, 5, 5, 5));

        JLabel userLabel = new JLabel("Bentornato, " + user.name, SwingConstants.LEFT);
        userLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
        userLabel.setIcon(new ImageIcon("images/user.png"));

        JButton logoutButton = new JButton("Esci", new ImageIcon("images/logout.png"));
        logoutButton.setHorizontalAlignment(SwingConstants.RIGHT);
        logoutButton.setBorderPainted(false);
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.logout();
            }
        });

        add(userLabel, BorderLayout.WEST);
        add(logoutButton, BorderLayout.EAST);
    }
}
