package GUI.ReferenceEditor;

import java.awt.event.ActionEvent;

import javax.swing.JSpinner;

import DAO.BibliographicReferenceDAO;
import GUI.Categories.CategoriesTreeManager;

/**
 * Pannello di dialogo per la creazione di un nuovo riferimento video.
 */
public class VideoCreator extends OnlineResourceCreator {

    private JSpinner width;
    private JSpinner height;
    private JSpinner frameRate;
    // TODO: durata

    public VideoCreator(CategoriesTreeManager categoriesTreeManager, BibliographicReferenceDAO referenceDAO) {
        this("Video", categoriesTreeManager, referenceDAO);
    }

    private VideoCreator(String dialogueTitle, CategoriesTreeManager categoriesTreeManager, BibliographicReferenceDAO referenceDAO) {
        super(dialogueTitle, categoriesTreeManager, referenceDAO);
    }

    @Override
    protected void setup(CategoriesTreeManager categoriesTreeManager) {
        super.setup(categoriesTreeManager);

        width = new JSpinner();
        height = new JSpinner();
        frameRate = new JSpinner();

        addComponent("Larghezza", width);
        addComponent("Altezza", height);
        addComponent("Frequenza", frameRate);
    }

    @Override
    protected void onConfirmClick() {
        // TODO Auto-generated method stub

    }

}
