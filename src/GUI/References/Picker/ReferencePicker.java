package GUI.References.Picker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.border.EmptyBorder;

import Controller.CategoryController;
import Controller.ReferenceController;
import Entities.Category;
import Entities.References.BibliographicReference;
import Exceptions.CategoryDatabaseException;
import Exceptions.ReferenceDatabaseException;
import GUI.Categories.CategoriesTreePanel;
import GUI.Categories.CategorySelectionListener;
import GUI.References.ReferenceListPanel;
import GUI.References.ReferenceSelectionListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Finestra di dialogo per scegliere un riferimento.
 */
public class ReferencePicker extends JDialog implements CategorySelectionListener, ReferenceSelectionListener {

    private CategoryController categoryController;
    private ReferenceController referenceController;

    private CategoriesTreePanel categoriesPanel;
    private ReferenceListPanel referencesPanel;
    private JButton confirmButton;

    private ArrayList<ReferencePickerListener> pickerListeners;
    private Collection<? extends BibliographicReference> referencesToExclude;

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
    public ReferencePicker(CategoryController categoryController, ReferenceController referenceController) {
        setCategoryController(categoryController);
        setReferenceController(referenceController);

        setTitle("Aggiungi riferimento");
        setModal(true);
        setSize(500, 500);
        setResizable(false);

        JPanel contentPane = new JPanel(new BorderLayout(10, 10));
        setContentPane(contentPane);

        categoriesPanel = new CategoriesTreePanel();
        categoriesPanel.addSelectionListener(this);

        referencesPanel = new ReferenceListPanel();
        referencesPanel.addReferenceSelectionListener(this);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, categoriesPanel, referencesPanel);
        splitPane.setResizeWeight(0.3);
        splitPane.setBorder(new EmptyBorder(10, 10, 0, 10));

        contentPane.add(splitPane, BorderLayout.CENTER);

        confirmButton = new JButton("Conferma");
        confirmButton.setEnabled(false);
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (pickerListeners == null)
                    return;

                for (ReferencePickerListener listener : pickerListeners) {
                    setVisible(false);
                    listener.onReferencePick(referencesPanel.getSelectedReference());
                }
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(new EmptyBorder(0, 50, 0, 50));

        buttonPanel.add(confirmButton);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);
    }

    @Override
    public void setVisible(boolean b) {
        setVisible(b, null);
    }

    /**
     * Chiama la funzione {@code setVisible(b)} e imposta i riferimenti da escludere quando viene selezionata una categoria.
     * 
     * @param b
     *            se {@code true}, viene mostrato il pannello
     * @param referencesToExclude
     *            riferimenti da escludere
     */
    public void setVisible(boolean b, Collection<? extends BibliographicReference> referencesToExclude) {
        boolean showErrorMessage = false;
        String errorMessage = null;

        if (b) {
            this.referencesToExclude = referencesToExclude;

            try {
                categoriesPanel.setCategoriesTree(categoryController.getTree());
                categoriesPanel.clearSelection();

                referencesPanel.setReferences(null);
            } catch (CategoryDatabaseException e) {
                showErrorMessage = true;
                errorMessage = e.getMessage();
            }
        }

        super.setVisible(b);

        if (showErrorMessage) {
            JOptionPane.showMessageDialog(this, errorMessage, "Errore", JOptionPane.ERROR_MESSAGE);
        }
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
            throw new IllegalArgumentException("categoryController can't be null");

        this.categoryController = categoryController;
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
    public void onCategorySelection(Category selectedCategory) {
        try {
            List<BibliographicReference> referencesToShow = referenceController.get(selectedCategory);

            if (referencesToExclude != null)
                referencesToShow.removeAll(referencesToExclude);

            referencesPanel.setReferences(referencesToShow);
        } catch (ReferenceDatabaseException e) {
            referencesPanel.setReferences(null);
            JOptionPane.showMessageDialog(this, e.getMessage(), "Errore recupero riferimenti", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void onCategoryClearSelection() {
        referencesPanel.setReferences(null);
    }

    @Override
    public void onReferenceSelection(BibliographicReference reference) {
        confirmButton.setEnabled(reference != null);
    }

    /**
     * Aggiunge un listener all'evento di selezione di un riferimento.
     * Se {@code listener == null} o se è già registrato all'evento, non succede niente.
     * 
     * @param listener
     *            listener da aggiungere
     */
    public void addReferencePickerListener(ReferencePickerListener listener) {
        if (listener == null)
            return;

        if (pickerListeners == null)
            pickerListeners = new ArrayList<>(3);

        if (pickerListeners.contains(listener))
            return;

        pickerListeners.add(listener);
    }

    /**
     * Rimuove un listener dall'evento di selezione di un riferimento.
     * Se {@code listener == null}, non succede niente.
     * 
     * @param listener
     *            listener da rimuovere
     */
    public void removeReferencePickerListener(ReferencePickerListener listener) {
        if (listener != null && pickerListeners != null)
            pickerListeners.remove(listener);
    }

}
