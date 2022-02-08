package Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import DAO.CategoryDAO;
import Entities.Category;
import Entities.References.BibliographicReference;
import Exceptions.CategoryDatabaseException;
import GUI.Utilities.Tree.CustomTreeModel;
import GUI.Utilities.Tree.CustomTreeNode;

/**
 * Controller per gestire il recupero, l'inserimento, la rimozione e la modifica di categorie.
 */
public class CategoryController {
    private CategoryDAO categoryDAO;

    private List<Category> categories;
    private CustomTreeModel<Category> treeModel;

    private boolean needToRetrieveFromDatabase;
    private boolean treeNeedsUpdate;

    private HashMap<Integer, Category> idToCategory;

    /**
     * Crea un nuovo controller delle categorie con il DAO indicato.
     * 
     * @param categoryDAO
     *            DAO per interfacciarsi col database
     * @throws IllegalArgumentException
     *             se {@code categoryDAO == null}
     */
    public CategoryController(CategoryDAO categoryDAO) {
        setCategoryDAO(categoryDAO);

        idToCategory = new HashMap<>();
    }

    /**
     * Imposta la classe DAO per recuperare le categorie dal database.
     * 
     * @param categoryDAO
     *            classe DAO per il database
     * @throws IllegalArgumentException
     *             se {@code categoryDAO == null}
     */
    public void setCategoryDAO(CategoryDAO categoryDAO) {
        if (categoryDAO == null)
            throw new IllegalArgumentException("categoryDAO can't be null");

        this.categoryDAO = categoryDAO;

        forceNextRetrievalFromDatabase();
    }

    /**
     * Restituisce la classe DAO usata per recuperare le categorie.
     * 
     * @return classe DAO per le categorie
     */
    public CategoryDAO getCategoryDAO() {
        return categoryDAO;
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
    public void save(Category category) throws CategoryDatabaseException {
        if (category == null)
            throw new IllegalArgumentException("category can't be null");

        getCategoryDAO().save(category);

        addToHashMap(category);

        CustomTreeNode<Category> node = new CustomTreeNode<Category>(category);
        CustomTreeNode<Category> parentNode = treeModel.findNode(category.getParent());

        treeModel.addNode(node, parentNode);
    }

    /**
     * @param category
     *            nodo di cui modificare la categoria
     * @param newName
     *            nuovo nome della categoria
     * @throws IllegalArgumentException
     *             se {@code categoryNode == null}, la categoria associata al nodo è nulla o il nuovo nome non è valido
     * @throws CategoryDatabaseException
     *             se la modifica non va a buon fine
     */
    public void update(Category category, String newName) throws CategoryDatabaseException {
        if (category == null)
            throw new IllegalArgumentException("category can't be null");

        if (category.getName().equals(newName))
            return;

        String oldName = category.getName();
        category.setName(newName);

        try {
            getCategoryDAO().update(category);
        } catch (CategoryDatabaseException e) {
            category.setName(oldName);
            throw e;
        }
    }

    /**
     * Rimuove una categoria dal database.
     * 
     * @param category
     *            nodo della categoria da rimuovere
     * @throws IllegalArgumentException
     *             se {@code categoryNode == null} o se la categoria associata al nodo è nulla
     * @throws CategoryDatabaseException
     *             se la rimozione non va a buon fine
     */
    public void remove(Category category) throws CategoryDatabaseException {
        if (category == null)
            throw new IllegalArgumentException("category can't be null");

        getCategoryDAO().remove(category);

        removeFromHashMap(category);

        treeModel.removeNodeFromParent(treeModel.findNode(category));
    }

    /**
     * Recupera tutte le categorie dell'utente.
     * <p>
     * Dopo essere state recuperate una prima volta, le categorie rimangono in memoria
     * in modo da evitare di dover creare nuove connessioni col database.
     * <p>
     * Il recupero dal database viene eseguito la prima volta dopo aver cambiato il DAO usato con {@link #setCategoryDAO(CategoryDAO)}, ma
     * è possibile forzarlo chiamando prima {@link #forceNextRetrievalFromDatabase()}.
     * 
     * @return lista con le categorie dell'utente.
     * @throws CategoryDatabaseException
     *             se il recupero delle categorie dal database non va a buon fine
     * 
     * @see #forceNextRetrievalFromDatabase()
     */
    public List<Category> getAll() throws CategoryDatabaseException {
        if (needToRetrieveFromDatabase) {
            retrieveFromDatabase();
        }

        return categories;
    }

    /**
     * Recupera tutte le categorie associate al riferimento bibliografico.
     * 
     * @param reference
     *            riferimento di cui recuperare le categorie
     * @return lista con le categorie associate al riferimento
     * @throws CategoryDatabaseException
     *             se il recupero delle categorie dal database non va a buon fine
     */
    public List<Category> get(BibliographicReference reference) throws CategoryDatabaseException {

        // ci serve perchè dobbiamo prima assicurarci che idToCategory sia pieno
        if (needToRetrieveFromDatabase) {
            retrieveFromDatabase();
        }

        List<Integer> ids = getCategoryDAO().getIDs(reference);
        ArrayList<Category> categories = new ArrayList<>();

        for (Integer id : ids) {
            categories.add(idToCategory.get(id));
        }

        categories.trimToSize();
        return categories;
    }

    /**
     * Restituisce l'albero delle categorie.
     * 
     * @return
     *         albero delle categorie dell'utente
     * @throws CategoryDatabaseException
     *             se il recupero non va a buon fine
     */
    public CustomTreeModel<Category> getTree() throws CategoryDatabaseException {
        if (treeNeedsUpdate) {
            CustomTreeNode<Category> root = new CustomTreeNode<Category>(null);
            root.setLabel("I miei riferimenti");

            treeModel = new CustomTreeModel<>(root);

            HashMap<Category, CustomTreeNode<Category>> categoryToNode = new HashMap<>();
            categoryToNode.put(null, root);

            for (Category category : getAll()) {
                categoryToNode.put(category, new CustomTreeNode<Category>(category));
            }

            for (Category category : getAll()) {
                CustomTreeNode<Category> node = categoryToNode.get(category);
                CustomTreeNode<Category> parent = categoryToNode.get(category.getParent());

                treeModel.addNode(node, parent);
            }

            treeNeedsUpdate = false;
        }

        return treeModel;
    }

    /**
     * Impone che, al prossimo recupero, le categorie vengano recuperate di nuovo direttamente dal database.
     */
    public void forceNextRetrievalFromDatabase() {
        needToRetrieveFromDatabase = true;
        treeNeedsUpdate = true;
    }

    private void retrieveFromDatabase() throws CategoryDatabaseException {
        idToCategory.clear();

        categories = getCategoryDAO().getAll();

        for (Category category : categories) {
            addToHashMap(category);
        }

        needToRetrieveFromDatabase = false;
    }

    private void addToHashMap(Category category) {
        if (category == null)
            return;

        idToCategory.put(category.getID(), category);
    }

    private void removeFromHashMap(Category category) {
        if (category == null)
            return;

        idToCategory.remove(category.getID());
    }

}
