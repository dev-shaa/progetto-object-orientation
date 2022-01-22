package GUI.Homepage.UserInfo;

import Entities.User;
import java.awt.event.*;
import java.util.ArrayList;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Pannello che mostra il nome utente e il pulsante per eseguire il logout.
 */
public class UserInfoPanel extends JPanel {

    private JLabel userLabel;
    private ArrayList<LogoutListener> logoutListeners;

    /**
     * Crea il pannello con le informazioni dell'utente passato.
     * 
     * @param user
     *            utente che ha eseguito l'accesso
     * @throws IllegalArgumentException
     *             se {@code user == null}
     * @see #setUser(User)
     */
    public UserInfoPanel(User user) throws IllegalArgumentException {

        Color darkGray = Color.decode("#24292f");

        setLayout(new BorderLayout(5, 0));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        setBackground(darkGray);

        setUser(user);

        JButton logout = new JButton(new ImageIcon("images/logout_white.png"));
        logout.setToolTipText("Esci");
        logout.setHorizontalAlignment(SwingConstants.RIGHT);
        logout.setBackground(darkGray);
        logout.setBorderPainted(false);
        logout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (LogoutListener logoutListener : logoutListeners) {
                    logoutListener.onLogout();
                }
            }
        });

        add(userLabel, BorderLayout.WEST);
        add(logout, BorderLayout.EAST);
    }

    /**
     * Aggiorna il pannello con le informazioni dell'utente passato.
     * 
     * @param user
     *            utente che ha eseguito l'accesso
     * @throws IllegalArgumentException
     *             se {@code user == null}
     */
    public void setUser(User user) throws IllegalArgumentException {
        if (user == null)
            throw new IllegalArgumentException("user non può essere null");

        if (userLabel == null) {
            userLabel = new JLabel();
            userLabel.setHorizontalAlignment(SwingConstants.LEADING);
            userLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
            userLabel.setIcon(new ImageIcon("images/bookmark_light.png"));
            userLabel.setForeground(Color.WHITE);
        }

        userLabel.setText("<html><b>Bentornato, " + user.getName() + "</b></html>");
    }

    /**
     * Aggiunge un listener all'evento di logout.
     * 
     * @param listener
     *            listener da aggiungere
     */
    public void addLogoutListener(LogoutListener listener) {
        if (listener != null) {
            if (logoutListeners == null)
                logoutListeners = new ArrayList<>(1);

            logoutListeners.add(listener);
        }
    }

    /**
     * Rimuove un listener dall'evento di logout.
     * 
     * @param listener
     *            listener da rimuovere
     */
    public void removeLogoutListener(LogoutListener listener) {
        if (listener != null && logoutListeners != null)
            logoutListeners.remove(listener);
    }

}
