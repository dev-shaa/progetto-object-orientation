import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Pannello per la ricerca dei riferimenti per parole chiave, autori, categorie e data.
 */
public class SearchPanel extends JPanel {

    private TextSearchPanel tagSearchField;
    private TextSearchPanel authorSearchField;
    private TreeSelectionPanel categoriesSearchField;
    private DatePickerPanel datePicker;

    private ReferencePanel referencePanel;
    private CategoriesTree categoriesTree;

    /**
     * Crea {@code ReferenceSearchPanel}.
     * 
     * @param referencePanel
     * @param categoriesTree
     */
    public SearchPanel(ReferencePanel referencePanel, CategoriesTree categoriesTree) {
        // TODO: getter setter
        this.referencePanel = referencePanel;
        this.categoriesTree = categoriesTree;

        setLayout(new BorderLayout(5, 5));
        setBorder(new EmptyBorder(5, 5, 5, 5));

        add(getPanelLabel(), BorderLayout.NORTH);
        add(getSearchFieldPanel(), BorderLayout.CENTER);
    }

    private JLabel getPanelLabel() {
        return new JLabel("<html><b>Ricerca riferimenti</b></html>");
    }

    private JPanel getSearchFieldPanel() {
        JPanel searchPanel = new JPanel();
        searchPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.PAGE_AXIS));

        setupTagSearchField();
        searchPanel.add(tagSearchField);

        setupAuthorsSearchField();
        searchPanel.add(authorSearchField);

        setupCategoriesSearchField();
        searchPanel.add(categoriesSearchField);

        setupDatePicker();
        searchPanel.add(datePicker);

        Component spacing = Box.createVerticalGlue();
        spacing.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        searchPanel.add(spacing);

        searchPanel.add(getSearchButton());

        return searchPanel;
    }

    private void setupTagSearchField() {
        tagSearchField = new TextSearchPanel("Parole chiave:", "Parole chiave da ricercare, separate da virgole (esempio: Liste, Alberi)");
        tagSearchField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        tagSearchField.setAlignmentX(JTextField.LEFT_ALIGNMENT);
    }

    private void setupAuthorsSearchField() {
        authorSearchField = new TextSearchPanel("Autori:", "Autori da ricercare, separati da virgole (esempio: Mario Rossi, Ciro Esposito)");
        authorSearchField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        authorSearchField.setAlignmentX(JTextField.LEFT_ALIGNMENT);
    }

    private void setupCategoriesSearchField() {
        categoriesSearchField = new TreeSelectionPanel(categoriesTree);
        categoriesSearchField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        categoriesSearchField.setAlignmentX(TreeSelectionPanel.LEFT_ALIGNMENT);
    }

    private void setupDatePicker() {
        datePicker = new DatePickerPanel();
        datePicker.setMaximumSize(new Dimension(Integer.MAX_VALUE, 106));
        datePicker.setAlignmentX(DatePickerPanel.LEFT_ALIGNMENT);
    }

    private JButton getSearchButton() {
        JButton searchButton = new JButton("Cerca");
        searchButton.setIcon(new ImageIcon("images/search.png"));
        searchButton.setAlignmentX(JButton.LEFT_ALIGNMENT);
        searchButton.setMaximumSize(new Dimension(100, 32));
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO: cerca
                // TODO: carica dal database
                // TODO: imposta riferimenti da mostrare
            }
        });

        return searchButton;
    }

}
