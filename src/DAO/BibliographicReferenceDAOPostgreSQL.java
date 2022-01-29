package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Entities.Category;
import Entities.User;
import Entities.References.BibliographicReference;
import Entities.References.OnlineResources.Image;
import Entities.References.OnlineResources.SourceCode;
import Entities.References.OnlineResources.Video;
import Entities.References.OnlineResources.Website;
import Entities.References.PhysicalResources.Article;
import Entities.References.PhysicalResources.Book;
import Entities.References.PhysicalResources.Thesis;
import Exceptions.ReferenceDatabaseException;

public class BibliographicReferenceDAOPostgreSQL implements BibliographicReferenceDAO {

    private User user;

    public BibliographicReferenceDAOPostgreSQL(User user) throws IllegalArgumentException {
        setUser(user);
    }

    public void setUser(User user) throws IllegalArgumentException {
        if (user == null)
            throw new IllegalArgumentException("user non può essere null");

        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public List<BibliographicReference> getReferences() {
        // TODO: DEBUG:
        Article article = new Article("Articolo 1");
        ArrayList<Category> categories = new ArrayList<>(1);
        categories.add(new Category("AAA", 0));

        article.setCategories(categories);
        article.setDescription("Breve descrizione dell'articolo");

        ArrayList<BibliographicReference> references = new ArrayList<>(1);
        references.add(article);

        return references;

        // Connection connection = null;
        // PreparedStatement tagsStatement = null;
        // ResultSet tagsResultSet = null;
        // String tagsQuery = "select * from tags natural join reference_tag where id = ?";
        // ArrayList<BibliographicReference> references = new ArrayList<>();

        // try {
        // connection = DatabaseController.getConnection();
        // tagsStatement = connection.prepareStatement(tagsQuery);

        // tagsStatement.setString(1, "article");
        // tagsStatement.setString(2, user.getName());

        // tagsResultSet = tagsStatement.executeQuery();

        // while (tagsResultSet.next()) {
        // // Article article = new Article(resultSet.getString("title"), authors)
        // }

        // return references.toArray(new BibliographicReference[references.size()]);
        // } catch (Exception e) {
        // throw new ReferenceDatabaseException("Impossibile recuperare i riferimenti dell'utente.");
        // } finally {
        // try {
        // if (tagsResultSet != null)
        // tagsResultSet.close();

        // if (tagsStatement != null)
        // tagsStatement.close();

        // if (connection != null)
        // connection.close();
        // } catch (Exception e) {
        // // non fare niente
        // }
        // }
    }

    @Override
    public void removeReference(BibliographicReference reference) throws ReferenceDatabaseException {
        Connection connection = null;
        Statement statement = null;
        String query = "delete from bibliographicReference where id = ";

        try {
            connection = DatabaseController.getConnection();
            statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (Exception e) {
            throw new ReferenceDatabaseException("Impossibile recuperare i riferimenti dell'utente.");
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
    public void saveReference(Article article) throws ReferenceDatabaseException, IllegalArgumentException {
        if (article == null)
            throw new IllegalArgumentException("article non può essere null");

        Connection connection = null;
        Statement statement = null;
        ResultSet set = null;

        try {
            connection = DatabaseController.getConnection();
            connection.setAutoCommit(false);

            statement = connection.createStatement();

            String query = "insert into category(title, pubblicationDate, DOI, description, language) values("
                    + article.getTitle() + ", "
                    + article.getPubblicationDate() + ","
                    + article.getDOI() + ","
                    + article.getDescription() + ","
                    + article.getLanguage()
                    + ")";

            statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);

            set = statement.getGeneratedKeys();

            if (set.first()) {
                article.setID(set.getInt(1));
            }

            query = "insert into article(id, pageCount, url, publisher, issn) values("
                    + article.getID() + ", "
                    + article.getPageCount() + ","
                    + article.getURL() + ","
                    + article.getPublisher() + ","
                    + article.getISSN()
                    + ")";

            statement.executeUpdate(query);

            connection.commit();
        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (Exception r) {
                // non fare niente
            }

            throw new ReferenceDatabaseException("Impossibile aggiungere nuova categoria.");
        } finally {
            try {
                if (set != null)
                    set.close();

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
    public void saveReference(Book book) throws ReferenceDatabaseException {
        // TODO Auto-generated method stub

    }

    @Override
    public void saveReference(Thesis thesis) throws ReferenceDatabaseException {
        // TODO Auto-generated method stub

    }

    @Override
    public void saveReference(Image image) throws ReferenceDatabaseException {
        // TODO Auto-generated method stub

    }

    @Override
    public void saveReference(SourceCode sourceCode) throws ReferenceDatabaseException {
        // TODO Auto-generated method stub

    }

    @Override
    public void saveReference(Video video) throws ReferenceDatabaseException {
        // TODO Auto-generated method stub

    }

    @Override
    public void saveReference(Website website) throws ReferenceDatabaseException {
        // TODO Auto-generated method stub

    }

}
