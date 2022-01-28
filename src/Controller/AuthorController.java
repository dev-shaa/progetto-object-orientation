package Controller;

import java.util.ArrayList;
import java.util.List;

import DAO.AuthorDAO;
import Entities.Author;

public class AuthorController {

    private AuthorDAO authorDAO;
    private ArrayList<Author> authors;

    public AuthorController(AuthorDAO authorDAO) {
        this.setAuthorDAO(authorDAO);
    }

    public AuthorDAO getAuthorDAO() {
        return authorDAO;
    }

    public void setAuthorDAO(AuthorDAO authorDAO) {
        if (authorDAO == null)
            throw new IllegalArgumentException("authorDAO can't be null");

        this.authorDAO = authorDAO;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void saveAuthor(Author author) {

    }
}
