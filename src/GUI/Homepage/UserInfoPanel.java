package GUI.Homepage;

import GUI.Controller;
import Entities.User;
import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Pannello che mostra il nome utente e il pulsante per eseguire il logout.
 */
public class UserInfoPanel extends JPanel {

    /**
     * Crea {@code UserInfoPanel} con i dati relativi all'utente.
     * 
     * @param controller
     *            il controller del programma
     * @param user
     *            l'utente che ha eseguito l'accesso
     */
    public UserInfoPanel(Controller controller, User user) {

        Color darkGray = Color.decode("#24292f");

        setLayout(new BorderLayout(5, 0));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        setBackground(darkGray);

        String welcomeMessage = "<html><b>Bentornato, " + user.getName() + "</b></html>";

        JLabel icon = new JLabel(welcomeMessage, SwingConstants.LEFT);
        icon.setHorizontalTextPosition(SwingConstants.RIGHT);
        icon.setIcon(new ImageIcon("images/bookmark_light.png"));
        icon.setForeground(Color.WHITE);

        JButton logout = new JButton(new ImageIcon("images/logout_white.png"));
        logout.setToolTipText("Esci");
        logout.setHorizontalAlignment(SwingConstants.RIGHT);
        logout.setBackground(darkGray);
        logout.setBorderPainted(false);
        logout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.openLoginPage();
            }
        });

        add(icon, BorderLayout.WEST);
        add(logout, BorderLayout.EAST);
    }
}
