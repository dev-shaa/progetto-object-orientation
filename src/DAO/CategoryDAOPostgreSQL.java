import java.util.ArrayList;
import java.util.HashMap;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Implementazione dell'interfaccia CategoryDAO per database relazionali
 * PostgreSQL.
 * 
 * @version 0.3
 * @author Salvatore Di Gennaro
 * @see CategoryDAO
 */
public class CategoryDAOPostgreSQL extends CategoryDAO {

    private final String nameKey = "name";
    private final String parentKey = "parent";

    /**
     * Crea {@code CategoryDAOPostgreSQL} per interfacciarsi al database PostgreSQL relativo alle categorie dell'utente.
     * 
     * @param user
     *            l'utente che accede al database
     * @throws IllegalArgumentException
     *             se l'utente di input è {@code null}
     */
    public CategoryDAOPostgreSQL(User user) {
        super(user);
    }

    @Override
    public void addCategory(Category category) throws CategoryDatabaseException {
        try {
            Connection connection = DatabaseController.getInstance().getConnection();
            Statement statement = connection.createStatement();

            // TODO: implementa

            statement.close();
            DatabaseController.getInstance().closeConnection(connection);
        } catch (Exception e) {
            throw new CategoryDatabaseException("Impossibile aggiungere nuova categoria.");
        }
    }

    @Override
    public void changeCategory(Category category, String name) throws CategoryDatabaseException {
        try {
            Connection connection = DatabaseController.getInstance().getConnection();
            Statement statement = connection.createStatement();

            // TODO: implementa

            statement.close();
            DatabaseController.getInstance().closeConnection(connection);
        } catch (Exception e) {
            throw new CategoryDatabaseException("Impossibile modificare questa categoria.");
        }
    }

    @Override
    public void removeCategory(Category category) throws CategoryDatabaseException {
        try {
            Connection connection = DatabaseController.getInstance().getConnection();
            Statement statement = connection.createStatement();

            // TODO: implementa

            statement.close();
            DatabaseController.getInstance().closeConnection(connection);
        } catch (Exception e) {
            throw new CategoryDatabaseException("Impossibile rimuovere questa categoria.");
        }
    }

    @Override
    public Category[] getUserCategories() throws CategoryDatabaseException {

        // DEBUG: solo per testing
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

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseController.getInstance().getConnection();
            statement = connection.createStatement();
            String query = ""; // TODO: implementa
            resultSet = statement.executeQuery(query);

            ArrayList<Category> categories = new ArrayList<Category>(0);
            HashMap<String, Category> nameToCategory = new HashMap<String, Category>();

            while (resultSet.next()) {
                Category category = new Category(resultSet.getString(nameKey));
                categories.add(category);
                nameToCategory.put(category.getName(), category);
            }

            resultSet.first();

            for (Category category : categories) {
                String parentCategoryName = resultSet.getString(parentKey);
                category.setParent(nameToCategory.get(parentCategoryName));
                resultSet.next();
            }

            return categories.toArray(new Category[categories.size()]);
        } catch (Exception e) {
            throw new CategoryDatabaseException("Impossibile recuperare le categorie dell'utente.");
        } finally {
            try {
                resultSet.close();
                statement.close();
                connection.close();
            } catch (Exception e) {
                // non fare niente
            }
        }
    }

}
