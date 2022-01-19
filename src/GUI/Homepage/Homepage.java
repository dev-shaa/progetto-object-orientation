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
public class Homepage extends JFrame implements CategorySelectionListener {

    CategoriesTreeManager categoriesTreeManager;
    ReferencePanel referencePanel;
    CategoriesPanel categoriesPanel;
    SearchPanel referenceSearchPanel;

    /**
     * Crea la pagina principale con i dati relativi all'utente.
     * 
     * @param controller
     * @param user
     *            utente che ha eseguito l'accesso
     * @throws IllegalArgumentException
     *             se controller o user sono nulli
     * @throws CategoryDatabaseException
     */
    public Homepage(Controller controller, User user) throws IllegalArgumentException, CategoryDatabaseException {
        if (controller == null || user == null)
            throw new IllegalArgumentException();

        CategoryDAO categoryDAO = new CategoryDAOPostgreSQL(user);
        BibliographicReferenceDAO bibliographicReferenceDAO = new BibliographicReferenceDAO();

        setTitle("Pagina principale");
        setMinimumSize(new Dimension(400, 400));
        setBounds(100, 100, 800, 600);
        setCloseOperation();

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout(5, 5));
        setContentPane(contentPane);

        categoriesTreeManager = new CategoriesTreeManager(categoryDAO);
        referencePanel = new ReferencePanel(categoriesTreeManager, bibliographicReferenceDAO);
        categoriesPanel = new CategoriesPanel(categoriesTreeManager);
        referenceSearchPanel = new SearchPanel(referencePanel, categoriesTreeManager);

        JSplitPane subSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, categoriesPanel, referencePanel);
        subSplitPane.setResizeWeight(0.15);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, subSplitPane, referenceSearchPanel);
        splitPane.setResizeWeight(0.8);

        contentPane.add(new UserInfoPanel(controller, user), BorderLayout.NORTH);
        contentPane.add(splitPane, BorderLayout.CENTER);
    }

    /**
     * Imposta le operazioni di chiusura: chiede all'utente conferma prima di uscire.
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

    @Override
    public void onCategorySelected(Category selectedCategory) {
        referencePanel.showReferences(selectedCategory);
    }

}
