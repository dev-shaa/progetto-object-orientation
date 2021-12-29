import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.*;
import java.awt.*;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
// import java.awt.GridLayout;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 * Classe che si occupa di mostrare un pannello con le categorie dell'utente
 * sotto forma di albero, con ogni nodo che rappresenta una categoria.
 * 
 * @version 0.1
 * @author Salvatore Di Gennaro
 * @see MainWindow
 */
public class ReferencePanel extends JPanel {

    ArrayList<Riferimento> riferimenti;
    DefaultTableModel referenceTableModel;
    DefaultTableModel referencePreviewTableModel;

    public ReferencePanel() {
        riferimenti = new ArrayList<Riferimento>(); // FIXME: prendi dal database

        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBorder(new EmptyBorder(5, 5, 5, 5));

        Dimension spacingAreaSize = new Dimension(0, 10);

        add(getButtonsPanel());
        add(Box.createRigidArea(spacingAreaSize));
        add(getReferenceListPanel());
        add(Box.createRigidArea(spacingAreaSize));
        add(getReferenceViewPanel());
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

        JButton deleteReferenceButton = new JButton(new ImageIcon("images/folder_delete.png"));
        deleteReferenceButton.setToolTipText("Elimina riferimento");
        deleteReferenceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteReference();
            }
        });

        JTextField searchBox = new JTextField();
        searchBox.setToolTipText("Cerca riferimento per nome, autore o parole chiave");
        searchBox.setMaximumSize(new Dimension(300, Integer.MAX_VALUE));
        searchBox.addActionListener(new ActionListener() {
            // viene triggerato quando si preme enter mentre è selezionato
            public void actionPerformed(ActionEvent e) {
                searchReferences(searchBox.getText());
            }
        });

        JButton searchButton = new JButton(new ImageIcon("images/search.png"));
        searchButton.setToolTipText("Cerca riferimento per nome, autore o parole chiave");
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchReferences(searchBox.getText());
            }
        });

        buttonsPanel.add(createReferenceButton);
        buttonsPanel.add(editReferenceButton);
        buttonsPanel.add(deleteReferenceButton);
        buttonsPanel.add(searchBox);
        buttonsPanel.add(searchButton);

        return buttonsPanel;
    }

    private JScrollPane getReferenceListPanel() {
        // TODO: gestisci colonne
        String[] referenceTableColumns = { "Nome", "Autori", "Data pubblicazione" };

        referenceTableModel = new DefaultTableModel(referenceTableColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        updateReferences();

        // https://stackoverflow.com/questions/10128064/jtable-selected-row-click-event

        JTable referenceTable = new JTable(referenceTableModel);
        referenceTable.setFillsViewportHeight(true);
        referenceTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        referenceTable.setAutoCreateRowSorter(true); // FIXME: eccezione out of bounds quando si cambia l'ordine mentre
                                                     // è selezionata una riga
        referenceTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                displayReference(referenceTable.getSelectedRow(), referencePreviewTableModel);
            }
        });

        JScrollPane referencePanel = new JScrollPane(referenceTable);

        return referencePanel;
    }

    private JScrollPane getReferenceViewPanel() {
        // due colonne: a sinistra il tipo di informazione, a destra il dato
        // "Titolo" : "Progetto"
        // "Autore" : "Mario Rossi"

        referencePreviewTableModel = new DefaultTableModel(0, 2) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable referenceViewTable = new JTable(referencePreviewTableModel);
        referenceViewTable.setTableHeader(null);
        referenceViewTable.setFillsViewportHeight(true);

        JScrollPane referencePreviewPanel = new JScrollPane(referenceViewTable);

        return referencePreviewPanel;
    }

    private void addReferenceToPanel(DefaultTableModel model, Riferimento riferimento) {
        model.addRow(new Object[] { riferimento.nome, riferimento.autore, riferimento.data });
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

    private void searchReferences(String input) {
        // TODO: implementa ricerca

        String[] words = input.split("");
    }

    private void updateReferences() {
        clearTable(referenceTableModel);

        for (Riferimento riferimento : riferimenti) {
            addReferenceToPanel(referenceTableModel, riferimento);
        }
    }

    private void displayReference(int index, DefaultTableModel tableModel) {
        clearTable(tableModel);

        // TODO: implementa
        tableModel.addRow(new Object[] { "nome", riferimenti.get(index).nome });
        tableModel.addRow(new Object[] { "autore", riferimenti.get(index).autore });
        tableModel.addRow(new Object[] { "anno", riferimenti.get(index).data });
    }

    private void clearTable(DefaultTableModel tableModel) {
        while (tableModel.getRowCount() > 0)
            tableModel.removeRow(0);
    }

}
