package ConnectionDB;

import java.sql.*;
import java.util.*;


public class ConnectionDB {
    static String DB = CredentialsDB.credentials.getOrDefault("DB", "pokemons");
    static String PORT = CredentialsDB.credentials.getOrDefault("PORT","5433");
    static String USER = CredentialsDB.credentials.getOrDefault("USER", "postgres");
    static String HOST = CredentialsDB.credentials.getOrDefault("HOST", "localhost");
    static String PASS = CredentialsDB.credentials.getOrDefault("PASS", "12345");
    static private Connection connection = null;

    public static void main(String[] args) throws SQLException {
        connection = connectToDatabase();
    }

    public static Connection connectToDatabase() {
        Connection newConnection = null;
        try {
            Class.forName("org.postgresql.Driver"); // Verifica que exista el driver de postgresl
        } catch (ClassNotFoundException err) {
            System.out.println("Error al registrar el driver de PostgreSQL: " + err);
        }

        // Database connect
        try {
            newConnection = DriverManager.getConnection(
                    "jdbc:postgresql://" + HOST + ":" + PORT +"/" + DB, USER, PASS);
            System.out.println("¡Se ha establecido la conexión con la base de datos correctamente!");

        } catch (SQLException err) {
            err.printStackTrace();
        }

        return newConnection;
    }

    /**
     * Ex: executeQuery("SELECT * FROM ticket;");
     * @param sqlQuery A SQL code with the desired query
     * @return A list with each row of the query contained in a Map, each column is a key. If the result is empty returns null
     * */
    public static List<Map<String, String>> executeQuery(String sqlQuery) throws SQLException {
        connection = connectToDatabase();
        Statement stmt = connection.createStatement(); // Objeto de tipo sentencia SQL
        ResultSet rs = stmt.executeQuery(sqlQuery);
        List<Map<String, String>> elements = Utils.resultSetToList(rs, null);
        rs.close();
        stmt.close();
        // Se cierra conexion a la BD
        connection.close();
        // Se retorna toda la coleccion

        return elements;
    }

    /**
     *
     * @param sql The sql statement
     * @return the amount of rows that were affected
     * @throws SQLException
     */
    public static int insertUpdateDelete(String sql) throws SQLException {
        connection = connectToDatabase();
        Statement stmt = connection.createStatement();
        int rowsAffected;

        rowsAffected = stmt.executeUpdate(sql);
        stmt.close();
        connection.close();
        return rowsAffected;
    }
}
