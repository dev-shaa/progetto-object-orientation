import java.awt.*;
import java.awt.event.*;
import java.awt.BorderLayout;
import javax.swing.*;

/**
 * Classe che si occupa di impostare le componenti base della pagina principale,
 * che mostra tutti i riferimenti e le categorie.
 * 
 * @version 0.3
 * @author Salvatore Di Gennaro
 * @see UserInfoPanel
 * @see CategoriesPanel
 * @see ReferencePanel
 */
public class Homepage extends JFrame {

    /**
     * Crea {@code Homepage} con i dati relativi all'utente.
     * 
     * @param user
     *            l'utente che ha eseguito l'accesso
     * @since 0.1
     */
    public Homepage(Controller controller, User user, CategoryDAO categoryDAO) throws IllegalArgumentException, CategoryDatabaseException {

        setTitle("Pagina principale");
        setMinimumSize(new Dimension(400, 400));
        setBounds(100, 100, 720, 540);
        setCloseOperation();

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout(5, 5));
        setContentPane(contentPane);

        ReferencePanel referencePanel = new ReferencePanel(controller);
        CategoriesPanel categoryPanel = new CategoriesPanel(referencePanel, categoryDAO);
        ReferenceSearchPanel referenceSearchPanel = new ReferenceSearchPanel(this);

        JSplitPane subSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, categoryPanel, referencePanel);
        subSplitPane.setResizeWeight(0.15);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, subSplitPane, referenceSearchPanel);
        splitPane.setResizeWeight(0.8);

        contentPane.add(new UserInfoPanel(controller, user), BorderLayout.NORTH);
        contentPane.add(splitPane, BorderLayout.CENTER);
    }

    private void setCloseOperation() {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                int confirmDialogBoxOption = JOptionPane.showConfirmDialog(null, "Sicuro di volere uscire?", "Esci", JOptionPane.YES_NO_OPTION);

                if (confirmDialogBoxOption == JOptionPane.YES_OPTION)
                    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        });
    }

}
