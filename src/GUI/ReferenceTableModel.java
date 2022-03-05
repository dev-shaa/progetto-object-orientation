package GUI;

import java.util.ArrayList;
import java.util.List;
import Entities.References.BibliographicReference;
import Utilities.Table.CustomTableModel;

/**
 * Modello di tabella per mostrare riferimenti bibliografici.
 * <p>
 * Mostra quattro colonne: titolo, autori, data di pubblicazione e numero di citazioni ricevute.
 */
public class ReferenceTableModel extends CustomTableModel<BibliographicReference> {

    private ArrayList<String> columns;

    /**
     * Crea un nuovo modello.
     */
    public ReferenceTableModel() {
        super();

        columns = new ArrayList<>(4);
        columns.add("Titolo");
        columns.add("Autori");
        columns.add("Data di pubblicazione");
        columns.add("Citazioni ricevute");
    }

    @Override
    public Object getValueAt(int row, int column) {
        BibliographicReference reference = getAt(row);

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
    public List<String> getColumns() {
        return columns;
    }

}