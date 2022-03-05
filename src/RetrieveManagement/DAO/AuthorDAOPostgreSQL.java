package RetrieveManagement.DAO;

import java.sql.*;
import java.util.*;
import Entities.Author;
import RetrieveManagement.Connections.ConnectionController;
import RetrieveManagement.Connections.CustomConnection;

/**
 * Implementazione dell'interfaccia AuthorDAO per database relazionali PostgreSQL.
 * 
 * @see AuthorDAO
 */
public class AuthorDAOPostgreSQL implements AuthorDAO {

    @Override
    public void save(Collection<? extends Author> authors) throws SQLException {
        if (authors == null || authors.isEmpty())
            return;

        CustomConnection connection = null;
        PreparedStatement insertStatement = null;
        PreparedStatement retrieveIDStatement = null;
        ResultSet idResultSet = null;

        // nota: non è possibile usare la clausola "returning" perchè ci sono più vincoli nell'inserimento
        // con "on conflict" ne è possibile specificare solo uno al massimo
        // non specificandolo non è possibile usare "returning"
        // essendo la combinazione di nome e orcid univoca, almeno possiamo eseguire una query per recuperare l'id dell'autore
        String insertCommand = "insert into author(name, orcid) values (?, ?) on conflict do nothing";

        // nota: per orcid usiamo "is not distinct" perchè potrebbe avere anche valore null
        String retrieveIDCommand = "select id from author where lower(name) = lower(?) and orcid is not distinct from ?";

        try {
            connection = ConnectionController.getInstance().getConnection();

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
                    author.setID(idResultSet.getInt("id"));
            }

            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            if (idResultSet != null)
                idResultSet.close();

            if (retrieveIDStatement != null)
                retrieveIDStatement.close();

            if (insertStatement != null)
                insertStatement.close();

            if (connection != null)
                connection.close();
        }
    }

    @Override
    public List<Author> get(int referenceID) throws SQLException {
        CustomConnection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        String query = "select * from authorship join author on author = id where reference = " + referenceID;

        try {
            connection = ConnectionController.getInstance().getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            ArrayList<Author> authors = new ArrayList<>();

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String orcid = resultSet.getString("orcid");
                int id = resultSet.getInt("id");

                authors.add(new Author(name, orcid, id));
            }

            authors.trimToSize();
            return authors;
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