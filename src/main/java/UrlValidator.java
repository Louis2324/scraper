import java.util.regex.Pattern;

public class UrlValidator {
    private static final Pattern URL_PATTERN =
            Pattern.compile("^(https?://)[\\w.-]+(\\.[\\w.-]+)+.*$");

    public static boolean isValid(String url) {
        return URL_PATTERN.matcher(url).matches();
    }
}
