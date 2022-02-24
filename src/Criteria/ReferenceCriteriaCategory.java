package Criteria;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import Entities.Category;
import Entities.References.BibliographicReference;

/**
 * Implementazione di {@code ReferenceCriteria} per filtrare i riferimenti presenti in una categoria.
 */
public class ReferenceCriteriaCategory implements ReferenceCriteria {

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
    public ArrayList<BibliographicReference> filter(Collection<? extends BibliographicReference> references) {
        if (references == null)
            return null;

        ArrayList<BibliographicReference> filteredReferences = new ArrayList<>();

        for (BibliographicReference reference : references) {
            if (isReferenceContainedInCategory(reference))
                filteredReferences.add(reference);
        }

        filteredReferences.trimToSize();

        return filteredReferences;
    }

    private boolean isReferenceContainedInCategory(BibliographicReference reference) {
        List<Category> categories = reference.getCategories();
        return (category == null && categories.isEmpty()) || categories.contains(category);
    }

}