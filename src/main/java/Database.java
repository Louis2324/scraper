import java.sql.*;

public class Database {

    private static final String URL = "jdbc:postgresql://localhost:5432/scrapper_db";
    private static final String USER = "scrapper_user";
    private static final String PASSWORD = "strongpassword";

    public Database() {
        try {
            Class.forName("org.postgresql.Driver"); // <-- forces driver registration
            initialize();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("PostgreSQL Driver not found!", e);
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw e;
        }
    }



    private Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public void initialize() {
        try (Connection conn = connect(); Statement st = conn.createStatement()) {
            st.execute("""
                CREATE TABLE IF NOT EXISTS website (
                    id SERIAL PRIMARY KEY,
                    name TEXT,
                    start_time BIGINT,
                    end_time BIGINT,
                    elapsed_time BIGINT,
                    total_kilobytes DOUBLE PRECISION
                )
            """);

            st.execute("""
                CREATE TABLE IF NOT EXISTS link (
                    id SERIAL PRIMARY KEY,
                    url TEXT,
                    website_id INTEGER REFERENCES website(id),
                    elapsed_time BIGINT,
                    kilobytes DOUBLE PRECISION
                )
            """);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int insertWebsite(String name, long startTime) {
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO website(name, start_time) VALUES (?, ?) RETURNING id"
             )) {

            ps.setString(1, name);
            ps.setLong(2, startTime);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }

    public long getStartTime(int id) {
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT start_time FROM website WHERE id=?"
             )) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getLong(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    public void updateWebsite(int id, long endTime, double totalKB) {
        long elapsed = endTime - getStartTime(id);

        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(
                     "UPDATE website SET end_time=?, elapsed_time=?, total_kilobytes=? WHERE id=?"
             )) {

            ps.setLong(1, endTime);
            ps.setLong(2, elapsed);
            ps.setDouble(3, totalKB);
            ps.setInt(4, id);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertLink(String url, int websiteId, long elapsed, double kb) {
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO link(url, website_id, elapsed_time, kilobytes) VALUES (?, ?, ?, ?)"
             )) {

            ps.setString(1, url);
            ps.setInt(2, websiteId);
            ps.setLong(3, elapsed);
            ps.setDouble(4, kb);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
