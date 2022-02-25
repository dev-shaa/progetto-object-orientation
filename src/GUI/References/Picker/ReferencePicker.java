package GUI.References.Picker;

import java.util.Collection;
import java.util.List;

import javax.swing.*;
import javax.swing.event.EventListenerList;

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

    private Collection<? extends BibliographicReference> selectableReferences;
    private EventListenerList pickerListeners;

    /**
     * Crea una nuova finestra di dialogo.
     */
    public ReferencePicker() {
        super();

        pickerListeners = new EventListenerList();

        setTitle("Seleziona riferimento");
        setModal(true);
        setSize(500, 500);
        setResizable(false);

        JPanel contentPane = new JPanel(new BorderLayout(10, 10));
        setContentPane(contentPane);

        categoriesPanel = new CategoriesTreePanel();
        categoriesPanel.addCategorySelectionListener(this);

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
                firePickEvent();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 50));

        buttonPanel.add(confirmButton);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);
    }

    @Override
    public void setVisible(boolean b) {
        if (b) {
            categoriesPanel.clearSelection();
            referencesPanel.clear();
            setLocationRelativeTo(null);
            revalidate();
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
     * @param selectableReferences
     *            riferimenti selezionabili
     */
    public void setAvailableReferences(Collection<? extends BibliographicReference> selectableReferences) {
        this.selectableReferences = selectableReferences;
    }

    @Override
    public void onReferenceSelection(BibliographicReference reference) {
        confirmButton.setEnabled(reference != null);
    }

    @Override
    public void onCategorySelection(Category selectedCategory) {
        ReferenceCriteriaCategory categoryFilter = new ReferenceCriteriaCategory(selectedCategory);
        List<? extends BibliographicReference> referencesToShow = categoryFilter.filter(selectableReferences);

        referencesPanel.setReferences(referencesToShow);
    }

    @Override
    public void onCategoryDeselection() {
        referencesPanel.clear();
    }

    /**
     * Aggiunge un listener all'evento di selezione di un riferimento.
     * 
     * @param listener
     *            listener da aggiungere
     */
    public void addReferencePickerListener(ReferencePickerListener listener) {
        pickerListeners.add(ReferencePickerListener.class, listener);
    }

    /**
     * Rimuove un listener dall'evento di selezione di un riferimento.
     * 
     * @param listener
     *            listener da rimuovere
     */
    public void removeReferencePickerListener(ReferencePickerListener listener) {
        pickerListeners.remove(ReferencePickerListener.class, listener);
    }

    /**
     * Notifica gli ascoltatori dell'evento di selezione di un riferimento.
     */
    private void firePickEvent() {
        BibliographicReference selectedReference = referencesPanel.getSelectedReference();
        ReferencePickerListener[] listeners = pickerListeners.getListeners(ReferencePickerListener.class);

        for (ReferencePickerListener listener : listeners)
            listener.onReferencePick(selectedReference);

        setVisible(false);
    }

}