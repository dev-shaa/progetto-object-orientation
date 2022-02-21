package Criteria;

import java.util.Collection;
import java.util.List;

import Entities.References.BibliographicReference;

/**
 * TODO: commenta
 */
public interface ReferenceCriteria {

    /**
     * TODO: commenta
     * 
     * @param references
     * @return
     */
    public List<? extends BibliographicReference> get(Collection<? extends BibliographicReference> references);
}
