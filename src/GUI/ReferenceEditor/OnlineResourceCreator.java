package GUI.ReferenceEditor;

import GUI.Categories.*;
import javax.swing.*;

public abstract class OnlineResourceCreator extends ReferenceCreator {

    private JTextField url;

    public OnlineResourceCreator(String dialogueTitle, CategoriesTreeManager categoriesTreeManager) {
        super(dialogueTitle, categoriesTreeManager);
    }

    @Override
    protected void setup(CategoriesTreeManager categoriesTreeManager) {
        super.setup(categoriesTreeManager);

        url = new JTextField();

        addComponent("URL", url);
    }
}
