package Utilities.Tree;

import java.util.EventListener;

/**
 * L'interfaccia per i listener che vogliono essere avvertiti quando viene selezionato o deselezionato un elemento in un albero.
 */
public interface CustomTreeItemSelectionListener<T extends Object> extends EventListener {

    /**
     * Invocato quando viene selezionato un elemento dall'albero.
     * 
     * @param selectedItem
     *            elemento selezionato
     */
    public void onTreeItemSelection(T selectedItem);

    /**
     * Invocato quando vengono deselezionati gli elementi dell'albero.
     */
    public void onTreeItemDeselection();
}