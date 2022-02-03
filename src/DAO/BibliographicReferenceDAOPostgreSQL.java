package DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import Controller.DatabaseController;
import Entities.Author;
import Entities.Category;
import Entities.Tag;
import Entities.User;
import Entities.References.BibliographicReference;
import Entities.References.ReferenceLanguage;
import Entities.References.OnlineResources.*;
import Entities.References.PhysicalResources.*;
import Exceptions.DatabaseConnectionException;
import Exceptions.ReferenceDatabaseException;

/**
 * Implementazione dell'interfaccia BibliographicReferenceDAO per database relazionali PostgreSQL.
 * 
 * @see BibliographicReferenceDAO
 */
public class BibliographicReferenceDAOPostgreSQL implements BibliographicReferenceDAO {

    private User user;

    /**
     * 
     * @param user
     * @throws IllegalArgumentException
     */
    public BibliographicReferenceDAOPostgreSQL(User user) throws IllegalArgumentException {
        setUser(user);
    }

    /**
     * Imposta l'utente di cui recuperare i riferimenti.
     * 
     * @param user
     *            utente di cui recuperare i riferimenti
     * @throws IllegalArgumentException
     *             se {@code user == null}
     */
    public void setUser(User user) {
        if (user == null)
            throw new IllegalArgumentException("user non può essere null");

        this.user = user;
    }

    /**
     * Restituisce l'utente assegnato a questo DAO.
     * 
     * @return utente assegnato al DAO
     */
    public User getUser() {
        return user;
    }

    @Override
    public List<BibliographicReference> getAll() throws ReferenceDatabaseException {
        Connection connection = null;

        Statement referenceStatement = null;
        ResultSet referenceResultSet = null;
        String referenceQuery = "select * from bibliographic_reference natural join website where owner = '" + getUser().getName() + "'";

        PreparedStatement relatedReferencesStatement = null;
        ResultSet relatedReferencesResultSet = null;
        String relatedReferencesQuery = "select * from quotations where quoted_by = ?";

        ArrayList<BibliographicReference> references = new ArrayList<>();
        HashMap<Integer, BibliographicReference> idToReference = new HashMap<>();
        HashMap<BibliographicReference, Collection<Integer>> referenceToRelatedID = new HashMap<>();

        try {
            connection = DatabaseController.getConnection();

            referenceStatement = connection.prepareStatement(referenceQuery);
            relatedReferencesStatement = connection.prepareStatement(relatedReferencesQuery);

            referenceStatement = connection.createStatement();
            referenceResultSet = referenceStatement.executeQuery(referenceQuery);

            while (referenceResultSet.next()) {
                Website website = new Website(referenceResultSet.getString("title"), referenceResultSet.getString("url"));
                website.setID(referenceResultSet.getInt("id"));
                website.setDOI(referenceResultSet.getString("doi"));
                website.setPubblicationDate(referenceResultSet.getDate("pubblication_date"));
                website.setDescription(referenceResultSet.getString("description"));
                website.setLanguage(ReferenceLanguage.getFromString(referenceResultSet.getString("language")));

                // FIXME: categorie

                // FIXME: autori
                // AuthorDAO authorDAO = new AuthorDAO();
                // website.setAuthors(authorDAO.getAuthorsOf(website));

                // FIXME: tag
                // tagsStatement.setInt(1, website.getID());
                // tagsResultSet = tagsStatement.executeQuery();
                // ArrayList<Tag> tags = new ArrayList<>();
                // while (tagsResultSet.next())
                // tags.add(new Tag(tagsResultSet.getString("name")));
                // tags.trimToSize();
                // website.setTags(tags);

                relatedReferencesStatement.setInt(1, website.getID());
                relatedReferencesResultSet = relatedReferencesStatement.executeQuery();

                ArrayList<Integer> relatedReferencesID = new ArrayList<>();

                while (relatedReferencesResultSet.next())
                    relatedReferencesID.add(relatedReferencesResultSet.getInt("has_quoted"));

                relatedReferencesID.trimToSize();

                referenceToRelatedID.put(website, relatedReferencesID);
                idToReference.put(website.getID(), website);

                references.add(website);
            }

            for (BibliographicReference reference : references) {
                ArrayList<BibliographicReference> relatedReferences = new ArrayList<>();

                for (Integer referenceID : referenceToRelatedID.get(reference)) {
                    relatedReferences.add(idToReference.get(referenceID));
                }

                relatedReferences.trimToSize();

                reference.setRelatedReferences(relatedReferences);
            }

            return references;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ReferenceDatabaseException("Impossibile recuperare i riferimenti dell'utente.");
        } finally {
            try {
                if (referenceResultSet != null)
                    referenceResultSet.close();

                if (referenceStatement != null)
                    referenceStatement.close();

                if (connection != null)
                    connection.close();
            } catch (Exception e) {
                // non fare niente
            }
        }

    }

    /**
     * {@inheritDoc}
     * 
     * @throws IllegalArgumentException
     *             se {@code reference == null}
     */
    @Override
    public void remove(BibliographicReference reference) throws ReferenceDatabaseException {
        if (reference == null)
            throw new IllegalArgumentException("reference can't be null");

        Connection connection = null;
        Statement statement = null;
        String query = "delete from bibliographic_reference where id = " + reference.getID();

        try {
            connection = DatabaseController.getConnection();
            statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (Exception e) {
            throw new ReferenceDatabaseException("Impossibile rimuovere il riferimento.");
        } finally {
            try {
                if (statement != null)
                    statement.close();

                if (connection != null)
                    connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // https://stackoverflow.com/a/63970374

    @Override
    public void save(Article article) throws ReferenceDatabaseException {
        if (article == null)
            return;

        int pageCount = article.getPageCount();
        String url = format(article.getURL());
        String publisher = format(article.getPublisher());
        String issn = format(article.getISSN());

        String query = "insert into article(id, page_count, url, publisher, issn) values("
                + article.getID() + "," + pageCount + "," + url + "," + publisher + "," + issn
                + ") on conflict (id) do update set page_count = " + pageCount + ", url = " + url + ", publisher = " + publisher + ", issn = " + issn;

        save(article, query);
    }

    @Override
    public void save(Book book) throws ReferenceDatabaseException {
        if (book == null)
            return;

        int pageCount = book.getPageCount();
        String url = format(book.getURL());
        String publisher = format(book.getPublisher());
        String isbn = format(book.getISBN());

        String query = "insert into article(id, page_count, url, publisher, isbn) values("
                + book.getID() + "," + pageCount + "," + url + "," + publisher + "," + isbn
                + ") on conflict (id) do update set page_count = " + pageCount + ", url = " + url + ", publisher = " + publisher + ", isbn = " + isbn;

        save(book, query);
    }

    @Override
    public void save(Thesis thesis) throws ReferenceDatabaseException {
        if (thesis == null)
            return;

        int pageCount = thesis.getPageCount();
        String url = format(thesis.getURL());
        String publisher = format(thesis.getPublisher());
        String university = format(thesis.getUniversity());
        String faculty = format(thesis.getFaculty());

        String query = "insert into article(id, page_count, url, publisher, university, faculty) values("
                + thesis.getID() + "," + pageCount + "," + url + "," + publisher + "," + university + "," + faculty
                + ") on conflict (id) do update set page_count = " + pageCount + ", url = " + url + ", publisher = " + publisher + ", university = " + university + ", faculty = " + faculty;

        save(thesis, query);
    }

    @Override
    public void save(Image image) throws ReferenceDatabaseException {
        // TODO Auto-generated method stub

    }

    @Override
    public void save(SourceCode sourceCode) throws ReferenceDatabaseException {
        // TODO Auto-generated method stub

    }

    @Override
    public void save(Video video) throws ReferenceDatabaseException {
        // TODO Auto-generated method stub

    }

    @Override
    public void save(Website website) throws ReferenceDatabaseException {
        if (website == null)
            return;

        String url = format(website.getURL());

        String query = "insert into website(id, url) values(" + website.getID() + "," + url + ") on conflict (id) do update set url = " + url;

        save(website, query);
    }

    private void save(BibliographicReference reference, String query) throws ReferenceDatabaseException {
        if (reference == null)
            return;

        Connection connection = null;
        PreparedStatement referenceStatement = null;
        Statement subStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseController.getConnection();
            connection.setAutoCommit(false);

            String sql = null;

            if (reference.getID() == null)
                sql = "insert into bibliographic_reference(owner, title, doi, description, language, pubblication_date) values(?,?,?,?,?,?) on conflict";
            else
                sql = "update bibliographic_reference set owner = ?, title = ?, doi = ?, description = ?, language = ?, pubblication_date = ? where id = " + reference.getID();

            referenceStatement = connection.prepareStatement(sql);

            referenceStatement.setString(1, getUser().getName());
            referenceStatement.setString(2, format(reference.getTitle()));
            referenceStatement.setString(3, format(reference.getDOI()));
            referenceStatement.setString(4, format(reference.getDescription()));

            String language = reference.getLanguage() == ReferenceLanguage.NOTSPECIFIED ? null : reference.getLanguage().name();
            referenceStatement.setString(5, reference.getLanguage() == ReferenceLanguage.NOTSPECIFIED ? null : language);

            Date date = reference.getPubblicationDate() == null ? null : new Date(reference.getPubblicationDate().getTime());
            referenceStatement.setDate(6, date);

            referenceStatement.executeUpdate();

            if (reference.getID() == null) {
                resultSet = referenceStatement.getGeneratedKeys();

                if (resultSet.next())
                    reference.setID(resultSet.getInt(1));
            }

            subStatement = connection.createStatement();

            subStatement.executeUpdate(query);

            connection.commit();
        } catch (SQLException | DatabaseConnectionException e) {
            try {
                connection.rollback();
            } catch (Exception r) {
                // non fare niente
            }

            throw new ReferenceDatabaseException("Impossibile aggiungere nuovo riferimento.");
        } finally {
            try {
                if (resultSet != null)
                    resultSet.close();

                if (subStatement != null)
                    subStatement.close();

                if (referenceStatement != null)
                    referenceStatement.close();

                if (connection != null)
                    connection.close();
            } catch (Exception e) {
                // non fare niente
            }
        }
    }

    private String format(String input) {
        if (input == null || input.isBlank())
            return null;

        return "'" + input + "'";
    }

}
