import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 * 
 */
public class ReferenceListPanel extends JScrollPane {

    private JTable referencesTable;
    private DefaultTableModel referencesTableModel;

    private ArrayList<BibliographicReference> displayedReferences;

    private final String titleLabel = "Titolo";
    private final String authorsLabel = "Autori";
    private final String pubblicationDateLabel = "Data di pubblicazione";
    private final String quotationCountLabel = "Citazione ricevute";

    /**
     * 
     */
    public ReferenceListPanel() {
        String[] tableColumns = { titleLabel, authorsLabel, pubblicationDateLabel, quotationCountLabel };
        this.referencesTableModel = new DefaultTableModel(tableColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        displayedReferences = new ArrayList<>();

        referencesTable = new JTable(referencesTableModel);
        referencesTable.setFillsViewportHeight(true);
        referencesTable.setAutoCreateRowSorter(true);
        referencesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        setViewportView(referencesTable);
    }

    /**
     * Imposta i riferimenti da mostrare nell'elenco.
     * 
     * @param references
     *            i riferimenti da mostrare
     * @throws IllegalArgumentException
     *             se {@code references == null}
     */
    public void setReferences(List<BibliographicReference> references) throws IllegalArgumentException {
        if (references == null)
            throw new IllegalArgumentException("references non può essere null");

        removeAllReferences();

        for (BibliographicReference riferimento : references) {
            addReference(riferimento);
        }
    }

    /**
     * Aggiunge un riferimento all'elenco attuale.
     * 
     * @param reference
     *            riferimento da aggiungere
     */
    public void addReference(BibliographicReference reference) {
        displayedReferences.add(reference);
        referencesTableModel.addRow(new Object[] { reference.getTitle(), reference.getAuthorsAsString(), reference.getPubblicationDate() });
    }

    /**
     * Rimuove il riferimento selezionato attualmente dalla tabella.
     */
    public void removeSelectedReference() throws IndexOutOfBoundsException {
        displayedReferences.remove(getSelectedReferenceIndex());
        referencesTableModel.removeRow(getSelectedReferenceIndex());
    }

    /**
     * Rimuove tutte le righe dalla tabella.
     */
    private void removeAllReferences() {
        if (displayedReferences != null)
            displayedReferences.clear();

        referencesTableModel.setRowCount(0);
    }

    /**
     * Restituisce il riferimento selezionato nella tabella.
     * 
     * @return
     *         il riferimento selezionato, {@code null} se non è selezionato niente
     */
    public BibliographicReference getSelectedReference() {
        try {
            return displayedReferences.get(getSelectedReferenceIndex());
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * TODO: commenta
     * Restituisce l'indice del riferimento selezionato.
     * 
     * @return
     * 
     * @throws IndexOutOfBoundsException
     */
    private int getSelectedReferenceIndex() throws IndexOutOfBoundsException {
        return referencesTable.convertRowIndexToModel(referencesTable.getSelectedRow());
    }

    /**
     * Aggiunge un listener che viene avvertito ogni volta che la selezione della tabella cambia.
     * 
     * @param listener
     *            listener della selezione
     */
    public void addListSelectionListener(ListSelectionListener listener) {
        referencesTable.getSelectionModel().addListSelectionListener(listener);
    }

}
