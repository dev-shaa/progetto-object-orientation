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
    private User getUser() {
        return user;
    }

    @Override
    public List<BibliographicReference> getAll() throws ReferenceDatabaseException {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        ArrayList<BibliographicReference> references = new ArrayList<>();
        HashMap<Integer, BibliographicReference> idToReference = new HashMap<>();
        HashMap<BibliographicReference, Collection<Integer>> referenceToRelatedID = new HashMap<>();

        try {
            connection = DatabaseController.getConnection();

            statement = connection.createStatement();

            references.addAll(getArticles(statement, resultSet));
            references.addAll(getBooks(statement, resultSet));
            references.addAll(getThesis(statement, resultSet));
            references.addAll(getImages(statement, resultSet));
            references.addAll(getWebsites(statement, resultSet));
            references.addAll(getVideos(statement, resultSet));
            references.addAll(getSourceCodes(statement, resultSet));

            for (BibliographicReference reference : references) {
                idToReference.put(reference.getID(), reference);
                referenceToRelatedID.put(reference, getRelatedReferencesID(connection, reference));
            }

            // ASSEGNAZIONE RIMANDI
            for (BibliographicReference reference : references) {
                ArrayList<BibliographicReference> relatedReferences = new ArrayList<>();

                for (Integer referenceID : referenceToRelatedID.get(reference)) {
                    BibliographicReference relatedReference = idToReference.get(referenceID);
                    relatedReferences.add(relatedReference);
                    relatedReference.setQuotationCount(relatedReference.getQuotationCount() + 1);
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
                if (resultSet != null)
                    resultSet.close();

                if (statement != null)
                    statement.close();

                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
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

    /**
     * {@inheritDoc}
     * 
     * @throws IllegalArgumentException
     *             se {@code article == null}
     */
    @Override
    public void save(Article article) throws ReferenceDatabaseException {
        if (article == null)
            throw new IllegalArgumentException("article can't be null");

        String pageCount = article.getPageCount() == 0 ? null : String.valueOf(article.getPageCount());
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

    /**
     * {@inheritDoc}
     * 
     * @throws IllegalArgumentException
     *             se {@code book == null}
     */
    @Override
    public void save(Book book) throws ReferenceDatabaseException {
        if (book == null)
            throw new IllegalArgumentException("book can't be null");

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

    /**
     * {@inheritDoc}
     * 
     * @throws IllegalArgumentException
     *             se {@code thesis == null}
     */
    @Override
    public void save(Thesis thesis) throws ReferenceDatabaseException {
        if (thesis == null)
            throw new IllegalArgumentException("thesis can't be null");

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

    /**
     * {@inheritDoc}
     * 
     * @throws IllegalArgumentException
     *             se {@code image == null}
     */
    @Override
    public void save(Image image) throws ReferenceDatabaseException {
        if (image == null)
            throw new IllegalArgumentException("image can't be null");

        String url = getFormattedStringForQuery(image.getURL());
        String width = getFormattedStringForQuery(image.getWidth() == 0 ? null : String.valueOf(image.getWidth()));
        String height = getFormattedStringForQuery(image.getHeight() == 0 ? null : String.valueOf(image.getHeight()));

        String command = null;

        if (image.getID() == null)
            command = "insert into image(id, url, width, height) values(?," + url + "," + width + "," + height + ")";
        else
            command = "update image set url = " + url + ", width = " + width + ", height = " + height + " where id = ?";

        save(image, command);
    }

    /**
     * {@inheritDoc}
     * 
     * @throws IllegalArgumentException
     *             se {@code video == null}
     */
    @Override
    public void save(Video video) throws ReferenceDatabaseException {
        if (video == null)
            throw new IllegalArgumentException("video can't be null");

        String url = getFormattedStringForQuery(video.getURL());
        String width = video.getWidth() == 0 ? null : String.valueOf(video.getWidth());
        String height = video.getHeight() == 0 ? null : String.valueOf(video.getHeight());
        String framerate = video.getFrameRate() == 0 ? null : String.valueOf(video.getFrameRate());
        String duration = video.getDuration() == 0 ? null : String.valueOf(video.getDuration());

        String command = null;

        if (video.getID() == null)
            command = "insert into video(id, url, width, height, framerate, duration) values(?," + url + "," + width + "," + height + "," + framerate + "," + duration + ")";
        else
            command = "update video set url = " + url + ", width = " + width + ", height = " + height + ", framerate = " + framerate + ", duration = " + duration + " where id = ?";

        save(video, command);
    }

    /**
     * {@inheritDoc}
     * 
     * @throws IllegalArgumentException
     *             se {@code sourceCode == null}
     */
    @Override
    public void save(SourceCode sourceCode) throws ReferenceDatabaseException {
        if (sourceCode == null)
            throw new IllegalArgumentException("sourceCode can't be null");

        String url = getFormattedStringForQuery(sourceCode.getURL());
        String programmingLanguage = getFormattedStringForQuery(sourceCode.getProgrammingLanguage().name());

        String command = null;

        if (sourceCode.getID() == null)
            command = "insert into source_code(id, url, programming_language) values(?," + url + "," + programmingLanguage + ")";
        else
            command = "update source_code set url = " + url + ", programming_language = " + programmingLanguage + " where id = ?";

        save(sourceCode, command);
    }

    /**
     * {@inheritDoc}
     * 
     * @throws IllegalArgumentException
     *             se {@code website == null}
     */
    @Override
    public void save(Website website) throws ReferenceDatabaseException {
        if (website == null)
            throw new IllegalArgumentException("website can't be null");

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

            relatedReferenceInsertStatement.setInt(1, reference.getID());
            for (BibliographicReference relatedReference : reference.getRelatedReferences()) {
                relatedReferenceInsertStatement.setInt(2, relatedReference.getID());
                relatedReferenceInsertStatement.executeUpdate();
            }

            categoriesRemoveStatement.setInt(1, reference.getID());
            categoriesRemoveStatement.executeUpdate();

            categoriesInsertStatement.setInt(2, reference.getID());
            for (Category category : reference.getCategories()) {
                categoriesInsertStatement.setInt(1, category.getID());
                categoriesInsertStatement.executeUpdate();
            }

            tagRemoveStatement.setInt(1, reference.getID());
            tagRemoveStatement.executeUpdate();

            for (Tag tag : reference.getTags()) {
                tagInsertStatement.setString(1, tag.getName());
                tagInsertStatement.setInt(2, reference.getID());
                tagInsertStatement.executeUpdate();
            }

            authorsRemoveStatement.setInt(1, reference.getID());
            authorsRemoveStatement.executeUpdate();

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

    private List<Article> getArticles(Statement statement, ResultSet resultSet) throws SQLException {
        String referenceQuery = "select * from bibliographic_reference natural join article where owner = '" + getUser().getName() + "'";

        resultSet = statement.executeQuery(referenceQuery);

        ArrayList<Article> articles = new ArrayList<>();

        while (resultSet.next()) {
            Article article = new Article(resultSet.getString("title"));
            article.setID(resultSet.getInt("id"));
            article.setDOI(resultSet.getString("doi"));
            article.setPubblicationDate(resultSet.getDate("pubblication_date"));
            article.setDescription(resultSet.getString("description"));
            article.setLanguage(ReferenceLanguage.getFromString(resultSet.getString("language")));
            article.setISSN(resultSet.getString("issn"));
            article.setPageCount(resultSet.getInt("page_count"));
            article.setURL(resultSet.getString("url"));
            article.setPublisher(resultSet.getString("publisher"));

            articles.add(article);
        }

        articles.trimToSize();

        return articles;
    }

    private List<Book> getBooks(Statement statement, ResultSet resultSet) throws SQLException {
        String referenceQuery = "select * from bibliographic_reference natural join book where owner = '" + getUser().getName() + "'";

        resultSet = statement.executeQuery(referenceQuery);

        ArrayList<Book> books = new ArrayList<>();

        while (resultSet.next()) {
            Book book = new Book(resultSet.getString("title"));
            book.setID(resultSet.getInt("id"));
            book.setDOI(resultSet.getString("doi"));
            book.setPubblicationDate(resultSet.getDate("pubblication_date"));
            book.setDescription(resultSet.getString("description"));
            book.setLanguage(ReferenceLanguage.getFromString(resultSet.getString("language")));
            book.setISBN(resultSet.getString("isbn"));
            book.setPageCount(resultSet.getInt("page_count"));
            book.setURL(resultSet.getString("url"));
            book.setPublisher(resultSet.getString("publisher"));

            books.add(book);
        }

        books.trimToSize();

        return books;
    }

    private List<Thesis> getThesis(Statement statement, ResultSet resultSet) throws SQLException {
        String referenceQuery = "select * from bibliographic_reference natural join thesis where owner = '" + getUser().getName() + "'";

        resultSet = statement.executeQuery(referenceQuery);

        ArrayList<Thesis> theses = new ArrayList<>();

        while (resultSet.next()) {
            Thesis thesis = new Thesis(resultSet.getString("title"));
            thesis.setID(resultSet.getInt("id"));
            thesis.setDOI(resultSet.getString("doi"));
            thesis.setPubblicationDate(resultSet.getDate("pubblication_date"));
            thesis.setDescription(resultSet.getString("description"));
            thesis.setLanguage(ReferenceLanguage.getFromString(resultSet.getString("language")));
            thesis.setUniversity(resultSet.getString("university"));
            thesis.setFaculty(resultSet.getString("faculty"));
            thesis.setPageCount(resultSet.getInt("page_count"));
            thesis.setURL(resultSet.getString("url"));
            thesis.setPublisher(resultSet.getString("publisher"));

            theses.add(thesis);
        }

        theses.trimToSize();

        return theses;
    }

    private List<Website> getWebsites(Statement statement, ResultSet resultSet) throws SQLException {
        String referenceQuery = "select * from bibliographic_reference natural join website where owner = '" + getUser().getName() + "'";

        resultSet = statement.executeQuery(referenceQuery);

        ArrayList<Website> websites = new ArrayList<>();

        while (resultSet.next()) {
            Website website = new Website(resultSet.getString("title"), resultSet.getString("url"));
            website.setID(resultSet.getInt("id"));
            website.setDOI(resultSet.getString("doi"));
            website.setPubblicationDate(resultSet.getDate("pubblication_date"));
            website.setDescription(resultSet.getString("description"));
            website.setLanguage(ReferenceLanguage.getFromString(resultSet.getString("language")));

            websites.add(website);
        }

        websites.trimToSize();

        return websites;
    }

    private List<Image> getImages(Statement statement, ResultSet resultSet) throws SQLException {
        String referenceQuery = "select * from bibliographic_reference natural join image where owner = '" + getUser().getName() + "'";

        resultSet = statement.executeQuery(referenceQuery);

        ArrayList<Image> images = new ArrayList<>();

        while (resultSet.next()) {
            Image image = new Image(resultSet.getString("title"), resultSet.getString("url"));
            image.setID(resultSet.getInt("id"));
            image.setDOI(resultSet.getString("doi"));
            image.setPubblicationDate(resultSet.getDate("pubblication_date"));
            image.setDescription(resultSet.getString("description"));
            image.setLanguage(ReferenceLanguage.getFromString(resultSet.getString("language")));
            image.setWidth(resultSet.getInt("width"));
            image.setHeight(resultSet.getInt("height"));

            images.add(image);
        }

        images.trimToSize();

        return images;
    }

    private List<Video> getVideos(Statement statement, ResultSet resultSet) throws SQLException {
        String referenceQuery = "select * from bibliographic_reference natural join video where owner = '" + getUser().getName() + "'";

        resultSet = statement.executeQuery(referenceQuery);

        ArrayList<Video> videos = new ArrayList<>();

        while (resultSet.next()) {
            Video video = new Video(resultSet.getString("title"), resultSet.getString("url"));
            video.setID(resultSet.getInt("id"));
            video.setDOI(resultSet.getString("doi"));
            video.setPubblicationDate(resultSet.getDate("pubblication_date"));
            video.setDescription(resultSet.getString("description"));
            video.setLanguage(ReferenceLanguage.getFromString(resultSet.getString("language")));
            video.setWidth(resultSet.getInt("width"));
            video.setHeight(resultSet.getInt("height"));
            video.setFrameRate(resultSet.getInt("framerate"));
            video.setDuration(resultSet.getInt("duration"));

            videos.add(video);
        }

        videos.trimToSize();

        return videos;
    }

    private List<SourceCode> getSourceCodes(Statement statement, ResultSet resultSet) throws SQLException {
        String referenceQuery = "select * from bibliographic_reference natural join source_code where owner = '" + getUser().getName() + "'";

        resultSet = statement.executeQuery(referenceQuery);

        ArrayList<SourceCode> sourceCodes = new ArrayList<>();

        while (resultSet.next()) {
            SourceCode sourceCode = new SourceCode(resultSet.getString("title"), resultSet.getString("url"));
            sourceCode.setID(resultSet.getInt("id"));
            sourceCode.setDOI(resultSet.getString("doi"));
            sourceCode.setPubblicationDate(resultSet.getDate("pubblication_date"));
            sourceCode.setDescription(resultSet.getString("description"));
            sourceCode.setLanguage(ReferenceLanguage.getFromString(resultSet.getString("language")));
            sourceCode.setProgrammingLanguage(ProgrammingLanguage.getFromString(resultSet.getString("programming_language")));

            sourceCodes.add(sourceCode);
        }

        sourceCodes.trimToSize();

        return sourceCodes;
    }

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
