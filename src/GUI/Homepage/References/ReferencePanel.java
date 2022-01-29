package GUI.Homepage.References;

import GUI.Homepage.Homepage;
import GUI.Utilities.JPopupButton;
import Entities.References.*;

import javax.swing.*;
import javax.swing.border.*;

import Controller.ReferenceController;

import java.awt.*;
import java.awt.event.*;
import java.util.Collection;

/**
 * Pannello che mostra i riferimenti cercati o presenti in una categoria, con funzioni di creazione, modifica e rimozione.
 */
public class ReferencePanel extends JPanel implements ReferenceListSelectionListener {

    private ReferenceController referenceManager;

    private ReferenceListPanel listPanel;
    private ReferenceInfoPanel infoPanel;

    private JButton editReferenceButton;
    private JButton deleteReferenceButton;

    /**
     * TODO: commenta
     * Crea un nuovo pannello.
     * 
     * @param categoryManager
     * @param referenceManager
     * @throws IllegalArgumentException
     */
    public ReferencePanel(Homepage homepage, ReferenceController referenceManager) throws IllegalArgumentException {
        this.referenceManager = referenceManager;

        setLayout(new BorderLayout(5, 5));
        setBorder(new EmptyBorder(5, 5, 5, 5));

        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);

        // #region create button

        JPopupButton createReferenceButton = new JPopupButton(new ImageIcon("images/file_add.png"));
        createReferenceButton.setToolTipText("Crea riferimento");

        JMenuItem articleOption = new JMenuItem("Articolo");
        articleOption.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // homepage.openArticleEditor(null);
            }
        });

        JMenuItem bookOption = new JMenuItem("Libro");
        bookOption.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // homepage.openBookEditor(null);
            }
        });

        JMenuItem thesisOption = new JMenuItem("Tesi");
        thesisOption.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // openThesisEditor(null);
            }
        });

        JMenuItem websiteOption = new JMenuItem("Sito web");
        websiteOption.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // openWebsiteEditor(null);
            }
        });

        JMenuItem sourceCodeOption = new JMenuItem("Codice sorgente");
        sourceCodeOption.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // openSourceCodeEditor(null);
            }
        });

        JMenuItem imageOption = new JMenuItem("Immagine");
        imageOption.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // openImageEditor(null);
            }
        });

        JMenuItem videoOption = new JMenuItem("Video");
        videoOption.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // openVideoEditor(null);
            }
        });

        createReferenceButton.addToPopupMenu(articleOption);
        createReferenceButton.addToPopupMenu(bookOption);
        createReferenceButton.addToPopupMenu(thesisOption);
        createReferenceButton.addPopupSeparator();
        createReferenceButton.addToPopupMenu(websiteOption);
        createReferenceButton.addToPopupMenu(sourceCodeOption);
        createReferenceButton.addToPopupMenu(imageOption);
        createReferenceButton.addToPopupMenu(videoOption);

        // #endregion

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

        toolbar.add(createReferenceButton);
        toolbar.add(editReferenceButton);
        toolbar.add(deleteReferenceButton);

        infoPanel = new ReferenceInfoPanel();

        listPanel = new ReferenceListPanel();
        listPanel.addReferenceSelectionListener(this);

        JSplitPane referenceSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, listPanel, infoPanel);
        referenceSplitPane.setDividerSize(10);
        referenceSplitPane.setResizeWeight(0.6f);

        add(toolbar, BorderLayout.NORTH);
        add(referenceSplitPane, BorderLayout.CENTER);
    }

    @Override
    public void onReferenceSelection(BibliographicReference reference) {
        infoPanel.showReference(reference);

        boolean shouldButtonsBeEnabled = reference != null;
        editReferenceButton.setEnabled(shouldButtonsBeEnabled);
        deleteReferenceButton.setEnabled(shouldButtonsBeEnabled);
    }

    private void changeSelectedReference() {
        BibliographicReference selectedReference = listPanel.getSelectedReference();

        if (selectedReference == null)
            return;

        // if (selectedReference instanceof Article) {
        // openArticleEditor((Article) selectedReference);
        // } else if (selectedReference instanceof Book) {
        // openBookEditor((Book) selectedReference);
        // } else if (selectedReference instanceof Image) {
        // openImageEditor((Image) selectedReference);
        // } else if (selectedReference instanceof SourceCode) {
        // openSourceCodeEditor((SourceCode) selectedReference);
        // } else if (selectedReference instanceof Thesis) {
        // openThesisEditor((Thesis) selectedReference);
        // } else if (selectedReference instanceof Video) {
        // openVideoEditor((Video) selectedReference);
        // } else if (selectedReference instanceof Website) {
        // openWebsiteEditor((Website) selectedReference);
        // }
    }

    private void removeSelectedReference() {
        try {
            int result = JOptionPane.showConfirmDialog(null, "Vuoi eliminare questo riferimento?", "Elimina riferimento", JOptionPane.YES_NO_OPTION);

            if (result == JOptionPane.YES_OPTION) {
                referenceManager.removeReference(listPanel.getSelectedReference());
                listPanel.removeSelectedReference();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Impossibile eliminare il riferimento");
        }
    }

    public void setReferences(Collection<? extends BibliographicReference> references) {
        listPanel.setReferences(references);
    }

}
