package GUI.ReferenceEditor;

import GUI.Categories.*;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import DAO.BibliographicReferenceDAO;

public abstract class PublicationCreator extends ReferenceCreator {

    private JSpinner pageCount;
    private JTextField url;
    private JTextField publisher;

    /**
     * Crea {@code PublicationCreator} con il titolo, le categorie e il DAO indicati.
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
    public PublicationCreator(String dialogueTitle, CategoriesTreeManager categoriesTreeManager, BibliographicReferenceDAO referenceDAO) throws IllegalArgumentException {
        super(dialogueTitle, categoriesTreeManager, referenceDAO);
    }

    @Override
    protected void setup(CategoriesTreeManager categoriesTreeManager) {
        super.setup(categoriesTreeManager);

        pageCount = new JSpinner();
        url = new JTextField();
        publisher = new JTextField();

        addComponent("Pagine", pageCount);
        addComponent("URL", url);
        addComponent("Editore", publisher);
    }

    // TODO: page count

    /**
     * Restituisce l'URL inserito dall'utente.
     * 
     * @return
     *         URL di input
     */
    protected String getURL() {
        return url.getText().trim();
    }

    /**
     * Restituisce l'editore inserito dall'utente.
     * 
     * @return
     *         editore di input
     */
    protected String getPublisher() {
        return publisher.getText().trim();
    }

}
