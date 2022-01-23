package GUI.Homepage.References.ReferenceEditor;

import javax.swing.JOptionPane;

import DAO.BibliographicReferenceDAO;
import Entities.References.OnlineResources.Website;
import Exceptions.ReferenceDatabaseException;
import Exceptions.RequiredFieldMissingException;
import GUI.Homepage.Categories.CategoriesTreeManager;

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
        Website websiteToFill = website == null ? new Website("placeholder", "placeholder") : website;

        System.out.println("aaa");

        try {
            fillReferenceValues(websiteToFill);
            getReferenceDAO().saveWebsite(websiteToFill);
        } catch (RequiredFieldMissingException e) {
            JOptionPane.showMessageDialog(this, "Uno o più campi obbligatori non sono stati inseriti.", "Campi obbligatori mancanti", JOptionPane.ERROR_MESSAGE);
        } catch (ReferenceDatabaseException e) {
            JOptionPane.showMessageDialog(this, "Si è verificato un errore durante il salvataggio", "Errore database", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    protected void fillReferenceValues(Website reference) throws IllegalArgumentException, RequiredFieldMissingException {
        super.fillReferenceValues(reference);

        // qui non dobbiamo fare niente in realtà, però mettiamola per correttezza
    }

}
