package GUI.Homepage.References.Editor;

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

import Controller.AuthorController;
import Entities.Author;

/**
 * Finestra di dialogo per la registrazione di un nuovo autore.
 */
public class AuthorEditor extends JDialog {

    private JTextField firstName;
    private JTextField lastName;
    private JTextField ORCID;

    private AuthorController controller;

    private final Dimension maximumSize = new Dimension(Integer.MAX_VALUE, 24);
    private final float alignment = Container.CENTER_ALIGNMENT;

    /**
     * Crea una nuova finestra di dialogo.
     * 
     * @param controller
     *            controller degli autori per la registrazione
     * @throws IllegalArgumentException
     *             se {@code controller == null}
     */
    public AuthorEditor(AuthorController controller) {
        super();

        setController(controller);

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

    /**
     * Imposta il controller degli autori.
     * 
     * @param controller
     *            controller degli autori
     * @throws IllegalArgumentException
     *             se {@code controller == null}
     */
    public void setController(AuthorController controller) {
        if (controller == null)
            throw new IllegalArgumentException("controller can't be null");

        this.controller = controller;
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
            controller.saveAuthor(new Author(firstName.getText().trim(), lastName.getText().trim(), ORCID.getText().trim()));

            setVisible(false);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Impossibile creare un nuovo autore", "Errore creazione autore", JOptionPane.ERROR_MESSAGE);
        }
    }

}
