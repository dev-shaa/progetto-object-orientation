package GUI.ReferenceEditor;

import DAO.BibliographicReferenceDAO;
import GUI.Categories.CategoriesTreeManager;
import Entities.References.OnlineResources.OnlineResource;
import Exceptions.RequiredFieldMissingException;
import javax.swing.*;

/**
 * Pannello di dialogo per la creazione o modifica di un riferimento a una risorsa online.
 */
public abstract class OnlineResourceEditor extends ReferenceEditor {

    private JTextField URL;

    /**
     * Crea un nuovo pannello di dialogo per la modifica di un riferimento a una risorsa online, inserendo i valori già presenti all'interno dei campi.
     * 
     * @param dialogueTitle
     *            titolo della schermata di dialogo
     * @param categoriesTree
     *            albero delle categorie in cui è possibile inserire il riferimento
     * @param referenceDAO
     *            classe DAO per salvare i riferimenti nel database
     * @param onlineResource
     *            risorsa online da modificare (se nullo, non verrà inserito alcun valore e si considera come se si stesse creando un nuovo riferimento)
     * @throws IllegalArgumentException
     *             se referenceDAO non è un valore valido
     * 
     * @see #setReferenceDAO(BibliographicReferenceDAO)
     */
    public OnlineResourceEditor(String dialogueTitle, CategoriesTreeManager categoriesTree, BibliographicReferenceDAO referenceDAO, OnlineResource onlineResource) throws IllegalArgumentException {
        super(dialogueTitle, categoriesTree, referenceDAO, onlineResource);

        if (onlineResource != null) {
            setURLValue(onlineResource.getURL());
        }
    }

    @Override
    protected void setup(CategoriesTreeManager categoriesTree) {
        super.setup(categoriesTree);

        URL = new JTextField();

        addFieldComponent(URL, "URL*", "URL della risorsa");
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

    /**
     * Riempie i campi della risorsa online passata con i valori inseriti dall'utente.
     * 
     * @param onlineResource
     *            risorsa online da riempire
     * @throws IllegalArgumentException
     *             se {@code onlineResource == null}
     * @throws RequiredFieldMissingException
     *             se i campi obbligatori non sono stati riempiti
     * @see #fillReferenceValues(Entities.References.BibliographicReference)
     */
    protected void fillOnlineResourceValues(OnlineResource onlineResource) throws IllegalArgumentException, RequiredFieldMissingException {
        super.fillReferenceValues(onlineResource);

        onlineResource.setURL(getURLValue());
    }

}
