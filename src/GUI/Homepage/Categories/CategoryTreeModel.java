package GUI.Homepage.Categories;

import Entities.Category;
import GUI.Utilities.CustomTreeModel;
import GUI.Utilities.CustomTreeNode;

public class CategoryTreeModel extends CustomTreeModel<Category> {

    public CategoryTreeModel(CustomTreeNode<Category> root) {
        super(root);
    }

    public CategoryTreeModel(CustomTreeNode<Category> root, boolean asksAllowsChildren) {
        super(root, asksAllowsChildren);
    }

}
