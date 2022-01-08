import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ReferenceDisplayPanel extends JScrollPane {

    private JTable detailsTable;
    private DefaultTableModel detailsModel;

    public ReferenceDisplayPanel() {
        this.detailsModel = new DefaultTableModel(0, 2) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        this.detailsTable = new JTable(this.detailsModel);
        this.detailsTable.setTableHeader(null);

        setViewportView(detailsTable);
    }

    public void showReference(BibliographicReference reference) {
        if (reference == null) {
            detailsModel.setRowCount(0);
        } else {
            List<BibliographicReferenceField> referenceInfo = reference.getInfoAsStrings();

            detailsModel.setRowCount(referenceInfo.size());

            for (int i = 0; i < referenceInfo.size(); i++) {
                detailsModel.setValueAt(referenceInfo.get(i).getName(), i, 0);
                detailsModel.setValueAt(referenceInfo.get(i).getValue(), i, 1);
            }
        }
    }

}
