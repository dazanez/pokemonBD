package Pokemon;

import ConnectionDB.ConnectionDB;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pokemon {
    int number;
    String name, type1, type2;
    int total, hp, attack, defense, spAttack, spDefense, speed, generation;
    boolean legendary;

    public Pokemon(String[] pokemonData) {
        this(parseIntOrNull(pokemonData[0]),
        pokemonData[1],
        pokemonData[2],
        pokemonData[3],
        parseIntOrNull(pokemonData[4]),
        parseIntOrNull(pokemonData[5]),
        parseIntOrNull(pokemonData[6]),
        parseIntOrNull(pokemonData[7]),
        parseIntOrNull(pokemonData[8]),
        parseIntOrNull(pokemonData[9]),
        parseIntOrNull(pokemonData[10]),
        parseIntOrNull(pokemonData[11]),
        parseBoolOrNull(pokemonData[12]));
    }

    public Pokemon(int number, String name, String type1, String type2, int total,
                   int hp, int attack, int defense, int spAttack, int spDefense,
                   int speed, int generation, boolean legendary)
    {
        this.number = number;
        this.name = name;
        this.type1 = type1;
        this.type2 = type2;
        this.total = total;
        this.hp = hp;
        this.attack = attack;
        this.defense = defense;
        this.spAttack = spAttack;
        this.spDefense = spDefense;
        this.speed = speed;
        this.generation = generation;
        this.legendary = legendary;
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

    public static String[] getAllTypes() {
        String sql = "SELECT DISTINCT type1 FROM pokemon ORDER BY type1;";
        String[] pokemonTypes = new String[]{};

        try {
            List<Map<String, String>> typesMaps = ConnectionDB.executeQuery(sql);
            pokemonTypes = new String[typesMaps.size()];
            int count = 0;
            for (Map<String, String> typeMap : typesMaps)
                pokemonTypes[count++] = typeMap.getOrDefault("type1", "Sin tipo");
        }
        catch (SQLException err) {
            System.err.println("Ocurrió un error obteniendo los tipos de pokemon");
        }
        return pokemonTypes;
    }

    public static List<Pokemon> getBestsDefense() {
        String sql = "SELECT * FROM pokemon ORDER BY defense DESC LIMIT 10;";
        List<Pokemon> pokemons = new ArrayList();

        try {
            List<Map<String, String>> pokemonsMaps = ConnectionDB.executeQuery(sql);
            pokemons = pokemonMapsToList(pokemonsMaps);
        }
        catch (SQLException err) {
            System.err.println("Los mejores en defensa aún se están preparando");
            err.printStackTrace();
        }

        return pokemons;
    }


    public static List<Pokemon> getBestsAttack() {
        String sql = "SELECT * FROM pokemon ORDER BY attack DESC LIMIT 10;";
        List<Pokemon> pokemons = new ArrayList();

        try {
            List<Map<String, String>> pokemonsMaps = ConnectionDB.executeQuery(sql);
            pokemons = pokemonMapsToList(pokemonsMaps);
        }
        catch (SQLException err) {
            System.err.println("Los mejores en defensa aún se están preparando");
            err.printStackTrace();
        }

        return pokemons;
    }

    public static List<Pokemon> getAllPokemons() {
        String sql = "SELECT * FROM pokemon";
        List<Pokemon> pokemones = new ArrayList<>();
        try {
            List<Map<String, String>> pokemonesMaps = ConnectionDB.executeQuery(sql);
            pokemones = pokemonMapsToList(pokemonesMaps);
        } catch (SQLException err) {
            System.err.println("No se pudo obtener todos los pokemones");
        }

        return pokemones;
    }

    public static List<Pokemon> getByType(String type) {
        String sql = String.format("SELECT * FROM pokemon WHERE type1 = '%s' or type2 = '%s';", type, type);
        List<Pokemon> pokemons = new ArrayList();

        try {
            List<Map<String, String>> pokemonsMaps = ConnectionDB.executeQuery(sql);
            pokemons = pokemonMapsToList(pokemonsMaps);
        }
        catch (SQLException err) {
            System.err.println("Pokemones por tipo fallaron");
            err.printStackTrace();
        }

        return pokemons;
    }

    public static List<Pokemon> pokemonMapsToList(List<Map<String, String>> pokemonsMaps) {
        List<Pokemon> pokemons = new ArrayList<>();
        for (Map<String, String> pokemonsMap: pokemonsMaps) {
            String[] pokemonArray = new String[]{
                    pokemonsMap.getOrDefault("number", "Sin numero"),
                    pokemonsMap.getOrDefault("name", "Sin nombre"),
                    pokemonsMap.getOrDefault("type1", "Sin tipo1"),
                    pokemonsMap.getOrDefault("type2", "Sin tipo 2"),
                    pokemonsMap.getOrDefault("total", "Sin total"),
                    pokemonsMap.getOrDefault("hp", "Sin hp"),
                    pokemonsMap.getOrDefault("attack", "Sin ataque"),
                    pokemonsMap.getOrDefault("defense", "Sin defensa"),
                    pokemonsMap.getOrDefault("sp_attack", "Sin ataque sp"),
                    pokemonsMap.getOrDefault("sp_defense", "Sin defensa sp"),
                    pokemonsMap.getOrDefault("speed", "Sin velocidad"),
                    pokemonsMap.getOrDefault("generation", "Sin generación"),
                    pokemonsMap.getOrDefault("generation", "Sin legendario"),
            };
            pokemons.add(new Pokemon(pokemonArray));
        }
        return pokemons;
    }

    /**
     *
     * @param pokemons
     * @return A map with the names of the best. Ex: {"attack": "charizard", "defense":}
     */
    public static Map<String, Pokemon> getBestsPokemons(List<Pokemon> pokemons) {
        Map<String, Pokemon> bests = new HashMap<>();
        int bestAttack = 0, bestDefense = 0, bestSpeed = 0, bestHp = 0;

        for (Pokemon pokemon : pokemons) {
            if (pokemon.attack > bestAttack){
                bests.put("atack", pokemon);
                bestAttack = pokemon.attack;
            }
            if (pokemon.defense > bestDefense) {
                bests.put("defense", pokemon);
                bestDefense = pokemon.defense;
            }
            if (pokemon.speed > bestSpeed) {
                bests.put("speed", pokemon);
                bestSpeed = pokemon.speed;
            }
            if (pokemon.hp > bestHp) {
                bests.put("hp", pokemon);
                bestHp = pokemon.hp;
            }
        }
        return bests;
    }

    public static List<List<String>> toRowsList(List<Pokemon> pokemonList) {
        List<List<String>> rows = new ArrayList<>();

        for (Pokemon pokemon : pokemonList) {
            List<String> pokemonStrList = new ArrayList<>();
            pokemonStrList.add(String.valueOf(pokemon.number));
            pokemonStrList.add(String.valueOf(pokemon.name));
            pokemonStrList.add(String.valueOf(pokemon.type1));
            pokemonStrList.add(String.valueOf(pokemon.type2));
            pokemonStrList.add(String.valueOf(pokemon.total));
            pokemonStrList.add(String.valueOf(pokemon.hp));
            pokemonStrList.add(String.valueOf(pokemon.attack));
            pokemonStrList.add(String.valueOf(pokemon.defense));
            pokemonStrList.add(String.valueOf(pokemon.spAttack));
            pokemonStrList.add(String.valueOf(pokemon.spDefense));
            pokemonStrList.add(String.valueOf(pokemon.speed));
            pokemonStrList.add(String.valueOf(pokemon.generation));
            pokemonStrList.add(String.valueOf(pokemon.legendary));

            rows.add(pokemonStrList);
        }

        return rows;
    }

    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public String getType1() {
        return type1;
    }

    public String getType2() {
        return type2;
    }

    public int getTotal() {
        return total;
    }

    public int getHp() {
        return hp;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    public int getSpAttack() {
        return spAttack;
    }

    public int getSpDefense() {
        return spDefense;
    }

    public int getSpeed() {
        return speed;
    }

    public int getGeneration() {
        return generation;
    }

    public boolean isLegendary() {
        return legendary;
    }

    @Override
    public String toString() {
        return "Pokemon{" +
                "number=" + number +
                ", name='" + name + '\'' +
                ", type1='" + type1 + '\'' +
                ", type2='" + type2 + '\'' +
                ", total=" + total +
                ", hp=" + hp +
                ", attack=" + attack +
                ", defense=" + defense +
                ", spAttack=" + spAttack +
                ", spDefense=" + spDefense +
                ", speed=" + speed +
                ", generation=" + generation +
                ", legendary=" + legendary +
                '}';
    }
}
