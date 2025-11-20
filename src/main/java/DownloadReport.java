public class DownloadReport {

    public static class ReportInfo {
        public long kilobytes;
        public long elapsedTime;

        public ReportInfo(long kb, long time) {
            this.kilobytes = kb;
            this.elapsedTime = time;
        }
    }
}
