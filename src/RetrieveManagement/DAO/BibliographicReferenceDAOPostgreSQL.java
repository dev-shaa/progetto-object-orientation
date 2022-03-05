package RetrieveManagement.DAO;

import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.function.Function;

import Entities.*;
import Entities.References.*;
import Entities.References.OnlineResources.*;
import Entities.References.PhysicalResources.*;
import RetrieveManagement.Connections.ConnectionController;
import RetrieveManagement.Connections.CustomConnection;
import Utilities.Functions.CheckedFunction;

/**
 * Implementazione dell'interfaccia BibliographicReferenceDAO per database relazionali PostgreSQL.
 * 
 * @see BibliographicReferenceDAO
 */
public class BibliographicReferenceDAOPostgreSQL implements BibliographicReferenceDAO {

    private User user;

    /**
     * Crea una nuova classe DAO per interfacciarsi al database PostgreSQL relativo ai riferimenti dell'utente.
     * 
     * @param user
     *            l'utente che accede al database
     * @throws IllegalArgumentException
     *             se {@code user == null}
     */
    public BibliographicReferenceDAOPostgreSQL(User user) {
        if (user == null)
            throw new IllegalArgumentException("user non può essere null");

        this.user = user;
    }

    @Override
    public ArrayList<BibliographicReference> getAll() throws SQLException {
        CustomConnection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        ArrayList<BibliographicReference> allReferences = new ArrayList<>();
        HashMap<Integer, BibliographicReference> idToReference = new HashMap<>();
        HashMap<BibliographicReference, Collection<Integer>> referenceToRelatedReferencesIDs = new HashMap<>();

        try {
            connection = ConnectionController.getInstance().getConnection();
            statement = connection.createStatement();

            allReferences.addAll(getArticles(statement, resultSet));
            allReferences.addAll(getBooks(statement, resultSet));
            allReferences.addAll(getThesis(statement, resultSet));
            allReferences.addAll(getImages(statement, resultSet));
            allReferences.addAll(getWebsites(statement, resultSet));
            allReferences.addAll(getVideos(statement, resultSet));
            allReferences.addAll(getSourceCodes(statement, resultSet));

            for (BibliographicReference reference : allReferences) {
                idToReference.put(reference.getID(), reference);

                List<Integer> relatedReferencesIDs = getRelatedReferencesIDs(statement, resultSet, reference);
                referenceToRelatedReferencesIDs.put(reference, relatedReferencesIDs);
            }

            // ASSEGNAZIONE RIMANDI
            for (BibliographicReference reference : allReferences) {
                ArrayList<BibliographicReference> relatedReferences = new ArrayList<>();

                for (Integer referenceID : referenceToRelatedReferencesIDs.get(reference)) {
                    BibliographicReference relatedReference = idToReference.get(referenceID);
                    relatedReferences.add(relatedReference);
                    relatedReference.setQuotationCount(relatedReference.getQuotationCount() + 1);
                }

                relatedReferences.trimToSize();
                reference.setRelatedReferences(relatedReferences);
            }

            allReferences.trimToSize();
            return allReferences;
        } finally {
            if (resultSet != null)
                resultSet.close();

            if (statement != null)
                statement.close();

            if (connection != null)
                connection.close();
        }

    }

    /**
     * {@inheritDoc}
     * 
     * @throws IllegalArgumentException
     *             se {@code reference == null} o se {@code reference} non ha un ID
     */
    @Override
    public void remove(BibliographicReference reference) throws SQLException {
        if (reference == null)
            throw new IllegalArgumentException("reference can't be null");

        if (reference.getID() == null)
            throw new IllegalArgumentException("reference doesn't have an ID");

        CustomConnection connection = null;
        Statement statement = null;
        String command = "delete from bibliographic_reference where id = " + reference.getID();

        try {
            connection = ConnectionController.getInstance().getConnection();
            statement = connection.createStatement();
            statement.executeUpdate(command);
        } finally {
            if (statement != null)
                statement.close();

            if (connection != null)
                connection.close();
        }
    }

    // #region SAVE

    @Override
    public void save(Article article) throws SQLException {
        if (article == null)
            return;

        String pageCount = article.getPageCount() == 0 ? null : String.valueOf(article.getPageCount());
        String url = getFormattedStringForQuery(article.getURL());
        String publisher = getFormattedStringForQuery(article.getPublisher());
        String issn = getFormattedStringForQuery(article.getISSN());

        Function<Integer, String> insertCommandGetter = (id) -> {
            return "insert into article(id, page_count, url, publisher, issn) values(" + id + "," + pageCount + "," + url + "," + publisher + "," + issn + ")";
        };

        Function<Integer, String> updateCommandGetter = (id) -> {
            return "update article set page_count = " + pageCount + ", url = " + url + ", publisher = " + publisher + ", issn = " + issn + " where id = " + id;
        };

        save(article, insertCommandGetter, updateCommandGetter);
    }

    @Override
    public void save(Book book) throws SQLException {
        if (book == null)
            return;

        String pageCount = getFormattedStringForQuery(book.getPageCount() == 0 ? null : String.valueOf(book.getPageCount()));
        String url = getFormattedStringForQuery(book.getURL());
        String publisher = getFormattedStringForQuery(book.getPublisher());
        String isbn = getFormattedStringForQuery(book.getISBN());

        Function<Integer, String> insertCommandGetter = (id) -> {
            return "insert into book(id, page_count, url, publisher, isbn) values(" + id + "," + pageCount + "," + url + "," + publisher + "," + isbn + ")";
        };

        Function<Integer, String> updateCommandGetter = (id) -> {
            return "update book set page_count = " + pageCount + ", url = " + url + ", publisher = " + publisher + ", isbn = " + isbn + " where id = " + id;
        };

        save(book, insertCommandGetter, updateCommandGetter);
    }

    @Override
    public void save(Thesis thesis) throws SQLException {
        if (thesis == null)
            return;

        String pageCount = getFormattedStringForQuery(thesis.getPageCount() == 0 ? null : String.valueOf(thesis.getPageCount()));
        String url = getFormattedStringForQuery(thesis.getURL());
        String publisher = getFormattedStringForQuery(thesis.getPublisher());
        String university = getFormattedStringForQuery(thesis.getUniversity());
        String faculty = getFormattedStringForQuery(thesis.getFaculty());

        Function<Integer, String> insertCommandGetter = (id) -> {
            return "insert into thesis(id, page_count, url, publisher, university, faculty) values(" + id + "," + pageCount + "," + url + "," + publisher + "," + university + "," + faculty + ")";
        };

        Function<Integer, String> updateCommandGetter = (id) -> {
            return "update thesis set page_count = " + pageCount + ", url = " + url + ", publisher = " + publisher + ", university = " + university + ", faculty = " + faculty + " where id = " + id;
        };

        save(thesis, insertCommandGetter, updateCommandGetter);
    }

    @Override
    public void save(Image image) throws SQLException {
        if (image == null)
            return;

        String url = getFormattedStringForQuery(image.getURL());
        String width = getFormattedStringForQuery(image.getWidth() == 0 ? null : String.valueOf(image.getWidth()));
        String height = getFormattedStringForQuery(image.getHeight() == 0 ? null : String.valueOf(image.getHeight()));

        Function<Integer, String> insertCommandGetter = (id) -> {
            return "insert into image(id, url, width, height) values(" + id + "," + url + "," + width + "," + height + ")";
        };

        Function<Integer, String> updateCommandGetter = (id) -> {
            return "update image set url = " + url + ", width = " + width + ", height = " + height + " where id = " + id;
        };

        save(image, insertCommandGetter, updateCommandGetter);
    }

    @Override
    public void save(Video video) throws SQLException {
        if (video == null)
            return;

        String url = getFormattedStringForQuery(video.getURL());
        String width = video.getWidth() == 0 ? null : String.valueOf(video.getWidth());
        String height = video.getHeight() == 0 ? null : String.valueOf(video.getHeight());
        String framerate = video.getFrameRate() == 0 ? null : String.valueOf(video.getFrameRate());
        String duration = video.getDuration() == 0 ? null : String.valueOf(video.getDuration());

        Function<Integer, String> insertCommandGetter = (id) -> {
            return "insert into video(id, url, width, height, framerate, duration) values(" + id + "," + url + "," + width + "," + height + "," + framerate + "," + duration + ")";
        };

        Function<Integer, String> updateCommandGetter = (id) -> {
            return "update video set url = " + url + ", width = " + width + ", height = " + height + ", framerate = " + framerate + ", duration = " + duration + " where id = " + id;
        };

        save(video, insertCommandGetter, updateCommandGetter);
    }

    @Override
    public void save(SourceCode sourceCode) throws SQLException {
        if (sourceCode == null)
            return;

        String programmingLanguage = getFormattedProgrammingLanguageForQuery(sourceCode.getProgrammingLanguage());
        String url = getFormattedStringForQuery(sourceCode.getURL());

        Function<Integer, String> insertCommandGetter = (id) -> {
            return "insert into source_code(id, url, programming_language) values(" + id + "," + url + "," + programmingLanguage + ")";
        };

        Function<Integer, String> updateCommandGetter = (id) -> {
            return "update source_code set url = " + url + ", programming_language = " + programmingLanguage + " where id = " + id;
        };

        save(sourceCode, insertCommandGetter, updateCommandGetter);
    }

    @Override
    public void save(Website website) throws SQLException {
        if (website == null)
            return;

        String url = getFormattedStringForQuery(website.getURL());

        Function<Integer, String> insertCommandGetter = (id) -> {
            return "insert into website(id, url) values(" + id + "," + url + ")";
        };

        Function<Integer, String> updateCommandGetter = (id) -> {
            return "update website set url = " + url + " where id = " + id;
        };

        save(website, insertCommandGetter, updateCommandGetter);
    }

    private void save(BibliographicReference reference, Function<Integer, String> insertCommandGetterForSubclass, Function<Integer, String> updateCommandGetterForSubclass) throws SQLException {
        if (reference == null)
            return;

        CustomConnection connection = null;
        PreparedStatement referenceStatement = null;
        Statement referenceSubclassStatement = null;
        ResultSet resultSet = null;

        // nota: non c'è una funzione per impostare un enum in una preparedStatement e setString() non va bene
        // quindi dobbiamo inserirlo nella query dall'inizio
        String language = getFormattedLanguageForQuery(reference.getLanguage());
        String referenceInsertCommand = null;

        if (reference.getID() == null)
            referenceInsertCommand = "insert into bibliographic_reference(owner, title, doi, description, language, pubblication_date) values(?,?,?,?," + language + ",?)";
        else
            referenceInsertCommand = "update bibliographic_reference set owner = ?, title = ?, doi = ?, description = ?, language = " + language + ", pubblication_date = ? where id = " + reference.getID();

        try {
            connection = ConnectionController.getInstance().getConnection();

            // vogliamo che sia una transazione per evitare che il programma non sia coordinato con il database
            connection.setAutoCommit(false);

            referenceStatement = connection.prepareStatement(referenceInsertCommand, Statement.RETURN_GENERATED_KEYS);

            referenceStatement.setString(1, user.getName());
            referenceStatement.setString(2, reference.getTitle());
            referenceStatement.setString(3, reference.getDOI());
            referenceStatement.setString(4, reference.getDescription());
            referenceStatement.setDate(5, convertToSQLDate(reference.getPubblicationDate()));

            referenceStatement.executeUpdate();

            // se l'id è nullo, abbiamo inserito un nuovo riferimento, quindi dobbiamo recuperare la chiave appena generata
            // però non lo impostiamo subito, perchè se le istruzioni successive falliscono viene eseguito il rollback
            // quindi avremo un id che nel database non corrisponde a niente
            Integer referenceID = reference.getID();

            referenceSubclassStatement = connection.createStatement();

            if (referenceID == null) {
                resultSet = referenceStatement.getGeneratedKeys();

                if (resultSet.next()) {
                    referenceID = resultSet.getInt(1);
                    referenceSubclassStatement.executeUpdate(insertCommandGetterForSubclass.apply(referenceID));
                }
            } else {
                referenceSubclassStatement.executeUpdate(updateCommandGetterForSubclass.apply(referenceID));
            }

            insertRelatedReferences(connection, referenceID, reference.getRelatedReferences());
            insertCategories(connection, referenceID, reference.getCategories());
            insertAuthors(connection, referenceID, reference.getAuthors());

            connection.commit();

            // l'id lo mettiamo alla fine, in questo modo siamo sicuri che sia andato tutto bene prima
            // e quindi abbiamo un id valido
            reference.setID(referenceID);
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            if (resultSet != null)
                resultSet.close();

            if (referenceSubclassStatement != null)
                referenceSubclassStatement.close();

            if (referenceStatement != null)
                referenceStatement.close();

            if (connection != null)
                connection.close();
        }
    }

    private void insertRelatedReferences(CustomConnection connection, int id, Collection<? extends BibliographicReference> relatedReferences) throws SQLException {
        PreparedStatement removeStatement = null;
        PreparedStatement insertStatement = null;

        String relatedReferenceRemoveCommand = "delete from quotations where quoted_by = ?";
        String relatedReferenceInsertCommand = "insert into quotations(quoted_by, quotes) values(?, ?)";

        try {
            removeStatement = connection.prepareStatement(relatedReferenceRemoveCommand);
            removeStatement.setInt(1, id);
            removeStatement.executeUpdate();

            insertStatement = connection.prepareStatement(relatedReferenceInsertCommand);
            insertStatement.setInt(1, id);
            for (BibliographicReference relatedReference : relatedReferences) {
                insertStatement.setInt(2, relatedReference.getID());
                insertStatement.executeUpdate();
            }
        } finally {
            if (insertStatement != null)
                insertStatement.close();

            if (removeStatement != null)
                removeStatement.close();
        }
    }

    private void insertCategories(CustomConnection connection, int id, Collection<Category> categories) throws SQLException {
        PreparedStatement removeStatement = null;
        PreparedStatement insertStatement = null;
        String removeCommand = "delete from reference_grouping where reference = ?";
        String insertCommand = "insert into reference_grouping(category, reference) values(?, ?)";

        try {
            removeStatement = connection.prepareStatement(removeCommand);
            removeStatement.setInt(1, id);
            removeStatement.executeUpdate();

            insertStatement = connection.prepareStatement(insertCommand);
            insertStatement.setInt(2, id);

            for (Category category : categories) {
                insertStatement.setInt(1, category.getID());
                insertStatement.executeUpdate();
            }
        } finally {
            if (insertStatement != null)
                insertStatement.close();

            if (removeStatement != null)
                removeStatement.close();
        }
    }

    private void insertAuthors(CustomConnection connection, int id, Collection<Author> authors) throws SQLException {
        PreparedStatement removeStatement = null;
        PreparedStatement insertStatement = null;
        String removeCommand = "delete from authorship where reference = ?";
        String insertCommand = "insert into authorship(reference, author) values(?, ?)";

        try {
            removeStatement = connection.prepareStatement(removeCommand);
            removeStatement.setInt(1, id);
            removeStatement.executeUpdate();

            insertStatement = connection.prepareStatement(insertCommand);
            insertStatement.setInt(1, id);

            for (Author author : authors) {
                insertStatement.setInt(2, author.getID());
                insertStatement.executeUpdate();
            }
        } finally {
            if (insertStatement != null)
                insertStatement.close();

            if (removeStatement != null)
                insertStatement.close();
        }
    }

    // #endregion

    // #region GETTER

    private ArrayList<Article> getArticles(Statement statement, ResultSet resultSet) throws SQLException {
        CheckedFunction<ResultSet, Article, SQLException> getArticleFromResultSet = (set) -> {
            Article article = new Article(set.getString("title"));
            article.setID(set.getInt("id"));
            article.setDOI(set.getString("doi"));
            article.setPubblicationDate(set.getDate("pubblication_date"));
            article.setDescription(set.getString("description"));
            article.setLanguage(ReferenceLanguage.getFromString(set.getString("language")));
            article.setISSN(set.getString("issn"));
            article.setPageCount(set.getInt("page_count"));
            article.setURL(set.getString("url"));
            article.setPublisher(set.getString("publisher"));
            return article;
        };

        return getReferences(statement, resultSet, "article", getArticleFromResultSet);
    }

    private ArrayList<Book> getBooks(Statement statement, ResultSet resultSet) throws SQLException {
        CheckedFunction<ResultSet, Book, SQLException> getBookFromResultSet = (set) -> {
            Book book = new Book(set.getString("title"));
            book.setID(set.getInt("id"));
            book.setDOI(set.getString("doi"));
            book.setPubblicationDate(set.getDate("pubblication_date"));
            book.setDescription(set.getString("description"));
            book.setLanguage(ReferenceLanguage.getFromString(set.getString("language")));
            book.setISBN(set.getString("isbn"));
            book.setPageCount(set.getInt("page_count"));
            book.setURL(set.getString("url"));
            book.setPublisher(set.getString("publisher"));
            return book;
        };

        return getReferences(statement, resultSet, "book", getBookFromResultSet);
    }

    private ArrayList<Thesis> getThesis(Statement statement, ResultSet resultSet) throws SQLException {
        CheckedFunction<ResultSet, Thesis, SQLException> getThesisFromResultSet = (set) -> {
            Thesis thesis = new Thesis(set.getString("title"));
            thesis.setID(set.getInt("id"));
            thesis.setDOI(set.getString("doi"));
            thesis.setPubblicationDate(set.getDate("pubblication_date"));
            thesis.setDescription(set.getString("description"));
            thesis.setLanguage(ReferenceLanguage.getFromString(set.getString("language")));
            thesis.setUniversity(set.getString("university"));
            thesis.setFaculty(set.getString("faculty"));
            thesis.setPageCount(set.getInt("page_count"));
            thesis.setURL(set.getString("url"));
            thesis.setPublisher(set.getString("publisher"));
            return thesis;
        };

        return getReferences(statement, resultSet, "thesis", getThesisFromResultSet);
    }

    private ArrayList<Website> getWebsites(Statement statement, ResultSet resultSet) throws SQLException {
        CheckedFunction<ResultSet, Website, SQLException> getImageFromResultSet = (set) -> {
            Website website = new Website(set.getString("title"), set.getString("url"));
            website.setID(set.getInt("id"));
            website.setDOI(set.getString("doi"));
            website.setPubblicationDate(set.getDate("pubblication_date"));
            website.setDescription(set.getString("description"));
            website.setLanguage(ReferenceLanguage.getFromString(set.getString("language")));
            return website;
        };

        return getReferences(statement, resultSet, "website", getImageFromResultSet);
    }

    private ArrayList<Image> getImages(Statement statement, ResultSet resultSet) throws SQLException {
        CheckedFunction<ResultSet, Image, SQLException> getImageFromResultSet = (set) -> {
            Image image = new Image(set.getString("title"), set.getString("url"));
            image.setID(set.getInt("id"));
            image.setDOI(set.getString("doi"));
            image.setPubblicationDate(set.getDate("pubblication_date"));
            image.setDescription(set.getString("description"));
            image.setLanguage(ReferenceLanguage.getFromString(set.getString("language")));
            image.setWidth(set.getInt("width"));
            image.setHeight(set.getInt("height"));
            return image;
        };

        return getReferences(statement, resultSet, "image", getImageFromResultSet);
    }

    private ArrayList<Video> getVideos(Statement statement, ResultSet resultSet) throws SQLException {
        CheckedFunction<ResultSet, Video, SQLException> foo = (set) -> {
            Video video = new Video(set.getString("title"), set.getString("url"));
            video.setID(set.getInt("id"));
            video.setDOI(set.getString("doi"));
            video.setPubblicationDate(set.getDate("pubblication_date"));
            video.setDescription(set.getString("description"));
            video.setLanguage(ReferenceLanguage.getFromString(set.getString("language")));
            video.setWidth(set.getInt("width"));
            video.setHeight(set.getInt("height"));
            video.setFrameRate(set.getInt("framerate"));
            video.setDuration(set.getInt("duration"));
            return video;
        };

        return getReferences(statement, resultSet, "video", foo);
    }

    private ArrayList<SourceCode> getSourceCodes(Statement statement, ResultSet resultSet) throws SQLException {
        CheckedFunction<ResultSet, SourceCode, SQLException> foo = (set) -> {
            SourceCode sourceCode = new SourceCode(set.getString("title"), set.getString("url"));
            sourceCode.setID(set.getInt("id"));
            sourceCode.setDOI(set.getString("doi"));
            sourceCode.setPubblicationDate(set.getDate("pubblication_date"));
            sourceCode.setDescription(set.getString("description"));
            sourceCode.setLanguage(ReferenceLanguage.getFromString(set.getString("language")));
            sourceCode.setProgrammingLanguage(ProgrammingLanguage.getFromString(set.getString("programming_language")));
            return sourceCode;
        };

        return getReferences(statement, resultSet, "source_code", foo);
    }

    private <T> ArrayList<T> getReferences(Statement statement, ResultSet resultSet, String tableName, CheckedFunction<ResultSet, T, SQLException> getterFromResultSet) throws SQLException {
        String referenceQuery = "select * from bibliographic_reference natural join " + tableName + " where owner = '" + user.getName() + "'";
        resultSet = statement.executeQuery(referenceQuery);

        ArrayList<T> references = new ArrayList<>();

        while (resultSet.next())
            references.add(getterFromResultSet.call(resultSet));

        return references;
    }

    private List<Integer> getRelatedReferencesIDs(Statement statement, ResultSet resultSet, BibliographicReference reference) throws SQLException {
        String relatedReferencesQuery = "select quotes from quotations where quoted_by = " + reference.getID();
        ArrayList<Integer> relatedReferencesIDs = new ArrayList<>();

        resultSet = statement.executeQuery(relatedReferencesQuery);

        while (resultSet.next())
            relatedReferencesIDs.add(resultSet.getInt("quotes"));

        relatedReferencesIDs.trimToSize();
        return relatedReferencesIDs;
    }

    // #endregion

    // #region UTILITIES

    private String getFormattedStringForQuery(String input) {
        return (input == null || input.isEmpty() || input.isBlank()) ? null : "'" + input + "'";
    }

    private java.sql.Date convertToSQLDate(Date date) {
        return date == null ? null : new java.sql.Date(date.getTime());
    }

    private String getFormattedLanguageForQuery(ReferenceLanguage language) {
        return getFormattedStringForQuery(language == ReferenceLanguage.NOTSPECIFIED ? null : language.name());
    }

    private String getFormattedProgrammingLanguageForQuery(ProgrammingLanguage programmingLanguage) {
        return getFormattedStringForQuery(programmingLanguage == ProgrammingLanguage.NOTSPECIFIED ? null : programmingLanguage.name());
    }

    // #endregion

}