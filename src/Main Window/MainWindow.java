import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import java.awt.BorderLayout;
import javax.swing.*;

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

    ReferencePanel referencePanel;
    CategoryPanel categoryPanel;
    ReferenceSearchPanel referenceSearchPanel;

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
        // subSplitPane.setDividerSize(10);
        subSplitPane.setResizeWeight(0.15);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, subSplitPane, referenceSearchPanel);
        // splitPane.setDividerSize(10);
        splitPane.setResizeWeight(0.8);

        contentPane.add(getUserInfoPanel(controller, user), BorderLayout.NORTH);
        contentPane.add(splitPane, BorderLayout.CENTER);
        // contentPane.add(referencePanel, BorderLayout.CENTER);
        // contentPane.add(categoryPanel, BorderLayout.WEST);
        // contentPane.add(referenceSearchPanel, BorderLayout.EAST);
    }

    private void setCloseOperation() {
        // TODO: sarebbe meglio delegare la chiusura del programma al controller

        // mostra un messaggio di conferma quando l'utente tenta di uscire
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

    private JPanel getUserInfoPanel(Controller controller, User user) {

        // FIXME: alignment

        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new FlowLayout());
        userInfoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 16));

        JLabel userLabel = new JLabel(user.name, SwingConstants.LEFT);
        userLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
        userLabel.setIcon(new ImageIcon("images/user.png"));

        JButton logoutButton = new JButton(new ImageIcon("images/logout.png"));
        logoutButton.setHorizontalAlignment(SwingConstants.RIGHT);
        logoutButton.setToolTipText("Log out");
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.logout();
            }
        });

        userInfoPanel.add(userLabel);
        userInfoPanel.add(logoutButton);

        return userInfoPanel;
    }

    public void searchReferences(String[] keywords, Category[] categories, Date startDate, Date endDate) {
        // TODO: implementa

        // referencePanel.setReferences(null);
    }

}
