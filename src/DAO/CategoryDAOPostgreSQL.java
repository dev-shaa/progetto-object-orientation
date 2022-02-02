package DAO;

import Entities.*;
import Exceptions.*;
import GUI.Utilities.CustomTreeModel;
import GUI.Utilities.CustomTreeNode;

import java.util.ArrayList;
import java.util.HashMap;

import Controller.DatabaseController;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Implementazione dell'interfaccia CategoryDAO per database relazionali PostgreSQL.
 * 
 * @see CategoryDAO
 */
public class CategoryDAOPostgreSQL implements CategoryDAO {

    private User user;

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
     *             se {@code user == null}
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

    /**
     * {@inheritDoc}
     * 
     * @throws IllegalArgumentException
     *             se {@code category == null}
     */
    @Override
    public void addCategory(Category category) throws CategoryDatabaseException {
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

            String parentID = category.getParent() == null ? "null" : String.valueOf(category.getParent().getId());

            String query = "insert into category(name, parent, owner) values('"
                    + category.getName() + "', "
                    + parentID + ", '"
                    + user.getName() + "')";

            // il database genera un ID per ogni categoria, quindi vogliamo aggiornare la
            // classe category prima di concludere ogni operazione
            statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);

            ResultSet set = statement.getGeneratedKeys();

            if (set.next()) {
                category.setId(set.getInt(1));
            }

            connection.commit();
        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (Exception r) {
                throw new CategoryDatabaseException("Impossibile aggiungere nuova categoria.");
            }

            e.printStackTrace();
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

    /**
     * {@inheritDoc}
     * 
     * @throws IllegalArgumentException
     *             se {@code category == null}
     */
    @Override
    public void updateCategoryName(Category category) throws CategoryDatabaseException {
        if (category == null)
            throw new IllegalArgumentException("category non può essere null");

        Connection connection = null;
        Statement statement = null;

        try {
            connection = DatabaseController.getConnection();
            statement = connection.createStatement();

            String query = "update category set name = '" + category.getName() + "' where id = " + category.getId();

            statement.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
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

    /**
     * {@inheritDoc}
     * 
     * @throws IllegalArgumentException
     *             se {@code category == null}
     */
    @Override
    public void removeCategory(Category category) throws CategoryDatabaseException {
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
    public CustomTreeModel<Category> getUserCategories() throws CategoryDatabaseException {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        CustomTreeNode<Category> root = new CustomTreeNode<Category>(null);
        root.setLabel("I miei riferimenti");

        CustomTreeModel<Category> tree = new CustomTreeModel<>(root);

        try {
            connection = DatabaseController.getConnection();
            statement = connection.createStatement();
            String query = "select * from category where owner = '" + user.getName() + "'";
            resultSet = statement.executeQuery(query);

            ArrayList<CustomTreeNode<Category>> categories = new ArrayList<>();
            HashMap<Integer, CustomTreeNode<Category>> idToNode = new HashMap<>();
            HashMap<CustomTreeNode<Category>, Integer> nodeToParentID = new HashMap<>();

            idToNode.put(0, root);

            // occupiamoci prima di recuperare e creare tutte le categorie
            while (resultSet.next()) {
                Category category = new Category(resultSet.getString("name"), resultSet.getInt("id"));
                CustomTreeNode<Category> node = new CustomTreeNode<Category>(category);

                categories.add(node);
                idToNode.put(category.getId(), node);
                nodeToParentID.put(node, resultSet.getInt("parent"));
            }

            // occupiamoci di assegnare i genitori giusti
            for (CustomTreeNode<Category> node : categories) {
                CustomTreeNode<Category> parent = idToNode.get(nodeToParentID.get(node));

                node.getUserObject().setParent(parent.getUserObject());
                tree.addNode(node, parent);
            }

            return tree;
        } catch (Exception e) {
            e.printStackTrace();
            throw new CategoryDatabaseException("Impossibile recuperare le categorie dell'utente.");
        } finally {
            try {
                if (resultSet != null)
                    resultSet.close();

                if (statement != null)
                    statement.close();

                if (connection != null)
                    connection.close();
            } catch (Exception e) {
                // non fare niente
            }
        }
    }

}