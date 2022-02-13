package GUI.Utilities.Tree;

/**
 * Interfaccia per i listener che vogliono essere avvertiti quando viene selezionato un oggetto nel {@code TreePanel}.
 */
public interface TreePanelSelectionListener<T extends Object> {

    /**
     * Invocato quando viene selezionata un oggetto.
     * 
     * @param object
     *            oggetto selezionato
     */
    public void onTreePanelSelection(T object);

    /**
     * Invocato quando viene deselezionato tutto.
     */
    public void onTreePanelClearSelection();
}
