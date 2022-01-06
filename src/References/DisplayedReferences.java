import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

/**
 * Classe che si occupa di gestire i riferimenti mostrati.
 */
public class DisplayedReferences {

    private Controller controller;

    private ArrayList<BibliographicReference> displayedReferences;
    private DefaultTableModel displayedReferencesTableModel;

    /**
     * Crea {@code DisplayedReferences}.
     * 
     * @param controller
     */
    public DisplayedReferences(Controller controller) {
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
     * @param index
     *            indice del riferimento voluto
     * @return
     *         riferimento di indice {@code index}
     * @throws IndexOutOfBoundsException
     *             se l'indice è fuori dagli estremi dell'array dei riferimenti ({@code index < 0 || index >= size()})
     */
    public BibliographicReference getReference(int index) throws IndexOutOfBoundsException {
        try {
            return displayedReferences.get(index);
        } catch (IndexOutOfBoundsException e) {
            throw e;
        }
    }

    /**
     * Apre la pagina di creazione di un riferimento.
     */
    public void createReference() {
        controller.openReferenceCreatorPage();
    }

    /**
     * Apre la pagina di modifica di un riferimento.
     * 
     * @param index
     *            indice del riferimento da modificare
     * @throws IndexOutOfBoundsException
     *             se l'indice è fuori dagli estremi dell'array dei riferimenti ({@code index < 0 || index >= size()})
     */
    public void editReference(int index) throws IndexOutOfBoundsException {
        try {
            // TODO: apri pagina di creazione del riferimento (passando i valori attuali del riferimento)
            // controller.openReferenceCreatorPage(riferimento);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    /**
     * Rimove un riferimento dal database e dalla tabella.
     * 
     * @param index
     *            indice del riferimento da modificare
     * @throws IndexOutOfBoundsException
     *             se l'indice è fuori dagli estremi dell'array dei riferimenti ({@code index < 0 || index >= size()})
     */
    public void removeReference(int index) throws IndexOutOfBoundsException {
        try {
            // TODO: rimuovi riferimento dal database

            displayedReferences.remove(index);
            displayedReferencesTableModel.removeRow(index);
        } catch (IndexOutOfBoundsException e) {
            throw e;
        }
    }

    /**
     * Imposta i riferimenti da mostrare nell'elenco.
     * 
     * @param references
     *            i riferimenti da mostrare
     */
    public void setReferences(ArrayList<BibliographicReference> references) {
        displayedReferences = new ArrayList<BibliographicReference>();

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
        displayedReferencesTableModel.addRow(new Object[] { reference.name, reference.author, reference.date });
    }

    private void clearTable() {
        while (displayedReferencesTableModel.getRowCount() > 0)
            displayedReferencesTableModel.removeRow(0);
    }
}
