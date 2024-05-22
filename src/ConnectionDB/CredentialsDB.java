package ConnectionDB;

import java.util.HashMap;

public class CredentialsDB {
    // Modify the values according to your database credentials (DO NOT modify the
    // keys)
    static HashMap<String, String> credentials = new HashMap<>() {
        {
            put("DB", "pokemons");
            put("PORT", "5433");
            put("USER", "postgres");
            put("HOST", "localhost");
            put("PASS", "12345");
        }
    };
}
