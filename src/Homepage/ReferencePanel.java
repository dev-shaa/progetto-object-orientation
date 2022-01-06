import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.FlowLayout;

/**
 * Classe che si occupa di mostrare i riferimenti cercati o presenti in una categoria.
 * 
 * @version 0.5
 * @see Homepage
 */
public class ReferencePanel extends JPanel {

    private Controller controller;

    private DisplayedReferences displayedReferences;
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
    public ReferencePanel(Controller controller) {
        this.controller = controller;
        this.displayedReferences = new DisplayedReferences(controller);

        setLayout(new BorderLayout(5, 5));
        setBorder(new EmptyBorder(5, 5, 5, 5));

        add(getButtonsPanel(), BorderLayout.NORTH);

        JSplitPane referenceSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, getReferenceListPane(), getReferenceDetailsPane());
        referenceSplitPane.setDividerSize(10);
        referenceSplitPane.setResizeWeight(0.6f);

        add(referenceSplitPane, BorderLayout.CENTER);
    }

    public DisplayedReferences getDisplayedReferences() {
        return this.displayedReferences;
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
        referencesTable = new JTable(displayedReferences.getDisplayedReferencesModel());
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
        displayedReferences.addReference();
    }

    private void editSelectedReference() {
        displayedReferences.changeReference(selectedReferenceIndex);
    }

    private void removeSelectedReference() {
        try {
            int result = JOptionPane.showConfirmDialog(null, "Vuoi eliminare questo riferimento?", "Elimina riferimento", JOptionPane.YES_NO_OPTION);

            if (result == JOptionPane.YES_OPTION) {
                displayedReferences.removeReference(selectedReferenceIndex);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Impossibile eliminare il riferimento");
        }
    }

    private boolean isSelectedRowNull() {
        return selectedReferenceIndex == -1;
    }

    private void displaySelectedReferenceDetails() {
        String textToDisplay = selectedReferenceIndex == -1 ? "" : displayedReferences.getReference(selectedReferenceIndex).getFormattedDetails();

        referenceDetails.setText(textToDisplay);
    }

}
