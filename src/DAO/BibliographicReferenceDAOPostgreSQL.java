package DAO;

import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.function.Function;

import Controller.ConnectionController;
import Entities.*;
import Entities.References.*;
import Entities.References.OnlineResources.*;
import Entities.References.PhysicalResources.*;
import Exceptions.Database.ReferenceDatabaseException;

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
    public List<BibliographicReference> getAll() throws ReferenceDatabaseException {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        ArrayList<BibliographicReference> allReferences = new ArrayList<>();
        HashMap<Integer, BibliographicReference> idToReference = new HashMap<>();
        HashMap<BibliographicReference, Collection<Integer>> referenceToRelatedReferencesIDs = new HashMap<>();

        try {
            connection = ConnectionController.getConnection();
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
        } catch (Exception e) {
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
     *             se {@code reference == null} o se {@code reference} non ha un ID
     */
    @Override
    public void remove(BibliographicReference reference) throws ReferenceDatabaseException {
        if (reference == null)
            throw new IllegalArgumentException("reference can't be null");

        if (reference.getID() == null)
            throw new IllegalArgumentException("reference doesn't have an ID");

        Connection connection = null;
        Statement statement = null;
        String command = "delete from bibliographic_reference where id = " + reference.getID();

        try {
            connection = ConnectionController.getConnection();
            statement = connection.createStatement();
            statement.executeUpdate(command);
        } catch (Exception e) {
            throw new ReferenceDatabaseException("Impossibile rimuovere il riferimento.");
        } finally {
            try {
                if (statement != null)
                    statement.close();

                if (connection != null)
                    connection.close();
            } catch (Exception e) {
                // non fare niente
            }
        }
    }

    @Override
    public void save(Article article) throws ReferenceDatabaseException {
        if (article == null)
            return;

        String pageCount = article.getPageCount() == 0 ? null : String.valueOf(article.getPageCount());
        String url = getFormattedStringForQuery(article.getURL());
        String publisher = getFormattedStringForQuery(article.getPublisher());
        String issn = getFormattedStringForQuery(article.getISSN());

        Function<Integer, String> insertCommandGetter = new Function<Integer, String>() {
            @Override
            public String apply(Integer id) {
                return "insert into article(id, page_count, url, publisher, issn) values(" + id + "," + pageCount + "," + url + "," + publisher + "," + issn + ")";
            }
        };

        Function<Integer, String> updateCommandGetter = new Function<Integer, String>() {
            @Override
            public String apply(Integer id) {
                return "update article set page_count = " + pageCount + ", url = " + url + ", publisher = " + publisher + ", issn = " + issn + " where id = " + id;
            }
        };

        save(article, insertCommandGetter, updateCommandGetter);
    }

    @Override
    public void save(Book book) throws ReferenceDatabaseException {
        if (book == null)
            return;

        String pageCount = getFormattedStringForQuery(book.getPageCount() == 0 ? null : String.valueOf(book.getPageCount()));
        String url = getFormattedStringForQuery(book.getURL());
        String publisher = getFormattedStringForQuery(book.getPublisher());
        String isbn = getFormattedStringForQuery(book.getISBN());

        Function<Integer, String> insertCommandGetter = new Function<Integer, String>() {
            @Override
            public String apply(Integer id) {
                return "insert into book(id, page_count, url, publisher, isbn) values(" + id + "," + pageCount + "," + url + "," + publisher + "," + isbn + ")";
            }
        };

        Function<Integer, String> updateCommandGetter = new Function<Integer, String>() {
            @Override
            public String apply(Integer id) {
                return "update book set page_count = " + pageCount + ", url = " + url + ", publisher = " + publisher + ", isbn = " + isbn + " where id = " + id;
            }
        };

        save(book, insertCommandGetter, updateCommandGetter);
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

        Function<Integer, String> insertCommandGetter = new Function<Integer, String>() {
            @Override
            public String apply(Integer id) {
                return "insert into thesis(id, page_count, url, publisher, university, faculty) values(" + id + "," + pageCount + "," + url + "," + publisher + "," + university + "," + faculty + ")";
            }
        };

        Function<Integer, String> updateCommandGetter = new Function<Integer, String>() {
            @Override
            public String apply(Integer id) {
                return "update thesis set page_count = " + pageCount + ", url = " + url + ", publisher = " + publisher + ", university = " + university + ", faculty = " + faculty + " where id = " + id;
            }
        };

        save(thesis, insertCommandGetter, updateCommandGetter);
    }

    @Override
    public void save(Image image) throws ReferenceDatabaseException {
        if (image == null)
            return;

        String url = getFormattedStringForQuery(image.getURL());
        String width = getFormattedStringForQuery(image.getWidth() == 0 ? null : String.valueOf(image.getWidth()));
        String height = getFormattedStringForQuery(image.getHeight() == 0 ? null : String.valueOf(image.getHeight()));

        Function<Integer, String> insertCommandGetter = new Function<Integer, String>() {
            @Override
            public String apply(Integer id) {
                return "insert into image(id, url, width, height) values(" + id + "," + url + "," + width + "," + height + ")";
            }
        };

        Function<Integer, String> updateCommandGetter = new Function<Integer, String>() {
            @Override
            public String apply(Integer id) {
                return "update image set url = " + url + ", width = " + width + ", height = " + height + " where id = " + id;
            }
        };

        save(image, insertCommandGetter, updateCommandGetter);
    }

    @Override
    public void save(Video video) throws ReferenceDatabaseException {
        if (video == null)
            return;

        String url = getFormattedStringForQuery(video.getURL());
        String width = video.getWidth() == 0 ? null : String.valueOf(video.getWidth());
        String height = video.getHeight() == 0 ? null : String.valueOf(video.getHeight());
        String framerate = video.getFrameRate() == 0 ? null : String.valueOf(video.getFrameRate());
        String duration = video.getDuration() == 0 ? null : String.valueOf(video.getDuration());

        Function<Integer, String> insertCommandGetter = new Function<Integer, String>() {
            @Override
            public String apply(Integer id) {
                return "insert into video(id, url, width, height, framerate, duration) values(" + id + "," + url + "," + width + "," + height + "," + framerate + "," + duration + ")";
            }
        };

        Function<Integer, String> updateCommandGetter = new Function<Integer, String>() {
            @Override
            public String apply(Integer id) {
                return "update video set url = " + url + ", width = " + width + ", height = " + height + ", framerate = " + framerate + ", duration = " + duration + " where id = " + id;
            }
        };

        save(video, insertCommandGetter, updateCommandGetter);
    }

    @Override
    public void save(SourceCode sourceCode) throws ReferenceDatabaseException {
        if (sourceCode == null)
            return;

        String programmingLanguage = getFormattedProgrammingLanguageForQuery(sourceCode.getProgrammingLanguage());
        String url = getFormattedStringForQuery(sourceCode.getURL());

        Function<Integer, String> insertCommandGetter = new Function<Integer, String>() {
            @Override
            public String apply(Integer id) {
                return "insert into source_code(id, url, programming_language) values(" + id + "," + url + "," + programmingLanguage + ")";
            }
        };

        Function<Integer, String> updateCommandGetter = new Function<Integer, String>() {
            @Override
            public String apply(Integer id) {
                return "update source_code set url = " + url + ", programming_language = " + programmingLanguage + " where id = " + id;
            }
        };

        save(sourceCode, insertCommandGetter, updateCommandGetter);
    }

    @Override
    public void save(Website website) throws ReferenceDatabaseException {
        if (website == null)
            return;

        String url = getFormattedStringForQuery(website.getURL());

        Function<Integer, String> insertCommandGetter = new Function<Integer, String>() {
            @Override
            public String apply(Integer id) {
                return "insert into website(id, url) values(" + id + "," + url + ")";
            }
        };

        Function<Integer, String> updateCommandGetter = new Function<Integer, String>() {
            @Override
            public String apply(Integer id) {
                return "update website set url = " + url + " where id = " + id;
            }
        };

        save(website, insertCommandGetter, updateCommandGetter);
    }

    /**
     * Salva un riferimento nel database.
     * 
     * @param reference
     *            riferimento da salvare
     * @param insertCommandGetterForSubclass
     *            funzione per ottenere il comando di inserimento di una sottoclasse, dato un certo ID
     * @param updateCommandGetterForSubclass
     *            funzione per ottenere il comando di modifica di una sottoclasse, dato un certo ID
     * @throws ReferenceDatabaseException
     *             se si verifica un errore
     */
    private void save(BibliographicReference reference, Function<Integer, String> insertCommandGetterForSubclass, Function<Integer, String> updateCommandGetterForSubclass) throws ReferenceDatabaseException {
        if (reference == null)
            return;

        Connection connection = null;
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
            connection = ConnectionController.getConnection();

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
        } catch (Exception e) {
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

                if (referenceSubclassStatement != null)
                    referenceSubclassStatement.close();

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
     * Inserisce nel database le associazioni tra riferimento e rimandi.
     * 
     * @param connection
     *            connessione da usare
     * @param id
     *            id del riferimento che si sta inserendo
     * @param relatedReferences
     *            rimandi da associare
     * @throws SQLException
     *             se si verifica un errore
     */
    private void insertRelatedReferences(Connection connection, int id, Collection<? extends BibliographicReference> relatedReferences) throws SQLException {
        PreparedStatement removeStatement = null;
        PreparedStatement insertStatement = null;

        String relatedReferenceRemoveCommand = "delete from related_references where quoted_by = ?";
        String relatedReferenceInsertCommand = "insert into related_references values(?, ?)";

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

    /**
     * Inserisce nel database le associazioni tra riferimento e categorie.
     * 
     * @param connection
     *            connessione da usare
     * @param id
     *            id del riferimento che si sta inserendo
     * @param categories
     *            categorie da associare
     * @throws SQLException
     *             se si verifica un errore
     */
    private void insertCategories(Connection connection, int id, Collection<Category> categories) throws SQLException {
        PreparedStatement removeStatement = null;
        PreparedStatement insertStatement = null;
        String removeCommand = "delete from category_reference_association where reference = ?";
        String insertCommand = "insert into category_reference_association values(?, ?)";

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

    /**
     * Inserisce nel database le associazioni tra riferimento e autori.
     * 
     * @param connection
     *            connessione da usare
     * @param id
     *            id del riferimento che si sta inserendo
     * @param authors
     *            autori da associare
     * @throws SQLException
     *             se si verifica un errore
     */
    private void insertAuthors(Connection connection, int id, Collection<Author> authors) throws SQLException {
        PreparedStatement removeStatement = null;
        PreparedStatement insertStatement = null;
        String removeCommand = "delete from author_reference_association where reference = ?";
        String insertCommand = "insert into author_reference_association values(?, ?)";

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

    /**
     * Restituisce tutti gli articoli dell'utente dal database.
     * 
     * @param statement
     *            statement da usare per il recupero
     * @param resultSet
     *            resultset da usare per il recupero
     * @return lista con gli articoli dell'utente
     * @throws SQLException
     *             se si verifica un errore
     */
    private List<Article> getArticles(Statement statement, ResultSet resultSet) throws SQLException {
        String referenceQuery = "select * from bibliographic_reference natural join article where owner = '" + user.getName() + "'";

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
        String referenceQuery = "select * from bibliographic_reference natural join book where owner = '" + user.getName() + "'";

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
        String referenceQuery = "select * from bibliographic_reference natural join thesis where owner = '" + user.getName() + "'";

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
        String referenceQuery = "select * from bibliographic_reference natural join website where owner = '" + user.getName() + "'";

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
        String referenceQuery = "select * from bibliographic_reference natural join image where owner = '" + user.getName() + "'";

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
        String referenceQuery = "select * from bibliographic_reference natural join video where owner = '" + user.getName() + "'";

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
        String referenceQuery = "select * from bibliographic_reference natural join source_code where owner = '" + user.getName() + "'";

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

    /**
     * Restituisce gli identificativi dei rimandi di un riferimento.
     * 
     * @param statement
     *            statement da usare per il recupero
     * @param resultSet
     *            resultset da usare per il recupero
     * @param reference
     *            riferimento di cui recuperare i rimandi
     * @return lista con gli identificativi dei rimandi
     * @throws SQLException
     *             se si verifica un errore
     */
    private List<Integer> getRelatedReferencesIDs(Statement statement, ResultSet resultSet, BibliographicReference reference) throws SQLException {
        if (statement == null || reference == null || reference.getID() == null)
            throw new IllegalArgumentException();

        String relatedReferencesQuery = "select quotes from related_references where quoted_by = " + reference.getID();
        ArrayList<Integer> relatedReferencesIDs = new ArrayList<>();

        resultSet = statement.executeQuery(relatedReferencesQuery);

        while (resultSet.next())
            relatedReferencesIDs.add(resultSet.getInt("quotes"));

        relatedReferencesIDs.trimToSize();
        return relatedReferencesIDs;
    }

    // #region UTILITIES

    private String getFormattedStringForQuery(String input) {
        return (input == null || input.isEmpty() || input.isBlank()) ? null : "'" + input + "'";
    }

    private java.sql.Date convertToSQLDate(Date date) {
        return date == null ? null : new java.sql.Date(date.getTime());
    }

    /**
     * Converte la lingua di un riferimento in una stringa inseribile nel database.
     * 
     * @param language
     *            lingua del riferimento
     * @return stringa inseribile in un database
     */
    private String getFormattedLanguageForQuery(ReferenceLanguage language) {
        return getFormattedStringForQuery(language == ReferenceLanguage.NOTSPECIFIED ? null : language.name());
    }

    /**
     * TODO: commenta
     * 
     * @param programmingLanguage
     * @return
     */
    private String getFormattedProgrammingLanguageForQuery(ProgrammingLanguage programmingLanguage) {
        return getFormattedStringForQuery(programmingLanguage == ProgrammingLanguage.NOTSPECIFIED ? null : programmingLanguage.name());
    }

    // #endregion

}