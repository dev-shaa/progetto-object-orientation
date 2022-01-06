import javax.swing.*;
import javax.swing.border.*;
// import javax.swing.plaf.PopupMenuUI;

// import javax.swing.table.DefaultTableModel;
// import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
// import java.awt.GridBagLayout;
// import java.util.*;
// import java.util.ArrayList;
// import java.util.Date;

public class ReferenceSearchPanel extends JPanel {

    private Homepage controller;

    private TextSearchPanel tagSearchField;
    private TextSearchPanel authorSearchField;
    private TextSearchPanel categoriesSearchField;
    private DatePickerPanel datePicker;

    public ReferenceSearchPanel(Homepage controller) {
        this.controller = controller;

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

        // Parole chiave
        tagSearchField = new TextSearchPanel("Parole chiave:", "Parole chiave da ricercare, separate da virgole (esempio: Liste, Alberi)");
        tagSearchField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        tagSearchField.setAlignmentX(JTextField.LEFT_ALIGNMENT);

        searchPanel.add(tagSearchField);

        // Autori
        authorSearchField = new TextSearchPanel("Autori:", "Autori da ricercare, separati da virgole (esempio: Mario Rossi, Ciro Esposito)");
        authorSearchField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        authorSearchField.setAlignmentX(JTextField.LEFT_ALIGNMENT);

        searchPanel.add(authorSearchField);

        // Categorie
        categoriesSearchField = new TextSearchPanel("Categorie:", "Categorie in cui ricercare, separate da virgole (esempio: Informatica, Basi di Dati)");
        categoriesSearchField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        categoriesSearchField.setAlignmentX(JTextField.LEFT_ALIGNMENT);

        // TODO: suggerimenti

        searchPanel.add(categoriesSearchField);

        // Data
        datePicker = new DatePickerPanel();
        datePicker.setMaximumSize(new Dimension(Integer.MAX_VALUE, 106));
        datePicker.setAlignmentX(DatePickerPanel.LEFT_ALIGNMENT);

        searchPanel.add(datePicker);

        Component spacing = Box.createVerticalGlue();
        spacing.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        searchPanel.add(spacing);

        searchPanel.add(getSearchButton());

        return searchPanel;
    }

    private JButton getSearchButton() {
        JButton searchButton = new JButton("Cerca");
        searchButton.setIcon(new ImageIcon("images/search.png"));
        searchButton.setAlignmentX(JButton.LEFT_ALIGNMENT);
        searchButton.setMaximumSize(new Dimension(100, 32));
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO: cerca
            }
        });

        return searchButton;
    }

}