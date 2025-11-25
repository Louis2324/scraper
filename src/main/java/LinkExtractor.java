import java.util.*;
import java.util.regex.*;

public class LinkExtractor {

    private static final Pattern LINK_PATTERN = Pattern.compile(
            "<a[^>]*?href\\s*=\\s*['\"]([^'\"]+)['\"]",
            Pattern.CASE_INSENSITIVE
    );

    public static List<String> extractLinks(String html, String baseUrl) {
        List<String> links = new ArrayList<>();
        Matcher matcher = LINK_PATTERN.matcher(html);

        String baseDomain = Utils.getBaseUrl(baseUrl);

        while (matcher.find()) {
            String raw = matcher.group(1);
            if (raw.startsWith("javascript:") || raw.startsWith("mailto:"))
                continue;
            String absolute = Utils.resolveUrl(baseUrl, raw);
            links.add(absolute);
        }

        return links;
    }
}
