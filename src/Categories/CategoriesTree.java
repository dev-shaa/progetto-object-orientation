import java.util.List;
import java.util.HashMap;
import javax.swing.tree.DefaultTreeModel;

/**
 * Classe che gestisce l'albero delle categorie dell'utente,
 * con funzioni di inserimento, modifica e rimozione dal database.
 * 
 * @version 0.9
 * @author Salvatore Di Gennaro
 * @see CategoryDAO
 */
public class CategoriesTree {

    private CategoryDAO categoryDAO;
    private DefaultTreeModel categoriesTreeModel;

    /**
     * Crea {@code CategoriesTree} con i dati relativi all'utente.
     * 
     * @param categoryDAO
     *            classe DAO per recuperare dati dal database
     * @throws IllegalArgumentException
     *             se {@code categoryDAO == null}
     * @throws CategoryDatabaseException
     *             se il recupero delle categorie dal database non va a buon fine
     */
    public CategoriesTree(CategoryDAO categoryDAO) throws IllegalArgumentException, CategoryDatabaseException {

        if (categoryDAO == null)
            throw new IllegalArgumentException("categoryDAO non può essere null.");

        this.categoryDAO = categoryDAO;

        CategoryMutableTreeNode tree = getTreeFromList(categoryDAO.getUserCategories());
        categoriesTreeModel = new DefaultTreeModel(tree);
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
     * @return il nuovo nodo creato
     * @throws IllegalArgumentException
     *             se il nome della categoria non è valido
     * @throws CategoryDatabaseException
     *             se la crezione della categoria nel database non va a buon fine
     * @see Category
     */
    public CategoryMutableTreeNode addCategoryNode(CategoryMutableTreeNode parent, String name) throws IllegalArgumentException, CategoryDatabaseException {
        Category parentCategory = parent == null ? null : parent.getUserObject();
        Category newCategory = new Category(name, parentCategory);

        categoryDAO.addCategory(newCategory);

        CategoryMutableTreeNode newCategoryNode = new CategoryMutableTreeNode(newCategory);
        categoriesTreeModel.insertNodeInto(newCategoryNode, parent, parent.getChildCount());

        return newCategoryNode;
    }

    /**
     * Modifica il nome della categoria associata a un nodo.
     * 
     * @param node
     *            il nodo di cui modificare la categoria
     * @param newName
     *            il nuovo nome della categoria
     * @throws IllegalArgumentException
     *             se il nodo non può essere modificato
     * @throws CategoryDatabaseException
     *             se la modifica della categoria nel database non va a buon fine
     * @see CategoryMutableTreeNode
     */
    public void changeCategoryNodeName(CategoryMutableTreeNode node, String newName) throws IllegalArgumentException, CategoryDatabaseException {
        if (!node.canBeChanged())
            throw new IllegalArgumentException("Il nodo selezionato non può essere modificato.");

        Category category = node.getUserObject();

        if (!category.isNameValid(newName))
            throw new IllegalArgumentException("Il nome della categoria non può essere nullo.");

        categoryDAO.changeCategory(category, newName);
        category.setName(newName);
        categoriesTreeModel.reload(node);
    }

    /**
     * Rimuove il nodo categoria indicato dall'albero e dal database.
     * 
     * @param node
     *            il nodo da rimuovere
     * @throws IllegalArgumentException
     *             se {@code node} non può essere eliminato
     * @throws CategoryDatabaseException
     *             se la rimozione della categoria nel database non va a buon fine
     * @see CategoryMutableTreeNode
     */
    public void removeCategoryNode(CategoryMutableTreeNode node) throws IllegalArgumentException, CategoryDatabaseException {
        if (!node.canBeChanged())
            throw new IllegalArgumentException("Il nodo selezionato non può essere modificato.");

        categoryDAO.removeCategory(node.getUserObject());
        CategoryMutableTreeNode parent = node.getParent();
        node.removeFromParent();
        categoriesTreeModel.reload(parent);
    }

    /**
     * Converte una lista di {@code Category} in un albero.
     * Il nodo radice contiene una categoria {@code null} e serve solo per contenere tutte i nodi creati.
     * Se una categoria ha un genitore che non è presente nella lista, non viene inclusa nell'albero.
     * 
     * @param categories
     *            la lista di categoria da convertire in albero
     * @return
     *         il nodo radice dell'albero
     * @throws IllegalArgumentException
     *             se {@code categories == null}
     */
    private CategoryMutableTreeNode getTreeFromList(List<Category> categories) throws IllegalArgumentException {
        if (categories == null)
            throw new IllegalArgumentException("La lista delle categorie non può essere nulla.");

        HashMap<Category, CategoryMutableTreeNode> categoriesNodeMap = new HashMap<Category, CategoryMutableTreeNode>();

        CategoryMutableTreeNode root = new CategoryMutableTreeNode();

        categoriesNodeMap.put(null, root);

        for (Category current : categories) {
            categoriesNodeMap.put(current, new CategoryMutableTreeNode(current));
        }

        for (Category current : categories) {
            Category parent = current.getParent();

            CategoryMutableTreeNode currentNode = categoriesNodeMap.get(current);
            CategoryMutableTreeNode parentNode = categoriesNodeMap.get(parent);

            if (parentNode != null) {
                parentNode.insert(currentNode, parentNode.getChildCount());

                categoriesNodeMap.put(parent, parentNode);
                categoriesNodeMap.put(current, currentNode);
            }
        }

        return root;
    }

}
