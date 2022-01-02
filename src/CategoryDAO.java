// import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Interfaccia che si occupa di gestire la parte di database
 * relativo alle categorie.
 * 
 * @version 0.2
 * @author Salvatore Di Gennaro
 */
public interface CategoryDAO {

    /**
     * Salva una nuova categoria nel database.
     * 
     * @param category Nuova categoria da salvare.
     * @param user     Utente che ha creato la nuova categoria.
     * @throws Exception
     * @since 0.1
     */
    public void saveCategory(Category category, User user) throws Exception;

    /**
     * Modifica una categoria nel database.
     * 
     * @param category Categoria da modificare.
     * @param newName  Nuovo nome da assegnare alla categoria.
     * @throws Exception
     * @since 0.1
     */
    public void updateCategory(Category category, String newName) throws Exception;

    /**
     * Elimina una categoria nel database.
     * 
     * @param category Categoria da eliminare.
     * @throws Exception
     * @since 0.1
     */
    public void deleteCategory(Category category) throws Exception;

    /**
     * Ottiene tutte le categorie appartenenti a un utente nel database.
     * 
     * @param user Utente
     * @return Lista contenente tutte le categorie dell'utente.
     * @throws Exception
     * @since 0.2
     */
    public DefaultMutableTreeNode getUserCategoriesTree(User user) throws Exception;
}
