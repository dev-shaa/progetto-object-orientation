package Database.Repositories;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Database.DAO.CategoryDAO;
import Entities.Category;
import Exceptions.Database.CategoryDatabaseException;
import Utilities.Tree.CustomTreeModel;
import Utilities.Tree.CustomTreeNode;

/**
 * Repository per gestire il recupero, l'inserimento, la rimozione e la modifica di categorie.
 */
public class CategoryRepository {

    private CategoryDAO categoryDAO;

    private List<Category> categories;
    private CustomTreeModel<Category> treeModel;
    private HashMap<Integer, Category> idToCategory;

    private boolean needToRetrieveFromDatabase;
    private boolean treeNeedsUpdate;

    /**
     * Crea un nuovo controller delle categorie con il DAO indicato.
     * 
     * @param categoryDAO
     *            DAO per interfacciarsi col database
     * @throws IllegalArgumentException
     *             se {@code categoryDAO == null}
     */
    public CategoryRepository(CategoryDAO categoryDAO) {
        setCategoryDAO(categoryDAO);
        idToCategory = new HashMap<>();
    }

    /**
     * Imposta il DAO delle categorie.
     * <p>
     * Questa funzione chiama anche {@link #forceNextRetrievalFromDatabase()}.
     * 
     * @param categoryDAO
     *            DAO delle categorie
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
     * Salva una categoria, inserendola anche nell'albero corrente.
     * 
     * @param category
     *            categoria da salvare
     * @throws CategoryDatabaseException
     *             se il salvataggio non va a buon fine
     */
    public void save(Category category) throws CategoryDatabaseException {
        try {
            categoryDAO.save(category);
            idToCategory.put(category.getID(), category);

            if (treeModel != null) {
                CustomTreeNode<Category> node = new CustomTreeNode<Category>(category);
                CustomTreeNode<Category> parentNode = treeModel.findNode(category.getParent());
                treeModel.add(node, parentNode);
            }
        } catch (SQLException e) {
            throw new CategoryDatabaseException("Impossibile salvare la categoria.", e);
        }
    }

    /**
     * Aggiorna il nome della categoria.
     * 
     * @param category
     *            categoria da modificare
     * @param newName
     *            nuovo nome della categoria
     * @throws IllegalArgumentException
     *             se {@code category == null} o il nuovo nome non è valido
     * @throws CategoryDatabaseException
     *             se la modifica non va a buon fine
     */
    public void update(Category category, String newName) throws CategoryDatabaseException {
        if (category == null)
            throw new IllegalArgumentException("category can't be null");

        String oldName = category.getName();

        try {
            category.setName(newName);
            categoryDAO.update(category);

            if (treeModel != null)
                treeModel.reload();
        } catch (Exception e) {
            category.setName(oldName);
            throw new CategoryDatabaseException("Impossibile aggiornare la categoria.", e);
        }

    }

    /**
     * Rimuove una categoria.
     * 
     * @param category
     *            categoria da rimuovere
     * @throws CategoryDatabaseException
     *             se la rimozione non va a buon fine
     */
    public void remove(Category category) throws CategoryDatabaseException {
        try {
            categoryDAO.remove(category);
            idToCategory.remove(category.getID());

            if (treeModel != null)
                treeModel.remove(treeModel.findNode(category));
        } catch (Exception e) {
            throw new CategoryDatabaseException("Impossibile eliminare la categoria.", e);
        }
    }

    /**
     * Recupera tutte le categorie dell'utente.
     * <p>
     * Dopo essere state recuperate una prima volta, le categorie rimangono in memoria.
     * <p>
     * Il recupero dal database viene eseguito la prima volta dopo aver cambiato il DAO usato con {@link #setCategoryDAO(CategoryDAO)}, ma
     * è possibile forzarlo chiamando prima {@link #forceNextRetrievalFromDatabase()}.
     * 
     * @return lista con le categorie dell'utente.
     * @throws CategoryDatabaseException
     *             se il recupero delle categorie dal database non va a buon fine
     */
    public List<Category> getAll() throws CategoryDatabaseException {
        try {
            if (needToRetrieveFromDatabase)
                retrieveFromDatabase();

            return categories;
        } catch (Exception e) {
            throw new CategoryDatabaseException("Impossibile recuperare le categorie dell'utente.", e);
        }
    }

    /**
     * Recupera tutte le categorie associate al riferimento bibliografico.
     * 
     * @param referenceID
     *            identificativo riferimento di cui recuperare le categorie
     * @return lista con le categorie associate al riferimento
     * @throws CategoryDatabaseException
     *             se il recupero delle categorie non va a buon fine
     */
    public List<Category> get(int referenceID) throws CategoryDatabaseException {
        try {
            if (needToRetrieveFromDatabase)
                retrieveFromDatabase();

            List<Integer> ids = categoryDAO.getCategoriesIDFor(referenceID);
            ArrayList<Category> categories = new ArrayList<>();

            for (Integer id : ids)
                categories.add(idToCategory.get(id));

            categories.trimToSize();
            return categories;
        } catch (SQLException e) {
            throw new CategoryDatabaseException("Impossibile recuperare le categorie associate al riferimento.", e);
        }
    }

    /**
     * Restituisce le categorie dell'utente in forma di albero.
     * 
     * @return
     *         albero delle categorie
     * @throws CategoryDatabaseException
     *             se il recupero non va a buon fine
     */
    public CustomTreeModel<Category> getTree() throws CategoryDatabaseException {
        if (treeNeedsUpdate) {
            List<Category> categories = getAll();

            CustomTreeNode<Category> root = new CustomTreeNode<Category>(null);
            root.setLabel("I miei riferimenti");

            treeModel = new CustomTreeModel<>(root);

            HashMap<Category, CustomTreeNode<Category>> categoryToNode = new HashMap<>();
            categoryToNode.put(null, root);

            // mappa prima tutti i nodi
            for (Category category : categories)
                categoryToNode.put(category, new CustomTreeNode<Category>(category));

            // associa i genitori dei nodi
            for (Category category : categories) {
                CustomTreeNode<Category> node = categoryToNode.get(category);
                CustomTreeNode<Category> parent = categoryToNode.get(category.getParent());

                treeModel.add(node, parent);
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

    private void retrieveFromDatabase() throws SQLException {
        idToCategory.clear();
        categories = categoryDAO.getAll();

        for (Category category : categories)
            idToCategory.put(category.getID(), category);

        needToRetrieveFromDatabase = false;
    }

}