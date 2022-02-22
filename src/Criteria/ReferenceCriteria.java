package Criteria;

import java.util.Collection;
import java.util.List;

import Entities.References.BibliographicReference;

/**
 * TODO: commenta
 */
public interface ReferenceCriteria {

    /**
     * Restituisce una lista di riferimenti che corrispondono ai criteri di filtri.
     * 
     * @param references
     *            riferimenti da filtrare
     * @return una lista di elementi filtrati
     */
    public List<BibliographicReference> get(Collection<? extends BibliographicReference> references);

}
