package Utilities.Table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * Implementazione di default per mostrare una tabella con elementi dello stesso tipo.
 */
public abstract class CustomTableModel<T extends Object> extends AbstractTableModel {

    private ArrayList<T> items = new ArrayList<>();

    /**
     * Crea un nuovo modello.
     */
    public CustomTableModel() {
        super();
    }

    /**
     * Restituisce una lista contenente i nomi delle colonne della tabella.
     * 
     * @return colonne della tabella
     */
    public abstract List<String> getColumns();

    @Override
    public String getColumnName(int index) {
        if (index >= 0 && index < getColumnCount())
            return getColumns().get(index);
        else
            return super.getColumnName(index);
    }

    @Override
    public int getColumnCount() {
        return getColumns().size();
    }

    @Override
    public int getRowCount() {
        return items.size();
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    /**
     * Imposta gli elementi da mostrare.
     * <p>
     * Se {@code items == null}, ha lo stesso effetto di {@code clear()}
     * 
     * @param items
     *            elementi da mostrare
     */
    public void setItems(Collection<? extends T> items) {
        this.items.clear();

        if (items != null)
            this.items.addAll(items);

        fireTableDataChanged();
    }

    /**
     * Rimuove l'elemento presente nella riga indicata.
     * 
     * @param row
     *            riga da rimuovere
     * @throws IndexOutOfBoundsException
     *             se {@code row < 0 || row > getRowCount()}
     */
    public void removeAt(int row) {
        items.remove(row);
        fireTableRowsDeleted(row, row);
    }

    /**
     * Recupera l'elemento presenta alla riga indicata.
     * 
     * @param row
     *            riga dell'elemnento da recuperare
     * @throws IndexOutOfBoundsException
     *             se {@code row < 0 || row > getRowCount()}
     * @return elemento da recuperare
     */
    public T getAt(int row) {
        return items.get(row);
    }

    /**
     * Rimuove tutti gli elementi dal modello.
     */
    public void clear() {
        items.clear();
        fireTableDataChanged();
    }

}