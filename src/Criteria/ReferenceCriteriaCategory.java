package Criteria;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import Entities.Category;
import Entities.References.BibliographicReference;

/**
 * TODO: commenta
 */
public class ReferenceCriteriaCategory implements ReferenceCriteria {

    private Category category;

    public ReferenceCriteriaCategory(Category category) {
        setCategory(category);
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }

    @Override
    public List<? extends BibliographicReference> get(Collection<? extends BibliographicReference> references) {
        if (references == null)
            return null;

        Predicate<BibliographicReference> categoryFilter = e -> e.isContainedIn(category);
        return references.stream().filter(categoryFilter).collect(Collectors.toList());
    }
}
