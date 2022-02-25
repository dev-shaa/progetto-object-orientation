package GUI.Categories;

import java.util.EventListener;

import Entities.Category;

/**
 * Interfaccia per i listener che vogliono essere avvertiti quando viene selezionata una categoria.
 */
public interface CategorySelectionListener extends EventListener {

    /**
     * Invocato quando viene selezionata una categoria.
     * 
     * @param selectedCategory
     *            categoria selezionata
     */
    public void onCategorySelection(Category selectedCategory);

    /**
     * Invocato quando viene deselezionata una categoria.
     */
    public void onCategoryDeselection();
}