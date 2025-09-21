package mx.uv.mediatracking.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class DatabaseService {

    public static Connection getConnection() throws SQLException {
        // Construimos la URL usando los valores de ConfigLoader
        String url = "jdbc:mariadb://"
                + ConfigLoader.getDbHost() + ":"
                + ConfigLoader.getDbPort() + "/"
                + ConfigLoader.getDbName();

        return DriverManager.getConnection(url, ConfigLoader.getDbUser(), ConfigLoader.getDbPassword());
    }

    public static void initializeDatabase() {

        String sqlSeries = "CREATE TABLE IF NOT EXISTS series ("
                + "id INT PRIMARY KEY, "
                + "title VARCHAR(255) NOT NULL, "
                + "overview TEXT, "
                + "poster_path VARCHAR(255), "
                + "number_of_seasons INT, "
                + "current_season INT DEFAULT 1, "
                + "current_episode INT DEFAULT 0, "
                + "status VARCHAR(50) NOT NULL"
                + ");";

        String sqlMovies = "CREATE TABLE IF NOT EXISTS movies ("
                + "id INT PRIMARY KEY, "
                + "title VARCHAR(255) NOT NULL, "
                + "overview TEXT, "
                + "poster_path VARCHAR(255), "
                + "release_date VARCHAR(20),"
                + "status VARCHAR(50) NOT NULL"
                + ");";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sqlSeries);
            stmt.execute(sqlMovies);
            System.out.println("Base de datos y tablas verificadas/creadas con éxito en MariaDB.");

        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    public void saveSeries(Serie serie) {

    }

    public List<Serie> getAllSeries() {
        return null;
    }
}
