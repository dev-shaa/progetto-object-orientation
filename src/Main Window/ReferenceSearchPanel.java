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

    private MainWindow controller;

    private TextSearchPanel tagSearchField;
    private TextSearchPanel authorSearchField;
    private TextSearchPanel categoriesSearchField;
    private DatePickerPanel earliestDate;
    private DatePickerPanel latestDate;

    public ReferenceSearchPanel(MainWindow controller) {
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
        searchPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Autori
        authorSearchField = new TextSearchPanel("Autori:", "Autori da ricercare, separati da virgole (esempio: Mario Rossi, Ciro Esposito)");
        authorSearchField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        authorSearchField.setAlignmentX(JTextField.LEFT_ALIGNMENT);

        searchPanel.add(authorSearchField);
        searchPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Categorie
        categoriesSearchField = new TextSearchPanel("Categorie:", "Categorie in cui ricercare, separate da virgole (esempio: Informatica, Basi di Dati)");
        categoriesSearchField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        categoriesSearchField.setAlignmentX(JTextField.LEFT_ALIGNMENT);

        // TODO: suggerimenti

        searchPanel.add(categoriesSearchField);
        searchPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Data
        earliestDate = new DatePickerPanel("Da:");
        earliestDate.setAlignmentX(DatePickerPanel.LEFT_ALIGNMENT);
        earliestDate.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));

        searchPanel.add(earliestDate);
        searchPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        latestDate = new DatePickerPanel("A:");
        latestDate.setAlignmentX(DatePickerPanel.LEFT_ALIGNMENT);
        latestDate.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));

        searchPanel.add(latestDate);
        searchPanel.add(Box.createRigidArea(new Dimension(0, 50)));

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
