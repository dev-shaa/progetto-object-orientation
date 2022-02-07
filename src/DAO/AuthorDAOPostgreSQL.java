package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import Controller.DatabaseController;
import Entities.Author;
import Entities.References.BibliographicReference;
import Exceptions.AuthorDatabaseException;
import Exceptions.DatabaseConnectionException;

/**
 * Implementazione dell'interfaccia AuthorDAO per database relazionali PostgreSQL.
 * 
 * @see AuthorDAO
 */
public class AuthorDAOPostgreSQL implements AuthorDAO {

    @Override
    public void save(Collection<? extends Author> authors) throws AuthorDatabaseException {
        if (authors == null || authors.isEmpty())
            return;

        Connection connection = null;
        PreparedStatement insertStatement = null;
        PreparedStatement retrieveIDStatement = null;
        ResultSet idResultSet = null;

        String insertCommand = "insert into author(name, orcid) values (?, ?) on conflict do nothing";
        String retrieveIDCommand = "select id from author where name = ? and orcid is not distinct from ?";

        try {
            connection = DatabaseController.getConnection();
            insertStatement = connection.prepareStatement(insertCommand);
            retrieveIDStatement = connection.prepareStatement(retrieveIDCommand);

            for (Author author : authors) {
                insertStatement.setString(1, author.getName());
                insertStatement.setString(2, author.getORCID());
                insertStatement.executeUpdate();

                retrieveIDStatement.setString(1, author.getName());
                retrieveIDStatement.setString(2, author.getORCID());
                idResultSet = retrieveIDStatement.executeQuery();

                if (idResultSet.next()) {
                    int id = idResultSet.getInt("id");
                    author.setId(id);
                }
            }
        } catch (SQLException | DatabaseConnectionException e) {
            try {
                connection.rollback();
            } catch (Exception ex) {
                // non fare niente
            }

            e.printStackTrace();

            throw new AuthorDatabaseException("Impossibile salvare l'autore");
        } finally {
            try {
                if (idResultSet != null)
                    idResultSet.close();

                if (insertStatement != null)
                    insertStatement.close();

                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                // non fare niente
            }
        }
    }

    @Override
    public List<Author> get(BibliographicReference reference) throws AuthorDatabaseException {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        String query = "select * from author_reference_association join author on author = id where reference = " + reference.getID();

        try {
            connection = DatabaseController.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            ArrayList<Author> authors = new ArrayList<>();

            while (resultSet.next()) {
                authors.add(new Author(resultSet.getString("name"), resultSet.getString("orcid")));
            }

            authors.trimToSize();
            return authors;
        } catch (SQLException | DatabaseConnectionException e) {
            e.printStackTrace();
            throw new AuthorDatabaseException("Impossibile salvare l'autore");
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
