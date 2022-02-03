package GUI;

import Entities.*;
import Entities.References.BibliographicReference;
import Entities.References.OnlineResources.*;
import Entities.References.OnlineResources.Image;
import Entities.References.PhysicalResources.*;
import GUI.Categories.*;
import GUI.References.*;
import GUI.Search.*;
import GUI.Utilities.PopupButton;
import Controller.Controller;
import DAO.CategoryDAOPostgreSQL;

import java.awt.*;
import java.awt.event.*;
import java.awt.BorderLayout;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Classe che si occupa di impostare le componenti base della pagina principale, che mostra tutti i riferimenti e le categorie.
 */
public class Homepage extends JFrame implements CategorySelectionListener, ReferenceSelectionListener, SearchListener {

    private Controller controller;

    private JLabel userLabel;
    private JButton logoutButton;

    private CategoriesTreePanel categoriesTreePanel;
    private JButton createCategoryButton;
    private JButton updateCategoryButton;
    private JButton removeCategoryButton;

    private ReferenceListPanel referenceListPanel;
    private ReferenceInfoPanel referenceInfoPanel;
    private JButton updateReferenceButton;
    private JButton removeReferenceButton;

    private SearchPanel referenceSearchPanel;

    /**
     * Crea una nuova pagina principale con il controller indicato.
     * 
     * @param controller
     *            controller della GUI
     */
    public Homepage(Controller controller) {
        setController(controller);

        setTitle("Pagina principale");
        setMinimumSize(new Dimension(400, 400));
        setBounds(100, 100, 800, 600);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                int confirmDialogBoxOption = JOptionPane.showConfirmDialog(null, "Sicuro di volere uscire?", "Esci", JOptionPane.YES_NO_OPTION);

                if (confirmDialogBoxOption == JOptionPane.YES_OPTION)
                    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        });

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout(5, 5));
        setContentPane(contentPane);

        contentPane.add(setupUserInfoPanel(), BorderLayout.NORTH);

        referenceSearchPanel = new SearchPanel(getController().getCategoryController().get());
        referenceSearchPanel.addReferenceSearchListener(this);

        JSplitPane subSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, setupCategoriesPanel(), setupReferencePanel());
        subSplitPane.setResizeWeight(0.15);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, subSplitPane, referenceSearchPanel);
        splitPane.setResizeWeight(1);

        contentPane.add(splitPane, BorderLayout.CENTER);
    }

    @Override
    public void setVisible(boolean b) {
        if (b)
            reset();

        super.setVisible(b);
    }

    private void reset() {
        updateUserLabel();

        categoriesTreePanel.setCategoriesTree(getController().getCategoryController().get());
        referenceSearchPanel.setTreeModel(getController().getCategoryController().get());
    }

    /**
     * Imposta il controller della GUI.
     * 
     * @param controller
     *            controller della GUI
     */
    public void setController(Controller controller) {
        if (controller == null)
            throw new IllegalArgumentException("controller can't be null");

        this.controller = controller;
    }

    /**
     * Restituisce il controller della GUI.
     * 
     * @return controller
     */
    public Controller getController() {
        return controller;
    }

    // CATEGORIES

    private JPanel setupCategoriesPanel() {
        JPanel categoriesPanel = new JPanel();

        categoriesTreePanel = new CategoriesTreePanel(getController().getCategoryController().get());
        categoriesTreePanel.addSelectionListener(this);

        categoriesPanel.setLayout(new BorderLayout(5, 5));
        categoriesPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);

        createCategoryButton = new JButton(new ImageIcon("images/folder_add.png"));
        createCategoryButton.setToolTipText("Crea nuova categoria");
        createCategoryButton.setEnabled(false);
        createCategoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addCategory();
            }
        });

        updateCategoryButton = new JButton(new ImageIcon("images/folder_edit.png"));
        updateCategoryButton.setToolTipText("Modifica categoria selezionata");
        updateCategoryButton.setEnabled(false);
        updateCategoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                changeCategory();
            }
        });

        removeCategoryButton = new JButton(new ImageIcon("images/folder_delete.png"));
        removeCategoryButton.setToolTipText("Elimina categoria selezionata");
        removeCategoryButton.setEnabled(false);
        removeCategoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeCategory();
            }
        });

        toolbar.add(createCategoryButton);
        toolbar.add(updateCategoryButton);
        toolbar.add(removeCategoryButton);

        categoriesPanel.add(toolbar, BorderLayout.NORTH);
        categoriesPanel.add(categoriesTreePanel, BorderLayout.CENTER);

        return categoriesPanel;
    }

    @Override
    public void onCategorySelection(Category selectedCategory) {
        createCategoryButton.setEnabled(true);
        updateCategoryButton.setEnabled(selectedCategory != null);
        removeCategoryButton.setEnabled(selectedCategory != null);

        referenceListPanel.setReferences(getController().getReferenceController().getReferences(selectedCategory));
    }

    @Override
    public void onCategoryClearSelection() {
        createCategoryButton.setEnabled(false);
        updateCategoryButton.setEnabled(false);
        removeCategoryButton.setEnabled(false);
    }

    private void addCategory() {
        try {
            String newCategoryName = getCategoryNameFromUser("Nuova categoria");

            if (newCategoryName != null) {
                getController().getCategoryController().save(newCategoryName, categoriesTreePanel.getSelectedNode());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void changeCategory() {
        try {
            String newName = getCategoryNameFromUser(categoriesTreePanel.getSelectedNode().getUserObject().getName());

            if (newName != null) {
                getController().getCategoryController().update(categoriesTreePanel.getSelectedNode(), newName);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void removeCategory() {
        try {
            int confirmDialogBoxOption = JOptionPane.showConfirmDialog(null, "Sicuro di voler eliminare questa categoria?", "Elimina categoria", JOptionPane.YES_NO_OPTION);

            if (confirmDialogBoxOption == JOptionPane.YES_OPTION) {
                getController().getCategoryController().remove(categoriesTreePanel.getSelectedNode());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private String getCategoryNameFromUser(String defaultName) {
        return (String) JOptionPane.showInputDialog(this, "Inserisci il nuovo nome della categoria", "Nuova categoria", JOptionPane.PLAIN_MESSAGE, null, null, defaultName);
    }

    // REFERENCES

    private JPanel setupReferencePanel() {
        JPanel referencePanel = new JPanel();

        referencePanel.setLayout(new BorderLayout(5, 5));
        referencePanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);

        PopupButton createReferenceButton = new PopupButton(new ImageIcon("images/file_add.png"));
        createReferenceButton.setToolTipText("Crea riferimento");

        JMenuItem articleOption = new JMenuItem("Articolo");
        articleOption.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getController().openArticleEditor(null);
            }
        });

        JMenuItem bookOption = new JMenuItem("Libro");
        bookOption.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getController().openBookEditor(null);
            }
        });

        JMenuItem thesisOption = new JMenuItem("Tesi");
        thesisOption.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getController().openThesisEditor(null);
            }
        });

        JMenuItem websiteOption = new JMenuItem("Sito web");
        websiteOption.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getController().openWebsiteEditor(null);
            }
        });

        JMenuItem sourceCodeOption = new JMenuItem("Codice sorgente");
        sourceCodeOption.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getController().openSourceCodeEditor(null);
            }
        });

        JMenuItem imageOption = new JMenuItem("Immagine");
        imageOption.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getController().openImageEditor(null);
            }
        });

        JMenuItem videoOption = new JMenuItem("Video");
        videoOption.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getController().openVideoEditor(null);
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

        updateReferenceButton = new JButton(new ImageIcon("images/file_edit.png"));
        updateReferenceButton.setToolTipText("Modifica riferimento");
        updateReferenceButton.setEnabled(false);
        updateReferenceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                changeSelectedReference();
            }
        });

        removeReferenceButton = new JButton(new ImageIcon("images/file_remove.png"));
        removeReferenceButton.setToolTipText("Elimina riferimento");
        removeReferenceButton.setEnabled(false);
        removeReferenceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeSelectedReference();
            }
        });

        toolbar.add(createReferenceButton);
        toolbar.add(updateReferenceButton);
        toolbar.add(removeReferenceButton);

        referenceInfoPanel = new ReferenceInfoPanel();
        referenceListPanel = new ReferenceListPanel();
        referenceListPanel.addReferenceSelectionListener(this);

        JSplitPane referenceSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, referenceListPanel, referenceInfoPanel);
        referenceSplitPane.setDividerSize(10);
        referenceSplitPane.setResizeWeight(0.6f);

        referencePanel.add(toolbar, BorderLayout.NORTH);
        referencePanel.add(referenceSplitPane, BorderLayout.CENTER);

        return referencePanel;
    }

    @Override
    public void onReferenceSelection(BibliographicReference reference) {
        referenceInfoPanel.setReference(reference);
        updateReferenceButton.setEnabled(reference != null);
        removeReferenceButton.setEnabled(reference != null);
    }

    private void changeSelectedReference() {
        BibliographicReference selectedReference = referenceListPanel.getSelectedReference();

        if (selectedReference == null)
            return;

        if (selectedReference instanceof Article) {
            getController().openArticleEditor((Article) selectedReference);
        } else if (selectedReference instanceof Book) {
            getController().openBookEditor((Book) selectedReference);
        } else if (selectedReference instanceof Image) {
            getController().openImageEditor((Image) selectedReference);
        } else if (selectedReference instanceof SourceCode) {
            getController().openSourceCodeEditor((SourceCode) selectedReference);
        } else if (selectedReference instanceof Thesis) {
            getController().openThesisEditor((Thesis) selectedReference);
        } else if (selectedReference instanceof Video) {
            getController().openVideoEditor((Video) selectedReference);
        } else if (selectedReference instanceof Website) {
            getController().openWebsiteEditor((Website) selectedReference);
        }
    }

    private void removeSelectedReference() {
        try {
            int result = JOptionPane.showConfirmDialog(this, "Vuoi eliminare questo riferimento?", "Elimina riferimento", JOptionPane.YES_NO_OPTION);

            if (result == JOptionPane.YES_OPTION) {
                getController().getReferenceController().removeReference(referenceListPanel.getSelectedReference());
                referenceListPanel.removeSelectedReference();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Impossibile eliminare il riferimento");
        }
    }

    // SEARCH

    @Override
    public void search(Search search) {
        categoriesTreePanel.clearSelection();
        referenceListPanel.setReferences(getController().getReferenceController().getReferences(search));
    }

    // USER

    private JPanel setupUserInfoPanel() {
        JPanel userInfoPanel = new JPanel();

        Color darkGray = Color.decode("#24292f");

        userInfoPanel.setLayout(new BorderLayout(5, 0));
        userInfoPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        userInfoPanel.setBackground(darkGray);

        updateUserLabel();

        logoutButton = new JButton(new ImageIcon("images/logout_white.png"));
        logoutButton.setToolTipText("Esci");
        logoutButton.setHorizontalAlignment(SwingConstants.RIGHT);
        logoutButton.setBackground(darkGray);
        logoutButton.setBorderPainted(false);
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });

        userInfoPanel.add(userLabel, BorderLayout.WEST);
        userInfoPanel.add(logoutButton, BorderLayout.EAST);

        return userInfoPanel;
    }

    private void updateUserLabel() {
        if (userLabel == null)
            userLabel = new JLabel();

        userLabel.setText("Benvenuto, " + getController().getUser());
    }

    private void logout() {
        controller.openLoginPage();
    }

}