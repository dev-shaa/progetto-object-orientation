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
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        // TODO: command
        String command = "insert into author(name, orcid) values (?, ?) on conflict do nothing returning id";

        try {
            connection = DatabaseController.getConnection();
            connection.setAutoCommit(false);

            statement = connection.prepareStatement(command, Statement.RETURN_GENERATED_KEYS);

            for (Author author : authors) {
                statement.setString(1, author.getName());
                statement.setString(2, author.getORCID());
                statement.executeUpdate();

                resultSet = statement.getGeneratedKeys();

                if (resultSet.next()) {
                    author.setId(resultSet.getInt("id"));
                }
            }

            connection.commit();
        } catch (SQLException | DatabaseConnectionException e) {
            try {
                connection.rollback();
            } catch (Exception ex) {
                // non fare niente
            }

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
