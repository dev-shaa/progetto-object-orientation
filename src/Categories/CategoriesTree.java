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
    public DefaultMutableTreeNode addCategoryNode(DefaultMutableTreeNode parent, String name) throws IllegalArgumentException, CategoryDatabaseException {
        try {
            Category category = new Category(name, getCategoryFromNode(parent));

            categoryDAO.addCategory(category, user);

            DefaultMutableTreeNode newCategoryNode = new DefaultMutableTreeNode(category);
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
    public void changeCategoryNodeName(DefaultMutableTreeNode node, String newName) throws IllegalArgumentException, CategoryDatabaseException {
        try {
            if (!canNodeBeChanged(node))
                throw new IllegalArgumentException("Il nodo selezionato non può essere modificato.");

            Category category = getCategoryFromNode(node);
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
    public void removeCategoryNode(DefaultMutableTreeNode node) throws IllegalArgumentException, CategoryDatabaseException {
        try {
            if (!canNodeBeChanged(node))
                throw new IllegalArgumentException("Il nodo selezionato non può essere modificato.");

            categoryDAO.deleteCategory(getCategoryFromNode(node));
            TreeNode parent = node.getParent();
            node.removeFromParent();
            categoriesTreeModel.reload(parent);
        } catch (CategoryDatabaseException e) {
            throw e;
        }
    }

    /**
     * Restituisce la categoria associata a {@code node}.
     * 
     * @param node
     *            nodo dell'albero
     * @return la {@code Category} associata a {@code node}, {@code null} se non vi è associata una categoria
     * @throws IllegalArgumentException
     *             se {@code node == null}
     */
    public Category getCategoryFromNode(DefaultMutableTreeNode node) throws IllegalArgumentException {
        if (node == null)
            throw new IllegalArgumentException("Il nodo non può essere nullo.");

        return (Category) node.getUserObject();
    }

    /**
     * 
     * 
     * @param node
     * @return
     */
    public boolean canNodeBeChanged(DefaultMutableTreeNode node) {
        return node != null && !node.isRoot();
    }

}
