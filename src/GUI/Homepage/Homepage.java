package GUI.Homepage;

import GUI.*;
import DAO.*;
import Entities.*;
import Exceptions.*;
import GUI.Homepage.Categories.*;
import GUI.Homepage.References.*;
import GUI.Homepage.Search.*;
import GUI.Homepage.UserInfo.LogoutListener;
import GUI.Homepage.UserInfo.UserInfoPanel;

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
public class Homepage extends JFrame implements CategorySelectionListener, LogoutListener, ReferenceSearchListener {

    private User user;
    private Controller controller;

    // private CategoryTreeModel categoriesTree;
    private ReferencePanel referencePanel;
    private CategoriesPanel categoriesPanel;
    private SearchPanel referenceSearchPanel;

    /**
     * TODO: commenta
     * Crea la pagina principale con i dati relativi all'utente.
     * 
     * @param controller
     * @param user
     *            utente che ha eseguito l'accesso
     * @throws IllegalArgumentException
     *             se controller o user sono nulli
     * @throws CategoryDatabaseException
     */
    public Homepage(Controller controller, User user) throws IllegalArgumentException, CategoryDatabaseException, ReferenceDatabaseException {
        setController(controller);
        setUser(user);

        setTitle("Pagina principale");
        setMinimumSize(new Dimension(400, 400));
        setBounds(100, 100, 800, 600);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                int confirmDialogBoxOption = JOptionPane.showConfirmDialog(null, "Sicuro di volere uscire?", "Esci", JOptionPane.YES_NO_OPTION);

                if (confirmDialogBoxOption == JOptionPane.YES_OPTION)
                    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        });

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout(5, 5));
        setContentPane(contentPane);

        CategoryDAO categoryDAO = new CategoryDAOPostgreSQL(user);
        BibliographicReferenceDAO referenceDAO = new BibliographicReferenceDAOPostgreSQL(user);

        // categoriesTree = new CategoryDAOPostgreSQL(user).getUserCategories();
        categoriesPanel = new CategoriesPanel(categoryDAO);
        categoriesPanel.getTreePanel().addSelectionListener(this);

        referencePanel = new ReferencePanel(categoriesPanel.getCategoriesTree(), referenceDAO);
        referenceSearchPanel = new SearchPanel(categoriesPanel.getCategoriesTree());
        referenceSearchPanel.addReferenceSearchListener(this);

        JSplitPane subSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, categoriesPanel, referencePanel);
        subSplitPane.setResizeWeight(0.15);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, subSplitPane, referenceSearchPanel);
        splitPane.setResizeWeight(1);

        UserInfoPanel userInfoPanel = new UserInfoPanel(user);
        userInfoPanel.addLogoutListener(this);

        contentPane.add(userInfoPanel, BorderLayout.NORTH);
        contentPane.add(splitPane, BorderLayout.CENTER);
    }

    public void setController(Controller controller) throws IllegalArgumentException {
        if (controller == null)
            throw new IllegalArgumentException("controller non può essere null");

        this.controller = controller;
    }

    public void setUser(User user) {
        if (user == null)
            throw new IllegalArgumentException("user non può essere null");

        this.user = user;
    }

    @Override
    public void onLogout() {
        controller.openLoginPage();
    }

    @Override
    public void onCategorySelected(Category selectedCategory) {
        referencePanel.showReferences(selectedCategory);
    }

    @Override
    public void onReferenceSearch(Search search) {
        // referencePanel.showReferences(search);
    }

}
