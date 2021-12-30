import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.FlowLayout;
import java.util.ArrayList;

/**
 * Classe che si occupa di mostrare i riferimenti presenti in una categoria.
 * 
 * @version 0.2
 * @author Salvatore Di Gennaro
 * @see MainWindow
 */
public class ReferencePanel extends JSplitPane {

    private User user;

    private ArrayList<Riferimento> referenceList;
    private DefaultTableModel referenceListTableModel;
    private JTextArea referenceDetailsArea;

    /**
     * Crea {@code ReferencePanel} con i dati relativi all'utente.
     * 
     * @param user
     * @since 0.1
     * @author Salvatore Di Gennaro
     */
    public ReferencePanel(User user) {
        // DEBUG:
        referenceList = new ArrayList<Riferimento>();
        referenceList.add(new Riferimento("aaa", "autore1"));
        referenceList.add(new Riferimento("bbb", "autore2"));
        referenceList.add(new Riferimento("ccc", "autore3"));
        referenceList.add(new Riferimento("ddd", "autore4"));

        // TODO: questo pannello è piccolo

        this.user = user;

        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBorder(new EmptyBorder(5, 5, 5, 5));

        Dimension spacingAreaSize = new Dimension(0, 10);

        add(getButtonsPanel());
        add(Box.createRigidArea(spacingAreaSize));
        add(getReferenceListPane());
        add(Box.createRigidArea(spacingAreaSize));
        add(getReferenceDetailsPane());
    }

    private JPanel getButtonsPanel() {
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
        buttonsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));

        JButton createReferenceButton = new JButton(new ImageIcon("images/file_add.png"));
        createReferenceButton.setToolTipText("Nuovo riferimento");
        createReferenceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createReference();
            }
        });

        JButton editReferenceButton = new JButton(new ImageIcon("images/file_edit.png"));
        editReferenceButton.setToolTipText("Modifica riferimento");
        editReferenceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editReference();
            }
        });

        JButton deleteReferenceButton = new JButton(new ImageIcon("images/file_remove.png"));
        deleteReferenceButton.setToolTipText("Elimina riferimento");
        deleteReferenceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteReference();
            }
        });

        // ActionListener searchListener = new ActionListener() {
        // public void actionPerformed(ActionEvent e) {
        // // TODO: cerca
        // // searchReferences(searchBox.getText());
        // }
        // };

        // JTextField searchBox = new JTextField();
        // searchBox.setToolTipText("Cerca riferimento per nome, autore o parole
        // chiave");
        // searchBox.setMaximumSize(new Dimension(300, Integer.MAX_VALUE));
        // searchBox.addActionListener(searchListener);

        // JButton searchButton = new JButton(new ImageIcon("images/search.png"));
        // searchButton.setToolTipText("Cerca riferimento per nome, autore o parole
        // chiave");
        // searchButton.addActionListener(searchListener);

        buttonsPanel.add(createReferenceButton);
        buttonsPanel.add(editReferenceButton);
        buttonsPanel.add(deleteReferenceButton);
        // buttonsPanel.add(searchBox);
        // buttonsPanel.add(searchButton);

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

        JTable referenceListTable = new JTable(referenceListTableModel);
        referenceListTable.setFillsViewportHeight(true);
        referenceListTable.setAutoCreateRowSorter(true); // FIXME: eccezione out of bounds quando si cambia l'ordine
                                                         // mentre è selezionata una riga
        referenceListTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        referenceListTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                displayReferenceDetails(referenceListTable.getSelectedRow());
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
        // TODO: apri pagina di creazione del riferimento
    }

    private void editReference() {
        // TODO: apri pagina di creazione del riferimento (passando i valori attuali del
        // riferimento)
    }

    private void deleteReference() {
        int result = JOptionPane.showConfirmDialog(null, "Vuoi eliminare questo riferimento?",
                "Elimina riferimento",
                JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            try {
                // TODO: cancella dal database

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Impossibile eliminare il riferimento");
            }
        }
    }

    public void searchReferences() {
        // TODO: implementa
    }

    /**
     * Carica nel pannello della lista tutti i riferimenti presenti nella categoria
     * di input.
     * 
     * @param category categoria in cui cercare i riferimenti
     * @param user     utente che ha eseguito l'accesso
     */
    public void loadReferencesListFromCategory(Category category, User user) {
        // TODO: carica riferimenti dal database
        // riferimenti = RiferimentoDAO.load();

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
        // model.addRow(new Object[] { riferimento.nome, riferimento.autore,
        // riferimento.data });
    }

    private void displayReferenceDetails(int selectedReferenceIndex) {
        // TODO: implementa
    }

}
