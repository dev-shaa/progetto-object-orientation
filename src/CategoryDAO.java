// PLACEHOLDER

import java.util.ArrayList;

public interface CategoryDAO {

    // TODO: decidi se lasciarlo interfaccia o abstract class
    // TODO: decidi se renderlo singleton

    public void saveCategory(Category category) throws Exception;

    public void updateCategory(Category category, String name) throws Exception;

    public ArrayList<Category> getAllCategory(User user) throws Exception;
}
