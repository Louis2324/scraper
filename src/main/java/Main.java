import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter website URL: ");
        String inputUrl = scanner.nextLine().trim();

        if (!UrlValidator.isValid(inputUrl)) {
            System.out.println("Invalid URL! Exiting...");
            return;
        }

        long startTime = System.currentTimeMillis();

        Database db = new Database();
        db.initialize();

        String domainName = Utils.getDomainName(inputUrl);
        String saveFolder = domainName;
        Utils.createFolder(saveFolder);

        System.out.println("Downloading homepage...");

        String homepageHtml = HtmlFetcher.fetch(inputUrl);
        if (homepageHtml == null) {
            System.out.println("Could not fetch homepage. Exiting...");
            return;
        }

        String homePath = saveFolder + "/index.html";
        FileDownloader.saveTextToFile(homepageHtml, homePath);

        int websiteId = db.insertWebsite(domainName, startTime);

        List<String> links = LinkExtractor.extractLinks(homepageHtml, inputUrl);
        System.out.println("Found " + links.size() + " links");

        long totalKB = 0;

        for (String link : links) {
            System.out.println("\nDownloading: " + link);

            DownloadReport.ReportInfo info =
                    FileDownloader.downloadBinary(link, saveFolder);

            if (info != null) {
                db.insertLink(link, websiteId, info.elapsedTime, info.kilobytes);
                totalKB += info.kilobytes;

                System.out.println(" -> downloaded " + info.kilobytes + " KB in "
                        + Utils.formatDuration(info.elapsedTime));
            }
        }

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;

        db.updateWebsite(websiteId, endTime, totalKB);

        System.out.println("\n=== DOWNLOAD COMPLETE ===");
        System.out.println("Website: " + domainName);
        System.out.println("Total KB downloaded: " + totalKB);
        System.out.println("Total time: " + Utils.formatDuration(totalTime));
    }
}
