package RetrieveManagement.DAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.sql.*;

import Entities.*;
import RetrieveManagement.Connections.ConnectionController;
import RetrieveManagement.Connections.CustomConnection;

/**
 * Implementazione dell'interfaccia CategoryDAO per database relazionali PostgreSQL.
 * 
 * @see CategoryDAO
 */
public class CategoryDAOPostgreSQL implements CategoryDAO {

    private String username;

    /**
     * Crea una nuova classe DAO per interfacciarsi al database PostgreSQL relativo alle categorie dell'utente.
     * 
     * @param username
     *            il nome dell'utente di cui si devono gestire i riferimenti
     */
    public CategoryDAOPostgreSQL(String username) {
        this.username = username;
    }

    @Override
    public void save(Category category) throws SQLException {
        if (category == null)
            return;

        CustomConnection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        String parentID = category.getParent() == null ? null : String.valueOf(category.getParent().getID());
        String command = "insert into category(name, parent, owner) values(?," + parentID + ",?)";

        try {
            connection = ConnectionController.getInstance().getConnection();

            // il database genera, per ogni nuova categoria, un ID unico
            // se non siamo in grado di recuperarlo e assegnarlo alla categoria, annulliamo tutta l'operazione
            connection.setAutoCommit(false);

            statement = connection.prepareStatement(command, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, category.getName());
            statement.setString(2, username);
            statement.executeUpdate();

            resultSet = statement.getGeneratedKeys();

            if (resultSet.next())
                category.setID(resultSet.getInt(1));

            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            if (resultSet != null)
                resultSet.close();

            if (statement != null)
                statement.close();

            if (connection != null)
                connection.close();
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @throws IllegalArgumentException
     *             se {@code category == null} o se {@code category} non ha un ID
     */
    @Override
    public void update(Category category) throws SQLException {
        if (category == null)
            throw new IllegalArgumentException("category can't be null");

        if (category.getID() == null)
            throw new IllegalArgumentException("category doesn't have an ID");

        CustomConnection connection = null;
        PreparedStatement statement = null;
        String command = "update category set name = ? where id = ?";

        try {
            connection = ConnectionController.getInstance().getConnection();
            statement = connection.prepareStatement(command);

            statement.setString(1, category.getName());
            statement.setInt(2, category.getID());

            statement.executeUpdate();
        } finally {
            if (statement != null)
                statement.close();

            if (connection != null)
                connection.close();
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @throws IllegalArgumentException
     *             se {@code category == null} o se {@code category} non ha un ID
     */
    @Override
    public void remove(Category category) throws SQLException {
        if (category == null)
            throw new IllegalArgumentException("category can't be null");

        if (category.getID() == null)
            throw new IllegalArgumentException("category doesn't have an ID");

        CustomConnection connection = null;
        Statement statement = null;
        String command = "delete from category where id = " + category.getID();

        try {
            connection = ConnectionController.getInstance().getConnection();
            statement = connection.createStatement();
            statement.executeUpdate(command);
        } finally {
            if (statement != null)
                statement.close();

            if (connection != null)
                connection.close();
        }
    }

    @Override
    public List<Category> getAll() throws SQLException {
        CustomConnection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = "select * from category where owner = ?";

        try {
            connection = ConnectionController.getInstance().getConnection();
            statement = connection.prepareStatement(query);
            statement.setString(1, username);
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
        } finally {
            if (resultSet != null)
                resultSet.close();

            if (statement != null)
                statement.close();

            if (connection != null)
                connection.close();
        }
    }

    @Override
    public List<Integer> getCategoriesIDFor(int referenceID) throws SQLException {
        CustomConnection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        String query = "select category from reference_grouping where reference = " + referenceID;

        try {
            connection = ConnectionController.getInstance().getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            ArrayList<Integer> ids = new ArrayList<>();

            while (resultSet.next())
                ids.add(resultSet.getInt("category"));

            ids.trimToSize();
            return ids;
        } finally {
            if (resultSet != null)
                resultSet.close();

            if (statement != null)
                statement.close();

            if (connection != null)
                connection.close();
        }
    }

}