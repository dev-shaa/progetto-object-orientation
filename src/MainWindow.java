import java.awt.*;
import java.awt.event.*;
import java.awt.BorderLayout;
import javax.swing.*;
import javax.swing.border.*;

/**
 * Classe che si occupa di impostare le componenti base della pagina principale,
 * che mostra tutti i riferimenti e le categorie.
 * 
 * @version 0.1
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
    public MainWindow(User user) {
        setTitle("Pagina principale");
        setMinimumSize(new Dimension(400, 400));
        setBounds(100, 100, 720, 540);

        // mostra un messaggio di conferma quando l'utente tenta di uscire
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                int confirmDialogBoxOption = JOptionPane.showConfirmDialog(null,
                        "Sicuro di volere uscire?\nTutte le modifiche non salvate saranno perse.", "Esci",
                        JOptionPane.YES_NO_OPTION);

                if (confirmDialogBoxOption == JOptionPane.YES_OPTION)
                    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        });

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
