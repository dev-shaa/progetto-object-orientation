package GUI.References.List;

import java.util.ArrayList;
import java.util.List;

import Entities.References.BibliographicReference;
import Utilities.Table.CustomTableModel;

public class ReferenceTableModel extends CustomTableModel<BibliographicReference> {

    private ArrayList<String> columns;

    public ReferenceTableModel() {
        columns = new ArrayList<>(4);
        columns.add("Titolo");
        columns.add("Autori");
        columns.add("Data di pubblicazione");
        columns.add("Citazioni ricevute");
    }

    @Override
    public Object getValueAt(int row, int column) {
        BibliographicReference reference = get(row);

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