import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class FileDownloader {

    public static void saveTextToFile(String text, String path) {
        try {
            FileWriter fw = new FileWriter(path);
            fw.write(text);
            fw.close();
        } catch (Exception ignored) {}
    }

    public static DownloadReport.ReportInfo downloadBinary(String urlStr, String folder) {
        long start = System.currentTimeMillis();
        long bytes = 0;

        try {
            URL url = new URL(urlStr);
            String fileName = Utils.extractFileName(urlStr);
            String filePath = folder + "/" + fileName;

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            InputStream in = conn.getInputStream();
            FileOutputStream out = new FileOutputStream(filePath);

            byte[] buffer = new byte[4096];
            int read;

            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
                bytes += read;
            }

            in.close();
            out.close();

            long end = System.currentTimeMillis();
            long kb = bytes / 1024;

            return new DownloadReport.ReportInfo(kb, end - start);

        } catch (Exception e) {
            return null;
        }
    }
}
