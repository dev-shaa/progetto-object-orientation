import javax.swing.tree.DefaultTreeModel;

/**
 * Classe che gestisce l'albero delle categorie dell'utente,
 * con funzioni di inserimento, modifica e rimozione dal database.
 */
public class CategoriesTree {

    private User user;
    private CategoryDAO categoryDAO;
    private DefaultTreeModel categoriesTreeModel;

    /**
     * Crea {@code CategoriesTree} con i dati relativi all'utente.
     * 
     * @param categoryDAO
     *            classe DAO per recuperare dati dal database
     * @param user
     *            utente di cui recuperare le categorie
     * @throws IllegalArgumentException
     *             se {@code categoryDAO} o {@code user} sono nulli
     * @throws CategoryDatabaseException
     *             se il recupero delle categorie dal database non va a buon fine
     */
    public CategoriesTree(CategoryDAO categoryDAO, User user) throws IllegalArgumentException, CategoryDatabaseException {

        if (categoryDAO == null)
            throw new IllegalArgumentException("categoryDAO non può essere null.");

        if (user == null)
            throw new IllegalArgumentException("user non può essere null.");

        this.user = user;
        this.categoryDAO = categoryDAO;

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
     * @throws CategoryDatabaseException
     *             se la crezione della categoria nel database non va a buon fine
     */
    public CategoryMutableTreeNode addCategoryNode(CategoryMutableTreeNode parent, String name) throws IllegalArgumentException, CategoryDatabaseException {
        try {
            Category parentCategory = parent == null ? null : parent.getCategory();

            Category newCategory = new Category(name, parentCategory);

            categoryDAO.addCategory(newCategory, user);

            CategoryMutableTreeNode newCategoryNode = new CategoryMutableTreeNode(newCategory);
            categoriesTreeModel.insertNodeInto(newCategoryNode, parent, parent.getChildCount());

            return newCategoryNode;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (CategoryDatabaseException e) {
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
     *             se il nodo non può essere modificato
     * @throws CategoryDatabaseException
     *             se la modifica della categoria nel database non va a buon fine
     */
    public void changeCategoryNodeName(CategoryMutableTreeNode node, String newName) throws IllegalArgumentException, CategoryDatabaseException {
        try {
            if (!canNodeBeChanged(node))
                throw new IllegalArgumentException("Il nodo selezionato non può essere modificato.");

            Category category = node.getCategory();

            if (!category.isNameValid(newName))
                throw new IllegalArgumentException("Il nome della categoria non può essere nullo.");

            categoryDAO.updateCategory(category, newName);
            category.setName(newName);
            categoriesTreeModel.reload(node);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (CategoryDatabaseException e) {
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
     * @throws CategoryDatabaseException
     *             se la rimozione della categoria nel database non va a buon fine
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
     * Restituisce {@code true} se il nodo può essere modificato (quindi se non è nullo e non è la radice dell'albero), {@code false} altrimenti.
     * 
     * @param node
     *            il nodo da controllare
     * @return
     *         {@code true} se il nodo può essere modificato
     */
    public boolean canNodeBeChanged(CategoryMutableTreeNode node) {
        return node != null && !node.isRoot();
    }

}
