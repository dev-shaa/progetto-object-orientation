package GUI.ReferenceEditor;

import GUI.Categories.CategoriesTreeManager;
import DAO.BibliographicReferenceDAO;
import javax.swing.*;

/**
 * TODO: commenta
 */
public abstract class OnlineResourceCreator extends ReferenceCreator {

    private JTextField url;

    /**
     * Crea {@code OnlineResourceCreator} con il titolo, le categorie e il DAO indicati.
     * 
     * @param dialogueTitle
     *            titolo della finestra di dialogo
     * @param categoriesTreeManager
     *            manager dell'albero delle categorie
     * @param referenceDAO
     *            classe DAO dei riferimenti
     * @throws IllegalArgumentException
     *             se referenceDAO non è valido
     * @see #setReferenceDAO(BibliographicReferenceDAO)
     */
    public OnlineResourceCreator(String dialogueTitle, CategoriesTreeManager categoriesTreeManager, BibliographicReferenceDAO referenceDAO) throws IllegalArgumentException {
        super(dialogueTitle, categoriesTreeManager, referenceDAO);
    }

    @Override
    protected void setup(CategoriesTreeManager categoriesTreeManager) {
        super.setup(categoriesTreeManager);

        url = new JTextField();

        addComponent("URL", url);
    }
}
