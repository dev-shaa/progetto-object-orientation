package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import Controller.DatabaseController;
import Entities.Tag;
import Entities.References.BibliographicReference;
import Exceptions.TagDatabaseException;

public class TagDAOPostgreSQL implements TagDAO {

    @Override
    public void save(Tag tag) throws TagDatabaseException {
        if (tag == null)
            return;

        Connection connection = null;
        Statement statement = null;
        String query = "insert into tag values('" + tag.getName() + "')";

        try {
            connection = DatabaseController.getConnection();
            statement = connection.createStatement();

            statement.executeUpdate(query);
        } catch (Exception e) {
            throw new TagDatabaseException("Impossibile salvare parola chiave");
        } finally {
            try {
                if (statement != null)
                    statement.close();

                if (connection != null)
                    connection.close();
            } catch (Exception e) {
                // non fare niente
                e.printStackTrace();
            }
        }
    }

    @Override
    public void save(Collection<? extends Tag> tags) throws TagDatabaseException {
        if (tags == null || tags.isEmpty())
            return;

        Connection connection = null;
        PreparedStatement statement = null;
        String command = "insert into tag values(?) on conflict do nothing";

        try {
            connection = DatabaseController.getConnection();
            connection.setAutoCommit(false);

            statement = connection.prepareStatement(command);

            for (Tag tag : tags) {
                statement.setString(1, tag.getName());
                statement.executeUpdate(command);
            }

            connection.commit();
        } catch (Exception e) {
            throw new TagDatabaseException("Impossibile salvare parole chiave");
        } finally {
            try {
                if (statement != null)
                    statement.close();

                if (connection != null)
                    connection.close();
            } catch (Exception e) {
                // non fare niente
                e.printStackTrace();
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
        String query = "select tag from reference_tag_associations where reference = " + reference.getID();

        try {
            connection = DatabaseController.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            ArrayList<Tag> tags = new ArrayList<>();

            while (resultSet.next())
                tags.add(new Tag(resultSet.getString("tag")));

            tags.trimToSize();
            return tags;
        } catch (Exception e) {
            throw new TagDatabaseException("Impossibile recuperare parole chiave");
        } finally {
            try {
                if (statement != null)
                    statement.close();

                if (connection != null)
                    connection.close();
            } catch (Exception e) {
                // non fare niente
                e.printStackTrace();
            }
        }
    }

}
