package DataInsertion;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CsvReader {
    String[] titles;
    String csvFilePath;
    String currentLine;
    BufferedReader lineReader;
    static char separator = ',';

    /**
     * Takes the first line as the title line, and sets the titles property using it
     * @param csvFilePath The path of the CSV file on the PC
     */
    public CsvReader(String csvFilePath) {
        this.csvFilePath = csvFilePath;

        try {
            lineReader = new BufferedReader(new FileReader(csvFilePath));

            currentLine = lineReader.readLine();
            titles = currentLine != null ? currentLine.split(String.valueOf(separator)) : null;
        } catch (IOException err) {
            System.err.println("Hubo un error al leer el archivo CSV");
            err.printStackTrace();

            lineReader = null;
            currentLine = null;
            titles = null;
        }
    }

    /**
     *
     * @return A String array with the titles of the CSV file
     */
    public String[] getTitles() {
        return titles;
    }

    /**
     * Reads the next line of the current csv file
     * @return A string with the values of the line, or null if the end of the file has no characters
     */
    public String[] nextLine() {
        try {
            currentLine = lineReader.readLine();
        } catch (IOException err) {
            System.err.println("Ocurrió obteniendo la linea siguiente");
            err.printStackTrace();
        }

        return currentLine != null ? currentLine.split(String.valueOf(separator)) : null;
    }

    /**
     * Is the most recent line that has been read. It is updated every time nextLine() is called
     * @return An array with the elements of the current line
     */
    public String[] getCurrentLine() {
        return currentLine != null ? currentLine.split(String.valueOf(separator)) : null;
    }

    public void close() {
        try {
            lineReader.close();
            System.out.println("Se cerro el archivo CSV, ya no se debe usar nextLine()");
        } catch (IOException err) {
            System.out.println("Ocurrió un error cerrando el CSV");
            err.printStackTrace();
        }
    }
}
