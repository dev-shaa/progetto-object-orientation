package GUI.References;

import java.util.ArrayList;
import java.util.Collection;
import javax.swing.table.AbstractTableModel;
import Entities.References.BibliographicReference;

/**
 * {@code TableModel} non modificabile per gestire i riferimenti.
 * <p>
 * Ha quattro colonne, indicanti il titolo, gli autori, la data di pubblicazione e le citazioni ricevute dei riferimenti.
 */
public class ReferenceTableModel extends AbstractTableModel {

    private ArrayList<BibliographicReference> references;
    private final String[] columnNames = { "Titolo", "Autori", "Data di pubblicazione", "Citazioni ricevute" };

    /**
     * Crea un nuovo modello vuoto.
     */
    public ReferenceTableModel() {
        this(null);
    }

    /**
     * Crea un nuovo modello inserendo i riferimenti passati.
     * 
     * @param references
     *            riferimenti da mostrare
     */
    public ReferenceTableModel(Collection<? extends BibliographicReference> references) {
        super();
        this.references = new ArrayList<>();
        setReferences(references);
    }

    @Override
    public String getColumnName(int column) {
        if (column >= 0 && column < columnNames.length)
            return columnNames[column];
        else
            return super.getColumnName(column);
    }

    @Override
    public int getRowCount() {
        return references.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int row, int column) {
        BibliographicReference reference = getReferenceAt(row);

        switch (column) {
            case 0:
                return reference.getTitle();
            case 1:
                return reference.getAuthorsAsString();
            case 2:
                return reference.getFormattedDate();
            case 3:
                return reference.getQuotationCount();
            default:
                return null;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    /**
     * Imposta i riferimenti da mostrare nella tabella.
     * <p>
     * Se {@code references == null}, ha lo stesso effetto di {@link #clear()}.
     * 
     * @param references
     *            riferimenti da mostrare
     */
    public void setReferences(Collection<? extends BibliographicReference> references) {
        this.references.clear();

        if (references != null)
            this.references.addAll(references);

        fireTableDataChanged();
    }

    /**
     * Rimuove il riferimento presente all'indice di riga indicato.
     * 
     * @param row
     *            indice della riga da rimuovere
     * @throws IndexOutOfBoundsException
     *             se l'indice di riga non è compreso tra gli estremi della tabella
     */
    public void removeReferenceAt(int row) {
        references.remove(row);
        fireTableRowsDeleted(row, row);
    }

    /**
     * Restituisce il riferimento presente all'indice di riga indicato.
     * 
     * @param row
     *            indice della riga del riferimento da recuperare
     * @return riferimento alla riga indicata
     * @throws IndexOutOfBoundsException
     *             se l'indice di riga non è compreso tra gli estremi della tabella
     */
    public BibliographicReference getReferenceAt(int row) {
        return references.get(row);
    }

    /**
     * Rimuove tutti i riferimenti dalla tabella.
     */
    public void clear() {
        references.clear();
        fireTableDataChanged();
    }

}
