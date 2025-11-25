import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter website URL: ");
        String url = sc.nextLine();

        if (!UrlValidator.isValid(url)) {
            System.out.println("Invalid URL. Exiting.");
            return;
        }

        String folderName = Utils.getDomainName(url);
        Utils.createFolder(folderName);

        Database db = new Database();
        long startTime = System.currentTimeMillis();
        int siteId = db.insertWebsite(folderName, startTime);

        // Download home page
        String html = HtmlFetcher.fetch(url);
        if (html == null) {
            System.out.println("Failed to fetch home page.");
            return;
        }
        String homeFileName = Utils.extractFileName(url);
        FileDownloader.saveTextToFile(html, folderName + "/" + homeFileName);

        // Extract links
        List<String> links = LinkExtractor.extractLinks(html, url);
        System.out.println("Extracted ("+ links.size() +") Links:");
        for (String link : links) {
            System.out.println(link);
            DownloadReport.ReportInfo info = FileDownloader.downloadBinary(link, folderName);
            if (info != null) {
                db.insertLink(link, siteId, info.elapsedTime, info.kilobytes);
                System.out.println("Downloaded " + link + " (" + info.kilobytes + " KB in " + info.elapsedTime + " ms)");
            } else {
                System.out.println("Failed to download " + link);
            }
        }

        long endTime = System.currentTimeMillis();
        double totalKB = links.stream()
                .mapToDouble(l -> {
                    DownloadReport.ReportInfo info = FileDownloader.downloadBinary(l, folderName);
                    return info != null ? info.kilobytes : 0;
                }).sum();

        db.updateWebsite(siteId, endTime, totalKB);
        System.out.println("Website download completed: " + folderName);
        System.out.println("Time: "+ Utils.formatDuration(endTime - startTime) + "   Total KB: " + totalKB);
    }
}
