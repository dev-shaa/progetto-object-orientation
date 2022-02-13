package GUI.Utilities.Tree;

public interface TreePanelSelectionListener<T extends Object> {
    public void onObjectSelection(T object);

    public void onObjectDeselection();
}
