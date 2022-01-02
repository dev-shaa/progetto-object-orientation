import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.FlowLayout;
// import java.awt.GridLayout;
// import java.awt.GridBagLayout;
import java.util.ArrayList;

/**
 * Classe che si occupa di mostrare i riferimenti presenti in una categoria.
 * 
 * @version 0.3
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

    /**
     * Crea {@code ReferencePanel} con i dati relativi all'utente.
     * 
     * @param user
     * @since 0.1
     * @author Salvatore Di Gennaro
     */
    public ReferencePanel(User user) {
        this.user = user;
        referenceList = new ArrayList<Riferimento>();

        setLayout(new BorderLayout(5, 5));
        setBorder(new EmptyBorder(5, 5, 5, 5));

        JSplitPane referenceSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, getReferenceListPane(), getReferenceDetailsPane());
        referenceSplitPane.setDividerSize(10);
        referenceSplitPane.setResizeWeight(0.6f);

        add(getButtonsPanel(), BorderLayout.NORTH);
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

        JButton editReferenceButton = new JButton(new ImageIcon("images/file_edit.png")); // TODO: modifica icona
        editReferenceButton.setToolTipText("Modifica riferimento");
        editReferenceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editSelectedReference();
            }
        });

        JButton deleteReferenceButton = new JButton(new ImageIcon("images/file_remove.png"));
        deleteReferenceButton.setToolTipText("Elimina riferimento");
        deleteReferenceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteSelectedReference();
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
                selectedReferenceIndex = referenceListTable.getSelectedRow();

                displayReferenceDetails(selectedReferenceIndex);
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
    }

    private void editSelectedReference() {
        // TODO: apri pagina di creazione del riferimento (passando i valori attuali del
        // riferimento)
        // controller.openReferenceCreatorPage(riferimento);
    }

    private void deleteSelectedReference() {
        try {
            if (selectedReferenceIndex != -1) {
                int result = JOptionPane.showConfirmDialog(null, "Vuoi eliminare questo riferimento?", "Elimina riferimento", JOptionPane.YES_NO_OPTION);

                if (result == JOptionPane.YES_OPTION) {

                    // TODO: cancella dal database

                    referenceList.remove(selectedReferenceIndex);
                    referenceListTableModel.removeRow(selectedReferenceIndex);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Impossibile eliminare il riferimento");
        }
    }

    public void setReferences(ArrayList<Riferimento> references) {
        this.referenceList = references;

        removeAllReferencesFromTable(referenceListTableModel);

        for (Riferimento riferimento : referenceList) {
            addReferenceToTable(referenceListTableModel, riferimento);
        }
    }

    /**
     * Carica nel pannello della lista tutti i riferimenti presenti nella categoria
     * di input.
     * 
     * @param category
     *            categoria in cui cercare i riferimenti
     * @param user
     *            utente che ha eseguito l'accesso
     */
    public void loadReferencesListFromCategory(Category category, User user) {
        // TODO: carica riferimenti dal database

        // DEBUG:
        if (category != null) {
            referenceList = new ArrayList<Riferimento>();
            referenceList.add(new Riferimento("aaa " + category.getName(), "autore1"));
            referenceList.add(new Riferimento("bbb " + category.getName(), "autore2"));
            referenceList.add(new Riferimento("ccc " + category.getName(), "autore3"));
        }

        removeAllReferencesFromTable(referenceListTableModel);

        for (Riferimento riferimento : referenceList) {
            addReferenceToTable(referenceListTableModel, riferimento);
        }

    }

    private void removeAllReferencesFromTable(DefaultTableModel tableModel) {
        while (tableModel.getRowCount() > 0)
            tableModel.removeRow(0);
    }

    private void addReferenceToTable(DefaultTableModel model, Riferimento riferimento) {
        model.addRow(new Object[] { riferimento.nome, riferimento.autore,
                riferimento.data });
    }

    private void displayReferenceDetails(int selectedReferenceIndex) {
        // TODO: implementa
    }

}
