package GUI.Homepage.References.ReferenceEditor;

import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.border.EmptyBorder;

import DAO.BibliographicReferenceDAO;
import Entities.Category;
import Entities.References.BibliographicReference;
import Exceptions.ReferenceDatabaseException;
import GUI.Homepage.Categories.CategoriesTreeManager;
import GUI.Homepage.Categories.CategorySelectionListener;
import GUI.Homepage.Categories.CategoryTreePanel;
import GUI.Homepage.References.ReferenceListPanel;
import GUI.Homepage.References.ReferenceSelectionListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Finestra di dialogo per scegliere un rimando da aggiungere a un riferimento.
 */
public class ReferenceChooserDialog extends JDialog implements CategorySelectionListener, ReferenceSelectionListener {

    private CategoryTreePanel categories;
    private ReferenceListPanel references;
    private JButton confirmButton;

    private ArrayList<ReferenceChooserSelectionListener> selectionListeners;

    private BibliographicReferenceDAO referenceDAO;

    /**
     * Crea una nuova finestra di dialogo con le categorie da cui è possibile scegliere i rimandi.
     * 
     * @param categoriesTree
     *            albero delle categorie da scegliere
     */
    public ReferenceChooserDialog(CategoriesTreeManager categoriesTree, BibliographicReferenceDAO referenceDAO) {
        setTitle("Aggiungi riferimento");
        setModal(true);
        setSize(500, 500);
        setResizable(false);

        setReferenceDAO(referenceDAO);

        JPanel contentPane = new JPanel(new BorderLayout(10, 10));
        setContentPane(contentPane);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(new EmptyBorder(0, 50, 0, 50));

        confirmButton = new JButton("Conferma");
        confirmButton.setEnabled(false);
        confirmButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectionListeners == null)
                    return;

                for (ReferenceChooserSelectionListener quotationSelectionListener : selectionListeners) {
                    setVisible(false);
                    quotationSelectionListener.onReferenceChooserSelection(references.getSelectedReference());
                }
            }

        });

        buttonPanel.add(confirmButton);

        categories = new CategoryTreePanel(categoriesTree);
        categories.addSelectionListener(this);

        references = new ReferenceListPanel();
        references.addReferenceSelectionListener(this);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, categories, references);
        splitPane.setResizeWeight(0.3);
        splitPane.setBorder(new EmptyBorder(10, 10, 0, 10));

        contentPane.add(splitPane, BorderLayout.CENTER);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);
    }

    @Override
    public void onCategorySelected(Category selectedCategory) {
        try {
            references.setReferences(referenceDAO.getReferences(selectedCategory));
        } catch (ReferenceDatabaseException e) {
            JOptionPane.showMessageDialog(this, "Si è verificato un errore durante il salvataggio", "Errore database", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void onReferenceSelection(BibliographicReference reference) {
        confirmButton.setEnabled(reference != null);
    }

    /**
     * Aggiunge un listener all'evento di selezione di un rimando.
     * 
     * @param listener
     *            listener da aggiungere
     */
    public void addQuotationSelectionListener(ReferenceChooserSelectionListener listener) {
        if (listener == null)
            return;

        if (selectionListeners == null)
            selectionListeners = new ArrayList<>(3);

        selectionListeners.add(listener);
    }

    /**
     * Rimuove un listener dall'evento di selezione di un rimando.
     * 
     * @param listener
     *            listener da rimuovere
     */
    public void removeQuotationSelectionListener(ReferenceChooserSelectionListener listener) {
        if (listener != null && selectionListeners != null)
            selectionListeners.remove(listener);
    }

    /**
     * Imposta la classe DAO per interfacciarsi al database.
     * 
     * @param referenceDAO
     *            classe DAO dei riferimenti
     * @throws IllegalArgumentException
     *             se {@code referenceDAO == null}
     */
    public void setReferenceDAO(BibliographicReferenceDAO referenceDAO) throws IllegalArgumentException {
        if (referenceDAO == null)
            throw new IllegalArgumentException("referenceDAO non può essere null");

        this.referenceDAO = referenceDAO;
    }

}
