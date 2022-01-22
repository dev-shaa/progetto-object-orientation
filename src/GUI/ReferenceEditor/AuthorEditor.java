package GUI.ReferenceEditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import DAO.AuthorDAO;
import Entities.Author;

// import Entities.Author;

public class AuthorEditor extends JDialog {

    private static JTextField firstName;
    private static JTextField lastName;
    private static JTextField ORCID;

    private AuthorDAO authorDAO;

    private final Dimension maximumSize = new Dimension(Integer.MAX_VALUE, 24);
    private final float alignment = Container.CENTER_ALIGNMENT;

    public AuthorEditor() {
        super();

        authorDAO = new AuthorDAO();

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
        contentPane.setBorder(new EmptyBorder(30, 30, 30, 30));
        setContentPane(contentPane);

        setTitle("Nuovo autore");
        setSize(300, 300);
        setResizable(false);
        setModal(true);

        firstName = new JTextField();
        addField("Nome", firstName);

        lastName = new JTextField();
        addField("Cognome", lastName);

        ORCID = new JTextField();
        addField("ORCID", ORCID);

        JButton confirmButton = new JButton("Salva");
        confirmButton.setAlignmentX(alignment);
        confirmButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                saveAuthor();
            }

        });

        contentPane.add(confirmButton);
    }

    private void addField(String label, JComponent component) {
        JLabel labelField = new JLabel(label);
        labelField.setMaximumSize(maximumSize);
        labelField.setAlignmentX(alignment);

        component.setMaximumSize(maximumSize);
        component.setAlignmentX(alignment);

        getContentPane().add(labelField);
        getContentPane().add(component);
        getContentPane().add(Box.createVerticalGlue());
    }

    private void saveAuthor() {
        try {
            authorDAO.SaveAuthor(new Author(firstName.getText().trim(), lastName.getText().trim(), ORCID.getText().trim()));

            setVisible(false);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Impossibile creare un nuovo autore", "Errore creazione autore", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void setVisible(boolean b) {
        if (b) {
            firstName.setName(null);
            lastName.setName(null);
            ORCID.setName(null);
        }

        super.setVisible(b);
    }

}
