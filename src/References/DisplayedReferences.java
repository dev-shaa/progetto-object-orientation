import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 * Classe che si occupa di gestire la tabella dei riferimenti mostrati.
 * 
 * @version 0.9
 * @author Salvatore Di Gennaro
 */
public class DisplayedReferences {

    private Controller controller;

    private ArrayList<BibliographicReference> displayedReferences;
    private DefaultTableModel displayedReferencesTableModel;

    /**
     * Crea {@code DisplayedReferences}.
     * 
     * @param controller
     *            controller dell'applicazione
     * @since 0.9
     * @throws IllegalArgumentException
     *             se {@code controller == null}
     */
    public DisplayedReferences(Controller controller) throws IllegalArgumentException {
        if (controller == null)
            throw new IllegalArgumentException("controller non può essere null.");

        this.controller = controller;
        this.displayedReferences = new ArrayList<BibliographicReference>();

        String[] referenceListTableColumns = { "Nome", "Autori", "Data pubblicazione", "Citazioni ricevute" };
        displayedReferencesTableModel = new DefaultTableModel(referenceListTableColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    /**
     * Restituisce il table model dei riferimenti.
     * 
     * @return
     *         il table model dei riferimenti
     */
    public DefaultTableModel getDisplayedReferencesModel() {
        return displayedReferencesTableModel;
    }

    /**
     * Restituisce il riferimento che si trova in posizione {@code index}.
     * 
     * @param modelIndex
     *            indice nel table model del riferimento voluto
     * @return
     *         riferimento di indice {@code index}
     * @throws IndexOutOfBoundsException
     *             se l'indice è fuori dagli estremi dell'array dei riferimenti ({@code index < 0 || index >= displayedReferences.size()})
     */
    public BibliographicReference getReference(int modelIndex) throws IndexOutOfBoundsException {
        return displayedReferences.get(modelIndex);
    }

    /**
     * Apre la pagina di creazione di un riferimento.
     */
    public void addReference() {
        controller.openReferenceCreatorPage();
    }

    /**
     * Apre la pagina di modifica di un riferimento.
     * 
     * @param modelIndex
     *            indice nel table model del riferimento da modificare
     * @throws IndexOutOfBoundsException
     *             se l'indice è fuori dagli estremi dell'array dei riferimenti ({@code index < 0 || index >= displayedReferences.size()})
     */
    public void changeReference(int modelIndex) throws IndexOutOfBoundsException {
        controller.openReferenceCreatorPage(displayedReferences.get(modelIndex));
    }

    /**
     * Rimove un riferimento dal database e dalla tabella.
     * 
     * @param modelIndex
     *            indice nel table model del riferimento da modificare
     * @throws IndexOutOfBoundsException
     *             se l'indice è fuori dagli estremi del\l'array dei riferimenti ({@code index < 0 || index >= size()})
     */
    public void removeReference(int modelIndex) throws IndexOutOfBoundsException {
        // TODO: rimuovi riferimento dal database

        displayedReferences.remove(modelIndex);
        displayedReferencesTableModel.removeRow(modelIndex);
    }

    /**
     * Imposta i riferimenti da mostrare nell'elenco.
     * 
     * @param references
     *            i riferimenti da mostrare
     */
    public void setReferences(List<BibliographicReference> references) {
        clearTable();

        for (BibliographicReference riferimento : references) {
            addReferenceToTable(riferimento);
        }
    }

    /**
     * Imposta i riferimenti da mostrare nell'elenco, caricandoli dalla categoria di input.
     * 
     * @param category
     *            categoria in cui cercare i riferimenti
     * @param user
     *            utente che ha eseguito l'accesso
     */
    public void setReferences(Category category) {
        // TODO: carica riferimenti dal database
    }

    /**
     * Aggiunge un riferimento all'attuale elenco.
     * 
     * @param reference
     *            riferimento da aggiungere
     */
    public void addReferenceToTable(BibliographicReference reference) {
        displayedReferences.add(reference);
        displayedReferencesTableModel.addRow(new Object[] { reference.getTitle(), reference.getAuthors(), reference.getPubblicationDate().toString() });
    }

    /**
     * Rimuove tutte le righe dalla tabella.
     */
    public void clearTable() {
        displayedReferences = new ArrayList<BibliographicReference>();

        while (displayedReferencesTableModel.getRowCount() > 0)
            displayedReferencesTableModel.removeRow(0);
    }

}
