package Utilities.Criteria;

import java.util.List;

import Entities.Category;
import Entities.References.BibliographicReference;

/**
 * Implementazione di {@code ReferenceCriteria} per filtrare i riferimenti presenti in una categoria.
 */
public class ReferenceCriteriaCategory extends Criteria<BibliographicReference> {

    private Category category;

    /**
     * Crea un nuovo filtro con la categoria indicata.
     * 
     * @param category
     *            categoria con cui filtrare i riferimenti
     */
    public ReferenceCriteriaCategory(Category category) {
        this.category = category;
    }

    @Override
    protected boolean doesItemMatch(BibliographicReference reference) {
        List<Category> categories = reference.getCategories();
        return (category == null && categories.isEmpty()) || categories.contains(category);
    }

}