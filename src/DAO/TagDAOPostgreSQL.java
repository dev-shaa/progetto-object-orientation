package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import Controller.ConnectionController;
import Entities.Tag;
import Exceptions.Database.DatabaseConnectionException;
import Exceptions.Database.TagDatabaseException;

/**
 * Implementazione dell'interfaccia TagDAO per database relazionali PostgreSQL.
 * 
 * @see TagDAO
 */
public class TagDAOPostgreSQL implements TagDAO {

    @Override
    public void save(int referenceID, Collection<? extends Tag> tags) throws TagDatabaseException {
        if (tags == null || tags.isEmpty())
            return;

        Connection connection = null;
        PreparedStatement tagRemoveStatement = null;
        PreparedStatement tagInsertStatement = null;
        ResultSet resultSet = null;

        // per aggiornare i tag associati a un riferimento è più semplice rimuoverli tutti e poi aggiungere i nuovi

        String tagRemoveCommand = "delete from tag where reference = ?";
        String tagInsertCommand = "insert into tag values(?, ?)";

        try {
            connection = ConnectionController.getConnection();
            connection.setAutoCommit(false);

            tagRemoveStatement = connection.prepareStatement(tagRemoveCommand);
            tagInsertStatement = connection.prepareStatement(tagInsertCommand);

            tagRemoveStatement.setInt(1, referenceID);
            tagRemoveStatement.executeUpdate();

            tagInsertStatement.setInt(2, referenceID);
            for (Tag tag : tags) {
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

    @Override
    public List<Tag> getAll(int referenceID) throws TagDatabaseException {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        String command = "select name from tag where reference = " + referenceID;

        try {
            connection = ConnectionController.getConnection();
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