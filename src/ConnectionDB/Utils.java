package ConnectionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {
    static Connection connection;

    /**
     * Given a ResultSetMetaData you'll get a list the names of the columns
     * @param metaData the result of the getMetadata() method from a ResultSet object
     * @return A Strings Array with the names of the columns
     * @throws SQLException
     */
    public static String[] getColumnsNames(ResultSetMetaData metaData) throws SQLException {
        int numColumns = metaData.getColumnCount();
        String[] columnsNames = new String[numColumns];

        for (int i = 1; i <= numColumns; i++)
            columnsNames[i-1] = metaData.getColumnName(i); // getColumnName index starts in 1

        return columnsNames;
    }

    /**
     *
     * @param tableName The name of the table.
     * @return A Strings Array with the names of the columns.
     * @throws SQLException
     */
    public static String[] getColumnsNames(String tableName) throws SQLException {
        connection = ConnectionDB.connectToDatabase();
        Statement stmt = connection.createStatement(); // Objeto de tipo sentencia SQL
        ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName + " LIMIT 1;");
        String[] columnsNames = getColumnsNames(rs.getMetaData());

        stmt.close();
        rs.close();
        connection.close();
        return columnsNames;
    }

    /**
     *
     * @param rs A ResultSet object to get the results from
     * @param columns The columns that must be included in the result list
     * @return A list with every row of the query contained in a Map, each column is a key. If the result is empty returns null.
     * @throws SQLException
     */
    public static List<Map<String, String>> resultSetToList(ResultSet rs, String[] columns) throws SQLException {
        boolean emptyQuery = true;
        List<Map<String, String>> elements = new ArrayList<>();
        Map<String, String> currentRow;
        columns = (columns == null || columns.equals(new String[]{})) ? getColumnsNames(rs.getMetaData()) : columns; // Antes mostraba las columnas que se registraban en una lista, pero eso se puede definir en la consulta

        while (rs.next()) {
            emptyQuery = false;
            // Se obtienen datos de las tablas
            // Se crea un objeto con los datos obtenidos de la DB
            currentRow = new HashMap<>();
            for (String column : columns)
                currentRow.put(column, rs.getString(column));
            elements.add(currentRow);
        }

        return emptyQuery ? null : elements;
    }
    public static String getRowsStr(List<Map<String, String>> rows) {
        String rowsStr = "";
        for (Map<String, String> row : rows) {
            for (Map.Entry<String, String> entry : row.entrySet())
                rowsStr += entry.getKey() + ": " + entry.getValue() + " | ";
            rowsStr += "\n";
        }
        return rowsStr;
    }
}
