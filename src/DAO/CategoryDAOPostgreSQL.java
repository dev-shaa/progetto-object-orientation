package DAO;

import Entities.*;
import Exceptions.*;
// import GUI.Homepage.Categories.CategoryTreeNode;
import GUI.Homepage.Categories.CategoryTreeModel;
import GUI.Utilities.CustomTreeNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Implementazione dell'interfaccia CategoryDAO per database relazionali
 * PostgreSQL.
 * 
 * @see CategoryDAO
 */
public class CategoryDAOPostgreSQL implements CategoryDAO {

    private User user;

    private final String idKey = "id";
    private final String nameKey = "name";
    private final String parentKey = "parent_category";

    /**
     * Crea {@code CategoryDAOPostgreSQL} per interfacciarsi al database PostgreSQL
     * relativo alle categorie dell'utente.
     * 
     * @param user
     *            l'utente che accede al database
     * @throws IllegalArgumentException
     *             se l'utente di input è {@code null}
     */
    public CategoryDAOPostgreSQL(User user) throws IllegalArgumentException {
        setUser(user);
    }

    /**
     * Imposta l'utente di cui recuperare le categorie.
     *
     * @param user
     *            utente di cui recuperare le categorie.
     * @throws IllegalArgumentException
     *             se l'utente di input è {@code null}
     */
    public void setUser(User user) throws IllegalArgumentException {
        if (user == null)
            throw new IllegalArgumentException("L'utente non può essere nullo.");

        this.user = user;
    }

    /**
     * Restituisce l'utente che accede al database.
     *
     * @return utente che accede al database
     */
    public User getUser() {
        return this.user;
    }

    @Override
    public void addCategory(Category category) throws IllegalArgumentException, CategoryDatabaseException {
        if (category == null)
            throw new IllegalArgumentException("category non può essere null");

        Connection connection = null;
        Statement statement = null;

        try {
            // connection = DatabaseController.getInstance().getConnection();
            connection = DatabaseController.getConnection(); // TODO:
            connection.setAutoCommit(false);

            statement = connection.createStatement();

            String parentID = category.getParent() == null ? "NULL" : String.valueOf(category.getParent().getId());

            String query = "insert into category(name, parent_category, parent_user) values("
                    + category.getName() + ", "
                    + parentID + ", "
                    + user.getName() + ")";

            // il database genera un ID per ogni categoria, quindi vogliamo aggiornare la
            // classe category prima di concludere ogni operazione
            statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);

            ResultSet set = statement.getGeneratedKeys();

            if (set.first()) {
                category.setId(set.getInt(1));
            }

            connection.commit();
        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (Exception r) {
                throw new CategoryDatabaseException("Impossibile aggiungere nuova categoria.");
            }

            throw new CategoryDatabaseException("Impossibile aggiungere nuova categoria.");
        } finally {
            try {
                if (statement != null)
                    statement.close();

                if (connection != null)
                    connection.close();
            } catch (Exception e) {
                // non fare niente
                e.printStackTrace();
            }
        }
    }

    @Override
    public void updateCategoryName(Category category) throws IllegalArgumentException, CategoryDatabaseException {
        if (category == null)
            throw new IllegalArgumentException("category non può essere null");

        Connection connection = null;
        Statement statement = null;

        try {
            connection = DatabaseController.getConnection();
            statement = connection.createStatement();

            String query = "update category set name = " + category.getName() + " where id = " + category.getId();

            statement.executeUpdate(query);
        } catch (Exception e) {
            throw new CategoryDatabaseException("Impossibile modificare questa categoria.");
        } finally {
            try {
                if (statement != null)
                    statement.close();

                if (connection != null)
                    connection.close();
            } catch (Exception e) {
                // non fare niente
                e.printStackTrace();
            }
        }
    }

    @Override
    public void removeCategory(Category category) throws IllegalArgumentException, CategoryDatabaseException {
        if (category == null)
            throw new IllegalArgumentException("category non può essere null");

        Connection connection = null;
        Statement statement = null;

        try {
            connection = DatabaseController.getConnection();
            statement = connection.createStatement();

            String query = "delete from category where id = " + category.getId();

            statement.executeUpdate(query);
        } catch (Exception e) {
            throw new CategoryDatabaseException("Impossibile rimuovere questa categoria.");
        } finally {
            try {
                statement.close();
                connection.close();
            } catch (Exception e) {
                // non fare niente
                e.printStackTrace();
            }
        }
    }

    @Override
    public CategoryTreeModel getUserCategories() throws CategoryDatabaseException {

        // TODO:

        CustomTreeNode<Category> root = new CustomTreeNode<Category>(null);
        root.setLabel("I miei riferimenti");

        CustomTreeNode<Category> nodeA = new CustomTreeNode<Category>(new Category("AAA", 0));
        CustomTreeNode<Category> nodeB = new CustomTreeNode<Category>(new Category("BBB", 1));
        CustomTreeNode<Category> nodeC = new CustomTreeNode<Category>(new Category("CCC", 2));

        CategoryTreeModel tree = new CategoryTreeModel(root);

        tree.addNode(nodeA, root);
        tree.addNode(nodeB, root);
        tree.addNode(nodeC, nodeB);

        return tree;
    }

    // @Override
    // public Category[] getUserCategories() throws CategoryDatabaseException {

    // // DEBUG: solo per testing
    // ArrayList<Category> categories = new ArrayList<Category>();
    // Category aa = new Category("aaa");
    // Category bb = new Category("bbb");
    // bb.setParent(aa);
    // Category cc = new Category("ccc");
    // cc.setParent(aa);
    // Category dd = new Category("ddd");
    // categories.add(aa);
    // categories.add(bb);
    // categories.add(cc);
    // categories.add(dd);
    // return categories.toArray(new Category[categories.size()]);

    // // Connection connection = null;
    // // Statement statement = null;
    // // ResultSet resultSet = null;

    // // try {
    // // connection = DatabaseController.getInstance().getConnection();
    // // statement = connection.createStatement();
    // // String query = "select * from category where parent_user = " +
    // // user.getName();
    // // resultSet = statement.executeQuery(query);

    // // ArrayList<Category> categories = new ArrayList<Category>();
    // // HashMap<String, Category> nameToCategory = new HashMap<String, Category>();

    // // // occupiamoci prima di recuperare e creare tutte le categorie
    // // while (resultSet.next()) {
    // // Category category = new Category(resultSet.getString(nameKey),
    // // resultSet.getInt(idKey));

    // // categories.add(category);
    // // nameToCategory.put(category.getName(), category);
    // // }

    // // resultSet.first();

    // // // occupiamoci di assegnare i genitori giusti
    // // for (Category category : categories) {
    // // String parentCategoryName = resultSet.getString(parentKey);
    // // category.setParent(nameToCategory.get(parentCategoryName));
    // // resultSet.next();
    // // }

    // // return categories.toArray(new Category[categories.size()]);
    // // } catch (Exception e) {
    // // throw new CategoryDatabaseException("Impossibile recuperare le categorie dell'utente.");
    // // } finally {
    // // try {
    // // resultSet.close();
    // // statement.close();
    // // connection.close();
    // // } catch (Exception e) {
    // // // non fare niente
    // // }
    // // }
    // // }
    // }

}
