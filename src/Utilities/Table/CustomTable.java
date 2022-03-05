package Utilities.Table;

import java.util.Collection;
import javax.swing.JTable;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Un'estensione di {@code JTable} per mostrare elementi di uno stesso tipo.
 */
public class CustomTable<T extends Object> extends JTable {

    private CustomTableModel<T> tableModel;
    private EventListenerList itemSelectionListeners = new EventListenerList();

    /**
     * Crea una nuova tabella.
     * 
     * @param tableModel
     *            modello della tabella da usare
     */
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

    /**
     * Imposta il modello di tabella da usare.
     * 
     * @param tableModel
     *            nuovo modello da usare
     */
    public void setModel(CustomTableModel<T> tableModel) {
        // FIXME: tableModel == null ?

        super.setModel(tableModel);
        this.tableModel = tableModel;
    }

    /**
     * Imposta gli elementi da mostrare.
     * 
     * @param items
     *            elementi da mostrare
     */
    public void setItems(Collection<? extends T> items) {
        tableModel.setItems(items);
    }

    /**
     * Rimuove l'elemento selezionato dalla tabella.
     * <p>
     * Non esegue nulla se non è selezionato un elemento.
     */
    public void removeSelectedItem() {
        try {
            tableModel.removeAt(getSelectedIndex());
        } catch (IndexOutOfBoundsException e) {
            // non fare nulla
        }
    }

    /**
     * Restituisce l'elemento attualmente selezionato, se c'è.
     * 
     * @return elemento selezionato
     */
    public T getSelectedItem() {
        try {
            return tableModel.getAt(getSelectedIndex());
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * Rimuove tutti gli elementi della tabella.
     */
    public void clear() {
        tableModel.clear();
    }

    /**
     * Aggiunge un ascoltatore all'evento di selezione di un elemento.
     * 
     * @param listener
     *            ascoltatore da aggiungere
     */
    public void addItemSelectionListener(CustomTableSelectionListener<T> listener) {
        itemSelectionListeners.add(CustomTableSelectionListener.class, listener);
    }

    /**
     * Rimuove un ascoltatore dall'evento di deselezione di un elemento.
     * 
     * @param listener
     *            ascoltatore da rimuovere
     */
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