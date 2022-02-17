package DAO;

import java.sql.*;
import java.util.*;

import Controller.DatabaseController;
import Entities.Author;
import Entities.References.BibliographicReference;
import Exceptions.Database.AuthorDatabaseException;
import Exceptions.Database.DatabaseConnectionException;

/**
 * Implementazione dell'interfaccia AuthorDAO per database relazionali PostgreSQL.
 * 
 * @see AuthorDAO
 */
public class AuthorDAOPostgreSQL implements AuthorDAO {

    /**
     * {@inheritDoc}
     * 
     * @implNote se {@code authors == null || authors.isEmpty()} non esegue nulla
     */
    @Override
    public void save(Collection<? extends Author> authors) throws AuthorDatabaseException {
        if (authors == null || authors.isEmpty())
            return;

        Connection connection = null;
        PreparedStatement insertStatement = null;
        PreparedStatement retrieveIDStatement = null;
        ResultSet idResultSet = null;

        // nota: non è possibile usare la clausola "returning" perchè ci sono più vincoli nell'inserimento
        // con "on conflict" ne è possibile specificare solo uno al massimo
        // non specificandolo non è possibile usare "returning"
        // essendo la combinazione di nome e orcid univoca, almeno possiamo eseguire una query per recuperare l'id dell'autore
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

                if (idResultSet.next())
                    author.setId(idResultSet.getInt("id"));
            }
        } catch (SQLException | DatabaseConnectionException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                // non fare niente
            }

            throw new AuthorDatabaseException("Impossibile salvare gli autori.");
        } finally {
            try {
                if (idResultSet != null)
                    idResultSet.close();

                if (retrieveIDStatement != null)
                    retrieveIDStatement.close();

                if (insertStatement != null)
                    insertStatement.close();

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
     *             se {@code reference == null}
     */
    @Override
    public List<Author> get(BibliographicReference reference) throws AuthorDatabaseException {
        if (reference == null)
            throw new IllegalArgumentException("reference can't be null");

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
            throw new AuthorDatabaseException("Impossibile recuperare gli autori.");
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
