import java.util.ArrayList;

public interface CategoryDAO {

    // TODO: decidi se lasciarlo interfaccia o abstract class
    // TODO: decidi se renderlo singleton

    public void saveCategory(Category category);

    public ArrayList<Category> getAllCategory(User user);
}
