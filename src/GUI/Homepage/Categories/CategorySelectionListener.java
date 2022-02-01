package GUI.Homepage.Categories;

import java.util.EventListener;
import Entities.Category;

/**
 * L'interfaccia per i listener che vogliono essere avvertiti quando viene selezionata una categoria.
 */
public interface CategorySelectionListener extends EventListener {

    /**
     * Invocato quando viene selezionata una categoria.
     * 
     * @param category
     *            categoria selezionata
     */
    public void onCategorySelection(Category category);

    /**
     * Invocato quando viene deselezionato tutto.
     */
    public void onCategoryClearSelection();
}
