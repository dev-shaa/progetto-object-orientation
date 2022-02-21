package GUI.References.Picker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.*;

import Criteria.ReferenceCriteriaCategory;
import Entities.Category;
import Entities.References.BibliographicReference;
import GUI.Categories.CategoriesTreePanel;
import GUI.Categories.CategorySelectionListener;
import GUI.References.ReferenceListPanel;
import GUI.References.ReferenceSelectionListener;
import GUI.Utilities.Tree.CustomTreeModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Finestra di dialogo per scegliere un riferimento.
 */
public class ReferencePicker extends JDialog implements CategorySelectionListener, ReferenceSelectionListener {

    private CategoriesTreePanel categoriesPanel;
    private ReferenceListPanel referencesPanel;
    private JButton confirmButton;

    private Collection<? extends BibliographicReference> references;
    private Collection<? extends BibliographicReference> referencesToExclude;

    private ArrayList<ReferencePickerListener> pickerListeners;

    /**
     * TODO: commenta
     */
    public ReferencePicker() {
        this(null, null);
    }

    /**
     * Crea una nuova finestra di dialogo in cui è possibile scegliere un riferimento.
     * 
     * @param categoriesTree
     *            albero delle categorie che è possibile selezionare
     * @param references
     *            tutti i riferimenti che è possibile selezionare
     */
    public ReferencePicker(CustomTreeModel<Category> categoriesTree, Collection<? extends BibliographicReference> references) {
        super();

        setTitle("Seleziona riferimento");
        setModal(true);
        setSize(500, 500);
        setResizable(false);

        setupLayout();

        setCategoriesTree(categoriesTree);
        setReferences(references);
    }

    private void setupLayout() {
        JPanel contentPane = new JPanel(new BorderLayout(10, 10));
        setContentPane(contentPane);

        categoriesPanel = new CategoriesTreePanel();
        categoriesPanel.addSelectionListener(this);

        referencesPanel = new ReferenceListPanel();
        referencesPanel.addReferenceSelectionListener(this);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, categoriesPanel, referencesPanel);
        splitPane.setResizeWeight(0.3);
        splitPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        contentPane.add(splitPane, BorderLayout.CENTER);

        confirmButton = new JButton("Conferma");
        confirmButton.setEnabled(false);
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                notifyListeners();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 50));

        buttonPanel.add(confirmButton);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);
    }

    @Override
    public void setVisible(boolean b) {
        setVisible(b, null);
    }

    /**
     * Chiama la funzione {@link #setVisible(boolean)} e imposta i riferimenti da escludere quando viene selezionata una categoria.
     * 
     * @param b
     *            se {@code true}, viene mostrato il pannello
     * @param referencesToExclude
     *            riferimenti da escludere
     */
    public void setVisible(boolean b, Collection<? extends BibliographicReference> referencesToExclude) {
        if (b) {
            setReferencesToExclude(referencesToExclude);
            categoriesPanel.clearSelection();
            referencesPanel.clear();
            setLocationRelativeTo(null);
        }

        super.setVisible(b);
    }

    /**
     * Imposta l'albero delle categorie che è possibile selezionare.
     * 
     * @param categoriesTree
     *            albero delle categorie
     */
    public void setCategoriesTree(CustomTreeModel<Category> categoriesTree) {
        categoriesPanel.setTreeModel(categoriesTree);
    }

    /**
     * Imposta i riferimenti che è possibile selezionare.
     * 
     * @param references
     *            riferimenti selezionabili
     */
    public void setReferences(Collection<? extends BibliographicReference> references) {
        this.references = references;
    }

    @Override
    public void onReferenceSelection(BibliographicReference reference) {
        confirmButton.setEnabled(reference != null);
    }

    @Override
    public void onCategorySelection(Category category) {
        ReferenceCriteriaCategory categoryFilter = new ReferenceCriteriaCategory(category);
        List<? extends BibliographicReference> referencesToShow = categoryFilter.get(references);

        if (referencesToShow != null && referencesToExclude != null)
            referencesToShow.removeAll(referencesToExclude);

        referencesPanel.setReferences(referencesToShow);
    }

    @Override
    public void onCategoryClearSelection() {
        referencesPanel.clear();
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

    private void setReferencesToExclude(Collection<? extends BibliographicReference> referencesToExclude) {
        this.referencesToExclude = referencesToExclude;
    }

    private void notifyListeners() {
        if (pickerListeners == null)
            return;

        for (ReferencePickerListener listener : pickerListeners)
            listener.onReferencePick(referencesPanel.getSelectedReference());

        setVisible(false);
    }

}
