package Utilities.Tree;

import java.util.EventListener;

public interface CustomTreeItemSelectionListener<T extends Object> extends EventListener {

    public void onTreeItemSelection(T selectedItem);

    public void onTreeItemDeselection();
}
