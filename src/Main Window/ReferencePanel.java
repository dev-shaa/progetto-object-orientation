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
 * @see MainWindow
 */
public class ReferencePanel extends JPanel {

    private Controller controller;
    private User user;

    private ArrayList<Riferimento> referenceList;
    private DefaultTableModel referenceListTableModel;
    private JTable referenceListTable;
    private JTextArea referenceDetailsArea;
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
        referenceList = new ArrayList<Riferimento>();

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

        referenceListTableModel = new DefaultTableModel(referenceListTableColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        referenceListTable = new JTable(referenceListTableModel);
        referenceListTable.setFillsViewportHeight(true);
        referenceListTable.setAutoCreateRowSorter(true);
        referenceListTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        referenceListTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting()) {
                    selectedReferenceIndex = referenceListTable.getSelectedRow();

                    editReferenceButton.setEnabled(!isSelectedRowNull());
                    deleteReferenceButton.setEnabled(!isSelectedRowNull());

                    displaySelectedReferenceDetails();
                }
            }
        });

        JScrollPane referenceListPane = new JScrollPane(referenceListTable);

        return referenceListPane;
    }

    private JScrollPane getReferenceDetailsPane() {
        referenceDetailsArea = new JTextArea();
        referenceDetailsArea.setEditable(false);

        JScrollPane referencePreviewPanel = new JScrollPane(referenceDetailsArea);

        return referencePreviewPanel;
    }

    private void createReference() {
        controller.openReferenceCreatorPage();

        // DEBUG:
        addReferenceToTable(new Riferimento("nome", "autore"));
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

                referenceList.remove(selectedReferenceIndex);
                referenceListTableModel.removeRow(selectedReferenceIndex);
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
    public void setReferences(ArrayList<Riferimento> references) {
        referenceListTable.clearSelection();
        referenceList = new ArrayList<Riferimento>();

        clearTable();

        for (Riferimento riferimento : references) {
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
        ArrayList<Riferimento> foo = new ArrayList<Riferimento>();
        foo.add(new Riferimento("aaa", "autore1"));
        foo.add(new Riferimento("bbb", "autore2"));
        foo.add(new Riferimento("ccc", "autore3"));

        setReferences(foo);
    }

    /**
     * Aggiunge un riferimento all'attuale elenco.
     * 
     * @param reference
     *            riferimento da aggiungere
     */
    public void addReferenceToTable(Riferimento reference) {
        referenceList.add(reference);
        referenceListTableModel.addRow(new Object[] { reference.nome, reference.autore, reference.data });
    }

    private boolean isSelectedRowNull() {
        return selectedReferenceIndex == -1;
    }

    private void clearTable() {
        while (referenceListTableModel.getRowCount() > 0)
            referenceListTableModel.removeRow(0);
    }

    private void displaySelectedReferenceDetails() {
        String textToDisplay = selectedReferenceIndex == -1 ? "" : referenceList.get(selectedReferenceIndex).toString();

        referenceDetailsArea.setText(textToDisplay);
    }

}
