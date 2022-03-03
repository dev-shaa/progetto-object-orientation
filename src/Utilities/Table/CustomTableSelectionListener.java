package Utilities.Table;

import java.util.EventListener;

public interface CustomTableSelectionListener<T extends Object> extends EventListener {
    public void onTableItemSelection(T item);
}
