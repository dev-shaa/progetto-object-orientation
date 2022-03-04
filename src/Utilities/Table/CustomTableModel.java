package Utilities.Table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public abstract class CustomTableModel<T extends Object> extends AbstractTableModel {

    private ArrayList<T> items = new ArrayList<>();

    public CustomTableModel() {
        super();
    }

    @Override
    public String getColumnName(int index) {
        if (index >= 0 && index < getColumnCount())
            return getColumns().get(index);
        else
            return super.getColumnName(index);
    }

    @Override
    public int getRowCount() {
        return items.size();
    }

    @Override
    public int getColumnCount() {
        return getColumns().size();
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    public void set(Collection<? extends T> items) {
        this.items.clear();

        if (items != null)
            this.items.addAll(items);

        fireTableDataChanged();
    }

    public void remove(int row) {
        items.remove(row);
        fireTableRowsDeleted(row, row);
    }

    public T get(int row) {
        return items.get(row);
    }

    public void clear() {
        items.clear();
        fireTableDataChanged();
    }

    public abstract List<String> getColumns();

}