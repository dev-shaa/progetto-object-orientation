package Criteria;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import Entities.Search;
import Entities.References.BibliographicReference;

/**
 * TODO: commenta
 */
public class ReferenceCriteriaSearch implements ReferenceCriteria {

    private Search search;

    /**
     * TODO: commenta
     * 
     * @param search
     */
    public ReferenceCriteriaSearch(Search search) {
        setSearch(search);
    }

    public void setSearch(Search search) {
        if (search == null)
            throw new IllegalArgumentException("search can't be null");

        this.search = search;
    }

    public Search getSearch() {
        return search;
    }

    @Override
    public List<? extends BibliographicReference> get(Collection<? extends BibliographicReference> references) {
        if (references == null)
            return null;

        Predicate<BibliographicReference> searchFilter = e -> e.wasPublishedBetween(search.getFrom(), search.getTo())
                && e.wasWrittenBy(search.getAuthors())
                && e.isTaggedWith(search.getTags())
                && e.isContainedIn(search.getCategories());

        return references.stream().filter(searchFilter).collect(Collectors.toList());
    }

}
