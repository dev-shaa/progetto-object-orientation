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
 * @version 0.3
 * @author Salvatore Di Gennaro
 * @see CategoryDAO
 */
public class CategoryDAOPostgreSQL extends CategoryDAO {

    private String categoryTableName = ""; // TODO: sistema

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
            // TODO: implementa
        } catch (Exception e) {
            throw new CategoryDatabaseException("Impossibile aggiungere nuova categoria.");
        }
    }

    @Override
    public void updateCategory(Category category, String name) throws CategoryDatabaseException {
        try {
            // TODO: implementa
        } catch (Exception e) {
            throw new CategoryDatabaseException("Impossibile modificare questa categoria.");
        }
    }

    @Override
    public void deleteCategory(Category category) throws CategoryDatabaseException {
        try {
            // TODO: implementa
            // Connection connection = DBController.getInstance().getConnection();

            // Statement statement = connection.createStatement();
            // String query = "";
            // statement.execute(query);

            // statement.close();
            // connection.close();
        } catch (Exception e) {
            throw new CategoryDatabaseException("Impossibile rimuovere questa categoria.");
        }
    }

    @Override
    public ArrayList<Category> getUserCategories() throws CategoryDatabaseException {
        try {
            ArrayList<Category> list = new ArrayList<Category>();

            // TODO: implementa

            return list;
        } catch (Exception e) {
            throw new CategoryDatabaseException("Impossibile recuperare le categorie dell'utente.");
        }
    }

}
