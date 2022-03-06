package Database.DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import Database.Connections.ConnectionController;
import Database.Connections.CustomConnection;
import Entities.Tag;

/**
 * Implementazione dell'interfaccia TagDAO per database relazionali PostgreSQL.
 * 
 * @see TagDAO
 */
public class TagDAOPostgreSQL implements TagDAO {

    @Override
    public void save(int referenceID, Collection<? extends Tag> tags) throws SQLException {
        if (tags == null || tags.isEmpty())
            return;

        CustomConnection connection = null;
        PreparedStatement tagRemoveStatement = null;
        PreparedStatement tagInsertStatement = null;
        ResultSet resultSet = null;

        // per aggiornare i tag associati a un riferimento è più semplice rimuoverli tutti e poi aggiungere i nuovi

        String tagRemoveCommand = "delete from tag where reference = ?";
        String tagInsertCommand = "insert into tag values(?, ?)";

        try {
            connection = ConnectionController.getInstance().getConnection();
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
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            if (resultSet != null)
                resultSet.close();

            if (tagInsertStatement != null)
                tagInsertStatement.close();

            if (tagRemoveStatement != null)
                tagRemoveStatement.close();

            if (connection != null)
                connection.close();
        }
    }

    @Override
    public List<Tag> getAll(int referenceID) throws SQLException {
        CustomConnection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        String command = "select name from tag where reference = " + referenceID;

        try {
            connection = ConnectionController.getInstance().getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(command);

            ArrayList<Tag> tags = new ArrayList<>();

            while (resultSet.next())
                tags.add(new Tag(resultSet.getString("name")));

            tags.trimToSize();

            return tags;
        } finally {
            if (resultSet != null)
                resultSet.close();

            if (statement != null)
                statement.close();

            if (connection != null)
                connection.close();
        }
    }

}