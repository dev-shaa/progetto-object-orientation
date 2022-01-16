package GUI.ReferenceEditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import Entities.Author;

public class AuthorEditor extends JDialog {

    private static JTextField firstName;
    private static JTextField lastName;
    private static JTextField ORCID;

    public AuthorEditor() {
        super();

        setTitle("Nuovo autore");
        setSize(300, 300);
        setModal(true);

        firstName = new JTextField();
        lastName = new JTextField();
        ORCID = new JTextField();

        JButton confirmButton = new JButton("Salva");
        confirmButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                saveAuthor();
            }

        });

        add(firstName);
        add(lastName);
        add(ORCID);
        add(confirmButton);

        setVisible(true);
    }

    private static Author saveAuthor() {
        try {
            Author newAuthor = new Author(firstName.getText().trim(), lastName.getText().trim(), ORCID.getText().trim());
            // TODO: salva nel database
            return newAuthor;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Impossibile creare un nuovo autore", "Errore creazione autore", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

}
