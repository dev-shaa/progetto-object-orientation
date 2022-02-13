package GUI.References;

import Entities.References.*;
import java.util.ArrayList;
import java.util.Collection;

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
public class ReferenceListPanel extends JScrollPane {

    private JTable referencesTable;
    private DefaultTableModel referencesTableModel;

    private ArrayList<BibliographicReference> displayedReferences;
    private ArrayList<ReferenceSelectionListener> selectionListeners;

    /**
     * Crea un {@code ReferenceListPanel} vuoto.
     */
    public ReferenceListPanel() {
        String[] tableColumns = { "Titolo", "Autori", "Data di pubblicazione", "Citazione ricevute" };

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
        referencesTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting())
                    return;

                notifyListeners();
            }

        });

        setViewportView(referencesTable);
    }

    /**
     * Imposta i riferimenti da mostrare nell'elenco.
     * 
     * @param references
     *            i riferimenti da mostrare (se {@code null} non viene mostrato nulla)
     */
    public void showReferences(Collection<? extends BibliographicReference> references) {
        clear();

        if (references == null)
            return;

        if (displayedReferences == null)
            displayedReferences = new ArrayList<>();

        displayedReferences.addAll(references);

        showDisplayedReferences();
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
     * Rimuove il riferimento selezionato attualmente dalla tabella.
     */
    public void removeSelectedReference() {
        displayedReferences.remove(getSelectedReferenceIndex());
        referencesTableModel.removeRow(getSelectedReferenceIndex());
    }

    /**
     * Rimuove tutte le righe dalla tabella.
     */
    public void clear() {
        if (displayedReferences != null)
            displayedReferences.clear();

        referencesTableModel.setRowCount(0);
    }

    /**
     * Ricarica il pannello, aggiornando le informazioni che vengono mostrate.
     */
    public void reload() {
        showDisplayedReferences();
    }

    /**
     * Aggiunge un listener all'evento di selezione di un riferimento.
     * Se {@code listener == null} o se è già registrato all'evento, non succede niente.
     * 
     * @param listener
     *            listener da aggiungere
     */
    public void addReferenceSelectionListener(ReferenceSelectionListener listener) {
        if (listener == null)
            return;

        if (selectionListeners == null)
            selectionListeners = new ArrayList<>();

        if (selectionListeners.contains(listener))
            return;

        selectionListeners.add(listener);
    }

    /**
     * Rimuove un listener dall'evento di selezione di un riferimento.
     * 
     * @param listener
     *            listener da rimuovere
     */
    public void removeReferenceSelectionListener(ReferenceSelectionListener listener) {
        if (listener == null || selectionListeners == null)
            return;

        selectionListeners.remove(listener);
    }

    private void showDisplayedReferences() {
        if (displayedReferences == null)
            return;

        referencesTableModel.setRowCount(displayedReferences.size());

        for (int i = 0; i < displayedReferences.size(); i++) {
            BibliographicReference reference = displayedReferences.get(i);

            referencesTableModel.setValueAt(reference.getTitle(), i, 0);
            referencesTableModel.setValueAt(reference.getAuthorsAsString(), i, 1);
            referencesTableModel.setValueAt(reference.getFormattedDate(), i, 2);
            referencesTableModel.setValueAt(reference.getQuotationCount(), i, 3);
        }
    }

    private int getSelectedReferenceIndex() {
        return referencesTable.convertRowIndexToModel(referencesTable.getSelectedRow());
    }

    private void notifyListeners() {
        if (selectionListeners == null)
            return;

        for (ReferenceSelectionListener listener : selectionListeners) {
            listener.onReferenceSelection(getSelectedReference());
        }
    }

}
