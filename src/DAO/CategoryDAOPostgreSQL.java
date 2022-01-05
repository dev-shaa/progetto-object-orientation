import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.tree.DefaultMutableTreeNode;

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

    // TODO: vedi se convertirlo ad array semplice
    @Override
    public DefaultMutableTreeNode getUserCategories(User user) throws CategoryDatabaseException {

        // https://www.java-success.com/00-%E2%99%A6-creating-tree-list-flattening-back-list-java/

        // DEBUG:

        try {
            DefaultMutableTreeNode root = new DefaultMutableTreeNode();

            DefaultMutableTreeNode foo = new DefaultMutableTreeNode(new Category("AAA", null));

            root.add(foo);
            foo.add(new DefaultMutableTreeNode(new Category("BBB", null)));
            root.add(new DefaultMutableTreeNode(new Category("CCC", null)));

            return root;

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
