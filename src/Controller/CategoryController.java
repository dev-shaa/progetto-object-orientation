package Controller;

import DAO.CategoryDAO;
import Entities.Category;
import Exceptions.CategoryDatabaseException;
import GUI.Utilities.CustomTreeModel;
import GUI.Utilities.CustomTreeNode;

/**
 * Controller per gestire il recupero, l'inserimento, la rimozione e la modifica di categorie.
 * Serve per non doversi sempre interfacciarsi col database e un modo più "sicuro" di passare le categorie tra classi.
 */
public class CategoryController {
    private CategoryDAO categoryDAO;
    private CustomTreeModel<Category> categoryTree;

    /**
     * Crea un nuovo controller delle categorie.
     * 
     * @param categoryDAO
     *            DAO per interfacciarsi col database
     * @throws IllegalArgumentException
     *             se {@code categoryDAO == null}
     * @throws CategoryDatabaseException
     *             se il recupero iniziale delle categorie non va a buon fine
     */
    public CategoryController(CategoryDAO categoryDAO) throws CategoryDatabaseException {
        setCategoryDAO(categoryDAO);
    }

    /**
     * Restituisce il DAO delle categorie.
     * 
     * @return
     *         DAO delle categorie
     */
    public CategoryDAO getCategoryDAO() {
        return categoryDAO;
    }

    /**
     * Imposta il DAO delle categorie. Questo funziona anche da "reset", in quanto verranno recuperate di nuovo
     * dal database le categorie con il DAO assegnato.
     * 
     * @param categoryDAO
     *            DAO delle categorie
     * @throws IllegalArgumentException
     *             se {@code categoryDAO == null}
     * @throws CategoryDatabaseException
     *             se non è possibile recuperare le categorie dal database
     */
    public void setCategoryDAO(CategoryDAO categoryDAO) throws CategoryDatabaseException {
        if (categoryDAO == null)
            throw new IllegalArgumentException("categoryDAO can't be null");

        this.categoryDAO = categoryDAO;

        this.categoryTree = getCategoryDAO().getUserCategories();
    }

    /**
     * Restituisce l'albero delle categorie.
     * 
     * @return
     *         albero delle categorie dell'utente
     */
    public CustomTreeModel<Category> getCategoriesTree() {
        return categoryTree;
    }

    /**
     * Salva una categoria nel database.
     * 
     * @param name
     *            nome della nuova categoria
     * @param parent
     *            nodo genitore della categoria
     * @throws IllegalArgumentException
     *             se il nome della categoria non è valido
     * @throws CategoryDatabaseException
     *             se il salvataggio non va a buon fine
     */
    public void addCategory(String name, CustomTreeNode<Category> parent) throws CategoryDatabaseException {
        Category category = new Category(name);

        if (parent != null)
            category.setParent(parent.getUserObject());

        categoryDAO.addCategory(category);

        categoryTree.addNode(new CustomTreeNode<Category>(category), parent);
    }

    /**
     * @param categoryNode
     *            nodo di cui modificare la categoria
     * @param newName
     *            nuovo nome della categoria
     * @throws IllegalArgumentException
     *             se {@code categoryNode == null}, la categoria associata al nodo è nulla o il nuovo nome non è valido
     * @throws CategoryDatabaseException
     *             se la modifica non va a buon fine
     */
    public void changeCategory(CustomTreeNode<Category> categoryNode, String newName) throws CategoryDatabaseException {
        if (categoryNode == null)
            throw new IllegalArgumentException("category node can't be null");

        Category category = categoryNode.getUserObject();

        if (category == null)
            throw new IllegalArgumentException("can't change name to null category");

        if (category.getName().equals(newName))
            return;

        String oldName = category.getName();
        category.setName(newName);

        try {
            categoryDAO.updateCategoryName(category);
        } catch (CategoryDatabaseException e) {
            category.setName(oldName);
            throw e;
        }
    }

    /**
     * Rimuove una categoria dal database.
     * 
     * @param categoryNode
     *            nodo della categoria da rimuovere
     * @throws IllegalArgumentException
     *             se {@code categoryNode == null} o se la categoria associata al nodo è nulla
     * @throws CategoryDatabaseException
     *             se la rimozione non va a buon fine
     */
    public void removeCategory(CustomTreeNode<Category> categoryNode) throws CategoryDatabaseException {
        if (categoryNode == null)
            throw new IllegalArgumentException("categoryNode can't be null");

        Category category = categoryNode.getUserObject();

        if (category == null)
            throw new IllegalArgumentException("can't delete null category");

        categoryDAO.removeCategory(category);

        categoryNode.removeFromParent();

        categoryTree.removeNodeFromParent(categoryNode);
    }

}
