import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Implementazione dell'interfaccia CategoryDAO per database relazionali
 * PostgreSQL.
 * 
 * @version 0.1
 * @author Salvatore Di Gennaro
 * @see CategoryDAO
 */
public class CategoryDAOPostgreSQL implements CategoryDAO {

    private String categoryTableName = ""; // TODO: sistema

    public CategoryDAOPostgreSQL() {
    }

    @Override
    public void saveCategory(Category category, User user) throws Exception {
        try {
            Connection connection = DBController.getInstance().getConnection();

            Statement statement = connection.createStatement();
            String query = "insert into " + categoryTableName + " values ("; // TODO: correggi
            statement.execute(query);

            statement.close();
            connection.close();
        } catch (Exception e) {
            throw new Exception();
        }
    }

    @Override
    public void updateCategory(Category category, String name) throws Exception {
        // TODO: Auto-generated method stub
        try {
            Connection connection = DBController.getInstance().getConnection();

            Statement statement = connection.createStatement();
            String query = ""; // TODO: correggi
            statement.execute(query);

            statement.close();
            connection.close();
        } catch (Exception e) {
            throw new Exception();
        }
    }

    @Override
    public void deleteCategory(Category category) throws Exception {
        // TODO: Auto-generated method stub
        try {
            Connection connection = DBController.getInstance().getConnection();

            Statement statement = connection.createStatement();
            String query = ""; // TODO: correggi
            statement.execute(query);

            statement.close();
            connection.close();
        } catch (Exception e) {
            throw new Exception();
        }
    }

    // TODO: vedi se convertirlo ad array semplice
    @Override
    public ArrayList<Category> getAllUserCategory(User user) throws Exception {
        try {
            Connection connection = DBController.getInstance().getConnection();

            Statement statement = connection.createStatement();
            String query = "";
            ResultSet resultSet = statement.executeQuery(query);

            ArrayList<Category> categories = new ArrayList<Category>();

            while (resultSet.next()) {
                // categories.add(new Category(name, parent)); // FIXME:
            }

            // DEBUG:
            categories.add(new Category("AAA", null));
            categories.add(new Category("BBB", null));
            categories.add(new Category("CCC", null));

            connection.close();

            return categories;
        } catch (Exception e) {
            throw new Exception();
        }
    }
}
