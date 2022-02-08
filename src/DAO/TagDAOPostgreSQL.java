package DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    public void save(Collection<? extends Tag> tags, BibliographicReference reference) throws TagDatabaseException {
        // TODO Auto-generated method stub

    }

    @Override
    public List<Tag> get(BibliographicReference reference) throws TagDatabaseException {
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
        } catch (Exception e) {
            e.printStackTrace();
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
