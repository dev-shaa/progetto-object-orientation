package GUI.Authors;

import java.util.ArrayList;
import java.util.List;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import Entities.Author;
import Exceptions.Input.InvalidAuthorInputException;

/**
 * Un pannello che permette all'utente di inserire degli autori.
 */
public class AuthorInputField extends JPanel {

    private JTextField firstAuthorField;
    private ArrayList<JTextField> otherAuthorsFields;

    private final Dimension maximumSize = new Dimension(Integer.MAX_VALUE, 24);
    private final ImageIcon removeIcon = new ImageIcon("images/button_remove.png");

    private final String addTooltip = "Aggiungi autore";
    private final String removeTooltip = "Rimuovi autore";
    private final String textTooltip = "Autore del riferimento.\nÈ possibile specificare l'ORCID mettendolo tra parentesi quadre.\nEsempio: \"Mario Rossi [0000-0000-0000-0000]\"";

    /**
     * Crea un nuovo {@code AuthorInputField}.
     */
    public AuthorInputField() {
        super();

        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        otherAuthorsFields = new ArrayList<>();

        addFirstAuthorField();
    }

    /**
     * Imposta gli autori da mostrare.
     * <p>
     * Se {@code authors} è nullo o vuoto, ha lo stesso effetto di chiamare {@link #clear()}.
     * 
     * @param authors
     *            autori da mostrare
     */
    public void setAuthors(List<Author> authors) {
        clear();

        if (authors == null || authors.isEmpty())
            return;

        firstAuthorField.setText(getStringFromAuthor(authors.get(0)));

        for (int i = 1; i < authors.size(); i++)
            addAuthorField(authors.get(i));

        revalidate();
    }

    /**
     * Restituisce gli autori inseriti dall'utente.
     * 
     * @return lista di autori
     */
    public ArrayList<Author> getAuthors() throws InvalidAuthorInputException {
        ArrayList<Author> authors = new ArrayList<>(otherAuthorsFields.size() + 1);

        Author firstAuthor = getAuthorFromString(firstAuthorField.getText());

        if (firstAuthor != null)
            authors.add(firstAuthor);

        for (JTextField authorField : otherAuthorsFields) {
            Author author = getAuthorFromString(authorField.getText());

            if (author != null && !authors.contains(author))
                authors.add(author);
        }

        authors.trimToSize();

        return authors;
    }

    /**
     * Reimposta tutti i campi.
     */
    public void clear() {
        otherAuthorsFields.clear();
        removeAll();

        addFirstAuthorField();

        revalidate();
    }

    private void addFirstAuthorField() {
        JPanel authorPanel = new JPanel(new BorderLayout(10, 0));
        authorPanel.setMaximumSize(maximumSize);

        firstAuthorField = new JTextField();
        firstAuthorField.setToolTipText(textTooltip);

        JButton addButton = new JButton(new ImageIcon("images/button_add.png"));
        addButton.setToolTipText(addTooltip);

        authorPanel.add(firstAuthorField, BorderLayout.CENTER);
        authorPanel.add(addButton, BorderLayout.EAST);

        JPanel thisPanel = this;
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addAuthorField(null);

                thisPanel.revalidate();
            }
        });

        add(authorPanel);
        add(Box.createVerticalStrut(10));
    }

    private void addAuthorField(Author author) {
        JPanel authorPanel = new JPanel(new BorderLayout(10, 0));
        authorPanel.setMaximumSize(maximumSize);

        JTextField textField = new JTextField();
        textField.setToolTipText(textTooltip);

        if (author != null)
            textField.setText(getStringFromAuthor(author));

        JButton removeButton = new JButton(removeIcon);
        removeButton.setToolTipText(removeTooltip);

        Component spacing = Box.createVerticalStrut(10);

        authorPanel.add(textField, BorderLayout.CENTER);
        authorPanel.add(removeButton, BorderLayout.EAST);
        otherAuthorsFields.add(textField);

        JPanel thisPanel = this;
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                otherAuthorsFields.remove(textField);

                thisPanel.remove(authorPanel);
                thisPanel.remove(spacing);
                thisPanel.revalidate();
            }
        });

        add(authorPanel);
        add(spacing);
    }

    private Author getAuthorFromString(String string) throws InvalidAuthorInputException {
        if (string == null || string.isEmpty() || string.isBlank())
            return null;

        int orcidStartIndex = string.indexOf('[');
        int orcidEndIndex = string.lastIndexOf(']');

        String orcid = null;

        if (orcidStartIndex != -1 && orcidEndIndex != -1 && orcidStartIndex < orcidEndIndex) {
            orcid = string.substring(orcidStartIndex + 1, orcidEndIndex);
        }

        String name = string.substring(0, orcidStartIndex == -1 ? string.length() : orcidStartIndex);

        try {
            return new Author(name, orcid);
        } catch (IllegalArgumentException e) {
            throw new InvalidAuthorInputException("L'ORCID inserito non è valido.");
        }
    }

    private String getStringFromAuthor(Author author) {
        if (author == null)
            return null;

        String output = author.getName();

        if (author.getORCID() != null)
            output += " [" + author.getORCID() + "]";

        return output;
    }

}
