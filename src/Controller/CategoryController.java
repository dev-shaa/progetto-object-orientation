package Controller;

import DAO.CategoryDAO;
import Entities.Category;
import GUI.Utilities.CustomTreeModel;
import GUI.Utilities.CustomTreeNode;

public class CategoryController {
    private CategoryDAO categoryDAO;
    private CustomTreeModel<Category> categoryTree;

    public CategoryController(CategoryDAO categoryDAO) {
        this.setCategoryDAO(categoryDAO);
    }

    public CategoryDAO getCategoryDAO() {
        return categoryDAO;
    }

    public void setCategoryDAO(CategoryDAO categoryDAO) {
        if (categoryDAO == null)
            throw new IllegalArgumentException("categoryDAO can't be null");

        this.categoryDAO = categoryDAO;

        try {
            this.categoryTree = categoryDAO.getUserCategories();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public CustomTreeModel<Category> getCategoriesTree() {
        return categoryTree;
    }

    public void addCategory(String name, CustomTreeNode<Category> parent) {
        // TODO:
    }

    public void changeCategory(CustomTreeNode<Category> category, String newName) {

    }

    public void removeCategory(CustomTreeNode<Category> category) {

    }

}
