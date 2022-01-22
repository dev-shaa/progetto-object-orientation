package GUI.Categories;

import DAO.CategoryDAO;
import Entities.Category;
import Exceptions.CategoryDatabaseException;
import java.util.HashMap;
import javax.swing.tree.DefaultTreeModel;

/**
 * Classe che gestisce l'albero delle categorie dell'utente, con funzioni di inserimento, modifica e rimozione dal database.
 * 
 * @see CategoryDAO
 * @see CategoryMutableTreeNode
 */
public class CategoriesTreeManager {

    private CategoryDAO categoryDAO;
    private DefaultTreeModel categoriesTreeModel;

    /**
     * Crea {@code CategoriesTree} con le categorie appartenenti all'utente.
     * 
     * @param categoryDAO
     *            classe DAO per recuperare le categorie dal database
     * @throws IllegalArgumentException
     *             se {@code categoryDAO} non è valido
     * @throws CategoryDatabaseException
     *             se il recupero delle categorie dal database non va a buon fine
     * @see #setCategoryDAO(CategoryDAO)
     */
    public CategoriesTreeManager(CategoryDAO categoryDAO) throws IllegalArgumentException, CategoryDatabaseException {
        setCategoryDAO(categoryDAO);
    }

    /**
     * Imposta la classe DAO per interfacciarsi col database delle categorie.
     * 
     * @param categoryDAO
     *            classe DAO per interfacciarsi col database delle categorie
     * @throws IllegalArgumentException
     *             se {@code categoryDAO == null}
     * @see CategoryDAO
     */
    public void setCategoryDAO(CategoryDAO categoryDAO) throws IllegalArgumentException, CategoryDatabaseException {
        if (categoryDAO == null)
            throw new IllegalArgumentException("categoryDAO non può essere null");

        this.categoryDAO = categoryDAO;
        categoriesTreeModel = new DefaultTreeModel(getTreeFromArray(categoryDAO.getUserCategories()));
    }

    /**
     * Restituisce l'albero delle categorie.
     * 
     * @return albero delle categorie
     * @see DefaultTreeModel
     */
    public DefaultTreeModel getTreeModel() {
        return this.categoriesTreeModel;
    }

    /**
     * Aggiunge un nuovo nodo categoria all'albero come figlio del nodo {@code parent} e lo salva nel database.
     * 
     * @param newCategory
     *            categoria da aggiungere
     * @param parentNode
     *            il nodo genitore della categoria da aggiungere
     * @throws IllegalArgumentException
     *             se il nome della categoria non è valido
     * @throws CategoryDatabaseException
     *             se la crezione della categoria nel database non va a buon fine
     * @see Category
     */
    public void addNode(Category newCategory, CategoryMutableTreeNode parentNode) throws IllegalArgumentException, CategoryDatabaseException {
        newCategory.setParent(parentNode == null ? null : parentNode.getUserObject());

        categoryDAO.addCategory(newCategory);

        CategoryMutableTreeNode newCategoryNode = new CategoryMutableTreeNode(newCategory);
        categoriesTreeModel.insertNodeInto(newCategoryNode, parentNode, parentNode.getChildCount());
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
     *             se la modifica della categoria nel database
     *             non va a buon fine
     * @see CategoryMutableTreeNode
     */
    public void changeNode(CategoryMutableTreeNode node, String newName) throws IllegalArgumentException, CategoryDatabaseException {
        if (!node.canBeChanged())
            throw new IllegalArgumentException("Il nodo selezionato non può essere modificato.");

        Category category = node.getUserObject();
        String categoryNameBeforeChange = category.getName();

        try {
            category.setName(newName);
            categoryDAO.changeCategory(category);
            categoriesTreeModel.reload(node);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (CategoryDatabaseException e) {
            category.setName(categoryNameBeforeChange);
            throw e;
        }
    }

    /**
     * Rimuove il nodo categoria indicato dall'albero e dal database.
     * 
     * @param node
     *            il nodo da rimuovere
     * @throws IllegalArgumentException
     *             se {@code node} non può essere eliminato
     * @throws CategoryDatabaseException
     *             se la rimozione della categoria nel
     *             database non va a buon fine
     * @see CategoryMutableTreeNode
     */
    public void removeNode(CategoryMutableTreeNode node) throws IllegalArgumentException, CategoryDatabaseException {
        if (!node.canBeChanged())
            throw new IllegalArgumentException("Il nodo selezionato non può essere rimosso.");

        categoryDAO.removeCategory(node.getUserObject());
        CategoryMutableTreeNode parent = node.getParent();
        node.removeFromParent();
        categoriesTreeModel.reload(parent);
    }

    /**
     * Converte una lista di {@code Category} in un albero.
     * Il nodo radice contiene una categoria {@code null} e serve solo per contenere
     * tutte i nodi creati.
     * Se una categoria ha un genitore che non è presente nella lista, non viene
     * inclusa nell'albero.
     * 
     * @param categories
     *            la lista di categoria da convertire in albero
     * @return
     *         il nodo radice dell'albero
     * @throws IllegalArgumentException
     *             se {@code categories == null}
     * @see CategoryMutableTreeNode
     */
    private CategoryMutableTreeNode getTreeFromArray(Category[] categories) throws IllegalArgumentException {
        if (categories == null)
            throw new IllegalArgumentException("categories non può essere null");

        HashMap<Category, CategoryMutableTreeNode> categoriesNodeMap = new HashMap<Category, CategoryMutableTreeNode>();

        CategoryMutableTreeNode root = new CategoryMutableTreeNode();

        categoriesNodeMap.put(null, root);

        for (Category category : categories) {
            categoriesNodeMap.put(category, new CategoryMutableTreeNode(category));
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
