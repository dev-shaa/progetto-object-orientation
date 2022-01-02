import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.awt.BorderLayout;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Classe che si occupa di impostare le componenti base della pagina principale,
 * che mostra tutti i riferimenti e le categorie.
 * 
 * @version 0.2
 * @author Salvatore Di Gennaro
 * @see CategoryPanel
 * @see ReferencePanel
 */
public class MainWindow extends JFrame {

    private Controller controller;
    private User user;
    private CategoryDAO categoryDAO;
    private ArrayList<CategoriesActionListener> categoriesActionListeners;

    /**
     * Crea {@code MainWindow} con i dati relativi all'utente.
     * 
     * @param user
     *            l'utente che ha eseguito l'accesso
     * @since 0.1
     * @author Salvatore Di Gennaro
     */
    public MainWindow(Controller controller, User user) {
        setController(controller);
        setUser(user);

        categoryDAO = new CategoryDAOPostgreSQL();
        categoriesActionListeners = new ArrayList<CategoriesActionListener>();

        setTitle("Pagina principale");
        setMinimumSize(new Dimension(400, 400));
        setBounds(100, 100, 720, 540);
        setCloseOperation();

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout(5, 5));
        setContentPane(contentPane);

        ReferencePanel referencePanel = new ReferencePanel(controller, user);
        CategoryPanel categoryPanel = new CategoryPanel(this, referencePanel);
        ReferenceSearchPanel referenceSearchPanel = new ReferenceSearchPanel(this);

        JSplitPane subSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, categoryPanel, referencePanel);
        subSplitPane.setResizeWeight(0.15);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, subSplitPane, referenceSearchPanel);
        splitPane.setResizeWeight(0.8);

        contentPane.add(getUserInfoPanel(controller, user.name), BorderLayout.NORTH);
        contentPane.add(splitPane, BorderLayout.CENTER);
    }

    private void setCloseOperation() {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                int confirmDialogBoxOption = JOptionPane.showConfirmDialog(null, "Sicuro di volere uscire?", "Esci",
                        JOptionPane.YES_NO_OPTION);

                if (confirmDialogBoxOption == JOptionPane.YES_OPTION)
                    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        });
    }

    private JPanel getUserInfoPanel(Controller controller, String username) {
        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BorderLayout(5, 0));
        userInfoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 16));
        userInfoPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        JLabel userLabel = new JLabel("Bentornato, " + username, SwingConstants.LEFT);
        userLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
        userLabel.setIcon(new ImageIcon("images/user.png"));

        JButton logoutButton = new JButton("Esci", new ImageIcon("images/logout.png"));
        logoutButton.setHorizontalAlignment(SwingConstants.RIGHT);
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.logout();
            }
        });

        userInfoPanel.add(userLabel, BorderLayout.WEST);
        userInfoPanel.add(logoutButton, BorderLayout.EAST);

        return userInfoPanel;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public Controller getController() {
        return this.controller;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }

    /**
     * 
     * @return
     * @throws Exception
     */
    public DefaultMutableTreeNode getUserCategories() throws Exception {
        try {
            return categoryDAO.getUserCategoriesTree(user);
        } catch (Exception e) {
            throw new Exception();
        }
    }

    /**
     * Crea una nuova categoria definita dall'utente.
     * 
     * @param parent
     *            categoria padre
     * @since 0.2
     */
    public void createCategoryFromUserInput(Category parent) {
        try {
            Category newCategory = new Category(getStringFromUser("Nuova categoria"), parent);

            categoryDAO.saveCategory(newCategory, user);

            for (CategoriesActionListener categoriesListener : categoriesActionListeners) {
                categoriesListener.onCategoriesAdd(newCategory);
            }

            // categoryPanel.addCategoryChildrenNodeToSelected(newCategory); // FIXME:
        } catch (InvalidInputException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Impossibile creare una nuova categoria");
        }
    }

    /**
     * Modifica una categoria esistente.
     * 
     * @param category
     *            categoria da modificare
     * @since 0.2
     */
    public void changeCategoryFromUserInput(Category category) {
        try {
            String newName = getStringFromUser(category.getName());

            categoryDAO.updateCategory(category, newName);

            category.setName(newName);

            for (CategoriesActionListener categoriesListener : categoriesActionListeners) {
                categoriesListener.onCategoriesChange(category);
            }
        } catch (InvalidInputException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Impossibile modificare la categoria");
        }
    }

    /**
     * Elimina una categoria esistente.
     * 
     * @param category
     *            categoria da eliminare
     */
    public void removeCategory(Category category) {
        try {
            int confirmDialogBoxOption = JOptionPane.showConfirmDialog(null, "Sicuro di volere eliminare questa categoria?", "Elimina categoria", JOptionPane.YES_NO_OPTION);

            if (confirmDialogBoxOption == JOptionPane.YES_OPTION) {
                categoryDAO.deleteCategory(category);

                for (CategoriesActionListener categoriesListener : categoriesActionListeners) {
                    categoriesListener.onCategoriesRemove(category);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Impossibile eliminare la categoria");
        }
    }

    /**
     * 
     * @param categoriesListener
     */
    public void addCategoriesActionListener(CategoriesActionListener categoriesListener) {
        categoriesActionListeners.add(categoriesListener);
    }

    private String getStringFromUser(String defaultString) throws Exception {
        String categoryName = (String) JOptionPane.showInputDialog(null, "Nome categoria:", "Nuova categoria", JOptionPane.PLAIN_MESSAGE, null, null, defaultString);

        if (categoryName.isEmpty())
            throw new InvalidInputException("Il nome della categoria non può essere vuoto.");

        return categoryName;
    }

}
