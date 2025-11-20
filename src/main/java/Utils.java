import java.io.File;
import java.net.URL;

public class Utils {

    public static void createFolder(String name) {
        File f = new File(name);
        if (!f.exists()) f.mkdir();
    }

    public static String getDomainName(String url) {
        try {
            URL u = new URL(url);
            return u.getHost();
        } catch (Exception e) {
            return "website";
        }
    }

    public static String extractFileName(String url) {
        if (url.endsWith("/")) return "index.html";
        return url.substring(url.lastIndexOf("/") + 1);
    }

    public static String getBaseUrl(String url) {
        try {
            URL u = new URL(url);
            return u.getProtocol() + "://" + u.getHost();
        } catch (Exception e) {
            return "";
        }
    }

    public static String resolveUrl(String base, String relative) {
        try {
            URL baseUrl = new URL(base);
            URL resolved = new URL(baseUrl, relative);
            return resolved.toString();
        } catch (Exception e) {
            return relative;
        }
    }
}
