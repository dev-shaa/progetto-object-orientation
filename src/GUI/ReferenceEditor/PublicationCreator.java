package GUI.ReferenceEditor;

import GUI.Categories.*;
import javax.swing.JSpinner;
import javax.swing.JTextField;

public abstract class PublicationCreator extends ReferenceCreator {

    private JSpinner pageCount;
    private JTextField url;
    private JTextField publisher;

    public PublicationCreator(String dialogueTitle, CategoriesTreeManager categoriesTreeManager) {
        super(dialogueTitle, categoriesTreeManager);
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

    protected String getURL() {
        return url.getText().trim();
    }

    protected String getPublisher() {
        return publisher.getText().trim();
    }

}
