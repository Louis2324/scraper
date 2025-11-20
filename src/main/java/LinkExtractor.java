import java.util.*;
import java.util.regex.*;

public class LinkExtractor {

    private static final Pattern LINK_PATTERN =
            Pattern.compile("<a\\s+[^>]*href\\s*=\\s*\"([^\"]+)\"", Pattern.CASE_INSENSITIVE);

    public static List<String> extractLinks(String html, String baseUrl) {
        List<String> links = new ArrayList<>();
        Matcher matcher = LINK_PATTERN.matcher(html);

        String baseDomain = Utils.getBaseUrl(baseUrl);

        while (matcher.find()) {
            String raw = matcher.group(1);

            // Skip junk
            if (raw.startsWith("javascript:") || raw.startsWith("mailto:"))
                continue;

            // Resolve relative links
            String absolute = Utils.resolveUrl(baseUrl, raw);

            // Only keep internal links
            if (absolute.startsWith(baseDomain)) {
                links.add(absolute);
            }
        }

        return links;
    }
}
