package GUI.ReferenceEditor;

import javax.swing.JOptionPane;

import DAO.BibliographicReferenceDAO;
import GUI.Categories.CategoriesTreeManager;
import Entities.References.OnlineResources.OnlineResource;
import Entities.References.OnlineResources.Website;
import Exceptions.RequiredFieldMissingException;

/**
 * Pannello di dialogo per la creazione o modifica di un riferimento a un sito web.
 */
public class WebsiteEditor extends OnlineResourceEditor<Website> {

    private Website website;

    /**
     * Crea un nuovo pannello di dialogo per la creazione di un riferimento a un sito web.
     * 
     * @param categoriesTree
     *            albero delle categorie in cui è possibile inserire il riferimento
     * @param referenceDAO
     *            classe DAO per salvare i riferimenti nel database
     * @throws IllegalArgumentException
     *             se referenceDAO non è un valore valido
     * 
     * @see #setReferenceDAO(BibliographicReferenceDAO)
     */
    public WebsiteEditor(CategoriesTreeManager categoriesTree, BibliographicReferenceDAO referenceDAO) throws IllegalArgumentException {
        super("Sito web", categoriesTree, referenceDAO);
    }

    @Override
    protected void setup(CategoriesTreeManager categoriesTree) {
        super.setup(categoriesTree);
    }

    @Override
    protected void resetFields(Website reference) {
        super.resetFields(reference);
    }

    @Override
    protected void saveReference() {
        Website websiteToFill = website == null ? new Website("placeholder", null, "placeholder") : website;

        System.out.println("aaa");

        try {
            fillWebsiteValues(websiteToFill);
            // TODO: salva nel database
        } catch (IllegalArgumentException e) {
            // TODO: handle
        } catch (RequiredFieldMissingException e) {
            JOptionPane.showMessageDialog(this, "Uno o più campi obbligatori non sono stati inseriti.", "Campi obbligatori mancanti", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Riempie i campi del sito web passato con i valori inseriti dall'utente.
     * 
     * @param website
     *            sito web da riempire
     * @throws IllegalArgumentException
     *             se {@code website == null}
     * @throws RequiredFieldMissingException
     *             se i campi obbligatori non sono stati riempiti
     * @see #fillOnlineResourceValues(OnlineResource)
     */
    protected void fillWebsiteValues(Website website) throws IllegalArgumentException, RequiredFieldMissingException {
        super.fillOnlineResourceValues(website);

        // qui non dobbiamo fare niente in realtà, però mettiamola per correttezza
    }

}
