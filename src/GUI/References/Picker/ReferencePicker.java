package GUI.References.Picker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.*;

import Entities.Category;
import Entities.References.BibliographicReference;
import Exceptions.Database.CategoryDatabaseException;
import Exceptions.Database.ReferenceDatabaseException;
import GUI.Categories.CategoriesTreePanel;
import GUI.Categories.CategorySelectionListener;
import GUI.References.ReferenceListPanel;
import GUI.References.ReferenceSelectionListener;
import Repository.CategoryRepository;
import Repository.ReferenceRepository;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Finestra di dialogo per scegliere un riferimento.
 */
public class ReferencePicker extends JDialog implements CategorySelectionListener, ReferenceSelectionListener {

    private CategoryRepository categoryRepository;
    private ReferenceRepository referenceRepository;

    private CategoriesTreePanel categoriesPanel;
    private ReferenceListPanel referencesPanel;
    private JButton confirmButton;

    private ArrayList<ReferencePickerListener> pickerListeners;
    private Collection<? extends BibliographicReference> referencesToExclude;

    /**
     * Crea una nuova finestra di dialogo in cui è possibile scegliere un riferimento.
     * 
     * @param owner
     *            proprietario della finestra
     * @param categoryRepository
     *            controller delle categorie
     * @param referenceRepository
     *            controller dei riferimenti
     * @throws IllegalArgumentException
     *             se {@code categoryManager == null} o {@code referenceManager == null}
     */
    public ReferencePicker(Dialog owner, CategoryRepository categoryRepository, ReferenceRepository referenceRepository) {
        super(owner);

        setCategoryRepository(categoryRepository);
        setReferenceRepository(referenceRepository);

        setTitle("Seleziona riferimento");
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
        String errorMessage = null;

        if (b) {
            this.referencesToExclude = referencesToExclude;
            referencesPanel.clear();

            try {
                categoriesPanel.setTreeModel(categoryRepository.getTree());
            } catch (CategoryDatabaseException e) {
                categoriesPanel.setTreeModel(null);
                errorMessage = e.getMessage();
            }

            setLocationRelativeTo(null);
        }

        super.setVisible(b);

        if (errorMessage != null)
            JOptionPane.showMessageDialog(this, errorMessage, "Errore", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Imposta il controller delle categorie.
     * Reimposta i pannelli delle categorie e dei riferimenti.
     * 
     * @param categoryRepository
     *            controller delle categorie
     * @throws IllegalArgumentException
     *             se {@code categoryController == null}
     */
    public void setCategoryRepository(CategoryRepository categoryRepository) {
        if (categoryRepository == null)
            throw new IllegalArgumentException("categoryRepository can't be null");

        this.categoryRepository = categoryRepository;
    }

    /**
     * Imposta il controller dei riferimenti.
     * 
     * @param referenceRepository
     *            controller dei riferimenti
     * @throws IllegalArgumentException
     *             se {@code referenceController == null}
     */
    public void setReferenceRepository(ReferenceRepository referenceRepository) {
        if (referenceRepository == null)
            throw new IllegalArgumentException("referenceRepository can't be null");

        this.referenceRepository = referenceRepository;
    }

    @Override
    public void onReferenceSelection(BibliographicReference reference) {
        confirmButton.setEnabled(reference != null);
    }

    @Override
    public void onCategorySelection(Category category) {
        try {
            List<BibliographicReference> referencesToShow = referenceRepository.get(category);

            if (referencesToExclude != null)
                referencesToShow.removeAll(referencesToExclude);

            referencesPanel.setReferences(referencesToShow);
        } catch (ReferenceDatabaseException e) {
            referencesPanel.clear();
            JOptionPane.showMessageDialog(this, e.getMessage(), "Errore recupero riferimenti", JOptionPane.ERROR_MESSAGE);
        }
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

    private void notifyListeners() {
        if (pickerListeners == null)
            return;

        for (ReferencePickerListener listener : pickerListeners)
            listener.onReferencePick(referencesPanel.getSelectedReference());

        setVisible(false);
    }

}
