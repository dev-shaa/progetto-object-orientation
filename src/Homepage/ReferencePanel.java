import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.awt.FlowLayout;
import java.util.ArrayList;

/**
 * Classe che si occupa di mostrare i riferimenti cercati o presenti in una categoria.
 * 
 * @version 0.4.5
 * @author Salvatore Di Gennaro
 * @see Homepage
 */
public class ReferencePanel extends JPanel {

    private Controller controller;
    private User user;

    private ArrayList<BibliographicReference> displayedReferences;
    private DefaultTableModel referencesTableModel;
    private JTable referencesTable;
    private JTextArea referenceDetails;
    private int selectedReferenceIndex;

    private JButton editReferenceButton;
    private JButton deleteReferenceButton;

    /**
     * Crea {@code ReferencePanel} con i dati relativi all'utente.
     * 
     * @param user
     * @since 0.1
     */
    public ReferencePanel(Controller controller, User user) {
        this.controller = controller;
        this.user = user;
        displayedReferences = new ArrayList<BibliographicReference>();

        setLayout(new BorderLayout(5, 5));
        setBorder(new EmptyBorder(5, 5, 5, 5));

        add(getButtonsPanel(), BorderLayout.NORTH);

        JSplitPane referenceSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, getReferenceListPane(), getReferenceDetailsPane());
        referenceSplitPane.setDividerSize(10);
        referenceSplitPane.setResizeWeight(0.6f);

        add(referenceSplitPane, BorderLayout.CENTER);
    }

    private JPanel getButtonsPanel() {
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));

        JButton createReferenceButton = new JButton(new ImageIcon("images/file_add.png"));
        createReferenceButton.setToolTipText("Nuovo riferimento");
        createReferenceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createReference();
            }
        });

        editReferenceButton = new JButton(new ImageIcon("images/file_edit.png"));
        editReferenceButton.setToolTipText("Modifica riferimento");
        editReferenceButton.setEnabled(false);
        editReferenceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editSelectedReference();
            }
        });

        deleteReferenceButton = new JButton(new ImageIcon("images/file_remove.png"));
        deleteReferenceButton.setToolTipText("Elimina riferimento");
        deleteReferenceButton.setEnabled(false);
        deleteReferenceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeSelectedReference();
            }
        });

        buttonsPanel.add(createReferenceButton);
        buttonsPanel.add(editReferenceButton);
        buttonsPanel.add(deleteReferenceButton);

        return buttonsPanel;
    }

    private JScrollPane getReferenceListPane() {
        String[] referenceListTableColumns = { "Nome", "Autori", "Data pubblicazione", "Citazioni ricevute" };

        referencesTableModel = new DefaultTableModel(referenceListTableColumns, 0) {
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
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting()) {
                    selectedReferenceIndex = referencesTable.getSelectedRow();

                    editReferenceButton.setEnabled(!isSelectedRowNull());
                    deleteReferenceButton.setEnabled(!isSelectedRowNull());

                    displaySelectedReferenceDetails();
                }
            }
        });

        JScrollPane referenceListPane = new JScrollPane(referencesTable);

        return referenceListPane;
    }

    private JScrollPane getReferenceDetailsPane() {
        referenceDetails = new JTextArea();
        referenceDetails.setEditable(false);

        JScrollPane referencePreviewPanel = new JScrollPane(referenceDetails);

        return referencePreviewPanel;
    }

    private void createReference() {
        controller.openReferenceCreatorPage();

        // DEBUG:
        addReferenceToTable(new BibliographicReference("nome", "autore"));
    }

    private void editSelectedReference() {
        // TODO: apri pagina di creazione del riferimento (passando i valori attuali del riferimento)
        // controller.openReferenceCreatorPage(riferimento);
    }

    private void removeSelectedReference() {
        try {
            int result = JOptionPane.showConfirmDialog(null, "Vuoi eliminare questo riferimento?", "Elimina riferimento", JOptionPane.YES_NO_OPTION);

            if (result == JOptionPane.YES_OPTION) {

                // TODO: cancella dal database

                displayedReferences.remove(selectedReferenceIndex);
                referencesTableModel.removeRow(selectedReferenceIndex);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Impossibile eliminare il riferimento");
        }
    }

    /**
     * Imposta i riferimenti da mostrare nell'elenco.
     * 
     * @param references
     *            i riferimenti da mostrare
     */
    public void setReferences(ArrayList<BibliographicReference> references) {
        referencesTable.clearSelection();
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

        // DEBUG:
        ArrayList<BibliographicReference> foo = new ArrayList<BibliographicReference>();
        foo.add(new BibliographicReference("aaa", "autore1"));
        foo.add(new BibliographicReference("bbb", "autore2"));
        foo.add(new BibliographicReference("ccc", "autore3"));

        setReferences(foo);
    }

    /**
     * Aggiunge un riferimento all'attuale elenco.
     * 
     * @param reference
     *            riferimento da aggiungere
     */
    public void addReferenceToTable(BibliographicReference reference) {
        displayedReferences.add(reference);
        referencesTableModel.addRow(new Object[] { reference.name, reference.author, reference.data });
    }

    private boolean isSelectedRowNull() {
        return selectedReferenceIndex == -1;
    }

    private void clearTable() {
        while (referencesTableModel.getRowCount() > 0)
            referencesTableModel.removeRow(0);
    }

    private void displaySelectedReferenceDetails() {
        String textToDisplay = selectedReferenceIndex == -1 ? "" : displayedReferences.get(selectedReferenceIndex).toString();

        referenceDetails.setText(textToDisplay);
    }

}
