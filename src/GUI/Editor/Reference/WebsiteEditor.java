package GUI.Editor.Reference;

import Entities.References.OnlineResources.Website;
import Exceptions.ReferenceDatabaseException;
import Exceptions.RequiredFieldMissingException;

import Controller.AuthorController;
import Controller.CategoryController;
import Controller.ReferenceController;

import java.awt.Frame;

import javax.swing.JOptionPane;

/**
 * Finestra di dialogo per la creazione o modifica di un riferimento a un sito web.
 */
public class WebsiteEditor extends OnlineResourceEditor<Website> {

    /**
     * Crea una nuova finestra di dialogo per la creazione o modifica di codice sorgente.
     * 
     * @param owner
     *            proprietario di questa finestra di dialogo
     * @param categoryController
     *            controller delle categorie
     * @param referenceController
     *            controller dei riferimenti
     * @param authorController
     *            controller degli autori
     * @throws IllegalArgumentException
     *             se {@code categoryController == null}, {@code referenceController == null} o {@code authorController == null}
     */
    public WebsiteEditor(Frame owner, CategoryController categoryController, ReferenceController referenceController, AuthorController authorController) {
        super(owner, "Sito web", categoryController, referenceController, authorController);
    }

    @Override
    protected void initializeFields() {
        super.initializeFields();
    }

    @Override
    protected void setFieldsValues(Website reference) {
        super.setFieldsValues(reference);
    }

    @Override
    protected void saveReference() {
        try {
            Website websiteToSave = getOpenReference() == null ? new Website("temp", "temp") : getOpenReference();
            fillReferenceValues(websiteToSave);
            getReferenceController().saveReference(websiteToSave);
        } catch (RequiredFieldMissingException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Campi obbligatori mancanti", JOptionPane.ERROR_MESSAGE);
        } catch (ReferenceDatabaseException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Errore database", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    protected void fillReferenceValues(Website reference) throws IllegalArgumentException, RequiredFieldMissingException {
        super.fillReferenceValues(reference);
    }

}
