import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Wrapper di DefaultMutableTreeNode che accetta Category come unico oggetto.
 */
public class CategoryMutableTreeNode extends DefaultMutableTreeNode {

    public CategoryMutableTreeNode() {
    }

    public CategoryMutableTreeNode(Category category) {
        super(category);
    }

    public CategoryMutableTreeNode(Category category, boolean allowsChildren) {
        super(category, allowsChildren);
    }

    public void add(CategoryMutableTreeNode newChild) {
        super.add(newChild);
    }

    public Category getCategory() {
        return (Category) userObject;
    }

    public CategoryMutableTreeNode getParent() {
        return (CategoryMutableTreeNode) super.getParent();
    }

}
