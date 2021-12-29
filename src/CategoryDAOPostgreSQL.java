import java.util.ArrayList;

public class CategoryDAOPostgreSQL implements CategoryDAO {

    public CategoryDAOPostgreSQL() {
    }

    @Override
    public void saveCategory(Category category) throws Exception {
        // TODO: Auto-generated method stub
    }

    @Override
    public void updateCategory(Category category, String name) throws Exception {
        // TODO Auto-generated method stub

    }

    // TODO: vedi se convertirlo ad array semplice
    @Override
    public ArrayList<Category> getAllCategory(User user) throws Exception {
        ArrayList<Category> categories = new ArrayList<Category>();

        // DEBUG:
        categories.add(new Category("AAA"));
        categories.add(new Category("BBB"));
        categories.add(new Category("CCC"));

        // TODO: Auto-generated method stub
        return categories;
    }

}
