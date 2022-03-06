package Utilities.Criteria;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import Entities.Author;
import Entities.Category;
import Entities.Tag;
import Entities.References.BibliographicReference;

/**
 * Implementazione di {@code ReferenceCriteria} per filtrare i riferimenti corrispondenti a una ricerca.
 */
public class ReferenceCriteriaSearch extends Criteria<BibliographicReference> {

    private Date from;
    private Date to;
    private Collection<? extends Tag> tags;
    private Collection<? extends Author> authors;
    private Collection<? extends Category> categories;

    /**
     * Crea un nuovo filtro di ricerca per i riferimenti.
     * 
     * @param search
     *            ricerca con cui filtrare i riferimenti
     * @throws IllegalArgumentException
     *             se {@code search == null}
     */
    public ReferenceCriteriaSearch(Date from, Date to, Collection<? extends Tag> tags, Collection<? extends Category> categories, Collection<? extends Author> authors) {
        if (areAllTermsNull(from, to, tags, categories, authors))
            throw new IllegalArgumentException("Gli elementi di una ricerca non possono essere tutti nulli.");

        this.from = from;
        this.to = to;
        this.tags = tags;
        this.categories = categories;
        this.authors = authors;
    }

    @Override
    protected boolean doesItemMatch(BibliographicReference reference) {
        return wasReferencePublishedBetween(reference, from, to)
                && wasReferenceWrittenBy(reference, authors)
                && isReferenceTaggedWith(reference, tags)
                && isReferenceContainedIn(reference, categories);
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

    private boolean areAllTermsNull(Date from, Date to, Collection<? extends Tag> tags, Collection<? extends Category> categories, Collection<? extends Author> authors) {
        return from == null && to == null && (tags == null || tags.isEmpty()) && (categories == null || categories.isEmpty()) && (authors == null || authors.isEmpty());
    }

}