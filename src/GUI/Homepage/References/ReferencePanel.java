package GUI.Homepage.References;

import DAO.*;
import GUI.*;
import Entities.*;
import Entities.References.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.FlowLayout;

// TODO: forse è possibile rimuovere il riferimento a controller se la creazion riferimento viene creata come finestra di dialogo

/**
 * Classe che si occupa di mostrare i riferimenti cercati o presenti in una
 * categoria.
 * 
 * @see ReferenceListPanel
 * @see ReferenceInfoPanel
 */
public class ReferencePanel extends JPanel {

    private Controller controller;
    private BibliographicReferenceDAO bibliographicReferenceDAO;

    private ReferenceListPanel listPanel;
    private ReferenceInfoPanel infoPanel;

    private JPanel buttonsPanel;
    private JButton createReferenceButton;
    private JButton editReferenceButton;
    private JButton deleteReferenceButton;
    private JPopupMenu newCategoryTypeSelection;

    /**
     * Crea {@code ReferencePanel} con i dati relativi all'utente.
     * 
     * @param controller
     * @param bibliographicReferenceDAO
     *                                  classe DAO per interfacciarsi al database
     *                                  dei riferimenti
     * @throws IllegalArgumentException
     *                                  se controller o bibiliographicReferenceDAO
     *                                  non sono validi
     * @see #setController(Controller)
     * @see #setBibiliographicReferenceDAO(BibliographicReferenceDAO)
     */
    public ReferencePanel(Controller controller, BibliographicReferenceDAO bibliographicReferenceDAO)
            throws IllegalArgumentException {
        setController(controller);
        setBibiliographicReferenceDAO(bibliographicReferenceDAO);

        this.infoPanel = new ReferenceInfoPanel();

        this.listPanel = new ReferenceListPanel();
        this.listPanel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting()) {
                    BibliographicReference selectedReference = listPanel.getSelectedReference();

                    infoPanel.showReference(selectedReference);

                    boolean shouldButtonsBeEnabled = selectedReference == null;
                    editReferenceButton.setEnabled(shouldButtonsBeEnabled);
                    deleteReferenceButton.setEnabled(shouldButtonsBeEnabled);
                }
            }
        });

        setLayout(new BorderLayout(5, 5));
        setBorder(new EmptyBorder(5, 5, 5, 5));

        JSplitPane referenceSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, listPanel, infoPanel);
        referenceSplitPane.setDividerSize(10);
        referenceSplitPane.setResizeWeight(0.6f);

        setupButtonsPanel();
        setupNewCategorySelectionPopupMenu();

        add(buttonsPanel, BorderLayout.NORTH);
        add(referenceSplitPane, BorderLayout.CENTER);
    }

    /**
     * Imposta il controller responsabile delle finestre.
     * 
     * @param controller
     * @throws IllegalArgumentException
     *                                  se {@code controller == null}
     */
    public void setController(Controller controller) throws IllegalArgumentException {
        if (controller == null)
            throw new IllegalArgumentException("controller non può essere null");

        this.controller = controller;
    }

    /**
     * Imposta la classe DAO per interfacciarsi col database e recuperare i
     * riferimenti.
     * 
     * @param bibliographicReferenceDAO
     *                                  classe DAO per i riferimenti
     * @throws IllegalArgumentException
     *                                  se {@code bibliographicReferenceDAO == null}
     */
    public void setBibiliographicReferenceDAO(BibliographicReferenceDAO bibliographicReferenceDAO)
            throws IllegalArgumentException {
        if (bibliographicReferenceDAO == null)
            throw new IllegalArgumentException("bibliographicReferenceDAO non può essere null");

        this.bibliographicReferenceDAO = bibliographicReferenceDAO;
    }

    /**
     * Imposta i pulsanti per creare, modificare o rimuovere un riferimento.
     */
    private void setupButtonsPanel() {
        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));

        createReferenceButton = new JButton(new ImageIcon("images/file_add.png"));
        createReferenceButton.setToolTipText("Nuovo riferimento");
        createReferenceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showReferenceCreationOptions();
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
    }

    /**
     * Configura il menu a tendina che appare quando viene premuto il tasto di
     * creazione del riferimento.
     */
    private void setupNewCategorySelectionPopupMenu() {
        newCategoryTypeSelection = new JPopupMenu();

        JMenuItem articleOption = new JMenuItem("Articolo");
        articleOption.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO: apri pagina creazione articolo
            }
        });

        JMenuItem bookOption = new JMenuItem("Libro");
        bookOption.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO: apri pagina creazione libro
            }
        });

        JMenuItem thesisOption = new JMenuItem("Tesi");
        thesisOption.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO: apri pagina creazione tesi
            }
        });

        JMenuItem videoOption = new JMenuItem("Video");
        videoOption.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO: apri pagina creazione video
            }
        });

        newCategoryTypeSelection.add(articleOption);
        newCategoryTypeSelection.add(bookOption);
        newCategoryTypeSelection.add(thesisOption);
        newCategoryTypeSelection.add(videoOption);
    }

    /**
     * Mostra un menu popup che permette di scegliere il tipo di riferimento da
     * creare.
     */
    private void showReferenceCreationOptions() {
        newCategoryTypeSelection.show(createReferenceButton, 0, createReferenceButton.getHeight());
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
            int result = JOptionPane.showConfirmDialog(null, "Vuoi eliminare questo riferimento?",
                    "Elimina riferimento", JOptionPane.YES_NO_OPTION);

            if (result == JOptionPane.YES_OPTION) {
                bibliographicReferenceDAO.removeReference(listPanel.getSelectedReference());
                listPanel.removeSelectedReference();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Impossibile eliminare il riferimento");
        }
    }

    /**
     * Mostra i riferimenti passati.
     * 
     * @param references
     *                   riferimenti da mostrare
     */
    public void showReferences(BibliographicReference[] references) {
        listPanel.setReferences(references);
    }

    /**
     * Carica tutti i riferimenti presenti in una categoria dal database e li mostra
     * a schermo.
     * 
     * @param category
     *                 categoria di cui mostrare i riferimenti
     */
    public void showReferences(Category category) {
        try {
            BibliographicReference[] references = bibliographicReferenceDAO.getReferences(category);
            showReferences(references);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

}
