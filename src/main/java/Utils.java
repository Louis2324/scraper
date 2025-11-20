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

    public static String formatDuration(long ms) {
        if (ms < 1000) {
            return ms + " ms";
        }

        long seconds = ms / 1000;
        long minutes = seconds / 60;
        long hours   = minutes / 60;

        seconds %= 60;
        minutes %= 60;

        StringBuilder sb = new StringBuilder();

        if (hours > 0) {
            sb.append(hours).append(" hr");
            if (hours > 1) sb.append("s");
            if (minutes > 0 || seconds > 0) sb.append(" ");
        }

        if (minutes > 0) {
            sb.append(minutes).append(" min");
            if (minutes > 1) sb.append("s");
            if (seconds > 0) sb.append(" ");
        }

        if (seconds > 0) {
            sb.append(seconds).append(" sec");
            if (seconds > 1) sb.append("s");
        }

        return sb.toString();
    }

}
