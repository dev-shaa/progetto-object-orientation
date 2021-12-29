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

public class ReferencePanel extends JPanel {

    ArrayList<Riferimento> riferimenti;
    DefaultTableModel referenceTableModel;

    public ReferencePanel() {
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
        // String[] referenceTableColumns = { "Nome", "Autore", "Data pubblicazione" };

        // referenceTableModel = new DefaultTableModel(referenceTableColumns, 0) {
        // @Override
        // public boolean isCellEditable(int row, int column) {
        // return false;
        // }
        // };

        // updateReferences();

        // //
        // https://stackoverflow.com/questions/10128064/jtable-selected-row-click-event

        // JTable referenceTable = new JTable(referenceTableModel);
        // referenceTable.setFillsViewportHeight(true);
        // referenceTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // referenceTable.setAutoCreateRowSorter(true); // FIXME: eccezione out of
        // bounds quando si cambia l'ordine mentre
        // // è selezionata una riga
        // referenceTable.getSelectionModel().addListSelectionListener(new
        // ListSelectionListener() {
        // public void valueChanged(ListSelectionEvent event) {
        // displayReference(referenceTable.getSelectedRow(),
        // referencePreviewTableModel);
        // }
        // });

        // JScrollPane referencePanel = new JScrollPane(referenceTable);
        JScrollPane referencePanel = new JScrollPane();

        return referencePanel;
    }

    private JScrollPane getReferenceViewPanel() {
        String[] referencePreviewTableColumns = new String[2];
        DefaultTableModel referencePreviewTableModel = new DefaultTableModel(referencePreviewTableColumns, 0) {
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
        // TODO: implementa
    }

    private void editReference() {
        // TODO: implementa
    }

    private void deleteReference() {
        int result = JOptionPane.showConfirmDialog(null, "Vuoi eliminare questo riferimento?",
                "Elimina riferimento",
                JOptionPane.YES_NO_OPTION);

        if (result == 0) {
            // TODO: cancella dal database
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

        tableModel.addRow(new Object[] { "nome", riferimenti.get(index).nome });
        tableModel.addRow(new Object[] { "autore", riferimenti.get(index).autore });
        tableModel.addRow(new Object[] { "anno", riferimenti.get(index).data });
    }

    private void clearTable(DefaultTableModel tableModel) {
        while (tableModel.getRowCount() > 0)
            tableModel.removeRow(0);
    }

}
