import javax.swing.*;
import javax.swing.border.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
// import javax.swing.tree.TreeNode;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.*;
// import javax.swing.table.TableModel;

import java.awt.*;
import java.awt.BorderLayout;
// import java.awt.GridLayout;
import java.awt.event.*;
import java.util.ArrayList;
// import java.util.Date;

public class MainWindow extends JFrame {

    ArrayList<Riferimento> riferimenti;
    DefaultTableModel referenceTableModel;
    DefaultMutableTreeNode selectedTreeNode;
    DefaultTreeModel treeModel;

    // NOTE: forse è possibile introdurre un cestino per i riferimenti

    public MainWindow(User user) {
        riferimenti = new ArrayList<Riferimento>();

        // TODO: ottieni riferimenti e categorie dal database
        // DEBUG:
        riferimenti.add(new Riferimento("riferimento 1", "autore 1"));
        riferimenti.add(new Riferimento("riferimento 2", "autore 2"));

        setTitle("Pagina principale");
        setMinimumSize(new Dimension(640, 480));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1280, 720);

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(0, 5, 10, 5));
        setContentPane(contentPane);

        JLabel userLabel = new JLabel(user.name, SwingConstants.RIGHT);
        userLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
        userLabel.setIcon(new ImageIcon("images/user.png"));
        userLabel.setMinimumSize(new Dimension(Integer.MAX_VALUE, 50));

        contentPane.add(userLabel, BorderLayout.NORTH);
        contentPane.add(createSidePanel(), BorderLayout.WEST);
        contentPane.add(createMainPanel(), BorderLayout.CENTER);
    }

    private JPanel createSidePanel() {

        // FIXME: è un pixel sopra il pannello principale

        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.PAGE_AXIS));
        sidePanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.LINE_AXIS));
        buttonsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));

        JButton createCategoryButton = new JButton(new ImageIcon("images/folder_add.png"));
        createCategoryButton.setToolTipText("Nuova categoria");
        createCategoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createCategory();
            }
        });

        JButton editCategoryButton = new JButton(new ImageIcon("images/folder_edit.png"));
        editCategoryButton.setToolTipText("Modifica categoria");

        JButton deleteCategoryButton = new JButton(new ImageIcon("images/folder_delete.png"));
        deleteCategoryButton.setToolTipText("Elimina categoria");
        deleteCategoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteCategory();
            }
        });

        buttonsPanel.add(createCategoryButton);
        buttonsPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        buttonsPanel.add(editCategoryButton);
        buttonsPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        buttonsPanel.add(deleteCategoryButton);

        // https://stackoverflow.com/questions/14386682/how-to-get-selected-node-from-jtree-after-selecting-it

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Tutti i riferimenti");
        treeModel = new DefaultTreeModel(root);

        JTree tree = new JTree(treeModel);
        tree.setEditable(false);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setShowsRootHandles(true);
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                selectedTreeNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            }
        });

        JScrollPane categoriesPanel = new JScrollPane(tree);

        sidePanel.add(buttonsPanel);
        sidePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidePanel.add(categoriesPanel);

        return sidePanel;
    }

    private JPanel createMainPanel() {

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.LINE_AXIS));
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
        buttonsPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        buttonsPanel.add(editReferenceButton);
        buttonsPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        buttonsPanel.add(deleteReferenceButton);
        buttonsPanel.add(Box.createRigidArea(new Dimension(30, 0)));
        buttonsPanel.add(searchBox);
        buttonsPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonsPanel.add(searchButton);

        String[] referenceTableColumns = { "Nome", "Autore", "Data pubblicazione" };
        referenceTableModel = new DefaultTableModel(referenceTableColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        updateReferences();

        // https://stackoverflow.com/questions/10128064/jtable-selected-row-click-event

        String[] referencePreviewTableColumns = new String[2];
        DefaultTableModel referencePreviewTableModel = new DefaultTableModel(referencePreviewTableColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

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

        JTable referenceViewTable = new JTable(referencePreviewTableModel);
        referenceViewTable.setTableHeader(null);
        referenceViewTable.setFillsViewportHeight(true);

        JScrollPane referencePreviewPanel = new JScrollPane(referenceViewTable);

        mainPanel.add(buttonsPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(referencePanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(referencePreviewPanel);

        return mainPanel;
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

    private void createCategory() {
        try {
            String categoryName = getStringFromUser();

            // TODO: salva nel database
            // CategoriesDAO.Instance.saveCategory();

            DefaultMutableTreeNode newTreeNode = new DefaultMutableTreeNode(categoryName);
            selectedTreeNode.add(newTreeNode);

            treeModel.reload();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Impossibile creare una nuova categoria");
        }
    }

    private void editCategory() {

    }

    private void deleteCategory() {
        try {
            // TODO: rimuovi dal database la categoria e le sue sottocategorie

            selectedTreeNode.removeFromParent();
            treeModel.reload();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Impossibile eliminare la categoria");
        }
    }

    private String getStringFromUser() throws Exception {
        String categoryName = (String) JOptionPane.showInputDialog(null, "Nome categoria:",
                "Nuova categoria",
                JOptionPane.PLAIN_MESSAGE,
                null, null, "Nuova categoria");

        if (categoryName.isEmpty())
            throw new Exception("Nome vuoto");

        return categoryName;
    }

}
