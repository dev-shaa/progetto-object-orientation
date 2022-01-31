package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import Controller.DatabaseController;
import Entities.Category;
import Entities.Tag;
import Entities.User;
import Entities.References.BibliographicReference;
import Entities.References.ReferenceLanguage;
import Entities.References.OnlineResources.*;
import Entities.References.PhysicalResources.*;
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

    @Override
    public List<BibliographicReference> getReferences() throws ReferenceDatabaseException {
        // TODO: DEBUG:
        try {
            Article article = new Article("Articolo 1");
            ArrayList<Category> categories = new ArrayList<>(1);
            categories.add(new Category("AAA", 1));

            article.setCategories(categories);
            article.setDescription("Breve descrizione dell'articolo");

            ArrayList<BibliographicReference> references = new ArrayList<>(1);
            references.add(article);

            return references;
        } catch (Exception e) {
            throw new ReferenceDatabaseException();
        }

        // Connection connection = null;

        // PreparedStatement referenceStatement = null;
        // ResultSet referenceResultSet = null;
        // String referenceQuery = "select * from bibliographic_reference natural join ? where user = " + getUser().getName();

        // PreparedStatement tagsStatement = null;
        // ResultSet tagsResultSet = null;
        // String tagsQuery = "select * from tags natural join reference_tag where id = ?";

        // PreparedStatement relatedReferencesStatement = null;
        // ResultSet relatedReferencesResultSet = null;
        // String relatedReferencesQuery = "select * from quotations where id = ?";

        // ArrayList<BibliographicReference> references = new ArrayList<>();

        // HashMap<Integer, BibliographicReference> idToReference = new HashMap<>();
        // HashMap<BibliographicReference, Collection<Integer>> referenceToRelatedID = new HashMap<>();

        // try {
        // connection = DatabaseController.getConnection();

        // referenceStatement = connection.prepareStatement(referenceQuery);
        // tagsStatement = connection.prepareStatement(tagsQuery);
        // relatedReferencesStatement = connection.prepareStatement(relatedReferencesQuery);

        // referenceStatement.setString(1, "article");
        // referenceResultSet = referenceStatement.executeQuery();
        // while (referenceResultSet.next()) {
        // Article article = new Article(referenceResultSet.getString("name"));
        // article.setID(referenceResultSet.getInt("id"));
        // article.setDOI(referenceResultSet.getString("doi"));
        // article.setPubblicationDate(referenceResultSet.getDate("pubblicationDate"));
        // article.setDescription(referenceResultSet.getString("description"));
        // article.setLanguage(ReferenceLanguage.valueOf(referenceResultSet.getString("language")));

        // article.setISSN(referenceResultSet.getString("issn"));

        // // TODO: tag, autori, categorie

        // tagsStatement.setString(1, article.getID().toString());
        // tagsResultSet = tagsStatement.executeQuery();

        // ArrayList<Tag> tags = new ArrayList<>();

        // while (tagsResultSet.next())
        // tags.add(new Tag(tagsResultSet.getString("name")));

        // tags.trimToSize();
        // article.setTags(tags);

        // relatedReferencesStatement.setString(1, article.getID().toString());
        // relatedReferencesResultSet = relatedReferencesStatement.executeQuery();

        // ArrayList<Integer> relatedReferencesID = new ArrayList<>();

        // while (relatedReferencesResultSet.next())
        // relatedReferencesID.add(relatedReferencesResultSet.getInt("aaa"));

        // relatedReferencesID.trimToSize();

        // referenceToRelatedID.put(article, relatedReferencesID);
        // idToReference.put(article.getID(), article);

        // references.add(article);
        // }

        // for (BibliographicReference reference : references) {
        // ArrayList<BibliographicReference> relatedReferences = new ArrayList<>();

        // for (Integer referenceID : referenceToRelatedID.get(reference)) {
        // relatedReferences.add(idToReference.get(referenceID));
        // }

        // reference.setRelatedReferences(relatedReferences);
        // }

        // return references;
        // } catch (Exception e) {
        // throw new ReferenceDatabaseException("Impossibile recuperare i riferimenti dell'utente.");
        // } finally {
        // try {
        // if (tagsResultSet != null)
        // tagsResultSet.close();

        // if (tagsStatement != null)
        // tagsStatement.close();

        // if (referenceResultSet != null)
        // referenceResultSet.close();

        // if (referenceStatement != null)
        // referenceStatement.close();

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

    // https://stackoverflow.com/a/63970374

    /**
     * {@inheritDoc}
     * 
     * @throws IllegalArgumentException
     *             se {@code article == null}
     */
    @Override
    public void saveReference(Article article) throws ReferenceDatabaseException {
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

            throw new ReferenceDatabaseException("Impossibile aggiungere nuovo riferimento.");
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

    /**
     * {@inheritDoc}
     * 
     * @throws IllegalArgumentException
     *             se {@code book == null}
     */
    @Override
    public void saveReference(Book book) throws ReferenceDatabaseException {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     * 
     * @throws IllegalArgumentException
     *             se {@code thesis == null}
     */
    @Override
    public void saveReference(Thesis thesis) throws ReferenceDatabaseException {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     * 
     * @throws IllegalArgumentException
     *             se {@code image == null}
     */
    @Override
    public void saveReference(Image image) throws ReferenceDatabaseException {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     * 
     * @throws IllegalArgumentException
     *             se {@code sourceCode == null}
     */
    @Override
    public void saveReference(SourceCode sourceCode) throws ReferenceDatabaseException {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     * 
     * @throws IllegalArgumentException
     *             se {@code video == null}
     */
    @Override
    public void saveReference(Video video) throws ReferenceDatabaseException {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     * 
     * @throws IllegalArgumentException
     *             se {@code website == null}
     */
    @Override
    public void saveReference(Website website) throws ReferenceDatabaseException {
        // TODO Auto-generated method stub

    }

}
