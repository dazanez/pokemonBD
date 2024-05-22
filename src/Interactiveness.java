import Pokemon.Pokemon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Interactiveness {
    static final int EXIT_PROGRAM = 0,
            CHECK_POKEMONS_BY_TYPE = 1,
            CHECK_BEST_DEFENDERS = 2,
            CHECK_BEST_ATTACKERS = 3,
            COMPARE_POKEMONS = 4;
    static String[] POKEMONS_ATTRIBUTES =
            new String[]{"number", "name", "type1", "type2", "total", "hp",
                    "attack", "defense", "sp_attack", "sp_defense", "speed", "generation", "legendary"};

    static Scanner scanner = new Scanner(System.in);
    public static void mainInteractive() {
        System.out.println(Draws.pokemonDraw);
        int seleccion = selectMainOptionInteractive();

        while (seleccion != EXIT_PROGRAM) {
            if (seleccion == CHECK_POKEMONS_BY_TYPE) {
                seleccion = checkPokemonsByTypeInteractive();
            }
            else if (seleccion == CHECK_BEST_ATTACKERS) {
                seleccion = checkBestAttackersInteractive();
            }
        }

        System.out.println("¡Adiós pokemonero! Vuelve si quieres saber algo más");
    }

    public static int selectMainOptionInteractive() {
        String[] options = new String[]{EXIT_PROGRAM + 1 + ". Salir",
                CHECK_POKEMONS_BY_TYPE + 1 + ". Consultar por tipo",
                CHECK_BEST_DEFENDERS + 1 + ". Ver mejores defensas",
                CHECK_BEST_ATTACKERS + 1 + ". Ver mejores atacantes",
                COMPARE_POKEMONS + 1 + ". Comparar bichos"
        };

        System.out.println();
        System.out.println(("*".repeat(7) + " Selecciona una opción " + "*".repeat(7)).repeat(3));
        String[] headers = ("↓ ".repeat(options.length)).split(" ");
        printTable(Arrays.asList(headers), Arrays.asList(Arrays.asList(options)));
        int seleccion = scanner.nextInt();
        return seleccion-1;
    }

    public static int checkPokemonsByTypeInteractive() {
        System.out.println("Estos son los tipos de pokemon que existen, ¿cuales quieres ver?");
        String[] types = Pokemon.getAllTypes();
        List<List<String>> rows = new ArrayList<>();

        for (String type : types) {
            rows.add(Arrays.asList(type));
        }

        printTable(Arrays.asList(POKEMONS_ATTRIBUTES),
                Pokemon.toRowsList(Pokemon.getByType(types[getOptionFromRows(Arrays.asList("Type"), rows)])));

        return selectMainOptionInteractive();
    }

    public static int checkBestAttackersInteractive() {
        List<List<String>> bests = Pokemon.toRowsList(Pokemon.getBestsAttack());
        System.out.printf("Estos son nuestros %d mejores atacantes. ¡De aquí podría salir tu favorito!", bests.size());

        printTable(Arrays.asList(POKEMONS_ATTRIBUTES), bests);

        return selectMainOptionInteractive();
    }

    /**
     * Prints the table with the elements and returns the index of the selected element
     * @param headers
     * @param rows
     * @return
     */
    public static int getOptionFromRows(List<String> headers, List<List<String>> rows) {
        int seleccion;
        List<String> headersToPrint = new ArrayList<>(headers);
        List<List<String>> rowsToPrint = new ArrayList<>();

        for (List<String> row: rows) {
            rowsToPrint.add(new ArrayList<>(row));
        }

        headersToPrint.add(0, "#");

        for (int i = 0; i < rowsToPrint.size(); i++) {
            rowsToPrint.get(i).add(0, String.valueOf(i + 1));
        }

        printTable(headersToPrint, rowsToPrint);

        seleccion = scanner.nextInt();
        System.out.println("Seleccionaste a: " + rows.get(seleccion-1));
        return seleccion-1;
    }


    public static void printTable(List<String> headers, List<List<String>> rows) {
        // Calcula la longitud máxima de cada columna
        int[] columnWidths = new int[headers.size()];

        for (int i = 0; i < headers.size(); i++) {
            columnWidths[i] = headers.get(i).length();
        }

        for (List<String> row : rows) {
            for (int i = 0; i < row.size(); i++) {
                columnWidths[i] = Math.max(columnWidths[i], row.get(i).length());
            }
        }

        // Define el formato de cada fila
        StringBuilder formatBuilder = new StringBuilder();
        for (int width : columnWidths) {
            formatBuilder.append("| %-").append(width).append("s ");
        }
        formatBuilder.append("|\n");
        String rowFormat = formatBuilder.toString();

        System.out.println();
        // Imprime los encabezados
        System.out.printf(rowFormat, headers.toArray());

        // Imprime el separador
        StringBuilder separatorBuilder = new StringBuilder();
        for (int width : columnWidths) {
            separatorBuilder.append("+").append("-".repeat(width + 2));
        }
        separatorBuilder.append("+\n");
        String separator = separatorBuilder.toString();
        System.out.print(separator);

        // Imprime las filas
        for (List<String> row : rows) {
            System.out.printf(rowFormat, row.toArray());
        }

        // Imprime el separador final
        System.out.print(separator);
    }
}
