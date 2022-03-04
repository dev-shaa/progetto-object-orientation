package Utilities.Table;

import java.util.Collection;
import javax.swing.JTable;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class CustomTable<T extends Object> extends JTable {

    private CustomTableModel<T> tableModel;
    private EventListenerList itemSelectionListeners = new EventListenerList();

    public CustomTable(CustomTableModel<T> tableModel) {
        super(tableModel);
        this.tableModel = tableModel;

        getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting())
                    fireItemSelectionEvent(getSelectedItem());
            }
        });
    }

    public void setModel(CustomTableModel<T> tableModel) {
        super.setModel(tableModel);
        this.tableModel = tableModel;
    }

    public void setItems(Collection<? extends T> items) {
        tableModel.set(items);
    }

    public void removeSelectedItem() {
        tableModel.remove(getSelectedIndex());
    }

    public T getSelectedItem() {
        try {
            return tableModel.get(getSelectedIndex());
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public void clear() {
        tableModel.clear();
    }

    public void addItemSelectionListener(CustomTableSelectionListener<T> listener) {
        itemSelectionListeners.add(CustomTableSelectionListener.class, listener);
    }

    public void removeItemSelectionListener(CustomTableSelectionListener<T> listener) {
        itemSelectionListeners.remove(CustomTableSelectionListener.class, listener);
    }

    private int getSelectedIndex() {
        return convertRowIndexToModel(getSelectedRow());
    }

    private void fireItemSelectionEvent(T selectedItem) {
        for (CustomTableSelectionListener<T> listener : getItemSelectionListeners()) {
            listener.onTableItemSelection(selectedItem);
        }
    }

    @SuppressWarnings("unchecked")
    private CustomTableSelectionListener<T>[] getItemSelectionListeners() {
        return (CustomTableSelectionListener<T>[]) itemSelectionListeners.getListeners(CustomTableSelectionListener.class);
    }

}