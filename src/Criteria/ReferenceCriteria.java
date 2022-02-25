package Criteria;

import java.util.ArrayList;
import java.util.Collection;
import Entities.References.BibliographicReference;

/**
 * Classe astratta per implementare il Criteria Pattern sui riferimenti.
 * https://www.tutorialspoint.com/design_pattern/filter_pattern.htm
 */
public abstract class ReferenceCriteria {

    /**
     * Restituisce una lista di riferimenti che corrispondono ai criteri di filtri.
     * 
     * @param references
     *            riferimenti da filtrare
     * @return una lista di elementi filtrati
     */
    public ArrayList<BibliographicReference> filter(Collection<? extends BibliographicReference> references) {
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

    /**
     * Controlla se il riferimento corrisponde ai filtri.
     * 
     * @param reference
     *            riferimento da controllare
     * @return {@code true} se il riferimento corrisponde ai filtri
     */
    protected abstract boolean doesReferenceMatch(BibliographicReference reference);
}