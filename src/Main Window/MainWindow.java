import java.awt.*;
import java.awt.event.*;
import java.awt.BorderLayout;
import javax.swing.*;
import javax.swing.border.*;

/**
 * Classe che si occupa di impostare le componenti base della pagina principale,
 * che mostra tutti i riferimenti e le categorie.
 * 
 * @version 0.2
 * @author Salvatore Di Gennaro
 * @see CategoryPanel
 * @see ReferencePanel
 */
public class MainWindow extends JFrame {

    /**
     * Crea {@code MainWindow} con i dati relativi all'utente.
     * 
     * @param user l'utente che ha eseguito l'accesso
     * @since 0.1
     * @author Salvatore Di Gennaro
     */
    public MainWindow(Controller controller, User user) {
        setTitle("Pagina principale");
        setMinimumSize(new Dimension(400, 400));
        setBounds(100, 100, 720, 540);
        setCloseOperation();

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout(5, 5));
        setContentPane(contentPane);

        ReferencePanel referencePanel = new ReferencePanel(user);
        CategoryPanel categoryPanel = new CategoryPanel(user, referencePanel);
        ReferenceSearchPanel referenceSearchPanel = new ReferenceSearchPanel(this);

        JSplitPane subSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, categoryPanel, referencePanel);
        subSplitPane.setResizeWeight(0.15);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, subSplitPane, referenceSearchPanel);
        splitPane.setResizeWeight(0.8);

        contentPane.add(getUserInfoPanel(controller, user.name), BorderLayout.NORTH);
        contentPane.add(splitPane, BorderLayout.CENTER);
    }

    private void setCloseOperation() {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                int confirmDialogBoxOption = JOptionPane.showConfirmDialog(null, "Sicuro di volere uscire?", "Esci",
                        JOptionPane.YES_NO_OPTION);

                if (confirmDialogBoxOption == JOptionPane.YES_OPTION)
                    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        });
    }

    private JPanel getUserInfoPanel(Controller controller, String username) {
        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BorderLayout(5, 0));
        userInfoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 16));
        userInfoPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        JLabel userLabel = new JLabel("Bentornato, " + username, SwingConstants.LEFT);
        userLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
        userLabel.setIcon(new ImageIcon("images/user.png"));

        JButton logoutButton = new JButton("Esci", new ImageIcon("images/logout.png"));
        logoutButton.setHorizontalAlignment(SwingConstants.RIGHT);
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.logout();
            }
        });

        userInfoPanel.add(userLabel, BorderLayout.WEST);
        userInfoPanel.add(logoutButton, BorderLayout.EAST);

        return userInfoPanel;
    }

}
