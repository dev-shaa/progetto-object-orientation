package Criteria;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import Entities.Author;
import Entities.Category;
import Entities.Search;
import Entities.Tag;
import Entities.References.BibliographicReference;

/**
 * TODO: commenta
 */
public class ReferenceCriteriaSearch implements ReferenceCriteria {

    private Search search;

    /**
     * Crea un nuovo filtro di ricerca per i riferimenti.
     * 
     * @param search
     *            ricerca con cui filtrare i riferimenti
     * @throws IllegalArgumentException
     *             se {@code search == null}
     */
    public ReferenceCriteriaSearch(Search search) {
        if (search == null)
            throw new IllegalArgumentException("search can't be null");

        this.search = search;
    }

    @Override
    public ArrayList<BibliographicReference> get(Collection<? extends BibliographicReference> references) {
        if (references == null)
            return null;

        ArrayList<BibliographicReference> filteredReferences = new ArrayList<>();

        for (BibliographicReference reference : references) {
            if (doesReferenceMatch(reference))
                filteredReferences.add(reference);
        }

        filteredReferences.trimToSize();
        return filteredReferences;
    }

    private boolean wasReferencePublishedAfter(BibliographicReference reference, Date date) {
        if (date == null)
            return true;

        if (reference.getPubblicationDate() == null)
            return false;

        return reference.getPubblicationDate().after(date);
    }

    private boolean wasReferencePublishedBefore(BibliographicReference reference, Date date) {
        if (date == null)
            return true;

        if (reference.getPubblicationDate() == null)
            return false;

        return reference.getPubblicationDate().before(date);
    }

    private boolean wasReferencePublishedBetween(BibliographicReference reference, Date start, Date end) {
        return wasReferencePublishedAfter(reference, start) && wasReferencePublishedBefore(reference, end);
    }

    private boolean wasReferenceWrittenBy(BibliographicReference reference, Collection<? extends Author> authors) {
        if (authors == null || authors.isEmpty())
            return true;

        return reference.getAuthors().containsAll(authors);
    }

    private boolean isReferenceTaggedWith(BibliographicReference reference, Collection<? extends Tag> tags) {
        if (tags == null || tags.isEmpty())
            return true;

        return reference.getTags().containsAll(tags);
    }

    private boolean isReferenceContainedIn(BibliographicReference reference, Collection<? extends Category> categories) {
        List<Category> referenceCategories = reference.getCategories();
        return (categories == null && referenceCategories.isEmpty()) || referenceCategories.containsAll(categories);
    }

    private boolean doesReferenceMatch(BibliographicReference reference) {
        return wasReferencePublishedBetween(reference, search.getFrom(), search.getTo())
                && wasReferenceWrittenBy(reference, search.getAuthors())
                && isReferenceTaggedWith(reference, search.getTags())
                && isReferenceContainedIn(reference, search.getCategories());
    }

}
