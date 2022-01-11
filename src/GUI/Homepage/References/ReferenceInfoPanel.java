package GUI.Homepage.References;

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

    private DefaultTableModel detailsModel;

    /**
     * Crea un pannello contenente una tabella composta da due colonne,
     * la prima contenente il nome dell'informazione e la seconda l'informazione
     * stessa.
     */
    public ReferenceInfoPanel() {
        detailsModel = new DefaultTableModel(0, 2) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable detailsTable = new JTable(detailsModel);
        detailsTable.setTableHeader(null);
        detailsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        detailsTable.setCellSelectionEnabled(true);

        setViewportView(detailsTable);
    }

    /**
     * Imposta il riferimento bibliografico di cui mostrare i dettagli.
     * Se {@code reference == null}, verrà mostrata una schermata vuota.
     * 
     * @param reference
     *                  riferimento da mostrare
     */
    public void showReference(BibliographicReference reference) {
        if (reference == null) {
            detailsModel.setRowCount(0);
        } else {
            List<BibliographicReferenceField> referenceInfo = reference.getReferenceFields();

            detailsModel.setRowCount(referenceInfo.size());

            for (int i = 0; i < referenceInfo.size(); i++) {
                detailsModel.setValueAt(referenceInfo.get(i).getName(), i, 0);
                detailsModel.setValueAt(referenceInfo.get(i).getValue(), i, 1);
            }
        }
    }

}
