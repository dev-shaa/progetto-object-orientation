package DAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Controller.ConnectionController;
import Entities.References.BibliographicReference;
import Entities.*;
import Exceptions.Database.CategoryDatabaseException;
import Exceptions.Database.DatabaseConnectionException;

/**
 * Implementazione dell'interfaccia CategoryDAO per database relazionali PostgreSQL.
 * 
 * @see CategoryDAO
 */
public class CategoryDAOPostgreSQL implements CategoryDAO {

    private User user;

    /**
     * Crea una nuova classe DAO per interfacciarsi al database PostgreSQL relativo alle categorie dell'utente.
     * 
     * @param user
     *            l'utente che accede al database
     * @throws IllegalArgumentException
     *             se {@code user == null}
     */
    public CategoryDAOPostgreSQL(User user) {
        if (user == null)
            throw new IllegalArgumentException("user can't be null");

        this.user = user;
    }

    /**
     * {@inheritDoc}
     * 
     * @throws IllegalArgumentException
     *             se {@code category == null}
     */
    @Override
    public void save(Category category) throws CategoryDatabaseException {
        if (category == null)
            throw new IllegalArgumentException("category can't be null");

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        String parentID = category.getParent() == null ? null : String.valueOf(category.getParent().getID());
        String command = "insert into category(name, parent, owner) values(?," + parentID + ",?)";

        try {
            connection = ConnectionController.getConnection();

            // il database genera, per ogni nuova categoria, un ID unico
            // se non siamo in grado di recuperarlo e assegnarlo alla categoria, annulliamo tutta l'operazione
            connection.setAutoCommit(false);

            statement = connection.prepareStatement(command, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, category.getName());
            statement.setString(2, user.getName());
            statement.executeUpdate();

            resultSet = statement.getGeneratedKeys();

            if (resultSet.next())
                category.setID(resultSet.getInt(1));

            connection.commit();
        } catch (SQLException | DatabaseConnectionException e) {
            try {
                connection.rollback();
            } catch (Exception r) {
                // non fare niente
            }

            throw new CategoryDatabaseException("Impossibile aggiungere nuova categoria.");
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

    /**
     * {@inheritDoc}
     * 
     * @throws IllegalArgumentException
     *             se {@code category == null} o se {@code category} non ha un ID
     */
    @Override
    public void update(Category category) throws CategoryDatabaseException {
        if (category == null)
            throw new IllegalArgumentException("category can't be null");

        if (category.getID() == null)
            throw new IllegalArgumentException("category doesn't have an ID");

        Connection connection = null;
        PreparedStatement statement = null;
        String command = "update category set name = ? where id = ?";

        try {
            connection = ConnectionController.getConnection();
            statement = connection.prepareStatement(command);

            statement.setString(1, category.getName());
            statement.setInt(2, category.getID());

            statement.executeUpdate(command);
        } catch (SQLException | DatabaseConnectionException e) {
            throw new CategoryDatabaseException("Impossibile modificare questa categoria.");
        } finally {
            try {
                if (statement != null)
                    statement.close();

                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                // non fare niente
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @throws IllegalArgumentException
     *             se {@code category == null} o se {@code category} non ha un ID
     */
    @Override
    public void remove(Category category) throws CategoryDatabaseException {
        if (category == null)
            throw new IllegalArgumentException("category can't be null");

        if (category.getID() == null)
            throw new IllegalArgumentException("category doesn't have an ID");

        Connection connection = null;
        Statement statement = null;
        String command = "delete from category where id = " + category.getID();

        try {
            connection = ConnectionController.getConnection();
            statement = connection.createStatement();

            statement.executeUpdate(command);
        } catch (SQLException | DatabaseConnectionException e) {
            throw new CategoryDatabaseException("Impossibile rimuovere questa categoria.");
        } finally {
            try {
                if (statement != null)
                    statement.close();

                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                // non fare niente
            }
        }
    }

    @Override
    public List<Category> getAll() throws CategoryDatabaseException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = "select * from category where owner = ?";

        try {
            connection = ConnectionController.getConnection();
            statement = connection.prepareStatement(query);
            statement.setString(1, user.getName());
            resultSet = statement.executeQuery();

            HashMap<Integer, Category> idToCategory = new HashMap<>();
            HashMap<Category, Integer> nodeToParentID = new HashMap<>();

            while (resultSet.next()) {
                Category category = new Category(resultSet.getString("name"), null, resultSet.getInt("id"));

                idToCategory.put(category.getID(), category);
                nodeToParentID.put(category, resultSet.getInt("parent"));
            }

            for (Category category : idToCategory.values()) {
                Integer parentID = nodeToParentID.get(category);

                if (parentID != null)
                    category.setParent(idToCategory.get(parentID));
            }

            ArrayList<Category> categories = new ArrayList<>();
            categories.addAll(idToCategory.values());
            categories.trimToSize();

            return categories;
        } catch (SQLException | DatabaseConnectionException e) {
            throw new CategoryDatabaseException("Impossibile recuperare le categorie dell'utente.");
        } finally {
            try {
                if (resultSet != null)
                    resultSet.close();

                if (statement != null)
                    statement.close();

                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                // non fare niente
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @throws IllegalArgumentException
     *             se {@code reference == null} o se {@code reference} non ha un ID
     */
    @Override
    public List<Integer> getIDs(BibliographicReference reference) throws CategoryDatabaseException {
        if (reference == null)
            throw new IllegalArgumentException("reference can't be null");

        if (reference.getID() == null)
            throw new IllegalArgumentException("reference doesn't have an ID");

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        String query = "select category from category_reference_association where reference = " + reference.getID();

        try {
            connection = ConnectionController.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            ArrayList<Integer> ids = new ArrayList<>();

            while (resultSet.next())
                ids.add(resultSet.getInt("category"));

            ids.trimToSize();

            return ids;
        } catch (SQLException | DatabaseConnectionException e) {
            throw new CategoryDatabaseException("Impossibile recuperare le categorie dell'utente.");
        } finally {
            try {
                if (resultSet != null)
                    resultSet.close();

                if (statement != null)
                    statement.close();

                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                // non fare niente
            }
        }
    }

}