package GUI.ReferenceEditor;

import GUI.Categories.*;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;

import DAO.BibliographicReferenceDAO;
import Entities.References.PhysicalResources.Book;

public class BookCreator extends PublicationCreator {

    private JTextField ISBN;

    /**
     * TODO: commenta
     * 
     * @param categoriesTreeManager
     * @param referenceDAO
     */
    public BookCreator(CategoriesTreeManager categoriesTreeManager, BibliographicReferenceDAO referenceDAO) {
        this("Libro", categoriesTreeManager, referenceDAO);
    }

    private BookCreator(String dialogueTitle, CategoriesTreeManager categoriesTreeManager, BibliographicReferenceDAO referenceDAO) throws IllegalArgumentException {
        super(dialogueTitle, categoriesTreeManager, referenceDAO);
    }

    @Override
    protected void setup(CategoriesTreeManager categoriesTreeManager) {
        super.setup(categoriesTreeManager);

        ISBN = new JTextField();

        addComponent("ISBN", ISBN);
    }

    protected String getISBN() {
        return ISBN.getText().trim();
    }

    @Override
    protected void onConfirmClick() {
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
}
