package mx.uv.mediatracking.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class DatabaseService {

    public static Connection getConnection() throws SQLException {
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


    public void saveMedia(Media media) throws SQLException {
        if (media instanceof Movie) {
            saveMovie((Movie) media);
        } else if (media instanceof Serie) {
            saveSerie((Serie) media);
        }
    }

    private void saveMovie(Movie movie) throws SQLException {
        String sql = "INSERT INTO movies (id, title, overview, poster_path, status) VALUES (?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE title=title;";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, movie.getId());
            pstmt.setString(2, movie.getTitle());
            pstmt.setString(3, movie.getOverview());
            pstmt.setString(4, movie.getPosterPath());
            pstmt.setString(5, "Sin empezar");
            pstmt.executeUpdate();
        }
    }

    private void saveSerie(Serie serie) throws SQLException {
        String sql = "INSERT INTO series (id, title, overview, poster_path, status) VALUES (?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE title=title;";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, serie.getId());
            pstmt.setString(2, serie.getTitle());
            pstmt.setString(3, serie.getOverview());
            pstmt.setString(4, serie.getPosterPath());
            pstmt.setString(5, "Sin empezar");
            pstmt.executeUpdate();
        }
    }

    public void deleteMedia(Media media) throws SQLException {
        String table = (media instanceof Movie) ? "movies" : "series";
        String sql = "DELETE FROM " + table + " WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, media.getId());
            pstmt.executeUpdate();
        }
    }

    public List<Media> getLatestActivity(int limit) {

        String sql = "(SELECT id, title, overview, poster_path, 'movie' as type FROM movies ORDER BY id DESC LIMIT ?) " +
                "UNION ALL " +
                "(SELECT id, title, overview, poster_path, 'serie' as type FROM series ORDER BY id DESC LIMIT ?)";

        List<Media> latestMedia = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, limit);
            pstmt.setInt(2, limit);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Media media;
                if ("movie".equals(rs.getString("type"))) {
                    media = new Movie();
                } else {
                    media = new Serie();
                }
                media.setId(rs.getInt("id"));
                media.setTitle(rs.getString("title"));
                media.setOverview(rs.getString("overview"));
                media.setPosterPath(rs.getString("poster_path"));
                latestMedia.add(media);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return latestMedia.stream().limit(limit).collect(Collectors.toList());
    }

    public List<Media> getAllMediaFromLibrary() {
        String sql = "(SELECT id, title, overview, poster_path, 'movie' as type FROM movies) " +
                "UNION ALL " +
                "(SELECT id, title, overview, poster_path, 'serie' as type FROM series)";

        List<Media> libraryMedia = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Media media;
                if ("movie".equals(rs.getString("type"))) {
                    media = new Movie();
                } else {
                    media = new Serie();
                }
                media.setId(rs.getInt("id"));
                media.setTitle(rs.getString("title"));
                media.setOverview(rs.getString("overview"));
                media.setPosterPath(rs.getString("poster_path"));
                libraryMedia.add(media);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return libraryMedia;
    }

}