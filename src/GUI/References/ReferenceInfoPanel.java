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
     * Se {@code reference == null}, verrà mostrata una schermata vuota.
     * 
     * @param reference
     *            riferimento da mostrare
     */
    public void setReference(BibliographicReference reference) {
        if (reference == null) {
            details.setRowCount(0);
            return;
        }

        List<BibliographicReferenceField> referenceInfo = reference.getReferenceFields();

        details.setRowCount(referenceInfo.size());

        for (int i = 0; i < referenceInfo.size(); i++) {
            details.setValueAt(referenceInfo.get(i).getName(), i, 0);
            details.setValueAt(referenceInfo.get(i).getValue(), i, 1);
        }
    }

}
