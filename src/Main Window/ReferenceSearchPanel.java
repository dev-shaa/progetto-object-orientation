import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.FlowLayout;
import java.util.ArrayList;

public class ReferenceSearchPanel extends JPanel {

    public ReferenceSearchPanel() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBorder(new EmptyBorder(5, 5, 5, 5));

        ActionListener searchListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO: cerca
                // searchReferences(searchBox.getText());
            }
        };

        JTextField searchBox = new JTextField();
        searchBox.setToolTipText("Cerca riferimento per nome, autore o parole chiave");
        searchBox.setMaximumSize(new Dimension(300, Integer.MAX_VALUE));
        searchBox.addActionListener(searchListener);

        JButton searchButton = new JButton(new ImageIcon("images/search.png"));
        searchButton.setToolTipText("Cerca riferimento per nome, autore o parole chiave");
        searchButton.addActionListener(searchListener);

        add(searchBox);
        add(searchButton);
    }

}
