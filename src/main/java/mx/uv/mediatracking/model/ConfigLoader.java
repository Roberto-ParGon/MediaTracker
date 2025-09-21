package mx.uv.mediatracking.model; // O el paquete donde la hayas creado

import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {

    private static final Properties properties = new Properties();

    // Este bloque estático se ejecuta una sola vez cuando la clase es cargada.
    static {
        try (InputStream input = ConfigLoader.class.getResourceAsStream("/config.properties")) {
            if (input == null) {
                System.err.println("¡Error Crítico! No se encontró el archivo config.properties.");
                // En una aplicación real, podrías querer lanzar una excepción o cerrar el programa.
            }
            properties.load(input);
        } catch (Exception ex) {
            ex.printStackTrace();
            // Manejar la excepción si el archivo no se puede cargar
        }
    }

    // Métodos públicos y estáticos para obtener cada valor
    public static String getDbHost() {
        return properties.getProperty("db.host");
    }

    public static String getDbPort() {
        return properties.getProperty("db.port");
    }

    public static String getDbName() {
        return properties.getProperty("db.name");
    }

    public static String getDbUser() {
        return properties.getProperty("db.user");
    }

    public static String getDbPassword() {
        return properties.getProperty("db.password");
    }

    public static String getApiKey() {
        return properties.getProperty("tmdb.apikey");
    }
}