package GUI.References;

import Entities.References.*;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Pannello che mostra un elenco di riferimenti.
 * Per ogni riferimento vengono mostrati il titolo, gli autori, la data di pubblicazione e il numero di citazioni ricevute da altri riferimenti.
 */
public class ReferenceListPanel extends JScrollPane {

    private JTable referencesTable;
    private ReferenceTableModel referencesTableModel;

    private ArrayList<ReferenceSelectionListener> selectionListeners;

    /**
     * Crea un nuovo pannello.
     */
    public ReferenceListPanel() {
        referencesTableModel = new ReferenceTableModel();

        referencesTable = new JTable(referencesTableModel);
        referencesTable.setAutoCreateRowSorter(true);
        referencesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        referencesTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting())
                    notifyListeners();
            }
        });

        setViewportView(referencesTable);
    }

    /**
     * Imposta i riferimenti da mostrare nell'elenco.
     * 
     * @param references
     *            i riferimenti da mostrare (se {@code null} ha lo stesso effetto di {@link #clear()})
     */
    public void setReferences(Collection<? extends BibliographicReference> references) {
        referencesTableModel.setReferences(references);
    }

    /**
     * Rimuove il riferimento selezionato attualmente dalla tabella.
     */
    public void removeSelectedReference() {
        referencesTableModel.removeReferenceAt(getSelectedReferenceIndex());
    }

    /**
     * Restituisce il riferimento selezionato nella tabella.
     * 
     * @return
     *         il riferimento selezionato, {@code null} se non è selezionato niente
     */
    public BibliographicReference getSelectedReference() {
        try {
            return referencesTableModel.getReferenceAt(getSelectedReferenceIndex());
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * Rimuove tutte le righe dalla tabella.
     */
    public void clear() {
        referencesTableModel.clear();
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
