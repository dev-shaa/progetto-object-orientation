package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import Controller.DatabaseController;
import Entities.*;
import Entities.References.*;
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

        ArrayList<BibliographicReference> references = new ArrayList<>();
        HashMap<Integer, BibliographicReference> idToReference = new HashMap<>();
        HashMap<BibliographicReference, Collection<Integer>> referenceToRelatedID = new HashMap<>();

        try {
            connection = DatabaseController.getConnection();

            tagsStatement = connection.prepareStatement(tagsCommand);
            // relatedReferencesStatement = connection.prepareStatement(relatedReferencesQuery);

            referenceStatement = connection.createStatement();
            referenceResultSet = referenceStatement.executeQuery(referenceQuery);

            references.addAll(getArticles(connection));
            references.addAll(getBooks(connection));
            // TODO: addAll(getThesis), ecc.

            for (BibliographicReference reference : references) {
                // RECUPERO TAG
                tagsStatement.setInt(1, reference.getID());
                tagsResultSet = tagsStatement.executeQuery();

                ArrayList<Tag> tags = new ArrayList<>();

                while (tagsResultSet.next())
                    tags.add(new Tag(tagsResultSet.getString("name")));

                tags.trimToSize();
                reference.setTags(tags);

                idToReference.put(reference.getID(), reference);
                referenceToRelatedID.put(reference, getRelatedReferencesID(connection, reference));
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

    @Override
    public void save(Article article) throws ReferenceDatabaseException {
        if (article == null)
            return;

        String pageCount = article.getPageCount() == 0 ? "null" : String.valueOf(article.getPageCount());
        String url = getFormattedStringForQuery(article.getURL());
        String publisher = getFormattedStringForQuery(article.getPublisher());
        String issn = getFormattedStringForQuery(article.getISSN());

        String command = null;

        if (article.getID() == null)
            command = "insert into article(id, page_count, url, publisher, issn) values(?," + pageCount + "," + url + "," + publisher + "," + issn + ")";
        else
            command = "update article set page_count = " + pageCount + ", url = " + url + ", publisher = " + publisher + ", issn = " + issn + " where id = ?";

        save(article, command);
    }

    @Override
    public void save(Book book) throws ReferenceDatabaseException {
        if (book == null)
            return;

        String pageCount = getFormattedStringForQuery(book.getPageCount() == 0 ? null : String.valueOf(book.getPageCount()));
        String url = getFormattedStringForQuery(book.getURL());
        String publisher = getFormattedStringForQuery(book.getPublisher());
        String isbn = getFormattedStringForQuery(book.getISBN());

        String command = null;

        if (book.getID() == null)
            command = "insert into book(id, page_count, url, publisher, isbn) values(?," + pageCount + "," + url + "," + publisher + "," + isbn + ")";
        else
            command = "update book set page_count = " + pageCount + ", url = " + url + ", publisher = " + publisher + ", isbn = " + isbn + " where id = ?";

        save(book, command);
    }

    @Override
    public void save(Thesis thesis) throws ReferenceDatabaseException {
        if (thesis == null)
            return;

        String pageCount = getFormattedStringForQuery(thesis.getPageCount() == 0 ? null : String.valueOf(thesis.getPageCount()));
        String url = getFormattedStringForQuery(thesis.getURL());
        String publisher = getFormattedStringForQuery(thesis.getPublisher());
        String university = getFormattedStringForQuery(thesis.getUniversity());
        String faculty = getFormattedStringForQuery(thesis.getFaculty());

        String command = null;

        if (thesis.getID() == null)
            command = "insert into thesis(id, page_count, url, publisher, university, faculty) values(?," + pageCount + "," + url + "," + publisher + "," + university + "," + faculty + ")";
        else
            command = "update thesis set page_count = " + pageCount + ", url = " + url + ", publisher = " + publisher + ", university = " + university + ", faculty = " + faculty + " where id = ?";

        save(thesis, command);
    }

    @Override
    public void save(Image image) throws ReferenceDatabaseException {
        if (image == null)
            return;

        String url = getFormattedStringForQuery(image.getURL());
        String width = getFormattedStringForQuery(image.getWidth() == 0 ? null : String.valueOf(image.getWidth()));
        String height = getFormattedStringForQuery(image.getHeight() == 0 ? null : String.valueOf(image.getHeight()));

        String command = null;

        if (image.getID() == null)
            command = "insert into image(id, url, width, height) values(?," + url + "," + width + "," + height + ")";
        else
            command = "update thesis set url = " + url + ", width = " + width + ", height = " + height + " where id = ?";

        save(image, command);
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

        String url = getFormattedStringForQuery(website.getURL());

        String command = null;

        if (website.getID() == null)
            command = "insert into website(id, url) values(? ," + url + ")";
        else
            command = "update website set url = " + url + " where id = ?";

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

        String language = getFormattedStringForQuery(reference.getLanguage() == ReferenceLanguage.NOTSPECIFIED ? null : reference.getLanguage().name());

        if (reference.getID() == null)
            referenceInsertCommand = "insert into bibliographic_reference(owner, title, doi, description, language, pubblication_date) values(?,?,?,?," + language + ",?)";
        else
            referenceInsertCommand = "update bibliographic_reference set owner = ?, title = ?, doi = ?, description = ?, language = '" + language + "', pubblication_date = ? where id = " + reference.getID();

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
            referenceStatement.setString(4, reference.getDescription());

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

            // TODO: rimuovi
            authorsRemoveStatement.setInt(1, reference.getID());

            for (Author author : reference.getAuthors()) {
                authorsInsertStatement.setInt(1, reference.getID());
                authorsInsertStatement.setInt(2, author.getId());
                authorsInsertStatement.executeUpdate();
            }

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

    private List<Article> getArticles(Connection connection) throws SQLException {
        Statement referenceStatement = null;
        ResultSet referenceResultSet = null;
        String referenceQuery = "select * from bibliographic_reference natural join article where owner = '" + getUser().getName() + "'";

        try {
            referenceStatement = connection.createStatement();
            referenceResultSet = referenceStatement.executeQuery(referenceQuery);

            ArrayList<Article> articles = new ArrayList<>();

            while (referenceResultSet.next()) {
                Article article = new Article(referenceResultSet.getString("title"));
                article.setID(referenceResultSet.getInt("id"));
                article.setDOI(referenceResultSet.getString("doi"));
                article.setPubblicationDate(referenceResultSet.getDate("pubblication_date"));
                article.setDescription(referenceResultSet.getString("description"));
                article.setLanguage(ReferenceLanguage.getFromString(referenceResultSet.getString("language")));
                article.setISSN(referenceResultSet.getString("issn"));
                article.setPageCount(referenceResultSet.getInt("page_count"));
                article.setURL(referenceResultSet.getString("url"));
                article.setPublisher(referenceResultSet.getString("publisher"));

                articles.add(article);
            }

            articles.trimToSize();

            return articles;
        } catch (Exception e) {
            throw e;
        } finally {
            if (referenceResultSet != null)
                referenceResultSet.close();

            if (referenceStatement != null)
                referenceStatement.close();
        }

    }

    private List<Book> getBooks(Connection connection) throws SQLException {
        // TODO: implementa

        return new ArrayList<>(0);
    }

    // TODO: getThesis, getWebsite, ecc.

    private List<Integer> getRelatedReferencesID(Connection connection, BibliographicReference reference) throws SQLException {
        Statement relatedReferencesStatement = null;
        ResultSet relatedReferencesResultSet = null;
        String relatedReferencesQuery = "select * from related_references where quoted_by = " + reference.getID();

        try {
            relatedReferencesStatement = connection.createStatement();
            relatedReferencesResultSet = relatedReferencesStatement.executeQuery(relatedReferencesQuery);

            ArrayList<Integer> relatedReferencesID = new ArrayList<>();

            while (relatedReferencesResultSet.next())
                relatedReferencesID.add(relatedReferencesResultSet.getInt("quotes"));

            relatedReferencesID.trimToSize();

            return relatedReferencesID;
        } catch (Exception e) {
            throw e;
        } finally {
            if (relatedReferencesResultSet != null)
                relatedReferencesResultSet.close();

            if (relatedReferencesStatement != null)
                relatedReferencesStatement.close();
        }

    }

    private String getFormattedStringForQuery(String input) {
        return (input == null || input.isBlank()) ? null : "'" + input + "'";
    }

}
