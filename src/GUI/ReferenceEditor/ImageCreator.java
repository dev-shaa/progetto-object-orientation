package GUI.ReferenceEditor;

import java.awt.event.ActionEvent;
import javax.swing.JSpinner;
import GUI.Categories.CategoriesTreeManager;

/**
 * Pannello di dialogo per la creazione di un nuovo riferimento a un'immagine.
 */
public class ImageCreator extends OnlineResourceCreator {

    private JSpinner width;
    private JSpinner height;

    /**
     * Crea un nuovo pannello di dialogo per la creazione di un riferimento a un'immagine.
     * 
     * @param categoriesTreeManager
     *            TODO: commenta
     */
    public ImageCreator(CategoriesTreeManager categoriesTreeManager) {
        this("Immagine", categoriesTreeManager);
    }

    private ImageCreator(String dialogueTitle, CategoriesTreeManager categoriesTreeManager) {
        super(dialogueTitle, categoriesTreeManager);
    }

    @Override
    protected void setup(CategoriesTreeManager categoriesTreeManager) {
        super.setup(categoriesTreeManager);

        width = new JSpinner();
        height = new JSpinner();

        addComponent("Larghezza", width);
        addComponent("Altezza", height);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO: Auto-generated method stub
    }

}
