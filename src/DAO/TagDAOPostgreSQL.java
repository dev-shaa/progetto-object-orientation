package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Controller.DatabaseController;
import Entities.Tag;
import Entities.References.BibliographicReference;
import Exceptions.Database.DatabaseConnectionException;
import Exceptions.Database.TagDatabaseException;

/**
 * Implementazione dell'interfaccia TagDAO per database relazionali PostgreSQL.
 * 
 * @see TagDAO
 */
public class TagDAOPostgreSQL implements TagDAO {

    /**
     * {@inheritDoc}
     * 
     * @throws IllegalArgumentException
     *             se {@code reference == null}
     * @implNote se al riferimento non sono associate parole chiave, non esegue nulla
     */
    @Override
    public void save(BibliographicReference reference) throws TagDatabaseException {
        if (reference == null)
            throw new IllegalArgumentException("reference can't be null");

        if (reference.getTags() == null || reference.getTags().isEmpty())
            return;

        Connection connection = null;
        PreparedStatement tagRemoveStatement = null;
        PreparedStatement tagInsertStatement = null;
        ResultSet resultSet = null;

        String tagRemoveCommand = "delete from tag where reference = ?";
        String tagInsertCommand = "insert into tag values(?, ?)";

        try {
            connection = DatabaseController.getConnection();
            connection.setAutoCommit(false);

            tagRemoveStatement = connection.prepareStatement(tagRemoveCommand);
            tagInsertStatement = connection.prepareStatement(tagInsertCommand);

            tagRemoveStatement.setInt(1, reference.getID());
            tagRemoveStatement.executeUpdate();

            tagInsertStatement.setInt(2, reference.getID());
            for (Tag tag : reference.getTags()) {
                tagInsertStatement.setString(1, tag.getName());
                tagInsertStatement.executeUpdate();
            }

            connection.commit();
        } catch (SQLException | DatabaseConnectionException e) {
            try {
                connection.rollback();
            } catch (Exception r) {
                // non fare niente
            }

            throw new TagDatabaseException("Impossibile salvare parole chiave.");
        } finally {
            try {
                if (resultSet != null)
                    resultSet.close();

                if (tagInsertStatement != null)
                    tagInsertStatement.close();

                if (tagRemoveStatement != null)
                    tagRemoveStatement.close();

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
    public List<Tag> get(BibliographicReference reference) throws TagDatabaseException {
        if (reference == null)
            throw new IllegalArgumentException("reference can't be null");

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        String command = "select name from tag where reference = " + reference.getID();

        try {
            connection = DatabaseController.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(command);

            ArrayList<Tag> tags = new ArrayList<>();

            while (resultSet.next())
                tags.add(new Tag(resultSet.getString("name")));

            tags.trimToSize();
            return tags;
        } catch (SQLException | DatabaseConnectionException e) {
            throw new TagDatabaseException("Impossibile recuperare le parole chiave del riferimento.");
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

}
