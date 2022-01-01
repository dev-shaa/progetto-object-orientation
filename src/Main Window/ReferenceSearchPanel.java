import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.PopupMenuUI;

// import javax.swing.table.DefaultTableModel;
// import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.GridBagLayout;
import java.util.*;
// import java.util.ArrayList;
import java.util.Date;

public class ReferenceSearchPanel extends JPanel {

    MainWindow controller;

    public ReferenceSearchPanel(MainWindow controller) {
        this.controller = controller;

        setLayout(new BorderLayout(5, 5));
        setBorder(new EmptyBorder(5, 5, 5, 5));

        add(new JLabel("<html><b>Ricerca riferimenti</b></html>"), BorderLayout.NORTH);
        add(getSearchFieldPanel(), BorderLayout.CENTER);
    }

    private JPanel getSearchFieldPanel() {
        JPanel searchPanel = new JPanel();
        searchPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.PAGE_AXIS));

        // Parole chiave
        TextSearchField tagSearchField = new TextSearchField("Parole chiave:",
                "Parole chiave da ricercare, separate da virgole (esempio: Informatica, Programmazione)");
        tagSearchField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        tagSearchField.setAlignmentX(JTextField.LEFT_ALIGNMENT);

        searchPanel.add(tagSearchField);
        searchPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Autori
        TextSearchField authorSearchField = new TextSearchField("Autori:",
                "Autori da ricercare, separati da virgole (esempio: Mario Rossi, Ciro Esposito)");
        authorSearchField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        authorSearchField.setAlignmentX(JTextField.LEFT_ALIGNMENT);

        searchPanel.add(authorSearchField);
        searchPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // TODO: lista categorie
        SelectionPopupMenu<String> categoriesSelectionMenu = new SelectionPopupMenu<String>(
                new String[] { "aaa", "bbb" });
        categoriesSelectionMenu.setAlignmentX(JButton.LEFT_ALIGNMENT);
        categoriesSelectionMenu.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));

        searchPanel.add(categoriesSelectionMenu);

        // Data
        DatePickerPanel earliestDate = new DatePickerPanel("Da:");
        earliestDate.setAlignmentX(DatePickerPanel.LEFT_ALIGNMENT);
        earliestDate.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));

        searchPanel.add(earliestDate);
        searchPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        DatePickerPanel latestDate = new DatePickerPanel("A:");
        latestDate.setAlignmentX(DatePickerPanel.LEFT_ALIGNMENT);
        latestDate.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));

        searchPanel.add(latestDate);
        searchPanel.add(Box.createRigidArea(new Dimension(0, 50)));

        // Pulsante di ricerca
        JButton searchButton = new JButton("Cerca");
        searchButton.setIcon(new ImageIcon("images/search.png"));
        searchButton.setAlignmentX(JButton.LEFT_ALIGNMENT);
        searchButton.setMaximumSize(new Dimension(100, 32));
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO: cerca
                // controller.searchReferences(keywords, categories, startDate, endDate);
            }
        });

        searchPanel.add(searchButton);

        return searchPanel;
    }

}
