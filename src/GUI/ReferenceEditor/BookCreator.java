package GUI.ReferenceEditor;

import GUI.Categories.*;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;

import Entities.References.PhysicalResources.Book;

public class BookCreator extends PublicationCreator {

    private JTextField ISBN;

    public BookCreator(CategoriesTreeManager categoriesTreeManager) {
        this("Libro", categoriesTreeManager);
    }

    private BookCreator(String dialogueTitle, CategoriesTreeManager categoriesTreeManager) {
        super(dialogueTitle, categoriesTreeManager);
    }

    @Override
    protected void setup(CategoriesTreeManager categoriesTreeManager) {
        super.setup(categoriesTreeManager);

        ISBN = new JTextField();

        addComponent("Università", ISBN);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Book book = new Book(getReferenceTitle());
        book.setDOI(getDOI());
        book.setDescription(getDescription());
        book.setISBN(getISBN());
        book.setLanguage(getLanguage());
        book.setPubblicationDate(getPubblicationDate());
        book.setPublisher(getPublisher());
        book.setTags(getTags());
        book.setURL(getURL());

        // TODO: salva nel database

        // TODO: autori, page count
    }

    protected String getISBN() {
        return ISBN.getText().trim();
    }
}
