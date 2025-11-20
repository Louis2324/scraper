import java.sql.*;

public class Database {
    private static final String URL = "jdbc:sqlite:downloads.db";

    public void initialize() {
        try (Connection conn = DriverManager.getConnection(URL)) {
            Statement st = conn.createStatement();

            st.execute("""
                CREATE TABLE IF NOT EXISTS website (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT,
                    start_time LONG,
                    end_time LONG,
                    elapsed_time LONG,
                    total_kilobytes LONG
                )
            """);

            st.execute("""
                CREATE TABLE IF NOT EXISTS link (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    url TEXT,
                    website_id INTEGER,
                    elapsed_time LONG,
                    kilobytes LONG
                )
            """);

        } catch (Exception ignored) {}
    }

    public int insertWebsite(String name, long startTime) {
        try (Connection conn = DriverManager.getConnection(URL)) {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO website(name, start_time) VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setString(1, name);
            ps.setLong(2, startTime);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            return rs.next() ? rs.getInt(1) : -1;

        } catch (Exception e) {
            return -1;
        }
    }

    public void updateWebsite(int id, long endTime, long totalKB) {
        try (Connection conn = DriverManager.getConnection(URL)) {
            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE website SET end_time=?, elapsed_time=?, total_kilobytes=? WHERE id=?"
            );

            long elapsed = endTime - (getStartTime(id));

            ps.setLong(1, endTime);
            ps.setLong(2, elapsed);
            ps.setLong(3, totalKB);
            ps.setInt(4, id);
            ps.executeUpdate();

        } catch (Exception ignored) {}
    }

    private long getStartTime(int id) {
        try (Connection conn = DriverManager.getConnection(URL)) {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT start_time FROM website WHERE id=?"
            );
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getLong(1);

        } catch (Exception ignored) {}
        return 0;
    }

    public void insertLink(String url, int websiteId, long elapsed, long kb) {
        try (Connection conn = DriverManager.getConnection(URL)) {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO link(url, website_id, elapsed_time, kilobytes) VALUES (?, ?, ?, ?)"
            );

            ps.setString(1, url);
            ps.setInt(2, websiteId);
            ps.setLong(3, elapsed);
            ps.setLong(4, kb);
            ps.executeUpdate();

        } catch (Exception ignored) {}
    }
}
