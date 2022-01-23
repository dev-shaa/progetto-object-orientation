package GUI.Homepage.References;

import DAO.*;
import GUI.*;
import GUI.Homepage.Categories.CategoriesTreeManager;
import GUI.Homepage.References.Editor.*;
import GUI.Homepage.Search.Search;
import GUI.Utilities.JPopupButton;
import Entities.*;
import Entities.References.*;
import Entities.References.OnlineResources.Image;
import Entities.References.OnlineResources.SourceCode;
import Entities.References.OnlineResources.Video;
import Entities.References.OnlineResources.Website;
import Entities.References.PhysicalResources.Article;
import Entities.References.PhysicalResources.Book;
import Entities.References.PhysicalResources.Thesis;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe che si occupa di mostrare i riferimenti cercati o presenti in una
 * categoria.
 * 
 * @see ReferenceListPanel
 * @see ReferenceInfoPanel
 */
public class ReferencePanel extends JPanel implements ReferenceSelectionListener {

    private CategoriesTreeManager categoriesTree;
    private BibliographicReferenceDAO referenceDAO;

    private ReferenceListPanel listPanel;
    private ReferenceInfoPanel infoPanel;

    private JButton editReferenceButton;
    private JButton deleteReferenceButton;

    private ArticleEditor articleEditor;
    private BookEditor bookEditor;
    private ImageEditor imageEditor;
    private SourceCodeEditor sourceCodeEditor;
    private ThesisEditor thesisEditor;
    private VideoEditor videoEditor;
    private WebsiteEditor websiteEditor;

    private ArrayList<BibliographicReference> references;

    /**
     * Crea un pannello di riferimenti.
     * 
     * @param categoriesTree
     * @param referenceDAO
     *            classe DAO per interfacciarsi al database dei riferimenti
     * @throws IllegalArgumentException
     *             se categoriesTreeManager o bibiliographicReferenceDAO non sono validi
     * @see #setCategoriesTreeManager(Controller)
     * @see #setBibliographicReferenceDAO(BibliographicReferenceDAO)
     */
    public ReferencePanel(CategoriesTreeManager categoriesTree, BibliographicReferenceDAO referenceDAO) throws IllegalArgumentException {
        setCategoriesTree(categoriesTree);
        setBibliographicReferenceDAO(referenceDAO);

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
                openArticleEditor(null);
            }
        });

        JMenuItem bookOption = new JMenuItem("Libro");
        bookOption.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openBookEditor(null);
            }
        });

        JMenuItem thesisOption = new JMenuItem("Tesi");
        thesisOption.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openThesisEditor(null);
            }
        });

        JMenuItem websiteOption = new JMenuItem("Sito web");
        websiteOption.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openWebsiteEditor(null);
            }
        });

        JMenuItem sourceCodeOption = new JMenuItem("Codice sorgente");
        sourceCodeOption.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openSourceCodeEditor(null);
            }
        });

        JMenuItem imageOption = new JMenuItem("Immagine");
        imageOption.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openImageEditor(null);
            }
        });

        JMenuItem videoOption = new JMenuItem("Video");
        videoOption.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openVideoEditor(null);
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

    private void openArticleEditor(Article article) {
        if (articleEditor == null)
            articleEditor = new ArticleEditor(categoriesTree, referenceDAO);

        articleEditor.setVisible(true, article);
    }

    private void openBookEditor(Book book) {
        if (bookEditor == null)
            bookEditor = new BookEditor(categoriesTree, referenceDAO);

        bookEditor.setVisible(true, book);
    }

    private void openImageEditor(Image image) {
        if (imageEditor == null)
            imageEditor = new ImageEditor(categoriesTree, referenceDAO);

        imageEditor.setVisible(true, image);
    }

    private void openSourceCodeEditor(SourceCode sourceCode) {
        if (sourceCodeEditor == null)
            sourceCodeEditor = new SourceCodeEditor(categoriesTree, referenceDAO);

        sourceCodeEditor.setVisible(true, sourceCode);
    }

    private void openThesisEditor(Thesis thesis) {
        if (thesisEditor == null)
            thesisEditor = new ThesisEditor(categoriesTree, referenceDAO);

        thesisEditor.setVisible(true, thesis);
    }

    private void openVideoEditor(Video video) {
        if (videoEditor == null)
            videoEditor = new VideoEditor(categoriesTree, referenceDAO);

        videoEditor.setVisible(true, video);
    }

    private void openWebsiteEditor(Website website) {
        if (websiteEditor == null)
            websiteEditor = new WebsiteEditor(categoriesTree, referenceDAO);

        websiteEditor.setVisible(true, website);
    }

    private void changeSelectedReference() {
        BibliographicReference selectedReference = listPanel.getSelectedReference();

        if (selectedReference == null)
            return;

        if (selectedReference instanceof Article) {
            openArticleEditor((Article) selectedReference);
        } else if (selectedReference instanceof Book) {
            openBookEditor((Book) selectedReference);
        } else if (selectedReference instanceof Image) {
            openImageEditor((Image) selectedReference);
        } else if (selectedReference instanceof SourceCode) {
            openSourceCodeEditor((SourceCode) selectedReference);
        } else if (selectedReference instanceof Thesis) {
            openThesisEditor((Thesis) selectedReference);
        } else if (selectedReference instanceof Video) {
            openVideoEditor((Video) selectedReference);
        } else if (selectedReference instanceof Website) {
            openWebsiteEditor((Website) selectedReference);
        }
    }

    private void removeSelectedReference() {
        try {
            int result = JOptionPane.showConfirmDialog(null, "Vuoi eliminare questo riferimento?", "Elimina riferimento", JOptionPane.YES_NO_OPTION);

            if (result == JOptionPane.YES_OPTION) {
                referenceDAO.removeReference(listPanel.getSelectedReference());
                listPanel.removeSelectedReference();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Impossibile eliminare il riferimento");
        }
    }

    /**
     * Imposta la classe DAO per interfacciarsi col database e recuperare i
     * riferimenti.
     * 
     * @param referenceDAO
     *            classe DAO per i riferimenti
     * @throws IllegalArgumentException
     *             se {@code bibliographicReferenceDAO == null}
     */
    public void setBibliographicReferenceDAO(BibliographicReferenceDAO referenceDAO) throws IllegalArgumentException {
        if (referenceDAO == null)
            throw new IllegalArgumentException("bibliographicReferenceDAO non può essere null");

        this.referenceDAO = referenceDAO;
    }

    /**
     * TODO: commenta
     * 
     * @param categoriesTree
     * @throws IllegalArgumentException
     */
    public void setCategoriesTree(CategoriesTreeManager categoriesTree) throws IllegalArgumentException {
        if (categoriesTree == null)
            throw new IllegalArgumentException("categories tree non può essere null");

        this.categoriesTree = categoriesTree;
    }

    /**
     * TODO: commenta
     * 
     * @return
     */
    public ArrayList<BibliographicReference> getReferences() {
        return references;
    }

    /**
     * Mostra i riferimenti passati.
     * 
     * @param references
     *            riferimenti da mostrare
     */
    public void showReferences(BibliographicReference[] references) {
        listPanel.setReferences(references);
    }

    /**
     * Mostra tutti i riferimenti presenti in una categoria.
     * 
     * @param category
     *            categoria di cui mostrare i riferimenti
     */
    public void showReferences(Category category) {
        if (references == null)
            return;

        List<BibliographicReference> referencesInCategory = references.stream().filter(e -> e.getCategories().contains(category)).toList();
        showReferences(referencesInCategory.toArray(new BibliographicReference[referencesInCategory.size()]));
    }

    /**
     * Carica tutti i riferimenti a partire da una ricerca e li mostra a schermo.
     * 
     * @param search
     *            ricerca
     */
    public void showReferences(Search search) {
        try {
            showReferences(referenceDAO.getReferences(search));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    @Override
    public void onReferenceSelection(BibliographicReference reference) {
        infoPanel.showReference(reference);

        boolean shouldButtonsBeEnabled = reference == null;
        editReferenceButton.setEnabled(shouldButtonsBeEnabled);
        deleteReferenceButton.setEnabled(shouldButtonsBeEnabled);
    }

}
