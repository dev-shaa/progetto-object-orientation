package DAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Controller.DatabaseController;
import Entities.References.BibliographicReference;
import Entities.*;
import Exceptions.*;

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
    public void save(Category category) throws CategoryDatabaseException {
        if (category == null)
            throw new IllegalArgumentException("category can't be null");

        Connection connection = null;
        Statement statement = null;

        try {
            connection = DatabaseController.getConnection();

            // il database genera, per ogni nuova categoria, un ID unico
            // se non siamo in grado di recuperarlo e assegnarlo alla categoria, annulliamo tutta l'operazione
            connection.setAutoCommit(false);

            statement = connection.createStatement();

            String parentID = category.getParent() == null ? "null" : String.valueOf(category.getParent().getID());

            String query = "insert into category(name, parent, owner) values('" + category.getName() + "', " + parentID + ", '" + getUser().getName() + "')";

            // il database genera un ID per ogni categoria, quindi vogliamo aggiornare la
            // classe category prima di concludere ogni operazione
            statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);

            ResultSet set = statement.getGeneratedKeys();

            if (set.next()) {
                category.setID(set.getInt(1));
            }

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
     *             se {@code category == null}
     */
    @Override
    public void update(Category category) throws CategoryDatabaseException {
        if (category == null)
            throw new IllegalArgumentException("category can't be null");

        Connection connection = null;
        Statement statement = null;

        try {
            connection = DatabaseController.getConnection();
            statement = connection.createStatement();

            String query = "update category set name = '" + category.getName() + "' where id = " + category.getID();

            statement.executeUpdate(query);
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
     *             se {@code category == null}
     */
    @Override
    public void remove(Category category) throws CategoryDatabaseException {
        if (category == null)
            throw new IllegalArgumentException("category can't be null");

        Connection connection = null;
        Statement statement = null;

        try {
            connection = DatabaseController.getConnection();
            statement = connection.createStatement();

            String query = "delete from category where id = " + category.getID();

            statement.executeUpdate(query);
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
        Statement statement = null;
        ResultSet resultSet = null;
        String query = "select * from category where owner = '" + getUser().getName() + "'";

        try {
            connection = DatabaseController.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            HashMap<Integer, Category> idToCategory = new HashMap<>();
            HashMap<Category, Integer> nodeToParentID = new HashMap<>();

            while (resultSet.next()) {
                Category category = new Category(resultSet.getString("name"), resultSet.getInt("id"));
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
     *             se {@code reference == null}
     */
    @Override
    public List<Integer> getIDs(BibliographicReference reference) throws CategoryDatabaseException {
        if (reference == null)
            throw new IllegalArgumentException("reference can't be null");

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseController.getConnection();
            statement = connection.createStatement();
            String query = "select category from category_reference_association where reference = " + reference.getID();
            resultSet = statement.executeQuery(query);

            ArrayList<Integer> ids = new ArrayList<>();

            while (resultSet.next()) {
                ids.add(resultSet.getInt("category"));
            }

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