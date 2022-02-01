package GUI.Homepage;

import Entities.*;
import GUI.Homepage.Categories.*;
import GUI.Homepage.References.*;
import GUI.Homepage.Search.*;
import GUI.Homepage.UserInfo.UserInfoPanel;

import Controller.Controller;

import java.awt.*;
import java.awt.event.*;
import java.awt.BorderLayout;
import javax.swing.*;

/**
 * Classe che si occupa di impostare le componenti base della pagina principale, che mostra tutti i riferimenti e le categorie.
 */
public class Homepage extends JFrame implements CategorySelectionListener, ReferenceSearchListener {

    private Controller controller;

    private ReferencePanel referencePanel;
    private CategoriesPanel categoriesPanel;
    private SearchPanel referenceSearchPanel;
    private UserInfoPanel userInfoPanel;

    /**
     * Crea una nuova pagina principale con il controller indicato.
     * 
     * @param controller
     *            controller della GUI
     */
    public Homepage(Controller controller) {
        setController(controller);

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

        userInfoPanel = new UserInfoPanel(getController().getUser());
        contentPane.add(userInfoPanel, BorderLayout.NORTH);

        categoriesPanel = new CategoriesPanel(getController().getCategoryController());
        categoriesPanel.getTreePanel().addSelectionListener(this);

        referencePanel = new ReferencePanel(getController().getReferenceController());

        referenceSearchPanel = new SearchPanel(getController().getCategoryController());
        referenceSearchPanel.addReferenceSearchListener(this);

        JSplitPane subSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, categoriesPanel, referencePanel);
        subSplitPane.setResizeWeight(0.15);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, subSplitPane, referenceSearchPanel);
        splitPane.setResizeWeight(1);

        contentPane.add(splitPane, BorderLayout.CENTER);
    }

    @Override
    public void setVisible(boolean b) {
        if (b)
            reset();

        super.setVisible(b);
    }

    @Override
    public void onCategorySelection(Category selectedCategory) {
        getReferencePanel().setReferences(getController().getReferenceController().getReferences(selectedCategory));
    }

    @Override
    public void onCategoryClearSelection() {
        // non fare niente
    }

    @Override
    public void onReferenceSearch(Search search) {
        getCategoriesPanel().getTreePanel().clearSelection();
        getReferencePanel().setReferences(getController().getReferenceController().getReferences(search));
    }

    /**
     * Restituisce il controller della GUI.
     * 
     * @return controller
     */
    public Controller getController() {
        return controller;
    }

    /**
     * Imposta il controller della GUI.
     * 
     * @param controller
     *            controller della GUI
     */
    public void setController(Controller controller) {
        if (controller == null)
            throw new IllegalArgumentException("controller can't be null");

        this.controller = controller;
    }

    /**
     * Restituisce il pannello dei riferimenti.
     * 
     * @return pannello dei riferimenti
     */
    public ReferencePanel getReferencePanel() {
        return referencePanel;
    }

    /**
     * Restituisce il pannello delle categorie.
     * 
     * @return pannello delle categorie
     */
    public CategoriesPanel getCategoriesPanel() {
        return categoriesPanel;
    }

    /**
     * Restituisce il pannello della ricerca.
     * 
     * @return pannello della ricerca
     */
    public SearchPanel getReferenceSearchPanel() {
        return referenceSearchPanel;
    }

    /**
     * Restituisce il pannello delle informazioni utente
     * 
     * @return pannello utente
     */
    public UserInfoPanel getUserInfoPanel() {
        return userInfoPanel;
    }

    private void reset() {
        getCategoriesPanel().setCategoryController(getController().getCategoryController());
        getReferencePanel().setReferenceController(getController().getReferenceController());
        getReferenceSearchPanel().setCategoriesController(getController().getCategoryController());
        getUserInfoPanel().setUser(getController().getUser());
    }

}
