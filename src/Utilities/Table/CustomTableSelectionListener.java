package Utilities.Table;

import java.util.EventListener;

/**
 * L'interfaccia per i listener che vogliono essere avvertiti quando viene selezionato o deselezionato un elemento in una tabella.
 */
public interface CustomTableSelectionListener<T extends Object> extends EventListener {

    /**
     * Invocato quando viene selezionato o deselezionato un elemento della tabella.
     * 
     * @param item
     *            elemento selezionato ({@code null} se è avvenuta una deselezione)
     */
    public void onTableItemSelection(T item);
}
