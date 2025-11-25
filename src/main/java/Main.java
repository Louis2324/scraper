public class Main {
    public static void main(String[] args) {
        Database db = new Database();

        int siteId = db.insertWebsite("example.com", System.currentTimeMillis());
        db.insertLink("https://example.com/page1", siteId, 1500, 32.5);
        db.updateWebsite(siteId, System.currentTimeMillis(), 32.5);

        System.out.println("Done.");
    }
}
