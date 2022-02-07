package DAO;

import Controller.DatabaseController;

import Entities.*;
import Entities.References.BibliographicReference;
import Exceptions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    @Override
    public void save(Category category) throws CategoryDatabaseException {
        if (category == null)
            return;

        Connection connection = null;
        Statement statement = null;

        try {
            connection = DatabaseController.getConnection();

            // il database genera, per ogni nuova categoria, un ID unico
            // se non siamo in grado di recuperarlo e assegnarlo alla categoria, annulliamo tutta l'operazione
            connection.setAutoCommit(false);

            statement = connection.createStatement();

            String parentID = category.getParent() == null ? "null" : String.valueOf(category.getParent().getID());

            String query = "insert into category(name, parent, owner) values('" + category.getName() + "', " + parentID + ", '" + user.getName() + "')";

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

    @Override
    public void update(Category category) throws CategoryDatabaseException {
        if (category == null)
            return;

        Connection connection = null;
        Statement statement = null;

        try {
            connection = DatabaseController.getConnection();
            statement = connection.createStatement();

            String query = "update category set name = '" + category.getName() + "' where id = " + category.getID();

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

    @Override
    public void remove(Category category) throws CategoryDatabaseException {
        if (category == null)
            return;

        Connection connection = null;
        Statement statement = null;

        try {
            connection = DatabaseController.getConnection();
            statement = connection.createStatement();

            String query = "delete from category where id = " + category.getID();

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
    public List<Category> getAll() throws CategoryDatabaseException {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseController.getConnection();
            statement = connection.createStatement();
            String query = "select * from category where owner = '" + user.getName() + "'";
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

    @Override
    public List<Integer> getIDs(BibliographicReference reference) throws CategoryDatabaseException {
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