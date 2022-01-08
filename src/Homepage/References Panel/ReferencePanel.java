import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.awt.FlowLayout;

// TODO: commenta

/**
 * Classe che si occupa di mostrare i riferimenti cercati o presenti in una categoria.
 * 
 * @version 0.6
 * @see Homepage
 * @see ReferenceListPanel
 * @see ReferenceInfoPanel
 */
public class ReferencePanel extends JPanel {

    private Controller controller;
    private BibliographicReferenceDAO bibliographicReferenceDAO;

    private ReferenceListPanel listPanel;
    private ReferenceInfoPanel infoPanel;

    private JButton createReferenceButton;
    private JButton editReferenceButton;
    private JButton deleteReferenceButton;

    /**
     * Crea {@code ReferencePanel} con i dati relativi all'utente.
     * 
     * @param user
     * @since 0.6
     */
    public ReferencePanel(Controller controller, BibliographicReferenceDAO bibliographicReferenceDAO) throws IllegalArgumentException {
        setController(controller);
        setBibiliographicReferenceDAO(bibliographicReferenceDAO);

        this.infoPanel = new ReferenceInfoPanel();

        this.listPanel = new ReferenceListPanel();
        this.listPanel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting()) {
                    boolean shouldButtonsBeEnabled = !listPanel.isSelectedRowNull();

                    editReferenceButton.setEnabled(shouldButtonsBeEnabled);
                    deleteReferenceButton.setEnabled(shouldButtonsBeEnabled);

                    infoPanel.showReference(listPanel.getSelectedReference());
                }
            }
        });

        setLayout(new BorderLayout(5, 5));
        setBorder(new EmptyBorder(5, 5, 5, 5));

        JSplitPane referenceSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, listPanel, infoPanel);
        referenceSplitPane.setDividerSize(10);
        referenceSplitPane.setResizeWeight(0.6f);

        add(getButtonsPanel(), BorderLayout.NORTH);
        add(referenceSplitPane, BorderLayout.CENTER);
    }

    /**
     * 
     * @param controller
     * @throws IllegalArgumentException
     */
    public void setController(Controller controller) throws IllegalArgumentException {
        if (controller == null)
            throw new IllegalArgumentException("controller non può essere null");

        this.controller = controller;
    }

    /**
     * 
     * @param bibliographicReferenceDAO
     * @throws IllegalArgumentException
     */
    public void setBibiliographicReferenceDAO(BibliographicReferenceDAO bibliographicReferenceDAO) throws IllegalArgumentException {
        if (bibliographicReferenceDAO == null)
            throw new IllegalArgumentException("bibliographicReferenceDAO non può essere null");

        this.bibliographicReferenceDAO = bibliographicReferenceDAO;
    }

    /**
     * 
     * @return
     */
    private JPanel getButtonsPanel() {
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));

        createReferenceButton = new JButton(new ImageIcon("images/file_add.png"));
        createReferenceButton.setToolTipText("Nuovo riferimento");
        createReferenceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addReference();
            }
        });

        editReferenceButton = new JButton(new ImageIcon("images/file_edit.png"));
        editReferenceButton.setToolTipText("Modifica riferimento");
        editReferenceButton.setEnabled(false);
        editReferenceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                changeSelectedReference();
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

    /**
     * Apre la pagina di creazione di un riferimento.
     */
    private void addReference() {
        controller.openReferenceCreatorPage();
    }

    /**
     * Apre la pagina di modifica del riferimento selezionato.
     */
    private void changeSelectedReference() {
        try {
            controller.openReferenceCreatorPage(listPanel.getSelectedReference());
        } catch (IndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(null, "Impossibile modificare il riferimento");
        }
    }

    /**
     * Rimuove il riferimento selezionato, chiedendo prima conferma all'utente.
     */
    private void removeSelectedReference() {
        try {
            int result = JOptionPane.showConfirmDialog(null, "Vuoi eliminare questo riferimento?", "Elimina riferimento", JOptionPane.YES_NO_OPTION);

            if (result == JOptionPane.YES_OPTION) {
                // TODO: rimuovi dal database

                listPanel.removeSelectedReference();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Impossibile eliminare il riferimento");
        }
    }

    /**
     * 
     * @param category
     */
    public void showReferencesOfCategory(Category category) {
        ArrayList<BibliographicReference> references = bibliographicReferenceDAO.getReferences(category);
        listPanel.setReferences(references);
    }

}
