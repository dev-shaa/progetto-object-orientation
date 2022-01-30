package GUI.Homepage.References;

import Controller.ReferenceController;
import GUI.Utilities.JPopupButton;
import Entities.References.*;
import Entities.References.PhysicalResources.*;
import Entities.References.OnlineResources.*;
import Entities.References.OnlineResources.Image;

import javax.swing.*;
import javax.swing.border.*;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Pannello che mostra i riferimenti cercati o presenti in una categoria, con funzioni di creazione, modifica e rimozione.
 */
public class ReferencePanel extends JPanel implements ReferenceSelectionListener {

    private ReferenceController referenceController;

    private ReferenceListPanel listPanel;
    private ReferenceInfoPanel infoPanel;

    private JButton editReferenceButton;
    private JButton deleteReferenceButton;

    private ArrayList<ReferenceEditorOptionListener> listeners;

    /**
     * Crea un nuovo pannello dei riferimenti.
     * 
     * @param referenceController
     *            controller dei riferimenti
     * @throws IllegalArgumentException
     *             se {@code referenceController == null}
     */
    public ReferencePanel(ReferenceController referenceController) {
        setReferenceController(referenceController);

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
                triggerArticleEditor(null);
            }
        });

        JMenuItem bookOption = new JMenuItem("Libro");
        bookOption.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                triggerBookEditor(null);
            }
        });

        JMenuItem thesisOption = new JMenuItem("Tesi");
        thesisOption.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                triggerThesisEditor(null);
            }
        });

        JMenuItem websiteOption = new JMenuItem("Sito web");
        websiteOption.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                triggerWebsiteEditor(null);
            }
        });

        JMenuItem sourceCodeOption = new JMenuItem("Codice sorgente");
        sourceCodeOption.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                triggerSourceCodeEditor(null);
            }
        });

        JMenuItem imageOption = new JMenuItem("Immagine");
        imageOption.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                triggerImageEditor(null);
            }
        });

        JMenuItem videoOption = new JMenuItem("Video");
        videoOption.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                triggerVideoEditor(null);
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

    /**
     * Imposta il controller dei riferimenti.
     * 
     * @param referenceController
     *            controller dei riferimenti
     * @throws IllegalArgumentException
     *             se {@code referenceController == null}
     */
    public void setReferenceController(ReferenceController referenceController) {
        if (referenceController == null)
            throw new IllegalArgumentException("referenceController can't be null");

        this.referenceController = referenceController;
    }

    /**
     * Restituisce il controller dei riferimenti.
     * 
     * @return
     *         controller dei riferimenti
     */
    public ReferenceController getReferenceController() {
        return referenceController;
    }

    /**
     * TODO:
     * 
     * @param references
     */
    public void setReferences(Collection<? extends BibliographicReference> references) {
        listPanel.setReferences(references);
    }

    /**
     * 
     * @param listener
     */
    public void addListener(ReferenceEditorOptionListener listener) {
        if (listener == null)
            return;

        if (listeners == null)
            listeners = new ArrayList<>(3);

        listeners.add(listener);
    }

    /**
     * 
     * @param listener
     */
    public void removeListener(ReferenceEditorOptionListener listener) {
        if (listener == null || listeners == null)
            return;

        listeners.remove(listener);
    }

    private void changeSelectedReference() {
        BibliographicReference selectedReference = listPanel.getSelectedReference();

        if (selectedReference == null)
            return;

        if (selectedReference instanceof Article) {
            triggerArticleEditor((Article) selectedReference);
        } else if (selectedReference instanceof Book) {
            triggerBookEditor((Book) selectedReference);
        } else if (selectedReference instanceof Image) {
            triggerImageEditor((Image) selectedReference);
        } else if (selectedReference instanceof SourceCode) {
            triggerSourceCodeEditor((SourceCode) selectedReference);
        } else if (selectedReference instanceof Thesis) {
            triggerThesisEditor((Thesis) selectedReference);
        } else if (selectedReference instanceof Video) {
            triggerVideoEditor((Video) selectedReference);
        } else if (selectedReference instanceof Website) {
            triggerWebsiteEditor((Website) selectedReference);
        }
    }

    private void removeSelectedReference() {
        try {
            int result = JOptionPane.showConfirmDialog(null, "Vuoi eliminare questo riferimento?", "Elimina riferimento", JOptionPane.YES_NO_OPTION);

            if (result == JOptionPane.YES_OPTION) {
                getReferenceController().removeReference(listPanel.getSelectedReference());
                listPanel.removeSelectedReference();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Impossibile eliminare il riferimento");
        }
    }

    private void triggerArticleEditor(Article article) {
        if (listeners == null)
            return;

        for (ReferenceEditorOptionListener listener : listeners) {
            listener.onArticleEditorCall(article);
        }
    }

    private void triggerBookEditor(Book book) {
        if (listeners == null)
            return;

        for (ReferenceEditorOptionListener listener : listeners) {
            listener.onBookEditorCall(book);
        }
    }

    private void triggerImageEditor(Image image) {
        if (listeners == null)
            return;

        for (ReferenceEditorOptionListener listener : listeners) {
            listener.onImageEditorCall(image);
        }
    }

    private void triggerSourceCodeEditor(SourceCode sourceCode) {
        if (listeners == null)
            return;

        for (ReferenceEditorOptionListener listener : listeners) {
            listener.onSourceCodeEditorCall(sourceCode);
        }
    }

    private void triggerThesisEditor(Thesis thesis) {
        if (listeners == null)
            return;

        for (ReferenceEditorOptionListener listener : listeners) {
            listener.onThesisEditorCall(thesis);
        }
    }

    private void triggerVideoEditor(Video video) {
        if (listeners == null)
            return;

        for (ReferenceEditorOptionListener listener : listeners) {
            listener.onVideoEditorCall(video);
        }
    }

    private void triggerWebsiteEditor(Website website) {
        if (listeners == null)
            return;

        for (ReferenceEditorOptionListener listener : listeners) {
            listener.onWebsiteEditorCall(website);
        }
    }

}
