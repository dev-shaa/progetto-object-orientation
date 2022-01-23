package GUI.Homepage.References.Chooser;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.border.EmptyBorder;

import Entities.Category;
import Entities.References.BibliographicReference;
import GUI.Homepage.Categories.CategorySelectionListener;
import GUI.Homepage.Categories.CategoryTreeModel;
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

    private CategoryTreePanel categoriesPanel;
    private ReferenceListPanel referencesPanel;
    private JButton confirmButton;

    private List<BibliographicReference> references;
    private ArrayList<ReferenceChooserSelectionListener> selectionListeners;

    /**
     * Crea una nuova finestra di dialogo con le categorie da cui è possibile scegliere i rimandi.
     * 
     * @param categoriesTree
     *            albero delle categorie da scegliere
     * @throws IllegalArgumentException
     */
    public ReferenceChooserDialog(CategoryTreeModel categoriesTree, List<BibliographicReference> references) {
        setTitle("Aggiungi riferimento");
        setModal(true);
        setSize(500, 500);
        setResizable(false);

        this.references = references;

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
                    quotationSelectionListener.onReferenceChooserSelection(referencesPanel.getSelectedReference());
                }
            }

        });

        buttonPanel.add(confirmButton);

        categoriesPanel = new CategoryTreePanel(categoriesTree);
        categoriesPanel.addSelectionListener(this);

        referencesPanel = new ReferenceListPanel();
        referencesPanel.addReferenceSelectionListener(this);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, categoriesPanel, referencesPanel);
        splitPane.setResizeWeight(0.3);
        splitPane.setBorder(new EmptyBorder(10, 10, 0, 10));

        contentPane.add(splitPane, BorderLayout.CENTER);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);
    }

    @Override
    public void onCategorySelected(Category selectedCategory) {
        if (references == null)
            return;

        List<BibliographicReference> referencesInCategory = references.stream().filter(e -> e.getCategories().contains(selectedCategory)).toList();
        referencesPanel.setReferences(referencesInCategory.toArray(new BibliographicReference[referencesInCategory.size()]));
    }

    @Override
    public void onReferenceSelection(BibliographicReference reference) {
        confirmButton.setEnabled(reference != null);
    }

    /**
     * Aggiunge un listener all'evento di selezione di un riferimento.
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
     * 
     * @param listener
     *            listener da rimuovere
     */
    public void removeReferenceChooserSelectionListener(ReferenceChooserSelectionListener listener) {
        if (listener != null && selectionListeners != null)
            selectionListeners.remove(listener);
    }

}
