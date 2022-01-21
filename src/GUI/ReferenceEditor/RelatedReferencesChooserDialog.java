package GUI.ReferenceEditor;

import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.border.EmptyBorder;

import Entities.Category;
import Entities.References.BibliographicReference;
import Entities.References.OnlineResources.SourceCode;
import Entities.References.PhysicalResources.Article;
import GUI.Categories.CategoriesTreeManager;
import GUI.Categories.CategorySelectionListener;
import GUI.Categories.CategoryTreePanel;
import GUI.Homepage.References.ReferenceListPanel;
import GUI.Homepage.References.ReferenceSelectionListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Finestra di dialogo per scegliere un rimando da aggiungere a un riferimento.
 */
public class RelatedReferencesChooserDialog extends JDialog implements CategorySelectionListener, ReferenceSelectionListener {

    private CategoryTreePanel categories;
    private ReferenceListPanel references;
    private JButton confirmButton;

    private ArrayList<RelatedReferencesSelectionListener> selectionListeners;

    /**
     * Crea una nuova finestra di dialogo con le categorie da cui è possibile scegliere i rimandi.
     * 
     * @param categoriesTree
     *            albero delle categorie da scegliere
     */
    public RelatedReferencesChooserDialog(CategoriesTreeManager categoriesTree) {
        setTitle("Aggiungi rimando");
        setModal(true);
        setSize(500, 500);
        setResizable(false);

        JPanel contentPane = new JPanel(new BorderLayout(10, 10));
        setContentPane(contentPane);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(new EmptyBorder(0, 50, 0, 50));

        confirmButton = new JButton("Conferma");
        confirmButton.setEnabled(false);
        confirmButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                for (RelatedReferencesSelectionListener quotationSelectionListener : selectionListeners) {
                    setVisible(false);
                    quotationSelectionListener.onRelatedReferenceSelection(references.getSelectedReference());
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
        // TODO: mostra riferimenti
        references.removeAllReferences();
        references.addReference(new Article("articolo lunghissimo di prova 1", null));
        references.addReference(new SourceCode("source", null, "url"));
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
    public void addQuotationSelectionListener(RelatedReferencesSelectionListener listener) {
        if (listener != null) {
            if (selectionListeners == null)
                selectionListeners = new ArrayList<>(3);

            selectionListeners.add(listener);
        }
    }

    /**
     * Rimuove un listener dall'evento di selezione di un rimando.
     * 
     * @param listener
     *            listener da rimuovere
     */
    public void removeQuotationSelectionListener(RelatedReferencesSelectionListener listener) {
        if (listener != null && selectionListeners != null)
            selectionListeners.remove(listener);
    }

}
