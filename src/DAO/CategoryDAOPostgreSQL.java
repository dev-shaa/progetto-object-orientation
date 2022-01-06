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
    public void addCategory(Category category, User user) throws CategoryDatabaseException {
        try {
            // Connection connection = DBController.getInstance().getConnection();

            // Statement statement = connection.createStatement();
            // String query = "insert into " + categoryTableName + " values ("; // TODO:
            // correggi
            // statement.execute(query);

            // statement.close();
            // connection.close();
        } catch (Exception e) {
            throw new CategoryDatabaseException("Impossibile aggiungere nuova categoria.");
        }
    }

    @Override
    public void updateCategory(Category category, String name) throws CategoryDatabaseException {
        // TODO: Auto-generated method stub
        try {
            // Connection connection = DBController.getInstance().getConnection();

            // Statement statement = connection.createStatement();
            // String query = ""; // TODO: correggi
            // statement.execute(query);

            // statement.close();
            // connection.close();
        } catch (Exception e) {
            throw new CategoryDatabaseException("Impossibile modificare questa categoria.");
        }
    }

    @Override
    public void deleteCategory(Category category) throws CategoryDatabaseException {
        // TODO: Auto-generated method stub
        try {
            // Connection connection = DBController.getInstance().getConnection();

            // Statement statement = connection.createStatement();
            // String query = ""; // TODO: correggi
            // statement.execute(query);

            // statement.close();
            // connection.close();
        } catch (Exception e) {
            throw new CategoryDatabaseException("Impossibile rimuovere questa categoria.");
        }
    }

    @Override
    public ArrayList<Category> getUserCategories(User user) throws CategoryDatabaseException {

        // https://www.java-success.com/00-%E2%99%A6-creating-tree-list-flattening-back-list-java/

        try {

            // DEBUG:
            ArrayList<Category> list = new ArrayList<Category>();
            Category cat1 = new Category("category1", null);
            Category cat2 = new Category("category2", null);
            Category cat3 = new Category("category3", null);
            Category cat4 = new Category("category4", cat1);
            Category cat5 = new Category("category5", cat4);
            Category cat6 = new Category("category6", cat2);
            list.add(cat1);
            list.add(cat2);
            list.add(cat3);
            list.add(cat4);
            list.add(cat5);
            list.add(cat6);
            return list;

            // Connection connection = DBController.getInstance().getConnection();

            // Statement statement = connection.createStatement();
            // String query = "";
            // ResultSet resultSet = statement.executeQuery(query);

            // DefaultMutableTreeNode root = new DefaultMutableTreeNode("Tutte le categorie");

            // while (resultSet.next()) {
            // // TODO:
            // }

            // statement.close();
            // connection.close();

            // return root;
        } catch (Exception e) {
            throw new CategoryDatabaseException("Impossibile recuperare le categorie dell'utente.");
        }
    }

}
