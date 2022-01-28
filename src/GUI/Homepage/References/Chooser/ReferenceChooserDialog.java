package GUI.Homepage.References.Chooser;

import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.border.EmptyBorder;

import Controller.CategoryController;
import Controller.ReferenceController;
import Entities.Category;
import Entities.References.BibliographicReference;
import GUI.Homepage.Categories.CategorySelectionListener;
import GUI.Homepage.Categories.CategoryTreePanel;
import GUI.Homepage.References.ReferenceListPanel;
import GUI.Homepage.References.ReferenceListSelectionListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Finestra di dialogo per scegliere un riferimento.
 */
public class ReferenceChooserDialog extends JDialog implements CategorySelectionListener, ReferenceListSelectionListener {

    private ReferenceController referenceController;

    private CategoryTreePanel categoriesPanel;
    private ReferenceListPanel referencesPanel;
    private JButton confirmButton;

    private ArrayList<ReferenceChooserSelectionListener> selectionListeners;

    /**
     * Crea una nuova finestra di dialogo in cui è possibile scegliere un riferimento.
     * 
     * @param categoryController
     *            controller delle categorie
     * @param referenceController
     *            controller dei riferimenti
     * @throws IllegalArgumentException
     *             se {@code categoryManager} o {@code referenceManager} sono nulli.
     */
    public ReferenceChooserDialog(CategoryController categoryController, ReferenceController referenceController) {
        setCategoryController(categoryController);
        setReferenceController(referenceController);

        setTitle("Aggiungi riferimento");
        setModal(true);
        setSize(500, 500);
        setResizable(false);

        JPanel contentPane = new JPanel(new BorderLayout(10, 10));
        setContentPane(contentPane);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, categoriesPanel, referencesPanel);
        splitPane.setResizeWeight(0.3);
        splitPane.setBorder(new EmptyBorder(10, 10, 0, 10));

        contentPane.add(splitPane, BorderLayout.CENTER);

        confirmButton = new JButton("Conferma");
        confirmButton.setEnabled(false);
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectionListeners == null)
                    return;

                for (ReferenceChooserSelectionListener listener : selectionListeners) {
                    setVisible(false);
                    listener.onReferenceChooserSelection(referencesPanel.getSelectedReference());
                }
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(new EmptyBorder(0, 50, 0, 50));

        buttonPanel.add(confirmButton);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Imposta il controller delle categorie.
     * Reimposta i pannelli delle categorie e dei riferimenti.
     * 
     * @param categoryController
     *            controller delle categorie
     * @throws IllegalArgumentException
     *             se {@code categoryController == null}
     */
    public void setCategoryController(CategoryController categoryController) {
        if (categoryController == null)
            throw new IllegalArgumentException("categoryManager can't be null");

        initializeCategoriesPanel(categoryController);
        initializeReferencesPanel();
    }

    /**
     * Imposta il controller dei riferimenti.
     * 
     * @param referenceController
     *            controller dei riferimenti
     * @throws IllegalArgumentException
     *             se {@code referenceController == null}
     */
    public void setReferenceController(ReferenceController referenceController) {
        if (referenceController == null)
            throw new IllegalArgumentException("referenceController can't be null");

        this.referenceController = referenceController;
    }

    @Override
    public void onCategorySelected(Category selectedCategory) {
        referencesPanel.setReferences(referenceController.getReferences(selectedCategory));
    }

    @Override
    public void onReferenceSelection(BibliographicReference reference) {
        confirmButton.setEnabled(reference != null);
    }

    /**
     * Aggiunge un listener all'evento di selezione di un riferimento.
     * Se {@code listener == null}, non succede niente.
     * 
     * @param listener
     *            listener da aggiungere
     */
    public void addReferenceChooserSelectionListener(ReferenceChooserSelectionListener listener) {
        if (listener == null)
            return;

        if (selectionListeners == null)
            selectionListeners = new ArrayList<>(3);

        selectionListeners.add(listener);
    }

    /**
     * Rimuove un listener dall'evento di selezione di un riferimento.
     * Se {@code listener == null}, non succede niente.
     * 
     * @param listener
     *            listener da rimuovere
     */
    public void removeReferenceChooserSelectionListener(ReferenceChooserSelectionListener listener) {
        if (listener != null && selectionListeners != null)
            selectionListeners.remove(listener);
    }

    private void initializeCategoriesPanel(CategoryController categoryController) {
        if (categoriesPanel == null) {
            categoriesPanel = new CategoryTreePanel(categoryController.getCategoriesTree());
            categoriesPanel.addSelectionListener(this);
        } else {
            categoriesPanel.setCategoriesTree(categoryController.getCategoriesTree());
        }
    }

    private void initializeReferencesPanel() {
        if (referencesPanel == null) {
            referencesPanel = new ReferenceListPanel();
            referencesPanel.addReferenceSelectionListener(this);
        } else {
            referencesPanel.setReferences(null);
        }
    }

}
