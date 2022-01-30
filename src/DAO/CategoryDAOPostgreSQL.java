package DAO;

import Entities.*;
import Exceptions.*;
import GUI.Utilities.CustomTreeModel;
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
    public CategoryDAOPostgreSQL(User user) {
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
            connection = DatabaseController.getConnection();

            // il database genera, per ogni nuova categoria, un ID unico
            // se non siamo in grado di recuperarlo e assegnarlo alla categoria, annulliamo tutta l'operazione
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
    public CustomTreeModel<Category> getUserCategories() throws CategoryDatabaseException {

        // DEBUG: solo per testing
        CustomTreeNode<Category> root = new CustomTreeNode<Category>(null);
        root.setLabel("I miei riferimenti");
        CustomTreeNode<Category> nodeA = new CustomTreeNode<Category>(new Category("AAA", 0));
        CustomTreeNode<Category> nodeB = new CustomTreeNode<Category>(new Category("BBB", 1));
        CustomTreeNode<Category> nodeC = new CustomTreeNode<Category>(new Category("CCC", 2));

        CustomTreeModel<Category> tree = new CustomTreeModel<Category>(root);

        tree.addNode(nodeA, root);
        tree.addNode(nodeB, root);
        tree.addNode(nodeC, nodeB);

        return tree;

        // TODO:

        // Connection connection = null;
        // Statement statement = null;
        // ResultSet resultSet = null;

        // CustomTreeNode<Category> root = new CustomTreeNode<Category>(null);
        // root.setLabel("I miei riferimenti");

        // CustomTreeModel<Category> tree = new CustomTreeModel<>(root);

        // try {
        // connection = DatabaseController.getConnection();
        // statement = connection.createStatement();
        // String query = "select * from category where parent_user = " + user.getName();
        // resultSet = statement.executeQuery(query);

        // ArrayList<CustomTreeNode<Category>> categories = new ArrayList<CustomTreeNode<Category>>();
        // HashMap<Integer, CustomTreeNode<Category>> idToNode = new HashMap<Integer, CustomTreeNode<Category>>();

        // idToNode.put(0, root);

        // // occupiamoci prima di recuperare e creare tutte le categorie
        // while (resultSet.next()) {
        // Category category = new Category(resultSet.getString(nameKey), resultSet.getInt(idKey));
        // CustomTreeNode<Category> node = new CustomTreeNode<Category>(category);

        // categories.add(node);
        // idToNode.put(category.getId(), node);
        // }

        // resultSet.first();

        // // occupiamoci di assegnare i genitori giusti
        // for (CustomTreeNode<Category> node : categories) {
        // CustomTreeNode<Category> parent = idToNode.get(resultSet.getInt(parentKey));

        // node.getUserObject().setParent(parent.getUserObject());
        // tree.addNode(node, parent);

        // resultSet.next();
        // }

        // return tree;
        // } catch (Exception e) {
        // throw new CategoryDatabaseException("Impossibile recuperare le categorie dell'utente.");
        // } finally {
        // try {
        // if (resultSet != null)
        // resultSet.close();

        // if (statement != null)
        // statement.close();

        // if (connection != null)
        // connection.close();
        // } catch (Exception e) {
        // // non fare niente
        // }
        // }
    }

}