package GUI.References;

import Entities.References.*;
import java.util.List;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 * Pannello che mostra le informazioni relative a un riferimento bibliografico.
 */
public class ReferenceInfoPanel extends JScrollPane {

    private DefaultTableModel details;

    /**
     * Crea un pannello contenente una tabella composta da due colonne,
     * la prima contenente il nome dell'informazione e la seconda l'informazione stessa.
     */
    public ReferenceInfoPanel() {
        details = new DefaultTableModel(0, 2) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable detailsTable = new JTable(details);
        detailsTable.setTableHeader(null);
        detailsTable.setCellSelectionEnabled(true);
        detailsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        setViewportView(detailsTable);
    }

    /**
     * Imposta il riferimento bibliografico di cui mostrare i dettagli.
     * Se {@code reference == null}, ha lo stesso effetto di {@link #clear()}
     * 
     * @param reference
     *            riferimento da mostrare
     */
    public void showReference(BibliographicReference reference) {
        if (reference == null) {
            clear();
            return;
        }

        List<BibliographicReferenceField> referenceInfo = reference.getReferenceFields();

        details.setRowCount(referenceInfo.size());

        for (int i = 0; i < referenceInfo.size(); i++) {
            details.setValueAt(referenceInfo.get(i).getName(), i, 0);
            details.setValueAt(referenceInfo.get(i).getValue(), i, 1);
        }
    }

    /**
     * Rimuove il riferimento mostrato.
     */
    public void clear() {
        details.setRowCount(0);
    }

}
