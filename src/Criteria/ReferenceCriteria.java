package Criteria;

import java.util.Collection;
import java.util.List;

import Entities.References.BibliographicReference;

/**
 * Interfaccia per implementare il Criteria Pattern sui riferimenti.
 * https://www.tutorialspoint.com/design_pattern/filter_pattern.htm
 */
public interface ReferenceCriteria {

    /**
     * Restituisce una lista di riferimenti che corrispondono ai criteri di filtri.
     * 
     * @param references
     *            riferimenti da filtrare
     * @return una lista di elementi filtrati
     */
    public List<BibliographicReference> filter(Collection<? extends BibliographicReference> references);

}