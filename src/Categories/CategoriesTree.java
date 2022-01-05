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
     * @param user
     * @throws Exception
     */
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
            Category category = new Category(name, getCategoryFromNode(parent));

            categoryDAO.addCategory(category, user);

            DefaultMutableTreeNode newCategoryNode = new DefaultMutableTreeNode(category);
            categoriesTreeModel.insertNodeInto(newCategoryNode, parent, parent.getChildCount());

            return newCategoryNode;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new Exception("Impossibile creare una nuova categoria.");
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
     * @throws Exception
     */
    public void changeCategoryNodeName(DefaultMutableTreeNode node, String newName) throws IllegalArgumentException, Exception {
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
            throw new Exception("Impossibile modificare questa categoria.");
        }
    }

    /**
     * Rimuove il nodo categoria indicato dall'albero e dal database.
     * 
     * @param node
     *            il nodo da rimuovere
     * @throws Exception
     */
    public void removeCategoryNode(DefaultMutableTreeNode node) throws IllegalArgumentException, Exception {
        try {
            if (!canNodeBeChanged(node))
                throw new IllegalArgumentException("Il nodo selezionato non può essere modificato.");

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

    private boolean canNodeBeChanged(DefaultMutableTreeNode node) {
        return node != null && !node.isRoot();
    }

}
