package GUI.Homepage.Categories;

import Entities.Category;
import GUI.Utilities.CustomTreeNode;

public class CategoryTreeNode extends CustomTreeNode<Category> {

    public CategoryTreeNode(Category userObject, CustomTreeNode<Category> parent) {
        super(userObject, parent);
    }

    public CategoryTreeNode(Category userObject, CustomTreeNode<Category> parent, boolean allowsChildren) {
        super(userObject, parent, allowsChildren);
    }

    public CategoryTreeNode(Category userObject) {
        super(userObject);
    }

}
