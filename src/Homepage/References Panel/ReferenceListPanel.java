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

        clearTable();

        for (BibliographicReference riferimento : references) {
            addReferenceToTable(riferimento);
        }
    }

    /**
     * Aggiunge un riferimento all'elenco attuale.
     * 
     * @param reference
     *            riferimento da aggiungere
     */
    public void addReferenceToTable(BibliographicReference reference) {
        displayedReferences.add(reference);
        referencesTableModel.addRow(new Object[] { reference.getTitle(), reference.getAuthors(), reference.getPubblicationDate() });
    }

    /**
     * Rimuove un riferimento dall'elenco attuale.
     * In caso il riferimento non fosse contenuto nell'elenco, non esegue nulla.
     * 
     * @param reference
     *            riferimento da rimuovere
     */
    public void removeReferenceFromTable(int modelIndex) throws IndexOutOfBoundsException {
        displayedReferences.remove(modelIndex);
        referencesTableModel.removeRow(modelIndex);
    }

    /**
     * Rimuove il riferimento selezionato attualmente dalla tabella.
     */
    public void removeSelectedReference() throws IndexOutOfBoundsException {
        // TODO: handle exception
        removeReferenceFromTable(getSelectedReferenceModelIndex());
    }

    /**
     * Rimuove tutte le righe dalla tabella.
     */
    public void clearTable() {
        if (displayedReferences != null)
            displayedReferences.clear();

        referencesTableModel.setRowCount(0);
    }

    /**
     * 
     * @param modelIndex
     * @return
     * @throws IndexOutOfBoundsException
     */
    public BibliographicReference getReference(int modelIndex) throws IndexOutOfBoundsException {
        return displayedReferences.get(modelIndex);
    }

    /**
     * Restituisce il riferimento selezionato nella tabella.
     * 
     * @return
     *         il riferimento selezionato, {@code null} se non è selezionato niente
     */
    public BibliographicReference getSelectedReference() {
        try {
            return displayedReferences.get(getSelectedReferenceModelIndex());
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * 
     * 
     * @return
     *         {@code true} se non è selezionata nessuna riga
     */
    public boolean isSelectedRowNull() {
        return referencesTable.getSelectedRow() == -1;
    }

    /**
     * 
     * @return
     * @throws IndexOutOfBoundsException
     */
    private int getSelectedReferenceModelIndex() throws IndexOutOfBoundsException {
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
