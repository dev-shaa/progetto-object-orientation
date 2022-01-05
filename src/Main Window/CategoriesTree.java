import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

// TODO: commenta

public class CategoriesTree {

    private User user;
    private CategoryDAO categoryDAO;
    private DefaultTreeModel categoriesTreeModel;

    public CategoriesTree(User user) throws Exception {
        this.user = user;
        categoryDAO = new CategoryDAOPostgreSQL();

        try {
            categoriesTreeModel = new DefaultTreeModel(categoryDAO.getUserCategories(user));
        } catch (Exception e) {
            throw new Exception("Impossibile accedere alle categorie dell'utente");
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
     * Lancia un IllegalArgumentException se {@code name == null } o {@code name.isEmpty()}.
     * 
     * @param parent
     *            il nodo parente della nuova categoria
     * @param name
     *            il nome della nuova categoria
     * @return il nodo creato
     * @throws IllegalArgumentException
     * @throws Exception
     */
    public DefaultMutableTreeNode addCategoryNode(DefaultMutableTreeNode parent, String name) throws IllegalArgumentException, Exception {
        try {
            if (name == null || name.isEmpty())
                throw new IllegalArgumentException("Il nome della categoria non può essere nullo.");

            Category category = new Category(name, getCategoryFromNode(parent));

            categoryDAO.addCategory(category, user);

            DefaultMutableTreeNode newCategoryNode = new DefaultMutableTreeNode(category);
            categoriesTreeModel.insertNodeInto(newCategoryNode, parent, parent.getChildCount());

            return newCategoryNode;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 
     * @param node
     * @param newName
     * @throws IllegalArgumentException
     * @throws Exception
     */
    public void changeCategoryNode(DefaultMutableTreeNode node, String newName) throws IllegalArgumentException, Exception {
        try {
            if (node == null || node.isRoot())
                throw new IllegalArgumentException("Il nodo selezionato non può essere modificato.");

            if (newName == null || newName.isEmpty())
                throw new IllegalArgumentException("Il nome della categoria non può essere nullo.");

            Category category = getCategoryFromNode(node);
            categoryDAO.updateCategory(category, newName);
            category.setName(newName);
            categoriesTreeModel.reload(node);
        } catch (Exception e) {
            throw new Exception("Impossibile modificare questa categoria.");
        }
    }

    /**
     * Rimuove il nodo categoria indicato dall'albero e dal database.
     * 
     * @param node
     *            il nodo da rimuovere
     * @throws IllegalArgumentException
     * @throws Exception
     */
    public void removeCategoryNode(DefaultMutableTreeNode node) throws IllegalArgumentException, Exception {
        try {
            if (node == null || node.isRoot())
                categoryDAO.deleteCategory(getCategoryFromNode(node));

            TreeNode parent = node.getParent();
            node.removeFromParent();
            categoriesTreeModel.reload(parent);
        } catch (Exception e) {
            throw new Exception("Impossibile eliminare questa categoria.");
        }
    }

    /**
     * 
     * @param node
     *            nodo dell'albero
     * @return la {@code Category} associata al nodo
     */
    public Category getCategoryFromNode(DefaultMutableTreeNode node) {
        if (node == null)
            return null;

        return (Category) node.getUserObject();
    }

}
