package GUI.Homepage;

import GUI.*;
import GUI.Categories.*;
import DAO.*;
import Entities.*;
import Exceptions.*;
import GUI.Homepage.References.*;
import GUI.Homepage.Search.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.BorderLayout;
import javax.swing.*;

/**
 * Classe che si occupa di impostare le componenti base della pagina principale,
 * che mostra tutti i riferimenti e le categorie.
 * 
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
     */
    public Homepage(Controller controller, User user, CategoryDAO categoryDAO, BibliographicReferenceDAO bibliographicReferenceDAO) throws IllegalArgumentException, CategoryDatabaseException {

        // TODO: dao check

        if (controller == null || user == null)
            throw new IllegalArgumentException();

        setTitle("Pagina principale");
        setMinimumSize(new Dimension(400, 400));
        setBounds(100, 100, 800, 600);
        setCloseOperation();

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout(5, 5));
        setContentPane(contentPane);

        CategoriesTreeManager categoriesTreeManager = new CategoriesTreeManager(categoryDAO);
        ReferencePanel referencePanel = new ReferencePanel(categoriesTreeManager, bibliographicReferenceDAO);
        CategoriesPanel categoriesPanel = new CategoriesPanel(categoriesTreeManager, referencePanel);
        SearchPanel referenceSearchPanel = new SearchPanel(referencePanel, categoriesTreeManager);

        JSplitPane subSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, categoriesPanel, referencePanel);
        subSplitPane.setResizeWeight(0.15);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, subSplitPane, referenceSearchPanel);
        splitPane.setResizeWeight(0.8);

        contentPane.add(new UserInfoPanel(controller, user), BorderLayout.NORTH);
        contentPane.add(splitPane, BorderLayout.CENTER);
    }

    /**
     * TODO: commenta
     * Imposta le operazioni di chiusura.
     */
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

}