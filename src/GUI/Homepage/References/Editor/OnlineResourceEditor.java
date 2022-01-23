package GUI.Homepage.References.Editor;

import DAO.BibliographicReferenceDAO;
import Entities.References.OnlineResources.OnlineResource;
import Exceptions.RequiredFieldMissingException;
import GUI.Homepage.Categories.CategoriesTreeManager;

import javax.swing.*;

/**
 * Pannello di dialogo per la creazione o modifica di un riferimento a una risorsa online.
 */
public abstract class OnlineResourceEditor<T extends OnlineResource> extends ReferenceEditorDialog<T> {

    private JTextField URL;

    /**
     * Crea un nuovo pannello di dialogo per la modifica di un riferimento a una risorsa online.
     * 
     * @param dialogueTitle
     *            titolo della schermata di dialogo
     * @param categoriesTree
     *            albero delle categorie in cui è possibile inserire il riferimento
     * @param referenceDAO
     *            classe DAO per salvare i riferimenti nel database
     * @throws IllegalArgumentException
     *             se referenceDAO non è un valore valido
     * 
     * @see #setReferenceDAO(BibliographicReferenceDAO)
     */
    public OnlineResourceEditor(String dialogueTitle, CategoriesTreeManager categoriesTree, BibliographicReferenceDAO referenceDAO) throws IllegalArgumentException {
        super(dialogueTitle, categoriesTree, referenceDAO);
    }

    @Override
    protected void setup(CategoriesTreeManager categoriesTree) {
        super.setup(categoriesTree);

        URL = new JTextField();

        addFieldComponent(URL, "URL*", "URL della risorsa");
    }

    @Override
    protected void resetFields(T reference) {
        super.resetFields(reference);

        setURLValue(reference == null ? null : reference.getURL());
    }

    @Override
    protected void fillReferenceValues(T reference) throws IllegalArgumentException, RequiredFieldMissingException {
        super.fillReferenceValues(reference);

        reference.setURL(getURLValue());
    }

    /**
     * Imposta il valore iniziale dell'URL.
     * 
     * @param URL
     *            URL iniziale del riferimento
     */
    protected void setURLValue(String URL) {
        this.URL.setText(URL);
    }

    /**
     * Restituisce l'URL inserito dall'utente.
     * 
     * @return
     *         URL del riferimento
     * @throws RequiredFieldMissingException
     *             se non è stato inserito un URL
     */
    protected String getURLValue() throws RequiredFieldMissingException {
        String newURL = convertEmptyStringToNull(URL.getText().trim());

        if (newURL == null)
            throw new RequiredFieldMissingException("L'URL di una risorsa online non può essere nullo.");

        return newURL;
    }

}
