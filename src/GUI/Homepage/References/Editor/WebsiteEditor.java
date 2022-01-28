package GUI.Homepage.References.Editor;

import Entities.References.OnlineResources.Website;
import Exceptions.ReferenceDatabaseException;
import Exceptions.RequiredFieldMissingException;

import Controller.AuthorController;
import Controller.CategoryController;
import Controller.ReferenceController;

import javax.swing.JOptionPane;

/**
 * Pannello di dialogo per la creazione o modifica di un riferimento a un sito web.
 */
public class WebsiteEditor extends OnlineResourceEditor<Website> {

    private Website website;

    /**
     * TODO: commenta
     * 
     * @param categoryController
     * @param referenceController
     * @param authorController
     */
    public WebsiteEditor(CategoryController categoryController, ReferenceController referenceController, AuthorController authorController) {
        super("Sito web", categoryController, referenceController, authorController);
    }

    @Override
    protected void initialize() {
        super.initialize();
    }

    @Override
    protected void setFieldsValues(Website reference) {
        super.setFieldsValues(reference);
    }

    @Override
    protected void saveReference() {
        try {
            Website websiteToFill = website == null ? new Website("placeholder", "placeholder") : website;
            fillReferenceValues(websiteToFill);
            getReferenceController().saveReference(websiteToFill);
        } catch (RequiredFieldMissingException e) {
            JOptionPane.showMessageDialog(this, "Uno o più campi obbligatori non sono stati inseriti.", "Campi obbligatori mancanti", JOptionPane.ERROR_MESSAGE);
        } catch (ReferenceDatabaseException e) {
            JOptionPane.showMessageDialog(this, "Si è verificato un errore durante il salvataggio", "Errore database", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    protected void fillReferenceValues(Website reference) throws RequiredFieldMissingException {
        super.fillReferenceValues(reference);

        // qui non dobbiamo fare niente in realtà, però mettiamola per correttezza
    }

}
