package Criteria;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Classe per filtrare una collezione di elementi in base a un filtro.
 */
public abstract class Criteria<T extends Object> {

    /**
     * Restituisce una lista di elementi che corrispondono ai filtri.
     * 
     * @param items
     *            elementi da filtrare
     * @return lista di elementi filtrati (assicurato non nulla)
     */
    public ArrayList<T> filter(Collection<? extends T> items) {
        if (items == null)
            return new ArrayList<>(0);

        ArrayList<T> filteredItems = new ArrayList<>();

        for (T item : items) {
            if (doesItemMatch(item))
                filteredItems.add(item);
        }

        filteredItems.trimToSize();
        return filteredItems;
    }

    /**
     * Controlla se un elemento corrisponde ai filtri.
     * 
     * @param item
     *            elemento da controllare
     * @return {@code true} se l'elemento corrisponde
     */
    protected abstract boolean doesItemMatch(T item);
}