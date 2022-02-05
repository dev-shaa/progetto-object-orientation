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

        PreparedStatement tagsStatement = null;
        ResultSet tagsResultSet = null;
        String tagsCommand = "select name from tag where reference = ?";

        PreparedStatement relatedReferencesStatement = null;
        ResultSet relatedReferencesResultSet = null;
        String relatedReferencesQuery = "select * from related_references where quoted_by = ?";

        ArrayList<BibliographicReference> references = new ArrayList<>();
        HashMap<Integer, BibliographicReference> idToReference = new HashMap<>();
        HashMap<BibliographicReference, Collection<Integer>> referenceToRelatedID = new HashMap<>();

        try {
            connection = DatabaseController.getConnection();

            tagsStatement = connection.prepareStatement(tagsCommand);
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

                // TODO: recupero autori
                // AuthorDAO authorDAO = new AuthorDAO();
                // website.setAuthors(authorDAO.getAuthorsOf(website));

                // RECUPERO TAG
                tagsStatement.setInt(1, website.getID());
                tagsResultSet = tagsStatement.executeQuery();
                ArrayList<Tag> tags = new ArrayList<>();

                while (tagsResultSet.next())
                    tags.add(new Tag(tagsResultSet.getString("name")));

                tags.trimToSize();
                website.setTags(tags);

                // RECUPERO ID RIMANDI
                relatedReferencesStatement.setInt(1, website.getID());
                relatedReferencesResultSet = relatedReferencesStatement.executeQuery();

                ArrayList<Integer> relatedReferencesID = new ArrayList<>();

                while (relatedReferencesResultSet.next())
                    relatedReferencesID.add(relatedReferencesResultSet.getInt("quotes"));

                relatedReferencesID.trimToSize();
                referenceToRelatedID.put(website, relatedReferencesID);
                idToReference.put(website.getID(), website);

                references.add(website);
            }

            // ASSEGNAZIONE RIMANDI
            for (BibliographicReference reference : references) {
                ArrayList<BibliographicReference> relatedReferences = new ArrayList<>();

                for (Integer referenceID : referenceToRelatedID.get(reference))
                    relatedReferences.add(idToReference.get(referenceID));

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

        String pageCount = article.getPageCount() == 0 ? "null" : String.valueOf(article.getPageCount());

        String command = null;

        if (article.getID() == null)
            command = "insert into article(id, page_count, url, publisher, issn) values(?," + pageCount + "," + article.getURL() + "," + article.getPublisher() + "," + article.getISSN() + ")";
        else
            command = "update article set page_count = " + pageCount + ", url = " + article.getURL() + ", publisher = " + article.getPublisher() + ", issn = " + article.getISSN() + " where id = ?";

        save(article, command);
    }

    @Override
    public void save(Book book) throws ReferenceDatabaseException {
        if (book == null)
            return;

        int pageCount = book.getPageCount();
        String url = format(book.getURL());
        String publisher = format(book.getPublisher());
        String isbn = format(book.getISBN());

        String query = "insert into book(id, page_count, url, publisher, isbn) values(?," + pageCount + "," + url + "," + publisher + "," + isbn
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

        String query = "insert into thesis(id, page_count, url, publisher, university, faculty) values(?," + pageCount + "," + url + "," + publisher + "," + university + "," + faculty
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

        String command = null;

        if (website.getID() == null)
            command = "insert into website(id, url) values(? ," + website.getURL() + ")";
        else
            command = "update website set url = " + website.getURL() + " where id = ?";

        save(website, command);
    }

    private void save(BibliographicReference reference, String subReferenceCommand) throws ReferenceDatabaseException {
        if (reference == null)
            return;

        Connection connection = null;
        PreparedStatement referenceStatement = null;
        PreparedStatement subReferenceStatement = null;
        PreparedStatement relatedReferenceRemoveStatement = null;
        PreparedStatement relatedReferenceInsertStatement = null;
        PreparedStatement categoriesRemoveStatement = null;
        PreparedStatement categoriesInsertStatement = null;
        PreparedStatement tagRemoveStatement = null;
        PreparedStatement tagInsertStatement = null;
        PreparedStatement authorsRemoveStatement = null;
        PreparedStatement authorsInsertStatement = null;
        ResultSet resultSet = null;

        String referenceInsertCommand = null;
        String relatedReferenceRemoveCommand = "delete from related_references where quoted_by = ?";
        String relatedReferenceInsertCommand = "insert into related_references values(?, ?)";
        String categoriesRemoveCommand = "delete from category_reference_association where reference = ?";
        String categoriesInsertCommand = "insert into category_reference_association values(?, ?)";
        String tagRemoveCommand = "delete from tag where reference = ?";
        String tagInsertCommand = "insert into tag values(?, ?)";
        String authorsRemoveCommand = "delete from author_reference_association where reference = ?";
        String authorsInsertCommand = "insert into author_reference_association values(?, ?)";

        String language = format(reference.getLanguage() == ReferenceLanguage.NOTSPECIFIED ? null : reference.getLanguage().name());

        if (reference.getID() == null)
            referenceInsertCommand = "insert into bibliographic_reference(owner, title, doi, description, language, pubblication_date) values(?,?,?,?," + language + ",?)";
        else
            referenceInsertCommand = "update bibliographic_reference set owner = ?, title = ?, doi = ?, description = ?, language = " + language + ", pubblication_date = ? where id = " + reference.getID();

        try {
            connection = DatabaseController.getConnection();
            connection.setAutoCommit(false);

            referenceStatement = connection.prepareStatement(referenceInsertCommand, Statement.RETURN_GENERATED_KEYS);
            subReferenceStatement = connection.prepareStatement(subReferenceCommand);
            relatedReferenceRemoveStatement = connection.prepareStatement(relatedReferenceRemoveCommand);
            relatedReferenceInsertStatement = connection.prepareStatement(relatedReferenceInsertCommand);
            categoriesRemoveStatement = connection.prepareStatement(categoriesRemoveCommand);
            categoriesInsertStatement = connection.prepareStatement(categoriesInsertCommand);
            tagRemoveStatement = connection.prepareStatement(tagRemoveCommand);
            tagInsertStatement = connection.prepareStatement(tagInsertCommand);
            authorsRemoveStatement = connection.prepareStatement(authorsRemoveCommand);
            authorsInsertStatement = connection.prepareStatement(authorsInsertCommand);

            referenceStatement.setString(1, getUser().getName());
            referenceStatement.setString(2, reference.getTitle());
            referenceStatement.setString(3, reference.getDOI());
            referenceStatement.setString(4, format(reference.getDescription()));

            Date date = reference.getPubblicationDate() == null ? null : new Date(reference.getPubblicationDate().getTime());
            referenceStatement.setDate(5, date);

            referenceStatement.executeUpdate();

            if (reference.getID() == null) {
                resultSet = referenceStatement.getGeneratedKeys();

                if (resultSet.next())
                    reference.setID(resultSet.getInt(1));
            }

            subReferenceStatement.setInt(1, reference.getID());
            subReferenceStatement.executeUpdate();

            relatedReferenceRemoveStatement.setInt(1, reference.getID());
            relatedReferenceRemoveStatement.executeUpdate();

            for (BibliographicReference relatedReference : reference.getRelatedReferences()) {
                relatedReferenceInsertStatement.setInt(1, reference.getID());
                relatedReferenceInsertStatement.setInt(2, relatedReference.getID());
                relatedReferenceInsertStatement.executeUpdate();
            }

            categoriesRemoveStatement.setInt(1, reference.getID());
            categoriesRemoveStatement.executeUpdate();

            for (Category category : reference.getCategories()) {
                categoriesInsertStatement.setInt(1, category.getID());
                categoriesInsertStatement.setInt(2, reference.getID());
                categoriesInsertStatement.executeUpdate();
            }

            tagRemoveStatement.setInt(1, reference.getID());
            tagRemoveStatement.executeUpdate();

            for (Tag tag : reference.getTags()) {
                tagInsertStatement.setString(1, tag.getName());
                tagInsertStatement.setInt(2, reference.getID());
                tagInsertStatement.executeUpdate();
            }

            // TODO: autori
            // authorsRemoveStatement.setInt(1, reference.getID());

            // for (Author author : reference.getAuthors()) {

            // }

            connection.commit();
        } catch (SQLException | DatabaseConnectionException e) {
            try {
                connection.rollback();
            } catch (Exception r) {
                // non fare niente
            }

            e.printStackTrace();
            throw new ReferenceDatabaseException("Impossibile aggiungere nuovo riferimento.");
        } finally {
            try {
                if (resultSet != null)
                    resultSet.close();

                if (relatedReferenceInsertStatement != null)
                    relatedReferenceInsertStatement.close();

                if (relatedReferenceRemoveStatement != null)
                    relatedReferenceRemoveStatement.close();

                if (subReferenceStatement != null)
                    subReferenceStatement.close();

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
