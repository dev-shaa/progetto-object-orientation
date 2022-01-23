package GUI.Homepage.References;

import Entities.References.*;
import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 * Pannello che mostra una tabella in cui ogni riga mostra un riferimento.
 * Sono presenti dei pulsanti per aggiungere, modificare o rimuovere un
 * riferimento.
 * Per ogni riferimento vengono mostrati il titolo, gli autori, la data di
 * pubblicazione
 * e il numero di citazioni ricevute da altri riferimenti.
 * È possibile ordinare i riferimenti in base al dettaglio voluto.
 * 
 * @see ReferencePanel
 */
public class ReferenceListPanel extends JScrollPane implements ListSelectionListener {

    private JTable referencesTable;
    private DefaultTableModel referencesTableModel;

    private ArrayList<BibliographicReference> displayedReferences;
    private ArrayList<ReferenceListSelectionListener> selectionListeners;

    private final String titleLabel = "Titolo";
    private final String authorsLabel = "Autori";
    private final String pubblicationDateLabel = "Data di pubblicazione";
    private final String quotationCountLabel = "Citazione ricevute";

    /**
     * Crea un {@code ReferenceListPanel} vuoto.
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
        referencesTable.getSelectionModel().addListSelectionListener(this);

        setViewportView(referencesTable);
    }

    /**
     * Imposta i riferimenti da mostrare nell'elenco.
     * 
     * @param references
     *            i riferimenti da mostrare (se {@code references == null}
     *            non viene mostrato nulla)
     */
    public void setReferences(BibliographicReference[] references) {
        removeAllReferences();

        if (references == null)
            return;

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
    public void removeAllReferences() {
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
     * Restituisce l'indice del riferimento selezionato.
     * 
     * @return
     *         l'indice del riferimento selezionato
     * @throws IndexOutOfBoundsException
     *             se l'ordinamento è attivo e l'indice del
     *             riferimento selezionato si trova al di
     *             fuori degli estremi della tabella
     *             (non dovrebbe mai succedere)
     */
    private int getSelectedReferenceIndex() throws IndexOutOfBoundsException {
        return referencesTable.convertRowIndexToModel(referencesTable.getSelectedRow());
    }

    /**
     * Aggiunge un listener all'evento di selezione di un riferimento.
     * 
     * @param listener
     *            listener da aggiungere
     */
    public void addReferenceSelectionListener(ReferenceListSelectionListener listener) {
        if (listener == null)
            return;

        if (selectionListeners == null)
            selectionListeners = new ArrayList<>(5);

        selectionListeners.add(listener);
    }

    /**
     * Rimuove un listener dall'evento di selezione di un riferimento.
     * 
     * @param listener
     *            listener da rimuovere
     */
    public void removeReferenceSelectionListener(ReferenceListSelectionListener listener) {
        if (listener == null || selectionListeners == null)
            return;

        selectionListeners.remove(listener);
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() || selectionListeners == null)
            return;

        for (ReferenceListSelectionListener listener : selectionListeners) {
            listener.onReferenceSelection(getSelectedReference());
        }
    }

}
