package DataInsertion;

import ConnectionDB.ConnectionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CsvToDatabase {
    static String sqlTableCreation = "DROP TABLE IF EXISTS pokemon;\n" +
            "CREATE TABLE pokemon(\n" +
            "id_pokemon SERIAL PRIMARY KEY,\n" +
            "number INT,\n" +
            "name VARCHAR(255),\n" +
            "type1 VARCHAR(255),\n" +
            "type2 VARCHAR(255),\n" +
            "total INT,\n" +
            "hp INT,\n" +
            "attack INT,\n" +
            "defense INT,\n" +
            "sp_attack INT,\n" +
            "sp_defense INT,\n" +
            "speed INT,\n" +
            "generation INT,\n" +
            "legendary BOOLEAN\n" +
            ");";

    public static boolean insertPokemons(String csvFilePath) {
        boolean insertionSuccess = false;

        String sqlInsert = "INSERT INTO pokemon(number, name, type1, type2, total, hp, attack, defense, sp_attack, sp_defense, speed, generation, legendary)\n" +
            "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?);";
        Connection connection;
        int batchSize = 20; // The statements will execute when reach a batch of 20

        try {
            CsvReader csv = new CsvReader(csvFilePath);

            connection = ConnectionDB.connectToDatabase();
            connection.setAutoCommit(false); // This avoids to execute every statement automatically

            connection.createStatement().execute(sqlTableCreation);
            System.out.println("La tabla pokemon se ha creado");

            PreparedStatement statement = connection.prepareStatement(sqlInsert);

            int count = 0;

            while (csv.getCurrentLine() != null) {
                String[] data = csv.nextLine();

                if (data == null) break;

                int number = parseIntOrNull(data[0]);
                String name = data[1], type1 = data[2], type2 = data[3];
                int total = parseIntOrNull(data[4]),
                        hp = parseIntOrNull(data[5]),
                        attack = parseIntOrNull(data[6]),
                        defense = parseIntOrNull(data[7]),
                        spAttack = parseIntOrNull(data[8]),
                        spDefense = parseIntOrNull(data[9]),
                        speed = parseIntOrNull(data[10]),
                        generation = parseIntOrNull(data[11]);
                boolean legendary = parseBoolOrNull(data[12]);

                statement.setInt(1, number);
                statement.setString(2, name);
                statement.setString(3, type1);
                statement.setString(4, type2);
                statement.setInt(5, total);
                statement.setInt(6, hp);
                statement.setInt(7, attack);
                statement.setInt(8, defense);
                statement.setInt(9, spAttack);
                statement.setInt(10, spDefense);
                statement.setInt(11, speed);
                statement.setInt(12, generation);
                statement.setBoolean(13, legendary);

                statement.addBatch();

                if (count % batchSize == 0) // Executes the statements when there are 20
                    statement.executeBatch();
                count++;
            }
            csv.close();

            statement.executeBatch();

            connection.commit();
            connection.close();
            insertionSuccess = true;

            System.out.println("Se insertaron los datos (aparentemente)");
        }
        catch (SQLException err) {
            System.err.println("Error en la inserción de datos");
            err.printStackTrace();
        }
        catch (Exception err) {
            System.err.println("Ocurrió un error en la inserción");
            err.printStackTrace();
        }

        return insertionSuccess;
    }

    public static Integer parseIntOrNull(String s) {
        Integer value = null;
        try {
            value = Integer.parseInt(s);
        } catch (NumberFormatException ignored) {}
        return value;
    }

    public static Boolean parseBoolOrNull(String s) {
        Boolean value = null;
        try {
            value = Boolean.parseBoolean(s);
        } catch (NumberFormatException ignored) {}
        return value;
    }
}
