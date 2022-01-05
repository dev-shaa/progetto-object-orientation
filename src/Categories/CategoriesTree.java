import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

// TODO: commenta

public class CategoriesTree {

    private User user;
    private CategoryDAO categoryDAO;
    private DefaultTreeModel categoriesTreeModel;

    /**
     * 
     * 
     * @param user
     * @throws Exception
     */
    public CategoriesTree(User user) throws CategoryDatabaseException {
        this.user = user;
        categoryDAO = new CategoryDAOPostgreSQL();

        try {
            categoriesTreeModel = new DefaultTreeModel(categoryDAO.getUserCategories(user));
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Restituisce l'albero delle categorie.
     * 
     * @return l'albero delle categorie
     */
    public DefaultTreeModel getTree() {
        return this.categoriesTreeModel;
    }

    /**
     * Aggiunge un nuovo nodo categoria all'albero come figlio del nodo {@code parent} e lo salva nel database.
     * 
     * @param parent
     *            il nodo parente della nuova categoria
     * @param name
     *            il nome della nuova categoria
     * @return il nodo creato
     * @throws IllegalArgumentException
     *             se {@code name == null } o {@code name.isEmpty()}
     * @throws Exception
     */
    public CategoryMutableTreeNode addCategoryNode(CategoryMutableTreeNode parent, String name) throws IllegalArgumentException, CategoryDatabaseException {
        try {
            Category category = new Category(name, parent.getCategory());

            categoryDAO.addCategory(category, user);

            CategoryMutableTreeNode newCategoryNode = new CategoryMutableTreeNode(category);
            categoriesTreeModel.insertNodeInto(newCategoryNode, parent, parent.getChildCount());

            return newCategoryNode;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Modifica il nome della categoria associata a un nodo
     * 
     * @param node
     *            il nodo di cui modificare la categoria
     * @param newName
     *            il nuovo nome della categoria
     * @throws IllegalArgumentException
     * 
     * @throws Exception
     */
    public void changeCategoryNodeName(CategoryMutableTreeNode node, String newName) throws IllegalArgumentException, CategoryDatabaseException {
        try {
            if (!canNodeBeChanged(node))
                throw new IllegalArgumentException("Il nodo selezionato non può essere modificato.");

            Category category = node.getCategory();
            categoryDAO.updateCategory(category, newName);
            category.setName(newName);
            categoriesTreeModel.reload(node);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Rimuove il nodo categoria indicato dall'albero e dal database.
     * 
     * @param node
     *            il nodo da rimuovere
     * @throws IllegalArgumentException
     *             se {@code node} non può essere eliminato (è nullo o è il nodo radice)
     * @throws Exception
     */
    public void removeCategoryNode(CategoryMutableTreeNode node) throws IllegalArgumentException, CategoryDatabaseException {
        try {
            if (!canNodeBeChanged(node))
                throw new IllegalArgumentException("Il nodo selezionato non può essere modificato.");

            categoryDAO.deleteCategory(node.getCategory());
            CategoryMutableTreeNode parent = node.getParent();
            node.removeFromParent();
            categoriesTreeModel.reload(parent);
        } catch (CategoryDatabaseException e) {
            throw e;
        }
    }

    /**
     * 
     * 
     * @param node
     * @return
     */
    public boolean canNodeBeChanged(CategoryMutableTreeNode node) {
        return node != null && !node.isRoot();
    }

}
